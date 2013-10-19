package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupESCashAdvance extends LookupPicker {
	private static final long serialVersionUID = 1L;
    long index=-1;
	public LookupESCashAdvance(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Expense Sheet Cash Advance");
		initData();
		setSize(800, 300);
	}
	public LookupESCashAdvance(Connection conn, long sessionid,long a) {
		super(conn, sessionid, "Lookup Expense Sheet Cash Advance");
		index=a;
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
			 Object[] listData=mapnya.doSelectWhere("status=3 and PayTo="+Long.toString(index)).toArray();
			 System.out.println("Index pegawai"+Long.toString(index));
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
						result.getUnit(),
						"General"
						});
			}
			mapnya = MasterMap.obtainMapperFor(PmtCAIOUProjectSettled.class);
			  mapnya.setActiveConn(m_conn);
			listData=mapnya.doSelectWhere("status=3").toArray();
		  
			//	PmtCAIOUProject[] result = logic.getAllP(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			//DecimalFormat formatDesimal = new DecimalFormat("#,###.00"); 
			for(int i = 0; i < listData.length;  i ++) {
				PmtCAIOUProjectSettled result=(PmtCAIOUProjectSettled)listData[i];	    
				getModel().addRow(new Object[]{
						String.valueOf((i + 1)),		 
						result,
						result.getCashAccount(),
						result.getBankAccount(),
						result.getPmtcaiouproject().getProject(),		        
						formatDesimal.format(result.getAmount()),
						formatDesimal.format(result.getExchangeRate()),
						result.getPmtcaiouproject().getUnit(),
						"IOU"
						//result.getPmtcaiouproject().getDepartment()
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
		if (obj instanceof PmtCAProject){
		PmtCAProject PmtCAProject = (PmtCAProject) obj;
		return PmtCAProject.getReferenceNo() == null ? "" : PmtCAProject.getReferenceNo();}
		else if(obj instanceof PmtCAIOUProjectSettled){
			PmtCAIOUProjectSettled PmtCAProject = (PmtCAIOUProjectSettled) obj;
		return PmtCAProject.getReferenceNo() == null ? "" : PmtCAProject.getReferenceNo();}
		else
			return null;
		
		//return "a ";
	}

}

