package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFormattedTextField.AbstractFormatter;
import com.jgoodies.binding.formatter.EmptyNumberFormatter;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.entity.SalesAdvance;
import pohaci.gumunda.titis.accounting.logic.BeginningSalesAdvanceLogic;
import pohaci.gumunda.titis.accounting.logic.SalesAdvanceBusinessLogic;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class LookupDownPaymentPicker extends LookupPicker {
	private static final long serialVersionUID = 1L;
	private ProjectData project;	
	public LookupDownPaymentPicker(Connection conn, long sessionid) {
		super(conn, sessionid, "Lookup Down Payment List");
		
		setColumn();
		initData();
		setSize(800, 300);
	}
	
	private void setColumn() {
		getModel().addColumn("No.");
		getModel().addColumn("Project Code");
		getModel().addColumn("Customer Name");
		getModel().addColumn("Receipt No");
		getModel().addColumn("Receipt Date");
		getModel().addColumn("Currency");
		getModel().addColumn("Sales Adv Amount");
		getModel().addColumn("Received by");
		getModel().addColumn("Status");
	}

	void initData() {
		getModel().clearRows();
		
		SalesAdvanceBusinessLogic logic = new SalesAdvanceBusinessLogic(this.m_conn, this.m_sessionid);
		logic.setProjectData(this.project);
		List list = new ArrayList();
		try {
			list = logic.getOutstandingList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Iterator iterator = list.iterator();
		int i = 0;
		while(iterator.hasNext()){
			SalesAdvance sa = (SalesAdvance) iterator.next();
			
			getModel().addRow(new Object[]{
					new Integer(i++),
					sa.getProject().getCode(),
					sa.getProject().getCustomer().getName(),
					sa,
					sa.getTransactionDate(),
					sa.getSalesAdvCurr().getSymbol(),
					new Double(sa.getSalesAdvAmount()),
					sa.getEmpReceived(),
					sa.statusInString()});
		}
		
		BeginningSalesAdvanceLogic bbLogic = new BeginningSalesAdvanceLogic(this.m_conn, this.m_sessionid);
		bbLogic.setProject(this.project);
		List bbList = new ArrayList();
		bbList =  bbLogic.getOutstanding();
		
		Iterator bbIterator = bbList.iterator();
		
		while(bbIterator.hasNext()){
			BeginningAccountReceivable bar = (BeginningAccountReceivable) bbIterator.next();
			
			getModel().addRow(new Object[]{
					new Integer(i++),
					bar.getProject().getCode(),
					bar.getProject().getCustomer().getName(),
					bar,
					bar.getTrans().getTransDate(),
					bar.getCurrency().getSymbol(),
					new Double(bar.getAccValue()),
					"",
					""});
		}
	}
	
	public void refreshData(ProjectData projectData){
		this.project = projectData;
		initData();
	}
	
	public void select() {
		int rowindex = m_table.getSelectedRow();
		if(rowindex != -1) {
			setObject(getModel().getValueAt(rowindex, 3));
		}
	}
	
	public void setObject(Object object) {
		Object oldObject = m_object;
		m_object = object;
		System.out.println("i'm here");
		// semoga tidak bermasalah....
		// spekulatif...
		// yang ini aslinya
		// kalau salah dikembaliin aja...
		/*if (m_object != null)
		 m_objectTextField.setText(new BigDecimal(((SalesAdvance)m_object).getSalesAdvAmount()).toString());
		 else
		 m_objectTextField.setText("");*/
		
		AbstractFormatter formatter = createNumberFormat();
		
		if (m_object != null)
			try {
				double value = 0;
				if (m_object instanceof SalesAdvance)
					value = ((SalesAdvance)m_object).getSalesAdvAmount();
				else if (m_object instanceof BeginningAccountReceivable)
					value = ((BeginningAccountReceivable)m_object).getAccValue();
				m_objectTextField.setText(formatter.valueToString(new Double(value)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			else
				m_objectTextField.setText("");
		firePropertyChange("object",oldObject,m_object);
	}
	
	private static EmptyNumberFormatter createNumberFormat() {
		EmptyNumberFormatter formatter = new EmptyNumberFormatter(new Double(0));
		formatter.setAllowsInvalid(false);
		DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
		decformat.applyPattern("#,##0.00");
		decformat.setMaximumFractionDigits(2);
		formatter.setFormat(decformat);
		return formatter;
	}
	
	public double getAmount(){		
		if (m_object instanceof SalesAdvance)
			return ((SalesAdvance)m_object).getSalesAdvAmount();
		else if (m_object instanceof BeginningAccountReceivable)
			return ((BeginningAccountReceivable)m_object).getAccValue();
		
		return 0;
	}
}