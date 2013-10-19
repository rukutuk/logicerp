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
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import pohaci.gumunda.cgui.IntegerCellEditor;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class JournalStandardSettingPanel extends JPanel implements ActionListener, TreeSelectionListener {
  /**
	 *
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JButton m_editBt, m_saveBt, m_cancelBt, m_removeBt;
  JTree m_tree;
  SettingTable m_table;
  boolean m_edit = false;

  DefaultMutableTreeNode m_node;

  private Hashtable htAttribute;

  private ArrayList m_appList;

  public JournalStandardSettingPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    initData();
  }

  void constructComponent() {
	initAttribute();

    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_removeBt = new JButton("Remove");
    m_removeBt.addActionListener(this);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    m_tree = new JTree(new DefaultMutableTreeNode("Application"));
    m_tree.setCellRenderer(new DefaultTreeCellRenderer());
    m_tree.addTreeSelectionListener(this);
    m_table = new SettingTable();

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

  private void initAttribute() {
	  htAttribute = new Hashtable();

	  // WARNING!!!
	  // HARUS DIPETAKEN DISINI
	  // ANTARA NO URUT APLIKASI DI INITDATA()
	  // VS ISI COMBO BOX BERDASARKAN ICONSTANTS

	  put(new Integer(0), IConstants.ATTR_RCV);
	  put(new Integer(1), IConstants.ATTR_RCV);
	  put(new Integer(2), IConstants.ATTR_RCV);
	  put(new Integer(3), IConstants.ATTR_RCV);
	  put(new Integer(4), IConstants.ATTR_RCV);
	  put(new Integer(5), IConstants.ATTR_PMT);
	  put(new Integer(6), IConstants.ATTR_PMT);

	  put(new Integer(7), IConstants.ATTR_INSTALL);
	  put(new Integer(8), IConstants.ATTR_CA);
	  put(new Integer(9), IConstants.ATTR_INSTALL);
	  put(new Integer(10), IConstants.ATTR_CA);
	  	  //Tambahan cok gung ketika coba CashAdvance General
	  put(new Integer(11), IConstants.ATTR_PMT);
	  put(new Integer(12), IConstants.ATTR_PMT);
	  //=====Ini untuk ES payment
	  put(new Integer(13), IConstants.ATTR_PMT);

	  //======================
	  put(new Integer(14), IConstants.ATTR_BANKCASH_TRANSFER);
	  put(new Integer(15), IConstants.ATTR_PMT);
	  put(new Integer(16), IConstants.ATTR_PMT_LOAN);
	  put(new Integer(17), IConstants.ATTR_PMT);

	  put(new Integer(18), IConstants.ATTR_CUSTOMER_STATUS);
	  //put(new Integer(19), IConstants.ATTR_INVOICE);
	  put(new Integer(20), IConstants.ATTR_AR_RCV);

	  put(new Integer(21), IConstants.ATTR_PURCHASE_RECEIPT);
	  put(new Integer(22), IConstants.ATTR_PMT);

	  put(new Integer(31), IConstants.ATTR_PMT);
	  put(new Integer(32), IConstants.ATTR_PMT);
	  put(new Integer(33), IConstants.ATTR_PMT);
	  put(new Integer(34), IConstants.ATTR_PMT);


  }

  private void put(Integer index, String[] attribute){
	  htAttribute.put(index, attribute);
  }

void initComponent() {
    m_editBt.setEnabled(false);
    m_removeBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void initData() {
    String[] apps = new String[]{
      IConstants.RECEIVE_EXPENSE_SHEET_DIFFERENCE,  //0
      IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER,
      IConstants.RECEIVE_EMPLOYEE_RECEIVABLE,
      IConstants.RECEIVE_LOAN,
      IConstants.RECEIVE_OTHERS,

      IConstants.PAYMENT_PROJECT_COST, // 5
      IConstants.PAYMENT_OPERASIONAL_COST,
      IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_INSTALLMENT,
      IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT,
      IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_INSTALLMENT,
      IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_SETTLEMENT,
      IConstants.PAYMENT_CASHADVANCE_PROJECT,
      IConstants.PAYMENT_CASHADVANCE_OTHERS,
      IConstants.PAYMENT_EXPENSESHEET_DIFFERENCE,
      IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
      IConstants.PAYMENT_EMPLOYEE_RECEIVABLE,
      IConstants.PAYMENT_LOAN,
      IConstants.PAYMENT_OTHERS,

      IConstants.SALES_ADVANCE, // 18
      IConstants.SALES_INVOICE,
      IConstants.SALES_AR_RECEIVE,

      IConstants.PURCHASE_RECEIPT, // 21
      IConstants.PURCHASE_AP_PAYMENT,

      IConstants.PAYROLL_VERIFICATION_PAYCHEQUES, // 23
      IConstants.PAYROLL_VERIFICATION_MEAL_ALLOWANCES,
      IConstants.PAYROLL_VERIFICATION_TRANSPORTATION_ALLOWANCES,
      IConstants.PAYROLL_VERIFICATION_OVERTIME,
      IConstants.PAYROLL_VERIFICATION_OTHER_ALLOWANCE,
      IConstants.PAYROLL_VERIFICATION_INSURANCE_ALLOWANCE,
      IConstants.PAYROLL_VERIFICATION_TAX21,

      IConstants.PAYROLL_PAYMENT_SALARY_HO, // 30
      IConstants.PAYROLL_PAYMENT_SALARY_UNIT,
      IConstants.PAYROLL_PAYMENT_TAX21_HO,
      IConstants.PAYROLL_PAYMENT_TAX21_UNIT,
      IConstants.PAYROLL_PAYMENT_EMPLOYEE_INSURANCE,

      IConstants.EXPENSE_SHEET_PROJECT, // 35
      IConstants.EXPENSE_SHEET_OTHERS,

      IConstants.MJ_STANDARD, // 37
      IConstants.MJ_NONSTANDARD_PROJECT,
      IConstants.MJ_NONSTANDARD_OTHERS,

      IConstants.CLOSING_TRANSACTION, // 40
      IConstants.VOID_TRANSACTION
      };
    DefaultTreeModel model = (DefaultTreeModel)m_tree.getModel();
    DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
    for(int i = 0; i < apps.length; i ++) {
      model.insertNodeInto(new DefaultMutableTreeNode(apps[i]), root, root.getChildCount());
    }

    // i add this
    m_appList = new ArrayList();

    for(int i=0; i<apps.length; i++){
    	m_appList.add(i, apps[i]);
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
    JournalStandardSetting[] journal = new JournalStandardSetting[0];
    try {
      journal = m_table.getJournalStandarSetting();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.createJournalStandardSetting(m_sessionid, IDBConstants.MODUL_MASTER_DATA, app, journal);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_table.setJournalStandardSetting(journal);
    m_edit = false;
    m_editBt.setEnabled(true);
    m_removeBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void cancel() {
    if(m_node != null)
      setSelectedObject(m_node.getUserObject());
  }

  void setSelectedObject(Object obj) {
    m_edit = false;
    m_editBt.setEnabled(true);
    m_removeBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    setJournalStandardSetting((String)obj);
  }

  void objectViewNoSelection() {
    m_edit = false;
    m_editBt.setEnabled(false);
    m_removeBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_table.clear();
  }

  void setJournalStandardSetting(String app) {
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      JournalStandardSetting[] journal = logic.getJournalStandardSetting(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, app);
      m_table.setJournalStandardSetting(journal);
    }
    catch(Exception ex) {
      ex.printStackTrace();
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
	int opt = JOptionPane.showConfirmDialog(this, "Are you sure to remove this setting?",
			"Setting Removal", JOptionPane.YES_NO_OPTION);
	if(opt==JOptionPane.YES_OPTION){
		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
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
  class SettingTable extends JTable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public SettingTable() {
      SettingTableModel model = new SettingTableModel();
      model.addColumn("Number");
      model.addColumn("Standard Journal");
      model.addColumn("Attribute");
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 0)
        return new IntegerCellEditor();
      else if(col == 1)
        return new JournalStandardCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            m_conn, m_sessionid);
      else if(col == 2){
    	  int idx = check();
    	  if(idx!=-1){

				try {
					JComboBox cbUsed = new JComboBox((String[]) htAttribute.get(new Integer(idx)));
					return new DefaultCellEditor(cbUsed);
				} catch (RuntimeException e) {
					//e.printStackTrace();
				}
				 return super.getCellEditor(row, col);
    	  }
    	  else
    		  return super.getCellEditor(row, col);
      }
      return super.getCellEditor(row, col);
    }

    private int check() {
    	TreePath path = m_tree.getSelectionPath();
        if(path == null)
        	return -1;

    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
    	int index = getIndex((String) node.getUserObject());

		return index;
	}

	private int getIndex(String string) {
		return m_appList.indexOf(string);
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

    void setJournalStandardSetting(JournalStandardSetting[] journal) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < journal.length; i ++)
        model.addRow(new Object[]{new Integer(journal[i].getNumber()), journal[i].getJournalStandard(), journal[i].getAttribute()});
    }

    public JournalStandardSetting[] getJournalStandarSetting() throws Exception {
      java.util.Vector vresult = new java.util.Vector();
      int row = getRowCount();
      for(int i = 0; i < row - 1; i ++) {
        short status = ((Integer)getValueAt(i, 0)).shortValue();
        JournalStandard journal = (JournalStandard)getValueAt(i, 1);
        if(journal == null)
          throw new Exception("Please insert standard journal at line " + (i + 1));
        String attribute = (String)getValueAt(i, 2);
        vresult.addElement(new JournalStandardSetting(status, journal, attribute));
      }

      JournalStandardSetting[] result = new JournalStandardSetting[vresult.size()];
      vresult.copyInto(result);
      return result;
    }

	public boolean isCellEditable(int row, int column) {
		if(m_edit){
			if(column == 0)
				return false;
			else if (column == 1)
				return true;
			else if (column == 2) {
				Object obj = getValueAt(row, 1);
				if (obj != null) {
						int idx = check();
						if (idx != -1) {
							Object[] string = (String[]) htAttribute
									.get(new Integer(idx));
							if (string != null)
								return true;
							return false;
						}
					}
				}else{
					return false;
				}
			return false;
		}
		return false;
	}
  }

  /**
   *
   */
  class SettingTableModel extends DefaultTableModel {
    /*public boolean isCellEditable(int row, int col) {
      if(m_edit) {
        if(row == getRowCount() - 1){
          switch( col ) {
            case 0:
              return false;
            case 1:
              return true;
          }
        }
        return true;
      }
      return false;
    }*/

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void setValueAt(Object obj, int row, int col) {
      if(col == 1) {
        if(obj instanceof JournalStandard)
          addJournalStandard(obj, row);
        else
          super.setValueAt(obj, row, col);
      }
      else super.setValueAt(obj, row, col);
    }

    void addJournalStandard(Object object, int rowindex) {
      boolean bduplicate = false;
      int i;//, lastrow;

      JournalStandard journal = (JournalStandard)object;
      for(i = 0; i < getRowCount() - 1; i++ ) {
        if(rowindex != i) {
          JournalStandard comp = (JournalStandard)getValueAt(i, 1);
          if(journal.getIndex() == comp.getIndex()) {
            bduplicate = true;
            break;
          }
        }
      }

      i++;
      if(bduplicate) {
        JOptionPane.showMessageDialog(m_table, journal + " has defined at line " + i);
        return;
      }

      Object obj = getValueAt(rowindex, 0);
      super.setValueAt(new Integer(rowindex + 1), rowindex, 0);
      super.setValueAt(journal, rowindex, 1);
      if(obj == null)
        super.addRow(new Object[]{});
    }
  }

  /**
   *
   */
  class DefaultTreeCellRenderer extends JLabel implements TreeCellRenderer {
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
        setIcon(new ImageIcon("../images/star_red.gif"));

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
