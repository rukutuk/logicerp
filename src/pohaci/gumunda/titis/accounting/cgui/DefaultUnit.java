package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class DefaultUnit {
	protected Unit m_unit;
	protected int m_index;
	public DefaultUnit(Unit unit){
		m_unit = unit;
	}
	
	public Unit getUnit(){
		return m_unit;
	}
	
	public int getIndex(){
		return m_index;
	}
	
	public static Unit createDefaultUnit(Connection connection, long sessionId){
		AccountingBusinessLogic logic = new AccountingBusinessLogic(connection);
		try {
			DefaultUnit dUnit = logic.getDefaultUnit(sessionId, IDBConstants.MODUL_ACCOUNTING);
			if(dUnit!=null){
				Unit unit = dUnit.getUnit();
				return unit;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
