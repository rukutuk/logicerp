package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.Misc;

public class JournalStandardPanel extends JPanel implements ActionListener, TreeSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JournalStandardTree m_tree;
  JButton m_addBt, m_editBt, m_deleteBt, m_saveBt, m_cancelBt, m_removeBt;
  JTextField m_parentTextField, m_codeTextField, m_descriptionTextField;
  JRadioButton m_groupRadioButton, m_nonRadioButton;
  JournalStandardTable m_table;
  JComboBox m_balanceComboBox;
  JournalPicker m_journalPicker;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  boolean m_editable = false;
  JournalStandard m_journal = null;
  DefaultMutableTreeNode m_parent = null;
  DefaultMutableTreeNode m_node = null;

  public JournalStandardPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    setEnabled(false);
  }

  void constructComponent() {
    m_groupRadioButton = new JRadioButton("Group");
    m_groupRadioButton.addActionListener(this);
    m_nonRadioButton = new JRadioButton("Not Group");
    m_nonRadioButton.addActionListener(this);
    m_nonRadioButton.setSelected(true);
    ButtonGroup bg = new ButtonGroup();
    bg.add(m_groupRadioButton);
    bg.add(m_nonRadioButton);

    JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    groupPanel.add(m_groupRadioButton);
    groupPanel.add(m_nonRadioButton);

    m_tree = new JournalStandardTree(m_conn, m_sessionid);
    m_tree.addTreeSelectionListener(this);
    m_table = new JournalStandardTable();
    m_balanceComboBox = new JComboBox(JournalStandardAccount.m_balances);

    m_parentTextField = new JTextField();
    m_parentTextField.setEditable(false);
    m_codeTextField = new JTextField();
    m_descriptionTextField = new JTextField();
    m_journalPicker = new JournalPicker(m_conn, m_sessionid);

    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    m_removeBt = new JButton("Remove Account"); // nyelip disini
    m_removeBt.addActionListener(this); // nyelip disini
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_removeBt.setEnabled(false); // nyelip
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JPanel northPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel formPanel = new JPanel();
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JLabel parentLabel = new JLabel("Parent");
    JLabel codeLabel = new JLabel("Code");
    JLabel descriptionLabel = new JLabel("Description");
    JLabel typeLabel = new JLabel("Type");
    JLabel journalLabel = new JLabel("Journal");

    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);
    buttonPanel.add(m_removeBt); // nyelip
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    formPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    formPanel.add(parentLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_parentTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(codeLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_codeTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(descriptionLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_descriptionTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(typeLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(groupPanel, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(journalLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_journalPicker, gridBagConstraints);

    northPanel.setLayout(new BorderLayout());
    northPanel.add(buttonPanel, BorderLayout.NORTH);
    northPanel.add(formPanel, BorderLayout.CENTER);

    centerPanel.setLayout(new BorderLayout(5, 5));
    centerPanel.add(northPanel, BorderLayout.NORTH);
    centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);

    splitPane.setLeftComponent(new JScrollPane(m_tree));
    splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    splitPane.setRightComponent(centerPanel);
    splitPane.setDividerLocation(300);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
  }

  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    m_codeTextField.setEditable(enabled);
    m_descriptionTextField.setEditable(enabled);
    m_groupRadioButton.setEnabled(enabled);
    m_nonRadioButton.setEnabled(enabled);

    if(enabled) {
      if(m_groupRadioButton.isSelected())
        m_journalPicker.setEnabled(false);
      else
        m_journalPicker.setEnabled(true);
    }
    else
      m_journalPicker.setEnabled(false);
  }

  void clear() {
    m_parentTextField.setText("");
    m_codeTextField.setText("");
    m_descriptionTextField.setText("");
    m_nonRadioButton.setSelected(true);
    m_journalPicker.setObject(null);
    m_table.clear();
  }

  void add() {
    DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
    TreePath path = m_tree.getSelectionPath();
    m_parent = (DefaultMutableTreeNode)path.getLastPathComponent();
    m_new = true;
    m_editable = true;

    clear();
    if(m_parent != model.getRoot())
      m_parentTextField.setText(((JournalStandard)m_parent.getUserObject()).getDescription());
    else
      m_parentTextField.setText(m_parent.getUserObject().toString());

    m_table.addRow();

    setEnabled(true);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_removeBt.setEnabled(true);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
  }

  void edit() {
    //DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
    TreePath path = m_tree.getSelectionPath();
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
    m_parent = (DefaultMutableTreeNode)node.getParent();
    m_journal = (JournalStandard)node.getUserObject();
    m_edit = true;
    m_editable = true;

    if(!m_journal.isGroup())
      m_table.insertRow();

    setEnabled(true);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_removeBt.setEnabled(true);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
  }

  void delete() {
    TreePath path = m_tree.getSelectionPath();
    if(!Misc.getConfirmation())
      return;

    if(path != null) {
      try {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        deleteNodeParent(node);
      }
      catch(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                      "Warning", JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  void deleteNodeParent(DefaultMutableTreeNode parent) throws Exception{
    while(parent.getChildCount() != 0){
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)parent.getFirstChild();
      deleteNodeParent(node);
    }
    deleteNode(parent);
  }

  void deleteNode(DefaultMutableTreeNode node) throws Exception {
    AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
    logic.deleteJournalStandard(m_sessionid, IDBConstants.MODUL_MASTER_DATA, ((JournalStandard)node.getUserObject()).getIndex());
    ((DefaultTreeModel)m_tree.getModel()).removeNodeFromParent(node);
  }

  void save() {
    DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
    JournalStandard journal = null;

    try {
      journal = getJournalStandard();
      if(m_parent != model.getRoot())
        journal.setParent((JournalStandard)m_parent.getUserObject());
      journal.setJournalStandardAccount(m_table.getJournalStandardAccount());
    }
    catch(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      if(m_new) {
        journal = logic.createJournalStandard(m_sessionid, IDBConstants.MODUL_MASTER_DATA, journal);
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(journal);
        model.insertNodeInto(child, m_parent, m_parent.getChildCount());
        m_tree.setSelectionPath(new TreePath(model.getPathToRoot((TreeNode)child)));
      }
      else {
        journal = logic.updateJournalStandard(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_journal.getIndex(), journal);
        m_node.setUserObject(journal);
        model.nodeChanged(m_node);
        m_table.setJournalStandardAccount(journal.getJournalStandardAccount());
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_new = false;
    m_edit = false;
    m_editable = false;
    setEnabled(false);
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_deleteBt.setEnabled(true);
    m_removeBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void cancel() {
    if(m_new) {
      setSelectedObject(m_parent.getUserObject());
    }
    else {
      setSelectedObject(m_journal);
    }
    m_editable = false;
  }

  void setSelectedObject(Object obj) {
    if(obj instanceof JournalStandard) {
      JournalStandard journal = (JournalStandard)obj;
      setJournalStandard(journal);

      if(journal.isGroup()) {
        m_addBt.setEnabled(true);
        m_table.clear();
      }
      else {
        m_addBt.setEnabled(false);
        setJournalStandardAccount(journal);
      }

      m_editBt.setEnabled(true);
      m_deleteBt.setEnabled(true);
    }
    else {
      clear();
      m_addBt.setEnabled(true);
      m_editBt.setEnabled(false);
      m_deleteBt.setEnabled(false);
    }

    m_new = false;
    m_edit = false;
    setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void objectViewNoSelection() {
    clear();
  }

  void setJournalStandardAccount(JournalStandard journal) {
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      JournalStandardAccount[] account = logic.getJournalStandardAccount(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, journal.getIndex());
      journal.setJournalStandardAccount(account);
      m_table.setJournalStandardAccount(account);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setJournalStandard(JournalStandard journal) {
	  m_journal = journal;
	  if(journal.getParent() != null)
		  m_parentTextField.setText(journal.getParent().getDescription());
	  else
		  m_parentTextField.setText("");
	  m_codeTextField.setText(journal.getCode());
	  m_descriptionTextField.setText(journal.getDescription());
	  if(journal.isGroup())
		  m_groupRadioButton.setSelected(true);
	  else
		  m_nonRadioButton.setSelected(true);
	  m_journalPicker.setObject(journal.getJournal());
  }

  JournalStandard getJournalStandard() throws Exception {
    java.util.ArrayList list = new java.util.ArrayList();
    String code = m_codeTextField.getText().trim();
    if(code.equals(""))
      list.add("Code");
    String description = m_descriptionTextField.getText().trim();
    if(description.equals(""))
      list.add("Description");
    boolean isgroup = false;

    if(m_groupRadioButton.isSelected())
      isgroup = true;

    Journal journal = (Journal)m_journalPicker.getObject();
    if(!isgroup && journal == null)
      list.add("Journal");

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += exception[i] + "\n";
      throw new Exception(strexc);
    }

    return new JournalStandard(code, description, isgroup, journal);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      add();
    }
    else if(e.getSource() == m_editBt) {
      edit();
    }
    else if(e.getSource() == m_deleteBt) {
      delete();
    }
    else if(e.getSource() == m_removeBt) {
    	removeAccount();
    }
    else if(e.getSource() == m_saveBt) {
      save();
    }
    else if(e.getSource() == m_cancelBt) {
      cancel();
    }
    else if(e.getSource() == m_groupRadioButton) {
      m_journalPicker.setEnabled(false);
      m_table.clear();
    }
    else if(e.getSource() == m_nonRadioButton) {
      if(m_edit) {
        if(m_node.getChildCount() > 0) {
          JOptionPane.showMessageDialog(this, "This node can't be updated,\n" +
                                        "because it's have one or more child");
          m_groupRadioButton.setSelected(true);
          return;
        }
      }

      m_journalPicker.setEnabled(true);
      if(m_journal != null)
        m_table.setJournalStandardAccount(m_journal.getJournalStandardAccount());
      m_table.insertRow();
    }
  }

  private void removeAccount() {
	  int rows[] = m_table.getSelectedRows();
	  if(rows.length==0)
		  return;
	  
	  int row = rows[0];
	  int opt = JOptionPane.showConfirmDialog(this, "Are you sure to remove this account?", 
			  "Account Removal", JOptionPane.YES_NO_OPTION);
	  if(opt==JOptionPane.YES_OPTION){
		  //delete
		  DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		  model.removeRow(row);
	  }
  }

public void valueChanged(TreeSelectionEvent e) {
    TreePath path = e.getNewLeadSelectionPath();
    if(path != null) {
      m_node = (DefaultMutableTreeNode)path.getLastPathComponent();
      setSelectedObject(m_node.getUserObject());
    }
    else
      objectViewNoSelection();
  }

  /**
   *
   */
  class JournalStandardTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JournalStandardTable() {
      JournalStandardTableModel model = new JournalStandardTableModel();
      model.addColumn("Description");
      model.addColumn("Account Code");
      model.addColumn("Normal Balance");
      model.addColumn("Hidden");
      model.addColumn("Calculate");
      setModel(model);
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 0)
        return new AccountTreeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            m_conn, m_sessionid);
      else if(col == 2)
        return new DefaultCellEditor(m_balanceComboBox);
      return super.getCellEditor(row, col);
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    void addRow() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{});
    }

    void insertRow() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.insertRow(getRowCount(), new Object[]{});
    }

    void setJournalStandardAccount(JournalStandardAccount[] account) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < account.length; i ++)
        model.addRow(new Object[]{
      account[i], account[i].getAccount().getCode(), account[i].getBalanceAsString(),
      new Boolean(account[i].isHidden()), new Boolean(account[i].isCalculate())
      });
    }

    JournalStandardAccount[] getJournalStandardAccount() throws Exception {
    	Vector vresult = new Vector();
    	
    	for(int i = 0; i < getRowCount() -1; i ++) {
    		Object obj = getValueAt(i, 0);
    		Account account = null;
    		
    		if(obj instanceof JournalStandardAccount)
    			account = ((JournalStandardAccount)obj).getAccount();
    		else if(obj instanceof Account)
    			account = (Account)obj;
    		
    		String strbalance = (String)getValueAt(i, 2);
    		short balance = JournalStandardAccount.balanceFromStringToID(strbalance);
    		
    		boolean ishidden = ((Boolean)getValueAt(i, 3)).booleanValue();
    		boolean iscalculate = ((Boolean)getValueAt(i, 4)).booleanValue();
    		
    		vresult.addElement(new JournalStandardAccount(account, balance, ishidden, iscalculate));
    	}
    	
    	JournalStandardAccount[] result = new JournalStandardAccount[vresult.size()];
    	vresult.copyInto(result);
    	return result;
    }
  }

  /**
   *
   */
  class JournalStandardTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 1)
        return false;
      if(m_editable) {
        if(row == getRowCount() - 1){
          switch( col ) {
            case 0:
              return true;
            case 2:
              return false;
            case 3:
              return false;
            case 4:
              return false;
          }
        }
        return true;
      }
      return false;
    }

    public Class getColumnClass(int col) {
      if(col == 3 || col == 4)
        return Boolean.class;
      return super.getColumnClass(col);
    }

    public void setValueAt(Object obj, int row, int col) {
      if(col == 0) {
        if(obj instanceof Account)
          addAccount(obj, row);
        else
          super.setValueAt(obj, row, col);
      }
      else
        super.setValueAt(obj, row, col);
    }

    void addAccount(Object object, int rowindex) {
      boolean bduplicate = false;
      int i;
      //, lastrow;

      Account account = (Account)object;
      for(i = 0; i < getRowCount() - 1; i++ ) {
        if(rowindex != i) {
          Account compaccount = null;
          Object obj = getValueAt(i, 0);
          if(obj instanceof JournalStandardAccount)
            compaccount = ((JournalStandardAccount)obj).getAccount();
          else
            compaccount = (Account)obj;

          if(account.getCode().equals(compaccount.getCode())) {
            bduplicate = true;
            break;
          }
        }
      }

      i++;
      if(bduplicate) {
        JOptionPane.showMessageDialog(JournalStandardPanel.this, account.getName() + " has defined at line " + i);
        return;
      }

      Object obj = getValueAt(rowindex, 1);
      super.setValueAt(new Account(account, Account.DESCRIPTION), rowindex, 0);
      super.setValueAt(account.getCode(), rowindex, 1);
      super.setValueAt(new Boolean(false), rowindex, 3);
      super.setValueAt(new Boolean(false), rowindex, 4);

      if(obj == null)
        super.addRow(new Object[]{});
    }
  }
}
/*
class AccountMouseAdapter extends MouseAdapter {
    public void mouseReleased(MouseEvent e) {
      if(e.isPopupTrigger() && (m_newTransaction || m_editTransaction)){
        int row = m_balanceTable.getSelectedRow();
        int lastRow = m_balanceTable.getRowCount() - 2;

        if(row < lastRow) {
          m_popup.show(m_balanceTable, e.getX(), e.getY());
          m_lastpopupX = e.getX();
          m_lastpopupY = e.getY();
        }
      }
    }
  }

void onDeleteRow() {
    BalanceTableModel model = (BalanceTableModel)m_balanceTable.getModel();
    int row = m_balanceTable.rowAtPoint(new Point(m_lastpopupX, m_lastpopupY));

    model.removeRow(row);
    model.updateContent(true);
  }
*/