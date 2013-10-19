package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;
import javax.swing.*;
import javax.swing.tree.*;

public class ReportLabelTree extends JTree {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  public ReportLabelTree(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {
    DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("Report Label"));
    setModel(model);
    //setCellRenderer(new WorksheetColumnTreeCellRenderer());
    ToolTipManager.sharedInstance().registerComponent(this);
  }
}