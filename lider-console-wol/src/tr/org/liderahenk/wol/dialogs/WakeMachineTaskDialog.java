package tr.org.liderahenk.wol.dialogs;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import tr.org.liderahenk.liderconsole.core.dialogs.DefaultTaskDialog;
import tr.org.liderahenk.wol.constants.WolConstants;
import tr.org.liderahenk.wol.i18n.Messages;

/**
 * 
 * @author <a href="mailto:mine.dogan@agem.com.tr">Mine Dogan</a>
 *
 */
public class WakeMachineTaskDialog extends DefaultTaskDialog {

	public WakeMachineTaskDialog(Shell parentShell, String dn) {
		super(parentShell, dn);
	}

	@Override
	public String createTitle() {
		return Messages.getString("WAKE_MACHINE");
	}

	@Override
	public Control createTaskDialogArea(Composite parent) {
		return null;
	}

	@Override
	public boolean validateBeforeExecution() {
		return true;
	}

	@Override
	public Map<String, Object> getParameterMap() {
		return null;
	}

	@Override
	public String getCommandId() {
		return "WAKE-MACHINE";
	}

	@Override
	public String getPluginName() {
		return WolConstants.PLUGIN_NAME;
	}

	@Override
	public String getPluginVersion() {
		return WolConstants.PLUGIN_VERSION;
	}

}
