package pohaci.gumunda.titis.hrm.cgui;

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
import java.util.Vector;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.application.*;

public class EmployeeCertificationPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  CertificationTable m_table;
  CertificationDetailTable m_detailtable;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;
  FilePanel m_filepanel;

  boolean m_editable = false;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  Certification m_certificate = null;

  public EmployeeCertificationPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    setEnabled(false);
  }

  void constructComponent() {
    JPanel centerPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel formPanel = new JPanel();
    JScrollPane scrollpane = new JScrollPane();
    JPanel buttonPanel = new JPanel();

    m_table = new CertificationTable();
    scrollpane.getViewport().add(m_table);
    scrollpane.setPreferredSize(new Dimension(400, 161));

    m_filepanel = new FilePanel("Certificate Attachment");
    m_detailtable = new CertificationDetailTable();

    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    buttonPanel.add(m_addBt);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    buttonPanel.add(m_editBt);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    buttonPanel.add(m_deleteBt);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    buttonPanel.add(m_saveBt);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    buttonPanel.add(m_cancelBt);

    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(false);

    formPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    formPanel.add(scrollpane, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
    formPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    formPanel.add(m_filepanel, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    formPanel.add(buttonPanel, gridBagConstraints);

    northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    northPanel.add(formPanel);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(northPanel, BorderLayout.NORTH);
    centerPanel.add(new JScrollPane(m_detailtable), BorderLayout.CENTER);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(15, 10, 8, 10));
    add(centerPanel, BorderLayout.CENTER);
  }

  public void setEnabled(boolean editable) {
    m_editable = editable;
    if(!editable) {
      m_addBt.setEnabled(editable);
      m_editBt.setEnabled(editable);
      m_saveBt.setEnabled(editable);
      m_cancelBt.setEnabled(editable);
      m_deleteBt.setEnabled(editable);
      m_filepanel.setEditable(editable);
    }
    else {
      m_addBt.setEnabled(editable);
      if(m_editedIndex != -1) {
        m_editBt.setEnabled(editable);
        m_deleteBt.setEnabled(editable);
      }
      else {
        m_editBt.setEnabled(!editable);
        m_deleteBt.setEnabled(!editable);
      }
      m_saveBt.setEnabled(!editable);
      m_cancelBt.setEnabled(!editable);
    }
  }

  void onAdd() {
    m_new = true;
    m_table.clear();
    m_filepanel.clear();
    m_filepanel.setEditable(true);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_editedIndex = m_detailtable.getSelectedRow();
    m_certificate = (Certification)m_detailtable.getValueAt(m_editedIndex, 0);
    m_filepanel.setEditable(true);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onSave() {
    Certification certificate = null;
    try {
      certificate = m_table.getCertification();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    if(m_new)
      m_detailtable.addCertification(certificate);
    else {
      m_detailtable.updateCertification(certificate, m_editedIndex);
    }

    m_new = false;
    m_edit = false;
    m_filepanel.setEditable(false);
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(true);
  }

  void onDelete() {
    if(!Misc.getConfirmation())
      return;

    m_detailtable.deleteCertification(m_editedIndex);
  }

  void onCancel() {
    m_table.stopCellEditing();

    if(!Misc.getConfirmation())
      return;

    if(m_new) {
      m_new = false;
      m_table.clear();
      m_filepanel.clear();
      m_filepanel.setEditable(false);
      m_addBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);

      if(m_editedIndex != -1) {
        m_editBt.setEnabled(true);
        m_deleteBt.setEnabled(true);
        m_table.setCertification(m_certificate);
      }
      else {
        m_editBt.setEnabled(false);
        m_deleteBt.setEnabled(false);
      }
    }
    else if(m_edit) {
      m_edit = false;
      m_detailtable.updateCertification(m_certificate, m_editedIndex);
      m_filepanel.setEditable(false);
      m_addBt.setEnabled(true);
      m_editBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);
      m_deleteBt.setEnabled(true);
    }
  }

  public Certification[] getCertification() {
    return m_detailtable.getCertification();
  }

  public void setCertification(Certification[] certificate) {
    m_new = false; m_edit = false;
    m_table.clear();
    m_filepanel.clear();
    m_detailtable.setCertification(certificate);
  }

  public void clear() {
    m_table.clear();
    m_filepanel.clear();
    m_detailtable.clear();
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      onAdd();
    }
    else if(e.getSource() == m_editBt) {
      onEdit();
    }
    else if(e.getSource() == m_saveBt) {
      onSave();
    }
    else if(e.getSource() == m_cancelBt) {
      onCancel();
    }
    else if(e.getSource() == m_deleteBt) {
      onDelete();
    }
  }


  /**
   *
   */
  class CertificationTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CertificationTable() {
      EducationTableModel model = new EducationTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");
      model.addRow(new Object[]{"Certificate No", ""});
      model.addRow(new Object[]{"Certificate Date", ""});
      model.addRow(new Object[]{"Institution", ""});
      model.addRow(new Object[]{"Qualification", ""});
      model.addRow(new Object[]{"Description", ""});
      model.addRow(new Object[]{"Start Course Date", ""});
      model.addRow(new Object[]{"End Course Date", ""});
      model.addRow(new Object[]{"Expiry Date", ""});
      model.addRow(new Object[]{"Result", ""});
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if((row == 1 || row == 5 || row == 6 || row == 7) && col == 1)
        return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
      else if(row == 3 && col == 1)
        return new QualificationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            "Qualification", m_conn, m_sessionid);
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void clear() {
      int count = getRowCount();
      for(int i = 0; i < count; i ++)
        setValueAt("", i, 1);
    }

    public Certification getCertification() throws Exception {
      stopCellEditing();
      java.util.ArrayList list = new java.util.ArrayList();

      String no = "", institute = "", description = "";
      Qualification qua = null;
      java.util.Date date, start = null, end = null, expire = null;
      String result = "", file = "";
      byte[] certificate = null;


      int row = 0;
      no = (String)getValueAt(row++, 1);
      if(no == null || no.equals(""))
        list.add("Certificate No");

      String strdate = (String)getValueAt(row++, 1);
      if(strdate.equals(""))
        list.add("Certification Date");

      institute = (String)getValueAt(row++, 1);

      Object obj = getValueAt(row++, 1);
      if(obj instanceof Qualification)
        qua = (Qualification)obj;
      else
        list.add("Qualification");

      description = (String)getValueAt(row++, 1);
      String strstart = (String)getValueAt(row++, 1);
      String strend = (String)getValueAt(row++, 1);
      String strexpire = (String)getValueAt(row++, 1);

      result = (String)getValueAt(row++, 1);
      file = m_filepanel.getFileName();
      certificate = m_filepanel.getFileByte();

      String strexc = "Please insert description of :\n";
      String[] exception = new String[list.size()];
      list.toArray(exception);
      if(exception.length > 0) {
        for(int i = 0; i < exception.length; i ++)
          strexc += "- " + exception[i] + "\n";
        throw new Exception(strexc);
      }

      try {
        date = m_dateformat.parse(strdate);
        if(!strstart.equals(""))
          start = m_dateformat.parse(strstart);
        if(!strend.equals(""))
          end = m_dateformat.parse(strend);
        if(!strexpire.equals(""))
          expire = m_dateformat.parse(strexpire);
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }

      return new Certification(no, date, institute, qua, description, start, end, expire,
                               result, file, certificate);
    }

    public void setCertification(Certification certificate) {
      int row = 0;

      setValueAt(certificate.getNo(), row++, 1);
      if(certificate.getDate() != null)
        setValueAt(m_dateformat.format(certificate.getDate()), row++, 1);
      else
        setValueAt("", row++, 1);

      setValueAt(certificate.getInstitute(), row++, 1);
      setValueAt(certificate.getQualification(), row++, 1);
      setValueAt(certificate.getDescription(), row++, 1);

      if(certificate.getStartDate() != null)
         setValueAt(m_dateformat.format(certificate.getStartDate()), row++, 1);
       else
        setValueAt("", row++, 1);

       if(certificate.getEndDate() != null)
         setValueAt(m_dateformat.format(certificate.getEndDate()), row++, 1);
       else
        setValueAt("", row++, 1);

       if(certificate.getExpireDate() != null)
        setValueAt(m_dateformat.format(certificate.getExpireDate()), row++, 1);
      else
        setValueAt("", row++, 1);

      setValueAt(certificate.getResult(), row++, 1);
      m_filepanel.setFileName(certificate.getFile());
      m_filepanel.setFileByte(certificate.getCertificate());
    }
  }

  /**
   *
   */
  class EducationTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;
      if(m_new || m_edit)
        return true;
      return false;
    }

    public void setValueAt(Object obj, int row, int col) {
      if(obj instanceof Qualification)
        super.setValueAt(new Qualification((Qualification)obj, Qualification.NAME), row, col);
      else
        super.setValueAt(obj, row, col);
    }
  }

  /**
   *
   */
  class CertificationDetailTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CertificationDetailTable() {
      CertificationDetailTableModel model = new CertificationDetailTableModel();
      model.addColumn("Certificate No");
      model.addColumn("Certificate Date");
      model.addColumn("Institution");
      model.addColumn("Qualification");
      model.addColumn("Description");
      model.addColumn("Start Date");
      model.addColumn("End Date");
      model.addColumn("Expiry Date");
      model.addColumn("Result");

      setModel(model);
      getSelectionModel().addListSelectionListener(model);
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }


    void addCertification(Certification certificate) {
      String date = "", start = "", end = "", expire = "";

      if(certificate.getDate() != null)
        date = m_dateformat.format(certificate.getDate());
      if(certificate.getStartDate() != null)
        start = m_dateformat.format(certificate.getStartDate());
      if(certificate.getEndDate() != null)
        end = m_dateformat.format(certificate.getEndDate());
      if(certificate.getExpireDate() != null)
        expire = m_dateformat.format(certificate.getExpireDate());

      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{
        certificate, date, certificate.getInstitute(), certificate.getQualification(),
        certificate.getDescription(), start, end, expire, certificate.getResult()
      });

      m_editedIndex = getRowCount() -1;
      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void updateCertification(Certification certificate, int row) {
      String date = "", start = "", end = "", expire = "";

      if(certificate.getDate() != null)
        date = m_dateformat.format(certificate.getDate());
      if(certificate.getStartDate() != null)
        start = m_dateformat.format(certificate.getStartDate());
      if(certificate.getEndDate() != null)
        end = m_dateformat.format(certificate.getEndDate());
      if(certificate.getExpireDate() != null)
        expire = m_dateformat.format(certificate.getExpireDate());

      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        certificate, date, certificate.getInstitute(), certificate.getQualification(),
        certificate.getDescription(), start, end, expire, certificate.getResult()
      });

      getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
    }

    void deleteCertification(int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
    }

    public Certification[] getCertification() {
      Vector vresult = new Vector();
      for(int i = 0; i < getRowCount(); i ++)
        vresult.addElement((Certification)getValueAt(i, 0));

      Certification[] result = new Certification[vresult.size()];
      vresult.copyInto(result);
      return result;
    }

    public void setCertification(Certification[] certificate) {
      m_editedIndex = -1;
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      for(int i = 0; i < certificate.length; i ++) {
        String date = "", start = "", end = "", expire = "";

        if(certificate[i].getDate() != null)
          date = m_dateformat.format(certificate[i].getDate());
        if(certificate[i].getStartDate() != null)
          start = m_dateformat.format(certificate[i].getStartDate());
        if(certificate[i].getEndDate() != null)
          end = m_dateformat.format(certificate[i].getEndDate());
        if(certificate[i].getExpireDate() != null)
          expire = m_dateformat.format(certificate[i].getExpireDate());

        model.addRow(new Object[]{
          certificate[i], date, certificate[i].getInstitute(), certificate[i].getQualification(),
          certificate[i].getDescription(), start, end, expire, certificate[i].getResult()
        });
      }
    }
  }

  /**
   *
   */
  class CertificationDetailTableModel extends DefaultTableModel implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }

    public void valueChanged(ListSelectionEvent e) {
      if(!e.getValueIsAdjusting()){
        int iRowIndex = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();

        if(m_new || m_edit){
          if(iRowIndex != m_editedIndex)
            m_detailtable.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
        }
        else{
          if(iRowIndex != -1){
            if(m_editable) {
              m_addBt.setEnabled(true);
              m_editBt.setEnabled(true);
              m_saveBt.setEnabled(false);
              m_cancelBt.setEnabled(false);
              m_deleteBt.setEnabled(true);
            }

            m_certificate = (Certification)getValueAt(iRowIndex, 0);
            m_table.setCertification(m_certificate);
            m_editedIndex = iRowIndex;
          }
          else{
            m_addBt.setEnabled(true);
            m_editBt.setEnabled(false);
            m_saveBt.setEnabled(false);
            m_cancelBt.setEnabled(false);
            m_deleteBt.setEnabled(false);

            m_table.clear();
            m_filepanel.clear();
          }
        }
      }
    }
  }
}