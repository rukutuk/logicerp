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
public class BalanceSheetReportContext extends ReportContext {

	public String getReportDescription() {
		String str = "";
		//String periodTypeStr = getPeriodTypeBasedOnLanguage();
		if (language == LanguagePack.INDONESIAN) {
			str = "Tanggal "
					+ indonesianDateformat.format(reportStartDate);
			if (viewComparative == true)
				str += " dan "
						+ indonesianDateformat.format(comparativeStartDate);
		} else if (language == LanguagePack.ENGLISH) {
			str = "At "
					+ englishDateformat.format(reportStartDate);
			if (viewComparative == true)
				str += " and " + englishDateformat.format(comparativeStartDate);
		}

		return str;
	}

	public BigDecimal calculateValue(Account account) {
		return sql.calcBalance(account, reportStartDate, journals, connection);
	}

	public BigDecimal calculateComparativeValue(Account account) {
		return sql.calcBalance(account, comparativeStartDate, journals, connection);
	}

}
