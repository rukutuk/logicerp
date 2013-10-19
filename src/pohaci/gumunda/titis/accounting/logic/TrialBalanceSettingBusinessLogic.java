/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.entity.TrialBalanceAccountTypeSetting;
import pohaci.gumunda.titis.accounting.entity.TrialBalanceJournalTypeSetting;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class TrialBalanceSettingBusinessLogic {
	private Connection connection;
	//private long sessionId;
	
	private GenericMapper journalMapper = MasterMap.obtainMapperFor(TrialBalanceJournalTypeSetting.class);
	private GenericMapper accountMapper = MasterMap.obtainMapperFor(TrialBalanceAccountTypeSetting.class);
	
	public TrialBalanceSettingBusinessLogic(Connection connection, long sessionId) {
		this.connection = connection;
		//this.sessionId = sessionId;
		
		this.journalMapper.setActiveConn(connection);
		this.accountMapper.setActiveConn(connection);
	}
	
	public List getTrialBalanceJournalTypeSettingList(){
		List list = this.journalMapper.doSelectAll();
		
		return list;
	}
	
	public List getTrialBalanceAccountTypeSettingList(){
		List list = this.accountMapper.doSelectAll();
		
		return list;
	}
	
	public List getJournalList(){
		GenericMapper mapper = MasterMap.obtainMapperFor(Journal.class);
		mapper.setActiveConn(connection);
		
		List list = mapper.doSelectAll();
		
		return list;
	}
	
	public void saveTrialBalanceJournalTypeSettingList(List toSaveList){
		// CARA YANG ANEH...
		// hapus yang ada dulu
		List list = this.journalMapper.doSelectAll();
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			TrialBalanceJournalTypeSetting setting = (TrialBalanceJournalTypeSetting) iterator.next();
			
			this.journalMapper.doDelete(setting);
		}
		
		// trus insert yang baru...
		// BENER-BENER ANEH...
		iterator = toSaveList.iterator();
		while(iterator.hasNext()){
			TrialBalanceJournalTypeSetting setting = (TrialBalanceJournalTypeSetting) iterator.next();
			
			this.journalMapper.doInsert(setting);
		}
	}
	
	public void saveTrialBalanceAccountTypeSettingList(List toSaveList){
		// CARA YANG ANEH...
		// hapus yang ada dulu
		List list = this.accountMapper.doSelectAll();
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			TrialBalanceAccountTypeSetting setting = (TrialBalanceAccountTypeSetting) iterator.next();
			
			this.accountMapper.doDelete(setting);
		}
		
		// trus insert yang baru...
		// BENER-BENER ANEH...
		iterator = toSaveList.iterator();
		while(iterator.hasNext()){
			TrialBalanceAccountTypeSetting setting = (TrialBalanceAccountTypeSetting) iterator.next();
			
			this.accountMapper.doInsert(setting);
		}
	}
}
