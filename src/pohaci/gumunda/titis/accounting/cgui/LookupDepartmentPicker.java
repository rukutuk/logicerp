package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.JOptionPane;

import pohaci.gumunda.titis.application.AttributeListDlg;
import pohaci.gumunda.titis.application.AttributePicker;
import pohaci.gumunda.titis.hrm.cgui.OrganizationTreeDlg;

public class LookupDepartmentPicker extends AttributePicker {
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	OrganizationTreeDlg m_treetDlg;
	   AttributeListDlg m_attrlistDlg = null;
	  //JButton m_okBt, m_cancelBt;

	  public LookupDepartmentPicker(Connection conn, long sessionid) {
	    super(conn, sessionid);
	    initComponent();
	    initData();
	  }

	  void initComponent() {
		  //m_attrlistDlg = new AttributeListDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
			       // "Department", m_conn, m_sessionid);
	  }

	  void initData() {
	   /* try {
	      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
	      Organization[] organization = logic.getAll(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

	      DefaultListModel model = (DefaultListModel)m_treetDlg.getAttributeList().getModel();
	      for(int i = 0; i < unit.length; i ++) {
	        model.addElement(new Unit(unit[i], Unit.CODE_DESCRIPTION));
	      }
	    }
	    catch(Exception ex) {
	      JOptionPane.showMessageDialog(this, ex.getMessage(),
	                                    "Warning", JOptionPane.WARNING_MESSAGE);
	    }*/
	  }

	  public void done() {
		m_treetDlg= new OrganizationTreeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn, m_sessionid,false);
		m_treetDlg.setVisible(true);
	    if(m_treetDlg.getResponse() == JOptionPane.OK_OPTION) {	      
	      setObject(m_treetDlg.getOrganization());	      	       
	    }
	  }
	  
	 /* public Unit getUnit(){
		 return n_unit;  
	  }*/

	}
