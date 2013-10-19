package pohaci.gumunda.titis.application.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TwoPhaseMapper extends GenericMapper {
	class TPhaseHydrationProvider extends LHydrationProvider{
		Map superMap;
		TPhaseHydrationProvider(Map superMap)
		{
			this.superMap=superMap;
		}
		public Object hydrateByRefLookup(Object pkValue, Class associatedClass) {
			if (associatedClass == mapping.clazz)
				return superMap.get(pkValue);
			return super.hydrateByRefLookup(pkValue, associatedClass);
		}
		
		public Object hydrateBySelect(Object pkValue, Class associatedClass) {
			if (associatedClass == mapping.clazz)
				return superMap.get(pkValue);
			return super.hydrateBySelect(pkValue, associatedClass);
		}
		
	}
	public Object doSelectByIndex(Object id) {
		// TODO Auto-generated method stub
		return super.doSelectByIndex(id);
	}
	private List doSelectPhaseOne(String clause, List referredIdList) {
		ArrayList phaseOneResult = new ArrayList();
		String selectQry = "SELECT " //+ " * "
			+ mapping.insertColumnList() 
			+ " FROM " + mapping.tableName + " WHERE " + clause;
		if (traceSql) 
			System.out.println(selectQry);
		checkConn();
		MasterMap.setPrimaryConnection(this.activeConn);
		mapping.setHydrationProvider(hprovider);
		
		try {
			PreparedStatement p = activeConn.prepareStatement(selectQry);
			ResultSet rs = p.executeQuery();
			while (rs.next())
			{
				Object obj=null;
				try {
					obj = mapping.createInstance();
				} catch (Exception e1) {
					actFailure(e1,"create instance during selectwhere");
					return null;
				}
				if (obj==null)
				{
					obj = mapping.createInstance(rs);
				} else {
					mapping.readFields(rs,obj,1, referredIdList); // first phase
				}
				phaseOneResult.add(obj);
			}
		} catch (Exception ex)
		{
			actFailure(ex,"phase one select failed");
		}
		return phaseOneResult;
	}
	public List doSelectWhere(String clause, Object[] params) {
//		ArrayList queriedIdList = new ArrayList();
//		ArrayList referredIdList = new ArrayList();
//		
//		String selectQry = "SELECT " //+ " * "
//		+ mapping.insertColumnList() 
//		+ " FROM " + mapping.tableName + " WHERE " + clause;
//		if (traceSql) 
//		System.out.println(selectQry);
//		
//		checkConn();
//		ArrayList resultList = new ArrayList();
//		int paramCount = 0;
//		if (params!=null)
//		paramCount = params.length;
//		Object obj;
//		MasterMap.setPrimaryConnection(this.activeConn);
//		mapping.setHydrationProvider(hprovider);
//		
//		try {
//		PreparedStatement p = activeConn.prepareStatement(selectQry);
//		int i;
//		for (i=0; i<paramCount; i++)
//		p.setObject(i+1,params[i]);
//		ResultSet rs = p.executeQuery();
//		while (rs.next())
//		{
//		try {
//		obj = mapping.createInstance();
//		} catch (Exception e1) {
//		actFailure(e1,"create instance during selectwhere");
//		return null;
//		}
//		if (obj==null)
//		{
//		obj = mapping.createInstance(rs);
//		} else {
//		mapping.readFields(rs,obj,1, referredIdList); // first phase
//		}
//		Object id = readPK(obj);
//		queriedIdList.add(id);
//		resultList.add(obj);
//		}
//		HashSet remainingIds = new HashSet();
//		ArrayList supraList = new ArrayList(resultList);
//		
//		do
//		{
//		remainingIds = new HashSet();
//		remainingIds.addAll(referredIdList);
//		remainingIds.removeAll(queriedIdList);
//		if (remainingIds.size()>0)
//		{
//		StringBuffer sb = new StringBuffer();
//		Iterator iterator = remainingIds.iterator();
//		while (iterator.hasNext())
//		{
//		sb.append(iterator.next());
//		if (iterator.hasNext())
//		sb.append(",");
//		}
//		String remIdsStr = sb.toString();
//		supraList.addAll(doSelectPhaseOne(" " + this.mapping.thePK.fieldName + " in (" + remIdsStr + ")",referredIdList));
//		queriedIdList.addAll(remainingIds);
//		}
//		} while (remainingIds.size()>0);
//		Map supraMap = createSuperMap(supraList);
//		
//		PreparedStatement p = activeConn.prepareStatement(selectQry);
//		int i;
//		for (i=0; i<paramCount; i++)
//		p.setObject(i+1,params[i]);
//		ResultSet rs = p.executeQuery();
//		while (rs.next())
//		{
//		try {
//		obj = mapping.createInstance();
//		} catch (Exception e1) {
//		actFailure(e1,"create instance during selectwhere");
//		return null;
//		}
//		if (obj==null)
//		{
//		obj = mapping.createInstance(rs);
//		} else {
//		mapping.readFields(rs,obj,1, referredIdList); // first phase
//		}
//		Object id = readPK(obj);
//		Object actualObject = supraMap.get(id);
//		mapping.readFields(rs,actualObject,2,referredIdList);
//		
//		}
//		
//		
//		} catch (Exception e) {
//		actFailure(e,"select where " + clause);
//		}
//		return resultList;
		return null;
	}
	
	private Map createSuperMap(ArrayList supraList) {		
		Map sMap = new HashMap();
		Iterator iterator = supraList.iterator();
		while (iterator.hasNext())
		{
			Object nobj = iterator.next();
			Object id = readPK(nobj);
			sMap.put(id,nobj);
		}
		Map supraMap = sMap;
		return supraMap;
	}
	
	public TwoPhaseMapper(DataMapping mapping) {
		super(mapping);
	}
	
	public TwoPhaseMapper(Class clazz) {
		super(clazz);
	}
	
}
