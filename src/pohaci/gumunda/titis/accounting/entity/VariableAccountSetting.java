package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.util.List;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class VariableAccountSetting {
	private long index;
	private String variable;
	private Account account;
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public static VariableAccountSetting createVariableAccountSetting(Connection conn, long sessionid, String variable){
		GenericMapper mapper = MasterMap.obtainMapperFor(VariableAccountSetting.class);
		mapper.setActiveConn(conn);
		
		String clause = IDBConstants.ATTR_VARIABLE + "='" + variable+"'";
		System.err.println(clause);
		List list = mapper.doSelectWhere(clause);
		
		VariableAccountSetting vas = null;
		if(list.size()>0){
			vas = (VariableAccountSetting) list.get(0);
		}
		return vas;
	}
}
