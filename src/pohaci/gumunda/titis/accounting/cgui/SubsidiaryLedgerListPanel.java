package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;
import pohaci.gumunda.titis.accounting.cgui.SubsidiaryLedgerList;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;


public class SubsidiaryLedgerListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	SubsidiaryLedgerList m_list;		
	Connection m_conn = null;
	long m_sessionid = -1;
	SubsidiaryAccountSetting m_subsidiariAcc;
	Object m_obj;
	
	public SubsidiaryLedgerListPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;		
		constructComponent();
	}
	
	public SubsidiaryLedgerListPanel(Connection conn, long sessionid,Object obj) {
		m_conn = conn;
		m_sessionid = sessionid;		
		m_obj = obj;
		constructComponent1();
	}
	
	void constructComponent() {		
		m_list = new SubsidiaryLedgerList(m_conn, m_sessionid);
		m_list.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 1) {
					Object temp=(Object)m_list.getModel().getElementAt(m_list.getSelectedIndex());
					setSelectedList(temp);	
				}
			}
		});		
		
		setLayout(new BorderLayout());		
		add(new JScrollPane(m_list), BorderLayout.CENTER);
	}
	
	void constructComponent1() {		
		m_list = new SubsidiaryLedgerList(m_conn, m_sessionid,m_obj);
		m_list.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 1) {
					Object temp=(Object)m_list.getModel().getElementAt(m_list.getSelectedIndex());
					setSelectedList(temp);	
				}
			}
		});		
		
		setLayout(new BorderLayout());		
		add(new JScrollPane(m_list), BorderLayout.CENTER);
	}
	
		
	public JList getList() {
		return m_list;
	}
	Object m_selected=null;
	public void setSelectedList(Object obj){
		m_selected=obj;
	}
	public Object getSelected(){
		return m_selected;
	}
}