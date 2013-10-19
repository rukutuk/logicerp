package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.SearchReceiptDialog.SearchTable;
import pohaci.gumunda.titis.accounting.cgui.SearchSalesArReceiptDialog.SearchTableArReceive;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class SalesReceivedLoader implements ReceiverLoader{
	private GenericMapper mapper 
	= MasterMap.obtainMapperFor(SalesReceived.class);
	public SalesReceivedLoader(Connection conn) {
		mapper.setActiveConn(conn);
	}
	
	public SalesReceivedLoader(Connection conn,Class clazz) {
		mapper  = MasterMap.obtainMapperFor(clazz);
		mapper.setActiveConn(conn);
	}
	
	public Object[] find(String criterion) {
		List list = mapper.doSelectWhere(criterion);
		return list.toArray();
	}
	
	public String getCriterion(SearchTable table,String[] kolom, String operator) {
		table.stopCellEditing();
		String criteria = ""; 
			
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-MM-yyyy");
		for (int row=0;row<table.getRowCount();row++){
			String equal = "=";
			Object value = table.getValueAt(row ,1);
			if (!table.getValueAt(row,1).equals("")){	    
				if (row ==0){
					value = "";						
				}else if (row==1){
					equal = " like ";
					value = "('%" + value.toString().toUpperCase()+"%')";
				}else if(row==6) {
					if(value.toString().equals("All")){
						equal = "";
						value=" in (0,1,2,3)";
					}else if(value.toString().equals("Not Submitted")) 
						value="0";
					else if(value.toString().equals("Submitted")) {
						equal = "";
						value=" in (1,2)";
					}
					else if(value.toString().equals("Posted")) 
						value="3";					
				}else if (row==2 || row==7){					
					try {
						Date dt = dateformat2.parse(value.toString());
						value = dateformat.format(dt);
					} catch (ParseException e) {
						e.printStackTrace();
					}					
					value = "'" + value + "'";				
				}else if (row == 3){
					value = new Long (((Employee)value).getIndex());					
				}else if (row == 4){
					if (value.toString().equals("Bank"))
						value = "'BANK'";
					else if (value.toString().equals("Cash"))
						value = "'CASH'";
					else
						value ="";
				}else if (row == 5){
					value =  new Long (((Unit)value).getIndex());					
				}else {
					value ="='" + value + "'";															
				}
				if (!value.equals("")){
					if (criteria.equals(""))
						criteria += kolom[row]  +  equal + value + "";
					else
						criteria += operator + kolom[row]  +  equal + value + "";
				}
				
			}
		}		
		if (criteria.equals("")){			
			criteria = " 1=1 ";
		}
		//System.err.println(criteria);
		return criteria ;
	}

	public String getCriterion(SearchTableArReceive table, String[] kolom, String operator) {
		table.stopCellEditing();
		String criteria = ""; 
			
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-MM-yyyy");
		for (int row=0;row<table.getRowCount();row++){
			String equal = "=";
			Object value = table.getValueAt(row ,1);
			if (!table.getValueAt(row,1).equals("")){	    
				if (row ==0){
					value = "";						
				}else if (row==1){
					equal = " like ";
					value = "('%" + value.toString().toUpperCase()+"%')";
				}else if(row==6) {
					if(value.toString().equals("All")){
						equal = "";
						value=" in (0,1,2,3)";
					}else if(value.toString().equals("Not Submitted")) 
						value="0";
					else if(value.toString().equals("Submitted")) {
						equal = "";
						value=" in (1,2)";
					}
					else if(value.toString().equals("Posted")) 
						value="3";					
				}else if (row==2 || row==7){					
					try {
						Date dt = dateformat2.parse(value.toString());
						value = dateformat.format(dt);
					} catch (ParseException e) {
						e.printStackTrace();
					}					
					value = "'" + value + "'";				
				}else if (row == 3){
					value = new Long (((Employee)value).getIndex());					
				}else if (row == 4){
					if (value.toString().equals("Bank"))
						value = "'BANK'";
					else if (value.toString().equals("Cash"))
						value = "'CASH'";
					else
						value ="";
				}else if (row == 5){
					value =  new Long (((Unit)value).getIndex());					
				}else {
					value ="='" + value + "'";															
				}
				if (!value.equals("")){
					if (criteria.equals(""))
						criteria += kolom[row]  +  equal + value + "";
					else
						criteria += operator + kolom[row]  +  equal + value + "";
				}
				
			}
		}		
		if (criteria.equals("")){			
			criteria = " 1=1 ";
		}
		//System.err.println(criteria);
		return criteria ;
	}
	
}
