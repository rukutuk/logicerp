package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import pohaci.gumunda.titis.accounting.cgui.Currency;

public class DataMapping {
	private static PropertyDescriptor[] descriptors;
	private Constructor zeroArgConstructor,multiArgConstructor;
	Class clazz;
	String tableName;
	Hashtable fields = new Hashtable();
	Hashtable unknownFields = new Hashtable();
	Class aggregatorClass;
	DataFields thePK;
	private boolean withUnderscore;
	DataFields[] multiArgs;
	HydrationProvider hydrationProvider;
	DataFields parentField;
	public Class getClazz()
	{
		return clazz;
	}
	public DataMapping()
	{
	}
	public void setHydrationProvider(HydrationProvider provider)
	{
		hydrationProvider = provider;
	}
	public DataFields field(String propName)
	{
		return (DataFields) fields.get(propName);
	}
	public DataMapping useMultiArgConstructor(String[] fieldNames)
	{
		Class[] paramTypes = new Class[fieldNames.length];
		int i;
		multiArgs = new DataFields[fieldNames.length];
		for (i=0; i<fieldNames.length; i++)
		{
			try {
			paramTypes[i] =
			field(fieldNames[i]).propDesc.getPropertyType();
			multiArgs[i] = field(fieldNames[i]);
			} catch (NullPointerException npe)
			{
				throw new RuntimeException( this.clazz.getName() + "." + fieldNames[i] + " : field not found",npe);
			}
		}
		try {
			multiArgConstructor = clazz.getConstructor(paramTypes);
			zeroArgConstructor = null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("cannot find constructor with params given",e);
		}
		return this;
	}
	public DataFields ownedByClass(Class clazz)
	{
		aggregatorClass = clazz;
		String fieldName = toDbName(stripPkg(clazz.getName()));
		DataFields f = new DataFields(this, fieldName);
		f.setFieldType("FIXED(38)"); // foreign key
		f.setNotInBean(true);
		replaceOrAddField(f);
		parentField = f;
		return f;
		//fields.put(, f);
	}
	public void setPK(String propName)
	{
		thePK =(DataFields)fields.get(propName);
		thePK.setIsPK(true);
	}
	public DataFieldAssociateToOne associate(String propName)
	{
		if (unknownFields.containsKey(propName))
		{

			DataFields f = (DataFields) unknownFields.get(propName);
			if (f.propDesc.getPropertyType().equals(Currency.class))
				throw new RuntimeException("Currency type must NOT be associated!");
			if (fields.containsKey(propName))
                System.err.println("association "+stripPkg(clazz.getName())+"." + propName + " already mapped");
			DataFieldAssociateToOne assocField = new DataFieldAssociateToOne(this, f.propDesc);
			this.replaceOrAddField(assocField);
			return assocField;
		}
        throw new RuntimeException("associated property " + propName + " not found in " + this.clazz.getName());
	}
	public DataFieldNullAssociation associateNull(String propName)
	{
		if (unknownFields.containsKey(propName))
		{
			DataFields f = (DataFields) unknownFields.get(propName);
			DataFieldNullAssociation assocField = new DataFieldNullAssociation(this,f.propDesc);
			this.replaceOrAddField(assocField);
			return assocField;
		}
		throw new RuntimeException("associated property " + propName + " not found in " + this.clazz.getName());
	}
	public static DataMapping create(Class clazz)
	{
		DataMapping mapping =new DataMapping();
		mapping.setTableName(stripPkg(clazz.getName()).toUpperCase());
		mapping.clazz = clazz;
		try {
			mapping.zeroArgConstructor = clazz.getConstructor(null);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
            if (GenericMapper.traceSql)
                System.err.println("zero arg constructor not found for " + clazz.getName());
		}
		descriptors = PropertyUtils.getPropertyDescriptors(clazz);
		for (int i=0; i< descriptors.length; i++)
		{
			DataFields f;

			f =	DataFields.createDataFields(mapping, descriptors[i]);
			if (f.getFieldType().equals("UNKNOWNTYPE"))
			{
				mapping.unknownFields.put(f.getPropName(),f);
				continue;
			}

			if (f.fieldName.equalsIgnoreCase("index"))
				f.setFieldName("AUTOINDEX");
			mapping.replaceOrAddField(f);
			if (f.fieldName.equals("AUTOINDEX"))
				mapping.setPK(f.propName);
		}
		return mapping;
	}
	public static String stripPkg(String name) {
		int lastIdx = name.lastIndexOf(".");
		if (lastIdx>=0)
			return name.substring(lastIdx+1);
		return name;
	}
	static Hashtable DbTypeMap = new Hashtable();
	static {
		DbTypeMap.put("String", "VARCHAR");
		DbTypeMap.put("Integer", "INT(38)");
		DbTypeMap.put("int", "INT");
		DbTypeMap.put("Short", "SMALLINT");
		DbTypeMap.put("short", "SMALLINT");
		DbTypeMap.put("Long", "INT(38)");
		DbTypeMap.put("long", "INT(38)");
		DbTypeMap.put("Float", "FLOAT");
		DbTypeMap.put("Date", "DATE");
		DbTypeMap.put("Double", "FIXED(38,2)");
		DbTypeMap.put("Boolean", "BOOLEAN");
	}
	static String toDbType(Class propertyType) {
		if (!DbTypeMap.containsKey( stripPkg(propertyType.getName())))
		{
			if (propertyType.equals(pohaci.gumunda.titis.accounting.cgui.Currency.class))
				return "INT(38)";
			return "UNKNOWNTYPE";
		}
		return (String) DbTypeMap.get(stripPkg(propertyType.getName()));
	}
	String toDbName(String name) {
		int i;
		StringBuffer outName = new StringBuffer();
		boolean prevIsLowerCase=false;
		for (i=0; i< name.length();i++)
		{
			char ch = name.charAt(i);
			if (Character.isUpperCase(ch) && prevIsLowerCase && withUnderscore)
			{
				outName.append('_');
			}
			outName.append(Character.toUpperCase(ch));
			prevIsLowerCase = Character.isLowerCase(ch);
		}
		return outName.toString();
	}
	public DataMapping setTableName(String s)
	{
		tableName=s;
		return this;
	}
	public void replaceOrAddField(DataFields f) {
		if (fields.containsKey(f.propName))
			fields.remove(f.propName);
		fields.put(f.propName,f);
	}

