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

import pohaci.gumunda.titis.accounting.cgui.UnitListDlg;
import pohaci.gumunda.titis.accounting.entity.Unit;

// import pohaci.gumunda.titis.accounting.cgui.*;

public class UnitListDlgPicker extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextField m_formulaTextField;

	JButton m_browseBt = new JButton("...");

	Connection m_conn = null;

	long m_sessionid = -1;

	// Account m_account = null;

	private UnitListDlg unitListDlg;

	private Unit m_unit;

	public UnitListDlgPicker(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initData();
	}

	public void initData() {

		m_browseBt.setPreferredSize(new Dimension(22, 18));
		m_browseBt.addActionListener(this);
		 m_formulaTextField = new JTextField() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void addMouseListener(
		          MouseListener l) { }

		      public boolean isFocusTraversable() {
		        return false;
		      }
		    };
		m_formulaTextField.setEditable(true);
		setEnabled(true);

		//m_formulaTextField.setEditable(false);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// m_payrollComponentPickerArea.setPreferredSize(new Dimension(50, 80));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(m_formulaTextField, gbc);

		gbc.gridx = 2;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(m_browseBt, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(new JLabel(" "), gbc);

	}

	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		m_formulaTextField.setEnabled(enable);
		m_browseBt.setEnabled(enable);
	}

	public void setUnit(Unit unit) {
		m_unit = unit;
		if (unit != null)
			m_formulaTextField.setText(m_unit.toString());
		else
			m_formulaTextField.setText(""); 
	}

	public Unit getUnit() {
		return m_unit;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_browseBt) {
			unitListDlg = new UnitListDlg(pohaci.gumunda.cgui.GumundaMainFrame
					.getMainFrame(), m_conn, m_sessionid);
			unitListDlg.setVisible(true);
			if (unitListDlg.getResponse() == JOptionPane.OK_OPTION) {
				setUnit(unitListDlg.getUnit());
			}
		}
	}
}