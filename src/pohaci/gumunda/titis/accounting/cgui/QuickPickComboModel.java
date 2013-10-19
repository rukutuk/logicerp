package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.DefaultComboBoxModel;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class QuickPickComboModel extends  DefaultComboBoxModel {
	private static final long serialVersionUID = 1L;
	
	public QuickPickComboModel(java.sql.Connection conn, long sessionid){
		initData(conn,sessionid);
	}

	private void initData(Connection conn, long sessionid) {
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		try {
			SubsidiaryAccountSetting[] accountSettings = logic.getAllSubsidiaryAccountSetting(sessionid, IDBConstants.MODUL_ACCOUNTING);
			int i;
			this.addElement("-");
			for (i=0; i<accountSettings.length;i++){
				this.addElement(accountSettings[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