	public String insertColumnList() {
		StringBuffer strList = new StringBuffer("");
		Collection fieldList = fields.values();
		Iterator iter = fieldList.iterator();

		while (iter.hasNext())
		{
			DataFields f = (DataFields)iter.next();
			strList.append(f.fieldName);
			if (iter.hasNext())
				strList.append(",");
		}

		return strList.toString();
	}
	public String insertColumnQMarks() {
		StringBuffer strList = new StringBuffer("(");
		Collection fieldList = fields.values();
		Iterator iter = fieldList.iterator();

		while (iter.hasNext())
		{
			iter.next();
			strList.append("?");
			if (iter.hasNext())
				strList.append(",");
		}
		strList.append(")");

		return strList.toString();
	}
	public String dropTableSql() {
		return "DROP TABLE " + tableName;
	}
	public String createTableSql() {

		StringBuffer createSql = new StringBuffer("CREATE TABLE ");
		createSql.append(tableName)
					.append(" (");
		Iterator iter = fields.values().iterator();
		while (iter.hasNext())
		{
			DataFields f = (DataFields)iter.next();
			createSql.append(f.columnSql());
			if (iter.hasNext())
				createSql.append(",\n");
		}
		Iterator iter2 = fields.values().iterator();
		while (iter2.hasNext())
		{
			DataFields f = (DataFields)iter2.next();
			String constraint = f.constraintSql();
			if (constraint != null)
			{
			 createSql.append(",");
			 createSql.append(constraint+"(autoindex)");
			}
		}

		createSql.append(")");
		return createSql.toString();
	}
	public void prepUpdateFields(PreparedStatement prepStmt, Object obj)
	{
		Iterator iter = fields.values().iterator();
		int paramIdx=1;
		while (iter.hasNext())
		{
			DataFields f = (DataFields)iter.next();
			if (f.isNotInBean())
				continue;
			if (f.isPK())
				continue;
			try {
				f.prepField(paramIdx,prepStmt,obj);
			} catch (Exception e) {
				System.err.println("setting field "+ f.fieldName + " failed:");
				e.printStackTrace();
				throw new RuntimeException("setting field "+ f.fieldName + " of table "+ tableName + " failed",e);

			}
			paramIdx ++;
		}
		try {
			thePK.prepField(paramIdx,prepStmt,obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void prepareFields(PreparedStatement prepStmt, Object obj, Object[] otherInfo) {
		Iterator iter = fields.values().iterator();
		int paramIdx=1;
		int otherInfoIdx=0;

		while (iter.hasNext())
		{
			DataFields f = (DataFields)iter.next();
			try {
				if (f.isNotInBean())
				{
					prepStmt.setObject(paramIdx,otherInfo[otherInfoIdx++]);
				} else
					f.prepField(paramIdx,prepStmt,obj);
			} catch (Exception ex)
			{
				System.err.println("cannot set field " + f.fieldName + " on " + tableName);
				ex.printStackTrace();
			}
			paramIdx ++;
		}
	}
	public String createUpdateColumnList() {
		StringBuffer updateList = new StringBuffer();
		Iterator iter = fields.values().iterator();
		while (iter.hasNext())
		{
			DataFields f = (DataFields)iter.next();
			if (f.isNotInBean())
				continue;
			if (f.isPK())
				continue;
			updateList.append(f.fieldName);
			updateList.append("=?");
			if (iter.hasNext())
				updateList.append(",");
		}
		return updateList.toString();
	}
	public boolean isWithUnderscore() {
		return withUnderscore;
	}
	public void setWithUnderscore(boolean withUnderscore) {
		this.withUnderscore = withUnderscore;
	}
	public DataMapping removeField(String propName) {
		if (fields.containsKey(propName))
			fields.remove(propName);
		return this;
	}
	public Object createInstance() throws Exception {
		if (zeroArgConstructor != null)
			return zeroArgConstructor.newInstance(null);
		else
			if (multiArgConstructor != null)
			{
				//Object[] initargs;
				//multiArgConstructor.newInstance(initargs);
			}

		return null;
	}
    public void readFields(ResultSet rs, Object obj, int phase, List selfRefIdList)
    {
        Collection fieldList = fields.values();
        Iterator iter = fieldList.iterator();
        //int colidx = 1;
        while (iter.hasNext())
        {
            DataFields f = (DataFields)iter.next();
            if (!f.notInBean && (f.phase==phase))
            try {
                Object fieldVal = f.readValueFrom(rs);
                if (fieldVal!=null)
                    f.writeToObj(obj, fieldVal);
            } catch (Exception e) {
                throw new RuntimeException("fail reading field " +
                        f.fieldName + " on table " + tableName,e);
            }
            else if (!f.notInBean) // different phase
            {
                Object id;
                if (f instanceof DataFieldAssociateToOne)
                {
                    DataFieldAssociateToOne f1 = (DataFieldAssociateToOne) f;
                    if (f1.associatedClass == clazz && ((id = f1.readIdFrom(rs))!=null))
                        selfRefIdList.add(id);
                }

            }
        }
    }
	public void readFields(ResultSet rs, Object obj) {
		Collection fieldList = fields.values();
		Iterator iter = fieldList.iterator();
		//int colidx = 1;
		while (iter.hasNext())
		{
			DataFields f = (DataFields)iter.next();
			if (!f.notInBean)
			try {
				Object fieldVal = f.readValueFrom(rs);
				if (fieldVal!=null)
					f.writeToObj(obj, fieldVal);
			} catch (Exception e) {
				throw new RuntimeException("fail reading field " +
						f.fieldName + " on table " + tableName,e);
			}
		}

	}
	public Object createInstance(ResultSet rs) {
		int i;
		try {
		Object[] unhydratedArray = new Object[multiArgs.length];
		for (i=0; i<unhydratedArray.length; i++)
			unhydratedArray[i] = multiArgs[i].readValueFrom(rs);
//				rs.getObject(multiArgs[i].fieldName);
		return multiArgConstructor.newInstance(unhydratedArray);
		} catch (Exception ex)
		{
			throw new RuntimeException("failed during create object " + this.clazz.getName() + " (w/ multiargs constructor",ex);
		}
	}
}
