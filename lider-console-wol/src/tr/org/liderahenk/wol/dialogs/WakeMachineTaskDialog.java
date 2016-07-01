package tr.org.liderahenk.wol.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.dialogs.DefaultTaskDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.wol.constants.WolConstants;
import tr.org.liderahenk.wol.i18n.Messages;

/**
 * 
 * @author <a href="mailto:mine.dogan@agem.com.tr">Mine Dogan</a>
 *
 */
public class WakeMachineTaskDialog extends DefaultTaskDialog {
	
	private Text txtMacAddress;
	private Text txtPorts;
	private Text txtTime;
	private Text txtIpAddress;
	
	private Button btnAdd;
	private Button btnRemoveItem;
	
	private TableViewer tblMachines;
	private TableItem tblItem;
	
	private List<String> macAddressList = new ArrayList<String>();
	private List<String> ipAddressList = new ArrayList<String>();
	private List<String> portList = new ArrayList<String>();
	private List<String> timeList = new ArrayList<String>();
	
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
		
		Label lblMac = new Label(composite, SWT.NONE);
		lblMac.setText(Messages.getString("MAC"));
		lblMac.setLayoutData(new GridData(1, 1, false, false, 2, 2));
		FontData data = lblMac.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(data.getName(), data
		    .getHeight(), SWT.BOLD));
		lblMac.setFont(font);
		
		Label lblMacAddresses = new Label(composite, SWT.NONE);
		lblMacAddresses.setText(Messages.getString("MAC_ADDRESS"));
		
		txtMacAddress = new Text(composite, SWT.BORDER);
		
		Label lblControl = new Label(composite, SWT.NONE);
		lblControl.setText(Messages.getString("CONTROL"));
		lblControl.setLayoutData(new GridData(1, 1, false, false, 2, 2));
		data = lblControl.getFont().getFontData()[0];
		font = new Font(composite.getDisplay(), new FontData(data.getName(), data
		    .getHeight(), SWT.BOLD));
		lblControl.setFont(font);
		
		Label lblIpAddress = new Label(composite, SWT.NONE);
		lblIpAddress.setText(Messages.getString("IP_ADDRESS"));
		
		txtIpAddress = new Text(composite, SWT.BORDER);
		
		Label lblPorts = new Label(composite, SWT.NONE);
		lblPorts.setText(Messages.getString("PORT"));
		
		txtPorts = new Text(composite, SWT.BORDER);
		
		Label lblTime = new Label(composite, SWT.NONE);
		lblTime.setText(Messages.getString("CONTROL_TIME"));
		
		txtTime = new Text(composite, SWT.BORDER);
		
		btnAdd = new Button(composite, SWT.PUSH);
		btnAdd.setText(Messages.getString("ADD"));
		btnAdd.setLayoutData(new GridData(1, 1, false, false, 2, 2));
		btnAdd.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				tblItem = new TableItem(tblMachines.getTable(), SWT.NONE);
				tblItem.setText(0, txtMacAddress.getText());
				macAddressList.add(txtMacAddress.getText());
				tblItem.setText(1, txtIpAddress.getText());
				ipAddressList.add(txtIpAddress.getText());
				tblItem.setText(2, txtPorts.getText());
				portList.add(txtPorts.getText());
				tblItem.setText(3, txtTime.getText());
				timeList.add(txtTime.getText());
				
				txtMacAddress.setText("");
				txtIpAddress.setText("");
				txtPorts.setText("");
				txtTime.setText("");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});
		
		Label lblMachines = new Label(composite, SWT.NONE);
		lblMachines.setText(Messages.getString("MACHINES"));
		data = lblMachines.getFont().getFontData()[0];
		font = new Font(composite.getDisplay(), new FontData(data.getName(), data
		    .getHeight(), SWT.BOLD));
		lblMachines.setFont(font);
		
		tblMachines = SWTResourceManager.createTableViewer(composite);
		tblMachines.getTable().setLinesVisible(true);
		createColumns();
		
		btnRemoveItem = new Button(composite, SWT.PUSH);
		btnRemoveItem.setText(Messages.getString("REMOVE_ITEM"));
		btnRemoveItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = tblMachines.getTable().getSelectionIndex();
				tblMachines.getTable().remove(tblMachines.getTable().getSelectionIndices());
				
				macAddressList.remove(selectionIndex);
				ipAddressList.remove(selectionIndex);
				portList.remove(selectionIndex);
				timeList.remove(selectionIndex);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		return null;
	}
	
	private void createColumns() {
		SWTResourceManager.createTableViewerColumn(tblMachines, Messages.getString("MAC_COLUMN"), 120);
		SWTResourceManager.createTableViewerColumn(tblMachines, Messages.getString("IP_COLUMN"), 120);
		SWTResourceManager.createTableViewerColumn(tblMachines, Messages.getString("PORT_COLUMN"), 120);
		SWTResourceManager.createTableViewerColumn(tblMachines, Messages.getString("TIME_COLUMN"), 120);
	}

	@Override
	public void validateBeforeExecution() throws ValidationException {
		
		if(macAddressList.contains("")) {
			throw new ValidationException(Messages.getString("REMOVE_ITEM_HAS_NO_MAC_ADDRESS"));
		}
		if(portList.contains("")) {
			throw new ValidationException(Messages.getString("REMOVE_ITEM_HAS_NO_PORT"));
		}
	}

	@Override
	public Map<String, Object> getParameterMap() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(WolConstants.PARAMETERS.MAC_ADDRESS, macAddressList);
		parameterMap.put(WolConstants.PARAMETERS.PORT, portList);
		parameterMap.put(WolConstants.PARAMETERS.IP_ADDRESS, ipAddressList);
		parameterMap.put(WolConstants.PARAMETERS.TIME, timeList);
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
