package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class PersonalBusinessListDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  BusinessTable m_table;
  JButton m_okBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  Personal m_personal = null;

  public PersonalBusinessListDlg(JFrame owner, Connection conn, long sessionid, Personal personal) {
    super(owner, "Personal Business", true);
    setSize(600, 500);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_personal = personal;

    constructComponent();
    initData();
  }

  void constructComponent() {
    m_table = new BusinessTable();
    m_okBt = new JButton("   OK   ");
    m_okBt.addActionListener(this);

    JPanel centerPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(m_okBt);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(centerPanel, BorderLayout.CENTER);
  }

  void initData() {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_table.setPersonalBusiness(logic.getPersonalBusiness(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, m_personal.getIndex()));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_okBt) {
      dispose();
    }
  }

  /**
   *
   */
  class BusinessTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessTable() {
      BusinessTableModel model = new BusinessTableModel();
      model.addColumn("Company");
      model.addColumn("Address");
      model.addColumn("City");
      model.addColumn("Province");
      model.addColumn("Post Code");
      model.addColumn("Country");
      model.addColumn("Website");
      model.addColumn("Job Title");
      model.addColumn("Department");
      model.addColumn("Office");
      model.addColumn("Phone");
      model.addColumn("Fax");
      setModel(model);
    }

    public void setPersonalBusiness(PersonalBusiness[] business) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < business.length; i ++) {
        String postcode = "";
        if(business[i].getPostCode() > 0)
          postcode = String.valueOf(business[i].getPostCode());

        String phone = "";
        if(!business[i].getPhone1().equals(""))
          phone += business[i].getPhone1();

        if(!business[i].getPhone2().equals("")) {
          if(phone.equals(""))
            phone += business[i].getPhone2();
          else
            phone += ", " + business[i].getPhone2();
        }

        model.addRow(new Object[]{
          business[i], business[i].getAddress(), business[i].getCity(), business[i].getProvince(),
          postcode, business[i].getCountry(), business[i].getWebsie(),
          business[i].getJobTitle(), business[i].getDepartement(), business[i].getOffice(),
          phone, business[i].getFax()
        });
      }
    }
  }

  /**
   *
   */
  class BusinessTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}