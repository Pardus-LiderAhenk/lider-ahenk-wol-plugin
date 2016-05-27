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
		
		ICommandResult commandResult = null;
		
		String dn = context.getRequest().getDnList().get(0);
        Map<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("dn", dn);
        List<? extends IAgent> agents = agentDao.findByProperties(IAgent.class, propertiesMap, null, 1);
        IAgent agent = agents.get(0);
        String[] macAddresses = agent.getMacAddresses().split(",");
        String[] ipAddresses = agent.getIpAddresses().split(",");
        
        for (int i = 0; i < macAddresses.length; i++) {
			String macAddress = macAddresses[i];
			macAddress = macAddress.replace("'", "");
			String ipAddress = ipAddresses[i];
			ipAddress = ipAddress.replace("'", "");
			
			try {
            	String command = "echo " + liderPassword + " | sudo -S wakeonlan " + macAddress;
                Process process = Runtime.getRuntime().exec(command);
                int exitValue = process.waitFor();
                if (exitValue != 0) {
                	logger.error("Failed to execute command: " + command);
                }
                Thread.sleep(1000);
                
                String pingCommand = "ping -c1 -W1 " + ipAddress;
                process = Runtime.getRuntime().exec(pingCommand);
                exitValue = process.waitFor();
                if (exitValue != 0) {
                	logger.error("Failed to execute command: " + pingCommand);
                }
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                
                // read the output from the command
                String commandOutput = null;
                while ((commandOutput = stdInput.readLine()) != null) {
                    if(commandOutput.contains("100% packet loss")) {
                    	commandResult = resultFactory.create(CommandResultStatus.ERROR, new ArrayList<String>(), this);
                    }
                    else {
                    	commandResult = resultFactory.create(CommandResultStatus.OK, new ArrayList<String>(), this);
                    }
                }
                
                
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
            }
		}
		
		return commandResult;
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
