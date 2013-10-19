package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class MaritalStatusPanel extends SimpleEmployeeAttributePanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public MaritalStatusPanel(Connection conn, long sessionid) {
    super(conn, sessionid);
  }

  void initData() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.setRowCount(0);

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      SimpleEmployeeAttribute[] status = logic.getAllMaritalStatus(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < status.length;  i ++) {
        model.addRow(new Object[]{status[i], status[i].getDescription()});
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void onSave() {
    m_table.stopCellEditing();

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    Object objValue = model.getValueAt(m_editedIndex, 0);
    String desc = "";
    if(objValue instanceof SimpleEmployeeAttribute)
      desc = ((SimpleEmployeeAttribute)objValue).getDescription();
    else
      desc = (String)objValue;

    if(desc == null || desc.equals("")){
      JOptionPane.showMessageDialog(this, "Description of Marital Status have to fill.");
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

      if(m_new){
        SimpleEmployeeAttribute status = logic.createMaritalStatus(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new SimpleEmployeeAttribute(desc));
        model.setValueAt(status, m_editedIndex, 0);
      }
      else if(m_edit){
        SimpleEmployeeAttribute status = logic.updateMaritalStatus(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_attr.getIndex(), new SimpleEmployeeAttribute(desc));
        model.setValueAt(status, m_editedIndex, 0);
      }
    }
    catch(Exception ex){
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_new = false;
    m_edit = false;
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(true);
  }

  void onDelete() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    m_editedIndex = m_table.getSelectedRow();
    SimpleEmployeeAttribute status = (SimpleEmployeeAttribute)model.getValueAt(m_editedIndex, 0);

    if(!pohaci.gumunda.titis.application.Misc.getConfirmation())
      return;

    try{
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      logic.deleteMaritalStatus(m_sessionid, IDBConstants.MODUL_MASTER_DATA, status.getIndex());
    }
    catch(Exception ex){
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }
    model.removeRow(m_editedIndex);
  }
}