package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceTransactionCode;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEmpReceivable;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.PmtEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCostDetail;
import pohaci.gumunda.titis.accounting.entity.PmtOthersDetail;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCostDetail;
import pohaci.gumunda.titis.accounting.entity.RcvEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.RcvOthersDetail;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.BeginningEmpReceivableBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.PeriodStartEnd;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.hrm.cgui.SimpleEmployee;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

//payment debet , receive kredit
public class EmployeeReceivable{
	JRViewer m_jrv;
	Connection m_conn;
	long m_sessionid;
	PeriodStartEnd m_periodStartEnd;
	UnitPicker m_unitPicker;
	Account m_account;
	DecimalFormat m_desimalFormat; 
	AccountingBusinessLogic m_logic;
	DatePicker m_datePicker;
	
	public EmployeeReceivable(){
		setEmpty();
	}
	
	/*public EmployeeReceivable(Connection conn,long sessionid,UnitPicker unitPicker,DatePicker date,boolean isExcel){
	 m_conn = conn;
	 m_sessionid = sessionid;
	 m_unitPicker = unitPicker;
	 m_datePicker = date;
	 setNonEmpty(isExcel);
	 }
	 */
	public EmployeeReceivable(Connection conn,long sessionid,DatePicker date,boolean isExcel){
		m_conn = conn;
		m_sessionid = sessionid;
		m_datePicker = date;
		setNonEmpty(isExcel);
	}
	
