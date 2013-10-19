package pohaci.gumunda.titis.accounting.cgui;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningLoan;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtLoan;
import pohaci.gumunda.titis.accounting.entity.RcvLoan;
import pohaci.gumunda.titis.accounting.logic.BeginningBalanceBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.LoanReceiptBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupLoanReceiptPicker extends LookupPicker {
	private static final long serialVersionUID = 1L;
	//private CompanyLoan companyLoan;
	private GenericMapper m_entityMapper;
	private List rcvloanList;
	private CompanyLoan m_companyLoan=null;

	public LookupLoanReceiptPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Loan Receipt");
		m_entityMapper = MasterMap.obtainMapperFor(RcvLoan.class);
		m_entityMapper.setActiveConn(m_conn);
		header();
		initData();
		setSize(800, 300);
	}

	public LookupLoanReceiptPicker(Connection conn, long sessionid,CompanyLoan companyLoan) {
		super(conn, sessionid, "Lookup Loan Receipt");
		m_entityMapper = MasterMap.obtainMapperFor(RcvLoan.class);
		m_entityMapper.setActiveConn(m_conn);
		m_companyLoan = companyLoan;
		header();
		initData();
		setSize(800, 300);
	}

	void initData() {
		getModel().clearRows();
		int k = 0;
		LoanReceiptBusinessLogic logic = new LoanReceiptBusinessLogic(m_conn, m_sessionid);
		if (m_companyLoan!=null){
			logic.setCompanyLoan(m_companyLoan);
			rcvloanList = new ArrayList();
			try {
				rcvloanList = logic.getOutstandingList();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Iterator iterator = rcvloanList.iterator();
			while(iterator.hasNext()){
				RcvLoan rcv = (RcvLoan) iterator.next();
				getModel().addRow(new Object[]{
						new Integer(++k),
						rcv.getCompanyLoan().getCreditorList().toString(),
						rcv.getCompanyLoan().getName(),
						rcv,
						rcv.getDateReceived(),
						new Double(rcv.getAmount()),
						rcv.getEmpReceived(),
						StateTemplateEntity.status2String(rcv.getStatus())
				});
			}

			GenericMapper maper = MasterMap.obtainMapperFor(BeginningLoan.class);
			maper.setActiveConn(this.m_conn);
			List list=maper.doSelectWhere(IDBConstants.ATTR_COMPANY_LOAN + "=" + m_companyLoan.getIndex());
			for(int i=0;i<list.size();i++){
				BeginningLoan data=(BeginningLoan)list.get(i);
				System.err.println(data.getIndex());

				GenericMapper maper2 = MasterMap.obtainMapperFor(PmtLoan.class);
				maper2.setActiveConn(this.m_conn);
				List list2=maper2.doSelectWhere(IDBConstants.ATTR_BEGINNING_BALANCE + "=" + data.getIndex() + " AND " +
						IDBConstants.ATTR_STATUS + "=3");
				double accumulate =0;
				for(int j=0;j<list2.size();j++){
					PmtLoan pmtLoan=(PmtLoan)list2.get(j);
					accumulate += pmtLoan.getAmount();
				}

				CompanyLoan compLoan =data.getCompanyLoan();
				if (data.getTrans()==null){
					BeginningBalanceBusinessLogic beginLogic = new BeginningBalanceBusinessLogic(m_conn, m_sessionid);
					data.setTrans(beginLogic.findTransaction(data.getCompanyLoan().getUnit()));
					data.showReferenceNo(true);
				}
				data.showReferenceNo(true);
				if (accumulate<data.getAccValue()){
					getModel().addRow(new Object[]{
							new Integer(++k),
							compLoan.getCreditorList().toString(),
							compLoan.getName(),
							data,
							"",
							new Double(data.getAccValue()),
							"",
							""
					});
				}
			}
		}
	}

	private void header() {
		getModel().addColumn("No.");
		getModel().addColumn("CreditorCode");
		getModel().addColumn("Loan Code");
		getModel().addColumn("Receipt No");
		getModel().addColumn("Receipt Date");
		getModel().addColumn("Receipt Amount");
		getModel().addColumn("Received by");
		getModel().addColumn("Status");
	}

	public void refreshData(CompanyLoan companyLoan){
		this.m_companyLoan=companyLoan;
		initData();
	}

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if(rowindex != -1) {
			setObject(getModel().getValueAt( rowindex, 3));
		}
	}
}

