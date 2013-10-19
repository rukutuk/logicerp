package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class CAIOUListPanel extends JPanel{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CAIOUList m_list;
	//  JToggleButton m_searchBt, m_refreshBt; 
	  JButton m_printviewBt;
	  Object objnya;
	  Connection m_conn = null;
	  long m_sessionid = -1;
	  public CAIOUListPanel(Connection conn, long sessionid,Object obj) {
	    m_conn = conn;
	    m_sessionid = sessionid;
	    objnya=obj;
	    constructComponent(obj);
	  }
	
	void constructComponent(Object obj) {
		m_list = new CAIOUList(m_conn, m_sessionid,obj);
	    m_list.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
	          if(e.getClickCount() >= 1) {
	        	 // String temp=(String)m_list.getModel().getElementAt(m_list.getSelectedIndex());
	        	  if (m_list.getSelectedIndex()!=-1)
	        	  {Object temp= m_list.getModel().getElementAt(m_list.getSelectedIndex());
	        	  setSelectedCAIOU(temp);}	
	           }
			}
		});
	    JScrollPane scroll= new JScrollPane(m_list);
	    setLayout(new BorderLayout());
	    add(scroll, BorderLayout.CENTER);
	  }

	  public JList getList() {
	    return m_list;
	  }

	  //Tambahan cok gung 9 Juni
	  Object m_selected=null;
	  public void  setSelectedCAIOU(Object obj){
		m_selected=obj;	  
		firePropertyChange("object",obj,m_selected);
	  }
	  
	  //Untuk refresh data eh
	  public void refreshData()
	  {  //constructComponent(objnya);
		  m_list.initData(objnya); 
	  }
	 
	  public double getAmountInstallment(){
		  return m_list.getAmountnya();
	  }
	}
