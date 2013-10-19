package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldSqlDate extends DataFields {

	protected DataFieldSqlDate(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
	}
	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return rs.getDate(colidx);
	}

	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		java.sql.Date fieldVal = (Date) propDesc.getReadMethod().invoke(obj,null);
		prepStmt.setDate(paramIdx, fieldVal);
	}
}
