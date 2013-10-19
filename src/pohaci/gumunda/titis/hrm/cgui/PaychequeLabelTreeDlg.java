package pohaci.gumunda.titis.hrm.cgui;

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
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class PaychequeLabelTreeDlg extends JDialog implements ActionListener, TreeSelectionListener{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_owner;
  PaychequeLabelTree m_accTree;
  JButton m_ok, m_cancel;

  Connection m_conn = null;
  long m_sessionid = -1;
  PaychequeLabel m_label = null;

  public PaychequeLabelTreeDlg(JFrame owner, String title, Connection conn, long sessionid) {
    super(owner, ( title == null ) ? "Chart of Account" : title, true);
    setSize(350, 350);

    m_owner = owner;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponents();
  }

  void constructComponents(){
    m_accTree = new PaychequeLabelTree(m_conn, m_sessionid);
    m_accTree.addTreeSelectionListener(this);
    JScrollPane sp = new JScrollPane();
    sp.getViewport().add(m_accTree);

    m_ok = new JButton("OK");
    m_cancel = new JButton("Cancel");
    m_ok.addActionListener(this);
    m_cancel.addActionListener(this);

    JPanel btpanel = new JPanel();
    btpanel.add(m_ok);
    btpanel.add(m_cancel);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(sp, BorderLayout.CENTER);
    getContentPane().add(btpanel, BorderLayout.SOUTH);
  }

  public void setVisible(boolean flag){
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation((dim.width/2)-(getWidth()/2),(dim.height/2)-(getHeight()/2));
    super.setVisible(flag);
  }

  public PaychequeLabel getPaychequeLabel(){
    return m_label;
  }

  public void actionPerformed(ActionEvent e){
    if(e.getSource() == m_ok){
      if(m_label == null)
        return;

      dispose();
    }
    else if(e.getSource() == m_cancel){
      m_label = null;
      dispose();
    }
  }

  public void valueChanged(TreeSelectionEvent e) {
    TreePath path = e.getNewLeadSelectionPath();
    if(path == null)
      return;

    DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
    if(node != m_accTree.getModel().getRoot())
      m_label = (PaychequeLabel)node.getUserObject();
  }
}