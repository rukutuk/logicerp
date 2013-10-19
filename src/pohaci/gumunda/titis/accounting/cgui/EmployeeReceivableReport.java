package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.cgui.report.EmployeeReceivable;
import pohaci.gumunda.titis.application.DatePicker;


public class EmployeeReceivableReport extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	UnitPicker m_unitPicker;
	DatePicker m_date;
	JButton m_btnView;
	JToggleButton m_btnExcel;
	JRViewer m_jrv;
	JPanel m_tablePanel = new JPanel();	 
	
	public EmployeeReceivableReport(Connection conn,long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		setJasper();
		initComponents();
	}
	
	void initComponents() {
		JLabel dateLbl = new JLabel("Date");		
		
		m_unitPicker = new UnitPicker(m_conn,m_sessionid);
		m_date = new DatePicker();
		m_date.setDate(new Date());
		m_date.setPreferredSize(new Dimension(300,22));
		m_btnView = new JButton("View");
		m_btnView.addActionListener(this);
		m_btnExcel = new JToggleButton(new ImageIcon("../images/excell.gif"));
		m_btnExcel.addActionListener(this);
		
		
		JPanel centerPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel northRightPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel northLeftPanel = new JPanel(); 		   
		
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(10, 6, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		leftPanel.add(dateLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		leftPanel.add(new JLabel(" "), gridBagConstraints);		

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(10, 3, 1, 3);	    
		leftPanel.add(m_date, gridBagConstraints);	
		
		/*gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(1, 6, 1, 0);
		leftPanel.add(unitCodeLbl, gridBagConstraints);*/
		
		gridBagConstraints.gridx = 1;
		leftPanel.add(new JLabel(" "), gridBagConstraints);
		
		/*gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		leftPanel.add(m_unitPicker , gridBagConstraints);*/
		
		rightPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		
		/*gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(3, 0, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		rightPanel.add(new JLabel(" "), gridBagConstraints);*/
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;	
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(m_btnView, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(m_btnExcel, gridBagConstraints);	
		
		northLeftPanel.setLayout(new BorderLayout());
		northLeftPanel.add(leftPanel,BorderLayout.NORTH);	    
		
		northRightPanel.setLayout(new BorderLayout());
		northRightPanel.add(rightPanel,BorderLayout.NORTH);
  
		m_tablePanel.setLayout(new BorderLayout());
		m_tablePanel.setBorder(BorderFactory.createEtchedBorder());		
		m_tablePanel.add(m_jrv, BorderLayout.CENTER);
		
		centerPanel.setLayout(new GridBagLayout());// menentukan cartesius x,y
		gridBagConstraints = new GridBagConstraints(); 
		
		northLeftPanel.setPreferredSize(new Dimension(350, 40)); // size northLeftPanel
		gridBagConstraints.gridx = 0; 
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; // mengikuti perbesaran component horizontal
		centerPanel.add(northLeftPanel, gridBagConstraints); // centerpanel diincludkan kompenent menurut gridbag
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = GridBagConstraints.NORTH; // meletakkan posisi keatas 
		centerPanel.add(northRightPanel, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // mengisi sisa
		centerPanel.add(new JPanel(), gridBagConstraints);
		
		// panel buat mendorong agar keatas.	    
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH; // perbesarannya secara horisontal dan vertikal
		centerPanel.add(m_tablePanel, gridBagConstraints);
		setLayout(new BorderLayout());		
		add(centerPanel);
		
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
	
	public void setJasper(){
		EmployeeReceivable jasper = new EmployeeReceivable();		
		m_jrv = jasper.getPrintView();
	}
	
	public void getJasper(boolean isExcel){		
		EmployeeReceivable jasper = new EmployeeReceivable(m_conn,m_sessionid,m_date,isExcel);
		if (!isExcel)
		setPanel(jasper);
	}

	private void setPanel(EmployeeReceivable jasper) {
		if (m_jrv != null){			
			m_tablePanel.remove(m_jrv);			
		}
		m_jrv = jasper.getPrintView();		
		m_tablePanel.add(m_jrv, BorderLayout.CENTER);		
		m_tablePanel.validate();
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();		
		if (m_date.getDate()==null)
			addInvalid("Date must selected");
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
