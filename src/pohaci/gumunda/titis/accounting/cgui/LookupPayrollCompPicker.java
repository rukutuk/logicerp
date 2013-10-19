package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;

public class LookupPayrollCompPicker extends LookupPicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String ATR_JOURNAL;
	JournalStandardAccount[] m_jsacc;
	public LookupPayrollCompPicker(Connection conn, long sessionid,JournalStandardAccount[] jsacc) {
		super(conn, sessionid, "Unit Payroll Payment");
		m_jsacc = jsacc;
		setSize(800, 300);
		initData();
	}
	
	
	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("Account");
		getModel().addColumn("Description");
		getModel().addColumn("Normal Balance");
		try {	
			for (int i = 0; i < m_jsacc.length; i++) {
				if (!m_jsacc[i].isHidden()) {
					getModel().addRow(new Object[] {new Integer(i+1),m_jsacc[i].getAccount().getCode(),
							m_jsacc[i],m_jsacc[i].getBalanceAsString() });
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 2);
			setObject(obj);
		}
	}
	
	/*protected String getCaption(Object obj) {
	 Account Acct = (Account) obj;
	 return Acct.getName() == null ? "" : Acct.getName();
	 }*/
}
