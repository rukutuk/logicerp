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

public class WorksheetCreatorDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  JTextField m_worksheetTextField;
  JButton m_saveBt, m_cancelBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  Worksheet m_worksheet = null;

  public WorksheetCreatorDlg(JFrame owner, Connection conn, long sessionid) {
    super(owner, "Add Woksheet", true);
    setSize(300, 120);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
  }

  void constructComponent() {
    m_worksheetTextField = new JTextField();
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
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
    centerPanel.add(new JLabel("Worksheet Name"), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    centerPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    centerPanel.add(m_worksheetTextField, gridBagConstraints);

    buttonPanel.add(m_saveBt);
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

  void save() {
    String name = m_worksheetTextField.getText().trim();
    if(name.equals("")) {
      JOptionPane.showMessageDialog(this, "Please Insert\n Worksheet Name");
      return;
    }

    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      m_worksheet = logic.createWorksheet(m_sessionid, IDBConstants.MODUL_MASTER_DATA, new Worksheet(name));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public int getResponse() {
    return m_iResponse;
  }

  public Worksheet getWorksheet() {
    return m_worksheet;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_saveBt) {
      save();
    }
    else if(e.getSource() == m_cancelBt) {
      dispose();
    }
  }
}