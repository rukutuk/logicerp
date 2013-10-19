package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;

public class AssignPanel extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	EmployeePicker m_employeePicker;
	JTextField m_jobTextField;
	DatePicker m_datePicker;	
	
	public AssignPanel(Connection conn, long sessionid, String title) {
		m_conn = conn;
		m_sessionid = sessionid;
		setEmployee1();
		constructComponent(title);
	}
	public AssignPanel(Connection conn, long sessionid, String title,JPanel a) {
		m_conn = conn;
		m_sessionid = sessionid;
		setEmployee1();
		constructComponent(title);		
	}
	
	public AssignPanel(Connection conn, long sessionid, String title,String iou) {
		m_conn = conn;
		m_sessionid = sessionid;
		setEmployee();
		constructComponent(title);		
	}
	
	void constructComponent(String title) {			
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		Misc.setGridBagConstraints(centerPanel, new JLabel("Personnel"), gridBagConstraints,
				0, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		Misc.setGridBagConstraints(centerPanel, new JLabel(" "), gridBagConstraints,
				1, 0, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
		Misc.setGridBagConstraints(centerPanel, m_employeePicker, gridBagConstraints,
				2, 0, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);
		Misc.setGridBagConstraints(centerPanel, new JLabel("Job Title"), gridBagConstraints,
				0, 1, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		Misc.setGridBagConstraints(centerPanel, new JLabel(" "), gridBagConstraints,
				1, 1, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
		Misc.setGridBagConstraints(centerPanel, m_jobTextField, gridBagConstraints,
				2, 1, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);
		Misc.setGridBagConstraints(centerPanel, new JLabel("Date"), gridBagConstraints,
				0, 2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		Misc.setGridBagConstraints(centerPanel, new JLabel(" "), gridBagConstraints,
				1, 2, GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
		Misc.setGridBagConstraints(centerPanel, m_datePicker, gridBagConstraints,
				2, 2, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title,
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
		add(centerPanel, BorderLayout.CENTER);
	}
	private void setEmployee1() {
		m_employeePicker = new EmployeePicker(m_conn, m_sessionid,(short)1);
		m_employeePicker.addPropertyChangeListener(this);
		m_jobTextField = new JTextField();
		m_datePicker = new DatePicker();
		m_employeePicker.setPreferredSize(new Dimension(205,18));
		m_jobTextField.setPreferredSize(new Dimension(145,18));
		m_datePicker.setPreferredSize(new Dimension(145,18));
	}
	
	private void setEmployee() {
		m_employeePicker = new EmployeePicker(m_conn, m_sessionid,(short)1);
		m_employeePicker.addPropertyChangeListener(this);
		m_jobTextField = new JTextField();
		m_datePicker = new DatePicker();
		m_employeePicker.setPreferredSize(new Dimension(160,18));
		m_jobTextField.setPreferredSize(new Dimension(160,18));
		m_datePicker.setPreferredSize(new Dimension(160,18));
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		m_employeePicker.setEditable(enabled);
		m_datePicker.setEditable(enabled);
		m_jobTextField.setEnabled(enabled);
	}
	
	public void setDate(java.util.Date date) {
		m_datePicker.setDate(date);
	}
	
	public java.util.Date getDate() {
		return m_datePicker.getDate();
	}
	
	public Employee getEmployee() {
		return m_employeePicker.getEmployee();
	}
	
	public void setEmployee(Employee e) {
		m_employeePicker.setEmployee(e);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("employee")){
			m_jobTextField.setText(m_employeePicker.getJobTitle());
		}
	}
	
	public void setJobTitle(String a){
		m_jobTextField.setText(a);
	}
	
	public String getJobTitle(){
		return  m_jobTextField.getText();
	}
}