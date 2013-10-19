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
import java.sql.Connection;
import javax.swing.*;
import javax.swing.event.*;

import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class LeavePermitionPanel extends JPanel implements ListSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  EmployeeListPanel m_list;
  EmployeeProfileTable m_table;
  EmployeeLeavePermitionPanel m_leavePanel;
  EmployeeOfficeHourPermitionPanel m_officePanel;
  EmployeeLeavePermissionHistoryPanel m_leaveHistoryPanel;

  public LeavePermitionPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {
    m_list = new EmployeeListPanel(m_conn, m_sessionid);
    m_list.getList().addListSelectionListener(this);
    m_table = new EmployeeProfileTable(m_conn, m_sessionid);
    m_leavePanel = new EmployeeLeavePermitionPanel(m_conn, m_sessionid);
    m_officePanel = new EmployeeOfficeHourPermitionPanel(m_conn, m_sessionid);
    m_leaveHistoryPanel = new EmployeeLeavePermissionHistoryPanel(m_conn, m_sessionid);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JTabbedPane tabbedPane = new JTabbedPane();

    tabbedPane.addTab("Profile", new JScrollPane(m_table));
    tabbedPane.addTab("Leave Permission", m_leavePanel);
    tabbedPane.addTab("Leave & Permission History", m_leaveHistoryPanel);
    tabbedPane.addTab("Office Hour Permission", m_officePanel);

    splitPane.setLeftComponent(m_list);
    splitPane.setRightComponent(tabbedPane);
    splitPane.setDividerLocation(200);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
  }

  void setSelectedObject(Employee employee) {
    m_table.setEmployee(employee);
    setEmployeeAttribute(employee.getIndex());
    m_leavePanel.setEmployee(employee);
    m_leavePanel.setEditable(true);
    m_leavePanel.clear();
    m_officePanel.setEmployee(employee);
    m_officePanel.setEditable(true);
    m_officePanel.clear();
    m_leaveHistoryPanel.setEmployee(employee);
  }

  void setEmployeeAttribute(long index) {
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      EmployeeEducation[] education = logic.getEmployeeEducation(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);
      setMaxEducation(education);

      Employment[] employment = logic.getEmployeeEmployment(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);
      setMaxEmployment(employment);

      Qualification[] qua = logic.getEmployeeQualification(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, index);
      setEmployeeQualification(qua);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setMaxEducation(EmployeeEducation[] education) {
    java.util.Date befdate = null, afterdate = null;
    int index = -1;

    for(int i = 0; i < education.length; i ++) {
      if(befdate == null) {
        befdate = education[i].getTo();
        index = i;
      }

      if(i + 1 < education.length) {
        afterdate = education[i + 1].getTo();
        if(befdate.compareTo(afterdate) == -1) {
          befdate = afterdate;
          index = i + 1;
        }
      }
    }

    if(index != -1)
      m_table.setValueAt(education[index].getGrade(), 2, 1);
  }

  void setMaxEmployment(Employment[] employ) {
    java.util.Date befdate = null, afterdate = null;
    int index = -1;

    for(int i = 0; i < employ.length; i ++) {
      if(befdate == null) {
        befdate = employ[i].getTMT();
        index = i;
      }

      if(i + 1 < employ.length) {
        afterdate = employ[i + 1].getTMT();
        if(befdate.compareTo(afterdate) == -1) {
          befdate = afterdate;
          index = i + 1;
        }
      }
    }

    if(index != -1) {
      m_table.setValueAt(employ[index].getJobTitle(), 3, 1);
      m_table.setValueAt(employ[index].getDepartment(), 4, 1);
      m_table.setValueAt(employ[index].getUnit(), 5, 1);
      m_table.setValueAt(employ[index].getWorkAgreement(), 6, 1);
    }
  }

  public void setEmployeeQualification(Qualification[] qua) {
    String strqua = "";
    for(int i = 0; i < qua.length; i ++) {
      if(i == 0)
        strqua += new Qualification(qua[i], Qualification.NAME).toString();
      else
        strqua += ", " + new Qualification(qua[i], Qualification.NAME).toString();
    }
    m_table.setValueAt(strqua, 13, 1);
  }

  public void valueChanged(ListSelectionEvent e) {
    if(!e.getValueIsAdjusting()){
      int iRowIndex = m_list.getList().getMinSelectionIndex();

      if(iRowIndex != -1){
        Employee employee = (Employee)m_list.getList().getSelectedValue();
        setSelectedObject(employee);
      }
    }
  }
}