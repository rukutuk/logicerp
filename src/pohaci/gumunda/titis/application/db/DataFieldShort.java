package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldShort extends DataFields {
	public DataFieldShort(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
		fieldType = "SMALLINT";
	}

	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return new Short(rs.getShort(colidx));
	}

	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		Short fieldVal = (Short)propDesc.getReadMethod().invoke(obj,null);
		prepStmt.setShort(paramIdx, fieldVal.shortValue());
	}

}
