package pohaci.gumunda.titis.accounting.beginningbalance;

import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class BeginningBankDetail extends BeginningBalanceSheetDetail {
  protected BankAccount m_bankAccount = BankAccount.nullObject();

	public BankAccount getBankAccount() {
		return m_bankAccount;
	}
	
	public void setBankAccount(BankAccount bankaccount) {
		m_bankAccount = bankaccount;
	}

	public String bankAccountName() {
		return m_bankAccount.getName();
	}

	public String bankAccountNo() {
		return m_bankAccount.getAccountNo();
	}

	public Object subsidiaryStr() {
		return bankAccountName();
	}

	protected Unit unit() {
		return m_bankAccount.getUnit();
	}

	// akibatnya
	protected long indexSubsidiary() {
		if(getBankAccount()==null)
			return -1;
		return getBankAccount().getIndex();
	}
	
}