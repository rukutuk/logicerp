package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class PayrollCategoryEditorDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  JTextField m_nameTextField;
  JTextArea m_descriptionTextArea;
  JButton m_okBt, m_cancelBt;

  PayrollCategory m_category = null;
  int m_iResponse = JOptionPane.NO_OPTION;

  public PayrollCategoryEditorDlg(JFrame mainframe, Connection conn, long sessionid) {
    super(mainframe, "Add Payroll Category", true);
    setSize(300, 200);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  public PayrollCategoryEditorDlg(JFrame mainframe, Connection conn, long sessionid, PayrollCategory category) {
    super(mainframe, "Edit Payroll Category", true);
    setSize(300, 200);
    m_mainframe = mainframe;
    m_conn = conn;
    m_sessionid = sessionid;
    m_category = category;

    constructComponent();
    initData();
  }

  void constructComponent() {
    m_nameTextField = new JTextField();
    m_descriptionTextArea = new JTextArea();
    m_okBt = new JButton("Save");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    JPanel centerPanel = new JPanel();
    JPanel formPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    formPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    Misc.setGridBagConstraints(formPanel, new JLabel("Category Name"), gridBagConstraints, 0, 0, GridBagConstraints.HORIZONTAL,
                               1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL,
                               1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(formPanel, m_nameTextField, gridBagConstraints, 2, 0, GridBagConstraints.HORIZONTAL,
                               GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    Misc.setGridBagConstraints(formPanel, new JLabel("Description"), gridBagConstraints, 0, 1, GridBagConstraints.HORIZONTAL,
                               1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 1, GridBagConstraints.HORIZONTAL,
                               1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    m_descriptionTextArea.setPreferredSize(new Dimension(100, 60));
    Misc.setGridBagConstraints(formPanel, new JScrollPane(m_descriptionTextArea), gridBagConstraints, 2, 1, GridBagConstraints.HORIZONTAL,
                               GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    centerPanel.add(formPanel, BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(centerPanel, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  }

  void initData() {
    if(m_category != null) {
      m_nameTextField.setText(m_category.getName());
      m_descriptionTextArea.setText(m_category.getDescription());
    }
  }

  void onOK() {
    PayrollCategory category;

    try {
      category = getDefinePayrollCategory();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      if(m_category == null)
        category = logic.createPayrollCategory(m_sessionid, IDBConstants.MODUL_MASTER_DATA, category);
      else
        category = logic.updatePayrollCategory(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_category.getIndex(), category);
    }
    catch(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_category = category;
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public PayrollCategory getPayrollCategory() {
    return m_category;
  }

  private PayrollCategory getDefinePayrollCategory() throws Exception {
    java.util.ArrayList list = new java.util.ArrayList();
    String name = m_nameTextField.getText().trim();
    String description = m_descriptionTextArea.getText().trim();

    if(name.equals(""))
      list.add("Category Name");

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += exception[i] + "\n";
      throw new Exception(strexc);
    }

    return new PayrollCategory(name, description);
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