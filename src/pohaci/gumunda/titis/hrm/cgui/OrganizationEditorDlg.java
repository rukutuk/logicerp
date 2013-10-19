package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class OrganizationEditorDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  JTextField m_parentTextField, m_codeTextField, m_nameTextField;
  JTextArea m_noteTextArea;
  JButton m_okBt, m_cancelBt;

  DefaultMutableTreeNode m_parent = null;
  Organization m_org = null;

  int m_iResponse = JOptionPane.NO_OPTION;
  Connection m_conn = null;
  long m_sessionid = -1;

  public OrganizationEditorDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent) {
    super(mainframe, "Add Department", true);
    setSize(400, 200);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;
    m_parent = parent;

    constructComponent();
    initData();
  }

  public OrganizationEditorDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent, Organization org) {
    super(mainframe, "Edit Department", true);
    setSize(400, 200);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;
    m_parent = parent;
    m_org = org;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JLabel label1 = new JLabel("Parent Name");
    JLabel label2 = new JLabel("Code");
    JLabel label3 = new JLabel("Name");
    JLabel label4 = new JLabel("Description");
    JPanel centerPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    m_parentTextField = new JTextField();
    m_parentTextField.setEditable(false);
    m_codeTextField = new JTextField();
    m_nameTextField = new JTextField();
    m_noteTextArea = new JTextArea();

    m_okBt = new JButton("Save");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    centerPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(1, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    centerPanel.add(label1, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_parentTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(label2, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_codeTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(label3, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_nameTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    centerPanel.add(label4, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.ipady = 40;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(new JScrollPane(m_noteTextArea), gridBagConstraints);

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(centerPanel, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  }

  void initData() {
    if(m_parent != null)
      m_parentTextField.setText(((Organization)m_parent.getUserObject()).getName());
    if(m_org != null) {
      m_codeTextField.setText(m_org.getCode());
      m_nameTextField.setText(m_org.getName());
      m_noteTextArea.setText(m_org.getDescription());
    }
  }

  void onOK() {
    Organization org;

    try {
      org = this.getDefineOrganization();
      if(m_parent != null)
        org.setParent((Organization)m_parent.getUserObject());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      if(m_org == null)
        org = logic.orgLogic.createOrganization(logic, m_sessionid, IDBConstants.MODUL_MASTER_DATA, org);
      else
        org = logic.orgLogic.updateOrganization(logic, m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_org.getIndex(), org);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_org = org;
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public Organization getOrganization() {
    return m_org;
  }

  private Organization getDefineOrganization() throws Exception {
    java.util.ArrayList list = new java.util.ArrayList();
    String code = m_codeTextField.getText().trim();
    String name = m_nameTextField.getText().trim();
    String note = m_noteTextArea.getText().trim();

    if(code.equals(""))
       list.add("Code");

    if(name.equals(""))
       list.add("Name");

    String strexc = "Please insert field of:\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += exception[i] + "\n";
      throw new Exception(strexc);
    }

    return new Organization(code, name, note);
  }

  public int getResponse() {
    return m_iResponse;
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
      onOK();
    }
    else if(e.getSource() == m_cancelBt) {
      dispose();
    }
  }
}
