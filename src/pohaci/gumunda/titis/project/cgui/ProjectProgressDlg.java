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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pohaci.gumunda.cgui.JNumberField;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class ProjectProgressDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  DatePicker m_datePicker;
  JTextArea m_descriptTextArea, m_remarkTextArea;
  JNumberField m_completedNumberField;
  EmployeePicker m_preparedPicker, m_approverPicker;

  JButton m_saveBt, m_cancelBt;

  ProjectProgress m_progress = null;
  ProjectData m_project = null;
  int m_iResponse = JOptionPane.NO_OPTION;

  public ProjectProgressDlg(JFrame owner, Connection conn, long sessionid, ProjectData project) {
    super(owner, "Project Progress", true);
    setSize(500, 300);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_project = project;

    constructComponent();
  }

  public ProjectProgressDlg(JFrame owner, Connection conn, long sessionid, ProjectProgress progress) {
    super(owner, "Project Progress", true);
    setSize(500, 300);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_progress = progress;

    constructComponent();
    setProgress(progress);
  }

  void constructComponent() {
    m_datePicker = new DatePicker();
    m_descriptTextArea = new JTextArea();
    m_completedNumberField = new JNumberField();
    m_remarkTextArea = new JTextArea();
    m_preparedPicker = new EmployeePicker(m_conn, m_sessionid);
    m_approverPicker = new EmployeePicker(m_conn, m_sessionid);

    JScrollPane descriptScroll = new JScrollPane(m_descriptTextArea);
    descriptScroll.setPreferredSize(new Dimension(100, 70));

    JScrollPane remarkScroll = new JScrollPane(m_remarkTextArea);
    remarkScroll.setPreferredSize(new Dimension(100, 70));

    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    JPanel formPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    JLabel dateLabel = new JLabel("Date");
    JLabel descriptLabel = new JLabel("Description");
    JLabel completedLabel = new JLabel("% Completed");
    JLabel preparedLabel = new JLabel("Prepared By");
    JLabel approverLabel = new JLabel("Approver");
    JLabel remarkLabel = new JLabel("Remark");

    formPanel.setLayout(new GridBagLayout());
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    gridBagConstraints.anchor = GridBagConstraints.NORTH;
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
    formPanel.add(completedLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    formPanel.add(m_completedNumberField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    formPanel.add(preparedLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    formPanel.add(m_preparedPicker, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(2, 2, 1, 0);
    formPanel.add(approverLabel, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 0);
    formPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 5;
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

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(formPanel, BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));

    getContentPane().setLayout(new BorderLayout(10, 0));
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

  public ProjectProgress getProgress() {
    return m_progress;
  }

  private ProjectProgress getDefineProgress() throws Exception {
    java.util.ArrayList list = new java.util.ArrayList();
    java.util.Date date = null;
    String description = "";
    float completion = 0.0f;
    Employee prepared = null, approver = null;
    String remark = "";

    date = m_datePicker.getDate();
    if(date == null)
      list.add("Date of Progress");

    description = m_descriptTextArea.getText().trim();
    completion = new Float(m_completedNumberField.getValue()).floatValue();

    prepared = m_preparedPicker.getEmployee();
    if(prepared == null)
      list.add("Prepared By");

    approver = m_approverPicker.getEmployee();
    if(approver == null)
      list.add("Approved By");

    remark = m_remarkTextArea.getText().trim();

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int j = 0; j < exception.length; j ++)
        strexc += "- " + exception[j] + "\n";
      throw new Exception(strexc);
    }

    return new ProjectProgress(date, description, completion, prepared, approver, remark);
  }

  public void setProgress(ProjectProgress progress) {
    m_datePicker.setDate(progress.getDate());
    m_descriptTextArea.setText(progress.getDescription());
    m_completedNumberField.setValue(progress.getCompletion());
    m_preparedPicker.setEmployee(progress.getPreparedBy());
    m_approverPicker.setEmployee(progress.getApprover());
    m_remarkTextArea.setText(progress.getRemark());
  }
  
  void onSave() {
    ProjectProgress progress = null;

    try {
      progress = getDefineProgress();
    } 
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      if(m_progress == null) {
        m_progress = logic.createProjectProgress(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
            m_project.getIndex(), progress);
      }
      else {
        m_progress = logic.updateProjectProgress(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
            m_progress.getIndex(), progress);
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

  public void actionPerformed(ActionEvent e) {
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