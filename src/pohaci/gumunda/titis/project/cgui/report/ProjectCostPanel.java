package pohaci.gumunda.titis.project.cgui.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.jgoodies.binding.formatter.EmptyNumberFormatter;

import net.sf.jasperreports.view.JRViewer;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.ProjectContract;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.ProjectDataPicker;

public class ProjectCostPanel extends JPanel implements ActionListener,PropertyChangeListener, TreeSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn;
	long m_sessionid;
	AccountbebanTree m_tree;
	JRViewer m_jrv;
	JPanel m_rightPanel = new JPanel();
	JTextField m_currTxt,m_custTxt,m_ipcTxt,m_startproject,m_endproject,m_validationdate,m_ipcdate;
	JTextArea m_workdescArea,m_actArea;
	DatePicker m_fromDate,m_toDate;
	JButton m_preview;
	JToggleButton m_btnExcel;
	ProjectDataPicker m_projectpicker;
	Account m_account;
	AbstractFormatter m_numberFormatter;
	JFormattedTextField m_amountTxt;
	
	public ProjectCostPanel(Connection conn,long sessionid){
		m_conn = conn;
		m_sessionid = sessionid;
		EmptyNumberFormatter formatter = createNumberFormatA();
		m_numberFormatter = formatter;	
		getJasper();
		constructComponent();
	}
	
	public void constructComponent(){
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		m_tree= new AccountbebanTree(m_conn,m_sessionid);
		m_tree.addTreeSelectionListener(this);
		
		m_preview = new JButton("Preview");
		m_preview.addActionListener(this);
		m_preview.setPreferredSize(new Dimension(30,25));
		
		m_btnExcel = new JToggleButton(new ImageIcon("../images/excell.gif"));
		m_btnExcel.addActionListener(this);
		m_btnExcel.setPreferredSize(new Dimension(20,25));
		
		JLabel fromLbl = new JLabel("From");
		JLabel toLbl = new JLabel("To");
		JLabel projectLbl = new JLabel("Project Code");
		JLabel custLbl = new JLabel("Customer");
		JLabel workdescLbl = new JLabel("Work Desc.");
		JLabel actLbl = new JLabel("Activity Code");
		JLabel ipcnoLbl = new JLabel("IPC No.");
		JLabel ipcdateLbl = new JLabel("IPC* Date");
		JLabel startdateLbl = new JLabel("Project Start Date");
		JLabel enddateLbl = new JLabel("Project End Date");
		JLabel ttlvalueLbl = new JLabel("Total Value");
		JLabel contractvalLbl = new JLabel("Contract Validation");
		
		m_fromDate  = new DatePicker();
		m_fromDate.setDate(new Date());
		m_toDate  = new DatePicker();
		m_toDate.setDate(new Date());
		
		m_projectpicker = new ProjectDataPicker(m_conn,m_sessionid);
		m_projectpicker.addPropertyChangeListener(this);
		
		m_currTxt = new JTextField();
		m_currTxt.setEditable(false);
		m_custTxt = new JTextField();
		m_custTxt.setEditable(false);
		m_amountTxt = new JFormattedTextField(m_numberFormatter);
		m_amountTxt.setEditable(false);
		m_ipcTxt = new JTextField();
		m_ipcTxt.setEditable(false);
		m_startproject = new JTextField();
		m_startproject.setEditable(false);
		
		m_endproject = new JTextField();
		m_endproject.setEditable(false);
		m_validationdate = new JTextField();
		m_validationdate.setEditable(false);
		m_ipcdate = new JTextField();
		m_ipcdate.setEditable(false);
		
		m_workdescArea = new JTextArea();
		JScrollPane workdescScroll = new JScrollPane(m_workdescArea);
		workdescScroll.setPreferredSize(new Dimension(80, 70));
		m_workdescArea.setLineWrap(true);		
		m_workdescArea.setEditable(false);
		
		m_actArea = new JTextArea();
		JScrollPane actScroll = new JScrollPane(m_actArea);
		m_actArea.setLineWrap(true);
		actScroll.setPreferredSize(new Dimension(80, 50));
		m_actArea.setEditable(false);
		
		JPanel compPanel  = new JPanel();
		compPanel.setPreferredSize(new Dimension(100,380));
		compPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(3, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(fromLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		compPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_fromDate, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(toLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		compPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_toDate, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(projectLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		compPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_projectpicker, gridBagConstraints);	
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(custLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		compPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_custTxt, gridBagConstraints);	
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(workdescLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		compPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(workdescScroll, gridBagConstraints);	
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(actLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		compPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(actScroll, gridBagConstraints);	
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(ipcnoLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_ipcTxt, gridBagConstraints);	
		
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(1, 1, 1, 6);
		compPanel.add(ipcdateLbl, gridBagConstraints);	
		
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_ipcdate, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		compPanel.add(startdateLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_startproject, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		compPanel.add(enddateLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_endproject, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		compPanel.add(ttlvalueLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		compPanel.add(m_currTxt, gridBagConstraints);
		
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_amountTxt, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		compPanel.add(contractvalLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_validationdate, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.gridwidth = 7;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		compPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_preview, gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 1;
		compPanel.add(m_btnExcel, gridBagConstraints);
		
		
		JPanel leftTopPanel = new JPanel();
		leftTopPanel.setLayout(new BorderLayout());
		leftTopPanel.add(compPanel,BorderLayout.NORTH);		
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(leftTopPanel,BorderLayout.NORTH);
		leftPanel.add(new JScrollPane(m_tree), BorderLayout.CENTER);
		
		m_rightPanel.setLayout(new BorderLayout());
		m_rightPanel.add(m_jrv,BorderLayout.CENTER);
		
		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(m_rightPanel);
		splitPane.setDividerLocation(350);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		add(splitPane, BorderLayout.CENTER);
	}
	
	public void getJasper(){		
		ProjectCost jasper = new ProjectCost();		
		setPanel(jasper);
	}
	
	private void setPanel(ProjectCost jasper) {
		if (m_jrv != null)
			m_rightPanel.remove(m_jrv);
		m_jrv = jasper.getPrintView();
		m_rightPanel.add(m_jrv, BorderLayout.CENTER);
		m_rightPanel.validate();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_preview){
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
	
	private void getJasper(boolean isExcel) {
		Object obj = m_projectpicker.getObject();
		ProjectCost jasper = new ProjectCost(m_conn,m_sessionid,obj,m_account,m_fromDate,m_toDate,isExcel);
		if (!isExcel)
			setPanel(jasper);
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (m_account== null)
			addInvalid("Account must selected");
		if (m_fromDate.getDate()==null)
			addInvalid("Start Date must selected");
		if (m_toDate.getDate()==null)
			addInvalid("End Date must selected");	
		if (m_projectpicker.getObject()==null)
			addInvalid("Project data must selected");	
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
	
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource()==m_projectpicker){
			paramProjectdata();			
		}		
	}
	
	
	private void paramProjectdata() {
		Object obj = m_projectpicker.getObject();
		if (obj instanceof ProjectData) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
			ProjectData project = (ProjectData) obj;
			ProjectContract projContract = getProjectContract(project);
			
			m_custTxt.setText(project.getCustomer().toString());
			m_workdescArea.setText(project.getWorkDescription());
			m_actArea.setText(project.getActivity().toString());
			m_ipcTxt.setText(project.getIPCNo());
			if (project.getIPCDate()!=null)
				m_ipcdate.setText(dateFormat.format(project.getIPCDate()));
			else 
				m_ipcdate.setText("");
			if (project.getStartdate()!=null)
				m_startproject.setText(dateFormat.format(project.getStartdate()));
			else 
				m_startproject.setText("");
			if (project.getEnddate()!=null)
				m_endproject.setText(dateFormat.format(project.getEnddate()));
			else
				m_endproject.setText("");
			if (projContract!=null){
				if (projContract.getCurrency()!=null)
					m_currTxt.setText(projContract.getCurrency().getSymbol());		
				else 
					m_currTxt.setText("");				
				m_amountTxt.setValue(new Double(projContract.getValue()));
			}else{
				m_currTxt.setText("");	
				m_amountTxt.setValue(null);
			}
			if (project.getValidation()!=null)
				m_validationdate.setText(dateFormat.format(project.getValidation()));
			else
				m_validationdate.setText("");
			
		}else{
			m_custTxt.setText("");
			m_workdescArea.setText("");
			m_actArea.setText("");
			m_ipcTxt.setText("");
			m_ipcdate.setText("");
			m_startproject.setText("");
			m_endproject.setText("");
			m_currTxt.setText("");
			m_amountTxt.setValue(null);
			m_validationdate.setText("");
			
		}
	}
	
	public static EmptyNumberFormatter createNumberFormatA() {
		EmptyNumberFormatter formatter = new EmptyNumberFormatter(new Double(0));
		formatter.setAllowsInvalid(false);
		DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
		decformat.applyPattern("#,##0.00");
		decformat.setMaximumFractionDigits(2);
		formatter.setFormat(decformat);
		return formatter;
	}
	
	private ProjectContract getProjectContract(ProjectData project) {
		GenericMapper mapper = MasterMap.obtainMapperFor(ProjectContract.class);
		mapper.setActiveConn(m_conn);		
		String whereClause = "PROJECT=" + project.getIndex();		
		List projectContract = mapper.doSelectWhere(whereClause);
		if(projectContract.size()>0)
			return (ProjectContract) projectContract.get(0);		
		return null;
	}
	
	DefaultMutableTreeNode m_node = null;
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if(path != null) {
			m_node = (DefaultMutableTreeNode)path.getLastPathComponent();
			setSelectedObject(m_node.getUserObject());
		}
	}
	
	void setSelectedObject(Object obj) {
		if (obj instanceof Account) {
			Account account = (Account) obj;
			setAccount(account);
			System.err.println(account.getName());
		}
		else if (obj instanceof String) {	
		}
	}
	
	public void setAccount(Account acc){
		m_account =acc;		
	}
	
}
