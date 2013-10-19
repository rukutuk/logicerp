package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldFloat extends DataFields {

	protected DataFieldFloat(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
		fieldType = "FLOAT";
	}
	
	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return new Float(rs.getFloat(colidx));
	}

	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		Float fieldVal = (Float)propDesc.getReadMethod().invoke(obj,null);
		prepStmt.setFloat(paramIdx, fieldVal.floatValue());
	}

}
