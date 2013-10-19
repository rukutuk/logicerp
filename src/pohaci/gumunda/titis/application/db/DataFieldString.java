package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldString extends DataFields {

	protected DataFieldString(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
	}
	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return rs.getString(colidx);
	}

	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		String fieldVal = (String)propDesc.getReadMethod().invoke(obj,null);
		prepStmt.setString(paramIdx, fieldVal);
	}

}
