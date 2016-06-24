package tr.org.liderahenk.wol.commands;

import java.util.ArrayList;

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
public class ShutDownCommand implements ICommand {
	
	private ICommandResultFactory resultFactory;
	private PluginInfoImpl pluginInfo;

	@Override
	public ICommandResult execute(ICommandContext context) throws Exception {
		ICommandResult commandResult = resultFactory.create(CommandResultStatus.OK, new ArrayList<String>(), this);
		return commandResult;
	}

	@Override
	public ICommandResult validate(ICommandContext context) {
		return resultFactory.create(CommandResultStatus.OK, null, this, null);
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

	@Override
	public String getCommandId() {
		return "SHUT-DOWN-MACHINE";
	}

	@Override
	public Boolean executeOnAgent() {
		return true;
	}

}
