package pohaci.gumunda.titis.accounting.cgui;

import javax.swing.*;
import net.sf.jasperreports.view.JRViewer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import pohaci.gumunda.titis.accounting.cgui.report.TrialBalance;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.application.*;

public class TrialBalanceReport extends JPanel  implements ActionListener{
	private static final long serialVersionUID = 1L;
	JRViewer m_jrv;
	Connection m_conn = null;
	long m_sessionid = -1;	
	PeriodSubsidiaryLedger m_periodStartEnd;
	JPanel m_centerPanel = new JPanel();
	JButton m_btnView;
	JToggleButton m_btnExcel;
	SubsidiaryAccountSetting[] m_subsidiaryAccounts;
	private boolean m_excel = false;
	
	
	public TrialBalanceReport(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		setJasper();		
		constructComponent();
	}	
	
	void constructComponent() {		
		m_btnView = new JButton("View");
		m_btnView.addActionListener(this);
		m_btnExcel = new JToggleButton(new ImageIcon("../images/excell.gif"));
		m_btnExcel.addActionListener(this);
		
		JPanel button2Panel = new JPanel();
		JPanel endPanel = new JPanel();	
		JPanel panelSisa = new JPanel();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(m_btnView);
		buttonPanel.add(m_btnExcel);
		
		m_periodStartEnd = new PeriodSubsidiaryLedger("Period");
		endPanel.setPreferredSize(new Dimension(500,70));
		endPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		endPanel.add(m_periodStartEnd, gridBagConstraints);
		gridBagConstraints.gridx = 1;		
		gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		endPanel.add(buttonPanel, gridBagConstraints);
		
		
		panelSisa.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0; 
		gridBagConstraints.gridy = 0;		
		panelSisa.add(endPanel, gridBagConstraints); // centerpanel diincludkan kompenent menurut gridbag
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // mengisi sisa
		panelSisa.add(new JPanel(), gridBagConstraints);
		
		m_centerPanel.setLayout(new BorderLayout());
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.add(button2Panel, BorderLayout.SOUTH);		
		
		setLayout(new BorderLayout());
		add(panelSisa, BorderLayout.NORTH);
		add(m_centerPanel, BorderLayout.CENTER);
	}
		
	public void setJasper(){		
		TrialBalance jasper = new TrialBalance(m_sessionid,m_conn);
		m_jrv = jasper.getPrintView();
	}
	
	public void getJasper(){		
		TrialBalance jasper = new TrialBalance(m_conn,m_sessionid,m_periodStartEnd,m_excel);
		if (!m_excel)
			setPanel(jasper);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_btnView){			
			if (!cekValidity()) 
				return;
			m_excel = false;
			getJasper();
		}	
		if(e.getSource() == m_btnExcel){
			if(!cekValidity())
				return;
			m_excel = true;
			getJasper();
		}
	}	
	
	private void setPanel(TrialBalance jasper) {
		if (m_jrv != null)
			m_centerPanel.remove(m_jrv);
		m_jrv = jasper.getPrintView();
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.validate();
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();		
		if (m_periodStartEnd.m_startDate.getDate()==null)
			addInvalid("Start Date must selected");
		if (m_periodStartEnd.m_endDate.getDate()==null)
			addInvalid("End Date must selected");
		if (validityMsgs.size()>0){
			StringBuffer result = new StringBuffer();
			Iterator iter = validityMsgs.iterator();
			while (iter.hasNext()){
				String o = iter.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this,result);
			return false;
		}
		return true;
	}
	
	private void addInvalid(String string) {
		validityMsgs.add(string);
	}
}