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

import pohaci.gumunda.cgui.*;

public class ContractPaymentDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  JTextField m_descriptTextField;
  JNumberField m_valueNumberField;
  JNumberField m_completionNumberField;
  JButton m_okBt, m_cancelBt;

  ContractPayment m_payment = null;
  int m_iResponse = JOptionPane.NO_OPTION;

  public ContractPaymentDlg(JFrame owner) {
    super(owner, "Payment Term", true);
    setSize(300, 200);
    m_mainframe = owner;
    constructComponent();
  }

  public ContractPaymentDlg(JFrame owner, ContractPayment payment) {
    super(owner, "Payment Term", true);
    setSize(300, 200);
    m_mainframe = owner;
    m_payment = payment;
    constructComponent();
    initData();
  }

  void constructComponent() {
    m_descriptTextField = new JTextField();
    m_valueNumberField = new JNumberField();
    m_completionNumberField = new JNumberField();

    m_okBt = new JButton("Save");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    JLabel label1 = new JLabel("Description");
    JLabel label2 = new JLabel("Value");
    JLabel label3 = new JLabel("% Completion");

    JPanel centerPanel = new JPanel();
    JPanel formPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    formPanel.setLayout(new GridBagLayout());
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    formPanel.add(label1, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel(" : "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_descriptTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(label2, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel(" : "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_valueNumberField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    formPanel.add(label3, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel(" : "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_completionNumberField, gridBagConstraints);

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    centerPanel.add(formPanel, BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().add(centerPanel, BorderLayout.CENTER);
  }

  void initData() {
    m_descriptTextField.setText(m_payment.getDescription());
    m_valueNumberField.setValue(m_payment.getValue());
    m_completionNumberField.setValue(m_payment.getCompletion());
  }

  public int getResponse() {
    return m_iResponse;
  }

  public ContractPayment getContactPayment() {
    return m_payment;
  }

  void onOK() {
    ContractPayment payment = null;

    try {
      String descript = m_descriptTextField.getText().trim();
      if(descript.equals(""))
        throw new Exception("Description have to fill");

      double value = m_valueNumberField.getValue();
      if(value == 0.0)
        throw new Exception("Value can't be filled with 0 (zero)");

      float completion = new Double(m_completionNumberField.getValue()).floatValue();

      payment = new ContractPayment(descript, value, completion);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    m_payment = payment;
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
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