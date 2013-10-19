

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
import java.sql.Connection;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.application.*;

public class ProjectContractPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
DatePicker m_eststartTextField, m_estendTextField;
  DatePicker m_actstartTextField, m_actendTextField;
  JComboBox m_currencyComboBox;
  JNumberField m_valueNumberField;
  JRadioButton m_ppnRb, m_nonppnRb;
  DatePicker m_validationTextField;
  JTextArea m_descriptTextArea;
  JButton m_addBt, m_editBt, m_deleteBt, m_saveBt, m_cancelBt;
  PaymentTermTable m_table;
  FilePanel m_filePanel;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_editable = false;

  ProjectData m_project = null;
  ProjectContract m_contract = null;
  protected Currency m_currency = null;
  protected ContractPayment[] m_payment = null;

  public ProjectContractPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();   
    reloadCurrencyCombo(" ",false);
  }

  void constructComponent() {
    m_eststartTextField = new DatePicker();
    m_estendTextField = new DatePicker();
    m_actstartTextField = new DatePicker();
    m_actendTextField = new DatePicker();
    m_currencyComboBox = new JComboBox();
    m_valueNumberField = new JNumberField();
    m_valueNumberField.setValue(0.0);
    m_valueNumberField.setPreferredSize(new Dimension(50, 18));
    m_ppnRb = new JRadioButton("Include PPN");
    m_ppnRb.setSelected(true);
    m_nonppnRb = new JRadioButton("Exclude PPN");
    ButtonGroup bg = new ButtonGroup();
    bg.add(m_ppnRb);
    bg.add(m_nonppnRb);
    m_validationTextField = new DatePicker();
    m_descriptTextArea = new JTextArea();
    m_table = new PaymentTermTable();

    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    m_filePanel = new FilePanel("Project Contract Attachment");

    JLabel eststartLabel = new JLabel("Estimate Start Date");
    JLabel estendLabel = new JLabel("Estimate End Date");
    JLabel actstartLabel = new JLabel("Actual Start Date");
    JLabel actendLabel = new JLabel("Actual End Date");
    JLabel valueLabel = new JLabel("Total Value");
    JLabel taxLabel = new JLabel("Tax Status");
    JLabel validationLabel = new JLabel("Contract Validation");
    JLabel descriptLabel = new JLabel("Description");

    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel valuePanel = new JPanel();
    JPanel radioPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel southPanel = new JPanel();
    JPanel button2Panel = new JPanel();

    valuePanel.setLayout(new BorderLayout());
    valuePanel.add(m_currencyComboBox, BorderLayout.WEST);
    valuePanel.add(m_valueNumberField, BorderLayout.CENTER);

    radioPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    radioPanel.add(m_ppnRb);
    radioPanel.add(m_nonppnRb);

    JScrollPane sp = new JScrollPane(m_descriptTextArea);
    m_descriptTextArea.setLineWrap(true);
    sp.setPreferredSize(new Dimension(100, 100));
    

    // leftpanel
    northPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    northPanel.add(eststartLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northPanel.add(m_eststartTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    northPanel.add(estendLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northPanel.add(m_estendTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    northPanel.add(actstartLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northPanel.add(m_actstartTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    northPanel.add(actendLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northPanel.add(m_actendTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    northPanel.add(valueLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northPanel.add(valuePanel, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    northPanel.add(taxLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    northPanel.add(radioPanel, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    northPanel.add(validationLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    northPanel.add(m_validationTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    northPanel.add(descriptLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northPanel.add(sp, gridBagConstraints);

    leftPanel.setLayout(new BorderLayout());
    leftPanel.add(northPanel, BorderLayout.NORTH);

    // rightpanel
    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);
    button2Panel.add(m_saveBt);
    button2Panel.add(m_cancelBt);

    southPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    southPanel.add(m_filePanel, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
    southPanel.add(button2Panel, gridBagConstraints);

    rightPanel.setLayout(new BorderLayout());
    rightPanel.add(buttonPanel, BorderLayout.NORTH);
    rightPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
    rightPanel.add(southPanel, BorderLayout.SOUTH);
    rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Payment Term",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11)));

    centerPanel.setLayout(new GridLayout(1, 2, 5, 0));
    centerPanel.add(leftPanel);
    centerPanel.add(rightPanel);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(centerPanel, BorderLayout.CENTER);
  }

  public void setEditable(boolean editable) {
    m_editable = editable;
    m_eststartTextField.setEditable(editable);
    m_estendTextField.setEditable(editable);
    m_actstartTextField.setEditable(editable);
    m_actendTextField.setEditable(editable);
    m_currencyComboBox.setEnabled(editable);
    m_valueNumberField.setEditable(editable);
    m_ppnRb.setEnabled(editable);
    m_nonppnRb.setEnabled(editable);
    m_validationTextField.setEditable(editable);
    m_descriptTextArea.setEnabled(editable);
    m_addBt.setEnabled(editable);
    m_editBt.setEnabled(editable);
    m_deleteBt.setEnabled(editable);
    m_saveBt.setEnabled(editable);
    m_cancelBt.setEnabled(editable);
  }

  void reloadCurrencyCombo(String dataCombo, boolean m_datacb) {
    m_currencyComboBox.removeAllItems();
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Currency[] currency = logic.getAllCurrency(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT);
      if (m_datacb){
          for(int i = 0; i < currency.length; i ++){             
                if (dataCombo.equals(currency[i].toString())){               
                    m_currencyComboBox.addItem(currency[i]);
                    m_currencyComboBox.setSelectedItem(currency[i]);
                }else{
                    m_currencyComboBox.addItem(currency[i]);
                }
          }
      } else {
            for(int i = 0; i < currency.length; i ++){   
                m_currencyComboBox.addItem(currency[i]);                
            }
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Perhatian", JOptionPane.WARNING_MESSAGE);
    }
  }

  void clear() {
    m_eststartTextField.setDate(null);
    m_estendTextField.setDate(null);
    m_actstartTextField.setDate(null);
    m_actendTextField.setDate(null);
    m_valueNumberField.setValue(0.0);
    m_ppnRb.setSelected(true);

    m_validationTextField.setDate(null);
    m_descriptTextArea.setText("");
    m_table.setContractPayment(new ContractPayment[0]);

    m_filePanel.clear();
  }

  public void setProjectData(ProjectData project) {
    m_project = project;
  }

  public void setProjectContract(ProjectContract contract) {    
    setNullFieldText();
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);    
    //java.text.SimpleDateFormat dateformat = new java.text.SimpleDateFormat("dd-MM-yyyy");    
    try{
        contract = logic.getProjectContract(m_sessionid,"get projectContract",m_project.getIndex());
    }catch (Exception ex){        
    }
    if (contract!=null){
        if (contract.getEstimateStartDate()!=null)
            m_eststartTextField.setDate(contract.getEstimateStartDate());
        if (contract.getEstimateEndDate()!=null)
            m_estendTextField.setDate(contract.getEstimateEndDate());
        if (contract.getActualStartDate()!=null)
            m_actstartTextField.setDate(contract.getActualStartDate());
        if (contract.getActualEndDate()!=null)
            m_actendTextField.setDate(contract.getActualEndDate());               
            m_valueNumberField.setValue(contract.getValue());
        if(contract.getPPN())
            m_ppnRb.setSelected(true);
        else
            m_nonppnRb.setSelected(true);
            
        try{        
            m_currency = logic.getCurrecy(m_sessionid,contract.getCurr());
        }catch(Exception ex){
            }
        if (m_currency!=null)
            reloadCurrencyCombo(m_currency.getSymbol().toString(),true);   
            
        m_validationTextField.setDate(contract.getValidation());
        m_descriptTextArea.setText(contract.getDescription());    
        m_filePanel.setFileName(contract.getFile());
        m_filePanel.setFileByte(contract.getSheet());

    }    
    
    try{
        m_payment = logic.getProjectContractPayment(m_sessionid,"get projectContract",contract.getIndex());          
    } catch(Exception ex){        
    }
    if (m_payment!=null)
        m_table.setContractPayment(m_payment);    
    
  }
  
  public void setNullFieldText(){
    m_eststartTextField.setDate(null);
    m_estendTextField.setDate(null);
    m_actstartTextField.setDate(null);
    m_actendTextField.setDate(null);               
    m_valueNumberField.setValue(0);
    m_validationTextField.setDate(null);
    m_descriptTextArea.setText(null);    
    
  }
  
  public ProjectContract getProjectContract() throws Exception {
    ArrayList list = new ArrayList();
    //java.text.SimpleDateFormat dateformat = new java.text.SimpleDateFormat("dd-MM-yyyy");

    java.util.Date eststartdate = null, estenddate = null;
    java.util.Date actstartdate = null, actenddate = null;
    java.util.Date valdate = null;

    eststartdate = m_eststartTextField.getDate();
    estenddate = m_estendTextField.getDate();
    actstartdate = m_actstartTextField.getDate();
    actenddate = m_actendTextField.getDate();

    double value = m_valueNumberField.getValue();
    Object obj = m_currencyComboBox.getSelectedItem();
    Currency currency = null;
    if(obj instanceof Currency)
      currency = (Currency)obj;

    boolean ppn = false;
    if(m_ppnRb.isSelected())
      ppn = true;

    valdate = m_validationTextField.getDate();

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += "- " + exception[i] + "\n";
      throw new Exception(strexc);
    }

    String description = m_descriptTextArea.getText().trim();
    String file = m_filePanel.getFileName();
    byte[] sheet = m_filePanel.getFileByte();

    return new ProjectContract(eststartdate, estenddate, actstartdate, actenddate, value, currency,
                               ppn, valdate, description, file, sheet);
  }

  public void setProjectContract(ProjectData project) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      ProjectContract contract = logic.getProjectContract(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, project.getIndex());      
      setProjectContract(m_contract = contract);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      ex.printStackTrace();
    }
  }

  void onSave() {
    ProjectContract contract = null;
    //ContractPayment[] payment = new ContractPayment[0];

    try {
      contract = getProjectContract();
      contract.setContractPayment(m_table.getContractPayment());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      if(m_contract == null)
        contract = logic.createProjectContract(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, m_project.getIndex(), contract);
      else {
        contract = logic.updateProjectContract(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, m_contract.getIndex(), contract);
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_contract = contract;
  }

  void onCancel() {
    if(m_contract != null)
      setProjectContract(m_contract);
    else
      clear();
  }

  void onAdd() {
    ContractPaymentDlg dlg = new ContractPaymentDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      ContractPayment payment = dlg.getContactPayment();
      m_table.addContractPayment(payment);
    }
  }

  void onEdit() {
    int row = m_table.getSelectedRow();
    if(row == -1)
      return;

    ContractPayment payment = (ContractPayment)m_table.getValueAt(row, 1);
    ContractPaymentDlg dlg = new ContractPaymentDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), payment);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      payment = dlg.getContactPayment();
      m_table.editContractPayment(payment, row);
    }
    m_table.updateNumber(0);
  }

  void onDelete() {
    int row = m_table.getSelectedRow();
    if(row == -1)
      return;

    ((DefaultTableModel)m_table.getModel()).removeRow(row);
    m_table.updateNumber(0);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_saveBt) {
      onSave();
    }
    else if(e.getSource() == m_cancelBt) {
      onCancel();
    }
    else if(e.getSource() == m_addBt) {
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
  class PaymentTermTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PaymentTermTable() {
      PaymentTermTableModel model = new PaymentTermTableModel();
      model.addColumn("No");
      model.addColumn("Description");
      model.addColumn("Value");
      model.addColumn("% Completed");
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
    }

    public TableCellRenderer getCellRenderer(int row, int col) {
      if(col == 2)
        return new DoubleCellRenderer(JLabel.RIGHT);
      else if(col == 3)
        return new FloatCellRenderer(JLabel.RIGHT);
      return super.getCellRenderer(row, col);
    }

    public void addContractPayment(ContractPayment payment) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{
        new Short((short)(getRowCount() + 1)),
        payment, new Double(payment.getValue()),
        new Float(payment.getCompletion())
      });
    }

    public void editContractPayment(ContractPayment payment, int row) {
      setValueAt(payment, row, 1);
      setValueAt(new Double(payment.getValue()), row, 2);
      setValueAt(new Float(payment.getCompletion()), row, 3);
    }

    public void setContractPayment(ContractPayment[] payment) {
      if (payment!=null){
          DefaultTableModel model = (DefaultTableModel)getModel();
          model.setRowCount(0);
          System.err.println("ke setContractPayment pjng payment :" + payment.length);
          for(int i = 0; i < payment.length; i ++)
            model.addRow(new Object[]{
          new Short((short)(i + 1)),
          payment[i], new Double(payment[i].getValue()),
          new Float(payment[i].getCompletion())
          });
      }
    }

    public ContractPayment[] getContractPayment() {
      java.util.Vector vresult = new java.util.Vector();
      int row = getRowCount();

      for(int i = 0; i < row; i ++)
        vresult.addElement((ContractPayment)getValueAt(i, 1));

      ContractPayment[] payment = new ContractPayment[vresult.size()];
      vresult.copyInto(payment);
      return payment;
    }
    
     void updateNumber(int col) {
        for(int i = 0; i < getRowCount(); i ++)
            setValueAt(String.valueOf(i + 1), i, col); 
    }
  }

  /**
   *
   */
  class PaymentTermTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}