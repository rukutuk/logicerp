package pohaci.gumunda.titis.accounting.cgui;

import javax.swing.*;

import net.sf.jasperreports.view.JRViewer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import pohaci.gumunda.titis.accounting.cgui.report.Sales;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.application.*;

public class SalesReport extends JPanel  implements ActionListener{
	private static final long serialVersionUID = 1L;
	JRViewer m_jrv;
	Connection m_conn = null;
	long m_sessionid = -1;	
	PeriodSubsidiaryLedger m_periodStartEnd;
	JPanel m_centerPanel = new JPanel();
	JButton m_btnView;
	JToggleButton m_btnExcel;
	private UnitPicker m_unitPicker;
	SubsidiaryAccountSetting[] m_subsidiaryAccounts;
	
	public SalesReport(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		setJasper();		
		constructComponent();
	}	
	
	void constructComponent() {
		JLabel UnitLbl = new JLabel("Unit Code");		
		m_btnView = new JButton("View");
		m_btnView.addActionListener(this);		
		m_btnExcel = new JToggleButton(new ImageIcon("../images/excell.gif"));
		m_btnExcel.addActionListener(this);
		
		JPanel button2Panel = new JPanel();
		
		JPanel addUnitPanel = new JPanel();
		JPanel endPanel = new JPanel();	
		JPanel panelSisa = new JPanel();
		
		m_periodStartEnd = new PeriodSubsidiaryLedger("Period");
		m_unitPicker = new UnitPicker(m_conn,m_sessionid);		
		
		addUnitPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(2, 4, 1, 1);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		addUnitPanel.add(UnitLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		addUnitPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		addUnitPanel.add(m_unitPicker, gridBagConstraints);
		
		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		addUnitPanel.add(m_btnView, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		addUnitPanel.add(m_btnExcel, gridBagConstraints);
		
		endPanel.setPreferredSize(new Dimension(350,120));
		endPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		endPanel.add(m_periodStartEnd, gridBagConstraints);
		gridBagConstraints.gridy = 1;
		endPanel.add(addUnitPanel, gridBagConstraints);
		
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
		Sales jasper = new Sales();
		m_jrv = jasper.getPrintView();
	}
	
	public void getJasper(boolean toexcel){		
		Sales jasper = new Sales(m_conn,m_sessionid,m_periodStartEnd,m_unitPicker,toexcel);
		if (!toexcel)
			setPanel(jasper);
	}
	
	private void setPanel(Sales jasper) {
		if (m_jrv!=null)
			m_centerPanel.remove(m_jrv);
		m_jrv = jasper.getPrintView();	
		m_centerPanel.add(m_jrv,BorderLayout.CENTER);
		m_centerPanel.validate();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_btnView){			
			if (!cekValidity()) 
				return;
			getJasper(false);
		}
		else if (e.getSource() == m_btnExcel){			
			if (!cekValidity()) 
				return;
			getJasper(true);
		} 
		
	}	
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();		
		if (m_periodStartEnd.m_startDate.getDate()==null)
			addInvalid("Start Date must selected");
		if (m_periodStartEnd.m_endDate.getDate()==null)
			addInvalid("End Date must selected");		
		/*	if (m_unitPicker.getUnit()==null)
		 addInvalid("Unit must selected");*/
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