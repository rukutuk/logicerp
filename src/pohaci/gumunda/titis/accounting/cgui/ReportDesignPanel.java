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
import javax.swing.tree.*;

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.AccountTree;
import pohaci.gumunda.titis.application.Misc;

public class ReportDesignPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  ReportPicker m_reportPicker;
  JButton m_addBt, m_deleteBt;
  ReportLabelTree m_labelTree;
  AccountTree m_accountTree;

  JPopupMenu m_popupMenu = new JPopupMenu();
  JMenuItem mi_add = new JMenuItem("Add");
  JMenuItem mi_edit = new JMenuItem("Edit");
  JMenuItem mi_delete = new JMenuItem("Delete");

  public ReportDesignPanel(Connection conn, long sessionid, String title) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent(title);
  }

  void constructComponent(String title) {
    m_reportPicker = new ReportPicker(m_conn, m_sessionid, this, title);
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);

    m_labelTree = new ReportLabelTree(m_conn, m_sessionid);
    m_labelTree.addMouseListener(new TreeMouseAdapter());
    m_accountTree = new AccountTree(m_conn, m_sessionid);

    m_popupMenu.add(mi_add);
    mi_add.addActionListener(this);
    m_popupMenu.add(mi_edit);
    mi_edit.addActionListener(this);
    m_popupMenu.add(mi_delete);
    mi_delete.addActionListener(this);

    JSplitPane splitPane = new JSplitPane();
    JPanel leftPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel rightPanel = new JPanel();

    // northPanel
    northPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(1, 0, 3, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    northPanel.add(new JLabel("Report Name"), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(1, 0, 3, 3);
    northPanel.add(m_reportPicker, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    northPanel.add(m_addBt, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    northPanel.add(m_deleteBt, gridBagConstraints);

    // leftPanel
    leftPanel.setLayout(new BorderLayout());
    leftPanel.add(northPanel, BorderLayout.NORTH);
    leftPanel.add(new JScrollPane(m_labelTree), BorderLayout.CENTER);

    // rightPanel
    rightPanel.setLayout(new BorderLayout());
    rightPanel.add(new JScrollPane(m_accountTree), BorderLayout.CENTER);

    splitPane.setDividerLocation(400);
    splitPane.setLeftComponent(leftPanel);
    splitPane.setRightComponent(rightPanel);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
  }

  void add() {
    ReportCreatorDlg dlg = new ReportCreatorDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);
    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      Report report = dlg.getReport();
      m_reportPicker.setObject(report);
    }
  }

  void delete() {
    Report report = (Report)m_reportPicker.getObject();
    if(report == null)
      return;
    if(!Misc.getConfirmation())
      return;

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.deleteBalanceSheetReport(m_sessionid, IDBConstants.MODUL_MASTER_DATA, report.getIndex());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_reportPicker.setObject(null);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      add();
    }
    else if(e.getSource() == m_deleteBt) {
      delete();
    }
  }

  /**
   *
   */
  class TreeMouseAdapter extends MouseAdapter {
    public void mouseReleased(MouseEvent e) {
      if(e.isPopupTrigger()){
        DefaultTreeModel model = (DefaultTreeModel)m_labelTree.getModel();
        TreePath path = m_labelTree.getSelectionPath();
        if(path != null){
          DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
          if(node == model.getRoot()) {
            mi_add.setEnabled(true);
            mi_edit.setEnabled(false);
            mi_delete.setEnabled(false);
          }
          else {
            mi_add.setEnabled(false);
            mi_edit.setEnabled(true);
            mi_delete.setEnabled(true);
          }

          Rectangle rectangle = m_labelTree.getPathBounds(path);
          if( rectangle.contains(e.getPoint()))
            m_popupMenu.show(m_labelTree, e.getX(), e.getY());
        }
      }
    }
  }
}