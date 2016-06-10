package tr.org.liderahenk.wol.dialogs;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.dialogs.DefaultTaskDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.wol.constants.WolConstants;
import tr.org.liderahenk.wol.i18n.Messages;

/**
 * 
 * @author <a href="mailto:mine.dogan@agem.com.tr">Mine Dogan</a>
 *
 */
public class ManageWolTaskDialog extends DefaultTaskDialog {
	
	private static final Logger logger = LoggerFactory.getLogger(ManageWolTaskDialog.class);
	
	public ManageWolTaskDialog(Shell parentShell, String dn) {
		super(parentShell, dn);
	}

	@Override
	public String createTitle() {
		return Messages.getString("ENABLE_WOL");
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
		return "MANAGE-WOL";
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
