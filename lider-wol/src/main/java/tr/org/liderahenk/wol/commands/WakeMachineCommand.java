package tr.org.liderahenk.wol.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private ICommandResultFactory resultFactory;
	private PluginInfoImpl pluginInfo;

	private IAgentDao agentDao;

	@Override
	public ICommandResult execute(ICommandContext context) throws Exception {

		// Find target agent
		String dn = context.getRequest().getDnList().get(0);
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		propertiesMap.put("dn", dn);
		List<? extends IAgent> agents = agentDao.findByProperties(IAgent.class, propertiesMap, null, 1);
		IAgent agent = agents.get(0);

		String[] ipAddresses = agent.getIpAddresses().replace(",", "").split(",");
		context.getRequest().getParameterMap().put("ipAddresses", ipAddresses);

		return resultFactory.create(CommandResultStatus.OK, new ArrayList<String>(), this);
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
		return true;
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
}
