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

import java.awt.dnd.*;
import java.awt.datatransfer.*;

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.Misc;

public class WorksheetDesignPanel extends JPanel implements ActionListener, Transferable,
    DragSourceListener, DragGestureListener, DropTargetListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  WorksheetPicker m_worksheetPicker;
  JButton m_addBt, m_deleteBt;
  JList m_list;
  WorksheetColumnTree m_tree;
  JPopupMenu m_popupMenu = new JPopupMenu();
  JMenuItem mi_add = new JMenuItem("Add");
  JMenuItem mi_delete = new JMenuItem("Delete");

  public static final DataFlavor flavor = new DataFlavor(JList.class, "JList");
  public static final DataFlavor[] flavors = new DataFlavor[]{flavor};

  public WorksheetDesignPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    reloadJournal();
  }

  void constructComponent() {
    m_list = new JList(new DefaultListModel());
    m_worksheetPicker = new WorksheetPicker(m_conn, m_sessionid, this);
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);

    m_tree = new WorksheetColumnTree(m_conn, m_sessionid);
    m_tree.addMouseListener(new TreeMouseAdapter());
    m_popupMenu.add(mi_add);
    mi_add.addActionListener(this);
    m_popupMenu.add(mi_delete);
    mi_delete.addActionListener(this);

    DragSource ds = new DragSource();
    DragSource.getDefaultDragSource();
    ds.createDefaultDragGestureRecognizer(m_list, DnDConstants.ACTION_COPY, this);
    m_tree.setDropTarget(new DropTarget(m_tree, this));

    JSplitPane splitPane = new JSplitPane();
    JPanel leftPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel rightPanel = new JPanel();

    // leftPanel
    leftPanel.setLayout(new BorderLayout());
    leftPanel.setBorder(BorderFactory.createEtchedBorder());
    leftPanel.add(new JLabel("Journal", SwingConstants.CENTER), BorderLayout.NORTH);
    leftPanel.add(new JScrollPane(m_list), BorderLayout.CENTER);

    // northPanel
    northPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(1, 0, 3, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    northPanel.add(new JLabel("Worksheet Name"), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(1, 0, 3, 3);
    northPanel.add(m_worksheetPicker, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    northPanel.add(m_addBt, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    northPanel.add(m_deleteBt, gridBagConstraints);

    // rightPanel
    rightPanel.setLayout(new BorderLayout());
    rightPanel.add(northPanel, BorderLayout.NORTH);
    rightPanel.add(new JScrollPane(m_tree), BorderLayout.CENTER);

    splitPane.setDividerLocation(200);
    splitPane.setLeftComponent(leftPanel);
    splitPane.setRightComponent(rightPanel);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
  }

  void reloadJournal() {
    DefaultListModel model = (DefaultListModel)m_list.getModel();

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Journal[] journal = logic.getAllJournal(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < journal.length; i ++)
        model.addElement(journal[i]);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void add() {
    WorksheetCreatorDlg dlg = new WorksheetCreatorDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);
    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      Worksheet worksheet = dlg.getWorksheet();
      m_worksheetPicker.setObject(worksheet);
    }
  }

  void delete() {
    Worksheet worksheet = (Worksheet)m_worksheetPicker.getObject();
    if(worksheet == null)
      return;
    if(!Misc.getConfirmation())
      return;

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.deleteWorksheet(m_sessionid, IDBConstants.MODUL_MASTER_DATA, worksheet.getIndex());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_worksheetPicker.setObject(null);
  }

  void addColumn() {
    Worksheet worksheet = (Worksheet)m_worksheetPicker.getObject();
    if(worksheet == null)
      return;

    WorksheetColumnCreatorDlg dlg = new WorksheetColumnCreatorDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, worksheet);
    dlg.setVisible(true);
    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
      DefaultMutableTreeNode  root = (DefaultMutableTreeNode)model.getRoot();
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(dlg.getWorksheetColumn());

      model.insertNodeInto(node, root, root.getChildCount());
      m_tree.setSelectionPath(new TreePath(model.getPathToRoot(node)));
    }
  }

  void deleteColumn() {
    TreePath path = m_tree.getSelectionPath();
    if(path != null){
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
      Object obj = node.getUserObject();

      if(!Misc.getConfirmation())
        return;

      if(obj instanceof WorksheetColumn){
        try {
          while(node.getChildCount() != 0){
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getFirstChild();
            deleteWorksheetJournal(child);
          }
          deleteWorksheetColumn(node);
        }
        catch(Exception ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                        JOptionPane.WARNING_MESSAGE);
        }
      }
      else {
        try {
          deleteWorksheetJournal(node);
        }
        catch(Exception ex) {
          JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                        JOptionPane.WARNING_MESSAGE);
        }
      }
    }
  }

  void deleteWorksheetColumn(DefaultMutableTreeNode node) throws Exception {
    AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
    logic.deleteWorksheetColumn(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
                                ((WorksheetColumn)node.getUserObject()).getIndex());
    ((DefaultTreeModel)m_tree.getModel()).removeNodeFromParent(node);
  }

  void deleteWorksheetJournal(DefaultMutableTreeNode node) throws Exception {
    DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
    //AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
    //logic.deleteAccount(m_sessionid, IDBConstants.MODUL_ACCOUNTING, (Account)node.getUserObject());
    model.removeNodeFromParent(node);
  }

  void insertJournal(Object obj) throws Exception {
    DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
    TreePath path = m_tree.getLeadSelectionPath();

    if(path != null) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
      if((node.getUserObject() instanceof WorksheetColumn)) {
        WorksheetColumn column = (WorksheetColumn)node.getUserObject();

        for(int i = 0; i < node.getChildCount(); i ++) {
          DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
          if(child.getUserObject().toString().equals(obj.toString()))
            throw new Exception(obj + " has included");
        }

        // perintah sql
        AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
        logic.createWorksheetJournal(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
                                     column.getIndex(), ((Journal)obj).getIndex());

        DefaultMutableTreeNode child = new DefaultMutableTreeNode(obj);
        model.insertNodeInto(child, node, node.getChildCount());
        m_tree.scrollPathToVisible(new TreePath(model.getPathToRoot(child)));
      }
    }
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      add();
    }
    else if(e.getSource() == m_deleteBt) {
      delete();
    }
    else if(e.getSource() == mi_add) {
      addColumn();
    }
    else if(e.getSource() == mi_delete) {
      deleteColumn();
    }
  }

  // implements Transferable
  public DataFlavor[] getTransferDataFlavors(){
    return flavors;
  }

  public boolean isDataFlavorSupported(DataFlavor f1){
    return true;
  }

  public Object getTransferData(DataFlavor f1){
    if(f1.equals(flavor))
      return m_list.getSelectedValue();
    else
      return null;
  }

  // implements DragSourceListener
  public void dragEnter(DragSourceDragEvent dsde){
  }

  public void dragOver(DragSourceDragEvent dsde){
  }

  public void dragExit(DragSourceEvent dsde){
  }

  public void dropActionChanged(DragSourceDragEvent dsde){
  }

  public void dragDropEnd(DragSourceDropEvent dsde){
    if(dsde.getDropSuccess())
      System.out.println(" Dropped Succeded");
  }

  // implements DragGestureListener
  public void dragGestureRecognized(DragGestureEvent dge){
    try{
      Transferable transferable = this;
      dge.startDrag(null, transferable);
    }
    catch(Exception ex){
      JOptionPane.showMessageDialog(null, "Pindahkan item ke variant ",
                                    "Informasi",JOptionPane.INFORMATION_MESSAGE);
    }
  }

  // implements DropTargetListener
  public void dragEnter(DropTargetDragEvent e) {
    e.acceptDrag(DnDConstants.ACTION_COPY);
  }

  public void dragOver(DropTargetDragEvent e) {
    e.acceptDrag(DnDConstants.ACTION_COPY);
  }

  public void dragExit(DropTargetEvent e) {
  }

  public void drop(DropTargetDropEvent e) {
    DropTargetContext targetContext = e.getDropTargetContext();

    try {
      DataFlavor[] dataFlavors = e.getCurrentDataFlavors();
      DataFlavor  transferDataFlavor = dataFlavors[0];
      Transferable tr = e.getTransferable();
      Object obj = tr.getTransferData(transferDataFlavor);
      Journal journal = (Journal)obj;

      insertJournal(journal);
      targetContext.dropComplete(true);
    }
    catch(Exception ex){
      targetContext.dropComplete(false);
      if(ex instanceof java.sql.SQLException)
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                      "Warning", JOptionPane.WARNING_MESSAGE);
      else
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                      "Information", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  public void dragScroll(DropTargetDragEvent e) {
  }

  public void dropActionChanged(DropTargetDragEvent e) {
  }

  /**
   *
   */
  class TreeMouseAdapter extends MouseAdapter {
    public void mouseReleased(MouseEvent e) {
      if(e.isPopupTrigger()){
        DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
        TreePath path = m_tree.getSelectionPath();
        if(path != null){
          DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
          if(node == model.getRoot()) {
            mi_add.setEnabled(true);
            mi_delete.setEnabled(false);
          }
          else {
            mi_add.setEnabled(false);
            mi_delete.setEnabled(true);
          }

          Rectangle rectangle = m_tree.getPathBounds(path);
          if( rectangle.contains(e.getPoint()))
            m_popupMenu.show(m_tree, e.getX(), e.getY());
        }
      }
    }
  }
}