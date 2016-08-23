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
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;
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
		
		GridData gdMac =  new GridData();
		gdMac.horizontalAlignment = SWT.FILL;
		gdMac.verticalAlignment = SWT.TOP;
		gdMac.grabExcessHorizontalSpace = true;
		
		Composite compMac = new Composite(composite, SWT.NONE);
		compMac.setLayout(new GridLayout(1, false));
		compMac.setLayoutData(gdMac);
		
		Label lblMac = new Label(compMac, SWT.NONE);
		lblMac.setText(Messages.getString("MAC"));
		lblMac.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		FontData data = lblMac.getFont().getFontData()[0];
		Font font = new Font(compMac.getDisplay(), new FontData(data.getName(), data
		    .getHeight(), SWT.BOLD));
		lblMac.setFont(font);
		
		Label lblMacAddresses = new Label(compMac, SWT.NONE);
		lblMacAddresses.setText(Messages.getString("MAC_ADDRESS"));
		
		txtMacAddress = new Text(compMac, SWT.BORDER);
        txtMacAddress.setLayoutData(gdMac);
        
        GridData gdControl =  new GridData();
		gdControl.horizontalAlignment = SWT.FILL;
		gdControl.verticalAlignment = SWT.TOP;
		gdControl.grabExcessHorizontalSpace = true;
        
        Composite compControl = new Composite(composite, SWT.NONE);
        compControl.setLayout(new GridLayout(1, false));
        compControl.setLayoutData(gdControl);
		
		Label lblControl = new Label(compControl, SWT.NONE);
		lblControl.setText(Messages.getString("CONTROL"));
		lblControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		data = lblControl.getFont().getFontData()[0];
		font = new Font(compControl.getDisplay(), new FontData(data.getName(), data
		    .getHeight(), SWT.BOLD));
		lblControl.setFont(font);
		
		Label lblIpAddress = new Label(compControl, SWT.NONE);
		lblIpAddress.setText(Messages.getString("IP_ADDRESS"));
		
		txtIpAddress = new Text(compControl, SWT.BORDER);
		txtIpAddress.setLayoutData(gdControl);
		
		Label lblPorts = new Label(compControl, SWT.NONE);
		lblPorts.setText(Messages.getString("PORT"));
		
		txtPorts = new Text(compControl, SWT.BORDER);
		txtPorts.setLayoutData(gdControl);
		
		Label lblTime = new Label(compControl, SWT.NONE);
		lblTime.setText(Messages.getString("CONTROL_TIME"));
		
		txtTime = new Text(compControl, SWT.BORDER);
		txtTime.setLayoutData(gdControl);
		txtTime.setText("30");
		
		btnAdd = new Button(composite, SWT.PUSH);
		btnAdd.setText(Messages.getString("ADD"));
		btnAdd.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true, 2, 2));
		btnAdd.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (txtMacAddress.getText() != null && !txtMacAddress.getText().isEmpty() &&
						!txtMacAddress.getText().replaceAll("\\s+","").isEmpty() && txtPorts.getText() != null &&
						!txtPorts.getText().isEmpty() && !txtPorts.getText().replaceAll("\\s+","").isEmpty() && 
						txtTime.getText() != null && !txtTime.getText().isEmpty() && 
						!txtTime.getText().replaceAll("\\s+","").isEmpty()) {
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
					txtTime.setText("30");
				}
				else {
					Notifier.warning(null, Messages.getString("FILL_MAC_ADDRESS_PORTS_AND_TIME"));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});
		
		Label lblMachines = new Label(composite, SWT.NONE);
		lblMachines.setText(Messages.getString("MACHINES"));
		lblMachines.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		data = lblMachines.getFont().getFontData()[0];
		font = new Font(composite.getDisplay(), new FontData(data.getName(), data
		    .getHeight(), SWT.BOLD));
		lblMachines.setFont(font);
		
		tblMachines = SWTResourceManager.createTableViewer(composite);
		GridData gridData = new GridData();
	    gridData.widthHint = 500;
	    gridData.heightHint = 300;
	    gridData.horizontalSpan = 2;
	    gridData.verticalSpan = 2;
	    tblMachines.getTable().setLayoutData(gridData);
		tblMachines.getTable().setLinesVisible(true);
		createColumns();
		
		btnRemoveItem = new Button(composite, SWT.PUSH);
		btnRemoveItem.setText(Messages.getString("REMOVE_ITEM"));
		btnRemoveItem.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true, 2, 2));
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
