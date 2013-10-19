package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.SearchMemorialJournalDialog.SearchTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class MemJournalStrdLoader implements JournalStandarLoader {
	private GenericMapper mapper;
	
	public MemJournalStrdLoader(Connection m_conn,Class clazz) {
		mapper = MasterMap.obtainMapperFor(clazz);
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
		String[] kolom = {"TRANSACTIONTYPE","TRANSACTIONCODE","TRANSACTIONDATE","UPPER(REFERENCENO)","PROJECT","UNIT",
				"DEPARTMENT","STATUS","SUBMITDATE"};
		for (int row=0;row<table.getRowCount();row++){
			String equal = "=";
			Object value = table.getValueAt(row ,1);
			if (row ==0){
				value = "";
			}
			else if (row ==7){
				if(value.toString().equals("All")) {
					equal = " in ";
					value="(0,1,2,3)";
				}
				else if(value.toString().equals("Not Submitted")) value="0";
				else if(value.toString().equals("Submitted")) {
					equal = " in ";
					value="(1,2)";
				}
				else if(value.toString().equals("Posted")) value="3";
			}
			if (!value.equals("")){
				if (row ==1){					
					value = new Long(((JournalStandardSetting)value).getJournalStandard().getIndex());				
				}
				else if (row ==3){
					equal = " like ";
					value = " UPPER('%" + value + "%')";	    				  
				}
				else if (row == 4){
					value = new Long (((ProjectData)value).getIndex());
				}else if (row == 5){
					value =  new Long (((Unit)value).getIndex());
				}else if (row == 6){
					value = new Long (((Organization)value).getIndex());
				}else if (row ==8 || row ==2){
					try {
						Date dt = dateformat2.parse(value.toString());
						value = dateformat.format(dt);
					} catch (ParseException e) {
						e.printStackTrace();
					}					
					value = "'" + value + "'";		
				}
			}
			
			if (!value.equals("")){
				if (criteria.equals(""))
					criteria += kolom[row]  +  equal + value;
				else
					criteria += operator + kolom[row]  +  equal + value;
				
			}
		}		
		if (criteria.equals("")){
			criteria = " 1=1 ";
		}
		System.err.println(criteria);
		return criteria;
	}
	
}
