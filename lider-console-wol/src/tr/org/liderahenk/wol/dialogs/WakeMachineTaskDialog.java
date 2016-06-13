package tr.org.liderahenk.wol.dialogs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.dialogs.DefaultTaskDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.wol.constants.WolConstants;
import tr.org.liderahenk.wol.i18n.Messages;

/**
 * 
 * @author <a href="mailto:mine.dogan@agem.com.tr">Mine Dogan</a>
 *
 */
public class WakeMachineTaskDialog extends DefaultTaskDialog {
	
	private Text txtMacAddress;

	public WakeMachineTaskDialog(Shell parentShell, String dn) {
		super(parentShell, dn);
	}

	@Override
	public String createTitle() {
		return Messages.getString("WAKE_MACHINE");
	}

	@Override
	public Control createTaskDialogArea(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblMacAddress = new Label(composite, SWT.NONE);
		lblMacAddress.setText(Messages.getString("MAC_ADDRESS"));
		
		txtMacAddress = new Text(composite, SWT.BORDER);
		
		return null;
	}

	@Override
	public void validateBeforeExecution() throws ValidationException {
		
		if (txtMacAddress.getText() == null || txtMacAddress.getText().isEmpty()) {
            throw new ValidationException(Messages.getString("MAC_ADDRESS_CANNOT_BE_EMPTY"));
        }
		
	}

	@Override
	public Map<String, Object> getParameterMap() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(WolConstants.PARAMETERS.MAC_ADDRESS, txtMacAddress.getText());
		return parameterMap;
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
