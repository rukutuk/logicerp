package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.application.AttributeCellEditor;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class LeavePermissionTypeCellEditor extends AttributeCellEditor {

	//private short m_type = -1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeavePermissionTypeCellEditor(JFrame owner, String title,
			Connection conn, long sessionid, short type) {
		super(owner, title, conn, sessionid, type);
	}
	
	public void initData() {
		DefaultListModel model = this.getAttributeListModel();
	    try {
	    	HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
	    	LeavePermissionType[] object = logic.getAllLeavePermissionType(
	    		  m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_type);
	    	for(int i = 0; i < object.length; i ++)
	    		model.addElement(new LeavePermissionType(object[i], LeaveType.CODE_DESCRIPTION));
	    }
	    	catch(Exception ex) {
	    		JOptionPane.showMessageDialog(m_owner, ex.getMessage(),
	                                    "Warning", JOptionPane.WARNING_MESSAGE);
	    }
	}
}
