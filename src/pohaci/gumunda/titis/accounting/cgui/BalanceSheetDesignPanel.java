package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

public class BalanceSheetDesignPanel {
	Connection m_conn;
	long m_sessionid = -1;
public BalanceSheetDesignPanel(Connection conn, long sessionid) {
		
		m_conn = conn;
		m_sessionid = sessionid;		
		/*constructComponent();
		initData();*/
	}
}
