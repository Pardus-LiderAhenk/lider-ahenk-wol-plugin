package tr.org.liderahenk.wol.handlers;

import org.eclipse.swt.widgets.Display;

import tr.org.liderahenk.liderconsole.core.handlers.SingleSelectionHandler;
import tr.org.liderahenk.wol.dialogs.ShutDownTaskDialog;

public class ShutDownTaskHandler extends SingleSelectionHandler {

	@Override
	public void executeWithDn(String dn) {
		ShutDownTaskDialog dialog = new ShutDownTaskDialog(Display.getDefault().getActiveShell(), dn);
		dialog.create();
		dialog.open();
	} 
}
