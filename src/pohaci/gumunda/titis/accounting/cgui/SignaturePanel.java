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
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;

public class SignaturePanel extends JPanel implements ActionListener, TreeSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JButton m_editBt, m_saveBt, m_cancelBt, m_removeBt;
  JTree m_tree;
  SignatureTable m_table;
  JComboBox m_statusComboBox;
  boolean m_edit = false;

  DefaultMutableTreeNode m_node;

  public SignaturePanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    initData();
  }

  void constructComponent() {
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_removeBt = new JButton("Remove");
    m_removeBt.addActionListener(this);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    m_tree = new JTree(new DefaultMutableTreeNode("Application"));
    m_tree.setCellRenderer(new SignatureTreeCellRenderer());
    m_tree.addTreeSelectionListener(this);
    m_table = new SignatureTable();
    m_statusComboBox = new JComboBox(Signature.m_atype);

    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_removeBt);
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(buttonPanel, BorderLayout.NORTH);
    centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);

    splitPane.setDividerLocation(200);
    splitPane.setLeftComponent(new JScrollPane(m_tree));
    splitPane.setRightComponent(centerPanel);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);

    initComponent();
  }

  void initComponent() {
    m_editBt.setEnabled(false);
    m_removeBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void initData() {
    String[] apps = new String[]{
      IConstants.RECEIVE_EXPENSE_SHEET_DIFFERENCE,
      IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER,
      IConstants.RECEIVE_EMPLOYEE_RECEIVABLE,
      IConstants.RECEIVE_LOAN,
      IConstants.RECEIVE_OTHERS,
      IConstants.PAYMENT_PROJECT_COST,
      IConstants.PAYMENT_OPERASIONAL_COST,
      IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT,
      IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_INSTALLMENT,
      IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT,
      IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_RECEIVE,
      IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS,
      IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_INSTALLMENT,
      IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT,
      IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_RECEIVE,
      IConstants.PAYMENT_CASHADVANCE_PROJECT,
      IConstants.PAYMENT_CASHADVANCE_OTHERS,
      IConstants.PAYMENT_EXPENSESHEET_DIFFERENCE,
      IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
      IConstants.PAYMENT_EMPLOYEE_RECEIVABLE,
      IConstants.PAYMENT_LOAN,
      IConstants.PAYMENT_OTHERS,
      
      IConstants.SALES_ADVANCE,
      IConstants.SALES_INVOICE,
      IConstants.SALES_AR_RECEIVE,
      
      IConstants.PURCHASE_RECEIPT,
      IConstants.PURCHASE_AP_PAYMENT,
      
      IConstants.PAYROLL_VERIFICATION_PAYCHEQUES,     
      IConstants.PAYROLL_VERIFICATION_MEAL_ALLOWANCES,
      IConstants.PAYROLL_VERIFICATION_TRANSPORTATION_ALLOWANCES,
      IConstants.PAYROLL_VERIFICATION_OVERTIME,
      IConstants.PAYROLL_VERIFICATION_OTHER_ALLOWANCE,
      IConstants.PAYROLL_VERIFICATION_INSURANCE_ALLOWANCE,
      IConstants.PAYROLL_VERIFICATION_TAX21,
      
      IConstants.PAYROLL_PAYMENT_SALARY_HO,
      IConstants.PAYROLL_PAYMENT_SALARY_UNIT,
      IConstants.PAYROLL_PAYMENT_TAX21_HO,
      IConstants.PAYROLL_PAYMENT_TAX21_UNIT,
      IConstants.PAYROLL_PAYMENT_EMPLOYEE_INSURANCE,
      
      IConstants.EXPENSE_SHEET_PROJECT,
      IConstants.EXPENSE_SHEET_OTHERS,
      
      IConstants.MJ_STANDARD,
      IConstants.MJ_NONSTANDARD_PROJECT,
      IConstants.MJ_NONSTANDARD_OTHERS,
    };
    DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
    for(int i = 0; i < apps.length; i ++) {
      model.insertNodeInto(new DefaultMutableTreeNode(apps[i]), root, root.getChildCount());
    }
    m_tree.expandPath(new TreePath(model.getPathToRoot(root)));
  }

  void edit() {
    m_edit = true;
    m_editBt.setEnabled(false);
    m_removeBt.setEnabled(true);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_table.insertRow();
  }

  void save() {
    TreePath path = m_tree.getSelectionPath();
    if(path == null)
      return;

    DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
    String app = (String)node.getUserObject();
    Signature[] signature = new Signature[0];
    try {
      signature = m_table.getSignature();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.createSignature(m_sessionid, IDBConstants.MODUL_MASTER_DATA, app, signature);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_table.setSignature(signature);
    m_edit = false;
    m_editBt.setEnabled(true);
    m_removeBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void cancel() {
    if(m_node != null)
      setSelectedObject(m_node.getUserObject());
    m_edit = false;
    m_editBt.setEnabled(true);
    m_removeBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void setSelectedObject(Object obj) {
    m_edit = false;
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    setSignature((String)obj);
  }

  void objectViewNoSelection() {
    m_edit = false;
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_table.clear();
  }

  void setSignature(String app) {
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Signature[] signature = logic.getSignature(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, app);
      m_table.setSignature(signature);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_editBt) {
      edit();
    }
    else if(e.getSource() == m_saveBt) {
      save();
    }
    else if(e.getSource() == m_cancelBt) {
      cancel();
    }
    else if(e.getSource() == m_removeBt) {
    	remove();
    }
  }

  private void remove() {
	int rows[] = m_table.getSelectedRows();
	if(rows.length==0)
		return;
	
	int row = rows[0];
	int opt = JOptionPane.showConfirmDialog(this, "Are you sure to remove this signature type",
			"Signature Type Removal", JOptionPane.YES_NO_OPTION);
	if(opt==JOptionPane.YES_OPTION){
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		model.removeRow(row);
	}
  }

public void valueChanged(TreeSelectionEvent e) {
    TreePath path = e.getNewLeadSelectionPath();
    DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
    if(path != null) {
      m_node = (DefaultMutableTreeNode)path.getLastPathComponent();
      if(m_node == (DefaultMutableTreeNode)model.getRoot())
        objectViewNoSelection();
      else
        setSelectedObject(m_node.getUserObject());
    }
    else
      objectViewNoSelection();
  }

  /**
   *
   */
  class SignatureTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SignatureTable() {
      SignatureTableModel model = new SignatureTableModel();
      model.addColumn("Type");
      model.addColumn("Employee");
      setModel(model);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 0)
        return new DefaultCellEditor(m_statusComboBox);
      else if(col == 1)
        return new EmployeeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                      m_conn, m_sessionid);
      return super.getCellEditor(row, col);
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }

    void insertRow() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.insertRow(getRowCount(), new Object[]{});
    }

    void setSignature(Signature[] signature) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < signature.length; i ++)
        model.addRow(new Object[]{signature[i].getTypeAsString(), signature[i].getEmployee()});
    }

    public Signature[] getSignature() throws Exception {
      java.util.Vector vresult = new java.util.Vector();
      int row = getRowCount();
      for(int i = 0; i < row - 1; i ++) {
        String strstatus = (String)getValueAt(i, 0);
        short type = Signature.typeFromStringToID(strstatus);
        SimpleEmployee employee = (SimpleEmployee)getValueAt(i, 1);
        if(employee == null)
          throw new Exception("Please insert employee at line " + (i + 1));
        vresult.addElement(new Signature(type, new SimpleEmployee(employee.getIndex(),
            employee.getFirstName(), employee.getMidleName(), employee.getLastName())));
      }

      Signature[] result = new Signature[vresult.size()];
      vresult.copyInto(result);
      return result;
    }
  }

  /**
   *
   */
  class SignatureTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(m_edit) {
        if(row == getRowCount() - 1){
          switch( col ) {
            case 0:
              return true;
            case 1:
              return false;
          }
        }
        return true;
      }
      return false;
    }

    public void setValueAt(Object obj, int row, int col) {
      if(col == 0) {
        if(obj instanceof String)
          addSignature(obj, row);
        else
          super.setValueAt(obj, row, col);
      }
      else super.setValueAt(obj, row, col);
    }

    void addSignature(Object object, int rowindex) {
      boolean bduplicate = false;
      int i;//, lastrow;

      String stritem = (String)object;
      for(i = 0; i < getRowCount() - 1; i++ ) {
        if(rowindex != i) {
          String strcompitem = (String)getValueAt(i, 0);
          if(stritem.equals(strcompitem)) {
            bduplicate = true;
            break;
          }
        }
      }

      i++;
      if(bduplicate) {
        JOptionPane.showMessageDialog(SignaturePanel.this, stritem + " has defined at line " + i);
        return;
      }

      Object obj = getValueAt(rowindex, 0);
      super.setValueAt(stritem, rowindex, 0);
      if(obj == null)
        super.addRow(new Object[]{});
    }
  }

  /**
   *
   */
  class SignatureTreeCellRenderer extends JLabel implements TreeCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final Color m_selectedBackgroundColor = new Color(156, 138, 206);
    protected boolean m_selected = false;

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      TreeModel tmodel = tree.getModel();
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
      String  stringValue = tree.convertValueToText(value, selected,
          expanded, leaf, row, hasFocus);
      setText(stringValue);
      setToolTipText(stringValue);

      if(node == tmodel.getRoot())
        setIcon(null);
      else
        setIcon(new ImageIcon("../images/star_green.gif"));

      if(hasFocus)
        setForeground(new Color(0, 0, 128));
      else
        setForeground(Color.black);

      m_selected = selected;
      return this;
    }

    public void paint(Graphics g) {
      Color            bColor;
      Icon             currentI = getIcon();

      if(m_selected)
        bColor = new Color(206, 206, 255);
      else if(getParent() != null)
        bColor = getParent().getBackground();
      else
        bColor = getBackground();

      g.setColor(bColor);
      if(currentI != null && getText() != null) {
        int          offset = (currentI.getIconWidth() + getIconTextGap());

        g.fillRect(offset - 1, 0, getWidth() - offset + 1,
                   getHeight());
      }
      else
        g.fillRect(0, 0, getWidth(), getHeight());

      super.paint(g);
    }
  }
}