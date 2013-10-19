package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldUtilDate extends DataFields {

	protected DataFieldUtilDate(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
	}
	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return (java.util.Date)rs.getDate(colidx);
	}

	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		java.util.Date fieldVal = (java.util.Date) propDesc.getReadMethod().invoke(obj,null);
		java.sql.Date sqlDate = (fieldVal == null) ? null : new java.sql.Date(fieldVal.getTime());
		prepStmt.setDate(paramIdx, sqlDate);
	}

}
