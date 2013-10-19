package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.AttributeListDlg;
import pohaci.gumunda.titis.application.AttributePicker;

public class UnitPicker extends AttributePicker {
	private static final long serialVersionUID = 1L;
	AttributeListDlg m_attrlistDlg = null;
	Unit m_unit = null;
	
	public UnitPicker(Connection conn, long sessionid) {
		super(conn, sessionid);
		initComponent();
		initData();
	}
	
	public UnitPicker(){
		super(null,0);
	}
	
	public void init(Connection conn, long sessionid) {
		this.m_conn = conn;
		this.m_sessionid = sessionid;
		initComponent();
		initData();
	}
	
	void initComponent() {
		m_attrlistDlg = new AttributeListDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				"Unit Code", m_conn, m_sessionid);
	}
	
	void initData() {
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			Unit[] unit = logic.getAllUnit(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			DefaultListModel model = (DefaultListModel)m_attrlistDlg.getAttributeList().getModel();
			for(int i = 0; i < unit.length; i ++) {				
				setDefaultUnit(unit[0]);				
				model.addElement(new Unit(unit[i], Unit.CODE_DESCRIPTION));
			}
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void done() {
		m_attrlistDlg.setVisible(true);
		if(m_attrlistDlg.getResponse() == JOptionPane.OK_OPTION) {
			Object[] object = m_attrlistDlg.getObject();
			if(object.length > 0)
				setObject(object[0]);
		} else {
			setObject(null);
		}
	}
	
	public Unit getUnit(){
		return (Unit) getObject();
	}
	
	public Unit getDefaultUnit(){		
		return m_unit; 
	}	
	
	public void setDefaultUnit(Unit unit){		
		m_unit = unit;
	}	
}
