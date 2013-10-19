package pohaci.gumunda.titis.accounting.helper;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.entity.ExchangeRate;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;


public class ExchangeRateHelper {
	
	private Connection connection;
	private GenericMapper mapper = MasterMap.obtainMapperFor(ExchangeRate.class);
	
	public ExchangeRateHelper(Connection connection, long sessionId) {
		this.connection = connection;
		this.mapper.setActiveConn(this.connection);
	}
	
	public double getExchangeRate(Currency referenceCurrency, Currency baseCurrency, Date currentDate){
		java.sql.Date sqlCurrentDate = utilToSqlDate(currentDate);
		
		List list = this.mapper.doSelectWhere("REFERENCECURRENCY=? AND BASECURRENCY=? AND VALIDFROM<=? AND VALIDTO>=?",
				new Object[]{new Long(referenceCurrency.getIndex()), new Long(baseCurrency.getIndex()),
				sqlCurrentDate, sqlCurrentDate});
		
		if(list.size()==0)
			return 1;
		
		ExchangeRate rate = (ExchangeRate) list.get(0); // dipaksa
		
		return rate.getExchangeRate();
	}
	
	public ExchangeRate getExchangeRateWithLastValidTo(){
		List list = this.mapper.doSelectWhere("1=1 ORDER BY VALIDTO DESC");
		
		if(list.size()==0)
			return null;
		
		return (ExchangeRate) list.get(0);
	}
	
	public List getConflictedExchangeRate(ExchangeRate rate, Currency referenceCurrency, Currency baseCurrency, Date validFrom, Date validTo){
		if(validFrom.compareTo(validTo)>0)
			return null;
		
		String whereClausa = null;
		Object[] params = null;
		
		java.sql.Date sqlValidTo = utilToSqlDate(validTo);
		java.sql.Date sqlValidFrom = utilToSqlDate(validFrom);
		
		if (rate != null) {
			whereClausa = "REFERENCECURRENCY = ? AND BASECURRENCY = ? AND "
					+ "NOT ((VALIDFROM > ? AND VALIDFROM > ?) OR (VALIDTO < ? AND VALIDTO < ?)) "
					+ "AND NOT (AUTOINDEX = ?)";

			params = new Object[] {
					new Long(referenceCurrency.getIndex()),
					new Long(baseCurrency.getIndex()), sqlValidTo,
					sqlValidFrom, sqlValidFrom, sqlValidTo,
					new Long(rate.getIndex()) };
		}else{
			whereClausa = 
				"REFERENCECURRENCY = ? AND BASECURRENCY = ? AND " +
				"NOT ((VALIDFROM > ? AND VALIDFROM > ?) OR (VALIDTO < ? AND VALIDTO < ?)) ";
					
			params = new Object[]{
				new Long(referenceCurrency.getIndex()), new Long(baseCurrency.getIndex()), 
				sqlValidTo, sqlValidFrom, sqlValidFrom, sqlValidTo
			};
		}
		List list = this.mapper.doSelectWhere(whereClausa, params);
		
		return list;
	}
	
	private java.sql.Date utilToSqlDate(java.util.Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }
}
