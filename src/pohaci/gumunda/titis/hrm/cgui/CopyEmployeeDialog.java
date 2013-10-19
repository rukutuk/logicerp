/**
 * 
 */
package pohaci.gumunda.titis.hrm.cgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

/**
 * @author dark-knight
 *
 */
public class CopyEmployeeDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8487516904793645157L;
	private JPanel jContentPane = null;
	private JPanel panelEmployee = null;
	private JScrollPane scrollPane = null;
	private JTable table = null;
	private JPanel panelNorth = null;
	private JButton buttonAdd = null;
	private JButton buttonDelete = null;
	private JPanel panelSouth = null;
	private JButton buttonCopy = null;
	private JButton buttonClose = null;
	
	private Connection conn;
	private long sessionId;
	protected JFrame owner;
	/**
	 * This is the default constructor
	 */
	public CopyEmployeeDialog(JFrame owner, Connection conn, long sessionId) {
		super(owner, "Copy Employee", true);
		this.conn = conn;
		this.sessionId = sessionId;
		this.owner = owner;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(502, 327);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
			jContentPane.add(getPanelEmployee(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes panelEmployee	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelEmployee() {
		if (panelEmployee == null) {
			panelEmployee = new JPanel();
			panelEmployee.setLayout(new BorderLayout());
			panelEmployee.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selected Employees to be Copied", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(51,51,51)));
			panelEmployee.add(getScrollPane(), java.awt.BorderLayout.CENTER);
			panelEmployee.add(getPanelNorth(), java.awt.BorderLayout.NORTH);
			panelEmployee.add(getPanelSouth(), java.awt.BorderLayout.SOUTH);
		}
		return panelEmployee;
	}

	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}

	/**
	 * This method initializes table	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTable() {
		if (table == null) {
			table = new JTable(getTableModel());
		}
		return table;
	}

	private TableModel getTableModel() {
		DefaultTableModel model = new DefaultTableModel() {
		    static final long serialVersionUID=1; 
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.addColumn("Employee");
		return model;
	}

	/**
	 * This method initializes panelNorth	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelNorth() {
		if (panelNorth == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			panelNorth = new JPanel();
			panelNorth.setLayout(flowLayout);
			panelNorth.add(getButtonAdd(), null);
			panelNorth.add(getButtonDelete(), null);
		}
		return panelNorth;
	}

	/**
	 * This method initializes buttonAdd	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonAdd() {
		if (buttonAdd == null) {
			buttonAdd = new JButton();
			buttonAdd.setText("Add");
			buttonAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onButtonAdd(e);
				}
			});
		}
		return buttonAdd;
	}

	protected void onButtonAdd(ActionEvent e) {
		SearchEmployeeDetailDlg dlg = new SearchEmployeeDetailDlg(GumundaMainFrame.getMainFrame(),
				this.conn, this.sessionId, true);
		
		dlg.setVisible(true);
		if(dlg.getResponse()==JOptionPane.OK_OPTION){
			Employee[] employees = dlg.getEmployee();
			for(int i=0; i<employees.length; i++){
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				if(!isAlreadyAdded(employees[i]))
					model.addRow(new Object[] { employees[i] });
			}
		}
	}

	private boolean isAlreadyAdded(Employee employee) {
		for(int i=0; i<table.getRowCount(); i++){
			Employee emp = (Employee) table.getValueAt(i, 0);
			if(emp.getIndex()==employee.getIndex())
				return true;
		}
		return false;
	}

	/**
	 * This method initializes buttonDelete	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonDelete() {
		if (buttonDelete == null) {
			buttonDelete = new JButton();
			buttonDelete.setText("Delete");
			buttonDelete.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onButtonDelete(e);
				}
			});
		}
		return buttonDelete;
	}

	protected void onButtonDelete(ActionEvent e) {
		int rows[] = table.getSelectedRows();
		for(int i=rows.length-1; i>=0; i--){
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.removeRow(rows[i]);
		}
	}

	/**
	 * This method initializes panelSouth	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelSouth() {
		if (panelSouth == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.RIGHT);
			panelSouth = new JPanel();
			panelSouth.setLayout(flowLayout1);
			panelSouth.add(getButtonCopy(), null);
			panelSouth.add(getButtonClose(), null);
		}
		return panelSouth;
	}

	/**
	 * This method initializes buttonCopy	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonCopy() {
		if (buttonCopy == null) {
			buttonCopy = new JButton();
			buttonCopy.setText("Copy");
			buttonCopy.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onButtonCopy(e);
				}
			});
		}
		return buttonCopy;
	}

	protected void onButtonCopy(ActionEvent e) {
		// dikopi
		int response = 
			JOptionPane.showConfirmDialog(this, "Are you sure to copy these employees?", "Confirmation", JOptionPane.YES_NO_OPTION);
		if(response==JOptionPane.YES_OPTION){
			copyEmployee(getEmployees());
		}
	}

	private void copyEmployee(Employee[] employees) {
		ArrayList errorList = new ArrayList();
		for(int i=0; i<employees.length; i++){
			Employee employee = employees[i];
			
			try {
				employee.setQualification(getQualification(employee));
				employee.setEmployment(getEmployment(employee));
				employee.setEducation(getEducation(employee));
				employee.setCertification(getCertification(employee));
				employee.setFamily(getFamily(employee));
				employee.setAccount(getAccount(employee));
				
				insertEmployeeData(employee);
			} catch (Exception e) {
				e.printStackTrace();
				errorList.add(employee);
			}
		}
		if(errorList.size()>0){
			StringBuffer result = new StringBuffer("There was an error while copying these employees:");
			Iterator iterator = errorList.iterator();
			while(iterator.hasNext()){
				String o = iterator.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this, result);
		}else{
			JOptionPane.showMessageDialog(this, "Successfully copy some employees");
		}
	}

	private Qualification[] getQualification(Employee employee) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(this.conn);
		Qualification[] qualifications = logic.getEmployeeQualification(
				this.sessionId, IDBConstants.MODUL_MASTER_DATA, employee
						.getIndex());
		return qualifications;
	}

	private EmployeeAccount[] getAccount(Employee employee) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(this.conn);
		EmployeeAccount[] accounts = logic.getEmployeeAccount(this.sessionId,
				IDBConstants.MODUL_MASTER_DATA, employee.getIndex());
		return accounts;
	}

	private EmployeeFamily[] getFamily(Employee employee) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(this.conn);
		EmployeeFamily[] families = logic.getEmployeeFamily(this.sessionId,
				IDBConstants.MODUL_MASTER_DATA, employee.getIndex());
		return families;
	}

	private Certification[] getCertification(Employee employee) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(this.conn);
		Certification[] certificates = logic.getEmployeeCertification(
				this.sessionId, IDBConstants.MODUL_MASTER_DATA, employee
						.getIndex());
		return certificates;
	}

	private EmployeeEducation[] getEducation(Employee employee) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(this.conn);
		EmployeeEducation[] educations = logic.getEmployeeEducation(
				this.sessionId, IDBConstants.MODUL_MASTER_DATA, employee
						.getIndex());
		return educations;
	}

	private Employment[] getEmployment(Employee employee) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(this.conn);
		Employment[] employments = logic.getEmployeeEmployment(this.sessionId,
				IDBConstants.MODUL_MASTER_DATA, employee.getIndex());
		return employments;
	}

	private void insertEmployeeData(Employee employee) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(this.conn);
		logic.createEmployee(this.sessionId,
				IDBConstants.MODUL_MASTER_DATA, employee);
	}

	private Employee[] getEmployees() {
		Vector result = new Vector();
		for(int i=0; i<table.getRowCount(); i++){
			result.addElement(table.getValueAt(i, 0));
		}
		
		Employee[] employees = new Employee[result.size()];
		result.copyInto(employees);

		return employees;
	}

	/**
	 * This method initializes buttonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getButtonClose() {
		if (buttonClose == null) {
			buttonClose = new JButton();
			buttonClose.setText("Close");
			buttonClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onButtonClose(e);
				}
			});
		}
		return buttonClose;
	}

	protected void onButtonClose(ActionEvent e) {
		setVisible(false);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
