/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.cgui.BaseCurrency;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.ProjectData;

/**
 * @author dark-knight
 *
 */
public class BeginningSalesAdvanceLogic extends BeginningBalanceBusinessLogic {
	Currency baseCurrency = null;
	ProjectData project = null;

	public void setProject(ProjectData project) {
		this.project = project;
	}

	public BeginningSalesAdvanceLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		baseCurrency = BaseCurrency.createBaseCurrency(connection, sessionId);
		baseCurrency.setIsBase(true);
		entityMapper = MasterMap
				.obtainMapperFor(BeginningAccountReceivable.class);
		entityMapper.setActiveConn(connection);
	}

	public List getOutstanding() {
		// specific account - hanya untuk sales advance
		VariableAccountSetting vas = VariableAccountSetting
				.createVariableAccountSetting(this.connection, this.sessionId,
						IConstants.ATTR_VARS_SALES_ADVANCE);
		List resultList = new ArrayList();
		if (project==null)
			return resultList;
		
		List list = entityMapper.doSelectWhere("account="
				+ vas.getAccount().getIndex() + " and project=" + project.getIndex());
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningAccountReceivable bb = (BeginningAccountReceivable) iterator
					.next();
			System.err.println(bb.getProject().getUnit().getDescription());
			Transaction trans = findTransaction(bb.getProject().getUnit());
			if (trans != null) {
				bb.setTrans(trans);
				bb.showReferenceNo(true);
				if (isOutstanding(bb)) {
					resultList.add(bb);
				}
			}
		}
		return resultList;
	}
	
	public boolean isOutstanding(BeginningAccountReceivable bb) {
		GenericMapper invoiceMapper = MasterMap.obtainMapperFor(SalesInvoice.class);
		invoiceMapper.setActiveConn(this.connection);

		String selectionClausa = IDBConstants.ATTR_BEGINNING_BALANCE + "=" + 
			bb.getIndex() + " AND STATUS=3";
		
		List invoiceList = invoiceMapper.doSelectWhere(selectionClausa);
		
		if(invoiceList.size()==0)
			return true;
		
		return false;
	}
}
