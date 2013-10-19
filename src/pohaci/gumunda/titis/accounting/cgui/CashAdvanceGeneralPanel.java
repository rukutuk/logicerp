package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;
import pohaci.gumunda.titis.accounting.cgui.panelloader.ITabbedTransactionPanel;
import pohaci.gumunda.titis.accounting.entity.PmtCAOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;

public class CashAdvanceGeneralPanel extends JPanel implements ITabbedTransactionPanel {
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	CashAdvanceGeneralProjectPanel projectPanel;
	CashAdvanceGeneralOthersPanel othersPanel;
	JTabbedPane tabbedPane;

	public CashAdvanceGeneralPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
	}

	void constructComponent() {
		tabbedPane = new JTabbedPane();
		projectPanel = new CashAdvanceGeneralProjectPanel(m_conn, m_sessionid);
		othersPanel = new CashAdvanceGeneralOthersPanel(m_conn, m_sessionid);
		tabbedPane.addTab("Project", projectPanel);
		tabbedPane.addTab("Other", othersPanel);

		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);
	}

	public void openAndLoadObject(StateTemplateEntity obj) {	
		// project kah?
		if(obj instanceof PmtCAProject){
			// project dong...
			tabbedPane.setSelectedIndex(0);
			projectPanel.doLoad(obj);
		}else if(obj instanceof PmtCAOthers){
			tabbedPane.setSelectedIndex(1);
			othersPanel.doLoad(obj);
		}
	}
}