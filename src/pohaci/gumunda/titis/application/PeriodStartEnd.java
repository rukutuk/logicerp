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
import java.awt.event.*;
import java.util.Date;

import javax.swing.*;

public class PeriodStartEnd extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String m_title = "";  
	public DatePicker m_startDate,m_endDate=null;
	
	public PeriodStartEnd(String title) {
		m_title = title;
		constructComponent(title);
	}
	
	void constructComponent(String title) {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title + ":",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));		
			
		m_startDate = new DatePicker();
		m_endDate = new DatePicker();
		m_startDate.setDate(new Date());
		m_endDate.setDate(new Date());
		
		JLabel startLabel = new JLabel("Start Date");
		startLabel.setPreferredSize(new Dimension(50, 22));
		JLabel yearLabel = new JLabel("End Date");
		yearLabel.setPreferredSize(new Dimension(50, 22));
		JPanel monthPanel = new JPanel();
		JPanel yearPanel = new JPanel();
		
		monthPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(2, 1, 1, 1);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		monthPanel.add(startLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		monthPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		monthPanel.add(m_startDate, gridBagConstraints);
		
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
		monthPanel.add(m_endDate, gridBagConstraints);    
		
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
