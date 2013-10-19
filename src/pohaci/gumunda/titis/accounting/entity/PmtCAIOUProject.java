package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.util.OtherSQLException;

public class PmtCAIOUProject  extends TransactionTemplateEntity implements GeneralIOweU{
	long index;           
	Employee payTo;            
	ProjectData project;  
	CashAccount cashAccount;
	
	double amount;     
	Organization department;
	
	public PmtCAIOUProject(){
		
	}
//	===Tamabahan
	public void setCashAccount(CashAccount temp){
		this.cashAccount=temp;
	}
	public CashAccount getCashAccount(){
		return this.cashAccount;
	}
	//===Tambahan
	
	
	public long getIndex() {		
		return index;
	}
	
	public void setIndex(long index) {		
		this.index = index;
	}
	
	public Organization getDepartment() {
		return department;
	}
	
	public void setDepartment(Organization department) {
		this.department = department;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Employee getPayTo() {
		return payTo;
	}
	public void setPayTo(Employee payTo) {
		this.payTo = payTo;
	}
	public ProjectData getProject() {		
		return project;
		
	}
	public void setProject(ProjectData project) {
		this.project = project;
	}
	public Object igetIOweUNo() {
		return getReferenceNo();
	}
	
	public Object igetIOweUDate() {
		return getTransactionDate();
	}
	
	public Object igetUnitCode() {
		if (unit!=null)
			return getUnit();
		return "";
	}
	
	public Object igetDepartment() {
		if (department!=null)
			return this.department;
		return "";
	}
	
	public Object igetPayTo() {
		return getPayTo();
	}
	
	public String toString() {    
		return this.referenceNo;
	}
	//Tambahan untuk mendapatkan ref no i owe U
	public void setIOUNo(long sessionId,java.sql.Connection conn) throws Exception
	{ 	//Ini untuk membangun pola agar dapat dicari datanya
		String pola="";
		pola=pola+getUnit().getCode();
	
       String type = "OU";
		
		String no = type + " " + pola;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMM");
		String period = simpleDateFormat.format(transactionDate);	
		no += (" " + period);
       int newCountNo = getNewNo(no,conn);
		
		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("000");
		newNo += df.format(newCountNo);
		
		setReferenceNo(newNo);
			
	}
	
/*	public void setIOUNo(long sessionId,java.sql.Connection conn) throws Exception
	{ 
//		 bikin si pemberi
		CashBankAccount sourceAccount = getCashAccount();
		TransactionDetail sourceDetail = 
			createNewTransDet(sourceAccount.getAccount(), -getAmount(),getCurrency(),getExchangeRate());
		ArrayList detailList = new ArrayList();
		detailList.add(sourceDetail);
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		//String type = "";
		//String code = "";
		//String attr = "";
			// buat yang cash
			//type = ReferenceNoGeneratorHelper.CASH_OUT;
			//code = ((CashAccount)sourceAccount).getUnit().getCode();
			String attr = IConstants.ATTR_PMT_CASH;
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_INSTALLMENT,
					attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
	
		
	    String unitCode = getUnit().getCode();
	    ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING);
	    setReferenceNo(noGenerator.createIOweYouReferenceNo(unitCode , getTransactionDate()));
	    
//		this is waktunya...
		Transaction trans = createNewTrans(journal, getUnit());
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getCurrency(), getExchangeRate(), true);

		trans.setTransactionDetail(details);
		// save lah
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());

	
	}*/
	

	public String getLastReferenceNo(Connection conn, String likeClausa)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			String query = "select max(" + IDBConstants.ATTR_REFERENCE_NO + 
			") as " + IDBConstants.ATTR_REFERENCE_NO +
			" from " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT + 
			" where " + IDBConstants.ATTR_REFERENCE_NO + 
			" like '" + likeClausa + "%'";
			
			ResultSet rs = stm.executeQuery(query);
			rs.next();
			
			return rs.getString(IDBConstants.ATTR_REFERENCE_NO);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}
	private int getNewNo(String no,Connection conn) {
		//	get the lastest one
		String getNo = "";
		try {
			getNo = getLastReferenceNo(conn,no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//	parsing
		int lastNo = 0;
		if (getNo != null) {
			if (getNo.length() > no.length()) {
				try {
					lastNo = Integer.parseInt(getNo.substring(getNo.length() - 3));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// adding one
		lastNo++;
		return lastNo;
	}
	
}
