/**
 * 
 */
package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.beanutils.PropertyUtils;

public class DataFieldAssociateToOne extends DataFields
{
	/**
	 * 
	 */
	Class associatedClass;
	String pkName;
	boolean hydrateBySelect=true;
	private boolean hydrateByRefLookup;
	//private boolean hydrateByJoin;
	public DataFieldAssociateToOne(DataMapping mapping, PropertyDescriptor propDesc)
	{
		super(mapping, propDesc);
		associatedClass = propDesc.getPropertyType();
		pkName = "index";
		setFieldType("FIXED(38)");
		longHelper = new DataFieldLong(mapping,propDesc);
        phase = 2;
	}
	
	public DataFields useRefLookup()
	{
		hydrateBySelect=false;
		hydrateByRefLookup=true;
		return this;
	}
	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		Object associatedObj = propDesc.getReadMethod().invoke(obj,null);
		Object pkValue = null;
		if (associatedObj!=null)
			pkValue = PropertyUtils.getProperty(associatedObj,pkName);
		else
			pkValue = null;
		prepStmt.setObject(paramIdx,pkValue);
	}
	DataFieldLong longHelper;
	public Object readValueFrom(ResultSet rs, int colIdx) throws SQLException {
		Object pkValue = longHelper.readValueFrom(rs,colIdx);
		Object associatedObj=null;
		if (hydrateBySelect)
			associatedObj = this.mapping.hydrationProvider.hydrateBySelect(pkValue,associatedClass);
		else if (hydrateByRefLookup)
			associatedObj = this.mapping.hydrationProvider.hydrateByRefLookup(pkValue,associatedClass);
		return associatedObj;
	}
	public String constraintSql()
	{
		GenericMapper mapper = MasterMap.obtainMapperFor(associatedClass);
		return "FOREIGN KEY (" + this.fieldName + ") REFERENCES " +
			mapper.mapping.tableName;
	}

    public Object readIdFrom(ResultSet rs) {
        int idx;
        try {
            idx = rs.findColumn(this.fieldName);
            Object id = longHelper.readValueFrom(rs,idx);
            return id;
        } catch (SQLException e) {
            return null;
        }
    }
}