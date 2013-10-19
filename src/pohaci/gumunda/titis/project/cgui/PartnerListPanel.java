package pohaci.gumunda.titis.project.cgui;

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

public class PartnerListPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
PartnerList m_list;
  JToggleButton m_serachBt, m_refreshBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_show = false;

  public PartnerListPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
  }

  void constructComponent() {
    System.out.println("Jtoolbar partner komponen");
    // definisikan list partner
    m_list = new PartnerList(m_conn, m_sessionid);
    
    m_serachBt = new javax.swing.JToggleButton(new ImageIcon("../images/filter2.gif"));
    m_serachBt.setToolTipText("Filter");
    m_serachBt.addActionListener(this);
    m_refreshBt = new javax.swing.JToggleButton(new ImageIcon("../images/refresh.gif"));
    m_refreshBt.setToolTipText("Refresh");
    m_refreshBt.addActionListener(this);  
    ButtonGroup bg = new ButtonGroup();
    bg.add(m_serachBt);
    bg.add(m_refreshBt);
    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);
    toolbar.add(m_serachBt);
    toolbar.add(m_refreshBt);

    // set list partner pada jtoolbar
    setLayout(new BorderLayout());
    add(toolbar, BorderLayout.NORTH);
    add(new JScrollPane(m_list), BorderLayout.CENTER);
  }

  public JList getList() {
    return m_list;
  }

  public void reset(Partner[] partner) {
    m_list.reset(partner);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_serachBt) {
      if(!m_show) {
        m_show = true;
        SearchPartnerDlg dlg = new SearchPartnerDlg(
            pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            this, m_conn, m_sessionid);
        dlg.setVisible(true);
        m_show = false;
      }
    }
    else if(e.getSource() == m_refreshBt) {
      m_list.refresh();
    }
  }
}