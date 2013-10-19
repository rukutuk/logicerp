/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.accounting.entity.PmtLoan;
import pohaci.gumunda.titis.accounting.entity.RcvLoan;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class LoanReceiptBusinessLogic extends TransactionBusinessLogic {

	private CompanyLoan companyLoan;
	
	public LoanReceiptBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(RcvLoan.class);
		entityMapper.setActiveConn(connection);
	}
	
	public void setCompanyLoan(CompanyLoan companyLoan) {
		this.companyLoan = companyLoan;
	}

	public List getOutstandingList() throws Exception {
		List resultList = new ArrayList();		
		if(companyLoan!=null){					
			String strWhere = "COMPANYLOAN=" + companyLoan.getIndex() + " AND STATUS=3";
			System.err.println(strWhere);
			List list = entityMapper.doSelectWhere(strWhere);		
			Iterator iterator = list.iterator();
			while(iterator.hasNext()){
				RcvLoan rcvLoan = (RcvLoan) iterator.next();			
				if(isOutstanding(rcvLoan))
					resultList.add(rcvLoan);
			}
		}		
		return resultList;
	}

	private boolean isOutstanding(RcvLoan rcvLoan) {
		double loan = rcvLoan.getAmount();
		double paid = getAccumulatedPaid(rcvLoan);
		return (loan > paid);
	}

	private double getAccumulatedPaid(RcvLoan rcv) {
		GenericMapper mapper = MasterMap.obtainMapperFor(PmtLoan.class);
		mapper.setActiveConn(connection);		
		List list = mapper.doSelectWhere("LOANRECEIPT=" + rcv.getIndex() + " AND STATUS=3");		
		Iterator iter = list.iterator();		
		double amt = 0;
		while(iter.hasNext()){
			PmtLoan pmt = (PmtLoan) iter.next();			
			amt += pmt.getAmount();
		}		
		return amt;
	}
}
