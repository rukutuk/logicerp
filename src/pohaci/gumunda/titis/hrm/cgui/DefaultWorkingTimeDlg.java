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
import java.text.SimpleDateFormat;
import java.sql.Connection;
import javax.swing.*;

import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.Misc;

public class DefaultWorkingTimeDlg extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String[] STR_DAYS = new String[] { "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	JFrame m_mainframe;
	JRadioButton m_defaultdayRadioButton, m_nondefaultdayRadioButton;
	JCheckBox[] m_dayCheckBox = new JCheckBox[STR_DAYS.length];
	JRadioButton m_defaulttimeRadioButton, m_nondefaulttimeRadioButton;
	JTextField m_fromTextField, m_toTextField;
	JButton m_saveBt/*, m_cancelBt*/;
	Connection m_conn = null;
	long m_sessionid = -1;
	DefaultWorkingDay m_day = null;
	DefaultWorkingTime m_time = null;
	SimpleDateFormat m_timeformat = new SimpleDateFormat("HH:mm");
	
	public DefaultWorkingTimeDlg(Connection conn, long sessionid) {		
		m_conn = conn;
		m_sessionid = sessionid;
			constructComponent();
			initData();		
	}

	void constructComponent() {
		for (int i = 0; i < STR_DAYS.length; i++) {
			m_dayCheckBox[i] = new JCheckBox(STR_DAYS[i]);
			m_dayCheckBox[i].setEnabled(false);
		}
		m_defaultdayRadioButton = new JRadioButton(DefaultWorkingDay.STR_TYPE1);
		m_defaultdayRadioButton.addActionListener(this);
		m_nondefaultdayRadioButton = new JRadioButton(
				DefaultWorkingDay.STR_TYPE2);
		m_nondefaultdayRadioButton.addActionListener(this);

		ButtonGroup bg = new ButtonGroup();
		bg.add(m_defaultdayRadioButton);
		bg.add(m_nondefaultdayRadioButton);

		m_defaulttimeRadioButton = new JRadioButton(
				DefaultWorkingTime.STR_TYPE1);
		m_defaulttimeRadioButton.addActionListener(this);
		m_nondefaulttimeRadioButton = new JRadioButton(
				DefaultWorkingTime.STR_TYPE2);
		m_nondefaulttimeRadioButton.addActionListener(this);

		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(m_defaulttimeRadioButton);
		bg2.add(m_nondefaulttimeRadioButton);

		m_fromTextField = new JTextField(8);
		m_fromTextField.setEditable(false);
		m_toTextField = new JTextField(8);
		m_toTextField.setEditable(false);

		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		/*m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);*/

		JLabel dayLabel = new JLabel("Set Working Day");
		JLabel timeLabel = new JLabel("Set Working Time");
		JLabel fromLabel = new JLabel("From");
		JLabel toLabel = new JLabel("To");

		JPanel dayPanel = new JPanel();
		//JPanel timePanel = new JPanel();
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		dayPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		dayPanel.add(dayLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(6, 0, 0, 0);
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		dayPanel.add(new JSeparator(), gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		dayPanel.add(m_defaultdayRadioButton, gridBagConstraints);

		gridBagConstraints.gridy = 2;
		dayPanel.add(m_nondefaultdayRadioButton, gridBagConstraints);

		gridBagConstraints.insets = new Insets(10, 15, 0, 0);
		for (int i = 0; i < STR_DAYS.length; i++) {
			gridBagConstraints.gridy = i + 3;
			dayPanel.add(m_dayCheckBox[i], gridBagConstraints);
		}

		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints.gridy = STR_DAYS.length + 3;
		dayPanel.add(timeLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.ipady = 0;
		gridBagConstraints.insets = new Insets(6, 0, 0, 0);
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		dayPanel.add(new JSeparator(), gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = STR_DAYS.length + 4;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		dayPanel.add(m_defaulttimeRadioButton, gridBagConstraints);

		gridBagConstraints.gridy = STR_DAYS.length + 5;
		dayPanel.add(m_nondefaulttimeRadioButton, gridBagConstraints);

		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(5, 15, 0, 0);
		gridBagConstraints.gridy = STR_DAYS.length + 6;
		dayPanel.add(fromLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.insets = new Insets(5, 5, 0, 0);
		dayPanel.add(m_fromTextField, gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		dayPanel.add(new JLabel(" "), gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(5, 15, 0, 0);
		gridBagConstraints.gridy = STR_DAYS.length + 7;
		dayPanel.add(toLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.insets = new Insets(5, 5, 0, 0);
		dayPanel.add(m_toTextField, gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		dayPanel.add(new JLabel(" "), gridBagConstraints);

		buttonPanel.add(m_saveBt);
		//buttonPanel.add(m_cancelBt);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(17, 10, 8, 10));
		centerPanel.add(dayPanel, BorderLayout.NORTH);
		centerPanel.add(new JLabel(" "), BorderLayout.CENTER);
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);

		setLayout(new BorderLayout());
		add(centerPanel, BorderLayout.CENTER);
		/*getContentPane().setLayout(new BorderLayout());
		getContentPane().add(centerPanel, BorderLayout.CENTER);*/
	}

	public void setVisible(boolean flag) {
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int) (rc.getWidth() - rcthis.getWidth()) / 2 + rc.x,
				(int) (rc.getHeight() - rcthis.getHeight()) / 2 + rc.y,
				(int) rcthis.getWidth(), (int) rcthis.getHeight());
		super.setVisible(flag);
	}

	void setSelectedDefaultDay() {
		for (int i = 0; i < STR_DAYS.length; i++) {
			m_dayCheckBox[i].setEnabled(false);
			if (i < STR_DAYS.length - 2)
				m_dayCheckBox[i].setSelected(true);
			else
				m_dayCheckBox[i].setSelected(false);
		}
	}

	void setSelectedNonDefaultDay(DefaultWorkingDay day) {
		if (day != null) {
			for (int i = 0; i < STR_DAYS.length; i++) {
				m_dayCheckBox[i].setSelected(day.getDays()[i]);
				m_dayCheckBox[i].setEnabled(true);
			}
		} else {
			for (int i = 0; i < STR_DAYS.length; i++) {
				m_dayCheckBox[i].setSelected(false);
				m_dayCheckBox[i].setEnabled(true);
			}
		}
	}

	void setSelectedDefaultTime() {
		m_fromTextField.setEditable(false);
		m_toTextField.setEditable(false);
		m_fromTextField.setText("09:00");
		m_toTextField.setText("17:00");
	}

	void setSelectedNonDefaultTime(DefaultWorkingTime time) {
		m_fromTextField.setEditable(true);
		m_toTextField.setEditable(true);

		if (time != null) {
			m_fromTextField.setText(m_timeformat.format(m_time.getFrom()));
			m_toTextField.setText(m_timeformat.format(m_time.getTo()));
		} else {
			m_fromTextField.setText("09:00");
			m_toTextField.setText("17:00");
		}
	}

	void initData() {
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			m_day = logic.getDefaultWorkingDay(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);
			m_time = logic.getDefaultWorkingTime(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);
			if (m_day != null) {
				if (m_day.getTypeAsString().equals(DefaultWorkingDay.STR_TYPE1)) {
					m_defaultdayRadioButton.setSelected(true);
					setSelectedDefaultDay();
				} else {
					m_nondefaultdayRadioButton.setSelected(true);
					setSelectedNonDefaultDay(m_day);
				}
			}

			if (m_time != null) {
				if (m_time.getTypeAsString().equals(
						DefaultWorkingTime.STR_TYPE1)) {
					m_defaulttimeRadioButton.setSelected(true);
					setSelectedDefaultTime();
				} else {
					m_nondefaulttimeRadioButton.setSelected(true);
					setSelectedNonDefaultTime(m_time);
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	void save() {
		String strday = "", strtime = "";
		if (m_defaultdayRadioButton.isSelected())
			strday = DefaultWorkingDay.STR_TYPE1;
		else
			strday = DefaultWorkingDay.STR_TYPE2;

		m_day = new DefaultWorkingDay(DefaultWorkingDay
				.typeFromStringToID(strday), m_dayCheckBox[0].isSelected(),
				m_dayCheckBox[1].isSelected(), m_dayCheckBox[2].isSelected(),
				m_dayCheckBox[3].isSelected(), m_dayCheckBox[4].isSelected(),
				m_dayCheckBox[5].isSelected(), m_dayCheckBox[6].isSelected());

		if (m_defaulttimeRadioButton.isSelected())
			strtime = DefaultWorkingTime.STR_TYPE1;
		else
			strtime = DefaultWorkingTime.STR_TYPE2;
		try {
			java.sql.Time fromtime = new java.sql.Time(m_timeformat.parse(
					m_fromTextField.getText().trim()).getTime());
			java.sql.Time totime = new java.sql.Time(m_timeformat.parse(
					m_toTextField.getText().trim()).getTime());
			m_time = new DefaultWorkingTime(DefaultWorkingTime
					.typeFromStringToID(strtime), fromtime, totime);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
			return;
		}

		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			logic.createDefaultWorkingDay(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, m_day, m_time);
			Misc.hasSaved(this, m_day.getTypeAsString() + " and "
					+ m_time.getTypeAsString());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_defaultdayRadioButton) {
			setSelectedDefaultDay();
		} else if (e.getSource() == m_nondefaultdayRadioButton) {
			setSelectedNonDefaultDay(m_day);
		} else if (e.getSource() == m_defaulttimeRadioButton) {
			setSelectedDefaultTime();
		} else if (e.getSource() == m_nondefaulttimeRadioButton) {
			setSelectedNonDefaultTime(m_time);
		} else if (e.getSource() == m_saveBt) {
			save();
		}/* else if (e.getSource() == m_cancelBt) {
			//dispose();
		}*/
	}
}