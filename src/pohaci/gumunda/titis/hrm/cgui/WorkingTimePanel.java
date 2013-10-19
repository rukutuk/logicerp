package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.*;

import pohaci.gumunda.titis.accounting.entity.Unit;

import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class WorkingTimePanel extends JPanel implements ActionListener,
		PropertyChangeListener, FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EmployeePicker m_employeePicker;

	JTextField m_nameTextField, m_jobtitleTextField, m_departmentTextField,
			m_unitTextField;

	JButton m_editBt, m_saveBt, m_cancelBt;

	JCalendar m_calendar = null;

	WorkingTimeJDayChooser m_workTimeJDayChooser;

	JRadioButton m_presentRadioButton, m_presentlateRadioButton,
			m_absentRadioButton, m_fieldRadioButton, m_clearRadioButton;

	JCheckBox m_overtimeCheckBox;

	JTextField m_hourTextField;

	Connection m_conn = null;

	long m_sessionid = -1;

	// private HolidayJDayChooser m_dayChooser;
	private WorkingTimeState workingTimeState;

	private JRadioButton m_otherRadioButton;

	public WorkingTimePanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;

		try {
			new HRMSQLSAP().getDefaultWorkingDay(m_conn);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		constructComponent();

	}

	void constructComponent() {
		m_overtimeCheckBox = new JCheckBox("Overtime");
		m_presentRadioButton = new JRadioButton("Present, Not Late");
		m_presentlateRadioButton = new JRadioButton("Present, Late");
		m_absentRadioButton = new JRadioButton("Absent");
		m_fieldRadioButton = new JRadioButton("Field Visit");
		m_otherRadioButton = new JRadioButton("Other");
		m_clearRadioButton = new JRadioButton("Clear");

		setDisableRadioButton();

		m_overtimeCheckBox.addActionListener(this);
		m_presentRadioButton.addActionListener(this);
		m_presentlateRadioButton.addActionListener(this);
		m_absentRadioButton.addActionListener(this);
		m_fieldRadioButton.addActionListener(this);
		m_clearRadioButton.addActionListener(this);
		m_otherRadioButton.addActionListener(this);

		ButtonGroup bg = new ButtonGroup();
		bg.add(m_presentRadioButton);
		bg.add(m_presentlateRadioButton);
		bg.add(m_absentRadioButton);
		bg.add(m_fieldRadioButton);
		bg.add(m_otherRadioButton);
		bg.add(m_clearRadioButton);
		m_hourTextField = new JTextField(4);
		m_hourTextField.addFocusListener(this);

		m_employeePicker = new EmployeePicker(m_conn, m_sessionid);
		m_employeePicker.addPropertyChangeListener("employee", this);
		m_nameTextField = new JTextField();
		m_jobtitleTextField = new JTextField();
		m_departmentTextField = new JTextField();
		m_unitTextField = new JTextField();

		m_nameTextField.setEditable(false);
		m_jobtitleTextField.setEditable(false);
		m_departmentTextField.setEditable(false);
		m_unitTextField.setEditable(false);

		m_editBt = new JButton("Edit");
		m_editBt.addActionListener(this);
		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);

		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);

		overTimeSetting(false);

		m_workTimeJDayChooser = new WorkingTimeJDayChooser(this, m_conn,
				m_sessionid);
		m_calendar = new JCalendar(JMonthChooser.NO_SPINNER,
				m_workTimeJDayChooser);
		// m_calendar = new JCalendar();
		m_calendar.setPreferredSize(new Dimension(250, 250));

		JPanel mergePanel = new JPanel();
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JPanel employeePanel = new JPanel();
		JLabel noLabel = new JLabel("Employee No");
		JLabel nameLabel = new JLabel("Name");
		JLabel jobtitleLabel = new JLabel("Job Title");
		JLabel departmentLabel = new JLabel("Departement");
		JLabel unitLabel = new JLabel("Unit Code");

		JPanel legendPanel = new JPanel();
		JPanel leftlegendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftlegendPanel.add(legendPanel);
		JLabel legendLabel = new JLabel("Legend :");

		JPanel datePanel = new JPanel();
		JPanel leftdatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftdatePanel.add(datePanel);
		JLabel dateLabel = new JLabel("Select Date :");

		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		JPanel centerdescriptionPanel = new JPanel(new BorderLayout());
		centerdescriptionPanel.add(descriptionPanel, BorderLayout.CENTER);
		JLabel descriptionLabel = new JLabel("Set Selected Date To :");

		JPanel hourPanel = new JPanel();
		JPanel lefthourPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lefthourPanel.add(hourPanel);
		JLabel hourLabel = new JLabel("Hour # ");

		buttonPanel.add(m_editBt);
		buttonPanel.add(m_saveBt);
		buttonPanel.add(m_cancelBt);

		// construct employeePanel
		employeePanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(2, 0, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		employeePanel.add(noLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		employeePanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		employeePanel.add(m_employeePicker, gridBagConstraints);

		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		employeePanel.add(buttonPanel, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(2, 0, 1, 0);
		employeePanel.add(nameLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		employeePanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		employeePanel.add(m_nameTextField, gridBagConstraints);

		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(2, 10, 1, 0);
		employeePanel.add(departmentLabel, gridBagConstraints);

		gridBagConstraints.gridx = 4;
		gridBagConstraints.insets = new Insets(2, 0, 1, 0);
		employeePanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 5;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		employeePanel.add(m_departmentTextField, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		employeePanel.add(jobtitleLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		employeePanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		employeePanel.add(m_jobtitleTextField, gridBagConstraints);

		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(2, 10, 1, 0);
		employeePanel.add(unitLabel, gridBagConstraints);

		gridBagConstraints.gridx = 4;
		gridBagConstraints.insets = new Insets(2, 0, 1, 0);
		employeePanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 5;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		employeePanel.add(m_unitTextField, gridBagConstraints);

		// construct legendPanel
		legendPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 10, 0);
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		legendPanel.add(legendLabel, gridBagConstraints);

		int i = 0;
		for (i = 0; i < WorkingTime.m_color.length; i++) {
			gridBagConstraints.gridy = i + 1;
			gridBagConstraints.insets = new Insets(0, 0, -2, 0);
			legendPanel.add(new ColorPanel(WorkingTime.m_color[i],
					WorkingTime.m_strColor[i]), gridBagConstraints);
		}

		gridBagConstraints.gridy = i + 1;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.weightx = 1.0;
		legendPanel.add(new JPanel(), gridBagConstraints);

		// construct datePanel
		datePanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 10, 0);
		datePanel.add(dateLabel, gridBagConstraints);

		gridBagConstraints.gridy = 1;
		datePanel.add(m_calendar, gridBagConstraints);

		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.weightx = 1.0;
		datePanel.add(new JPanel(), gridBagConstraints);

		// construct descriptionPanel
		hourPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 10, 0);
		hourPanel.add(hourLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		hourPanel.add(m_hourTextField, gridBagConstraints);

		descriptionPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 10, 0);
		descriptionPanel.add(descriptionLabel, gridBagConstraints);

		gridBagConstraints.gridy = 1;
		descriptionPanel.add(m_presentRadioButton, gridBagConstraints);

		gridBagConstraints.gridy = 2;
		descriptionPanel.add(m_presentlateRadioButton, gridBagConstraints);

		gridBagConstraints.gridy = 3;
		descriptionPanel.add(m_absentRadioButton, gridBagConstraints);

		gridBagConstraints.gridy = 4;
		descriptionPanel.add(m_fieldRadioButton, gridBagConstraints);

		gridBagConstraints.gridy = 5;
		descriptionPanel.add(m_otherRadioButton, gridBagConstraints);

		gridBagConstraints.gridy = 6;
		gridBagConstraints.insets = new Insets(0, 0, 30, 0);
		descriptionPanel.add(m_clearRadioButton, gridBagConstraints);

		gridBagConstraints.gridy = 7;
		gridBagConstraints.insets = new Insets(0, 0, 10, 0);
		descriptionPanel.add(m_overtimeCheckBox, gridBagConstraints);

		gridBagConstraints.gridy = 8;
		gridBagConstraints.insets = new Insets(0, 10, 10, 0);
		descriptionPanel.add(lefthourPanel, gridBagConstraints);

		gridBagConstraints.gridy = 9;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.weightx = 1.0;
		descriptionPanel.add(new JPanel(), gridBagConstraints);

		mergePanel.setLayout(new BorderLayout(5, 5));
		mergePanel.add(leftlegendPanel, BorderLayout.WEST);
		mergePanel.add(leftdatePanel, BorderLayout.CENTER);

		setLayout(new BorderLayout(5, 5));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		add(employeePanel, BorderLayout.NORTH);
		add(mergePanel, BorderLayout.WEST);
		add(centerdescriptionPanel, BorderLayout.CENTER);
	}

	public void setEnableRadioButton() {
		m_overtimeCheckBox.setEnabled(true);
		m_presentRadioButton.setEnabled(true);
		m_presentlateRadioButton.setEnabled(true);
		m_absentRadioButton.setEnabled(true);
		m_fieldRadioButton.setEnabled(true);
		m_clearRadioButton.setEnabled(true);
		m_otherRadioButton.setEnabled(true);
	}

	public void setDisableRadioButton() {
		m_overtimeCheckBox.setEnabled(false);
		m_presentRadioButton.setEnabled(false);
		m_presentlateRadioButton.setEnabled(false);
		m_absentRadioButton.setEnabled(false);
		m_fieldRadioButton.setEnabled(false);
		m_clearRadioButton.setEnabled(false);
		m_otherRadioButton.setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("------------------  actionPerformed ");
		if (e.getSource() == m_saveBt) {
			save();
			setDisableRadioButton();
		} else if (e.getSource() == m_cancelBt) {
			cancel();
		} else if (e.getSource() == m_editBt) {
			edit();
		} else if (e.getSource() == m_overtimeCheckBox) {
			if (m_overtimeCheckBox.isSelected()) {
				// m_hourTextField.setEnabled(true);
				m_hourTextField.setEditable(true);
				m_hourTextField.setEnabled(true);
			} else {
				// m_hourTextField.setEnabled(false);
				m_hourTextField.setEditable(false);
				m_hourTextField.setEnabled(false);
				m_hourTextField.setText("");
			}

		} else if (e.getSource() == m_presentRadioButton
				&& m_presentRadioButton.isSelected()) {

			m_hourTextField.setEditable(false);
			m_overtimeCheckBox.setEnabled(true);
			m_hourTextField.setEnabled(false);
			m_hourTextField.setText("");
			m_overtimeCheckBox.setSelected(false);

			setWorkingTimeState(0, 0);
			m_workTimeJDayChooser.SetStatusSelectedDate(0);
		} else if (e.getSource() == m_presentlateRadioButton
				&& m_presentlateRadioButton.isSelected()) {

			m_hourTextField.setEditable(false);
			m_overtimeCheckBox.setEnabled(true);
			m_hourTextField.setEnabled(false);
			m_hourTextField.setText("");
			m_overtimeCheckBox.setSelected(false);

			setWorkingTimeState(1, 0);
			m_workTimeJDayChooser.SetStatusSelectedDate(1);
		} else if (e.getSource() == m_absentRadioButton
				&& m_absentRadioButton.isSelected()) {
			m_hourTextField.setEditable(false);
			m_hourTextField.setText("");
			m_overtimeCheckBox.setEnabled(false);

			m_overtimeCheckBox.setSelected(false);
			setWorkingTimeState(2, 0);
			m_workTimeJDayChooser.SetStatusSelectedDate(2);
		} else if (e.getSource() == m_fieldRadioButton
				&& m_fieldRadioButton.isSelected()) {

			m_hourTextField.setEditable(false);
			m_overtimeCheckBox.setEnabled(true);
			m_hourTextField.setText("");
			m_overtimeCheckBox.setSelected(false);

			setWorkingTimeState(3, 0);
			m_workTimeJDayChooser.SetStatusSelectedDate(3);
		} else if (e.getSource() == m_clearRadioButton
				&& m_clearRadioButton.isSelected()) {
			m_hourTextField.setEditable(false);
			m_overtimeCheckBox.setEnabled(false);
			m_hourTextField.setText("");
			m_overtimeCheckBox.setSelected(false);
			setWorkingTimeState(4, 0);
			m_workTimeJDayChooser.SetStatusSelectedDate(4);
		} else if (e.getSource() == m_otherRadioButton
				&& m_otherRadioButton.isSelected()) {
			m_hourTextField.setEditable(false);
			m_overtimeCheckBox.setEnabled(true);
			m_hourTextField.setEnabled(false);
			m_hourTextField.setText("");
			m_overtimeCheckBox.setSelected(false);

			setWorkingTimeState(5, 0);
			m_workTimeJDayChooser.SetStatusSelectedDate(5);

		}
	}

	public void overTimeSetting(boolean bool) {
		m_hourTextField.setEditable(bool);
		m_overtimeCheckBox.setEnabled(bool);
		m_overtimeCheckBox.setSelected(false);
		m_hourTextField.setText("");
		m_overtimeCheckBox.setEnabled(bool);
	}

	private void save() {
		WorkingTimeState[] arrayWorkingTimestate = null;
		try {
			arrayWorkingTimestate = m_workTimeJDayChooser.getWorkingTimeState();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			logic.createEmployeeOfficeWorkingtime(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, m_employeePicker
							.getEmployee(), arrayWorkingTimestate);

			m_workTimeJDayChooser
					.initEmployeeOfficeWorkingTime(m_employeePicker
							.getEmployee());
			// m_dayChooser.initHoliday();
			// m_dayChooser.setDay(1);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			ex.printStackTrace();
			System.out.println("ERROR EUY  di save 2");
			return;
		}

		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_calendar.enableMonthYear();
		m_cekedit = false;	
		disableOvertime(m_cekedit);
	}
	
	public void disableOvertime(boolean cekedit){
		m_overtimeCheckBox.setSelected(cekedit);
		m_hourTextField.setEditable(cekedit);		
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == m_employeePicker) {
			Employee emp = m_employeePicker.getEmployee();

			if (emp != null) {
				m_nameTextField.setText(emp.getFirstName() + " "
						+ emp.getMidleName() + " " + emp.getLastName());
				Employment[] employs = getEmployeeEmployment(emp.getIndex());
				Employment employment = getMaxEmployment(employs);
				if (employment != null) {
					m_jobtitleTextField.setText(employment.getJobTitle()
							.getName());
					Unit unit = employment.getUnit();
					m_unitTextField.setText(unit.getCode() + " "
							+ unit.getDescription());
					m_departmentTextField.setText(employment.getDepartment()
							.getName());
				}
				m_editBt.setEnabled(true);

				m_workTimeJDayChooser
						.initEmployeeOfficeWorkingTime(m_employeePicker
								.getEmployee());

				if (emp.getEmployment().length > 0) {
					m_departmentTextField.setText(emp.getEmployment()[0]
							.getDepartment().getName());
					m_jobtitleTextField.setText(emp.getEmployment()[0]
							.getJobTitle().getName());
					m_unitTextField.setText(emp.getEmployment()[0].getUnit()
							.getCode());
				}
			} else {
				// stay same as before
			}
		}
	}

	private void cancel() {

		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_hourTextField.setText("");
		m_overtimeCheckBox.setSelected(false);
		m_cancelBt.setEnabled(false);
		setDisableRadioButton();
		m_workTimeJDayChooser.initEmployeeOfficeWorkingTime(m_employeePicker
				.getEmployee());
		m_calendar.enableMonthYear();
	}

	boolean m_cekedit = false;
	private void edit() {
		m_cekedit = true;
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		setEnableRadioButton();
		m_calendar.disableMonthYear();
		if (m_workTimeJDayChooser.findWorkingTimeState(m_workTimeJDayChooser
				.getDate()) == null) {
			m_overtimeCheckBox.setEnabled(false);
			m_hourTextField.setEditable(false);
		}

	}

	public WorkingTimeState getWorkingTimeState() {

		return workingTimeState;
	}

	public void setWorkingTimeState(int astate, float overtime) {
		workingTimeState = new WorkingTimeState(
				m_calendar.dayChooser.getDate(), astate, overtime);
		System.out.println(" state = " + astate);
	}

	public void setSelectedRadioButton(int select, String value) {
		System.out.println("value = " + value);
		float valueFloat=0;
		if (value != null) {
			 valueFloat = new Float(value).floatValue();
			if (valueFloat > 0) {
				System.out
						.println("-------------------------------------BIG THAN ZERO");
			} else {
				System.out
						.println("-------------------------------------LESS THAN ZERO");
			}
		}

		boolean cekedit = false;
		switch (select) {
		case 0:
			m_presentRadioButton.setSelected(true);
			m_overtimeCheckBox.setEnabled(true);
			if (valueFloat > 0) {				
				cekedit = true;
				m_overtimeCheckBox.setSelected(true);
				System.out
						.println("-------------------------------------BIG THAN ZERO");
			}
			break;
		case 1:
			m_presentlateRadioButton.setSelected(true);
			m_overtimeCheckBox.setEnabled(true);
			if (valueFloat > 0) {
				cekedit = true;
				m_overtimeCheckBox.setSelected(true);
			}
			break;
		case 2:
			m_absentRadioButton.setSelected(true);
			m_overtimeCheckBox.setEnabled(false);
			break;
		case 3:
			m_fieldRadioButton.setSelected(true);
			m_overtimeCheckBox.setEnabled(true);
			if (valueFloat > 0) {
				cekedit = true;
				//m_overtimeCheckBox.setSelected(true);
			}
			break;
		case 5:
			m_otherRadioButton.setSelected(true);
			m_overtimeCheckBox.setEnabled(true);
			if (valueFloat > 0) {
				cekedit = true;				
			}
			break;
		default:
			m_clearRadioButton.setSelected(true);
			m_overtimeCheckBox.setEnabled(false);
			break;
		}
		if (m_cekedit){
			disableOvertime(cekedit);
		}				
		m_hourTextField.setText(value);
	}

	public Employee getEmployee() {
		return m_employeePicker.getEmployee();
	}

	private Employment[] getEmployeeEmployment(long index) {
		Employment[] employment = null;
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			employment = logic.getEmployeeEmployment(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, index);

			// getMaxEmployment(employment);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		} 
		return employment;

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

	public void clear() {
		m_hourTextField.setText("");
		m_overtimeCheckBox.setSelected(false);

	}

	public void focusGained(FocusEvent e) {
		//if (m_overtimeCheckBox)
	}

	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		System.out.println(" Hour = " + m_hourTextField.getText() + " , ");
		if (m_overtimeCheckBox.isSelected()) {
			if (m_presentRadioButton.isSelected()) {
				setWorkingTimeState(0, 0);
			} else if (m_presentlateRadioButton.isSelected()) {
				setWorkingTimeState(1, 0);
			} else if (m_absentRadioButton.isSelected()) {
				setWorkingTimeState(2, 0);
			} else if (m_fieldRadioButton.isSelected()) {
				setWorkingTimeState(3, 0);
			} else if (m_otherRadioButton.isSelected()) {
				setWorkingTimeState(5, 0);
			} else if (m_clearRadioButton.isSelected()) {
				setWorkingTimeState(4, 0);
			} 
			if (workingTimeState != null) {
				float overtimevalue = 0;
				try {
					String overtimeString = m_hourTextField.getText().trim();
					overtimevalue = new Float(overtimeString + "f")
							.floatValue();
					workingTimeState.setOverTime(overtimevalue);
					m_workTimeJDayChooser.periksaDanSave(workingTimeState);
					System.out.println(" Hour = " + m_hourTextField.getText());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this,
							"            incorect data", "Warning",
							JOptionPane.WARNING_MESSAGE);
					clear();
				}
			}
	 	}

	}
}

/**
 * 
 */
class ColorPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ColorPanel(Color color, String note) {
		JPanel rectPanel = new JPanel();
		rectPanel.setBackground(color);
		rectPanel.setPreferredSize(new Dimension(30, 30));
		rectPanel.setBorder(BorderFactory.createEtchedBorder(Color.black,
				Color.black));

		setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(rectPanel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(0, 20, 0, 0);
		add(new JLabel(note), gridBagConstraints);
	}

}
