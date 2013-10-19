package pohaci.gumunda.titis.accounting.cgui;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;

public interface CashBankAccount {

	public abstract Currency getCurrency();

	public abstract Unit getUnit();

	public abstract Account getAccount();

	public abstract long getIndex();
}