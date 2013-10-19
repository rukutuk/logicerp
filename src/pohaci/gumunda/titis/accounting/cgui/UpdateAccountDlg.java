package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dwx
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;

public class UpdateAccountDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  JTextField m_parentTextField;
  JLabel m_superLabel;
  JTextField m_codeTextField, m_nameTextField;
  JComboBox m_categoryComboBox;
  JTextArea m_noteTextArea;
  JRadioButton m_groupRb, m_notgroupRb;
  JRadioButton m_debetRb, m_creditRb;
  JButton m_okBt, m_cancelBt;

  DefaultMutableTreeNode m_parent = null;
  Account m_account = null;

  int m_iResponse = JOptionPane.NO_OPTION;
  Connection m_conn = null;
  long m_sessionid = -1;

  public UpdateAccountDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent) {
    super(mainframe, "Insert Account", true);
    setSize(400, 400);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;
    m_parent = parent;

    constructComponent();
    initData();
  }

  public UpdateAccountDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent, Account account) {
    super(mainframe, "Update Account", true);
    setSize(400, 400);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;
    m_parent = parent;
    m_account = account;

    constructComponent();
    initData();
  }

  void constructComponent() {
    JLabel label1 = new JLabel("Parent Account");
    JLabel label2 = new JLabel("Account Code");
    JLabel label3 = new JLabel("Account Name");
    JLabel label4 = new JLabel("Account Group");
    JLabel label5 = new JLabel("Description");

    JPanel centerPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel groupPanel = new JPanel();
    JPanel balancePanel = new JPanel();

    m_parentTextField = new JTextField();
    m_parentTextField.setEditable(false);
    m_superLabel = new JLabel("");
    m_codeTextField = new JTextField();
    m_nameTextField = new JTextField();
    m_categoryComboBox = new JComboBox(Account.getCategories());
    m_categoryComboBox.insertItemAt("", 0);
    m_categoryComboBox.setSelectedIndex(0);
    m_noteTextArea = new JTextArea();

    m_groupRb = new JRadioButton("Group");
    m_notgroupRb = new JRadioButton("Non Group");
    ButtonGroup bg1 = new ButtonGroup();
    bg1.add(m_groupRb);
    bg1.add(m_notgroupRb);
    m_debetRb = new JRadioButton(Account.STR_DEBET);
    m_creditRb = new JRadioButton(Account.STR_CREDIT);
    ButtonGroup bg2 = new ButtonGroup();
    bg2.add(m_debetRb);
    bg2.add(m_creditRb);

    m_notgroupRb.setSelected(true);
    m_debetRb.setSelected(true);

    groupPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Account Type",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11)));
    groupPanel.add(m_groupRb);
    groupPanel.add(m_notgroupRb);

    balancePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Balance",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11)));
    balancePanel.add(m_debetRb);
    balancePanel.add(m_creditRb);

    m_okBt = new JButton("OK");
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
    centerPanel.add(new JLabel(" : "), gridBagConstraints);

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
    centerPanel.add(new JLabel(" : "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    centerPanel.add(m_superLabel, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_codeTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(label3, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" : "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_nameTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(label4, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" : "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_categoryComboBox, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    centerPanel.add(label5, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" : "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.ipady = 40;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(new JScrollPane(m_noteTextArea), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.ipady = 10;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(groupPanel, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    centerPanel.add(balancePanel, gridBagConstraints);

    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    JPanel mergePanel = new JPanel(new BorderLayout());
    mergePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    mergePanel.add(centerPanel, BorderLayout.CENTER);
    mergePanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(mergePanel, BorderLayout.CENTER);
  }

  void initData() {
    if(m_account != null) {
      if(m_parent != null) {
        Account parentaccount = (Account)m_parent.getUserObject();
        m_parentTextField.setText(parentaccount.toString());
        m_superLabel.setText(parentaccount.getCode() + ".");
        m_codeTextField.setText(m_account.getCode().substring(parentaccount.getCode().length() + 1, m_account.getCode().length()));
      }
      else
        m_codeTextField.setText(m_account.getCode());

      m_nameTextField.setText(m_account.getName());
      m_categoryComboBox.setSelectedItem(m_account.getCategoryAsString());
      m_noteTextArea.setText(m_account.getNote());

      if(m_account.isGroup())
        m_groupRb.setSelected(true);
      else
        m_notgroupRb.setSelected(true);

      if(m_account.getBalanceAsString().equals(Account.STR_DEBET))
        m_debetRb.setSelected(true);
      else
        m_creditRb.setSelected(true);
    }
    else if(m_parent != null) {
      Account parentaccount = (Account)m_parent.getUserObject();
      m_parentTextField.setText(parentaccount.toString());
      m_superLabel.setText(parentaccount.getCode() + ".");
      m_categoryComboBox.setSelectedItem(parentaccount.getCategoryAsString());
      if(parentaccount.getBalanceAsString().equals(Account.STR_DEBET))
        m_debetRb.setSelected(true);
      else
        m_creditRb.setSelected(true);
    }
  }

  void onOK() {
    Account account;
    try {
      account = this.getDefineAccount();
      if(m_parent != null)
        account.setParent((Account)m_parent.getUserObject());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      if(m_account == null)
        account = logic.createAccount(m_sessionid, IDBConstants.MODUL_ACCOUNTING, account);
      else
        account = logic.updateAccount(m_sessionid, IDBConstants.MODUL_ACCOUNTING, m_account.getIndex(), account);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_account = account;
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public Account getAccount() {
    return m_account;
  }

  private Account getDefineAccount() throws Exception {
    String code = m_codeTextField.getText().trim();
    String name = m_nameTextField.getText().trim();
    String category = (String)m_categoryComboBox.getSelectedItem();
    boolean group = false;
    if(m_groupRb.isSelected())
      group = true;

    String balance = Account.STR_DEBET;
    if(m_creditRb.isSelected())
      balance =  Account.STR_CREDIT;

    String note = m_noteTextArea.getText().trim();

    if(code.equals(""))
       throw new Exception("please define the account's code");
    code = m_superLabel.getText().trim() + code;

    if(name.equals(""))
       throw new Exception("please define the account's name");
    if(category.equals(""))
       throw new Exception("please define the account's group");

    return new Account(code, name, Account.categoryFromStringToID(category), group,
                       Account.balanceFromStringToID(balance), note, "");
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