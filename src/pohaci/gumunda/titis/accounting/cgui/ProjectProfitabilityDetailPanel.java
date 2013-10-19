package pohaci.gumunda.titis.accounting.cgui;

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

import net.sf.jasperreports.view.JRViewer;

import pohaci.gumunda.titis.accounting.cgui.report.ProjectProfitabilityDetail;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.ProjectContract;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.ProjectDataPicker;

public class ProjectProfitabilityDetailPanel extends JPanel implements ActionListener,PropertyChangeListener{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn;
	long m_sessionid;
	JRViewer m_jrv;
	JPanel m_rightPanel = new JPanel();
	JTextField m_currTxt,m_custTxt,m_ipcTxt,m_startproject,m_endproject,m_validationdate,m_ipcdate;
	JTextArea m_workdescArea,m_actArea;
	DatePicker m_fromDate;
	JButton m_preview;
	JToggleButton m_btnExcel;
	ProjectDataPicker m_projectpicker;
	AbstractFormatter m_numberFormatter;
	JFormattedTextField m_amountTxt;
	ProjectProfitabilityDetail jasper;
	
	public ProjectProfitabilityDetailPanel(Connection conn,long sessionid){
		m_conn = conn;
		m_sessionid = sessionid;
		jasper = new ProjectProfitabilityDetail(m_conn,m_sessionid);
		/*EmptyNumberFormatter formatter = createNumberFormatA();
		 m_numberFormatter = formatter;*/
		setJasper();
		constructComponent();
	}
	
	public void constructComponent(){
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		m_preview = new JButton("Preview");
		m_preview.addActionListener(this);
		m_btnExcel = new JToggleButton(new ImageIcon("../images/excell.gif"));
		m_btnExcel.addActionListener(this);
		
		JLabel fromLbl = new JLabel("Date");
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
		compPanel.setPreferredSize(new Dimension(100,350));
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
		
		m_preview.setPreferredSize(new Dimension(30,25));
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_preview, gridBagConstraints);
		
		m_btnExcel.setPreferredSize(new Dimension(20,25));
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.weightx = 1.0;
		compPanel.add(m_btnExcel, gridBagConstraints);
		
		JPanel leftTopPanel = new JPanel();
		leftTopPanel.setLayout(new BorderLayout());
		leftTopPanel.add(compPanel,BorderLayout.NORTH);
		
		JPanel emptyPanel  = new JPanel();
		emptyPanel.setPreferredSize(new Dimension(100,350));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(leftTopPanel,BorderLayout.NORTH);
		leftPanel.add(emptyPanel, BorderLayout.CENTER);
		
		m_rightPanel.setLayout(new BorderLayout());
		m_rightPanel.add(m_jrv,BorderLayout.CENTER);
		
		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(m_rightPanel);
		splitPane.setDividerLocation(350);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		add(splitPane, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_preview){
			if (!cekValidity())
				return;
			Object obj = m_projectpicker.getObject();
			jasper.setNonEmpty(obj,m_fromDate,false);
			setPanel(jasper);
		}
		else if (e.getSource() == m_btnExcel){
			if (!cekValidity())
				return;
			Object obj = m_projectpicker.getObject();
			jasper.setNonEmpty(obj,m_fromDate,true);
		}
	}
	
	public void setJasper(){
		jasper.setEmpty();
		setPanel(jasper);
	}
	
	private void setPanel(ProjectProfitabilityDetail jasper) {
		if (m_jrv != null)
			m_rightPanel.remove(m_jrv);
		m_jrv = jasper.getPrintView();
		m_rightPanel.add(m_jrv, BorderLayout.CENTER);
		m_rightPanel.validate();
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		/*if (m_account== null)
		 addInvalid("Account must selected");*/
		if (m_fromDate.getDate()==null)
			addInvalid("Start Date must selected");
		/*if (m_toDate.getDate()==null)
		 addInvalid("End Date must selected");*/
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
	
	private ProjectContract getProjectContract(ProjectData project) {
		GenericMapper mapper = MasterMap.obtainMapperFor(ProjectContract.class);
		mapper.setActiveConn(m_conn);
		String whereClause = "PROJECT=" + project.getIndex();
		List projectContract = mapper.doSelectWhere(whereClause);
		if(projectContract.size()>0)
			return (ProjectContract) projectContract.get(0);
		return null;
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
	
}
