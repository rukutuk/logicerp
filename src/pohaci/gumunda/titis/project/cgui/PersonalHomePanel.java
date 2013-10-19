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
import javax.swing.*;
import javax.swing.table.*;

public class PersonalHomePanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JButton m_addBt, m_editBt, m_deleteBt;
  HomeTable m_table;
  boolean m_editable = false;

  public PersonalHomePanel() {
    constructComponent();
  }

  void constructComponent() {
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    m_table = new HomeTable();

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);

    setEnabled(false);
    setLayout(new BorderLayout());
    add(buttonPanel, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }

  public void setEnabled(boolean editable) {
    m_editable = editable;
    m_addBt.setEnabled(editable);
    m_editBt.setEnabled(editable);
    m_deleteBt.setEnabled(editable);
  }

  public void clear() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.setRowCount(0);
  }

  void onAdd() {
    PersonalHomeDlg dlg = new PersonalHomeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      PersonalHome home = dlg.getPersonalHome();
      m_table.setPersonalHome(home);
    }
  }

  void onEdit() {
    int row = m_table.getSelectedRow();
    if(row == -1)
      return;
    PersonalHome home = (PersonalHome)m_table.getValueAt(row, 0);
    PersonalHomeDlg dlg = new PersonalHomeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), home, m_editable);
    dlg.setVisible(true);

    if(m_editable && dlg.getResponse() == JOptionPane.OK_OPTION) {
      home = dlg.getPersonalHome();
      m_table.setPersonalHome(home, row);
    }
  }

  void onDelete() {
    int row = m_table.getSelectedRow();
    if(row != -1) {
      Object[] options = {"Yes", "No"};
      if(JOptionPane.showOptionDialog(this,
                                      "Are you sure ? ","Confirmation",
                                      JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])
                                      == JOptionPane.NO_OPTION)
        return;

      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.removeRow(row);
    }
  }

  public void setPersonalHome(PersonalHome[] home) {
    m_table.setPersonalHome(home);
  }

  public PersonalHome[] getPersonalHome() {
    return m_table.getPersonalHome();
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      onAdd();
    }
    else if(e.getSource() == m_editBt) {
      onEdit();
    }
    else if(e.getSource() == m_deleteBt) {
      onDelete();
    }
  }

  /**
   *
   */
  class HomeTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HomeTable() {
      HomeTableModel model = new HomeTableModel();
      model.addColumn("Address");
      model.addColumn("City");
      model.addColumn("Province");
      model.addColumn("Post Code");
      model.addColumn("Country");
      model.addColumn("Website");
      model.addColumn("Phone");
      model.addColumn("Mobile");
      model.addColumn("Fax");
      setModel(model);

      addMouseListener(new MouseAdapter() {
        public void mouseClicked( MouseEvent e ) {
          if(e.getClickCount() >= 2) {
            onEdit();
          }
        }
      });
    }

    public void setPersonalHome(PersonalHome home) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      String postcode = "";
      if(home.getPostCode() > 0)
        postcode = String.valueOf(home.getPostCode());

      String phone = "";
      if(!home.getPhone1().equals(""))
        phone += home.getPhone1();

      if(!home.getPhone2().equals("")) {
        if(phone.equals(""))
          phone += home.getPhone2();
        else
          phone += ", " + home.getPhone2();
      }

      String mobile = "";
      if(!home.getMobile1().equals(""))
        mobile += home.getMobile1();

      if(!home.getMobile2().equals("")) {
        if(mobile.equals(""))
          mobile += home.getMobile2();
        else
          mobile += ", " + home.getMobile2();
      }

      model.addRow(new Object[]{
        home, home.getCity(), home.getProvince(),
        postcode, home.getCountry(), home.getWebsie(),
        phone, mobile, home.getFax()
      });
    }

    public void setPersonalHome(PersonalHome home, int row) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      String postcode = "";
      if(home.getPostCode() > 0)
        postcode = String.valueOf(home.getPostCode());

      String phone = "";
      if(!home.getPhone1().equals(""))
        phone += home.getPhone1();

      if(!home.getPhone2().equals("")) {
        if(phone.equals(""))
          phone += home.getPhone2();
        else
          phone += ", " + home.getPhone2();
      }

      String mobile = "";
      if(!home.getMobile1().equals(""))
        mobile += home.getMobile1();

      if(!home.getMobile2().equals("")) {
        if(mobile.equals(""))
          mobile += home.getMobile2();
        else
          mobile += ", " + home.getMobile2();
      }

      model.removeRow(row);
      model.insertRow(row, new Object[]{
        home, home.getCity(), home.getProvince(),
        postcode, home.getCountry(), home.getWebsie(),
        phone, mobile, home.getFax()
      });
    }

    public void setPersonalHome(PersonalHome[] home) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < home.length; i ++) {
        String postcode = "";
        if(home[i].getPostCode() > 0)
          postcode = String.valueOf(home[i].getPostCode());

        String phone = "";
        if(!home[i].getPhone1().equals(""))
          phone += home[i].getPhone1();

        if(!home[i].getPhone2().equals("")) {
          if(phone.equals(""))
            phone += home[i].getPhone2();
          else
            phone += ", " + home[i].getPhone2();
        }

        String mobile = "";
        if(!home[i].getMobile1().equals(""))
          mobile += home[i].getMobile1();

        if(!home[i].getMobile2().equals("")) {
          if(mobile.equals(""))
            mobile += home[i].getMobile2();
          else
            mobile += ", " + home[i].getMobile2();
        }

        model.addRow(new Object[]{
          home[i], home[i].getCity(), home[i].getProvince(),
          postcode, home[i].getCountry(), home[i].getWebsie(),
          phone, mobile, home[i].getFax()
        });
      }
    }

    public PersonalHome[] getPersonalHome() {
      java.util.Vector vresult = new java.util.Vector();
      int row = getRowCount();

      for(int i = 0; i < row; i ++)
        vresult.addElement((PersonalHome)getValueAt(i, 0));

      PersonalHome[] home = new PersonalHome[vresult.size()];
      vresult.copyInto(home);
      return home;
    }
  }

  /**
   *
   */
  class HomeTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}