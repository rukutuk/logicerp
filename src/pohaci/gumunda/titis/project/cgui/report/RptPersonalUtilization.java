package pohaci.gumunda.titis.project.cgui.report;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;

import pohaci.gumunda.aas.dbapi.IDBConstants;
import pohaci.gumunda.cgui.DoubleCellRenderer;
import pohaci.gumunda.titis.accounting.cgui.ActivityTreeDlg;
import pohaci.gumunda.titis.accounting.cgui.UnitListDlg;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.StructuredTable;
import pohaci.gumunda.titis.hrm.cgui.AllowenceMultiplier;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.OrganizationTreeDlg;
import pohaci.gumunda.titis.hrm.cgui.SearchEmployeeDlg;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.EmployeeTimesheet;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class RptPersonalUtilization extends JPanel implements ActionListener {	
	private static final long serialVersionUID = 1L;
	private JButton m_excellBt,m_printpreviewBt,m_SearchBt,m_viewBt; 
	private JTextField m_unitCodetxt,m_departmentTxt,m_actCodeText;
	private JLabel m_unitCodeLbl,m_departmentLbl,m_actCodeLbl,m_mountLbl,m_yearLbl;
	private JComboBox m_mounthCb, m_yearCb;
	private JButton m_unitCodeBt,m_departmentBt,m_actCodeBt;
	
	Connection m_conn = null;
	long m_sessionid = -1;
	
	Unit m_unit = null;
	Organization m_org = null;
	Activity m_activity = null;
	long m_indexUnit,m_indexorg,m_indexAct;
	private PayrollTable m_table;
	AllowenceMultiplier[] m_areaCode = null;
	boolean m_show = false;
	EmployeeTimesheet[] m_employeeTimesheet = null;
	
	public RptPersonalUtilization(Connection conn,long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initComponents();
		setEditable(false);
	}
	
	void initComponents() {		
		java.util.Date date = new java.util.GregorianCalendar().getTime();
		java.text.DateFormat formatThn = new java.text.SimpleDateFormat("yyyy");
		java.text.DateFormat formatBln = new java.text.SimpleDateFormat("MM");
		String thn = formatThn.format(date);
		String bln = formatBln.format(date);
		m_excellBt = new JButton(new ImageIcon("../images/excell.gif"));
		m_excellBt.addActionListener(this);
		m_printpreviewBt = new JButton(new ImageIcon("../images/filter.gif"));
		m_printpreviewBt.addActionListener(this);
		m_SearchBt = new JButton(new ImageIcon("../images/filter2.gif"));
		m_SearchBt.addActionListener(this);
		m_viewBt = new JButton("view");
		m_viewBt.addActionListener(this);
		ButtonGroup bg = new ButtonGroup();
		bg.add(m_excellBt);
		bg.add(m_printpreviewBt);
		bg.add(m_SearchBt);
		bg.add(m_viewBt);
		
		JToolBar buttonbar = new JToolBar();
		JPanel buttonPanel = new JPanel();
		
		buttonbar.setFloatable(false);
		buttonbar.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		buttonbar.add(m_excellBt, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		buttonbar.add(m_printpreviewBt, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;        
		buttonbar.add(m_SearchBt, gridBagConstraints);
		
		gridBagConstraints.gridx = 3;
		buttonbar.add(m_viewBt, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		buttonbar.add(buttonPanel, gridBagConstraints);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(m_excellBt);
		buttonPanel.add(m_printpreviewBt);
		buttonPanel.add(m_viewBt);
		
		m_unitCodeLbl = new JLabel("Unit Code");
		m_unitCodetxt = new JTextField();
		m_unitCodeBt = new JButton();
		m_unitCodeBt = new JButton("...");
		m_unitCodeBt.addActionListener(this);
		m_unitCodeBt.setPreferredSize(new Dimension(20, 18));
		
		m_departmentLbl = new JLabel("Department");
		m_departmentTxt = new JTextField();		
		m_departmentBt = new JButton("...");
		m_departmentBt.addActionListener(this);
		m_departmentBt.setPreferredSize(new Dimension(20, 18));
		
		m_actCodeLbl = new JLabel("Activity Code");
		m_actCodeText = new JTextField();
		m_actCodeBt = new JButton("...");
		m_actCodeBt.addActionListener(this);
		m_actCodeBt.setPreferredSize(new Dimension(20, 18));
		
		m_mountLbl = new JLabel("Month");		
		m_mounthCb = new JComboBox();
		reloadMountCombo(bln);
		
		m_yearLbl = new JLabel("Year");
		m_yearCb = new JComboBox();		
		reloadYearCombo(thn);
		
		JPanel centerPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel northRightPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel northLeftPanel = new JPanel(); 
		JPanel tablePanel = new JPanel();	    
		
		leftPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(10, 6, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		leftPanel.add(m_unitCodeLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		leftPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		leftPanel.add(m_unitCodetxt, gridBagConstraints);
		
		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(10, 3, 1, 3);	    
		leftPanel.add(m_unitCodeBt, gridBagConstraints);	
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(1, 6, 1, 0);
		leftPanel.add(m_departmentLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		leftPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		leftPanel.add(m_departmentTxt, gridBagConstraints);
		
		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		leftPanel.add(m_departmentBt, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(2, 6, 1, 0);	    
		leftPanel.add(m_actCodeLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		leftPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;	    
		leftPanel.add(m_actCodeText, gridBagConstraints);
		
		gridBagConstraints.gridx = 3;	    
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		leftPanel.add(m_actCodeBt, gridBagConstraints);   
		
		
		rightPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(10, 0, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		rightPanel.add(m_mountLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		leftPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(10, 3, 1, 3);
		rightPanel.add(m_mounthCb, gridBagConstraints);	
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(2, 0, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		rightPanel.add(m_yearLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		rightPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_yearCb, gridBagConstraints);	
		
		northLeftPanel.setLayout(new BorderLayout());
		northLeftPanel.add(leftPanel,BorderLayout.NORTH);	    
		
		northRightPanel.setLayout(new BorderLayout());
		northRightPanel.add(rightPanel,BorderLayout.NORTH);
		
		m_table = new PayrollTable(new DefaultMutableTreeNode("Column"));	    
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createEtchedBorder());
		tablePanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
		
		centerPanel.setLayout(new GridBagLayout());// menentukan cartesius x,y
		gridBagConstraints = new GridBagConstraints(); 
		
		northLeftPanel.setPreferredSize(new Dimension(350, 80)); // size northLeftPanel
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
		centerPanel.add(tablePanel, gridBagConstraints);
		
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		add(buttonPanel,BorderLayout.NORTH);
		add(centerPanel);
		
	}  
	
	public void setEditable(boolean editable){
		m_SearchBt.setEnabled(editable);
		m_printpreviewBt.setEnabled(editable);
		m_excellBt.setEnabled(editable);
	}
	
	void reloadMountCombo(String bln){
		m_mounthCb.removeAllItems();    	
		m_mounthCb.addItem("January");
		m_mounthCb.addItem("February");   
		m_mounthCb.addItem("March");
		m_mounthCb.addItem("April");
		m_mounthCb.addItem("May");
		m_mounthCb.addItem("June");
		m_mounthCb.addItem("July");
		m_mounthCb.addItem("August");
		m_mounthCb.addItem("September");
		m_mounthCb.addItem("October");
		m_mounthCb.addItem("November");
		m_mounthCb.addItem("December");
		if (bln.equals("01")){	    		
			m_mounthCb.setSelectedItem("January");
		}else if (bln.equals("02")){	    		
			m_mounthCb.setSelectedItem("February");
		}else if (bln.equals("03")){	    		
			m_mounthCb.setSelectedItem("March");
		}else if (bln.equals("04")){	    		
			m_mounthCb.setSelectedItem("April");
		}else if (bln.equals("05")){	    		
			m_mounthCb.setSelectedItem("May");
		}else if (bln.equals("06")){	    		
			m_mounthCb.setSelectedItem("June");
		}else if (bln.equals("07")){	    		
			m_mounthCb.setSelectedItem("July");
		}else if (bln.equals("08")){	    		
			m_mounthCb.setSelectedItem("August");
		}else if (bln.equals("09")){	    		
			m_mounthCb.setSelectedItem("September");
		}else if (bln.equals("10")){	    		
			m_mounthCb.setSelectedItem("October");
		}else if (bln.equals("11")){	    		
			m_mounthCb.setSelectedItem("November");
		}else{	    		
			m_mounthCb.setSelectedItem("December");
		}
	}
	
	void reloadYearCombo(String thn){
		m_yearCb.removeAllItems();
		for (int i=0;i<20;i++){
			if (i>=10){    				
				m_yearCb.addItem("20" + i);
				if (thn.endsWith("20" + i))
					m_yearCb.setSelectedItem(thn);
			}else{
				m_yearCb.addItem("200" + i);
				if (thn.endsWith("200" + i))
					m_yearCb.setSelectedItem(thn);
			}
		}
	}
	
	void unit() {
		UnitListDlg dlg = new UnitListDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				m_conn, m_sessionid);
		dlg.setVisible(true);
		
		if(dlg.getResponse() == JOptionPane.OK_OPTION) {
			m_unit = dlg.getUnit();
			m_indexUnit = m_unit.getIndex();
			m_unitCodetxt.setText(m_unit.getCode() + " " + m_unit.getDescription());
		}
	}
	
	void organization() {
		OrganizationTreeDlg dlg = new OrganizationTreeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				m_conn, m_sessionid,false);
		dlg.setVisible(true);
		
		if(dlg.getResponse() == JOptionPane.OK_OPTION) {
			m_org = dlg.getOrganization();
			m_indexorg = m_org.getIndex();
			m_departmentTxt.setText(m_org.getName());
		}
	}
	
	void activity() {
		ActivityTreeDlg dlg = new ActivityTreeDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				m_conn, m_sessionid);
		dlg.setVisible(true);
		
		if(dlg.getResponse() == JOptionPane.OK_OPTION) {
			m_activity = dlg.getActivity();
			m_indexAct = m_activity.getIndex();
			m_actCodeText.setText(m_activity.toString());
		}
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==m_SearchBt){
			if(!m_show) {
				m_show = true;
				SearchEmployeeDlg dlg = new SearchEmployeeDlg(
						pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), 
						this,m_conn, m_sessionid);
				dlg.setVisible(true);
				m_show = false;
			}
		}
		else if(e.getSource() == m_unitCodeBt){
			unit();
		}
		else if(e.getSource() == m_departmentBt){    		
			organization();
		}    	
		else if(e.getSource() == m_actCodeBt){
			activity();
		}
		else if(e.getSource()==m_printpreviewBt){
			jaspertReport(false);
		}
		else if(e.getSource()==m_excellBt){
			jaspertReport(true);
			
		}else if (e.getSource()==m_viewBt){
			view(true,null,"");
		}
	}
	
	public void view(boolean cektable,String[] attr,String operator){
		String query = "";
		String tahun = String.valueOf(m_yearCb.getItemAt(m_yearCb.getSelectedIndex()));
		String selisih   = toDateSelisih(String.valueOf(m_mounthCb.getSelectedIndex()+1),tahun);
		String tglPilih = tahun + "-" +  (m_mounthCb.getSelectedIndex()+1) + "-20";    	
		tableHeader(); 
		if (cektable)
			query = query(m_indexUnit,m_indexorg,m_indexAct,tglPilih,selisih);
		else
			query = queryByCriteria(m_indexUnit,m_indexorg,m_indexAct,tglPilih,selisih,attr,operator);		
		tableFooter(query); 
	}
	public void jaspertReport(boolean check){
		String bulan = String.valueOf(m_mounthCb.getItemAt(m_mounthCb.getSelectedIndex()));
		String tahun = String.valueOf(m_yearCb.getItemAt(m_yearCb.getSelectedIndex()));    	    	
		String tglChar = bulan + " " + tahun;     	
		String unit = "";    	
		String dept = "";
		String act  = "";    	  	
		if (m_unit!=null)
			unit = m_unit.getDescription();
		if (m_org!=null)
			dept = m_org.getDescription();
		if (m_activity!=null)
			act = m_activity.getName();
		new pohaci.gumunda.titis.project.cgui.report.ProjectPersonelUtilizationJasper(m_sessionid,m_conn,unit,dept,act,tglChar,m_employeeTimesheet,check);	   
	}
	
	public void tableHeader(){
		try{
			m_areaCode= getAllAllowenceMultiplier();		
			m_table.resetDefaultColumn(m_areaCode);
			m_table.clearRow();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)m_table.getTableHeaderRoot();
			root.removeAllChildren();
			m_table.resetDefaultColumn(m_areaCode);		
			m_table.rebuildTable();
			m_table.clearRow();
			
			setEditable(true);
		} catch (Exception ex){	    	  
			ex.printStackTrace();
		}
	}
	
	public void tableFooter(String query){
		try {
			Vector data = null;
			ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
			
			m_employeeTimesheet = logic.getAllEmployeeTimesheetUtilization(query,m_sessionid,IDBConstants.MODUL_APPLICATION);				
			for(int i=0; i<m_employeeTimesheet.length; i++){					
				data = new Vector();
				data.addElement(String.valueOf(i+1));
				data.addElement(m_employeeTimesheet[i].getEmployeeno());
				data.addElement(m_employeeTimesheet[i].getAllName());    						
				data.addElement(m_employeeTimesheet[i].getQualification());
				data.addElement(m_employeeTimesheet[i].getWorkdescription());
				data.addElement(m_employeeTimesheet[i].getClient());
				data.addElement(m_employeeTimesheet[i].getIpcNo());
				for(int j=0;j<m_areaCode.length;j++){						
					if ((j+1)==Integer.parseInt(m_employeeTimesheet[i].getAreacode().substring(1))){
						data.addElement(new Integer(m_employeeTimesheet[i].getDays()));
					} else {
						data.addElement(" ");
					}
				}
				data.addElement(new Integer(m_employeeTimesheet[i].getReguler()));
				data.addElement(new Integer(m_employeeTimesheet[i].getHoliday()));
				m_table.addRow(data);
				m_table.setModelKu();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public String toDateSelisih(String bulan,String tahun){
		String selisih = ""; 	    	
		if (bulan.equals("1"))    		
			selisih = "'" + (Integer.parseInt(tahun)-1) + "-12-21' and '" + tahun + "-" + bulan + "-20'";   
		else 
			selisih = "'" + tahun + "-" + (Integer.parseInt(bulan)-1) + "-21' and '" + tahun + "-" + bulan + "-20'";		
		return selisih;
	}
	
	
	public String query(long unit,long dept,long act,String tglPilih,String selisih){
		String criteriaSelisih = " and f.entrydate between " + selisih;
		String criteria= "";
		if (unit>0){    		
			criteria += " and g.unit='" + unit + "'";
		}
		if (dept>0){
			
			criteria += " and g.department='" + dept + "'";
		}
		if (act>0){
			
			criteria += " and g.activity='" + act + "'";
		}
		String query = "select a.* from (select a.personal,e.employeeno,e.firstname,e.midlename,e.lastname,j.name jobtitle,d.name  qualification,f.workdescription,h.name as client,g.ipcno,a.days,a.areacode,a.reguler,a.holiday,f.entrydate,g.unit,g.department,g.activity " +
		"from timesheetdetail a, (select a.autoindex,b.tmt from employee a,(select a.autoindex , max(b.tmt) tmt from employeeemployment b, employee a where b.employee=a.autoindex  group by a.autoindex) b " +
		"where a.autoindex=b.autoindex) b, " +
		"qualification d,employee e,timesheet f,projectdata g,customer h, employeeemployment i,jobtitle j " +
		"where a.personal=b.autoindex and  a.qualification=d.autoindex and a.personal=e.autoindex  and  a.timesheet=f.autoindex and f.project=g.autoindex and g.customer=h.autoindex and i.employee=e.autoindex and b.tmt=i.tmt and i.jobtitle=j.autoindex" +
		criteria + criteriaSelisih + ") a " +
		"where not exists (select * from employeeretirement b where a.personal=b.employee and b.tmt<='" + tglPilih + "') order by personal";
		
		return query;
	}
	
	public String queryByCriteria(long unit,long dept,long act,String tglPilih,String selisih,String[] attr,String operator){    	
		String criteriaSelisih = " and f.entrydate between " + selisih;
		String criteria= "";
		String data = "";
		String item  ="";
		if (unit>0){    		
			criteria += " and g.unit='" + unit + "'";
		}
		if (dept>0){
			
			criteria += " and g.department='" + dept + "'";
		}
		if (act>0){
			
			criteria += " and g.activity='" + act + "'";
		}
		if (!attr[0].equals("")){
			if (data.equals(""))
				item = ""; 
			else 
				item = operator;
			data += item +" upper(a.employeeno) like '%" + attr[0].toUpperCase() + "%'";
		}
		if (!attr[1].equals("")){
			if (data.equals(""))
				item = ""; 
			else 
				item = operator;
			data += item +" UPPER(a.firstname& ' ' & a.midlename& ' ' & a.lastname) like '%" + attr[1].toUpperCase() + "%'";
		}
		if (!attr[2].equals("")){
			if (data.equals(""))
				item = ""; 
			else 
				item = operator;
			data += item +" upper(a.jobtitle) like '%" + attr[2].toUpperCase() + "%' ";
		}
		if (!attr[3].equals("")){
			if (data.equals(""))
				item = ""; 
			else 
				item = operator;
			data += item +" UPPER(a.workdescription) like '%" + attr[3].toUpperCase() + "%' ";
		}
		if (!attr[4].equals("")){
			if (data.equals(""))
				item = ""; 
			else 
				item = operator;
			data += item +" UPPER(a.qualification) like '%" + attr[4].toUpperCase() + "%' ";
		}
		data = "and (" + data + ")";  
		if (attr[0].equals("") && attr[1].equals("") && attr[2].equals("") && attr[3].equals("") && attr[4].equals("")){
			data = "";
			
		}    	
		String query = "select a.* from (select a.personal,e.employeeno,e.firstname,e.midlename,e.lastname,j.name jobtitle,d.name  qualification,f.workdescription,h.name as client,g.ipcno,a.days,a.areacode,a.reguler,a.holiday,f.entrydate,g.unit,g.department,g.activity " +
		"from timesheetdetail a, (select a.autoindex,b.tmt from employee a,(select a.autoindex , max(b.tmt) tmt from employeeemployment b, employee a where b.employee=a.autoindex  group by a.autoindex) b " +
		"where a.autoindex=b.autoindex) b, " +
		"qualification d,employee e,timesheet f,projectdata g,customer h, employeeemployment i,jobtitle j " +
		"where a.personal=b.autoindex and  a.qualification=d.autoindex and a.personal=e.autoindex  and  a.timesheet=f.autoindex and f.project=g.autoindex and g.customer=h.autoindex and i.employee=e.autoindex and b.tmt=i.tmt and i.jobtitle=j.autoindex" +
		criteria + criteriaSelisih + ") a " +
		"where not exists (select * from employeeretirement b where a.personal=b.employee and b.tmt<='" + tglPilih + "') " + data + " order by personal";
		
		return query;
	}
	
	private AllowenceMultiplier[] getAllAllowenceMultiplier() throws Exception{
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		return logic.getAllAllowenceMultiplier(m_sessionid," ");    	
	}

	class PayrollTable extends StructuredTable {

		private static final long serialVersionUID = 1L;
		PayrollTableModel m_tableModel;
		AllowenceMultiplier[] areaCode = null;
		public PayrollTable(DefaultMutableTreeNode root) {
			super(root);
			((DefaultTableModel)getModel()).setRowCount(0);	     
		}
		
		public void resetDefaultColumn(AllowenceMultiplier[] areaCode) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)getTableHeaderRoot();
			root.add(new DefaultMutableTreeNode("No"));
			root.add(new DefaultMutableTreeNode("Employee Id"));
			root.add(new DefaultMutableTreeNode("Employee Name"));	      
			root.add(new DefaultMutableTreeNode("Qualification"));
			root.add(new DefaultMutableTreeNode("Work Description"));
			root.add(new DefaultMutableTreeNode("Client"));
			root.add(new DefaultMutableTreeNode("IPC No"));
			DefaultMutableTreeNode nodeTotalDays = new DefaultMutableTreeNode("Total Days");	 
			for (int i = 0 ; i<areaCode.length;i++){
				nodeTotalDays.add(new DefaultMutableTreeNode(areaCode[i].getAreaCode()));		   
			}	      
			root.add(nodeTotalDays);
			
			DefaultMutableTreeNode nodeOverTime= new DefaultMutableTreeNode("Over Time");
			nodeOverTime.add(new DefaultMutableTreeNode("Reguler"));	
			nodeOverTime.add(new DefaultMutableTreeNode("Holiday"));
			root.add(nodeOverTime);
			
		}
		
		public void setModelKu(){
			getColumnModel().getColumn(0).setPreferredWidth(20);
			getColumnModel().getColumn(0).setMinWidth(20);
			getColumnModel().getColumn(0).setMaxWidth(20);
			getColumnModel().getColumn(1).setPreferredWidth(70);
			getColumnModel().getColumn(1).setMinWidth(70);
			getColumnModel().getColumn(1).setMaxWidth(70);	
			getColumnModel().getColumn(7).setPreferredWidth(30);
			getColumnModel().getColumn(7).setMinWidth(30);
			getColumnModel().getColumn(7).setMaxWidth(30);	
			getColumnModel().getColumn(8).setPreferredWidth(30);
			getColumnModel().getColumn(8).setMinWidth(30);
			getColumnModel().getColumn(8).setMaxWidth(30);
			getColumnModel().getColumn(9).setPreferredWidth(50);
			getColumnModel().getColumn(9).setMinWidth(50);
			getColumnModel().getColumn(9).setMaxWidth(50);	
			getColumnModel().getColumn(10).setPreferredWidth(50);
			getColumnModel().getColumn(10).setMinWidth(50);
			getColumnModel().getColumn(10).setMaxWidth(50);
		}
		
		public void clearRow() {
			((DefaultTableModel)getModel()).setRowCount(0);
		}
		
		public void addRow(Vector data) {
			((DefaultTableModel)getModel()).addRow(data);
			
		}
		
		public TableCellRenderer getCellRenderer(int row, int col) {
			if (col == 0)
				return new RightAllignmentCellRenderer();
			else if (col >= 7 )
				return new DoubleCellRenderer(JLabel.CENTER);
			return super.getCellRenderer(row, col);
		}
	}
	
	class PayrollTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}
	
	class RightAllignmentCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;		
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setText((value == null) ? "    " : value.toString());
			setHorizontalAlignment(JLabel.RIGHT);			
			return this;
		}
	}
}
