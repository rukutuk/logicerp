package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupPmtCaIouProjectPicker extends LookupPicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LookupPmtCaIouProjectPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Cash Advance 1");
		initData();
		setSize(800, 300);
	}
	
	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("ReferenceNo"); 	
		getModel().addColumn("Project");		    
		getModel().addColumn("Ammount");
		getModel().addColumn("Description");
		getModel().addColumn("Unit");
		getModel().addColumn("Department");
		
		try {   
			GenericMapper mapnya = MasterMap.obtainMapperFor(PmtCAIOUProjectSettled.class);
			  mapnya.setActiveConn(m_conn);
			 Object[] listData=mapnya.doSelectWhere("status=3").toArray();
		  
			//	PmtCAIOUProject[] result = logic.getAllP(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			DecimalFormat formatDesimal = new DecimalFormat("#,###.00"); 
			for(int i = 0; i < listData.length;  i ++) {
				PmtCAIOUProjectSettled result=(PmtCAIOUProjectSettled)listData[i];	    
				getModel().addRow(new Object[]{
						String.valueOf((i + 1)),		 
						result,
						result.getPmtcaiouproject().getProject(),		        
						formatDesimal.format(result.getAmount()),
						result.getDescription(),
						result.getPmtcaiouproject().getUnit(),
						result.getPmtcaiouproject().getDepartment()
					});
			}
			
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning 1", JOptionPane.WARNING_MESSAGE);
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
		PmtCAIOUProjectSettled pmtCAIOUProjectSettled = (PmtCAIOUProjectSettled) obj;
		return pmtCAIOUProjectSettled.getReferenceNo() == null ? "" : pmtCAIOUProjectSettled.getReferenceNo();
		//return "a ";
	}
}
