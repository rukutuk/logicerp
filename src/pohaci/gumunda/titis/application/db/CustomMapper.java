package pohaci.gumunda.titis.application.db;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public abstract class CustomMapper extends GenericMapper {

	public CustomMapper(Class clazz) {
		super((DataMapping)null);
	}

	public void doDelete(Object obj) {
		throw new UnsupportedOperationException("custommapper cant delete");
	}

	public void doInsert(Object obj, Object[] otherInfo) {
		throw new UnsupportedOperationException("custommapper cant insert");
		}

	public void doInsert(Object obj) {
		throw new UnsupportedOperationException("custommapper cant delete");
	}

	abstract Object[] implSelectAll();
	abstract Object implSelectById(long idx);
	public List doSelectAll() {
		Object[] arr = implSelectAll();
		return Arrays.asList(arr);
	}

	public Object doSelectByIndex(Object id) {
		Number idN = (Number)id;
		return implSelectById(idN.longValue());
	}

	public List doSelectWhere(String clause, Object[] params) {
		throw new UnsupportedOperationException("custommapper cant insert");
	}

	public boolean doUpdate(Object obj) {
		throw new UnsupportedOperationException("custommapper cant insert");
	}
	public static void install()
	{
		//MasterMap.setMapper(new ProjectDataMapper(),ProjectData.class);
	}
}

class ProjectDataMapper extends CustomMapper
{
	ProjectBusinessLogic logic;
	public ProjectDataMapper() {
		super(null);
		createLogic();
	}

	private void createLogic() {
		if (logic!=null)
			return;
		Connection conn = MasterMap.obtainLookupConnection();
		if (conn!=null)
			logic = new ProjectBusinessLogic(null);
	}

	Object[] implSelectAll() {
		createLogic();
		try {
			return logic.getAllProjectData(0,IDBConstants.MODUL_MASTER_DATA);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
	Object implSelectById(long idx) {
		createLogic();
		String q=
		"SELECT * FROM (" +
	  	"SELECT pd." + IDBConstants.ATTR_AUTOINDEX +
  		", pd." + IDBConstants.ATTR_CODE +
  		", cust." + IDBConstants.ATTR_NAME + " AS CUSTOMERNAME" +
  		", pd." + IDBConstants.ATTR_WORK_DESCRIPTION +
  		", act." + IDBConstants.ATTR_NAME + " AS ACTIVITYNAME" +
  		", pd." + IDBConstants.ATTR_ORNO +
  		", pd." + IDBConstants.ATTR_ORDATE +
  		", pd." + IDBConstants.ATTR_PONO  +
  		", pd." + IDBConstants.ATTR_PODATE +
  		", pd." + IDBConstants.ATTR_IPCNO +
  		", pd." + IDBConstants.ATTR_IPCDATE +
  		", pc." + IDBConstants.ATTR_ACTUAL_START_DATE +
  		", pc." + IDBConstants.ATTR_ACTUAL_END_DATE +
  		", pc." + IDBConstants.ATTR_VALIDATION +
  		", pd." + IDBConstants.ATTR_REGDATE +
  		", pd." + IDBConstants.ATTR_CUSTOMER +
  		", pd." + IDBConstants.ATTR_UNIT +
  		", pd." + IDBConstants.ATTR_FILE +
  		", pd." + IDBConstants.ATTR_SHEET +
  		", pd." + IDBConstants.ATTR_ACTIVITY +
  		", pd." + IDBConstants.ATTR_DEPARTMENT + " ";

  String querytable = "FROM " + IDBConstants.TABLE_PROJECT_DATA + " pd " +
  		"LEFT JOIN " + IDBConstants.TABLE_CUSTOMER + " cust " +
  		"ON pd." + IDBConstants.ATTR_CUSTOMER + "=cust." + IDBConstants.ATTR_AUTOINDEX + " " +
  		"LEFT JOIN " + IDBConstants.TABLE_ACTIVITY + " act " +
  		"ON pd." + IDBConstants.ATTR_ACTIVITY + "=act." + IDBConstants.ATTR_AUTOINDEX + " " +
  		"LEFT JOIN " + IDBConstants.TABLE_PROJECT_CONTRACT + " pc " +
  		"ON pd." + IDBConstants.ATTR_AUTOINDEX + "=pc." + IDBConstants.ATTR_PROJECT + ")";
  String qry = q + querytable;
		try {
			return logic.getProjectDataByCriteria(0,IDBConstants.MODUL_MASTER_DATA,
					qry + " WHERE autoindex = " + idx)[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}