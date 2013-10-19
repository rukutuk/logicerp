/**
 * 
 */
package pohaci.gumunda.titis.application.db;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFields {
	public static DataFields createDataFields(DataMapping mapping, PropertyDescriptor propDesc) {
		Class propType = propDesc.getPropertyType();
		
		// dicoba
		if(propType==null){
			if(propDesc instanceof IndexedPropertyDescriptor){
				propType = ((IndexedPropertyDescriptor)propDesc).getIndexedPropertyType();
			}
		}
		
		String typeName = propType.getName();
		
		if (typeName.equals("java.lang.String"))
			return new DataFieldString(mapping,propDesc);
		if (typeName.equals("java.util.Date"))
			return new DataFieldUtilDate(mapping,propDesc);
		if (typeName.equals("java.sql.Date"))
			return new DataFieldSqlDate(mapping,propDesc);
			
		if (typeName.equals("short") ||
				typeName.equals("java.lang.Short") 
				)
			return new DataFieldShort(mapping,propDesc);
		if (typeName.equals("long") ||
				typeName.equals("java.lang.Long") 
				)
			return new DataFieldLong(mapping,propDesc);
		if (typeName.equals("int") ||
				typeName.equals("java.lang.Integer") 
				)
			return new DataFieldInt(mapping,propDesc);
		if (typeName.equals("float") ||
			typeName.equals("java.lang.Float"))
			return new DataFieldFloat(mapping,propDesc);
		if (typeName.equals("double") ||
				typeName.equals("java.lang.Double"))
				return new DataFieldDouble(mapping,propDesc);
		if (typeName.equals("java.math.BigDecimal"))
			return new DataFieldBigDecimal(mapping,propDesc);
		if (typeName.equals("pohaci.gumunda.titis.accounting.cgui.Currency"))
			return new DataFieldCurrency(mapping,propDesc);
		if (typeName.equals("boolean") ||
				typeName.equals("java.lang.Boolean"))
			return new DataFieldBoolean(mapping,propDesc);
		
		return new DataFields(mapping, propDesc);
	}
	/**
	 * 
	 */
	final DataMapping mapping;
	String fieldName, propName, caption;
	int maxWidth = 61;
	String fieldType = "VARCHAR" ;
	boolean pk;
	boolean notInBean;
	PropertyDescriptor propDesc;
    int phase = 1;
	boolean unique;
	public DataFields(DataMapping mapping, String fieldName) {
		this.mapping = mapping;
		this.fieldName = fieldName;
		propName = fieldName;
		caption = fieldName;		
	}
	public DataFields useRefLookup()
	{
		return this;
	}
	protected DataFields(DataMapping mapping, PropertyDescriptor propDesc)
	{
		this.mapping = mapping;
		this.propDesc = propDesc;
		this.fieldName = this.mapping.toDbName(propDesc.getName());
		this.propName = propDesc.getName();
		this.caption = propDesc.getDisplayName();
		Class propType = propDesc.getPropertyType();
		if(propType==null){
			if(propDesc instanceof IndexedPropertyDescriptor)
				propType = ((IndexedPropertyDescriptor)propDesc).getIndexedPropertyType();
		}
		this.fieldType = DataMapping.toDbType(propType);
		if  (propDesc.getWriteMethod()==null)
		{
		//	System.err.println("no setter found for " + lastComponent(mapping.getClazz().getName()) + ":" + propDesc.getName());
		}
	}
//	private String lastComponent(String name) {
//		String[] strings = name.split("\\.");
//		if (strings.length==0) return name;
//		return strings[strings.length-1];
//	}
	public final void setNotInBean(boolean b) {
		notInBean = b;
	}
	public final void setIsPK(boolean b) {
		pk = b;
	}
	public final boolean isNotInBean()
	{
		return notInBean;
	}
	public final boolean isPK()
	{
		return pk;
	}
	public final String getCaption() {
		return caption;
	}
	public final DataFields  setCaption(String caption) {
		this.caption = caption;
		return this;
	}
	public final String getFieldName() {
		return fieldName;
	}
	public final DataFields setFieldName(String fieldName) {
		this.fieldName = fieldName;
		return this;
	}
	public final int getMaxWidth() {
		return maxWidth;
	}
	public final DataFields setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
		return this;
	}
	public final String getPropName() {
		return propName;
	}
	public final DataFields setPropName(String propName) {
		this.propName = propName;
		return this;
	}
	public final DataMapping up()
	{
		return this.mapping;
	}
	public String getFieldType() {
		return fieldType;
	}
	public DataFields setFieldType(String fieldType) {
		this.fieldType = fieldType;
		return this;
	}
	public final String columnSql() {
		String pkClause = "";
		if (pk) pkClause = " PRIMARY KEY NOT NULL AUTO_INCREMENT";
		else
		if (unique) pkClause = " UNIQUE ";
		String maxLenClause = "";
		if (fieldType.equals("VARCHAR"))
			maxLenClause = "(" + maxWidth + ")";
		return fieldName + " " + fieldType + maxLenClause +  pkClause;
	}
	public void prepField(int paramIdx,PreparedStatement prepStmt, Object obj) throws Exception {
		if (propDesc!=null)
		{
			Object v = propDesc.getReadMethod().invoke(obj,null);
			prepStmt.setObject(paramIdx, v);
			
		}
		throw new RuntimeException("unimplemented prepField on generic datafield");
	}
	public final boolean isUnique() {
		return unique;
	}
	public final void setUnique(boolean unique) {
		this.unique = unique;
	}
	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		throw new RuntimeException("unimplemented readValueFrom on generic datafield");
	}
	public Object readValueFrom(ResultSet rs) throws SQLException {
		int idx = rs.findColumn(this.fieldName);
		return readValueFrom(rs,idx);
	}
/*		Class paramType = 
			propDesc.getWriteMethod().getParameterTypes()[0];
		Object fieldVal = rs.getObject(colidx);
		if (fieldVal!=null)
		{
			if (Number.class.isAssignableFrom(fieldVal.getClass()) && 
					(!(fieldVal.getClass()==paramType)))
			{// is a number!
				Number num = (Number) fieldVal;
				String paramClassName = paramType.getName();
				if (paramType.isPrimitive() && paramClassName.equals("short"))
				{
					fieldVal = new Short(num.shortValue());
				} else
				if (paramType.equals(Short.class))
				{
					fieldVal = new Short(num.shortValue());
				} else if (paramType.equals(Integer.class))
				{
					fieldVal = new Integer(num.intValue());
				} else if (paramType.equals(Long.class))
				{
					fieldVal = new Long(num.longValue());
				}
			}
			
		}
		return fieldVal;
	}*/
	public void writeToObj(Object obj, Object fieldVal) throws IllegalAccessException, InvocationTargetException {
		propDesc.getWriteMethod().invoke(obj,new Object[] {
		fieldVal
		});
	}
	public String constraintSql()
	{
		return null;
	}
}