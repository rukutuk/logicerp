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

public class PayrollComponentTreeDlg extends JDialog implements ActionListener, TreeSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  PayrollComponentTree m_tree;
  JButton m_okBt, m_cancelBt;

  PayrollComponent m_component = null;
  int m_iResponse = JOptionPane.NO_OPTION;

  public PayrollComponentTreeDlg(JFrame owner, Connection conn, long sessionid) {
    super(owner, "Payroll Component", true);
    setSize(350, 350);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponents();
  }

  void constructComponents(){
    m_tree = new PayrollComponentTree(m_conn, m_sessionid, PayrollComponent.NONE);
    m_tree.addTreeSelectionListener(this);

    m_okBt = new JButton("Select");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(new JScrollPane(m_tree), BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  }

  public void setVisible(boolean flag){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  public PayrollComponent getPayrollComponent(){
    return m_component;
  }

  public int getResponse() {
    return m_iResponse;
  }

  public void actionPerformed(ActionEvent e){
    if(e.getSource() == m_okBt){
      if(m_component == null)
        return;

      if(m_component.isGroup()) {
        JOptionPane.showMessageDialog(this, "Please select not group payroll component", "Information", JOptionPane.INFORMATION_MESSAGE);
        return;
      }

      m_iResponse = JOptionPane.OK_OPTION;
      dispose();
    }
    else if(e.getSource() == m_cancelBt){
      dispose();
    }
  }

  public void valueChanged(TreeSelectionEvent e) {
    TreePath path = e.getNewLeadSelectionPath();
    if(path != null) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
      if(node != m_tree.getModel().getRoot())
        m_component = (PayrollComponent)node.getUserObject();
    }
  }
}

