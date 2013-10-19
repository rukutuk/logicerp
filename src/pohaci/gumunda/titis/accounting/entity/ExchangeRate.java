package pohaci.gumunda.titis.accounting.entity;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.util.Date;

import pohaci.gumunda.titis.accounting.cgui.Currency;

public class ExchangeRate {
  protected long m_index = 0;
  protected Currency m_reference = null;
  protected Currency m_base = null;
  protected double m_ammount = 0.0;
  protected Date m_validFrom = null;
  protected Date m_validTo = null;

  public ExchangeRate(Currency reference, Currency base, double exchangeRate, Date validFrom, Date validTo) {
    m_reference = reference;
    m_base = base;
    m_ammount = exchangeRate;
    m_validFrom = validFrom;
    m_validTo = validTo;
  }

  public ExchangeRate(long index, Currency reference, Currency base, double exchangeRate, Date validFrom, Date validTo) {
    m_index = index;
    m_reference = reference;
    m_base = base;
    m_ammount = exchangeRate;
    m_validFrom = validFrom;
    m_validTo = validTo;
  }

  public ExchangeRate(long index, ExchangeRate exchange) {
    m_index = index;
    m_reference = exchange.getReferenceCurrency();
    m_base = exchange.getBaseCurrency();
    m_ammount = exchange.getExchangeRate();
    m_validFrom = exchange.getValidFrom();
    m_validTo = exchange.getValidTo();
  }

  public long getIndex() {
    return m_index;
  }

  public Currency getReferenceCurrency() {
    return m_reference;
  }

  public Currency getBaseCurrency() {
    return m_base;
  }

  public double getExchangeRate() {
    return m_ammount;
  }

  public Date getValidFrom() {
    return m_validFrom;
  }

  public Date getValidTo() {
    return m_validTo;
  }

  public String toString() {
    return m_reference.toString();
  }
}