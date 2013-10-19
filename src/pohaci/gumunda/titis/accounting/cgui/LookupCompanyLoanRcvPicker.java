package pohaci.gumunda.titis.accounting.cgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;

import pohaci.gumunda.titis.accounting.entity.RcvLoan;
import pohaci.gumunda.titis.application.DoubleFormatted;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class LookupCompanyLoanRcvPicker extends LookupPicker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RcvLoan rcvloan ;
	private CompanyLoan companyLoan;
	private GenericMapper m_entityMapper;
	private List rcvloanList;
	private double accumulatedAmount;
	//private RcvLoan rcvFilter =new RcvLoan();
	private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");

	public LookupCompanyLoanRcvPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Company Loan Receive Detail");
		m_entityMapper = MasterMap.obtainMapperFor(RcvLoan.class);
		m_entityMapper.setActiveConn(m_conn);
		
		
		remove(m_browseBt);
		m_browseBt=new JButton("Detail");
		add(m_browseBt);
		m_browseBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				done1();
			}
		});
		initColumnHeader();
		initData();
		setSize(800, 300);
	}
	
	void initData() {
		rcvloanList=m_entityMapper.doSelectWhere("1=1");
		accumulatedAmount=0;
		int no=1;
		getModel().clearRows();
		for(int i = 0; i < rcvloanList.size();  i ++) {
			rcvloan=(RcvLoan) rcvloanList.get(i);
			if(companyLoan!=null){
				boolean loann=companyLoan.getIndex()==rcvloan.getCompanyLoan().getIndex();
				//boolean isFiltered=rcvloan.getIndex()==rcvFilter.getIndex();
				//if(loann && !isFiltered){
					if(loann ){
					getModel().addRow(new Object[]{
							new Integer(no++),
							companyLoan.getCreditorList().toString(),
							companyLoan.getName(),
							rcvloan.getReferenceNo(),
							dateformat.format(rcvloan.getDateReceived()), 
							new DoubleFormatted(rcvloan.getAmount()),
							rcvloan.getEmpReceived(), 
							StateTemplateEntity.status2String(rcvloan.getStatus())
					});
					if(rcvloan.getStatus()==StateTemplateEntity.State.POSTED){
						accumulatedAmount=accumulatedAmount+rcvloan.getAmount();
					}
				}else{
					System.out.println();
				}
			}else{
				accumulatedAmount=0;
			}
		}
	}
	
	public void setRcvFilter(RcvLoan rcvFilter){
		//this.rcvFilter=rcvFilter;
		initData();
	}
	/*
	 * seandainya pemakaian setRcvFilter tidak memungkinkan
	 
	public void setRcvFilterIndex(long ind){
		rcvFilter.setIndex(ind);
		initData();
	}*/
	
	private void initColumnHeader() {
		getModel().addColumn("No.");
		getModel().addColumn("CreditorCode");
		getModel().addColumn("Loan Code");
		getModel().addColumn("Receipt No");
		getModel().addColumn("Receipt Date");
		getModel().addColumn("Receipt Amount");
		getModel().addColumn("Received by");
		getModel().addColumn("Status");
	}
	public double getAccumulatedAmount(){
		return accumulatedAmount;
	}
	
	public void setCompanyLoan(CompanyLoan companyLoan){
		this.companyLoan=companyLoan;
		initData();
	}
	
	public void select() {
		int rowindex = m_table.getSelectedRow();
		if(rowindex != -1) {
			setObject(getModel().getValueAt(rowindex, 1));
		}
	}
	
}

