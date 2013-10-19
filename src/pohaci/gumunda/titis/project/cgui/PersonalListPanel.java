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

public class PersonalListPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
PersonalList m_list;
  JToggleButton m_searchBt, m_refreshBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_show = false;

  public PersonalListPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
  }

  void constructComponent() {
    System.out.println("jtoolbar personal");
    // definisikan list data personal
    m_list = new PersonalList(m_conn, m_sessionid);
    m_searchBt = new javax.swing.JToggleButton(new ImageIcon("../images/filter2.gif"));
    m_searchBt.setToolTipText("Filter");
    m_searchBt.addActionListener(this);
    m_refreshBt = new javax.swing.JToggleButton(new ImageIcon("../images/refresh.gif"));
    m_refreshBt.setToolTipText("Refresh");
    m_refreshBt.addActionListener(this);

    ButtonGroup bg = new ButtonGroup();
    bg.add(m_searchBt);
    bg.add(m_refreshBt);
    JToolBar toolbar = new JToolBar();
    toolbar.add(m_searchBt);
    toolbar.add(m_refreshBt);

    // set list personal pada jtoolbar
    setLayout(new BorderLayout());
    add(toolbar, BorderLayout.NORTH);
    add(new JScrollPane(m_list), BorderLayout.CENTER);
  }

  public JList getList() {
    return m_list;
  }

  public void reset(Personal[] personal) {
    m_list.reset(personal);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_searchBt) {
      if(!m_show) {
        m_show = true;
        SearchPersonalDlg dlg = new SearchPersonalDlg(
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