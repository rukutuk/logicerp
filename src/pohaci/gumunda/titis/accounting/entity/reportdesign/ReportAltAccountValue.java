/**
 *
 */
package pohaci.gumunda.titis.accounting.entity.reportdesign;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.NumberRounding;

/**
 * @author dark-knight
 *
 */
public class ReportAltAccountValue extends ReportAccountValue {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private boolean onStart = true;

	public boolean isOnStart() {
		return onStart;
	}

	public void setOnStart(boolean onStart) {
		this.onStart = onStart;
	}

	public ReportAltAccountValue(String label, boolean normalDebit, Account account) {
		super(label, normalDebit);
		this.account = account;
		indent = 1;
		allowsChildren = false;
	}

	public ReportAltAccountValue(boolean normalDebit, Account account) {
		super(account.getName(), normalDebit);
		this.account = account;
		indent = 1;
		allowsChildren = false;
	}

	public ReportAltAccountValue(String label, boolean normalDebit) {
		super(label, normalDebit);
		indent = 1;
		allowsChildren = false;
	}

	public void calculate(ReportContext ctx) {
		if (calculated)
			return;

		if(ctx instanceof IndirectCashFlowStatementReportContext){
			IndirectCashFlowStatementReportContext cfCtx = (IndirectCashFlowStatementReportContext) ctx;
			NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
			value = cfCtx.calculateAltValue(account, onStart);
			value = nr.round(value);
			compValue = cfCtx.calculateAltCompValue(account, onStart);
			compValue = nr.round(compValue);
			calculated = true;
			return;
		}

		value = ctx.calculateValue(account);
		compValue = ctx.calculateComparativeValue(account);
		calculated = true;
	}

}
