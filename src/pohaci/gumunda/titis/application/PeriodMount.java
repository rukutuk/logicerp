package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

public class PeriodMount extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

JTextField m_nameTextField, m_pathTextField;  

  File m_file = null;
  String m_title = "";
  String m_filename = "";
  byte[] m_filebyte = null;
  public JMonthChooser m_monthComboBox;
  public JSpinField m_yearField;
 
  public PeriodMount(String title) {
    m_title = title;
    constructComponent(title);
  }

  void constructComponent(String title) {
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title + ":",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
    
    m_nameTextField = new JTextField();
    m_nameTextField.setRequestFocusEnabled(false);
    m_pathTextField = new JTextField();
    m_pathTextField.setRequestFocusEnabled(false);
  
    m_monthComboBox = new JMonthChooser(JMonthChooser.NO_SPINNER);
	m_yearField = new JSpinField();
	m_yearField.setMaximum(9999);
	m_yearField.setValue(new java.util.GregorianCalendar()
			.get(java.util.Calendar.YEAR));
	
    JLabel monthLabel = new JLabel("Month");
    monthLabel.setPreferredSize(new Dimension(50, 22));
    JLabel yearLabel = new JLabel("Year");
    yearLabel.setPreferredSize(new Dimension(50, 22));
    JPanel monthPanel = new JPanel();
    JPanel yearPanel = new JPanel();

    monthPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 1, 1, 1);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    monthPanel.add(monthLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    monthPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    monthPanel.add(m_monthComboBox, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;    
    
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 1, 3, 1);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    monthPanel.add(yearLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    monthPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    monthPanel.add(m_yearField, gridBagConstraints);    

    setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    add(monthPanel, gridBagConstraints);
    gridBagConstraints.gridy = 1;
    add(yearPanel, gridBagConstraints);
  }
 

  public void actionPerformed(ActionEvent e) { 
  }
}
