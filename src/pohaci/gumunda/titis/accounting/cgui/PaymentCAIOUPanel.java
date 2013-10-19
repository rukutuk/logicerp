package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import pohaci.gumunda.titis.accounting.cgui.panelloader.ITabbedTransactionPanel;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;

public class PaymentCAIOUPanel extends javax.swing.JPanel implements ITabbedTransactionPanel {
	private static final long serialVersionUID = 1L;
	protected Connection m_conn = null;
	protected long m_sessionid = -1;    
    public PaymentCAIOUPanel(Connection conn, long sessionid) {
    	m_conn = conn;
        m_sessionid = sessionid;
        initComponents();
    }    
    
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        paymentCAProjectPanel1 = new PaymentCAIOUProjectPanel(m_conn,m_sessionid);
        paymentCAOthersPanel1 = new PaymentCAIOUOthersPanel(m_conn,m_sessionid);        
        setLayout(new java.awt.BorderLayout());
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(700, 650));
        jTabbedPane1.addTab("Project", paymentCAProjectPanel1);
        jTabbedPane1.addTab("Others", paymentCAOthersPanel1);
        add(jTabbedPane1, java.awt.BorderLayout.NORTH);
    }    
        
    private javax.swing.JTabbedPane jTabbedPane1;
    private PaymentCAIOUOthersPanel paymentCAOthersPanel1;
    private PaymentCAIOUProjectPanel paymentCAProjectPanel1;
    
	public void openAndLoadObject(StateTemplateEntity obj) {

		// jenis-jenis
		if (obj instanceof PmtCAIOUProjectInstall) {
			PmtCAIOUProjectInstall pmt = (PmtCAIOUProjectInstall) obj;
			
			// cari induknya
			StateTemplateEntity ca = pmt.getPmtcaiouproject();
			selectTab(true, ca, obj);
		}else if (obj instanceof PmtCAIOUProjectSettled) {
			PmtCAIOUProjectSettled pmt = (PmtCAIOUProjectSettled) obj;
			
			// cari induknya
			StateTemplateEntity ca = pmt.getPmtcaiouproject();
			selectTab(true, ca, obj);
		}else if (obj instanceof PmtCAIOUProjectReceive) {
			PmtCAIOUProjectReceive pmt = (PmtCAIOUProjectReceive) obj;
			
			// cari induknya
			StateTemplateEntity ca = pmt.getPmtcaiouproject();
			selectTab(true, ca, obj);
		}else if (obj instanceof PmtCAIOUOthersInstall) {
			PmtCAIOUOthersInstall pmt = (PmtCAIOUOthersInstall) obj;
			
			// cari induknya
			StateTemplateEntity ca = pmt.getPmtcaiouothers();
			selectTab(false, ca, obj);
		}else if (obj instanceof PmtCAIOUOthersSettled) {
			PmtCAIOUOthersSettled pmt = (PmtCAIOUOthersSettled) obj;
			
			// cari induknya
			StateTemplateEntity ca = pmt.getPmtcaiouothers();
			selectTab(false, ca, obj);
		}else if (obj instanceof PmtCAIOUOthersReceive) {
			PmtCAIOUOthersReceive pmt = (PmtCAIOUOthersReceive) obj;
			
			// cari induknya
			StateTemplateEntity ca = pmt.getPmtcaiouothers();
			selectTab(false, ca, obj);
		}
	}   
    
	private void selectTab(boolean isProject, StateTemplateEntity ca, StateTemplateEntity obj){
		// project kah?
		if(isProject){
			// project dong...
			jTabbedPane1.setSelectedIndex(0);
			paymentCAProjectPanel1.doLoadObject(ca);
			if(obj instanceof PmtCAIOUProjectInstall) {
				paymentCAProjectPanel1.loadInstallment(obj);
			}else if((obj instanceof PmtCAIOUProjectSettled)||(obj instanceof PmtCAIOUProjectReceive)){
				paymentCAProjectPanel1.loadSettlement();
			}
		}else{
			jTabbedPane1.setSelectedIndex(1);
			paymentCAOthersPanel1.doLoadObject(ca);
			if(obj instanceof PmtCAIOUOthersInstall) {
				paymentCAOthersPanel1.loadInstallment(obj);
			}else if((obj instanceof PmtCAIOUOthersSettled)||(obj instanceof PmtCAIOUOthersReceive)){
				paymentCAOthersPanel1.loadSettlement();
			}
		}	
	}
}
