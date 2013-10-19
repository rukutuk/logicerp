package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import pohaci.gumunda.titis.accounting.cgui.panelloader.ITabbedTransactionPanel;
import pohaci.gumunda.titis.accounting.entity.MemJournalNonStrd;

public class MJNonStandardPanel extends javax.swing.JPanel implements ITabbedTransactionPanel {
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	 long m_sessionid = -1;   

    public MJNonStandardPanel(Connection conn, long sessionid) {
    	m_conn = conn;
        m_sessionid = sessionid;
    	initComponents();
    }
    
    private void initComponents() {
     //   jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel2_1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        mJProjectPanel1 = new MJProjectPanel(m_conn,m_sessionid);
        mJOthersPanel1 = new MJOthersPanel(m_conn,m_sessionid);

        setLayout(new java.awt.BorderLayout());

       /* setPreferredSize(new java.awt.Dimension(950, 675));
        jSplitPane1.setDividerLocation(225);
        jSplitPane1.setDividerSize(10);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setPreferredSize(new java.awt.Dimension(950, 675));
        jPanel2.setLayout(new java.awt.BorderLayout());*/

        jPanel2.setPreferredSize(new java.awt.Dimension(225, 530));
        jPanel2_1.setLayout(new java.awt.BorderLayout());

        jPanel2_1.setPreferredSize(new java.awt.Dimension(250, 35));
        jPanel2.add(jPanel2_1, java.awt.BorderLayout.NORTH);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

       // jSplitPane1.setLeftComponent(jPanel2);
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(100, 675));
        jTabbedPane1.addTab("Project", mJProjectPanel1);

        jTabbedPane1.addTab("Others", mJOthersPanel1);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

     //   jSplitPane1.setRightComponent(jPanel1);

        add(jPanel1, java.awt.BorderLayout.CENTER);

    }

    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel2_1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private MJOthersPanel mJOthersPanel1;
    private MJProjectPanel mJProjectPanel1;

	public void openAndLoadObject(StateTemplateEntity obj) {
		MemJournalNonStrd object = (MemJournalNonStrd) obj;
		
		// project kah?
		if(object.getProject()!=null){
			// project dong...
			jTabbedPane1.setSelectedIndex(0);
			mJProjectPanel1.doLoad(obj);
		}else{
			jTabbedPane1.setSelectedIndex(1);
			mJOthersPanel1.doLoad(obj);
		}
	}
}
