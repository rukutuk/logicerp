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
import pohaci.gumunda.titis.application.NumberRounding;

/**
 * @author dark-knight
 *
 */
public abstract class ReportContext {

	protected boolean showZeroValue = false;

	protected boolean viewComparative = false;

	protected String reportPeriodLabel;

	protected String comparativePeriodLabel;

	protected LanguagePack language = null;

	protected String reportTitle = "";

	protected String periodType = "";

	protected Date reportStartDate;

	protected Date reportEndDate;

	protected Date comparativeStartDate;

	protected Date comparativeEndDate;

	public static final String PERIOD_ANNUALLY = "Annually";

	public static final String PERIOD_SEMESTER = "Semester";

	public static final String PERIOD_QUARTERLY = "Quarterly";

	public static final String PERIOD_MONTHLY = "Monthly";

	public static final String PERIOD_CUSTOM = "Custom";

	public static final String[] PERIODS = { PERIOD_ANNUALLY, PERIOD_SEMESTER,
			PERIOD_QUARTERLY, PERIOD_MONTHLY, PERIOD_CUSTOM };

	protected MySimpleDateFormat indonesianDateformat = MySimpleDateFormat.INDONESIAN;

	protected MySimpleDateFormat englishDateformat = MySimpleDateFormat.ENGLISH;

	protected NumberFormat numberFormat;

	protected AccountingSQLSAP sql;

	protected Connection connection;

	protected String journals;

	protected NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);

	public Date getComparativeEndDate() {
		return comparativeEndDate;
	}

	public void setComparativeEndDate(Date comparativeEndDate) {
		this.comparativeEndDate = comparativeEndDate;
	}

	public Date getComparativeStartDate() {
		return comparativeStartDate;
	}

	public void setComparativeStartDate(Date comparativeStartDate) {
		this.comparativeStartDate = comparativeStartDate;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public Date getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(Date reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public Date getReportStartDate() {
		return reportStartDate;
	}

	public void setReportStartDate(Date reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	public abstract String getReportDescription();

	public LanguagePack getLanguage() {
		return language;
	}

	public void setLanguage(LanguagePack language) {
		this.language = language;
	}

	public String getComparativePeriodLabel() {
		return comparativePeriodLabel;
	}

	public void setComparativePeriodLabel(String comparativePeriodLabel) {
		this.comparativePeriodLabel = comparativePeriodLabel;
	}

	public String getReportPeriodLabel() {
		return reportPeriodLabel;
	}

	public void setReportPeriodLabel(String reportPeriodLabel) {
		this.reportPeriodLabel = reportPeriodLabel;
	}

	public boolean isShowZeroValue() {
		return showZeroValue;
	}

	public void setShowZeroValue(boolean showZeroValue) {
		this.showZeroValue = showZeroValue;
	}

	public boolean isViewComparative() {
		return viewComparative;
	}

	public void setViewComparative(boolean viewComparative) {
		this.viewComparative = viewComparative;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	protected String getPeriodTypeBasedOnLanguage() {
		if (this.language == LanguagePack.INDONESIAN) {
			if (this.periodType == PERIOD_ANNUALLY)
				return "Tahun";
			else if (this.periodType == PERIOD_SEMESTER)
				return "Semester";
			else if (this.periodType == PERIOD_QUARTERLY)
				return "Triwulan";
			else if (this.periodType == PERIOD_MONTHLY)
				return "Bulan";
			else
				return "Periode";
		} else if (this.language == LanguagePack.ENGLISH) {
			if (this.periodType == PERIOD_ANNUALLY)
				return "Year";
			else if (this.periodType == PERIOD_SEMESTER)
				return "Semester";
			else if (this.periodType == PERIOD_QUARTERLY)
				return "Querter";
			else if (this.periodType == PERIOD_MONTHLY)
				return "Month";
			else
				return "Period";
		} else
			return "Period";
	}

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	public void setAccountingSQLSAP(AccountingSQLSAP sql) {
		this.sql = sql;
	}

	public AccountingSQLSAP getAccountingSQLSAP(){
		return this.sql;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection(){
		return this.connection;
	}

	public abstract BigDecimal calculateValue(Account account);

	public abstract BigDecimal calculateComparativeValue(Account account);

	public String getJournals() {
		return journals;
	}

	public void setJournals(String journals) {
		this.journals = journals;
	}
}
