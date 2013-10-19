package pohaci.gumunda.titis.project.cgui;

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
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.FilePanel;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class ProjectNotesDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  DatePicker m_datePicker;
  JTextArea m_descriptTextArea, m_actionTextArea, m_remarkTextArea;
  EmployeePicker m_responPicker, m_preparedPicker, m_approverPicker;
  FilePanel m_filepanel;
  JButton m_saveBt, m_cancelBt;

  ProjectNotes m_notes = null;
  ProjectData m_project = null;
  int m_iResponse = JOptionPane.NO_OPTION;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");  
  
  public ProjectNotesDlg(JFrame owner, Connection conn, long sessionid, ProjectData project) {
    super(owner, "Project Notes", true);
    setSize(700, 450);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_project = project;

    constructComponent();
  }

  public ProjectNotesDlg(JFrame owner, Connection conn, long sessionid, ProjectNotes notes) {
    super(owner, "Project Notes", true);
    setSize(700, 450);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_notes = notes;

    constructComponent();
    setNotes(notes);
  }

  void constructComponent() {
    m_filepanel = new FilePanel("Note Attachement");
    m_datePicker = new DatePicker();
    m_descriptTextArea = new JTextArea();
    m_actionTextArea = new JTextArea();
    m_remarkTextArea = new JTextArea();
    m_responPicker = new EmployeePicker(m_conn, m_sessionid);
    m_preparedPicker = new EmployeePicker(m_conn, m_sessionid);
    m_approverPicker = new EmployeePicker(m_conn, m_sessionid);

    JScrollPane descriptScroll = new JScrollPane(m_descriptTextArea);
    descriptScroll.setPreferredSize(new Dimension(100, 70));

    JScrollPane actionScroll = new JScrollPane(m_actionTextArea);
    actionScroll.setPreferredSize(new Dimension(100, 70));

    JScrollPane remarkScroll = new JScrollPane(m_remarkTextArea);
    remarkScroll.setPreferredSize(new Dimension(100, 70));

    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    JPanel formPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel centerPanel = new JPanel();

    JLabel dateLabel = new JLabel("Date");
    JLabel descriptLabel = new JLabel("Description");
    JLabel actionLabel = new JLabel("Action");
    JLabel responLabel = new JLabel("Responsibility");
    JLabel preparedLabel = new JLabel("Prepared By");
    JLabel approverLabel = new JLabel("Approver");
    JLabel remarkLabel = new JLabel("Remark");

    formPanel.setLayout(new GridBagLayout());
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    formPanel.add(dateLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    formPanel.add(m_datePicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    formPanel.add(descriptLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(descriptScroll, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    formPanel.add(actionLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(actionScroll, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    formPanel.add(responLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    formPanel.add(m_responPicker, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 2, 1, 0);
    formPanel.add(preparedLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    formPanel.add(m_preparedPicker, gridBagConstraints);

    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 2, 1, 0);
    formPanel.add(approverLabel, gridBagConstraints);

    gridBagConstraints.gridx = 7;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 8;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_approverPicker, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    formPanel.add(remarkLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(remarkScroll, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    formPanel.add(m_filepanel, gridBagConstraints);

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(formPanel, BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(centerPanel, BorderLayout.NORTH);
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  public int getResponse() {
    return m_iResponse;
  }

  public ProjectNotes getNotes() {
    return m_notes;
  }

  private ProjectNotes getDefineNotes() throws Exception {
    java.util.ArrayList list = new java.util.ArrayList();
    java.util.Date date = null;
    String description = "", action = "";
    Employee respon = null, prepared = null, approver = null;
    String remark = "", file = "";
    byte[] sheet = null;

    date = m_datePicker.getDate();
    if(date == null)
      list.add("Date of Notes");

    description = m_descriptTextArea.getText().trim();
    action = m_actionTextArea.getText().trim();

    respon = m_responPicker.getEmployee();
    if(respon == null)
      list.add("Responsibility");

    prepared = m_preparedPicker.getEmployee();
    if(prepared == null)
      list.add("Prepared By");

    approver = m_approverPicker.getEmployee();
    if(approver == null)
      list.add("Approver");

    remark = m_remarkTextArea.getText().trim();
    file = m_filepanel.getFileName();
    sheet = m_filepanel.getFileByte();

    String strexc = "Please insert :\n";
      String[] exception = new String[list.size()];
      list.toArray(exception);
      if(exception.length > 0) {
        for(int j = 0; j < exception.length; j ++)
          strexc += "- " + exception[j] + "\n";
        throw new Exception(strexc);
      }

    return new ProjectNotes(date, description, action, respon,
                            prepared, approver, remark, file, sheet);
  }

  public void setNotes(ProjectNotes notes) {
    m_datePicker.setDate(notes.getDate());
    m_descriptTextArea.setText(notes.getDescription());
    m_actionTextArea.setText(notes.getAction());
    m_responPicker.setEmployee(notes.getResponsibility());
    m_preparedPicker.setEmployee(notes.getPreparedBy());
    m_approverPicker.setEmployee(notes.getApprover());
    m_remarkTextArea.setText(notes.getRemark());
    m_filepanel.setFileName(notes.getFile());
    m_filepanel.setFileByte(notes.getSheet());
  }

  void onSave() {
    ProjectNotes notes = null;

    try {
      notes = getDefineNotes();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      if(m_notes == null) {
        m_notes = logic.createProjectNotes(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
            m_project.getIndex(), notes);
      }
      else {
        m_notes = logic.updateProjectNotes(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
            m_notes.getIndex(), notes);
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public void actionPerformed(ActionEvent e){
    if(e.getSource() == m_saveBt) {
      onSave();
    }
    else if(e.getSource() == m_cancelBt) {
      if(!Misc.getConfirmation())
        return;
      dispose();
    }
  }
}