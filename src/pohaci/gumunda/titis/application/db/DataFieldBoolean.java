package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldBoolean extends DataFields {

	protected DataFieldBoolean(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
		this.setFieldType("BOOLEAN");
	}
	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		Boolean cy = (Boolean ) propDesc.getReadMethod().invoke(obj,null);
		prepStmt.setBoolean(paramIdx,cy.booleanValue());			
	}
	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return Boolean.valueOf(rs.getBoolean(colidx));
	}

}
