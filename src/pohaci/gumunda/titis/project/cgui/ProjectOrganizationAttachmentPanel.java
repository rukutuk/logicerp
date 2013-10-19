package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;
import java.sql.Connection;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class ProjectOrganizationAttachmentPanel extends FilePanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;
  ProjectData m_project = null;

  public ProjectOrganizationAttachmentPanel(Connection conn, long sessionid) {
    super("Project Organization Attachment");
    m_conn = conn;
    m_sessionid = sessionid;
  }

  public void setProjectData(ProjectData project) {
    m_project = project;
  }

public void setFileAttactment(FileAttachment file) {    
    setFileName(file.getName());
    setFileByte(file.getBytes());
  }

  public void upload() {
    super.upload();

    if(getFileByte() == null)
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.createProjectProjectOrganizationAttachment(m_sessionid,
          IDBConstants.MODUL_PROJECT_MANAGEMENT, m_project.getIndex(),
          new FileAttachment(getFileName(), getFileByte()));
    }
    catch(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                    ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void delete() {
    if(getFileByte() == null)
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteProjectOrganizationAttachment(m_sessionid,
          IDBConstants.MODUL_PROJECT_MANAGEMENT, m_project.getIndex());

      super.delete();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                    ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }
}