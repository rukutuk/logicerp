package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningLoan;
import pohaci.gumunda.titis.accounting.entity.PmtLoan;
import pohaci.gumunda.titis.accounting.entity.RcvLoan;
import pohaci.gumunda.titis.accounting.logic.BeginningBalanceBusinessLogic;
import pohaci.gumunda.titis.application.DoubleFormatted;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupLoanPaymentDetail extends LookupPicker {
	
	private static final long serialVersionUID = 1L;
	private PmtLoan pmtLoan;
	private CompanyLoan companyLoan;
	private GenericMapper m_entityMapper;
	private List pmtloanList;
	Object m_obj=null;
	private double accumulatedPayment;
	private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
	PmtLoan m_entity;
		
	public LookupLoanPaymentDetail(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Loan");
		m_entityMapper = MasterMap.obtainMapperFor(PmtLoan.class);
		m_entityMapper.setActiveConn(conn);
		initColumnHeader();		
		setSize(800, 300);
	}
	
	public void setCompanyLoan(CompanyLoan companyLoan,PmtLoan entity) {
		this.companyLoan = companyLoan;
		m_entity = entity;
		procesData();
	}
	
	public double getAccumulatedPayment() {
		return accumulatedPayment;
	}
	
	public void clearTable(){
		getModel().clearRows();
	}
	public void setRcvLoan(Object obj){		
		m_obj = obj;		
		procesData();
	}
	
	double accumulatedRupiahBase;
	double accumulatedDollarBase;
	void procesData() {
		int no=1;
		clearTable();
		if (m_entity!=null){
			String where = pohaci.gumunda.titis.accounting.dbapi.IDBConstants.ATTR_AUTOINDEX + "!=" + m_entity.getIndex();
			pmtloanList=m_entityMapper.doSelectWhere(where);
		}else
			pmtloanList=m_entityMapper.doSelectAll();
		accumulatedPayment=0;
		accumulatedRupiahBase=0;
		accumulatedDollarBase=0;
		
		for(int i = 0; i < pmtloanList.size();  i ++) {
			pmtLoan=(PmtLoan) pmtloanList.get(i);
			if (m_obj instanceof RcvLoan) {
				RcvLoan rcvLoan = (RcvLoan) m_obj;
				if(companyLoan!=null && pmtLoan.getLoanReceipt()!=null && rcvLoan!=null){
					boolean isPosted = pmtLoan.getStatus()==StateTemplateEntity.State.POSTED;
					boolean isEqual=pmtLoan.getLoanReceipt().getReferenceNo().equals(rcvLoan.toString());
					if(isEqual && isPosted){
						accumulatedPayment=accumulatedPayment+pmtLoan.getAmount();
						getModel().addRow(new Object[]{
								new Integer(no++),
								companyLoan.getCreditorList().toString(),
								companyLoan.getName(),
								companyLoan.getCurrency().getSymbol()+" "+(new DoubleFormatted(companyLoan.getInitial())).toString(),
								rcvLoan.getCurrency().getSymbol()+" "+(new DoubleFormatted(rcvLoan.getAmount())).toString() , 
								pmtLoan.getReferenceNo(),
								dateformat.format(pmtLoan.getDateReceived()),  //6
								pmtLoan.getCurrency().getSymbol()+" "+(new DoubleFormatted(pmtLoan.getAmount())).toString() , 
								StateTemplateEntity.status2String(pmtLoan.getStatus())
						});						
					}
				}				
			}
			else if (m_obj instanceof BeginningLoan) {
				BeginningLoan beginningLoan = (BeginningLoan) m_obj;
				if (companyLoan!=null && pmtLoan.getBeginningBalance()!=null){
					boolean isPosted = pmtLoan.getStatus()==StateTemplateEntity.State.POSTED;
					if (pmtLoan.getBeginningBalance().getTrans()==null){						
						BeginningBalanceBusinessLogic logic = new BeginningBalanceBusinessLogic(m_conn, m_sessionid);
						//beginningLoan.setTrans(logic.findTransaction(beginningLoan.getCompanyLoan().getUnit()));
						pmtLoan.getBeginningBalance().setTrans(logic.findTransaction(beginningLoan.getCompanyLoan().getUnit()));
					}
					//boolean isEqual=beginningLoan.getTrans().getReference().equals(beginningLoan.toString());
					boolean isEqual=pmtLoan.getBeginningBalance().getIndex()==beginningLoan.getIndex();
					
					if (isPosted && isEqual){
						accumulatedPayment=accumulatedPayment+pmtLoan.getAmount();
						getModel().addRow(new Object[]{
								new Integer(no++),
								companyLoan.getCreditorList().toString(),
								companyLoan.getName(),
								companyLoan.getCurrency().getSymbol()+" "+(new DoubleFormatted(companyLoan.getInitial())).toString(),
								beginningLoan.getCurrency().getSymbol()+" "+(new DoubleFormatted(beginningLoan.getAccValue())).toString() , 
								pmtLoan.getReferenceNo(),
								dateformat.format(pmtLoan.getDateReceived()),  //6
								beginningLoan.getCurrency().getSymbol()+" "+(new DoubleFormatted(pmtLoan.getAmount())).toString(), 
								StateTemplateEntity.status2String(pmtLoan.getStatus())
						});
					}
				}
			}
		}
	}	
	
	private void initColumnHeader() {
		getModel().addColumn("No.");
		getModel().addColumn("CreditorCode");
		getModel().addColumn("Loan Code");
		getModel().addColumn("Initial Loan");
		getModel().addColumn("Total Receive");
		getModel().addColumn("Voucher No");
		getModel().addColumn("Voucher Date");
		getModel().addColumn("Total Payment");
		getModel().addColumn("Status");
	}
	
	public void select() {
		int rowindex = m_table.getSelectedRow();
		if(rowindex != -1) {
			setObject(getModel().getValueAt(rowindex, 6));
		}
	}
}