	private void setEmpty() {
		try {			
			String filename = "EmployeeReceivable";
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			
			parameters.put("param_logo", "../images/TS.gif");			
			parameters.put("param_unit_code","");			
			parameters.put("param_date", "");
			
			model.addColumn("Field0");
			model.addColumn("Field1");
			model.addColumn("Field2");
			model.addColumn("Field3");
			model.addColumn("Field4");
			model.addColumn("Field5");
			model.addColumn("Field6");
			model.addColumn("Field7");	
			model.addColumn("Field8");
			model.addColumn("Field9");	
			model.addColumn("status");
			model.addRow(new Object[]{"","","","","","","","","","",new Integer(0)});
			model.addRow(new Object[]{"Total","","","","","","","","","",new Integer(1)});
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			
			m_jrv = new JRViewer(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void setNonEmpty(boolean isExcel) {
		try {			
			String filename = "EmployeeReceivable";
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
			String compiledRptFilename = ReportUtils.compileReport(filename);
			if (compiledRptFilename == "") return;
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();
			
			parameters.put("param_logo", "../images/TS.gif");	
			parameters.put("param_unit_code","ALL");			
			//if(m_unitPicker.getUnit() != null)
			//	parameters.put("param_unit_code",m_unitPicker.getUnit().toString());			
			parameters.put("param_date", dateFormat.format(m_datePicker.getDate()));
			
			model.addColumn("Field0");
			model.addColumn("Field1");
			model.addColumn("Field2");
			model.addColumn("Field3");
			model.addColumn("Field4");
			model.addColumn("Field5");
			model.addColumn("Field6");
			model.addColumn("Field7");	
			model.addColumn("Field8");
			model.addColumn("Field9");	
			model.addColumn("status");
			
			ArrayList tmpList = new ArrayList() ;
			
			//untuk mendapatkan data ditable BeginningEmpReceivable
			
			VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(m_conn, m_sessionid, IConstants.ATTR_VARS_EMP_REC);
			Account acct = null;
			if (vas!=null) {
				acct = vas.getAccount();
			}
			long idxAcct = 0;
			if (acct!=null)
				idxAcct = acct.getIndex();
			
			GenericMapper mapper = MasterMap.obtainMapperFor(BeginningEmpReceivable.class);
			mapper.setActiveConn(m_conn);
			String strWhere = "1=1";
			if (idxAcct > 0)
				strWhere = "ACCOUNT=" + idxAcct;
			List list = mapper.doSelectWhere(strWhere);
			Iterator iterator = list.iterator();		
			//int i=0;
			double totInitialBalance = 0;
			double totReceived = 0;
			double totEndingBalance = 0;
			
			while(iterator.hasNext()){		
				BeginningEmpReceivable begPmtRec = (BeginningEmpReceivable)iterator.next();	
				
				BeginningEmpReceivableBusinessLogic logic = new BeginningEmpReceivableBusinessLogic(m_conn, m_sessionid);
				EmployeePicker helpEmp=new EmployeePicker(m_conn, m_sessionid);			
				Unit unit = helpEmp.findUnitEmployee(begPmtRec.getEmployee());
				if (unit!=null){
					Transaction trans = logic.findTransaction(unit);
					if (trans!=null){
						begPmtRec.setTrans(trans);
						// query untuk mendapatkan jobtitle,department dari employee yang terakhir
						SimpleEmployee emp = getEmployee(begPmtRec.getEmployee());
						if (emp!=null){
							GenericMapper mapper2 = MasterMap.obtainMapperFor(RcvEmpReceivable.class);
							mapper2.setActiveConn(m_conn);
							String strWhere2 = IDBConstants.ATTR_BEGINNING_BALANCE + "=" + begPmtRec.getIndex() + " AND " +
							IDBConstants.ATTR_STATUS + "=3";
							List list2 = mapper2.doSelectWhere(strWhere2);
							Iterator iterator2 = list2.iterator();
							double amountReceivable = 0;
							while(iterator2.hasNext()){
								RcvEmpReceivable empRecRec = (RcvEmpReceivable)iterator2.next();
								amountReceivable +=empRecRec.getAmount()*empRecRec.getExchangeRate();
							}
							String name = emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
							if ((begPmtRec.getAccValue()-amountReceivable)>0){
								totInitialBalance+=begPmtRec.getAccValue()*begPmtRec.getExchangeRate();
								totReceived+=amountReceivable;
								totEndingBalance+=((begPmtRec.getAccValue()*begPmtRec.getExchangeRate())-amountReceivable);
								ERReport obj = new ERReport(emp.getEmployeeNo(), name, emp.getJobTitleName(), emp.getDepartment(), 
										begPmtRec.getTrans().getReference(), begPmtRec.getTrans().getTransDate(),
										begPmtRec.getAccValue() * begPmtRec.getExchangeRate(),amountReceivable,
										(begPmtRec.getAccValue() * begPmtRec.getExchangeRate()) - amountReceivable);
								tmpList.add(obj);
							}
						}
					}		
				}
			}
			//untuk mendapatkan data ditable PmtEmpReceivable
			mapper = MasterMap.obtainMapperFor(PmtEmpReceivable.class);
			mapper.setActiveConn(m_conn);
			strWhere = IDBConstants.ATTR_TRANSACTION_DATE + "<='" + dateFormat2.format(m_datePicker.getDate()) + "' AND " +
			IDBConstants.ATTR_STATUS + "=3";
			System.err.println(strWhere);
			list = mapper.doSelectWhere(strWhere);
			iterator = list.iterator();		
			
			//int count = tmpList.size();
			//Object[] objs = tmpList.toArray();
			
			while(iterator.hasNext()){		
				PmtEmpReceivable empPmtRec = (PmtEmpReceivable)iterator.next();		
				// query untuk mendapatkan jobtitle,department dari employee yang terakhir
				SimpleEmployee emp = getEmployee(empPmtRec.getPayTo());				
				if (emp!=null){
					GenericMapper mapper2 = MasterMap.obtainMapperFor(RcvEmpReceivable.class);
					mapper2.setActiveConn(m_conn);
					String strWhere2 = IDBConstants.ATTR_EMP_RECEIVABLE + "=" + empPmtRec.getIndex() + " AND " +
					IDBConstants.ATTR_STATUS + "=3";
					
					List list2 = mapper2.doSelectWhere(strWhere2);
					Iterator iterator2 = list2.iterator();
					double amountReceivable = 0;
					while(iterator2.hasNext()){
						RcvEmpReceivable empRecRec = (RcvEmpReceivable)iterator2.next();
						// dikalikan ama exchange rate-nya pmt-nya
						amountReceivable +=empRecRec.getAmount()*empPmtRec.getExchangeRate();
						
					}
					String name = emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
					if (((empPmtRec.getAmount()*empPmtRec.getExchangeRate())-amountReceivable)>0){
						totInitialBalance+=empPmtRec.getAmount()*empPmtRec.getExchangeRate();
						totReceived+=amountReceivable;
						totEndingBalance+=((empPmtRec.getAmount()*empPmtRec.getExchangeRate())-amountReceivable);						
						ERReport obj = new ERReport(emp.getEmployeeNo(), name, emp.getJobTitleName(), emp.getDepartment(), 
								empPmtRec.getReferenceNo(), empPmtRec.getTransactionDate(),
								empPmtRec.getAmount() * empPmtRec.getExchangeRate(),
								amountReceivable,
								(empPmtRec.getAmount() * empPmtRec.getExchangeRate()) - amountReceivable);
						tmpList.add(obj);						
					}
				}
			}
			
			// RECEIVE OTHERS
			mapper = MasterMap.obtainMapperFor(RcvOthersDetail.class);
			mapper.setActiveConn(m_conn);
			String clause = " receiveOthers in (select autoindex from RcvOthers where status=3 and transactionDate<='" 
				+ dateFormat2.format(m_datePicker.getDate()) + "') "
				+ " and account in "
				+ "(select account from subsidiaryaccountsetting where subsidiaryaccount='Employee' and transactioncode=6)";
			
			// please remember that transactioncode=6 is Emp receivable (index start by 1); refer to BeginningBalanceTransaction
			
			List rs = mapper.doSelectWhere(clause);
			Iterator iterRcvOthersDetail = rs.iterator();
			HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			while(iterRcvOthersDetail.hasNext()) {
				RcvOthersDetail det = (RcvOthersDetail) iterRcvOthersDetail.next();
				Employee employ = getEmployee(det, hrmLogic);
				SimpleEmployee emp = getEmployee(employ);
				
				String employno = "";
				String name = "";
				String jobtitle="";
				String department ="";
				if (emp!=null){
					employno = emp.getEmployeeNo();
					name= emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
					jobtitle = emp.getJobTitleName();
					department = emp.getDepartment();								
				}else{
					employno = employ.getEmployeeNo();
					name= employ.getFirstName() + " " + employ.getMidleName() + " " +employ.getLastName();
				}
				
				totInitialBalance+=0;
				totReceived+=det.getaccValue()*det.getExchangerate();
				totEndingBalance+=-det.getaccValue()*det.getExchangerate();
				ERReport obj = new ERReport(employno, name, jobtitle, department, 
						det.getReceiveothers().getReferenceNo(), det.getReceiveothers().getTransactionDate(),
						0,
						det.getaccValue()*det.getExchangerate(),
						-det.getaccValue()*det.getExchangerate());
				tmpList.add(obj);
			}
			
			// PAYMENT OPERATIONAL COST			
			mapper = MasterMap.obtainMapperFor(PmtOperationalCostDetail.class);
			mapper.setActiveConn(m_conn);
			clause = " pmtoperationalcost in (select autoindex from pmtoperationalcost where status=3 and transactionDate<='" 
				+ dateFormat2.format(m_datePicker.getDate()) + "') "
				+ " and account in "
				+ "(select account from subsidiaryaccountsetting where subsidiaryaccount='Employee' and transactioncode=6)";
			
			// please remember that transactioncode=6 is Emp receivable (index start by 1); refer to BeginningBalanceTransaction
			
			rs = mapper.doSelectWhere(clause);
			Iterator iter = rs.iterator();
			//HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			while(iter.hasNext()) {
				PmtOperationalCostDetail det = (PmtOperationalCostDetail) iter.next();
				Employee employ = getEmployee(det, hrmLogic);
				SimpleEmployee emp = getEmployee(employ);
				
				String employno = "";
				String name = "";
				String jobtitle="";
				String department ="";
				if (emp!=null){
					employno = emp.getEmployeeNo();
					name= emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
					jobtitle = emp.getJobTitleName();
					department = emp.getDepartment();								
				}else{
					employno = employ.getEmployeeNo();
					name= employ.getFirstName() + " " + employ.getMidleName() + " " +employ.getLastName();
				}
				
				totInitialBalance+=det.getAccValue()*det.getExchangeRate();
				totReceived+=0;
				totEndingBalance+=det.getAccValue()*det.getExchangeRate();
				ERReport obj = new ERReport(employno, name, jobtitle, department, 
						det.getPmtOperationalCost().getReferenceNo(), det.getPmtOperationalCost().getTransactionDate(),
						det.getAccValue()*det.getExchangeRate(),
						0,
						det.getAccValue()*det.getExchangeRate());
				tmpList.add(obj);
			}
			
			// PAYMENT PROJECT COST			
			mapper = MasterMap.obtainMapperFor(PmtProjectCostDetail.class);
			mapper.setActiveConn(m_conn);
			clause = " pmtprojectcost in (select autoindex from pmtprojectcost where status=3 and transactionDate<='" 
				+ dateFormat2.format(m_datePicker.getDate()) + "') "
				+ " and account in "
				+ "(select account from subsidiaryaccountsetting where subsidiaryaccount='Employee' and transactioncode=6)";
			
			// please remember that transactioncode=6 is Emp receivable (index start by 1); refer to BeginningBalanceTransaction
			
			rs = mapper.doSelectWhere(clause);
			iter = rs.iterator();
			//HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			while(iter.hasNext()) {
				PmtProjectCostDetail det = (PmtProjectCostDetail) iter.next();
				Employee employ = getEmployee(det, hrmLogic);
				SimpleEmployee emp = getEmployee(employ);
				String employno = "";
				String name = "";
				String jobtitle="";
				String department ="";
				if (emp!=null){
					employno = emp.getEmployeeNo();
					name= emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
					jobtitle = emp.getJobTitleName();
					department = emp.getDepartment();								
				}else{
					employno ="";
					name= "";
				}
				totInitialBalance+=det.getaccValue()*det.getExchangeRate();
				totReceived+=0;
				totEndingBalance+=det.getaccValue()*det.getExchangeRate();
				ERReport obj = new ERReport(employno, name, jobtitle,department, 
						det.getPmtProjectCost().getReferenceNo(), det.getPmtProjectCost().getTransactionDate(),
						det.getaccValue()*det.getExchangeRate(),
						0,
						det.getaccValue()*det.getExchangeRate());
				tmpList.add(obj);
			}
			
			// PAYMENT OTHERS		
			mapper = MasterMap.obtainMapperFor(PmtOthersDetail.class);
			mapper.setActiveConn(m_conn);
			clause = " pmtothers in (select autoindex from pmtothers where status=3 and transactionDate<='" 
				+ dateFormat2.format(m_datePicker.getDate()) + "') "
				+ " and account in "
				+ "(select account from subsidiaryaccountsetting where subsidiaryaccount='Employee' and transactioncode=6)";
			
			// please remember that transactioncode=6 is Emp receivable (index start by 1); refer to BeginningBalanceTransaction
			
			rs = mapper.doSelectWhere(clause);
			iter = rs.iterator();
			//HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			while(iter.hasNext()) {
				PmtOthersDetail det = (PmtOthersDetail) iter.next();
				Employee employ = getEmployee(det, hrmLogic);
				SimpleEmployee emp = getEmployee(employ);
				
				String employno = "";
				String name = "";
				String jobtitle="";
				String department ="";
				if (emp!=null){
					employno = emp.getEmployeeNo();
					name= emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
					jobtitle = emp.getJobTitleName();
					department = emp.getDepartment();								
				}else{
					employno = employ.getEmployeeNo();
					name= employ.getFirstName() + " " + employ.getMidleName() + " " +employ.getLastName();
				}
				
				totInitialBalance+=det.getAccValue()*det.getExchangeRate();
				totReceived+=0;
				totEndingBalance+=det.getAccValue()*det.getExchangeRate();
				ERReport obj = new ERReport(employno, name, jobtitle,department, 
						det.getPmtOthers().getReferenceNo(), det.getPmtOthers().getTransactionDate(),
						det.getAccValue()*det.getExchangeRate(),
						0,
						det.getAccValue()*det.getExchangeRate());
				tmpList.add(obj);
			}
			
			// INI KONYOL
			/*mapper=MasterMap.obtainMapperFor(RcvOthers.class);
			 mapper.setActiveConn(m_conn); 
			 List rs=mapper.doSelectWhere(IDBConstants.ATTR_STATUS + "=3 AND " + IDBConstants.ATTR_TRANSACTION_DATE +"<='" + dateFormat2.format(m_datePicker.getDate()) + "'" );			
			 Iterator iterRcvOther = rs.iterator();
			 while(iterRcvOther.hasNext()){
			 RcvOthers rcvother = (RcvOthers)iterRcvOther.next();
			 mapper=MasterMap.obtainMapperFor(RcvOthersDetail.class);
			 mapper.setActiveConn(m_conn);
			 rs=mapper.doSelectAll();
			 Iterator iterotherdetail = rs.iterator();
			 while(iterotherdetail.hasNext()){
			 RcvOthersDetail otherdetail = (RcvOthersDetail)iterotherdetail.next();
			 String[] SubsidiaryAccSet = getSubsidiaryByindex(otherdetail.getAccount());
			 HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			 if (SubsidiaryAccSet!=null)
			 if (SubsidiaryAccSet[0].equals("Employee") && SubsidiaryAccSet[1].equals(BeginningBalanceTransactionCode.TRANSCODES[5])){				
			 Employee employ = getEmployee(otherdetail, hrmLogic);
			 SimpleEmployee emp = getEmployee(employ);
			 
			 String employno = "";
			 String name = "";
			 String jobtitle="";
			 String department ="";
			 if (emp!=null){
			 employno = emp.getEmployeeNo();
			 name= emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
			 jobtitle = emp.getJobTitleName();
			 department = emp.getDepartment();								
			 }else{
			 employno = employ.getEmployeeNo();
			 name= employ.getFirstName() + " " + employ.getMidleName() + " " +employ.getLastName();
			 }
			 
			 totInitialBalance+=0;
			 totReceived+=otherdetail.getaccValue()*otherdetail.getExchangerate();
			 totEndingBalance+=-otherdetail.getaccValue()*otherdetail.getExchangerate();
			 ERReport obj = new ERReport(employno, name, jobtitle, department, 
			 rcvother.getReferenceNo(), rcvother.getTransactionDate(),
			 0,
			 otherdetail.getaccValue()*otherdetail.getExchangerate(),
			 -otherdetail.getaccValue()*otherdetail.getExchangerate());
			 tmpList.add(obj);
			 }
			 }				
			 }*/
			
			/*mapper=MasterMap.obtainMapperFor(PmtOperationalCost.class);
			 mapper.setActiveConn(m_conn); 
			 rs=mapper.doSelectWhere(IDBConstants.ATTR_STATUS + "=3 AND " + IDBConstants.ATTR_TRANSACTION_DATE +"<='" + dateFormat2.format(m_datePicker.getDate()) + "'" );			
			 Iterator iteropercost = rs.iterator();
			 while(iteropercost.hasNext()){
			 PmtOperationalCost opercost = (PmtOperationalCost)iteropercost.next();
			 mapper=MasterMap.obtainMapperFor(PmtOperationalCostDetail.class);
			 mapper.setActiveConn(m_conn);
			 rs=mapper.doSelectAll();
			 Iterator iteropercostdetail = rs.iterator();
			 while(iteropercostdetail.hasNext()){
			 PmtOperationalCostDetail opercostdetail = (PmtOperationalCostDetail)iteropercostdetail.next();
			 String[] SubsidiaryAccSet = getSubsidiaryByindex(opercostdetail.getAccount());
			 HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			 if (SubsidiaryAccSet!=null)
			 if (SubsidiaryAccSet[0].equals("Employee") && SubsidiaryAccSet[1].equals(BeginningBalanceTransactionCode.TRANSCODES[5])){				
			 Employee employ = getEmployee(opercostdetail, hrmLogic);
			 SimpleEmployee emp = getEmployee(employ);
			 String employno = "";
			 String name = "";
			 String jobtitle="";
			 String department ="";
			 if (emp!=null){
			 employno = emp.getEmployeeNo();
			 name= emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
			 jobtitle = emp.getJobTitleName();
			 department = emp.getDepartment();								
			 }else{
			 employno = employ.getEmployeeNo();
			 name= employ.getFirstName() + " " + employ.getMidleName() + " " +employ.getLastName();
			 }
			 totInitialBalance+=opercostdetail.getAccValue()*opercostdetail.getExchangeRate();
			 totReceived+=0;
			 totEndingBalance+=opercostdetail.getAccValue()*opercostdetail.getExchangeRate();
			 ERReport obj = new ERReport(employno, name, jobtitle, department, 
			 opercost.getReferenceNo(), opercost.getTransactionDate(),
			 opercostdetail.getAccValue()*opercostdetail.getExchangeRate(),
			 0,
			 opercostdetail.getAccValue()*opercostdetail.getExchangeRate());
			 tmpList.add(obj);
			 }
			 }				
			 }
			 
			 mapper=MasterMap.obtainMapperFor(PmtProjectCost.class);
			 mapper.setActiveConn(m_conn); 
			 rs=mapper.doSelectWhere(IDBConstants.ATTR_STATUS + "=3 AND " + IDBConstants.ATTR_TRANSACTION_DATE +"<='" + dateFormat2.format(m_datePicker.getDate()) + "'" );			
			 Iterator iterprojcost = rs.iterator();
			 while(iterprojcost.hasNext()){
			 PmtProjectCost projcost = (PmtProjectCost)iterprojcost.next();
			 mapper=MasterMap.obtainMapperFor(PmtProjectCostDetail.class);
			 mapper.setActiveConn(m_conn);
			 rs=mapper.doSelectAll();
			 Iterator iterprojectcostdetail = rs.iterator();
			 while(iterprojectcostdetail.hasNext()){
			 PmtProjectCostDetail projcostdetail = (PmtProjectCostDetail)iterprojectcostdetail.next();
			 String[] SubsidiaryAccSet = getSubsidiaryByindex(projcostdetail.getAccount());
			 HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			 if (SubsidiaryAccSet!=null)
			 if (SubsidiaryAccSet[0].equals("Employee") && SubsidiaryAccSet[1].equals(BeginningBalanceTransactionCode.TRANSCODES[5])){				
			 Employee employ = getEmployee(projcostdetail, hrmLogic);
			 SimpleEmployee emp = getEmployee(employ);
			 String employno = "";
			 String name = "";
			 String jobtitle="";
			 String department ="";
			 if (emp!=null){
			 employno = emp.getEmployeeNo();
			 name= emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
			 jobtitle = emp.getJobTitleName();
			 department = emp.getDepartment();								
			 }else{
			 employno = employ.getEmployeeNo();
			 name= employ.getFirstName() + " " + employ.getMidleName() + " " +employ.getLastName();
			 }
			 totInitialBalance+=projcostdetail.getaccValue()*projcostdetail.getExchangeRate();
			 totReceived+=0;
			 totEndingBalance+=projcostdetail.getaccValue()*projcostdetail.getExchangeRate();
			 ERReport obj = new ERReport(employno, name, jobtitle,department, 
			 projcost.getReferenceNo(), projcost.getTransactionDate(),
			 projcostdetail.getaccValue()*projcostdetail.getExchangeRate(),
			 0,
			 projcostdetail.getaccValue()*projcostdetail.getExchangeRate());
			 tmpList.add(obj);
			 }
			 }				
			 }
			 
			 mapper=MasterMap.obtainMapperFor(PmtOthers.class);
			 mapper.setActiveConn(m_conn); 
			 rs=mapper.doSelectWhere(IDBConstants.ATTR_STATUS + "=3 AND " + IDBConstants.ATTR_TRANSACTION_DATE +"<='" + dateFormat2.format(m_datePicker.getDate()) + "'" );			
			 Iterator iterothers = rs.iterator();
			 while(iterothers.hasNext()){
			 PmtOthers projcost = (PmtOthers)iterothers.next();
			 mapper=MasterMap.obtainMapperFor(PmtOthersDetail.class);
			 mapper.setActiveConn(m_conn);
			 rs=mapper.doSelectAll();
			 Iterator iterotherdetail = rs.iterator();
			 while(iterotherdetail.hasNext()){
			 PmtOthersDetail otherdetail = (PmtOthersDetail)iterotherdetail.next();
			 String[] SubsidiaryAccSet = getSubsidiaryByindex(otherdetail.getAccount());
			 HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			 if (SubsidiaryAccSet!=null)
			 if (SubsidiaryAccSet[0].equals("Employee") && SubsidiaryAccSet[1].equals(BeginningBalanceTransactionCode.TRANSCODES[5])){				
			 Employee employ = getEmployee(otherdetail, hrmLogic);
			 SimpleEmployee emp = getEmployee(employ);
			 String employno = "";
			 String name = "";
			 String jobtitle="";
			 String department ="";
			 if (emp!=null){
			 employno = emp.getEmployeeNo();
			 name= emp.getFirstName() + " " + emp.getMidleName() + " " +emp.getLastName();
			 jobtitle = emp.getJobTitleName();
			 department = emp.getDepartment();								
			 }else{
			 employno = employ.getEmployeeNo();
			 name= employ.getFirstName() + " " + employ.getMidleName() + " " +employ.getLastName();
			 }
			 totInitialBalance+=otherdetail.getAccValue()*otherdetail.getExchangeRate();
			 totReceived+=0;
			 totEndingBalance+=otherdetail.getAccValue()*otherdetail.getExchangeRate();
			 ERReport obj = new ERReport(employno, name, jobtitle,department, 
			 projcost.getReferenceNo(), projcost.getTransactionDate(),
			 otherdetail.getAccValue()*otherdetail.getExchangeRate(),
			 0,
			 otherdetail.getAccValue()*otherdetail.getExchangeRate());
			 tmpList.add(obj);
			 }
			 }				
			 }*/
			
			
			iterator = tmpList.iterator();
			Collections.sort(tmpList);
			int no = 0;
			
			while (iterator.hasNext()){
				ERReport er = (ERReport) iterator.next();
				model.addRow(new Object[] {
						String.valueOf(++no),
						er.getEmpNo(),
						er.getEmpName(),
						er.getJobTitle(),
						er.getDepartment(),
						dateFormat.format(er.getRefDate()),
						er.getRefNo(),
						desimalFormat.format(er.getInitial()),
						desimalFormat.format(er.getReceived()),
						desimalFormat.format(er.getEnding()),
						new Integer(0)
				});
			}
			
			/*objs = tmpList.toArray();
			 for(int j=0; j<tmpList.size(); j++){
			 Object[] obj = (Object[]) objs[j];
			 model.addRow(new Object[]{obj[0].toString(),obj[1].toString(),obj[2].toString(),obj[3].toString(),
			 obj[4].toString(),desimalFormat.format(Double.parseDouble(obj[5].toString())),desimalFormat.format(Double.parseDouble(obj[6].toString())),
			 desimalFormat.format(Double.parseDouble(obj[7].toString())), obj[8]});
			 }*/
			//}
			
			
			model.addRow(new Object[]{"Total","","","","","","",desimalFormat.format(totInitialBalance),
					desimalFormat.format(totReceived),desimalFormat.format(totEndingBalance),new Integer(1)});
			
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledRptFilename,
					parameters, ds);
			
			if(!isExcel)
				m_jrv = new JRViewer(jasperPrint);
			else
				exportToExcel(jasperPrint);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * @param dateFormat2
	 * @param empPmtRec
	 * @return
	 * @throws Exception
	 */
	private SimpleEmployee getEmployee(Employee employ) throws Exception {
		if (employ == null)
			return null;
		
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String query ="select * from (select emp.autoindex,emp.employeeno,emp.firstname,emp.midlename," +
		"emp.lastname,job.name jobtitle,dept.name department " +
		"from employee emp " +
		"inner join (select e.* from employeeemployment e, " +
		"(select employee, max(tmt) tmt from " +
		"(select * from employeeemployment where tmt<'" +dateFormat2.format(m_datePicker.getDate())+ "') group by employee ) " +
		"lastemp where e.employee=lastemp.employee and e.tmt=lastemp.tmt) employment " +
		"on emp.autoindex=employment.employee " +
		"inner join jobtitle job on employment.jobtitle=job.autoindex " +
		"inner join organization dept on employment.department=dept.autoindex " +
		"where not exists " +
		"(SELECT employee FROM employeeretirement ret WHERE ret.tmt<='"+dateFormat2.format(m_datePicker.getDate())+"' and emp.autoindex=ret.employee) ) " +
		"where autoindex=" + employ.getIndex();
		System.err.println(query);				
		HRMBusinessLogic hrmlogic = new HRMBusinessLogic(m_conn);
		SimpleEmployee emp = hrmlogic.getEmployeeReceivableReportByUnit(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
		return emp;
	}
	
	public boolean isContaint(String name, List list){
		boolean status = false;
		Object[] objs = list.toArray();
		for(int j=0; j<list.size(); j++){
			Object[] obj = (Object[]) objs[j];
			if(obj[2].toString().equals(name))
				status = true;
		}
		return status;
	}
	
	public String[] getSubsidiaryByindex(Account acc){
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		SubsidiaryAccountSetting accSet = null;
		try{
			accSet = logic.getSubsidiaryAccountSettingByIndex(m_sessionid, IDBConstants.MODUL_MASTER_DATA,acc.getIndex());
		}catch (Exception ex){			
		}
		if (accSet!=null){
			return new String[]{accSet.getSubsidiaryAccount(),BeginningBalanceTransactionCode.TRANSCODES[accSet.getTranscode()-1]}; 
		}else
			return null;
	}
	
	private Employee getEmployee(Object detail, HRMBusinessLogic hrmLogic) {
		Employee emp = null;
		try {
			if (detail instanceof PmtProjectCostDetail) {
				PmtProjectCostDetail det = (PmtProjectCostDetail) detail;
				emp = hrmLogic.getEmployeeByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,det.getSubsidiAry());	
			}
			else if (detail instanceof PmtOperationalCostDetail) {
				PmtOperationalCostDetail det = (PmtOperationalCostDetail) detail;
				emp = hrmLogic.getEmployeeByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,det.getSubsidiAry());
			}
			else if (detail instanceof PmtOthersDetail) {
				PmtOthersDetail det = (PmtOthersDetail) detail;
				emp = hrmLogic.getEmployeeByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,det.getSubsidiAry());
			}
			if (detail instanceof RcvOthersDetail) {
				RcvOthersDetail det= (RcvOthersDetail) detail;
				emp = hrmLogic.getEmployeeByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,det.getSubsidiAry());
			}
		} catch (Exception e) {					
			e.printStackTrace();
		}	
		return emp;
	}
	
	public JRViewer getPrintView(){
		return m_jrv;
	}
	
	protected class ERReport implements Comparable {
		
		private String empNo = "";
		private String empName = "";
		private String jobTitle = "";
		private String department = "";
		private String refNo = "";
		private Date refDate = null;
		private double initial = 0;
		private double received = 0;
		private double ending = 0;
		
		public ERReport(String empNo, String empName, String jobTitle,
				String department, String refNo, Date refDate, double initial,
				double received, double ending) {
			this.empNo = empNo;
			this.empName = empName;
			this.jobTitle = jobTitle;
			this.department = department;
			this.refNo = refNo;
			this.refDate = refDate;
			this.initial = initial;
			this.received = received;
			this.ending = ending;
			
		}
		
		public int compareTo(Object arg0) {
			if (arg0 instanceof ERReport) {
				ERReport rpt = (ERReport) arg0;
				int comp = 0;
				comp = this.toString().compareTo(rpt.toString());
				
				if (comp == 0)
					comp = this.getRefDate().compareTo(rpt.getRefDate());
				
				return comp;
			} else
				return 0;
		}
		
		public String getDepartment() {
			return department;
		}
		
		public String getEmpName() {
			return empName;
		}
		
		public String getEmpNo() {
			return empNo;
		}
		
		public double getEnding() {
			return ending;
		}
		
		public double getInitial() {
			return initial;
		}
		
		public String getJobTitle() {
			return jobTitle;
		}
		
		public double getReceived() {
			return received;
		}
		
		public Date getRefDate() {
			return refDate;
		}
		
		public String getRefNo() {
			return refNo;
		}
		
		public String toString() {
			return empNo;
		}
		
	}
	
	public void exportToExcel(JasperPrint jasperPrint) throws Exception {
		javax.swing.JFileChooser jfc = new javax.swing.JFileChooser("reportsample/");
		
		jfc.setDialogTitle("Simpan Laporan Dalam File Excel");
		jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File file) {
				String filename = file.getName();
				return (filename.toLowerCase().endsWith(".xls") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
			}
			public String getDescription() {
				return "Laporan *.xls";
			}
		});
		
		jfc.setMultiSelectionEnabled(false);    
		jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);
		if  (jfc.showSaveDialog(null) == javax.swing.JOptionPane.OK_OPTION) {
			//JExcelApiExporter exporterXLS = new JExcelApiExporter();
			JExcelApiExporter exporter = new JExcelApiExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, changeFileExtension(jfc.getSelectedFile().getPath(), "xls"));
			exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
			exporter.exportReport();           
		}
	}
	
	public static String changeFileExtension(String filename, String newExtension ) {
		if (!newExtension.startsWith("."))
			newExtension = "." + newExtension;
		if (filename == null || filename.length()==0 ) {
			return newExtension;
		}
		
		int index = filename.lastIndexOf(".");
		if (index >= 0) {
			filename = filename.substring(0,index);
		}
		return filename += newExtension;
	}
	
}
