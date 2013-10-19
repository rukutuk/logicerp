package pohaci.gumunda.titis.accounting.cgui;

import pohaci.gumunda.titis.accounting.entity.Account;

public class TransactionValue{
  protected long m_accountindex = 0;
  protected double m_value = 0.0;
  protected long m_currencyindex = 0;
  protected double m_rate = 0.0;

  public TransactionValue(Account account, double value, Currency currency, double rate) {
    m_accountindex = account.getIndex();
    m_value = value;
    m_currencyindex = currency.getIndex();
    m_rate = rate;
  }

  public long getAccount() {
    return m_accountindex;
  }

  public double getValue() {
    return m_value;
  }

  public long getCurrency(){
    return m_currencyindex;
  }

  public double getExchangeRate() {
    return m_rate;
  }
}