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

public class PaychequeLabelEditorDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  JTextField m_parentTextField;
  JTextField m_labelTextField;

  JComboBox m_typeComboBox = new JComboBox(PaychequeLabel.m_types);
  JCheckBox m_showCheckBox = new JCheckBox("Show Sub Total");
  JButton m_okBt, m_cancelBt;

  DefaultMutableTreeNode m_parent = null;
  PaychequeLabel m_label = null;

  int m_iResponse = JOptionPane.NO_OPTION;
  Connection m_conn = null;
  long m_sessionid = -1;

  public PaychequeLabelEditorDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent) {
    super(mainframe, "Add/Edit Paycheque Label", true);
    setSize(350, 200);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;
    m_parent = parent;

    constructComponent();
    initData();
  }

  public PaychequeLabelEditorDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent, PaychequeLabel label) {
    super(mainframe, "Add/Edit Paycheque Label", true);
    setSize(350, 200);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;
    m_parent = parent;
    m_label = label;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JLabel label1 = new JLabel("Parent");
    JLabel label2 = new JLabel("Label");
    JLabel label3 = new JLabel("Balance Type");
    JPanel centerPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    m_parentTextField = new JTextField();
    m_parentTextField.setEditable(false);
    m_labelTextField = new JTextField();

    m_okBt = new JButton("Save");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    centerPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(1, 10, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    centerPanel.add(label1, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(1, 0, 1, 10);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_parentTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(1, 10, 1, 0);
    centerPanel.add(label2, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(1, 0, 1, 10);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_labelTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.insets = new Insets(1, 10, 1, 0);
    centerPanel.add(label3, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.insets = new Insets(1, 0, 1, 10);
    centerPanel.add(m_typeComboBox, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_showCheckBox, gridBagConstraints);

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(centerPanel, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  }

  void initData() {
    if(m_parent != null)
      m_parentTextField.setText(((PaychequeLabel)m_parent.getUserObject()).getLabel());
    if(m_label != null) {
      m_labelTextField.setText(m_label.getLabel());
      m_typeComboBox.setSelectedItem(m_label.getTypeAsString());
      m_showCheckBox.setSelected(m_label.isShow());
    }
  }

  void onOK() {
    PaychequeLabel label;

    try {
      label = this.getDefinePaychequeLabel();
      if(m_parent != null)
        label.setParent((PaychequeLabel)m_parent.getUserObject());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      if(m_label == null)
        label = logic.createPaychequeLabel(m_sessionid, IDBConstants.MODUL_MASTER_DATA, label);
      else
        label = logic.updatePaychequeLabel(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_label.getIndex(), label);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_label = label;
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public PaychequeLabel getPayChequeLabel() {
    return m_label;
  }

  private PaychequeLabel getDefinePaychequeLabel() throws Exception {
    java.util.ArrayList list = new java.util.ArrayList();
    String label = m_labelTextField.getText().trim();
    if(label.equals(""))
      list.add("Label");

    String strtype = (String)m_typeComboBox.getSelectedItem();
    if(strtype.equals(""))
      list.add("Balance Type");
    short type = PaychequeLabel.typeFromStringToID(strtype);
    boolean show = m_showCheckBox.isSelected();

    String strexc = "Please insert field of:\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += exception[i] + "\n";
      throw new Exception(strexc);
    }

    return new PaychequeLabel(label, type, show);
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