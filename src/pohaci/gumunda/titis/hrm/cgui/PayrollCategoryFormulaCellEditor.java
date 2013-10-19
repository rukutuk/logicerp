package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;
import javax.swing.*;
import pohaci.gumunda.titis.application.*;

public class PayrollCategoryFormulaCellEditor extends ObjectCellEditor {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public PayrollCategoryFormulaCellEditor(JFrame owner, Connection conn, long sessionid) {
    super(owner, conn, sessionid);
  }

public void done() {
	FormulaEntity formulaEntity = null;
	if(getObject() instanceof FormulaEntity)
		formulaEntity = (FormulaEntity)getObject();
	PayrollCategoryFormulaDlg dlg = new PayrollCategoryFormulaDlg(m_owner, m_conn, m_sessionid, formulaEntity,true);
	dlg.setVisible(true);
	if(dlg.getResponse() == JOptionPane.OK_OPTION) {
		setObject(dlg.getFormulaEntity());
	}
	else
		setObject(getObject());
}
}