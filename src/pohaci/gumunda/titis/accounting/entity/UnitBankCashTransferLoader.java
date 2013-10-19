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

public class UnitBankCashTransferLoader implements VoucherLoader {
	private GenericMapper mapper  ;


	public UnitBankCashTransferLoader(Connection m_conn, Class  clazz) {
		//MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
		mapper = MasterMap.obtainMapperFor(clazz);
		mapper.setActiveConn(m_conn);
	}

	public UnitBankCashTransferLoader(GenericMapper mapper) {
		this.mapper = mapper;
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
		String[] kolom = { "upper(paymentSource)"," " ,"UPPER(referenceNo)","transactiondate","empOriginator",
				"empApproved","empReceived","unit","status","submitdate"};//getValueAt(row,0)
		//boolean first=true;
		for( int row=0;row < table.getRowCount();row++){
			Object value = table. getValueAt(row,1);
			String equal = "=";
			if(row==0){
				if (!value.equals("Bank/Cash"))
					value="'"+value.toString().toUpperCase()+"'";
				else
					value ="";
			}
			if(row==1)
				value="";
			if(row==2 ){
				if(!value.toString().equals("")){
					equal = " like ";
					value = "'%"+value.toString().toUpperCase()+"%'";
				}
			}

			if(row==8) {
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
			}
			if(!value.equals("")){
				if(row==3||row==9){
					try {
						Date dt = dateformat2.parse(value.toString());
						value = dateformat.format(dt);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					value = "'" + value + "'";
				}
				if(row == 4 || row == 5 || row == 6 )
					value = new Long(((Employee)value).getIndex());
				if(row==7)
					value=new Long(((Unit)value).getIndex());


			}
			if (!value.equals("")){
				if (criteria.equals(""))
					criteria = kolom[row] + equal + value;
				else
					criteria += operator +kolom[row]+ equal + value;

			}
		}

		if(criteria.equals("")){
			criteria = " 1=1 ";
		}
		return criteria + " ORDER BY " + kolom[3] + " ASC," + kolom[2] + " ASC";
	}
	/*
	private String dateIndToSQL(String date){
		String tgl=date.substring(0,2);
		String bln=date.substring(3,5);
		String thn=date.substring(6,10);

		return "'"+thn+"-"+bln+"-"+tgl+"'";
	}*/

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.entity.VoucherLoader#getCriterion(pohaci.gumunda.titis.accounting.cgui.SearchVoucherFrame.SearchTable, java.lang.String)
	 */
	public String getCriterion(
			pohaci.gumunda.titis.accounting.cgui.SearchVoucherFrame.SearchTable table,
			String operator) {
		table.stopCellEditing();
		String criteria = "";
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-MM-yyyy");
		String[] kolom = { "upper(paymentSource)"," " ,"UPPER(referenceNo)","transactiondate","empOriginator",
				"empApproved","empReceived","unit","status","submitdate"};//getValueAt(row,0)
		//boolean first=true;
		for( int row=0;row < table.getRowCount();row++){
			Object value = table. getValueAt(row,1);
			String equal = "=";
			if(row==0){
				if (!value.equals("Bank/Cash"))
					value="'"+value.toString().toUpperCase()+"'";
				else
					value ="";
			}
			if(row==1)
				value="";
			if(row==2 ){
				if(!value.toString().equals("")){
					equal = " like ";
					value = "'%"+value.toString().toUpperCase()+"%'";
				}
			}

			if(row==8) {
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
			}
			if(!value.equals("")){
				if(row==3||row==9){
					try {
						Date dt = dateformat2.parse(value.toString());
						value = dateformat.format(dt);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					value = "'" + value + "'";
				}
				if(row == 4 || row == 5 || row == 6 )
					value = new Long(((Employee)value).getIndex());
				if(row==7)
					value=new Long(((Unit)value).getIndex());


			}
			if (!value.equals("")){
				if (criteria.equals(""))
					criteria = kolom[row] + equal + value;
				else
					criteria += operator +kolom[row]+ equal + value;

			}
		}

		if(criteria.equals("")){
			criteria = " 1=1 ";
		}
		return criteria + " ORDER BY " + kolom[3] + " ASC," + kolom[2] + " ASC";
	}

}
