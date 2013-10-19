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

public class SearchPartnerDetailDlg extends SearchPartnerDlg {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
PartnerTable m_detailtable;
  JButton m_selectBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  Partner[] m_partner = new Partner[0];

  public SearchPartnerDetailDlg(JFrame owner, Connection conn, long sessionid) {
    super(owner, null, conn, sessionid);
    setModal(true);
    setSize(450, 600);
    find();
  }

  void constructComponent() {
    
    m_detailtable = new PartnerTable();
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
      m_detailtable.setPartner(logic.getPartnerByCriteria(m_sessionid,
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

  public Partner[] getPartner() {
    return m_partner;
  }

  void onAdd() {
    int[] row = m_detailtable.getSelectedRows();
    java.util.Vector vresult = new java.util.Vector();
    for(int i = 0; i < row.length; i ++) {
      Partner partner = (Partner)m_detailtable.getValueAt(row[i], 1);
      vresult.addElement(new Partner(partner.getIndex(), partner));
    }
    m_partner = new Partner[vresult.size()];
    vresult.copyInto(m_partner);
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
  class PartnerTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PartnerTable() {
      PartnerTableModel model = new PartnerTableModel();
      model.addColumn("No");
      model.addColumn("Code");
      model.addColumn("Name");
      model.addColumn("Group");
      model.addColumn("Address");
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
      
      addMouseListener(new MouseAdapter() {
          public void mouseClicked( MouseEvent e ) {
            if(e.getClickCount() >= 2) {
              onAdd();
            }
          }
        });
    }

    void setPartner(Partner[] partner) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < partner.length; i ++) {
        OtherPartner part = new OtherPartner(partner[i].getIndex(), partner[i]);
        model.addRow(new Object[]{
        String.valueOf((i + 1)), part,
        part.getName(), part.getCompanyToString(), part.getAddress(),
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
  class PartnerTableModel extends DefaultTableModel {
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
  class OtherPartner extends Partner {
    public OtherPartner(long index, Partner partner) {
      super(index, partner);
    }

    public String toString() {
      return m_code;
    }
  }
}