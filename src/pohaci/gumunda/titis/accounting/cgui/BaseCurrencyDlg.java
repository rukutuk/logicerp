package pohaci.gumunda.titis.accounting.cgui;

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
import java.sql.Connection;

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;

public class BaseCurrencyDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  CurrencyPicker m_currencyPicker;
  JButton m_okBt, m_cancelBt;

  public BaseCurrencyDlg(JFrame owner, Connection conn, long sessionid) {
    super(owner, "Base Currency", true);
    setSize(400, 120);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    m_currencyPicker = new CurrencyPicker(m_conn, m_sessionid);
    m_okBt = new JButton("OK");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    JPanel mergePanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    centerPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(1, 0, 1, 0);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    centerPanel.add(new JLabel("Currency"), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_currencyPicker, gridBagConstraints);

    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    mergePanel.setLayout(new BorderLayout());
    mergePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    mergePanel.add(centerPanel, BorderLayout.CENTER);
    mergePanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().add(mergePanel, BorderLayout.CENTER);
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  void initData() {
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Currency currency = logic.getBaseCurrency(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
      if(currency != null)
        m_currencyPicker.setObject(new Currency(currency,Currency.DESCRIPTION));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void save() {
    Currency currency = null;
    try {
      currency = (Currency)m_currencyPicker.getObject();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      logic.createBaseCurrency(m_sessionid, IDBConstants.MODUL_MASTER_DATA, currency);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_okBt) {
      save();
      dispose();
    }
    else if(e.getSource() == m_cancelBt) {
      dispose();
    }
  }
}