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
import javax.swing.table.*;

import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class SearchCustomerDetailDlg extends SearchCustomerDlg {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
CustomerTable m_detailtable;
  JButton m_selectBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  Customer[] m_customer = new Customer[0];

  public SearchCustomerDetailDlg(JFrame owner, Connection conn, long sessionid) {
    super(owner, null, conn, sessionid);
    setModal(true);
    setSize(450, 600);
    find();
  }

  void constructComponent() {
    m_detailtable = new CustomerTable();

    m_detailtable.addMouseListener(new MouseAdapter()  {
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() >= 2) {
				onAdd();
			}
		}
	});
    m_selectBt = new JButton("Select");
    m_selectBt.addActionListener(this);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_selectBt);

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(new JScrollPane(m_detailtable), BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().add(criteriaPanel(), BorderLayout.NORTH);
    getContentPane().add(centerPanel, BorderLayout.CENTER);
  }

  void find() {
    String query = "";
    try {
      query = m_table.getCriterion();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_detailtable.setCustomer(logic.getCustomerByCriteria(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, query));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void clear() {
    super.clear();
    m_detailtable.clear();
  }

  public int getResponse() {
    return m_iResponse;
  }

  public Customer[] getCustomer() {
    return m_customer;
  }

  void onAdd() {
    int[] row = m_detailtable.getSelectedRows();
    java.util.Vector vresult = new java.util.Vector();
    for(int i = 0; i < row.length; i ++) {
      Customer customer = (Customer)m_detailtable.getValueAt(row[i], 1);
      vresult.addElement(new Customer(customer.getIndex(), customer));
    }
    m_customer = new Customer[vresult.size()];
    vresult.copyInto(m_customer);
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if(e.getSource() == m_selectBt) {
      onAdd();
    }
  }

  /**
   *
   */
  class CustomerTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerTable() {
      CustomerTableModel model = new CustomerTableModel();
      model.addColumn("No");
      model.addColumn("Code");
      model.addColumn("Name");
      model.addColumn("Group");
      model.addColumn("Address");
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
    }

    void setCustomer(Customer[] customer) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < customer.length; i ++) {
        OtherCustomer cust = new OtherCustomer(customer[i].getIndex(), customer[i]);
        model.addRow(new Object[]{
        String.valueOf((i + 1)), cust,
        cust.getName(), cust.getCompanyToString(), cust.getAddress(),
      });
      }
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }
  }

  /**
   *
   */
  class CustomerTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }

  /**
   *
   */
  class OtherCustomer extends Customer {
    public OtherCustomer(long index, Customer customer) {
      super(index, customer);
    }

    public String toString() {
      return m_code;
    }
  }
}