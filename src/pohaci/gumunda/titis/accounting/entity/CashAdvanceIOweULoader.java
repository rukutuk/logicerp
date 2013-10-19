package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.SearchIOweYouDialog.SearchTable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class CashAdvanceIOweULoader implements IOweULoader{
    private GenericMapper mapper  ; 
    
	public CashAdvanceIOweULoader(Connection m_conn, Class  clazz) {
		mapper = MasterMap.obtainMapperFor(clazz);
		mapper.setActiveConn(m_conn);
	}
	
	public CashAdvanceIOweULoader(GenericMapper mapper) {
		this.mapper = mapper;
	}
	

	public Object[] find(String criterion) {
		List list = mapper.doSelectWhere(criterion);
		return list.toArray();
	}

	public String getCriterion(SearchTable table,String operator) {
		String  criteria = "";
		table.stopCellEditing();
		         //         0       1              2             3       4        5
		String[] kolom = { "","UPPER(REFERENCENO)" ,"TRANSACTIONDATE","PAYTO","UNIT","DEPARTMENT"};
		boolean first=true;
		
		for( int row=1;row < table.getRowCount();row++){
			Object value = table. getValueAt(row,1);
			//if(row==1)value="";
			String equal = "=";
			if(!value.equals("")){
				if(row==4) value=new Long(((Unit)value).getIndex());
				if(row==1){
					equal = " like ";
					value = "'%"+value.toString().toUpperCase()+"%'";
				}
				if(row==3) value = new Long(((Employee)value).getIndex());
				if(row==2) {
					try {
						SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-MM-yyyy");
						SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
						Date dt = dateformat2.parse(value.toString());
						value = dateformat.format(dt);
					} catch (ParseException e) {
						e.printStackTrace();
					}		
					value = "'" + value + "'";
				}
				if(row==5) 
					value=new Long(((Organization)value).getIndex());
				if(first)
					criteria += " "+kolom[row]+equal+ value;
				else
					criteria += operator +kolom[row]+equal+ value;
				first=false;
			}
		}
		if(criteria.equals("")){
			criteria=" 1=1 ";
		}
		System.out.print(criteria);
		return criteria + " ORDER BY " + kolom[2] + " ASC," + kolom[1] + " ASC";
	}
	// 01-03-2007
	// 0123456789
	

}
