package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;

import pohaci.gumunda.titis.accounting.cgui.PayrollPaychequeVerificationPanel;


public class payrollPaycheqVerivyBusinessLogic {
	PayrollPaychequeVerificationPanel m_panel;
	public Connection m_conn = null;
	public long m_sessionid = -1;
	public payrollPaycheqVerivyBusinessLogic(){		
	}

	/*private void presentingDataTable( PayrollComponent[] m_payrollComponents) {
		PayrollColumnHelper helper = new PayrollColumnHelper();
		long tempIndex=-1;
		int no=0;
		int leftMargin = 0;
		int row=-1; // i add this
		helper.prepareColumnHelper(m_payrollComponents);
		m_panel.m_data = new Vector();
		m_panel.transVector.clear();		
		m_panel.m_rows = new Vector();		
		double total = 0;
		
		m_panel.m_grandtotal = 0;
		JournalStandardSettingPickerHelper help =
			new JournalStandardSettingPickerHelper(m_conn,m_sessionid, IDBConstants.ATTR_PAYROLL_COMPONENT);
		List jsList = 
			help.getJournalStandardSettingWithAccount(IConstants.PAYROLL_VERIFICATION_PAYCHEQUES);
		JournalStandardSetting jss = (JournalStandardSetting) jsList.get(0);
		JournalStandard js = jss.getJournalStandard();
		JournalStandardAccount[] jsa = js.getJournalStandardAccount();
		
		if (m_ceksearch){
			m_empPayrollSubmit  = getEmpPaySubHasSearch(m_empPayrollSubmit,m_emps);
		}
		
		if (m_empPayrollSubmit!=null)
			for(int i=0;i<m_empPayrollSubmit.length;i++){  
				if(m_empPayrollSubmit[i]!=null){
					if(tempIndex!=m_empPayrollSubmit[i].getEmployeeIndex()){
						if (m_data.size()>0){
							m_grandtotal+=total;
							m_table.addRow(m_data);
						}
						m_data = new Vector();
						tableContent(m_empPayrollSubmit[i], no++);
						tempIndex= m_empPayrollSubmit[i].getEmployeeIndex();
						leftMargin = m_data.size();
						row++; // i add this
						total = 0;
					}
					
					int colIdx = helper.getColumnIndex(m_empPayrollSubmit[i].getPayrollComponentIndex());
					PayrollComponent payrollComponent = helper.getPayrollComponent(m_empPayrollSubmit[i].getPayrollComponentIndex());
					
					if(colIdx!=-1){
						Account accHelper = m_payrollComponents[colIdx].getAccount();
						Account acc = getStandardAccount(jsa, accHelper);					
						
							double value=m_empPayrollSubmit[i].getValue();
							value = getValueCekType(payrollComponent, value);
							
							Object content=null;
							if(value!= -1){
								if(acc!=null){
									if(acc.equals(accHelper)){
										if(acc.getBalance() == 0)
											total += value;
										else 
											total -= value;
									}
								}								
								
								PayrollComponent payComp=helper.getPayrollComponent(m_empPayrollSubmit[i].getPayrollComponentIndex());
								
								// tambahan untuk penanda row							
								if ((payComp.getPaymentAsString() == PayrollComponent.PAYCHEQUE)
										&& (payComp.getSubmitAsString() == PayrollComponent.EMPLOYEE_RECEIVABLES)){									
									HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
									try {
										PayrollCategoryComponent[] pccs = logic.getSelectedEmployeePayrollComponent(
												m_sessionid,
												IDBConstants.MODUL_MASTER_DATA,
												m_empPayrollSubmit[i].getEmployeeIndex(),
												payComp.getIndex());
										if (pccs.length > 0)
											m_rows.add(new Integer(row));
									} catch (Exception e) {
										e.printStackTrace();										
									}
								}
								content = new Double(value);									
								transactionDetail = new TransactionDetail(payComp.getAccount(),((Double)content).doubleValue(),baseCurrency,1, unit, -1);
								transVector.add(transactionDetail);
							}else{
								content = "";
							}
							if (value>0)
								content = new Double(value);
							else
								content = "";
							helper.addDataAtColumn(m_data, colIdx+ leftMargin, content);
							if (total>0){
								helper.addDataAtColumn(m_data, leftMargin + helper.getColumnCount(), new Double(total));
							}else if (total<0){
								helper.addDataAtColumn(m_data, leftMargin + helper.getColumnCount(), new Double(total));
							}else{
								helper.addDataAtColumn(m_data, leftMargin + helper.getColumnCount(), "");
							}
							
							m_empPayrollSubmit[i].setRowComponentAtTable(row);
							m_empPayrollSubmit[i].setColComponentAtTable(colIdx + leftMargin);
							
						//}
					}
				}
			}
		if (m_data.size()>0) {
			m_grandtotal+=total;
			m_data.addElement(new Double(total));
			m_table.addRow(m_data);
		}
		m_data = new Vector();
		m_data.addElement("");
		m_data.addElement("GRAND TOTAL");
		m_data.addElement("");
		loopEmptyByPayrollComponent(m_payrollComponents);
		m_data.addElement(new Double(m_grandtotal ));
		m_table.addRow(m_data);
		m_table.setModelKu();
		
	}*/
}
