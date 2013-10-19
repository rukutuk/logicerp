package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.SearchExpenseSheetDialog.SearchTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class ExpenseSheetLoader implements SheetLoader {
	private GenericMapper mapper  = 
		MasterMap.obtainMapperFor(ExpenseSheet.class);
	
	public ExpenseSheetLoader(Connection m_conn) {
		mapper.setActiveConn(m_conn);
	}
	public Object[] find(String criterion) {
		List list = mapper.doSelectWhere(criterion);
		return list.toArray();
	}

	public String getCriterion(SearchTable table, String operator) {
		table.stopCellEditing();		
		String criteria = "";	    
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-MM-yyyy");
		String[] kolom = {"EXPENSETYPE","UPPER(REFERENCENO)","TRANSACTIONDATE","PROJECT","EMPORIGINATOR","EMPAPPROVED",
				"STATUS","SUBMITDATE"};	      
		for (int row=1;row<table.getRowCount();row++){
			String equal ="=";
			Object value = table.getValueAt(row ,1);
			if (!table.getValueAt(row,1).equals("")){
				if (row == 2 || row ==7) {
					try {
						Date dt = dateformat2.parse(value.toString());
						value = dateformat.format(dt);
					} catch (ParseException e) {
						e.printStackTrace();
					}		
					value = "'" + value + "'";	    		  
				}else if (row == 3){
					value = new Long (((ProjectData)value).getIndex());
				}else if (row == 4 || row ==5){
					value = new Long (((Employee)value).getIndex());	    			  
				}else if (row == 6){
					if(value.toString().equals("All")){ 
						equal = " in ";
						value=" (0,1,2,3)";				
					}else if(value.toString().equals("Not Submitted")) 
						value="0";
					else if(value.toString().equals("Submitted")) {
						equal = " in ";
						value=" (1,2)";
					}
					else if(value.toString().equals("Posted")) 
						value="3";		
				}else if (row==1){	 
					equal = " like ";
					value = "UPPER('%" + value + "%')";	    			  
				}	    		  
			}
			if (!value.equals("")){
				if (criteria.equals(""))
					criteria = kolom[row] + equal + value;
				else
					criteria += operator +kolom[row]+ equal + value;
				
			}
		}	     
		System.err.println(criteria);
		if (criteria.equals("")){
			criteria = " 1=1 ";
		}
		return criteria;
	}	
}
