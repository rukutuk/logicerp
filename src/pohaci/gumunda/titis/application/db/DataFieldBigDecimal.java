package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldBigDecimal extends DataFields {

	protected DataFieldBigDecimal(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
	}
	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return rs.getBigDecimal(colidx);
	}
	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		BigDecimal fieldVal = (BigDecimal)propDesc.getReadMethod().invoke(obj,null);
		prepStmt.setBigDecimal(paramIdx, fieldVal);
	}
}
