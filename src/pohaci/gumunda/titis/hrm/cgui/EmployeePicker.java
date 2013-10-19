package pohaci.gumunda.titis.hrm.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pohaci.gumunda.titis.accounting.cgui.DefaultUnit;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeePicker extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	JTextField m_employeeTextField = new JTextField();
	JButton m_browseBt = new JButton("...");
	Connection m_conn = null;
	long m_sessionid = -1;
	Employee m_employee = null;
	String jobTitle,unit,departmen;
	short m_type = 0;
	
	public static final short NUMBER = 0;
	public static final short NAME = 1;
	public static final short NUMBER_NAME = 2;
	
	public EmployeePicker(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_type = 1;		
		m_browseBt.setPreferredSize(new Dimension(22, 18));
		m_browseBt.addActionListener(this);
		m_employeeTextField.setRequestFocusEnabled(false);
		setLayout(new BorderLayout(3, 1));
		add(m_employeeTextField, BorderLayout.CENTER);
		add(m_browseBt, BorderLayout.EAST);
	}
	
	public EmployeePicker(Connection conn, long sessionid, short type){
		m_conn = conn;
		m_sessionid = sessionid;
		m_type = type;
		m_browseBt.setPreferredSize(new Dimension(22, 18));
		m_browseBt.addActionListener(this);
		m_employeeTextField.setRequestFocusEnabled(false);
		setLayout(new BorderLayout(3, 1));
		add(m_employeeTextField, BorderLayout.CENTER);
		add(m_browseBt, BorderLayout.EAST);
	}
	
	public void setEditable(boolean editable) {
		m_employeeTextField.setEnabled(editable);
		m_browseBt.setEnabled(editable);
	}
	
	public Employee getEmployee() {
		return m_employee;
	}
	
	public void setEmployee(Employee employee) {
		Employee old = m_employee;
		m_employee = employee;
		
		if (employee != null){
			if(m_type==NUMBER)
				m_employeeTextField.setText(m_employee.getEmployeeNo());
			else if(m_type==NAME)
				m_employeeTextField.setText(m_employee.getFirstName() +
						" " + m_employee.getMidleName() + " " + 
						m_employee.getLastName());
			else if(m_type==NUMBER_NAME)
				m_employeeTextField.setText(m_employee.getEmployeeNo() + " " +
						m_employee.getFirstName() +
						" " + m_employee.getMidleName() + " " + 
						m_employee.getLastName());
			else
				m_employeeTextField.setText(m_employee.getEmployeeNo());
			System.err.println("job title" + m_employee.getJobTitleName());
			//setJobTitle(m_employee.getJobTitleName());
			//setJobTitle(m_employee);
			
		}
		else
			m_employeeTextField.setText("");
		
		
		firePropertyChange("employee", old, m_employee);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_browseBt) {
			SearchEmployeeDetailDlg dlg = new SearchEmployeeDetailDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					m_conn, m_sessionid);
			dlg.setVisible(true);
			if (dlg.getResponse() == JOptionPane.OK_OPTION) {
				Employee[] employee = dlg.getEmployee();
				if(employee!=null){
					if (employee.length > 0) {
						Employee newEmp = employee[0];
						findEmployee(newEmp);
					} 
				}else{
					Employee newEmp = null;
					setEmployee(newEmp);
					setEmployee(newEmp);					
				}
			} 
		}
	}
	
	private Employment[] getEmployeeEmployment(long index) {
		Employment[] employment = null;
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			employment = logic.getEmployeeEmployment(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, index);
			
			return employment;
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		} 
		return null;
	}
	private Employment getMaxEmployment(Employment[] employ) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		Date currentday = calendar.getTime();  
		
		java.util.Date befdate = null, afterdate = null;
		int index = -1;
		
		for (int i = 0; i < employ.length; i++) {
			if (befdate == null) {
				befdate = employ[i].getTMT();
				index = i;
			}
			
			if (i + 1 < employ.length) {
				afterdate = employ[i + 1].getTMT();
				if(afterdate.after(currentday)) {
					break;
				}
				if (befdate.compareTo(afterdate) == -1) {
					befdate = afterdate;
					index = i + 1;
				}
			}
		}
		
		Employment emp = null;
		if (index != -1) {
			emp = employ[index];
		}
		return emp;
	}
	
	public void setJobTitle(String job){
		jobTitle=job;
	}
	
	public String getJobTitle(){
		return jobTitle;
	}
	public void setUnit(String job){
		unit=job;
	}
	
	public String getUnit(){
		return unit;
	}
	
	public void setDepartment(String job){
		departmen=job;
	}
	
	public String getDepartment(){
		return departmen;
	}
	
	public void findEmployee(Employee employee){
		if (employee!=null){
			Employment[] employs = getEmployeeEmployment(employee.getIndex());
			Employment  employment = getMaxEmployment(employs);
			if (employment!=null) {
				employment.getUnit().getDescription();
				employment.getDepartment().getName();
				setUnit(employment.getUnit().getDescription());
				setDepartment(employment.getDepartment().getName());
				setJobTitle(employment.getJobTitle().getName());
			}
			else{
				setJobTitle(null);
				setUnit(null);
				setDepartment(null);
			}
			setEmployee(employee);
		}
		else{
			setEmployee(null);
			setJobTitle("");
			setUnit("");
			setDepartment("");
		}
	}
	
	public Unit findUnitEmployee(Employee employee){
		if (employee!=null){
			Employment[] employs = getEmployeeEmployment(employee.getIndex());
			Employment  employment = getMaxEmployment(employs);
			if (employment!=null) {
				return employment.getUnit();
			}
			else				
				return setDefaultUnit();			
		}
		else
			return null;
	}
	
	public Unit setDefaultUnit(){
		Unit unit = DefaultUnit.createDefaultUnit(m_conn, m_sessionid);
		return unit;
		
		/*AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		try {
			DefaultUnit[] dUnit = logic.getAllDefaultUnit(m_sessionid, pohaci.gumunda.titis.accounting.dbapi.IDBConstants.MODUL_ACCOUNTING);
			if(dUnit.length !=0){
				Unit unit = logic.getUnitByDescription(dUnit[0].getUnit());
				return unit;				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
}
