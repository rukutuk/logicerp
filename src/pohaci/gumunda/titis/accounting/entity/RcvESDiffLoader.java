package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.SearchReceiptDialog.SearchTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class RcvESDiffLoader implements ReceiptLoader{
	private GenericMapper mapper  =
		MasterMap.obtainMapperFor(RcvESDiff.class);

	public RcvESDiffLoader(Connection m_conn) {
		mapper.setActiveConn(m_conn);
	}
	public Object[] find(String criterion) {
		List list = mapper.doSelectWhere(criterion);
		return list.toArray();
	}

	public String getCriterion(SearchTable table, String operator) {
		System.err.println("masuk sini");
		return null;
	}
	public String getCriterion(pohaci.gumunda.titis.accounting.cgui.SearchPurchaseReceiptDialog.SearchTable table, String operator) {
		return null;
	}

}
