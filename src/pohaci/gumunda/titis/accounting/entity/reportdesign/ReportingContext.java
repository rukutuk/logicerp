/**
 * 
 */
package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.Date;

import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.LanguagePack;

/**
 * @author dark-knight
 *
 */
public class ReportingContext {
	private Date date;

	private Date toDate;
	
	private Date compDate;

	private Date compToDate;

	private AccountingSQLSAP accountingSQLSAP;

	private Connection connection;

	private NumberFormat numberFormat;

	public static final String BALANCE = "Balance";

	public static final String INCOMESTMT = "Income";

	public static final String CASHFLOW = "Cashflow";
	
	

	private String reportType = BALANCE;
	
	private LanguagePack language;
	
	private String label;
	private String compLabel;

	public String getCompLabel() {
		return compLabel;
	}

	public void setCompLabel(String compLabel) {
		this.compLabel = compLabel;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date toDate() {
		return toDate;
	}

	private String reportTitle;

	public void setToDate(Date aDate) {
		toDate = aDate;
	}

	public BigDecimal calculateValue(Account acct) {
//		if (reportType.equals(BALANCE))
//			return accountingSQLSAP.calcBalance(acct, date, connection);
//		if (reportType.equals(INCOMESTMT))
//			return accountingSQLSAP.calcIncomeStmt(acct, date, toDate,
//					connection);
		return BigDecimal.valueOf(0);
	}

	public AccountingSQLSAP getAccountingSQLSAP() {
		return accountingSQLSAP;
	}

	public void setAccountingSQLSAP(AccountingSQLSAP accountingSQLSAP) {
		this.accountingSQLSAP = accountingSQLSAP;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Date getToDate() {
		return toDate;
	}

	public LanguagePack getLanguage() {
		return language;
	}

	public void setLanguage(LanguagePack language) {
		this.language = language;
	}

	public void setPeriodType(String selectedItem) {
		
	}

	public Date getCompDate() {
		return compDate;
	}

	public void setCompDate(Date compDate) {
		this.compDate = compDate;
	}

	public Date getCompToDate() {
		return compToDate;
	}

	public void setCompToDate(Date compToDate) {
		this.compToDate = compToDate;
	}
	
	
}
