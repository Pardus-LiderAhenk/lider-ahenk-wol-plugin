package tr.org.liderahenk.wol.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
	private Text txtIpAddress;
	
	private Button btnMacAddress;
	private Button btnIpAddress;
	private Button btnPort;
	private Button btnRemoveItem;
	
	private TableViewer tblMachines;
	private TableItem tblItem;
	
	private List<String> macAddressList = new ArrayList<String>();
	private List<String> ipAddressList = new ArrayList<String>();
	private List<String> portList = new ArrayList<String>();
	
	private static final Pattern MAC_ADDRESS_REGEX = Pattern.compile(
			"^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
	
	private static final Pattern IP_ADDRESS_REGEX = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	
	private static final Pattern PORT_REGEX = Pattern.compile(
			"^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
	
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
		composite.setLayout(new GridLayout(3, false));
		
		Label lblMacAddresses = new Label(composite, SWT.NONE);
		lblMacAddresses.setText(Messages.getString("MAC_ADDRESS"));
		
		txtMacAddress = new Text(composite, SWT.BORDER);
		txtMacAddress.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Device device = Display.getCurrent();
				Color black = new Color(device, 0, 0, 0);
				txtMacAddress.setForeground(black);
				txtMacAddress.redraw();
			}
		});
		
		btnMacAddress = new Button(composite, SWT.PUSH);
		btnMacAddress.setText(Messages.getString("ADD"));
		btnMacAddress.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Matcher matcher = MAC_ADDRESS_REGEX .matcher(txtMacAddress.getText());
				if(!(matcher.find())) {
					Device device = Display.getCurrent();
					Color red = new Color(device, 255, 0, 0);
					txtMacAddress.setForeground(red);
					txtMacAddress.redraw();
				}
				else {
					tblItem = new TableItem(tblMachines.getTable(), SWT.NONE);
					tblItem.setText(0, txtMacAddress.getText());
					macAddressList.add(txtMacAddress.getText());
					ipAddressList.add("");
					portList.add("");
					txtMacAddress.setText("");
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		Label lblIpAddress = new Label(composite, SWT.NONE);
		lblIpAddress.setText(Messages.getString("IP_ADDRESS"));
		
		txtIpAddress = new Text(composite, SWT.BORDER);
		txtIpAddress.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Device device = Display.getCurrent();
				Color black = new Color(device, 0, 0, 0);
				txtIpAddress.setForeground(black);
				txtIpAddress.redraw();
			}
		});
		
		btnIpAddress = new Button(composite, SWT.PUSH);
		btnIpAddress.setText(Messages.getString("ADD"));
		btnIpAddress.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Matcher matcher = IP_ADDRESS_REGEX .matcher(txtIpAddress.getText());
				if(!(matcher.find())) {
					Device device = Display.getCurrent();
					Color red = new Color(device, 255, 0, 0);
					txtIpAddress.setForeground(red);
					txtIpAddress.redraw();
				}
				else {
					tblItem.setText(1, txtIpAddress.getText());
					ipAddressList.remove(ipAddressList.size()-1);
					ipAddressList.add(txtIpAddress.getText());
					txtIpAddress.setText("");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});
		
		Label lblPorts = new Label(composite, SWT.NONE);
		lblPorts.setText(Messages.getString("PORT"));
		
		txtPorts = new Text(composite, SWT.BORDER);
		txtPorts.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Device device = Display.getCurrent();
				Color black = new Color(device, 0, 0, 0);
				txtPorts.setForeground(black);
				txtPorts.redraw();
			}
		});
		
		btnPort = new Button(composite, SWT.PUSH);
		btnPort.setText(Messages.getString("ADD"));
		btnPort.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Matcher matcher = PORT_REGEX .matcher(txtPorts.getText());
				if(!(matcher.find())) {
					Device device = Display.getCurrent();
					Color red = new Color(device, 255, 0, 0);
					txtPorts.setForeground(red);
					txtPorts.redraw();
				}
				else {
					tblItem.setText(2, txtPorts.getText());
					portList.remove(portList.size()-1);
					portList.add(txtPorts.getText());
					txtPorts.setText("");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});
		
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
	}

	@Override
	public void validateBeforeExecution() throws ValidationException {
		
		if(macAddressList == null || macAddressList.isEmpty()) {
			throw new ValidationException(Messages.getString("AT_LEAST_ONE_ITEM"));
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
		if(!(ipAddressList.isEmpty())) {
			parameterMap.put(WolConstants.PARAMETERS.IP_ADDRESS, ipAddressList);
		}
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
