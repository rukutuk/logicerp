/**
 *
 */
package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.math.BigDecimal;
import java.util.List;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.NumberRounding;

public class ReportAccountValue extends ReportRow {
	private static final long serialVersionUID = 1L;

	protected Account account;

	public void calculate(ReportContext ctx) {
		if (calculated)
			return;
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		value = ctx.calculateValue(account);
		value = nr.round(value);
		compValue = ctx.calculateComparativeValue(account);
		compValue = nr.round(compValue);
		calculated = true;
	}

	public ReportAccountValue(String label, boolean normalDebit, Account account) {
		super(label, normalDebit);
		this.account = account;
		indent = 1;
		allowsChildren = false;
	}

	public ReportAccountValue(boolean normalDebit, Account account) {
		super(account.getName(), normalDebit);
		this.account = account;
		indent = 1;
		allowsChildren = false;
	}

	public ReportAccountValue(String label, boolean normalDebit) {
		super(label, normalDebit);
		indent = 1;
		allowsChildren = false;
	}

	public Integer getCode() {
		return new Integer(1);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void storeOutput(List list) {
		if (!invisible) {
			if (!context.isShowZeroValue()) {
				if (context.isViewComparative()) {
					if (!((value.equals(BigDecimal.valueOf(0))) && (compValue
							.equals(BigDecimal.valueOf(0)))))
						super.storeOutput(list);
				} else {
					if (!value.equals(BigDecimal.valueOf(0)))
						super.storeOutput(list);
				}
			} else
				super.storeOutput(list);
		}
		/*
		 * if(!context.isShowZeroValue()){
		 * if(!((value.equals(BigDecimal.ZERO))&&(compValue.equals(BigDecimal.ZERO))))
		 * super.storeOutput(list); }
		 */
		// super.storeOutput(list);
	}
}
