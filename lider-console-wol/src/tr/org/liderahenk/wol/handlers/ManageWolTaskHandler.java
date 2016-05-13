package tr.org.liderahenk.wol.handlers;

import org.eclipse.swt.widgets.Display;

import tr.org.liderahenk.liderconsole.core.handlers.SingleSelectionHandler;
import tr.org.liderahenk.wol.dialogs.ManageWolTaskDialog;

public class ManageWolTaskHandler extends SingleSelectionHandler {
	
	@Override
	public void executeWithDn(String dn) {
		ManageWolTaskDialog dialog = new ManageWolTaskDialog(Display.getDefault().getActiveShell(), dn);
		dialog.create();
		dialog.open();
	}
}
