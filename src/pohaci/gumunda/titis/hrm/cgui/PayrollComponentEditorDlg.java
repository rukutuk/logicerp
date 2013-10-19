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

import pohaci.gumunda.titis.accounting.cgui.*;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class PayrollComponentEditorDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  JTextField m_parentTextField, m_codeTextField, m_descriptionTextField;
  JRadioButton m_groupRadioButton, m_nongroupRadioButton;
  AccountPicker m_accountPicker;
  JComboBox m_paymentComboBox, m_submitComboBox, m_reportComboBox;
  PaychequeLabelPicker m_labelPicker;
  JButton m_okBt, m_cancelBt;

  DefaultMutableTreeNode m_parent = null;
  PayrollComponent m_component = null;

  int m_iResponse = JOptionPane.NO_OPTION;
  Connection m_conn = null;
  long m_sessionid = -1;
  
  boolean m_first;
  
  String m_submitformtypes[];
  
  public PayrollComponentEditorDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent) {
    super(mainframe, "Add Payroll Component", true);
    setSize(400, 300);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;
    m_parent = parent;
    m_first = true;
    
    constructComponent();
    initData();
  }

  public PayrollComponentEditorDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent, PayrollComponent component) {
    super(mainframe, "Edit Payroll Component", true);
    setSize(400, 300);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;
    m_parent = parent;
    m_component = component;
    m_first = true;
    
    constructComponent();
    initData();
  }

  void constructComponent() {
    m_parentTextField = new JTextField();
    m_parentTextField.setEditable(false);
    m_codeTextField = new JTextField();
    m_descriptionTextField = new JTextField();

    m_groupRadioButton = new JRadioButton("Group");
    m_groupRadioButton.addActionListener(this);
    m_nongroupRadioButton = new JRadioButton("Non Group");
    m_nongroupRadioButton.setSelected(true);
    m_nongroupRadioButton.addActionListener(this);
    m_accountPicker = new AccountPicker(m_conn, m_sessionid);
    m_paymentComboBox = new JComboBox(PayrollComponent.m_payments);
    m_paymentComboBox.insertItemAt("", 0);
    m_paymentComboBox.setSelectedIndex(0);
    // i add this 1 line
    m_paymentComboBox.addActionListener(this);
    
    m_reportComboBox = new JComboBox(PayrollComponentReportLabel.LABELS);
    
    m_submitComboBox = new JComboBox(PayrollComponent.m_submits);
    m_submitComboBox.insertItemAt("", 0);
    m_submitComboBox.setSelectedIndex(0);
    m_labelPicker = new PaychequeLabelPicker(m_conn, m_sessionid);

    m_okBt = new JButton("Save");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    JLabel parentLabel = new JLabel("Parent");
    JLabel codeLabel = new JLabel("Code");
    JLabel desciptionLabel = new JLabel("Description");
    JLabel typeLabel = new JLabel("Type");
    JLabel accountLabel = new JLabel("Account");
    JLabel paymentLabel = new JLabel("Paymet Type");
    JLabel submitLabel = new JLabel("Submit Form Type");
    JLabel paychequeLabel = new JLabel("Paycheque Label");
    JLabel reportLabel = new JLabel("Report Label");

    JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel centerPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    ButtonGroup bg = new ButtonGroup();
    bg.add(m_groupRadioButton);
    bg.add(m_nongroupRadioButton);
    radioPanel.add(m_groupRadioButton);
    radioPanel.add(m_nongroupRadioButton);

    centerPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(1, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    centerPanel.add(parentLabel, gridBagConstraints);

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
    centerPanel.add(codeLabel, gridBagConstraints);

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
    centerPanel.add(desciptionLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_descriptionTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(typeLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(radioPanel, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(accountLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_accountPicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(paymentLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_paymentComboBox, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(submitLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_submitComboBox, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(paychequeLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_labelPicker, gridBagConstraints);
    
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    centerPanel.add(reportLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_reportComboBox, gridBagConstraints);

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
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
    if(m_parent != null)
      m_parentTextField.setText(((PayrollComponent)m_parent.getUserObject()).getDescription());
    if(m_component != null) {
      m_codeTextField.setText(m_component.getCode());
      m_descriptionTextField.setText(m_component.getDescription());
      if(m_component.isGroup()){
        m_groupRadioButton.setSelected(true);
        onGroupSelected();
      }else
      {
        m_nongroupRadioButton.setSelected(true);
        onNonGroupSelected();
      }

      m_accountPicker.setAccount(m_component.getAccount());
      if(!m_component.getPaymentAsString().equals("")){
    	  m_paymentComboBox.setSelectedItem(m_component.getPaymentAsString());
      }else{
    	  m_paymentComboBox.setSelectedIndex(0);
      }
      if(!m_component.getSubmitAsString().equals("")){
    	  m_submitComboBox.setSelectedItem(m_component.getSubmitAsString());
      }else{
    	  m_submitComboBox.setSelectedIndex(0);
      }
      if (m_component.getReportLabel() != null) {
				if (!m_component.getReportLabel().equals("")) {
					m_reportComboBox.setSelectedItem(m_component
							.getReportLabel());
				} else {
					m_reportComboBox.setSelectedItem("");
				}
			} else {
				m_reportComboBox.setSelectedItem("");
			}
      m_labelPicker.setPaychequeLabel(m_component.getPaychequeLabel());
    }
  }

  void onOK() {
    PayrollComponent component;

    try {
      component = this.getDefinePayrollComponent();
      if(m_parent != null)
        component.setParent((PayrollComponent)m_parent.getUserObject());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      if(m_component == null)
        component = logic.createPayrollComponent(m_sessionid, IDBConstants.MODUL_MASTER_DATA, component);
      else
        component = logic.updatePayrollComponent(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_component.getIndex(), component);
    }
    catch(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_component = component;
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public PayrollComponent getPayrollComponent() {
    return m_component;
  }

  private PayrollComponent getDefinePayrollComponent() throws Exception {
    java.util.ArrayList list = new java.util.ArrayList();
    String code = m_codeTextField.getText().trim();
    String description = m_descriptionTextField.getText().trim();
    boolean isgroup = m_groupRadioButton.isSelected();
    Account account = null;
    short payment = -1;
    short submit = -1;
    PaychequeLabel label = null;
    String reportLabel = "";
    if (!isgroup) {
        account = m_accountPicker.getAccount();
        payment = PayrollComponent.paymentFromStringToID((String)m_paymentComboBox.getSelectedItem());
        submit = PayrollComponent.submitFromStringToID((String)m_submitComboBox.getSelectedItem());
        label = m_labelPicker.getPaychequeLabel();
        reportLabel = (String) m_reportComboBox.getSelectedItem();
    } 

    if(code.equals(""))
      list.add("Code");
    
    if(description.equals(""))
      list.add("Description");
    
    // i change 4 lines below
    if (!isgroup) {
    	if(account==null)
    		list.add("Account");
    	if(payment == -1)
    		list.add("Payment Type");
    	if(submit == -1)
    		list.add("Submit Form Type");
    }

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += exception[i] + "\n";
      throw new Exception(strexc);
    }
    
    return new PayrollComponent(code, description, isgroup, account, payment, submit, label, reportLabel);
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
    else if(e.getSource()==m_groupRadioButton) {
        // i add this
        onGroupSelected();
    }
    else if(e.getSource()==m_nongroupRadioButton) {
        // i add this
        onNonGroupSelected();
    }
    else if(e.getSource()==m_paymentComboBox){
        // i add this
        onPaymentTypeSelected();
    }
  }

    private void onGroupSelected() {
        // i add this
        // matikan semua yang di bawahnya
        if (m_groupRadioButton.isSelected()) {
            
            m_accountPicker.setEnabled(false);
            m_accountPicker.setAccount(null);
            m_paymentComboBox.setSelectedIndex(0);
            m_submitComboBox.setEnabled(false);
            m_paymentComboBox.setEnabled(false);
            m_submitComboBox.setSelectedIndex(0);
            m_labelPicker.setEnabled(false);
            m_labelPicker.setPaychequeLabel(null);
            m_reportComboBox.setEnabled(false);
            m_reportComboBox.setSelectedIndex(0);
        }
    }

    private void onNonGroupSelected() {
       // i add this
       // hidupkan semua
        if (m_nongroupRadioButton.isSelected()) {
            m_accountPicker.setEnabled(true);
            m_paymentComboBox.setEnabled(true);
            m_submitComboBox.setEnabled(true);
            m_labelPicker.setEnabled(true);
            m_reportComboBox.setEnabled(true);
        }
    }

    private void onPaymentTypeSelected() {
        // i add this
        // sesuaikan submit type dengan payment typenya
        //if ((m_component==null) || (!m_first)) {
            if (m_paymentComboBox.getSelectedItem().toString()==PayrollComponent.PAYCHEQUE) {
            	m_submitformtypes = new String[]{PayrollComponent.PAYCHEQUE, PayrollComponent.FIELD_ALLOWANCE,
            			PayrollComponent.EMPLOYEE_RECEIVABLES};
                m_labelPicker.setEnabled(true);
                m_reportComboBox.setEnabled(true);
                submitForm();
            } else if (m_paymentComboBox.getSelectedItem().toString()==PayrollComponent.NON_PAYCHEQUE) {
                m_submitformtypes = new String[]{
                    PayrollComponent.MEAL_ALLOWANCE, PayrollComponent.TRANSPORTION_ALLOWANCE,
                    PayrollComponent.OVERTIME
                };
                m_labelPicker.setEnabled(false);
                m_reportComboBox.setEnabled(false);
                submitForm();
            } else if (m_paymentComboBox.getSelectedItem().toString()==PayrollComponent.NON_PAYMENT) {
            	m_submitformtypes = new String[]{
                    PayrollComponent.INSURANCE_ALLOWANCE, PayrollComponent.OTHER_ALLOWANCE
                };
                m_labelPicker.setEnabled(false);
                m_reportComboBox.setEnabled(false);
                submitForm();
            }
        
            m_submitComboBox.removeAllItems();
            if(m_submitformtypes!=null){
	            short len = (short)m_submitformtypes.length;
	            for(short i=0; i<len; i++){
	                m_submitComboBox.addItem(m_submitformtypes[i]);
	            }
            }
            m_submitComboBox.insertItemAt("", 0);
            m_submitComboBox.setSelectedIndex(0);
            if(!m_labelPicker.isEnabled()) { m_labelPicker.setPaychequeLabel(null); }
        //}
    }

	private void submitForm() {
		m_submitComboBox.removeAllItems();
		short len = (short)m_submitformtypes.length;
		for(short i=0; i<len; i++){
		    m_submitComboBox.addItem(m_submitformtypes[i]);
		}
		m_submitComboBox.insertItemAt("", 0);
		m_submitComboBox.setSelectedIndex(0);
		if(!m_labelPicker.isEnabled()) { m_labelPicker.setPaychequeLabel(null); }
	}
}
