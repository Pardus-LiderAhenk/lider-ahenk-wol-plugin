package tr.org.liderahenk.wol.dialogs;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import tr.org.liderahenk.liderconsole.core.dialogs.DefaultTaskDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.wol.constants.WolConstants;
import tr.org.liderahenk.wol.i18n.Messages;


/**
 * 
 * @author <a href="mailto:mine.dogan@agem.com.tr">Mine Dogan</a>
 *
 */
public class ShutDownTaskDialog extends DefaultTaskDialog {
	
	public ShutDownTaskDialog(Shell parentShell, String dn) {
		super(parentShell, dn);
	}

	@Override
	public String createTitle() {
		return Messages.getString("SHUT_DOWN_MACHINE");
	}

	@Override
	public Control createTaskDialogArea(Composite parent) {
		return null;
	}

	@Override
	public void validateBeforeExecution() throws ValidationException {
	}

	@Override
	public Map<String, Object> getParameterMap() {
		return null;
	}

	@Override
	public String getCommandId() {
		return "SHUT-DOWN-MACHINE";
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
