package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.TransactionPosted;
import pohaci.gumunda.titis.accounting.entity.TransactionPostedDetail;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.PeriodStartEnd;
import pohaci.gumunda.titis.application.ReportUtils;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class General_ladgerChild {
	//JasperReport m_jasperReport;
	String m_jasperReport;
	JRTableModelDataSource m_ds;
	Connection m_conn;
	long m_sessionid = -1;
	String filename = "GenLedgerChild.jrxml";
	AccountingBusinessLogic m_logic;

	boolean m_excel;
	public General_ladgerChild(boolean excel){
		try {
			m_excel = excel;
			//if(filename.equals(""))
			//	return;
			//m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			m_jasperReport = ReportUtils.compileReport("GenLedgerChild");
			DefaultTableModel model = new DefaultTableModel();
			tableheader(model);
			empty(model);
			m_ds = new JRTableModelDataSource(model);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	JasperPrint m_jasperPrint;
	JRMapCollectionDataSource m_submap;

	public General_ladgerChild(Connection conn,long sessionid,Account account,PeriodStartEnd periodStartEnd,UnitPicker unitPicker,boolean excel){
		try {
			m_conn = conn;
			m_sessionid = sessionid;
			m_excel = excel;
			m_logic = new AccountingBusinessLogic(m_conn);
			//if(filename.equals(""))
			//	return;
			//m_jasperReport = JasperCompileManager.compileReport("report/" + filename);
			m_jasperReport = ReportUtils.compileReport("Income_statement");
			DefaultTableModel model = new DefaultTableModel();
			tableheader(model);
			nonEmpty(model,account,periodStartEnd,unitPicker);
			m_ds = new JRTableModelDataSource(model);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public JasperPrint getJasperPrint(){
		return m_jasperPrint;
	}

	private void empty(DefaultTableModel model) {
		model.addRow(new Object[]{"","","","","","","","","","",new Integer(0)});
	}

	private void tableheader(DefaultTableModel model) {
		model.addColumn("fieldacccode");
		model.addColumn("fieldaccname");
		model.addColumn("fieldperiod");
		model.addColumn("field0");
		model.addColumn("field1");
		model.addColumn("field2");
		model.addColumn("field3");
		model.addColumn("field4");
		model.addColumn("field5");
		model.addColumn("field6");
		model.addColumn("status");
	}


	public void nonEmpty(DefaultTableModel model,Account account,PeriodStartEnd periodDate,UnitPicker unitPicker) {
		try {
			SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat m_desimalFormat = new DecimalFormat("#,##0.00");
			if (m_excel)
				m_desimalFormat = new DecimalFormat("###.00");

			String strUnitWhere = "";
			if (unitPicker.getUnit()!=null)
				strUnitWhere = " AND tp.UNIT="+ unitPicker.getUnit().getIndex();

			String queryBegining = "SELECT SUM("+IDBConstants.ATTR_EXCHANGE_RATE+"*"+IDBConstants.ATTR_VALUE+"*(1-2*"+IDBConstants.ATTR_BALANCE_CODE+")) debitvalue " +
			"FROM "+IDBConstants.TABLE_TRANSACTION_VALUE_POSTED+" tv, "+IDBConstants.ATTR_ACCOUNT+" ac, "+IDBConstants.ATTR_TRANSACTION_POSTED+" tp " +
			"WHERE " +
			"ac."+IDBConstants.ATTR_AUTOINDEX+" = tv."+IDBConstants.ATTR_ACCOUNT+" AND tp."+IDBConstants.ATTR_AUTOINDEX+"="+IDBConstants.ATTR_TRANSACTION_POSTED+" AND ac."+IDBConstants.ATTR_PATH+" LIKE '" + account.getTreePath() + "%' " +
			"AND tp.TRANSACTIONDATE < '"+ m_dateFormat2.format(periodDate.m_startDate.getDate()) +"'" + strUnitWhere;

			double debitValue = m_logic.getDebitValue(m_sessionid,IDBConstants.MODUL_ACCOUNTING,queryBegining);

			double totDeb = 0;
			double totCre = 0;

			double valBalance = debitValue;
			if (debitValue<0)
				valBalance = -debitValue;

			String period = m_dateFormat.format(periodDate.m_startDate.getDate()) + " - " +
			m_dateFormat.format(periodDate.m_endDate.getDate());
			model.addRow(new Object[]{account.getCode(),account.getName(),period,"","","Begining Balance","","","",m_desimalFormat.format(valBalance),new Integer(0)});

			String strUnitSelectWhere = "";
			if (unitPicker.getUnit()!=null)
				strUnitSelectWhere = " AND a." + IDBConstants.ATTR_UNIT + "=" +
				unitPicker.getUnit().getIndex();
			String query ="select distinct a.* from transactionposted a,transvalueposted b where  a.autoindex=b.transactionposted and b.account="+account.getIndex()+" and a.transactiondate " +
			"between '" + m_dateFormat2.format(periodDate.m_startDate.getDate()) +"' AND '" + m_dateFormat2.format(periodDate.m_endDate.getDate()) + "'" +
			strUnitSelectWhere + " order by a.transactiondate, a.posteddate, a.referenceno";
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			TransactionPosted[] transPost = logic.getTransactionCriteria(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);

			for (int i=0;i<transPost.length;i++){
				GenericMapper mapper=MasterMap.obtainMapperFor(TransactionPostedDetail.class);
				String unit = "";
				if (transPost[i].getUnit()!=null)
					unit = transPost[i].getUnit().toString();
				mapper.setActiveConn(m_conn);
				String selectWhere = IDBConstants.ATTR_ACCOUNT + "=" +account.getIndex()+ " AND " +IDBConstants.ATTR_TRANSACTION_POSTED+"="+transPost[i].getIndex();
				List RS=mapper.doSelectWhere(selectWhere);
				for(int j=0;j<RS.size();j++){
					TransactionPostedDetail transPostVal=(TransactionPostedDetail)RS.get(j);
					double value = transPostVal.getValue()*transPostVal.getExchangeRate();
					if (account.getBalance()==0){
						if (value>0){
							valBalance +=value;
							totDeb +=value;
							model.addRow(new Object[]{account.getCode(),account.getName(),period,m_dateFormat.format(transPost[i].getTransDate()),unit,transPost[i].getDescription(),transPost[i].getReference(),
									m_desimalFormat.format(value),"",m_desimalFormat.format(valBalance),new Integer(1)});
						}else{
							valBalance += value;
							totCre +=value;
							model.addRow(new Object[]{account.getCode(),account.getName(),period,m_dateFormat.format(transPost[i].getTransDate()),unit,transPost[i].getDescription(),transPost[i].getReference(),
									"",m_desimalFormat.format(-value),m_desimalFormat.format(valBalance),new Integer(1)});
						}
					}else{
						if (value>0){
							valBalance +=value;
							totCre+=value;
							model.addRow(new Object[]{account.getCode(),account.getName(),period,m_dateFormat.format(transPost[i].getTransDate()),unit,transPost[i].getDescription(),transPost[i].getReference(),
									"",m_desimalFormat.format(value),m_desimalFormat.format(valBalance),new Integer(1)});
						}else{
							valBalance +=value;
							totDeb+=value;
							model.addRow(new Object[]{account.getCode(),account.getName(),period,m_dateFormat.format(transPost[i].getTransDate()),unit,transPost[i].getDescription(),transPost[i].getReference(),
									m_desimalFormat.format(-value),"",m_desimalFormat.format(valBalance),new Integer(1)});
						}
					}
				}
			}
			String strTotDeb = "";
			String strTotCre = "";
			if (totDeb<0)
				totDeb = -totDeb;
			if (totCre<0)
				totCre = -totCre;

			strTotDeb = m_desimalFormat.format(totDeb);
			strTotCre = m_desimalFormat.format(totCre);
			
			model.addRow(new Object[]{"","","","","","","Total",strTotDeb,strTotCre,"",new Integer(1)});

		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public JRTableModelDataSource getDataSource(){
		return m_ds;
	}

	/*public JasperReport getJasperReport(){
		return m_jasperReport;
	}*/
	
	public String getJasperReport(){
		return m_jasperReport;
	}
}
