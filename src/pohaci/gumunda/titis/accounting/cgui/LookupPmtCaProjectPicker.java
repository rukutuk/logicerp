package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupPmtCaProjectPicker extends LookupPicker {
	private static final long serialVersionUID = 1L;

	public LookupPmtCaProjectPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Cash Advance 2");
		initData();
		setSize(800, 300);
	}
	
	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("ReferenceNo");
		getModel().addColumn("CashAccount");		    
		getModel().addColumn("BankAccount");
		getModel().addColumn("Project");
		getModel().addColumn("Amount");
		getModel().addColumn("ExchangeRate");
		getModel().addColumn("Unit");
		getModel().addColumn("Cash Advance Type");
		
		try {
			GenericMapper mapnya = MasterMap.obtainMapperFor(PmtCAProject.class);
			 mapnya.setActiveConn(m_conn);
			 Object[] listData=mapnya.doSelectWhere("status=3").toArray();
		 
			///*AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			//PmtCAProject[] result = logic.getAllPmtCAProject(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			DecimalFormat formatDesimal = new DecimalFormat("#,###.00"); 
			for(int i = 0; i < listData.length;  i ++) {
				PmtCAProject result=(PmtCAProject)listData[i];	    
				String bankaccount = "";
				String cashaccount = "";
				if (result.getCashAccount()!=null)
					bankaccount = result.getCashAccount().getName();
				if (result.getBankAccount()!=null)
					cashaccount = result.getBankAccount().getName();
				getModel().addRow(new Object[]{						
						String.valueOf((i + 1)),		 
						result,
						cashaccount,
						bankaccount,
						result.getProject(),
						formatDesimal.format(result.getAmount()),
						formatDesimal.format(result.getExchangeRate()),
						result.getUnit()
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
	      Object obj = getModel().getValueAt(rowindex, 1);
	      setObject(obj);
	    }
	}
	
	protected String getCaption(Object obj){
		PmtCAProject PmtCAProject = (PmtCAProject) obj;
		return PmtCAProject.getReferenceNo() == null ? "" : PmtCAProject.getReferenceNo();
		//return "a ";
	}

}
