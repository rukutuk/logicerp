package pohaci.gumunda.titis.application.db;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class GenericMapper {
	DataMapping mapping;
	Connection activeConn;
	static boolean traceSql = true; 
	protected LHydrationProvider hprovider;
	class LHydrationProvider implements HydrationProvider {

		public Object hydrateBySelect(Object pkValue, Class associatedClass) {
			if (pkValue == null)
				return null;
			GenericMapper m = MasterMap.obtainMapperFor(associatedClass);
			m.setActiveConn(activeConn);
			return m.doSelectByIndex(pkValue);
		}

		public Object hydrateByRefLookup(Object pkValue, Class associatedClass) {
			if (pkValue == null)
				return null;
			LookupMap map = MasterMap.obtainLookupMap(associatedClass);
			return map.lookupById(pkValue);
		}
		
	}
    public GenericMapper(DataMapping mapping)
    {	this.mapping = mapping;
    	this.hprovider = new LHydrationProvider();
    }
    public GenericMapper(Class clazz)
    {
    	this.mapping = DataMapping.create(clazz);
    	this.hprovider = new LHydrationProvider();
    }
    public void ddlDrop()
    {
    	String dropSql = mapping.dropTableSql();
    	if (traceSql) System.out.println(dropSql);
    	checkConn();
    	try {
			activeConn.createStatement().execute(dropSql);
		} catch (SQLException e) {
			actFailure(e,"drop table");
		}
    }
    public void ddlCreate()
    {
    	String createSql = mapping.createTableSql().replace("FIXED", "INT");    	
    	System.out.println(createSql);
    	if (traceSql) System.out.println(createSql);
    	checkConn();
        Statement s = null;
    	try {
            s = activeConn.createStatement();
			s.execute(createSql);
            s.close();
		} catch (SQLException e) {
			actFailure(e,"create table");
		}
        if (s!=null)
        try {
            s.close();
        } catch (SQLException e) {
        }
    }
    public void doInsert(Object obj){
    	doInsert(obj,null);
    }
    public final List doSelectWhere(String clause){
    	return doSelectWhere(clause,null);
    }
    
    public List doSelectWhere(String clause, Object[] params){
    	String selectQry = "SELECT " //+ " * "
    	+ mapping.insertColumnList() 
    	+ " FROM " + mapping.tableName + " WHERE " + clause;
    	if (traceSql) 
    		System.out.println(selectQry);
    	//System.out.println(selectQry);
    	
    	checkConn();
    	ArrayList resultList = new ArrayList();
    	int paramCount = 0;
    	if (params!=null)
    		paramCount = params.length;
    	Object obj;
    	MasterMap.setPrimaryConnection(this.activeConn);
    	mapping.setHydrationProvider(hprovider);
    	
    	try {
    		PreparedStatement p = activeConn.prepareStatement(selectQry);
    		int i;
    		for (i=0; i<paramCount; i++)
    			p.setObject(i+1,params[i]);
    		ResultSet rs = p.executeQuery();
    		while (rs.next()){
	    		boolean useMultiArgs = false;
				try {
	    			obj = mapping.createInstance();
	    			useMultiArgs  = (obj==null);
	    		} catch (Exception e1) {
	    			actFailure(e1,"create instance during selectwhere");
	    			return null;
	    		}
	    		obj = recordsetToObject(obj, useMultiArgs, rs);
	    		resultList.add(obj);
    		}
		} catch (Exception e) {
			actFailure(e,"select where " + clause);
		}
		return resultList;	
    }
    
    public List doSelectAll(){
    	return
    		doSelectWhere("1=1",null);
    }
    
    public final List doSelectChildsOf(Object parent){
    	Object parentId;
    	parentId = readParentPK(parent);
    	return doSelectWhere(mapping.parentField.fieldName + " = ?",
    			new Object[] { parentId } );
    }
    
    public Object doSelectByIndex(Object id){
    	if (traceSql) 
    		System.out.println(mapping.insertColumnList() );
    	if (traceSql) 
    		System.out.println(mapping.tableName );
    	String what="autoindex";
    	if(mapping.thePK!=null)
    		what=mapping.thePK.fieldName;
    		
    			
    	//System.out.println(mapping.thePK.fieldName);
    	String selectByIdx = "SELECT " + mapping.insertColumnList() 
    	+ " FROM " + mapping.tableName + " WHERE " + what + " = ?";
    	
    	if (traceSql) System.out.println(selectByIdx);
    	
    	checkConn();
    	Object obj;
    	boolean useMultiArgs;
    	mapping.setHydrationProvider(hprovider);
    	
		try {
			obj = mapping.createInstance();
			useMultiArgs = (obj==null);
		} catch (Exception e1) {
			actFailure(e1,"create instance during selectbyidx");
			return null;
		}
		try {
    		PreparedStatement p = activeConn.prepareStatement(selectByIdx);
    		p.setObject(1,id);
    		ResultSet rs = p.executeQuery();
    		if (rs.next())
    			return recordsetToObject(obj, useMultiArgs, rs);
    		else
    			return null;
		} catch (Exception e) {
			actFailure(e,"select by index");
		}
		return null;
    }
	protected Object recordsetToObject(Object obj, boolean useMultiArgs, ResultSet rs) throws SQLException {
        Object result;
		if (useMultiArgs)
        {
            result = mapping.createInstance(rs);
        }
		else
        {
			mapping.readFields(rs,obj);
            result = obj;
        }
		return result;
	}
	
    public void doInsert(Object obj,Object[] otherInfo){
    	boolean autoNumber = false;
    	if (mapping.thePK!=null)
    	  autoNumber = ((Number)readPK(obj)).longValue() == 0L;
        PreparedStatement prepStmt = null;
        PreparedStatement queryIdStmt = null;
    	if (obj!=null){
	    	if (mapping.getClazz().isAssignableFrom(obj.getClass())){
		    	String insSql = "INSERT INTO " + mapping.tableName +  " (" + mapping.insertColumnList() +
		    	") VALUES " +		    	
		    	 mapping.insertColumnQMarks();
		    	if (traceSql) 
		    		System.out.println(insSql);
		    	try {
		        	checkConn();
					prepStmt = activeConn.prepareStatement(insSql);
					mapping.prepareFields(prepStmt,obj,otherInfo);
					prepStmt.execute();
					if (prepStmt.getUpdateCount()!=1)
						actFailure(new Exception("updated rows <> 1"),"insert");
					if (autoNumber){
						queryIdStmt = activeConn.prepareStatement(
								"SELECT MAX(" + mapping.thePK.fieldName +") from " + mapping.tableName);
						ResultSet queryIdResult = queryIdStmt.executeQuery();                        
						if (queryIdResult.next()){
							Object id = mapping.thePK.readValueFrom(queryIdResult,1);
							try {
								mapping.thePK.writeToObj(obj,id);
                                if (traceSql)
                                    System.out.println("set id succeeded");
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
                        queryIdStmt.close();
					}
		    	} catch (SQLException e) {
					actFailure(e,"insert");
				}
                if (queryIdStmt!=null)
                    try {
                        queryIdStmt.close();
                    } catch (SQLException e) {
                    }
                if (prepStmt!=null)
                    try {
                        prepStmt.close();
                    } catch (SQLException e) {
                    }
	    	}
	    	else
	    		actFailure(new Exception("object type mismatch"),"insert");
    	}
    }
    
    public void doDelete(Object obj){
    	String delSQL = "DELETE FROM " + mapping.tableName + " WHERE " +
    	mapping.thePK.fieldName + " = ? ";
    	checkConn();
        PreparedStatement p = null;
    	try {
			p = activeConn.prepareStatement(delSQL);
			mapping.thePK.prepField(1,p,obj);
            if (traceSql)
                System.out.println(delSQL);
			p.executeUpdate();
		} catch (SQLException e) {
			actFailure(e,"delete");
		} catch (Exception e) {
			actFailure(e,"delete");
		}
        if (p!=null)
            try {
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public void doDeleteChildsOf(Object parent){
    	Object parentId = readParentPK(parent);
    	doDeleteByColumn(mapping.parentField.fieldName,parentId.toString());
    }
    //saya tambahain dikit boleh ya
    // boleh tapi namanya diganti ya
    public void doDeleteByColumn(String obj,String param)
    {
    	String delSQL = "DELETE FROM " + mapping.tableName + " WHERE " +
    	obj + " = ? ";
        if (traceSql)
            System.err.println(delSQL);
    	checkConn();
        PreparedStatement p=null;
    	try {
			p = activeConn.prepareStatement(delSQL);
		    p.setString(1, param);
			p.executeUpdate();
		} catch (SQLException e) {
			actFailure(e,"delete");
		} catch (Exception e) {
			actFailure(e,"delete");
		}
        if (p!=null)
            try {
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
    public boolean doUpdate(Object obj)
    {
    	if (mapping.aggregatorClass!=null)
    		throw new RuntimeException("Cannot update aggregated item class " + mapping.clazz.getName());
    	String updSQL = "UPDATE " + mapping.tableName + " SET " + mapping.createUpdateColumnList() 
    	+ " WHERE " +
    	mapping.thePK.fieldName + " = ? ";
    	if (traceSql) 
    		System.out.println(updSQL);
    	checkConn();
    	int rowCnt=0;
        PreparedStatement p = null;
    	try {
			p = activeConn.prepareStatement(updSQL);
			mapping.prepUpdateFields(p,obj);
			rowCnt= p.executeUpdate();
			System.out.println(updSQL);
		} catch (SQLException e) {
			actFailure(e,"update");
		}
        if (p!=null)
            try {
                p.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
		return rowCnt!=0;
    }
	protected void actFailure(Exception e, String actName) {
		e.printStackTrace();
		throw new RuntimeException(e);
		//JOptionPane.showMessageDialog(null,"Error while trying to do " + actName + " on " + mapping.tableName + "\r\n" +
		//		e.getMessage());
	}
	protected void checkConn() {
		if (activeConn==null)
			throw new RuntimeException("ActiveMapper:No connection given while trying to act");
	}
	public Connection getActiveConn() {
		return activeConn;
	}
	public void setActiveConn(Connection activeConn) {
		this.activeConn = activeConn;
	}
	public void doInsert(Object entity, long index) {
		// inlined:
		// this.doInsert(entity,new Object[] { new Long(index) });
		this.doInsert(entity,new Object[] { new Long(index) });
	}
	public void doInsert(Object entity, Object parent) {
	    Object parentId = readParentPK(parent);
	    this.doInsert(entity,new Object[] { parentId });
	}
	private Object readParentPK(Object parent) {
		Object parentId;
	    GenericMapper parentMapper = MasterMap.obtainMapperFor(mapping.aggregatorClass);
	    parentId = parentMapper.readPK(parent);
		return parentId;
	}
	public Object readPK(Object entity) {
		try {
			return mapping.thePK.propDesc.getReadMethod().invoke(entity,null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
