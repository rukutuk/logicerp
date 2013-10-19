package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFieldNullAssociation extends DataFields {

	protected DataFieldNullAssociation(DataMapping mapping, PropertyDescriptor propDesc) {
		super(mapping, propDesc);
		
	}

	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		prepStmt.setObject(paramIdx,null);
	}

	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		return null;
	}

    
}
