package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class TaxArt21PayrollComponentPicker extends JPanel implements
		ActionListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextArea m_payrollComponentPickerArea;

	JButton m_browseBt = new JButton("...");

	Connection m_conn = null;

	long m_sessionid = -1;

	Account m_account = null;

	TaxArt21PayrollComponentSelectionDlg dialog;

	TaxArt21Component ta21Comp;

	Vector savevector;

	private PayrollComponent[] Payrollcomponent;

	public TaxArt21PayrollComponentPicker(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		savevector = new Vector();
		initData();
	}
	
	public TaxArt21PayrollComponentPicker(Connection conn, long sessionid,TaxArt21Component ta21Comp) {
		m_conn = conn;
		m_sessionid = sessionid;
		this.ta21Comp=ta21Comp;
		savevector = new Vector();
		initData();
	}

	public TaxArt21PayrollComponentPicker() {
		savevector = new Vector();
		initData();
	}
	public void clear(){
		savevector.clear();
		//dialog.clearVector();
	}

	public void initData() {
		
		m_browseBt.setPreferredSize(new Dimension(22, 18));
		m_browseBt.addActionListener(this);
		m_payrollComponentPickerArea = new JTextArea() ;
		m_payrollComponentPickerArea.setEditable(false);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//m_payrollComponentPickerArea.setPreferredSize(new Dimension(50, 80));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(new JScrollPane(m_payrollComponentPickerArea), gbc);

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
		
		
		
		//drawPayrollComponent();

	}

	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		m_payrollComponentPickerArea.setEnabled(enable);
		m_browseBt.setEnabled(enable);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == m_browseBt) {
			dialog = new TaxArt21PayrollComponentSelectionDlg(GumundaMainFrame
					.getMainFrame(), m_conn, m_sessionid);
			dialog.setInitialData(savevector);
			dialog.initData();
			dialog.addPropertyChangeListener(this);
			dialog.setVisible(true);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals("payComp")) {
			System.out.println("firePropertyChange = " + evt.getPropertyName());
			savevector = dialog.getSaveVector();
			m_payrollComponentPickerArea.setText("");
			
			for (int i = 0; i < savevector.size(); i++) {
				PayrollComponent paycomp = (PayrollComponent) savevector.get(i);
				
				m_payrollComponentPickerArea.append(" "+ paycomp.getDescription()
						+ "  ["+paycomp.getAccount().getCode()+"]");
				m_payrollComponentPickerArea.append("\n");
				

			}
		}
	}

	public PayrollComponent[] getPayrollComponent() {
		PayrollComponent[] payroll = new PayrollComponent[savevector.size()];
		savevector.copyInto(payroll);
		for(int i=0;i<payroll.length;i++){
			System.out.println(payroll[i].toString());
		}
		return payroll;
	}

	public void setTaxArt21Component(TaxArt21Component tacomp) {
		ta21Comp = tacomp;
	}

	public void drawPayrollComponent() {
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			Payrollcomponent = logic.getTaxArt21Payroll(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, ta21Comp);
			
			
			for (int i = 0; i < Payrollcomponent.length; i++) {
				savevector.add(Payrollcomponent[i]);
				
				m_payrollComponentPickerArea.append(" "+Payrollcomponent[i].getDescription()+" ["
						+Payrollcomponent[i].getAccount().getCode()
						+"]");
				m_payrollComponentPickerArea.append("\n");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
}