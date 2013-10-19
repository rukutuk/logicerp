/**
 * 
 */
package pohaci.gumunda.titis.application.db;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import pohaci.gumunda.titis.accounting.cgui.Currency;

public class DataFieldCurrency extends DataFields
{
	
	public void prepField(int paramIdx, PreparedStatement prepStmt, Object obj) throws Exception {
		Currency cy = (Currency) propDesc.getReadMethod().invoke(obj,null);
		if (cy==null)
			prepStmt.setNull(paramIdx,Types.INTEGER);
		else
			prepStmt.setLong(paramIdx,cy.getIndex());			
	}
	
	public DataFieldCurrency(DataMapping mapping, PropertyDescriptor propDesc)
	{
		super(mapping, propDesc);
	}

	public Object readValueFrom(ResultSet rs, int colidx) throws SQLException {
		long currIdx = rs.getLong(colidx);
		return
		  mapping.hydrationProvider.hydrateByRefLookup(new Long(currIdx),Currency.class);
	}
		
	
}