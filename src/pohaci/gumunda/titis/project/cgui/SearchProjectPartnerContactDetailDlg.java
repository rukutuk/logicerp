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

public class SearchProjectPartnerContactDetailDlg extends SearchProjectPartnerContactDlg {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
PersonalTable m_detailtable;
  JButton m_addBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  Personal[] m_personal = new Personal[0];

  public SearchProjectPartnerContactDetailDlg(JFrame owner, Connection conn, long sessionid, Partner partner) {
    super(owner, null, conn, sessionid, partner);
    setModal(true);
    setSize(450, 600);
    find();
  }

  void constructComponent() {
    m_detailtable = new PersonalTable();
    m_addBt = new JButton("Select");
    m_addBt.addActionListener(this);

    JPanel centerPanel = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_addBt);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(new JScrollPane(m_detailtable), BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().add(criteriaPanel(), BorderLayout.NORTH);
    getContentPane().add(centerPanel, BorderLayout.CENTER);
  }

  public int getResponse() {
    return m_iResponse;
  }

  public Personal[] getPersonal() {
    return m_personal;
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
      m_detailtable.setPersonal(logic.getPersonalByCriteria(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, query));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void onAdd() {
    int[] row = m_detailtable.getSelectedRows();
    java.util.Vector vresult = new java.util.Vector();
    for(int i = 0; i < row.length; i ++) {
      Personal personal = (Personal)m_detailtable.getValueAt(row[i], 1);
      vresult.addElement(new OtherPersonal(personal.getIndex(), personal));
    }

    m_personal = new Personal[vresult.size()];
    vresult.copyInto(m_personal);

    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if(e.getSource() == m_addBt) {
      onAdd();
    }
  }

  /**
   *
   */
  class PersonalTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersonalTable() {
      PersonalTableModel model = new PersonalTableModel();
      model.addColumn("No");
      model.addColumn("Name");
      model.addColumn("Address");
      model.addColumn("City");
      model.addColumn("Postcode");
      model.addColumn("Phone");
      model.addColumn("Fax");
      model.addColumn("Email");
      model.addColumn("Website");
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
    }

    void setPersonal(Personal[] personal) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < personal.length; i ++) {
        OtherPersonal other = new OtherPersonal(personal[i].getIndex(), personal[i]);
        String postcode = "";
        if(other.getPostCode() > 0)
          postcode = String.valueOf(other.getPostCode());

        model.insertRow(getRowCount(), new Object[]{
        String.valueOf(i + 1), other,
        other.getAddress(), other.getCity(),
        postcode,
        other.getPhone1(),
        other.getFax1(),
        other.getEmail(),
        other.getWebSite()
      });
      }
    }
  }

  /**
   *
   */
  class PersonalTableModel extends DefaultTableModel {
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
  class OtherPersonal extends Personal {
    public OtherPersonal(long index, Personal personal) {
      super(index, personal);
    }

    public String toString() {
      String str = m_firstname + " " + m_lastname;
      return str;
    }
  }
}