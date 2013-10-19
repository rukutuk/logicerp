package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldInt extends DataFields {

	public DataFieldInt(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping,propDesc);
		fieldType = "FIXED(38)";
	}
	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return new Integer(rs.getInt(colidx));
	}

	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		Integer fieldVal = (Integer)propDesc.getReadMethod().invoke(obj,null);
		prepStmt.setInt(paramIdx, fieldVal.intValue());
	}

}
