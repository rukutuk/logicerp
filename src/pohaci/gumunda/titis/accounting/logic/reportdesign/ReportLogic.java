package pohaci.gumunda.titis.accounting.logic.reportdesign;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportAccountValue;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportSubtotal;

public abstract class ReportLogic {

	protected Connection connection;
	protected long sessionId;
	public abstract List getDesignList();
	public abstract Design updateDesign(Design design) throws Exception;
	public abstract Design insertDesign(Design design) throws Exception;
	public abstract void deleteDesign(Design design) throws Exception;	
	public abstract void updateRows(Design design) throws Exception;
	protected String createXML(ReportRow root) {
		String xml = "";	
		XStream xStream = new XStream();		
		initAlias(xStream);
		xml = xStream.toXML(root);	
		return xml;
	}

	protected ReportRow createObject(String xml) {
		ReportRow obj = null;	
		System.out.println(xml);
		XStream xStream = new XStream();		
		initAlias(xStream);		
		obj = (ReportRow) xStream.fromXML(xml);	
		return obj;
	}

	private void initAlias(XStream xStream) {
		xStream.setMode(XStream.XPATH_RELATIVE_REFERENCES);		
		xStream.alias("reportRow", ReportRow.class);
		xStream.alias("reportGroup", ReportGroup.class);
		xStream.alias("reportAccountValue", ReportAccountValue.class);
		xStream.alias("reportSubtotal", ReportSubtotal.class);
		xStream.alias("account", Account.class);
	}

	protected List parseString255(String source) {
		String src = source;	
		List list = new ArrayList();	
		while (src.length() > 0) {
			int max = src.length() > 250 ? 250 : (src.length());
			String parse = (String) src.substring(0, max);
			parse += "%";   // magic word
			if (src.length() > (250))
				src = src.substring(250, src.length());
			else
				src = "";
			list.add(parse);
		}	
		return list;
	}
}
