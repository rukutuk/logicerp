package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class JournalStandardSettingPickerHelper {
	private Connection connection;	
	
	public JournalStandardSettingPickerHelper(Connection connection, long sessionId, String modul) {
		this.connection = connection;		
	}
	
	public JournalStandardSettingPickerHelper(Connection connection, String modul) {
		this.connection = connection;		
	}
	
	public JournalStandard getJournalStandard(String jsCode){
		GenericMapper mapper = MasterMap.obtainMapperFor(JournalStandard.class);
		mapper.setActiveConn(connection);
		
		String whereClausa = "code='" + jsCode+ "'";
		List journalStandardList = mapper.doSelectWhere(whereClausa);
		return (JournalStandard) journalStandardList.get(0);
	}
	
	public JournalStandard getJournalStandard(long index){
		GenericMapper mapper = MasterMap.obtainMapperFor(JournalStandard.class);
		mapper.setActiveConn(connection);
		
		String whereClausa = "autoindex=" + index;
		List journalStandardList = mapper.doSelectWhere(whereClausa);
		return (JournalStandard) journalStandardList.get(0);
	}
	
	public JournalStandard getJournalStandardWithAccount(long index){
		JournalStandard journalStandard = getJournalStandard(index);
			
		List list = getJournalStandardAccount(journalStandard);
		JournalStandardAccount[] accounts = 
			(JournalStandardAccount[]) list.toArray(new JournalStandardAccount[list.size()]);
		journalStandard.setJournalStandardAccount(accounts);
		
		return journalStandard;
	}
	
	public List getJournalStandardSetting(String formIdentity){
		String whereClausa = IDBConstants.ATTR_APPLICATION + "='" + formIdentity + "'";		
		return getJournalStandardSettingList(whereClausa);
	}
	
	public List getJournalStandardSetting(String formIdentity, String formAttribute){
		String whereClausa = IDBConstants.ATTR_APPLICATION + "='" + formIdentity + "'" +
			" AND " + IDBConstants.ATTR_ATTRIBUTE + "='" + formAttribute + "'";
		System.err.println(whereClausa);
		return getJournalStandardSettingList(whereClausa);
	}

	public List getJournalStandardSettingList(String whereClausa) {
		GenericMapper mapper = MasterMap.obtainMapperFor(JournalStandardSetting.class);
		mapper.setActiveConn(connection);
	
		List journalStandardList = mapper.doSelectWhere(whereClausa);
		return journalStandardList;
	}
	
	public List getJournalStandardSettingWithAccount(String formIdentity) {
		List journalStandardList = getJournalStandardSetting(formIdentity);
	
		return getJournalStandardSettingWithAccountList(journalStandardList);
	}
	
	public List getJournalStandardSettingWithAccount(String formIdentity, String formAttribute){
		List journalStandardList = getJournalStandardSetting(formIdentity, formAttribute);
		
		return getJournalStandardSettingWithAccountList(journalStandardList);
	}

	private List getJournalStandardSettingWithAccountList(List journalStandardList) {
		Iterator iter = journalStandardList.iterator();
		while(iter.hasNext()){
			JournalStandardSetting journalStandardSetting = (JournalStandardSetting) iter.next();
			JournalStandard journalStandard = journalStandardSetting.getJournalStandard();
			
			List list = getJournalStandardAccount(journalStandard);
			JournalStandardAccount[] accounts = 
				(JournalStandardAccount[]) list.toArray(new JournalStandardAccount[list.size()]);
			journalStandard.setJournalStandardAccount(accounts);
		}
		
		return journalStandardList;
	}
	
	private List getJournalStandardAccount(long standardJournalId){
		GenericMapper mapper = MasterMap.obtainMapperFor(JournalStandardAccount.class);
		mapper.setActiveConn(connection);
		
		String whereClausa = "journal=" + standardJournalId;
		
		List accounts = mapper.doSelectWhere(whereClausa);
		return accounts;
	}

	public List getJournalStandardAccount(JournalStandard journalStandard) {
		return getJournalStandardAccount(journalStandard.getIndex());
	}
	
	public JournalStandardAccount[] getJournalStandardAccount(String jsCode) {
		JournalStandardAccount[] arr;
		List resultList = getJournalStandardAccount(getJournalStandard(jsCode).getIndex());
		arr = new JournalStandardAccount[resultList.size()];
		resultList.toArray(arr);
		return arr;
	}
	
} 
