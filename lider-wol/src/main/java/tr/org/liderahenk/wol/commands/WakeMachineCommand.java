package tr.org.liderahenk.wol.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.lider.core.api.persistence.dao.IAgentDao;
import tr.org.liderahenk.lider.core.api.persistence.entities.IAgent;
import tr.org.liderahenk.lider.core.api.plugin.ICommand;
import tr.org.liderahenk.lider.core.api.service.ICommandContext;
import tr.org.liderahenk.lider.core.api.service.ICommandResult;
import tr.org.liderahenk.lider.core.api.service.ICommandResultFactory;
import tr.org.liderahenk.lider.core.api.service.enums.CommandResultStatus;
import tr.org.liderahenk.wol.plugininfo.PluginInfoImpl;

/**
 * 
 * @author <a href="mailto:mine.dogan@agem.com.tr">Mine Dogan</a>
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class WakeMachineCommand implements ICommand {

	private Logger logger = LoggerFactory.getLogger(WakeMachineCommand.class);

	private ICommandResultFactory resultFactory;
	private PluginInfoImpl pluginInfo;

	private IAgentDao agentDao;

	private String liderPassword;

	@Override
	public ICommandResult execute(ICommandContext context) throws Exception {

		// Find target agent
		String dn = context.getRequest().getDnList().get(0);
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		propertiesMap.put("dn", dn);
		List<? extends IAgent> agents = agentDao.findByProperties(IAgent.class, propertiesMap, null, 1);
		IAgent agent = agents.get(0);

		String[] macAddresses = agent.getMacAddresses().replace(",", "").split(",");
		String[] ipAddresses = agent.getIpAddresses().replace(",", "").split(",");

		// We do not know which one of the MAC addresses resides in the same
		// network with Lider, so send 'magic packet' to all MAC addresses to
		// wake the machine up.
		for (int i = 0; i < macAddresses.length; i++) {
			try {
				// TODO IMPROVEMENT: do not pass password using echo!
				String command = "wakeonlan " + macAddresses[i];
				Process process = Runtime.getRuntime().exec(command);

				// Read command output
				String line, output = "", outputErr = "";
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((line = stdInput.readLine()) != null) {
					output += line;
				}
				BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				while ((line = stdErr.readLine()) != null) {
					outputErr += line;
				}

				int exitValue = process.waitFor();
				if (exitValue != 0) {
					logger.error("Failed to execute command. Exit value: {}, Output: {}, Error: {}",
							new Object[] { exitValue, output, outputErr });
				} else {
					logger.info("Executed command. Output: {}", output);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		// Finally, check if the machine is up and accessible
		boolean accessible = checkMachine(ipAddresses);
		return resultFactory.create(accessible ? CommandResultStatus.OK : CommandResultStatus.ERROR,
				new ArrayList<String>(), this);
	}

	/**
	 * Check whether specified IP addresses is accessible or not by using ping.
	 * 
	 * @param ipAddresses
	 * @return
	 */
	private boolean checkMachine(String[] ipAddresses) {

		boolean accessible = false;

		// Wait a little while for machine to start
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

		for (String ipAddress : ipAddresses) {
			try {
				// 'success' is returned if the ping succeeds, 'fail' otherwise.
				String command = "ping -c1 " + ipAddress + " > /dev/null 2>&1 && echo \"success\" || echo \"fail\"";
				Process process = Runtime.getRuntime().exec(command);

				// Read command output
				String line, output = "", outputErr = "";
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((line = stdInput.readLine()) != null) {
					output += line;
				}
				BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				while ((line = stdErr.readLine()) != null) {
					outputErr += line;
				}

				int exitValue = process.waitFor();
				if (exitValue != 0) {
					logger.error("Failed to execute command. Exit value: {}, Output: {}, Error: {}",
							new Object[] { exitValue, output, outputErr });
				} else {
					accessible = output.equalsIgnoreCase("success");
					if (accessible) {
						break;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		return accessible;
	}

	@Override
	public ICommandResult validate(ICommandContext context) {
		return resultFactory.create(CommandResultStatus.OK, null, this, null);
	}

	@Override
	public String getCommandId() {
		return "WAKE-MACHINE";
	}

	@Override
	public Boolean executeOnAgent() {
		return false;
	}

	public void setResultFactory(ICommandResultFactory resultFactory) {
		this.resultFactory = resultFactory;
	}

	@Override
	public String getPluginName() {
		return pluginInfo.getPluginName();
	}

	@Override
	public String getPluginVersion() {
		return pluginInfo.getPluginVersion();
	}

	public void setPluginInfo(PluginInfoImpl pluginInfoImpl) {
		this.pluginInfo = pluginInfoImpl;
	}

	public IAgentDao getAgentDao() {
		return agentDao;
	}

	public void setAgentDao(IAgentDao agentDao) {
		this.agentDao = agentDao;
	}

	public void setLiderPassword(String liderPassword) {
		this.liderPassword = liderPassword;
	}
}
