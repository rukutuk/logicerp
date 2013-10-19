package pohaci.gumunda.titis.project.cgui;

import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.application.ObjectCellEditor;

public class ProjectDataCellEditor extends ObjectCellEditor implements ActionListener  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SearchProjectDetailDlg m_projdlg = null;

	public ProjectDataCellEditor(JFrame owner, Connection conn, long sessionid) {
	    super(owner, conn, sessionid);
	}

	public void done() {		 
		m_projdlg = new SearchProjectDetailDlg(
		        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
		        m_conn, m_sessionid);
		m_projdlg.setVisible(true);
		
		if (m_projdlg.getResponse() == JOptionPane.OK_OPTION) {
			ProjectData[] project = m_projdlg.getProjectData();			
			if (project.length > 0) {				
				setObject(project[0]);
			}
		}
		
	}
}
