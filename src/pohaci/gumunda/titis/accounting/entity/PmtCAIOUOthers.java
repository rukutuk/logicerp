package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

//import pohaci.gumunda.titis.accounting.cgui.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
//import pohaci.gumunda.titis.application.db.GenericMapper;
//import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.util.OtherSQLException;

public class PmtCAIOUOthers extends TransactionTemplateEntity implements GeneralIOweU{
	long autoindex;
	Employee payTo;
	CashAccount cashAccount;
	double amount;
	Organization department;
	//===Tamabahan
	public void setCashAccount(CashAccount temp){
		this.cashAccount=temp;
	}
	public CashAccount getCashAccount(){
		return this.cashAccount;
	}
	//===Tambahan
	public void setIndex(long autoindex) {
		this.autoindex = autoindex;
	}
	public long getIndex() {
		return autoindex;
	}
	public PmtCAIOUOthers() {
		super();
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Organization getDepartment() {
		return department;
	}

	public void setDepartment(Organization department) {
		this.department = department;
	}

	public Employee getPayTo() {
		return payTo;
	}

	public void setPayTo(Employee payTo) {
		this.payTo = payTo;
	}

    public Object igetIOweUNo() {
		return getReferenceNo();
	}

	public Object igetIOweUDate() {
		return getTransactionDate();
	}

	public Object igetUnitCode() {
		return getUnit();
	}

	public Object igetDepartment() {
		return getDepartment();
	}

	public Object igetPayTo() {
		return getPayTo();
	}
	public String toString() {
		return this.referenceNo;
	}
//	Tambahan untuk mendapatkan ref no i owe U
	public void setIOUNo(long sessionId,java.sql.Connection conn) throws Exception
	{  	//Ini untuk membangun pola agar dapat dicari datanya
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
	{ String unitCode = getUnit().getCode();
	  ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING);
	  setReferenceNo(noGenerator.createIOweYouReferenceNo(unitCode, getTransactionDate()));

	}*/
	public String getLastReferenceNo(Connection conn, String likeClausa)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			String query = "select max(" + IDBConstants.ATTR_REFERENCE_NO +
			") as " + IDBConstants.ATTR_REFERENCE_NO +
			" from " + IDBConstants.TABLE_PMT_CA_IOU_OTHERS +
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
