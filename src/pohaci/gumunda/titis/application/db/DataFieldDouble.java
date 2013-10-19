package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldDouble extends DataFields {

	protected DataFieldDouble(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
		this.setFieldType("FIXED(38,2)");
	}
	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		double d = rs.getDouble(colidx);
		return new Double(d);
	}

	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		Double fieldVal = (Double)propDesc.getReadMethod().invoke(obj,null);
		prepStmt.setDouble(paramIdx, fieldVal.doubleValue());
	}

	
}
