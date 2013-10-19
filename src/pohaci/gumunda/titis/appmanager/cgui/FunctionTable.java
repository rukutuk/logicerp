package pohaci.gumunda.titis.appmanager.cgui;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.appmanager.entity.RoleMap;

public class FunctionTable extends JTable {

	private static final long serialVersionUID = 1L;
	private String application = "";
	private DefaultTableModel tableModel;
	
	public FunctionTable(String application) {
		this.application = application;
		
		setModel(getTableModel());
		
		getColumnModel().getColumn(0).setPreferredWidth(30);
		getColumnModel().getColumn(0).setMaxWidth(30);
		getColumnModel().getColumn(0).setMinWidth(30);
		
		refreshFunctionList();
	}

	private void refreshFunctionList() {
		getDefaultFunctionList();
	}

	private void getDefaultFunctionList() {
		//Boolean falseBool = new Boolean(false);
		if(application==RoleMap.APP_ACCOUNTING){
			/*Object[] objects = new Object[]{
				new Object[]{falseBool, ""}
			};*/
		}else if(application==RoleMap.APP_HRM){
			
		}else if(application==RoleMap.APP_PROJECT){
			
		}
	}

	private DefaultTableModel getTableModel() {
		if(tableModel==null){
			tableModel = new DefaultTableModel();
			tableModel.addColumn("");
			tableModel.addColumn("Function");
		}
		return tableModel;
	}
}
