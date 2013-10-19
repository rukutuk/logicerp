package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.SearchVoucherDialog.SearchTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;


public class PurchaseApPmtLoader implements VoucherLoader {
	private GenericMapper mapper  = 
		MasterMap.obtainMapperFor(PurchaseApPmt.class);
	
	public PurchaseApPmtLoader(Connection m_conn) {
		mapper.setActiveConn(m_conn);
	}
	
	public Object[] find(String criterion) {
		List list = mapper.doSelectWhere(criterion);
		return list.toArray();
	}
	
	public String getCriterion(SearchTable table,String operator) {
		table.stopCellEditing();
		String criteria = "";	      
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-MM-yyyy");
		String[] kolom = {"UPPER(PAYMENTSOURCE)","VOUCHERTYPE","UPPER(REFERENCENO)","TRANSACTIONDATE","EMPORIGINATOR","EMPAPPROVED",
				"EMPRECEIVED","UNIT","STATUS","SUBMITDATE"};	      
		for (int row=0;row<table.getRowCount();row++){
			String equal = "=";
			Object value = table.getValueAt(row ,1);	    
			if (row==0){
				if (value.toString().equals("Bank")) 
					value ="'BANK'";
				else if (value.toString().equals("Cash")) 
					value ="'CASH'";
				else 
					value = "";								
			}else if (row==1){
				value ="";			
			}else if (row==8){
				if(value.toString().equals("All")) {
					equal = " in ";
					value="(0,1,2,3)";
				}		
				else if(value.toString().equals("Not Submitted")) value="0";
				else if(value.toString().equals("Submitted")) {
					equal = " in ";
					value="(1,2)";
				}
				else if(value.toString().equals("Posted")) 
					value="3";		 				
			}
			if (!value.equals("")){
				if (row ==2){
					equal = " like ";
					value = "UPPER('%" + value + "%')";
				}
				else if (row==3 || row ==9)	{		
					try {
						Date dt = dateformat2.parse(value.toString());
						value = dateformat.format(dt);
					} catch (ParseException e) {
						e.printStackTrace();
					}					
					value = "'" + value + "'";	
				}
				else if (row==4 || row==5 || row==6)					
					value =  new Long (((Employee)value).getIndex());				
				else if (row ==7)					
					value = new Long (((Unit)value).getIndex());	
				
			}
			if (!value.equals("")){
				if (criteria.equals(""))
					criteria += kolom[row] +  equal + value;
				else
					criteria += operator + kolom[row]  +  equal + value;
			}
		} 
		
		if (criteria.equals("")){
			criteria = " 1=1 ";
		}
		return criteria + " order by " + kolom[3] + " ASC," + kolom[2] + " ASC";
	}
	
}
