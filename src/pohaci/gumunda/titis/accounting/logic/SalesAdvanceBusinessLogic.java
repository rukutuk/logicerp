/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.SalesAdvance;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.ProjectData;

/**
 * @author dark-knight
 *
 */
public class SalesAdvanceBusinessLogic extends TransactionBusinessLogic {

	private ProjectData projectData;
	
	public SalesAdvanceBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(SalesAdvance.class);
		entityMapper.setActiveConn(connection);
	}
	
	public void setProjectData(ProjectData projectData) {
		this.projectData = projectData;
	}
	
	public List getOutstandingList() throws Exception{
		List resultList = new ArrayList();
		
		if(projectData==null)
			System.err.println("There is no project data");
		
		// get all sales advance
		else{
			String clausa = IDBConstants.ATTR_PROJECT + "=" + projectData.getIndex() + " AND " +
			IDBConstants.ATTR_STATUS + "=3";		    
			
			List salesAdvanceList = entityMapper.doSelectWhere(clausa);		
			
			// prepare sales invoice mapper
			GenericMapper invoiceMapper = MasterMap.obtainMapperFor(SalesInvoice.class);
			invoiceMapper.setActiveConn(this.connection);
			
			// selection
			Iterator iterator = salesAdvanceList.iterator();
			
			while(iterator.hasNext()){
				SalesAdvance salesAdvance = (SalesAdvance) iterator.next();
				
				String selectionCluasa = IDBConstants.ATTR_SALES_ADVANCE + "=" + 
				salesAdvance.getIndex() + " AND STATUS=3";
				
				List invoiceList = invoiceMapper.doSelectWhere(selectionCluasa);
				
				if(invoiceList.size()==0)
					resultList.add(salesAdvance);
			}
			
			
		}
		return resultList;
	}
}
