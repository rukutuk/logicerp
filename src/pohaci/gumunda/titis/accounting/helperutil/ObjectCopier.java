/**
 * 
 */
package pohaci.gumunda.titis.accounting.helperutil;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.aas.dbapi.ConnectionManager;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.SalesItem;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class ObjectCopier {
	private Class clazz = SalesInvoice.class;
	private Class childClazz = SalesItem.class;
	
	private ConnectionManager connectionManager;
	private Connection connection;
	private ConnectionManager connectionManagerTarget;
	private Connection connectionTarget;
	
	/**
	 * 
	 */
	public ObjectCopier() {
		super();
		prepareConnection();
	}

	/**
	 * 
	 */
	private void prepareConnection() {
		try {
			connectionManager = new ConnectionManager("sampurna");
			connection = connectionManager.getConnection();
			
			connectionManagerTarget = new ConnectionManager("tisas");
			connectionTarget = connectionManagerTarget.getConnection();
		} catch (Exception ex) {
			System.err.println("connection error");
			System.exit(0);
		}
	}

	public void copy() {
		List list = getList(connection);
		
		copyToTarget(list, connectionTarget);
		
	}


	/**
	 * @param list
	 * @param activeConn
	 */
	private void copyToTarget(List list, Connection activeConn) {
		GenericMapper mapper = MasterMap.obtainMapperFor(clazz);
		mapper.setActiveConn(activeConn);
		
		Iterator iterator = list.iterator();
		
		GenericMapper childMapper = MasterMap.obtainMapperFor(childClazz);
		childMapper.setActiveConn(activeConn);
		
		while(iterator.hasNext()) {
			SalesInvoice object = (SalesInvoice) iterator.next();
			object.setIndex(0L);
			object.setTrans(null);
			object.setStatus(new Short("0").shortValue());
			object.setSubmitDate(null);
			
			
			mapper.doInsert(object);
			
			// child
			SalesItem[] items = object.getSalesItem();
			
			for(int i=0; i<items.length; i++) {
				SalesItem salesItem = items[i];
				salesItem.setSalesInvoice(object.getIndex());
				
				salesItem.setIndex(0L);
				
				childMapper.doInsert(salesItem);
			}
		}
	}

	/**
	 * @param activeConn 
	 * @return
	 */
	private List getList(Connection activeConn) {
		GenericMapper mapper = MasterMap.obtainMapperFor(clazz);
		mapper.setActiveConn(activeConn);
		
		String doWhere = "transactiondate >= '2009-01-01'";
		List list = mapper.doSelectWhere(doWhere);
		
		Iterator iterator = list.iterator();
		
		GenericMapper childMapper = MasterMap.obtainMapperFor(childClazz);
		childMapper.setActiveConn(activeConn);
		
		while(iterator.hasNext()) {
			SalesInvoice object = (SalesInvoice) iterator.next();
			
			List childList = childMapper.doSelectWhere("SALESINVOICE = " + new Long(object.getIndex()).toString());
			
			SalesItem[] items = (SalesItem[]) childList.toArray(new SalesItem[childList.size()]);
			
			object.setSalesItem(items);
			
		}
	
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ObjectCopier copier = new ObjectCopier();
		copier.copy();
	}

}
