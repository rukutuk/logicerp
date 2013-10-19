package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldLong extends DataFields {

	public DataFieldLong(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
		fieldType="FIXED(38)";
	}

	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return new Long(rs.getLong(colidx));
	}

	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		Long fieldVal = (Long)propDesc.getReadMethod().invoke(obj,null);
		prepStmt.setLong(paramIdx, fieldVal.longValue());
	}

}
