package pohaci.gumunda.titis.hrm.cgui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.*;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.EmployeePayroll;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.cgui.DoubleCellRenderer;

public class PayrollTaxArt21SubmitPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	JTextField m_statusTextField, m_submitdateTextField;
	UnitPicker m_unitPicker;
	JMonthChooser m_monthComboBox;
	JSpinField m_yearField;
	//TaxArt21ComponentPicker m_componentPicker;
	TaxArt21AccountPicker m_accountPicker;
	JButton m_viewBt, m_submitBt;
	JToggleButton m_excellBt, m_searchBt, m_previewBt;
	private PayrollTable m_table;
	Employee_n[] m_emps = null;
	boolean m_show = false;
	private Unit unit;
	private Vector vector =  new Vector();	
	private EmployeePayroll empPayroll;
	//private Vector data;
	private Account taxAccount;
	private TaxArt21Component[] taxArt21ComponentS;	
	private Vector m_vector;
	private TaxArt21Submit taxSubmitted;
	
	DefaultTableModel m_model;
	DecimalFormat m_formatDesimal;
	
	String[] rpt = {"","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
	
	String[] rptTtl = {"","","","","","","","","","","","","","","","","","","","","","",""};
	
	public PayrollTaxArt21SubmitPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		
		constructComponent();
		setEditable(false);
	}
	
	void constructComponent() {
		m_excellBt = new javax.swing.JToggleButton(new ImageIcon("../images/excell.gif"));    
		m_excellBt.addActionListener(this);
		m_searchBt = new javax.swing.JToggleButton(new ImageIcon("../images/filter2.gif")); 
		m_searchBt.addActionListener(this);
		m_previewBt = new javax.swing.JToggleButton(new ImageIcon("../images/filter.gif"));
		m_previewBt.addActionListener(this);
		
		m_statusTextField = new JTextField();
		m_statusTextField.setEditable(false);
		m_submitdateTextField = new JTextField();
		m_submitdateTextField.setEditable(false);
		m_unitPicker = new UnitPicker(m_conn, m_sessionid);
		
		m_monthComboBox=new JMonthChooser(JMonthChooser.NO_SPINNER);
		
		m_yearField = new JSpinField();
		m_yearField.setMaximum(9999);
		m_yearField.setValue(new java.util.GregorianCalendar().get(java.util.Calendar.YEAR));
		//m_componentPicker = new TaxArt21ComponentPicker(m_conn,m_sessionid);
		m_accountPicker = new TaxArt21AccountPicker(m_conn, m_sessionid);
		
		m_viewBt = new JButton("View");
		m_viewBt.addActionListener(this);
		m_submitBt = new JButton("Submit");
		m_submitBt.setEnabled(false);
		m_submitBt.addActionListener(this);
		
		JLabel statuslabel = new JLabel("Status");
		JLabel submitlabel = new JLabel("Submited Date");
		JLabel unitlabel = new JLabel("Unit Code");
		JLabel monthlabel = new JLabel("Month");
		JLabel yearlabel = new JLabel("Year");
		JLabel viewlabel = new JLabel("Viewed by Account");
		
		JPanel centerPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel formPanel = new JPanel();
		JPanel viewPanel = new JPanel();
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(m_excellBt);    
		buttonPanel.add(m_previewBt);
		buttonPanel.add(m_searchBt);
		buttonPanel.add(m_viewBt);
		buttonPanel.add(m_submitBt);
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		formPanel.setLayout(new GridBagLayout());
		Misc.setGridBagConstraints(formPanel, statuslabel, gridBagConstraints, 0, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_statusTextField, gridBagConstraints, 2, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, monthlabel, gridBagConstraints, 3, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 3, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 4, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_monthComboBox, gridBagConstraints, 5, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, submitlabel, gridBagConstraints, 0, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_submitdateTextField, gridBagConstraints, 2, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, yearlabel, gridBagConstraints, 3, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 3, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 4, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_yearField, gridBagConstraints, 5, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, unitlabel, gridBagConstraints, 0, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 2, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, new JLabel(" "), gridBagConstraints, 1, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, m_unitPicker, gridBagConstraints, 2, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(formPanel, viewPanel, gridBagConstraints, 0, 3,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		viewPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		Misc.setGridBagConstraints(viewPanel, viewlabel, gridBagConstraints, 0, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(viewPanel, new JLabel(" "), gridBagConstraints, 1, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		//Misc.setGridBagConstraints(viewPanel, m_componentPicker, gridBagConstraints, 2, 0,
		//                           GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(viewPanel, m_accountPicker, gridBagConstraints, 2, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		
		northPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		JPanel northleftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		northleftPanel.add(northPanel);
		Misc.setGridBagConstraints(northPanel, buttonPanel, gridBagConstraints, 0, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
		
		formPanel.setPreferredSize(new Dimension(450, 100));
		Misc.setGridBagConstraints(northPanel, formPanel, gridBagConstraints, 0, 1,
				GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		Misc.setGridBagConstraints(northPanel, new JPanel(), gridBagConstraints, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, new Insets(1, 1, 1, 1));
		
		m_table = new PayrollTable(new DefaultMutableTreeNode("Column"));
		
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEtchedBorder());
		centerPanel.add(
				new JScrollPane(m_table), BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(northleftPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
	}
	
	public void setEditable(boolean editable){
		m_searchBt.setEnabled(editable);
		m_excellBt.setEnabled(editable);
		m_previewBt.setEnabled(editable);
	}
	
	void jaspertReport(boolean check){
		int month = m_monthComboBox.getMonth();
		int year = m_yearField.getValue();
		Unit unit = (Unit) m_unitPicker.getObject();
		String tgl = getMount(month) + " - " + year;      	      
		if(unit!=null){	    	  
			month = month + 1;
			new pohaci.gumunda.titis.hrm.cgui.report.HRMPayrollTaxArt21Jasper(this,unit.getDescription(),tgl,m_emps,check);
		}else{
			JOptionPane.showMessageDialog(this, "Define unit",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}		 
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_excellBt){
			jaspertReport(true);
		}else if (e.getSource() == m_previewBt){
			jaspertReport(false);
		}else if (e.getSource() == m_viewBt){	
			view(true,null,"");	  
		} else if (e.getSource() == m_submitBt){	
			int val = JOptionPane.showConfirmDialog(this,
					"System will submit data for selected tax account  " + 
					"\n" + 
					"and for all employee within selected unit code." +
					"\n" + 
					"Continue to submit?", 
					"Submit Confirmation", JOptionPane.YES_NO_OPTION);
			if(val==JOptionPane.YES_OPTION)	
				submit();
		}else if (e.getSource()==m_searchBt){
			if(!m_show) {
				m_show = true;
				SearchEmployeeDlg dlg = new SearchEmployeeDlg(
						pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), 
						this,m_conn, m_sessionid);
				dlg.setVisible(true);
				m_show = false;
			}
		}
	}
	
	public void submit(){
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		if (taxSubmitted != null) {
			Date now = new Date();
			taxSubmitted.setStatus(PayrollSubmitStatus.SUBMITTED);
			taxSubmitted.setSubmittedDate(now);
			try {
				taxSubmitted = logic.createTaxArt21Submit(m_sessionid,
						IDBConstants.MODUL_MASTER_DATA, taxSubmitted);
				setTextfieldStatus(taxSubmitted);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/*HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		 EmployeePayrollTaxArt21[] Submit=null;
		 Submit = new EmployeePayrollTaxArt21[vector.size()];
		 vector.copyInto(Submit);*/
		
		
		/* if(submit!=null){
		 logic.createTaxArt21Submit(m_sessionid, IDBConstants.MODUL_MASTER_DATA, submit);
		 }*/
		
		/*if(Submit!=null){
		 try {	
		 if(m_updateFlag){
		 for (int i = 0; i < Submit.length; i++) {
		 logic.updateEmployeePayroll(m_sessionid,
		 IDBConstants.MODUL_MASTER_DATA,Submit[i]);
		 }
		 return;
		 }else{
		 logic.createEmployeePayrollSubmit(m_sessionid,
		 IDBConstants.MODUL_MASTER_DATA,Submit);
		 }
		 } catch (Exception e1) {
		 e1.printStackTrace();
		 }
		 }*/
		//setTextfieldStatus(Submit[0]);
	}
	
	public void setTextfieldStatus(TaxArt21Submit taxSubmitted){
		if(taxSubmitted.getStatus()==PayrollSubmitStatus.NOTSUBMITTED){
			m_statusTextField.setText(" Not Submitted");
			m_submitdateTextField.setText("");
		}else if(taxSubmitted.getStatus()==PayrollSubmitStatus.SUBMITTED){
			m_statusTextField.setText(" Submitted");
			String dateString =new SimpleDateFormat("dd-MM-yyyy").format(taxSubmitted.getSubmittedDate());
			m_submitdateTextField.setText(" "+dateString);
			m_submitBt.setEnabled(false);
		}else if(taxSubmitted.getStatus()==PayrollSubmitStatus.VERIFIED){
			m_statusTextField.setText(" Verified");
			String dateString =new SimpleDateFormat("dd-MM-yyyy").format(taxSubmitted.getSubmittedDate());
			m_submitdateTextField.setText(" "+dateString);
			m_submitBt.setEnabled(false);
		}else if(taxSubmitted.getStatus()==PayrollSubmitStatus.POSTED){
			m_statusTextField.setText(" Posted");
			String dateString =new SimpleDateFormat("dd-MM-yyyy").format(taxSubmitted.getSubmittedDate());
			m_submitdateTextField.setText(" "+dateString);
			m_submitBt.setEnabled(false);
		}
	}
	
	public void view(boolean cekEmployee,String[] attr,String operator){	  	  	  
		unit = (Unit) m_unitPicker.getObject();
		taxAccount = (Account) m_accountPicker.getObject();
		taxArt21ComponentS = getTaxArt21ComponentsByAccount(taxAccount);
		
	     String date = m_yearField.getValue() + "-" + (m_monthComboBox.getMonth() + 2) + "-1";
			if (m_monthComboBox.getMonth()==11)
				date = (m_yearField.getValue()+1) + "-" + (m_monthComboBox.getMonth() + 1) + "-1";
	
		java.util.ArrayList list = new java.util.ArrayList();
		if (unit ==null)
			list.add("Unit");
		if (taxAccount == null)
			list.add("Tax Art 21 Account");
		String strexc = "Please insert :\n";
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if (exception.length > 0) {
			for (int i = 0; i < exception.length; i++)
				strexc += exception[i] + "\n";
			JOptionPane.showMessageDialog(this, strexc, "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		//taxArt21ComponentRootS = m_componentPicker.getRootTaxArt21Components();  
		if (cekEmployee){
			try{
				m_emps = getEmployee(date, unit.getIndex());
			}catch(Exception ex){		
				ex.printStackTrace();
			}
			tableHeader(taxArt21ComponentS);
			tableBody();
			setEditable(true);
		} else {
			try{
				m_emps = getEmployeeBy_Criteria(date, unit.getIndex(),attr,operator);
			}catch(Exception ex){		
				ex.printStackTrace();
			}
			tableHeader(taxArt21ComponentS);
			tableBody();
			m_table.setModelKu();
		}
		
		setCursor(Cursor.getDefaultCursor());
	}
	
	private TaxArt21Component[] getTaxArt21ComponentsByAccount(
			Account account) {
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			TaxArt21Component[] components = logic.getTaxArt21ComponentsByAccount(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, account.getIndex());
			
			return components;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	
	public int getMonth(){
		return m_monthComboBox.getMonth() + 1;
	}
	public int getYear(){
		return m_yearField.getValue();
	}
	
	/*private EmployeePayrollTaxArt21  getEmployeePayrollTaxArt21(){
		EmployeePayrollTaxArt21 employeepayrollSubmit=new EmployeePayrollTaxArt21();
		employeepayrollSubmit.setMonth(getMonth());
		employeepayrollSubmit.setYear(getYear());
		employeepayrollSubmit.setUnit(unit);
		employeepayrollSubmit.setStatus(PayrollSubmitStatus.SUBMITTED);
		Calendar calendar=Calendar.getInstance();
		employeepayrollSubmit.setSubmittedDate(calendar.getTime());
		employeepayrollSubmit.setPayrollType(PayrollSubmitType.TAXART21_SUBMIT);
		return employeepayrollSubmit;
	}*/
	
	public EmployeePayrollTaxArt21 dummyClone(EmployeePayrollTaxArt21 payrollData) {
		EmployeePayrollTaxArt21 employeePayrollSubmit=new EmployeePayrollTaxArt21();
		employeePayrollSubmit.setEmployee_n(payrollData.getEmployee_n());
		employeePayrollSubmit.setJurnalID(payrollData.getJurnalID());
		employeePayrollSubmit.setMonth(payrollData.getMonth());
		employeePayrollSubmit.setPaymentPeriode(payrollData.getPaymentPeriode());
		employeePayrollSubmit.setStatus(payrollData.getStatus());
		employeePayrollSubmit.setSubmittedDate(payrollData.getSubmittedDate());
		employeePayrollSubmit.setUnit(payrollData.getUnit());
		employeePayrollSubmit.setYear(payrollData.getYear());
		employeePayrollSubmit.setPayrollType(6);
		
		return employeePayrollSubmit;
	}
	
	public void tableBody(){
		taxSubmitted = null;
		taxSubmitted = getTaxArt21Submit(getMonth(), getYear(), unit, taxAccount);
		
		empPayroll = new EmployeePayroll(m_conn, getYear(), getMonth(), m_sessionid);
		
		if(taxSubmitted!=null){
			if(taxSubmitted.getStatus()>PayrollSubmitStatus.NOTSUBMITTED){
				m_submitBt.setEnabled(false);
				setTextfieldStatus(taxSubmitted);
			}else{
				System.err.println("STATUS TIDAK BERUBAH!!!");
			}
		}else{
			m_submitBt.setEnabled(true);
			m_statusTextField.setText(" Not Submitted");
			m_submitdateTextField.setText("");
		}	  
		
		if(m_submitBt.isEnabled())
			getEnabledSubmit();
		else
			getDisabledSubmit();
	}
	
	boolean m_cekReport = false;
	public void tableBodyReport(DefaultTableModel model, boolean excel){
		if(excel)
			m_formatDesimal = new DecimalFormat("###.00");
		else m_formatDesimal = new DecimalFormat("#,##0.00");
		m_model = model;
		m_cekReport = true;		
		tableBody();
	}
	
	private TaxArt21Submit getTaxArt21Submit(int month, int year, Unit unitSelected,
			Account accountSelected) {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		try {
			return logic.getFullTaxArt21Submit(m_sessionid, IDBConstants.MODUL_MASTER_DATA, month, year, 
					unitSelected, accountSelected);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*private EmployeePayrollSubmit[] getEmployeePayrollSubmit(EmployeePayrollTaxArt21 employeepayrollSubmit)  {
		HRMBusinessLogic m_logic = new HRMBusinessLogic(m_conn);
		try {
			return m_logic.getEmployeePayrollSubmit(m_sessionid,IDBConstants.MODUL_MASTER_DATA,employeepayrollSubmit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	//used later
	public void getDisabledSubmit(){
		presentingData();
	}
	private void presentingData() {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		//long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		helper.prepareColumnHelper(taxArt21ComponentS);
		
		
		TaxArt21SubmitEmployeeDetail[] employeeDetails = taxSubmitted.getEmployeeDetails();
		employeeDetails = sortedDetails(employeeDetails);
		
		double[] ttl = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		for(int i=0; i<employeeDetails.length; i++){
			
			m_vector = new Vector();
			
			
			TaxArt21SubmitEmployeeDetail employeeDetail = employeeDetails[i];
			Employee_n emp = findEmp(employeeDetail);
			if (emp != null) {
				if (m_cekReport) {
					int n = 0;
					rpt[n++] = String.valueOf(i + 1);
					rpt[n++] = emp.getEmployeeno();
					rpt[n++] = emp.getFirstname() + " "
							+ emp.getMidlename() + " "
							+ emp.getLastname();
					rpt[n++] = emp.getJobtitle();
					rpt[n++] = emp.getPTKPStatus();

					TaxArt21SubmitComponentDetail[] componentDetails = employeeDetail
							.getComponentDetails();
					double monthly = 0;
					for (int j = 0; j < componentDetails.length; j++) {
						TaxArt21SubmitComponentDetail componentDetail = componentDetails[j];
						int colIdx = helper.getColumnIndex(componentDetail
								.getComponent().getIndex());
						if (colIdx != -1) {
							Double value = componentDetail.getValue();
							monthly += value.doubleValue();
							System.err.println(j + ": " + value.doubleValue());
							String data = m_formatDesimal.format(value
									.doubleValue());
							if (value.doubleValue() < 0)
								data = "("
										+ m_formatDesimal.format(-value
												.doubleValue()) + ")";
							rpt[n++] = data;

							ttl[j] += value.doubleValue();
							rptTtl[j] = m_formatDesimal.format(ttl[j]);
							if (ttl[j] < 0)
								rptTtl[j] = "("
										+ m_formatDesimal.format(-ttl[j]) + ")";
						}
					}
					addTaxElementReport(monthly, employeeDetail.getEmployee()
							.getAutoindex(), ttl, n);
					m_model
							.addRow(new Object[] { rpt[0], rpt[1], rpt[2],
									rpt[3], rpt[4], rpt[5], rpt[6], rpt[7],
									rpt[8], rpt[9], rpt[10], rpt[11], rpt[12],
									rpt[13], rpt[14], rpt[15], rpt[16],
									rpt[17], rpt[18], rpt[19], rpt[20],
									rpt[21], rpt[22], rpt[23], rpt[24],
									rpt[25], rpt[26], rpt[27], new Integer(1) });
					if (i == (employeeDetails.length - 1)) {
						m_model.addRow(new Object[] { "", "", "",
								"Total Amount (IDR)", "", rptTtl[0], rptTtl[1],
								rptTtl[2], rptTtl[3], rptTtl[4], rptTtl[5],
								rptTtl[6], rptTtl[7], rptTtl[8], rptTtl[9],
								rptTtl[10], rptTtl[11], rptTtl[12], rptTtl[13],
								rptTtl[14], rptTtl[15], rptTtl[16], rptTtl[17],
								rptTtl[18], rptTtl[19], rptTtl[20], rptTtl[21],
								rptTtl[22], new Integer(2) });
						m_cekReport = false;
					}

				} else {
					tableContent(emp, no++);
					leftMargin = m_vector.size();

					TaxArt21SubmitComponentDetail[] componentDetails = employeeDetail
							.getComponentDetails();
					double monthly = 0;
					for (int j = 0; j < componentDetails.length; j++) {
						TaxArt21SubmitComponentDetail componentDetail = componentDetails[j];

						int colIdx = helper.getColumnIndex(componentDetail
								.getComponent().getIndex());
						if (colIdx != -1) {
							Double value = componentDetail.getValue();
							monthly += value.doubleValue();
							helper.addDataAtColumn(m_vector, colIdx + leftMargin,
									value);
						}
					}
					if (m_vector.size() > 0) {
						m_vector = addTaxElement(monthly, m_vector, employeeDetail
								.getEmployee().getAutoindex());
						m_table.addRow(m_vector);
					}
					m_table.setModelKu();
				}
			}
		}
		
		/*for(int i=0;i<submit.length;i++){  
		 if(submit[i]!=null){	
		 if(tempIndex!=submit[i].getEmployeeIndex()){
		 if (m_data.size()>0)
		 m_table.addRow(m_data);
		 m_data = new Vector();
		 tableContent( submit[i], no++);
		 tempIndex = submit[i].getEmployeeIndex();
		 leftMargin = m_data.size();
		 } 
		 int colIdx = helper.getColumnIndex(submit[i].getTaxArt21Component().getIndex());
		 if(colIdx!=-1){
		 double value = submit[i].getValue();
		 Object content=null;
		 if(value!= -1)
		 content = new Double(value);
		 else
		 content = "";	
		 
		 helper.addDataAtColumn(m_data, colIdx + leftMargin, content);
		 }
		 }
		 }//end  for
		 */		/*if (m_data.size()>0) {
		 m_data.addElement("");
		 m_table.addRow(m_data);
		 }*/
		
		
	}
	
	private TaxArt21SubmitEmployeeDetail[] sortedDetails(TaxArt21SubmitEmployeeDetail[] employeeDetails) {
		List list = new ArrayList();
		for(int i=0; i<employeeDetails.length; i++){
			if(findEmp(employeeDetails[i])!=null)
				list.add(employeeDetails[i]);
		}
		TaxArt21SubmitEmployeeDetail[] details = (TaxArt21SubmitEmployeeDetail[]) list.toArray(new TaxArt21SubmitEmployeeDetail[list.size()]);
		return details;
	}

	private Employee_n findEmp(TaxArt21SubmitEmployeeDetail employeeDetail) {
		for(int i=0; i<m_emps.length; i++){
			if(m_emps[i].getAutoindex()==employeeDetail.getEmployee().getAutoindex())
				return m_emps[i];
		}
		return null;
	}

	private void tableContent(Employee_n emp, int i) {
		if(emp==null)
			return;
		m_vector.addElement(String.valueOf(i + 1));
		String name = emp.getFirstname() + " " + emp.getMidlename() + " " + emp.getLastname();
		m_vector.addElement(name); 
		m_vector.addElement(emp.getJobtitle());
		m_vector.addElement(emp.getPTKPStatus());
	}
	//used later
	public void getEnabledSubmit(){
		// ini gaya baru
		taxSubmitted = new TaxArt21Submit();
		taxSubmitted.setMonthSubmitted(getMonth());
		taxSubmitted.setYearSubmitted(getYear());
		taxSubmitted.setUnit(unit);
		taxSubmitted.setTaxAccount(taxAccount);
		
		Vector employeeDetailVector = new Vector();
		
		vector.clear();//vector is used for submit
		double[] ttl = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		for(int i=0; i<m_emps.length; i++){
			//-- initial employee data
			m_vector = new Vector();
			EmployeePayroll.TaxArt21CalcResult[] results=null;
			if (m_cekReport){				
				int n=0;
				rpt[n++]=String.valueOf(i+1);				
				rpt[n++] = m_emps[i].getEmployeeno();
				rpt[n++] = m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + 
				m_emps[i].getLastname();
				rpt[n++]=m_emps[i].getJobtitle();
				rpt[n++]=m_emps[i].getPTKPStatus();
				
				results = empPayroll
				.calcTaxArt21Component(getMonth(), getYear(), unit, m_emps[i],
						taxArt21ComponentS);
				double monthly = 0;
				for(int f=0;f<taxArt21ComponentS.length;f++){					
					for(int h =0;h<results.length;h++){
						if(taxArt21ComponentS[f].m_index == results[h].component.getIndex()){							
							Double value = results[h].value;
							if(value!=null){
								monthly += value.doubleValue();
								String data = m_formatDesimal.format(value.doubleValue());
								if (value.doubleValue()<0)
									data = "(" +m_formatDesimal.format(-value.doubleValue()) + ")";
								rpt[n++] = data;
								ttl[f] += value.doubleValue();
								rptTtl[f] = m_formatDesimal.format(ttl[f]);
								if (ttl[f]<0)
									rptTtl[f] = "(" +m_formatDesimal.format(-ttl[f]) + ")";																
							}else{
								//double val = 0;																
								rpt[n++] = "";
								rptTtl[f] = "";
							}
							break;						
						}
					}
				}
				addTaxElementReport(monthly, m_emps[i].getAutoindex(),ttl,n);
				
				m_model.addRow(new Object[]{rpt[0],rpt[1],rpt[2],rpt[3],rpt[4],rpt[5],rpt[6],rpt[7],rpt[8],
						rpt[9],rpt[10],rpt[11],rpt[12],rpt[13],rpt[14],rpt[15],rpt[16],rpt[17],rpt[18],rpt[19],rpt[20],
						rpt[21],rpt[22],rpt[23],rpt[24],rpt[25],rpt[26],rpt[27],new Integer(1)});
				if (i==(m_emps.length-1)){
					m_model.addRow(new Object[]{"","","","Total Amount (IDR)","",rptTtl[0],rptTtl[1],rptTtl[2],rptTtl[3],rptTtl[4],
							rptTtl[5],rptTtl[6],rptTtl[7],rptTtl[8],rptTtl[9],rptTtl[10],rptTtl[11],rptTtl[12],
							rptTtl[13],rptTtl[14],rptTtl[15],rptTtl[16],rptTtl[17],rptTtl[18],rptTtl[19],
							rptTtl[20],rptTtl[21],rptTtl[22],new Integer(2)});
					m_cekReport = false;		
				}
			}else{
				
				
				m_vector.addElement(String.valueOf(i+1));
				m_vector.addElement(m_emps[i].getFirstname() + " " + m_emps[i].getMidlename() + " " + 
						m_emps[i].getLastname());
				m_vector.addElement(m_emps[i].getJobtitle());	
				m_vector.addElement(m_emps[i].getPTKPStatus());
				//---
				
				//rather taxArt21ComponentsRootS than taxArt21Components, to take all result
				// coba taxArt21Components aza
				results = empPayroll
				.calcTaxArt21Component(getMonth(), getYear(), unit, m_emps[i],
						taxArt21ComponentS);
				
				//-- for table
				double monthly = 0;
				for(int f=0;f<taxArt21ComponentS.length;f++){
					
					for(int h =0;h<results.length;h++){
						System.err.println(f + "=" + h);
						if(taxArt21ComponentS[f].m_index == results[h].component.getIndex()){
							/*StringTokenizer token = new StringTokenizer(taxArt21ComponentS[f].m_code,".");
							 if(token.countTokens()>0){
							 
							 if(new Double(token.nextToken()).doubleValue()==1){*/
							Double value = results[h].value;
							if(value!=null){
								m_vector.addElement(value);
								monthly += value.doubleValue();
							}else{
								m_vector.addElement(value);
							}
							break;
							/*	  }
							 }*/
						}
					}
				}
				m_vector = addTaxElement(monthly, m_vector, m_emps[i].getAutoindex());
				
				
				m_table.addRow(m_vector);
				m_table.setModelKu();
			}
			
			//-- for submit
			// masih dengan taxArt21Components
			
			
			
			
			// ini anaknya
			TaxArt21SubmitEmployeeDetail employeeDetail = new TaxArt21SubmitEmployeeDetail();
			employeeDetail.setEmployee(m_emps[i]);
			employeeDetail.setJobTitle(m_emps[i].getJobtitle());
			
			Vector componentDetailVector = new Vector();
			
			for(int y=0; y<taxArt21ComponentS.length; y++){
				TaxArt21SubmitComponentDetail componentDetail = new TaxArt21SubmitComponentDetail();
				for (int x = 0; x < results.length; x++) {
					if (taxArt21ComponentS[y].getIndex() == results[x].component
							.getIndex()) {
						componentDetail.setComponent(taxArt21ComponentS[y]);
						componentDetail.setValue(results[x].value);
						break;
					}
				}
				componentDetailVector.add(componentDetail);
			}
			
			
			TaxArt21SubmitComponentDetail[] componentDetails = (TaxArt21SubmitComponentDetail[]) componentDetailVector
			.toArray(new TaxArt21SubmitComponentDetail[componentDetailVector
			                                           .size()]);
			employeeDetail.setComponentDetails(componentDetails);
			
			employeeDetailVector.add(employeeDetail);
			
			/*EmployeePayrollTaxArt21[] PayrollSubmit = new EmployeePayrollTaxArt21[taxArt21ComponentS.length];
			 employeepayrollSubmit.setEmployee_n(m_emps[i]);
			 for(int f=0;f<taxArt21ComponentS.length;f++){
			 PayrollSubmit[f]=dummyClone(employeepayrollSubmit);
			 for(int h =0;h<results.length;h++){
			 if(taxArt21ComponentS[f].m_index==results[h].component.getIndex()){
			 if (results[h].value != null) {
			 employeepayrollSubmit
			 .setValue(0);
			 
			 PayrollSubmit[f].setValue((double) results[h].value
			 .doubleValue());
			 } else {
			 employeepayrollSubmit
			 .setValue(0);
			 
			 PayrollSubmit[f].setValue(0);
			 }
			 PayrollSubmit[f].setTaxArt21Component(taxArt21ComponentS[f]);
			 vector.addElement(PayrollSubmit[f]);
			 break;
			 }
			 }
			 }*/
			
			
			TaxArt21SubmitEmployeeDetail[] details = (TaxArt21SubmitEmployeeDetail[]) employeeDetailVector.toArray(new TaxArt21SubmitEmployeeDetail[employeeDetailVector.size()]);
			taxSubmitted.setEmployeeDetails(details);
		}
	}
	
	private void addTaxElementReport(double monthly, long autoindex, double[] ttl,int pt) {
		
		String data = m_formatDesimal.format(monthly);							
		if (monthly<0)
			data = "(" + m_formatDesimal.format(-monthly) + ")";
		rpt[pt++]=data;
		ttl[16]+=monthly;
		rptTtl[16]=m_formatDesimal.format(ttl[16]);
		if (ttl[16]<0)
			rptTtl[16]="(" + m_formatDesimal.format(-ttl[16]) + ")";
		
		
		double yearly = monthly * 12;		
		data = m_formatDesimal.format(yearly);							
		if (yearly<0)
			data = "(" + m_formatDesimal.format(-yearly) + ")";
		rpt[pt++]=data;
		ttl[17] +=yearly;
		rptTtl[17]=m_formatDesimal.format(ttl[17]);
		if (ttl[17]<0)
			rptTtl[17]="(" + m_formatDesimal.format(-ttl[17]) + ")";
		
		double ptkp = getYearlyPTKP(autoindex);
		data = m_formatDesimal.format(ptkp);							
		if (ptkp<0)
			data = "(" + m_formatDesimal.format(-ptkp) + ")";
		rpt[pt++]=data;
		ttl[18] +=ptkp;
		rptTtl[18]=m_formatDesimal.format(ttl[18]);
		if (ttl[18]<0)
			rptTtl[18]="(" + m_formatDesimal.format(-ttl[18]) + ")";
		
		double pkp = yearly - ptkp;
		if(pkp<0)
			pkp = 0;
		data = m_formatDesimal.format(pkp);							
		if (pkp<0)
			data = "(" + m_formatDesimal.format(-pkp) + ")";
		rpt[pt++]=data;
		ttl[19] +=pkp;
		rptTtl[19]=m_formatDesimal.format(ttl[19]);
		if (ttl[19]<0)
			rptTtl[19]="(" + m_formatDesimal.format(-ttl[19]) + ")";
		
		NumberRounding rounding = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND_DOWN, -3);
		double roundingValue = rounding.round(pkp);
		data = m_formatDesimal.format(roundingValue);							
		if (roundingValue<0)
			data = "(" + m_formatDesimal.format(-roundingValue) + ")";
		rpt[pt++]=data;		
		ttl[20] +=roundingValue;
		rptTtl[20]=m_formatDesimal.format(ttl[20]);
		if (ttl[20]<0)
			rptTtl[20]="(" + m_formatDesimal.format(-ttl[20]) + ")";
		
		TaxArt21Tariff[] tariffs = getTariffs();
		double yearlyTax = getYearlyTax(roundingValue, tariffs);
		data = m_formatDesimal.format(yearlyTax);							
		if (yearlyTax<0)
			data = "(" + m_formatDesimal.format(-yearlyTax) + ")";
		rpt[pt++]=data;
		ttl[21] +=yearlyTax;
		rptTtl[21]=m_formatDesimal.format(ttl[21]);
		if (ttl[21]<0)
			rptTtl[21]="(" + m_formatDesimal.format(-ttl[21]) + ")";		
		
		double monthlyTax = yearlyTax / 12;
		data = m_formatDesimal.format(monthlyTax);							
		if (monthlyTax<0)
			data = "(" + m_formatDesimal.format(-monthlyTax) + ")";
		rpt[pt++]=data;		
		ttl[22] +=monthlyTax;
		rptTtl[22]=m_formatDesimal.format(ttl[22]);
		if (ttl[22]<0)
			rptTtl[22]="(" + m_formatDesimal.format(-ttl[22]) + ")";
	}
	
	private Vector addTaxElement(double monthly, Vector dataVector, long autoindex) {
		
		dataVector.addElement(new Double(monthly));
		double yearly = monthly * 12;
		dataVector.addElement(new Double(yearly));
		
		double ptkp = getYearlyPTKP(autoindex);
		dataVector.addElement(new Double(ptkp));
		
		double pkp = yearly - ptkp;
		if(pkp<0)
			pkp = 0;
		dataVector.addElement(new Double(pkp));
		
		NumberRounding rounding = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND_DOWN, -3);
		double roundingValue = rounding.round(pkp);
		dataVector.addElement(new Double(roundingValue));
		
		TaxArt21Tariff[] tariffs = getTariffs();
		double yearlyTax = getYearlyTax(roundingValue, tariffs);
		dataVector.addElement(new Double(yearlyTax));
		
		double monthlyTax = yearlyTax / 12;
		dataVector.addElement(new Double(monthlyTax));
		
		return dataVector;
	}
	
	private TaxArt21Tariff[] getTariffs() {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		TaxArt21Tariff[] tariffs = null;
		try {
			tariffs = logic.getAllTaxArt21Tariff(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tariffs;
	}
	
	private double getYearlyTax(double value, TaxArt21Tariff[] tariffs) {
		double residue = value;
		double tax = 0;
		double taxed = 0;
		
		for(int i=0; i<tariffs.length; i++){
			TaxArt21Tariff tariff = tariffs[i];
			
			double range = tariff.getMaximum() - tariff.getMinimum() + 1;
			
			if(residue>range){
				taxed = range;
			}else{
				taxed = residue;
			}
			residue -= taxed;
			
			double pctTariff = tariff.getTariff();
			
			tax += (taxed * (pctTariff / 100));
		}
		return tax;
	}
	
	private double getYearlyPTKP(long autoindex) {
		HRMSQLSAP sql = new HRMSQLSAP();
		double value = 0;
		try {
			value = sql.getPTKPbyEmployeeIndex(m_conn, autoindex);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public void tableHeader(TaxArt21Component[] taxArt21Component){	  
		TaxArt21ComponentSelectionTree plateTree = 
			new TaxArt21ComponentSelectionTree(m_conn, m_sessionid, taxArt21Component);
		if (taxArt21Component.length > 0) {
			DefaultMutableTreeNode rootTable = (DefaultMutableTreeNode)m_table.getTableHeaderRoot();
			rootTable.removeAllChildren();  
			m_table.resetDefaultColumn();
			rootTable.add((DefaultMutableTreeNode)plateTree.getModel().getRoot());
			m_table.rebuildTable();
			m_table.clearRow();
		}
	}
	
	private Employee_n[] getEmployee(String date, long unitId) throws Exception {
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		Employee_n[] emps = logic.getEmployeeAndPTKPByUnit(m_sessionid,
				IDBConstants.MODUL_MASTER_DATA, unitId, date);
		return emps;
	}
	
	private Employee_n[] getEmployeeBy_Criteria(String date, long unitId,String[] attr,String operator) throws Exception{
		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		
		String data = "";
		String item = "";
		if (!attr[0].equals("")){
			if (data.equals(""))
				item = ""; 
			else 
				item = operator;
			data += item + "UPPER(emp.firstname& ' ' & emp.midlename& ' ' & emp.lastname) " +
			"LIKE '%"+ attr[0].toUpperCase() +"%' ";
		} 
		if (!attr[1].equals("")){
			if (data.equals(""))
				item = ""; 
			else 
				item = operator;
			data +=  item + " UPPER(job.name) like '%" + attr[1].toUpperCase() + "%' ";
		}
		data = "and (" + data + ")";
		if (attr[0].equals("") || attr[0].equals(""))
			data = " ";
		
		String query = "select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename,emp.lastname,"
				+ "job.name as jobtitle, ptkp.name as ptkp "
				+ "from employee emp "
				+ "inner join "
				+ "(select e.* from employeeemployment e, "
				+ "(select employee, max(tmt) tmt from (select *   from employeeemployment where tmt<'"
				+ date
				+ "') "
				+ "group by employee ) lastemp "
				+ "where e.employee=lastemp.employee "
				+ "and e.tmt=lastemp.tmt and unit="
				+ unitId
				+ ") employment "
				+ "on emp.autoindex=employment.employee "
				+ "inner join jobtitle job on employment.jobtitle=job.autoindex "
				+ "inner join ptkp on emp.art21=ptkp.autoindex "
				+ "where emp.autoindex NOT IN( SELECT employee FROM employeeretirement WHERE tmt<'"
				+ date + "') " + data + " order by emp.employeeno";
		Employee_n[] emps = logic.getEmployeeAndPTKPByCriteria(m_sessionid, 
				IDBConstants.MODUL_MASTER_DATA,query);	
		return emps;
	}
	
	public String getMount(int mount){
		String m_mount = "";
		switch (mount){
		case 0: m_mount = "January";break;
		case 1: m_mount = "February";break;
		case 2: m_mount = "March";break;
		case 3: m_mount = "April";break;
		case 4: m_mount = "May";break;
		case 5: m_mount = "June";break;
		case 6: m_mount = "July";break;
		case 7: m_mount = "August";break;
		case 8: m_mount = "September";break;
		case 9: m_mount = "October";break;
		case 10: m_mount = "November";break;
		case 11: m_mount = "December";break;
		}
		return m_mount;
	}
	
	class PayrollTable extends StructuredTable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		PayrollTableModel m_tableModel;
		
		public PayrollTable(DefaultMutableTreeNode root) {
			super(root);
			((DefaultTableModel)getModel()).setRowCount(0);	 
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		
		public void resetDefaultColumn() {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)getTableHeaderRoot();
			root.add(new DefaultMutableTreeNode("No"));
			root.add(new DefaultMutableTreeNode("Name"));
			root.add(new DefaultMutableTreeNode("Job Title"));
			root.add(new DefaultMutableTreeNode("PTKP Status"));
		}
		
		public void setModelKu(){			
			Enumeration enumeration = getColumnModel().getColumns();
			while(enumeration.hasMoreElements()){
				TableColumn column = (TableColumn) enumeration.nextElement();
				column.setPreferredWidth(100);
				//column.setMinWidth(50);
				//column.setMaxWidth(50);
			}
			
			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);
		}
		
		public void clearRow() {
			((DefaultTableModel)getModel()).setRowCount(0);
		}
		
		public void addRow(Vector data) {
			((DefaultTableModel)getModel()).addRow(data);
			
		}
		
		public TableCellRenderer getCellRenderer(int row, int col) {
			if (col == 0)
				return new FormattedStandardCellRenderer(Font.PLAIN, JLabel.RIGHT);
			else if (col == 3)
				return new FormattedStandardCellRenderer(Font.PLAIN, JLabel.CENTER);
			else if (col > 3)
				return new DoubleCellRenderer(JLabel.RIGHT);
			return super.getCellRenderer(row, col);
		}
	}
	
	class PayrollTableModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}
	
	class RightAllignmentCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setText((value == null) ? "    " : value.toString());
			setHorizontalAlignment(JLabel.RIGHT);
			
			return this;
		}
	}
	
	class CenterllignmentCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setText((value == null) ? "    " : value.toString());
			setHorizontalAlignment(JLabel.CENTER);
			
			return this;
		}
	}
}
