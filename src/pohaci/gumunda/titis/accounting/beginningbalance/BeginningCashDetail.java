package pohaci.gumunda.titis.accounting.beginningbalance;

import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class BeginningCashDetail extends BeginningBalanceSheetDetail {
  protected CashAccount m_cashAccount = CashAccount.nullObject();

public CashAccount getCashAccount() {
	return m_cashAccount;
}

public void setCashAccount(CashAccount cashAccount) {
	m_cashAccount = cashAccount;
}

public String cashAccountName() {
	return m_cashAccount.getName();
}

public Object subsidiaryStr() {
	return cashAccountName();
}

protected Unit unit() {
	return m_cashAccount.getUnit();
}

// akibatnya
protected long indexSubsidiary() {
	if(getCashAccount()==null)
		return -1;
	return getCashAccount().getIndex();
}

}