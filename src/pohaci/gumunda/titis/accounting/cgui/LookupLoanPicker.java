package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class LookupLoanPicker extends LookupPicker {
	
	private static final long serialVersionUID = 1L;
	private Account account = null;
	
	public LookupLoanPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Loan");
		initData();
		setSize(800, 300);
	}
	
	public LookupLoanPicker(Connection conn, long sessionid, Account account) {
		super(conn, sessionid, "Lookup Loan");
		initData();
		setSize(800, 300);
		this.account = account;
	}
	
	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("Creditor Code");
		getModel().addColumn("Loan Code");
		getModel().addColumn("Loan Description");
		getModel().addColumn("Initial Loan Amount");
		getModel().addColumn("Loan Account");
		
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			CompanyLoan[] result = null;
			if (account==null)
				result = logic.getAllCompanyLoan(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			else 
				result = logic.getCompanyLoanByAccount(m_sessionid, IDBConstants.MODUL_MASTER_DATA, account);
					
			int i=0;
			for(i = 0; i < result.length;  i ++) {
				getModel().addRow(new Object[]{
						new Integer(i + 1),
						result[i].getCreditorList(), 
						result[i].getCode(),
						result[i].getName(), 
						new Double(result[i].getInitial()),
						result[i].getAccount(),
						result[i]
				});
			}			
			
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void select() {
		int rowindex = m_table.getSelectedRow();
		if(rowindex != -1) {
			setObject(getModel().getValueAt(rowindex, 6));
		}
	}
	public void findData(long index){
		AccountingSQLSAP isql = new AccountingSQLSAP();
		try{
			CompanyLoan obj=isql.getCompanyLoan(index, m_conn);
			setObject(obj);
			//   System.out.println(obj.getName());
		}
		catch (Exception ex) {
			//    JOptionPane.showMessageDialog(this, ex.getMessage(),
			//                                "Warning", JOptionPane.WARNING_MESSAGE);
		}
		
	}
}