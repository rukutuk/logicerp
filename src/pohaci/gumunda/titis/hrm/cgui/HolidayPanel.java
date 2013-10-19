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
import java.sql.Connection;
import javax.swing.*;

import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class HolidayPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JButton m_editBt, m_saveBt, m_cancelBt;

	JCheckBox m_publicCheckBox;

	JTextField m_descriptionTextField;

	HolidayJDayChooser m_dayChooser;

	JCalendar m_calendar;

	private String TempDescription;

	static Connection m_conn = null;

	static long m_sessionid = -1;

	public HolidayPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
		setEditableForm(false);
	}
	
	void constructComponent() {
		m_editBt = new JButton("Edit");
		m_editBt.addActionListener(this);
		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);

		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);

		m_dayChooser = new HolidayJDayChooser(this, m_conn, m_sessionid);
		m_calendar = new JCalendar(JMonthChooser.NO_SPINNER, m_dayChooser);
		m_calendar.setPreferredSize(new Dimension(250, 250));
		m_publicCheckBox = new JCheckBox("Public Holiday");
		m_publicCheckBox.addActionListener(this);
		m_descriptionTextField = new JTextField();
		m_dayChooser.setDay(1);
		JPanel mergePanel = new JPanel();
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

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
		JLabel date2Label = new JLabel("Set Selected Date to:"), descriptionLabel = new JLabel(
				"Description");

		buttonPanel.add(m_editBt);
		buttonPanel.add(m_saveBt);
		buttonPanel.add(m_cancelBt);

		legendPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 10, 0);
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		legendPanel.add(legendLabel, gridBagConstraints);

		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(0, 0, -2, 0);
		legendPanel.add(new ColorPanel(Color.WHITE, "Default Working Time"),
				gridBagConstraints);

		gridBagConstraints.gridy = 2;
		legendPanel.add(new ColorPanel(Color.RED, "Holiday"),
				gridBagConstraints);

		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		legendPanel.add(new ColorPanel(Color.lightGray,
				"Default Non-Working Time"), gridBagConstraints);

		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.weightx = 1.0;
		legendPanel.add(new JPanel(), gridBagConstraints);

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

		descriptionPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 10, 0);
		descriptionPanel.add(date2Label, gridBagConstraints);

		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(0, 0, 30, 0);
		descriptionPanel.add(m_publicCheckBox, gridBagConstraints);

		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(0, 0, 10, 0);
		descriptionPanel.add(descriptionLabel, gridBagConstraints);

		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		descriptionPanel.add(m_descriptionTextField, gridBagConstraints);

		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.weightx = 1.0;
		descriptionPanel.add(new JPanel(), gridBagConstraints);

		mergePanel.setLayout(new BorderLayout(5, 5));
		mergePanel.add(leftlegendPanel, BorderLayout.WEST);
		mergePanel.add(leftdatePanel, BorderLayout.CENTER);

		setLayout(new BorderLayout(5, 5));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		add(buttonPanel, BorderLayout.NORTH);
		add(mergePanel, BorderLayout.WEST);
		add(centerdescriptionPanel, BorderLayout.CENTER);
	}

	void setEditableForm(boolean editable) {
		m_publicCheckBox.setEnabled(editable);
		m_descriptionTextField.setEditable(editable);
	}

	void edit() {
		m_publicCheckBox.setEnabled(true);
		if (m_publicCheckBox.isSelected()) {
			m_descriptionTextField.setEditable(true);
		} else {
			m_descriptionTextField.setEditable(false);
		}
		m_calendar.disableMonthYear();
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		m_dayChooser.periksaDanSave(m_dayChooser.getDate());
	}

	void save() {
		Holiday[] holiday = null;
		try {
			m_dayChooser.periksaDanSave(m_dayChooser.holidaySelectedDate);
			m_dayChooser.holidaySelectedDate=null;
			holiday = m_dayChooser.getHoliday();
			System.out.println("holiday.length = " + holiday.length);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Information",
					JOptionPane.INFORMATION_MESSAGE);
			System.out.println("ERROR EUY  di save");
			return;
		}

		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			logic.createHoliday(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
					holiday);
			m_dayChooser.initHoliday();
			m_dayChooser.setDay(1);
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("ERROR EUY  di save 2");
			return;
		}

		setEditableForm(false);
		//m_descriptionTextField.setText("");
		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_calendar.enableMonthYear();
	}

	void cancel() {
		//clear();
		setEditableForm(false);
		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_dayChooser.initHoliday();
		m_dayChooser.setDay(1);
		m_calendar.enableMonthYear();
	}

	public void clear() {
		m_publicCheckBox.setSelected(false);
		m_descriptionTextField.setText("");
	}

	protected void setHoliday(Holiday holiday) {
		if (holiday==null)
		{
			m_publicCheckBox.setSelected(false);
			m_descriptionTextField.setText("");
		} else {
			m_publicCheckBox.setSelected(true);
			m_descriptionTextField.setText(holiday.getDescription());
		}
	}

	protected Holiday getHoliday() {
		String description = m_descriptionTextField.getText().trim();

		return new Holiday(m_calendar.dayChooser.getDate(), description);
	}
	
	public String getDescriptionMinusOne(){
		return TempDescription;
		
	}
	public void setDescriptionMinusOne(){
		TempDescription=m_descriptionTextField.getText().trim();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_editBt) {
			edit();
		} else if (e.getSource() == m_saveBt) {
			save();
			// m_dayChooser.periksaDanSave(getHoliday().getDate());
		} else if (e.getSource() == m_cancelBt) {
			cancel();
		} else if (e.getSource() == m_publicCheckBox) {
			if (m_publicCheckBox.isSelected()) {
				m_descriptionTextField.setEditable(true);
			} else {
				m_descriptionTextField.setEditable(false);
			}
		}
	}
	static class ColorPanel extends JPanel {
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


}
