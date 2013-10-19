/**
 *
 */
package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.math.BigDecimal;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.LanguagePack;

/**
 * @author dark-knight
 *
 */
public class IndirectCashFlowStatementReportContext extends ReportContext {

	public String getReportDescription() {
		String str = "";
		String periodTypeStr = getPeriodTypeBasedOnLanguage();
		if (language == LanguagePack.INDONESIAN) {
			str = "Untuk " + periodTypeStr + " yang Berakhir Pada Tanggal "
					+ indonesianDateformat.format(reportEndDate);
			if (viewComparative == true)
				str += " dan "
						+ indonesianDateformat.format(comparativeEndDate);
		} else if (language == LanguagePack.ENGLISH) {
			str = "For " + periodTypeStr + " Ended At "
					+ englishDateformat.format(reportEndDate);
			if (viewComparative == true)
				str += " and " + englishDateformat.format(comparativeEndDate);
		}

		return str;
	}

	public BigDecimal calculateValue(Account account) {
		System.out.println("IndirectCashFlow: " + reportTitle + " start: " + reportStartDate + ",  end: " + reportEndDate);
		return sql.calcIncomeStmt(account, reportStartDate, reportEndDate, journals, connection);
	}

	public BigDecimal calculateComparativeValue(Account account) {
		return sql.calcIncomeStmt(account, comparativeStartDate, comparativeEndDate, journals, connection);
	}

	public BigDecimal calculateAltValue(Account account, boolean onStart) {
		if(onStart)
			return sql.calcStartAltBalance(account, reportStartDate, journals, connection);

		return sql.calcBalance(account, reportEndDate, journals, connection);
	}

	public BigDecimal calculateAltCompValue(Account account, boolean onStart) {
		if(onStart)
			return sql.calcStartAltBalance(account, comparativeStartDate, journals, connection);

		return sql.calcBalance(account, comparativeEndDate, journals, connection);
	}
}
