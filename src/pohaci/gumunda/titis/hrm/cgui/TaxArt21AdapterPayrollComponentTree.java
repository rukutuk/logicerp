package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Connection;

//import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.application.ObjectCellEditor;

public class TaxArt21AdapterPayrollComponentTree extends ObjectCellEditor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection conn;
	private long sessionid;
//	private JFrame owner;
	private PayrollComponent m_payComp;

	public TaxArt21AdapterPayrollComponentTree(JFrame owner, Connection conn, long sessionid) {
		super(owner, conn, sessionid);
//		this.owner=owner;
		this.conn=conn;
		this.sessionid=sessionid;
	}
	public void setObject(Object obj)
	{
		super.setObject(obj);
		if (obj instanceof PayrollComponent) {
			PayrollComponent payComp = (PayrollComponent) obj;
			m_model.setValueAt(payComp.getAccount().getCode(), m_row, 1);
			m_model.setValueAt(payComp.getDescription(), m_row, 0);
		}
		
	}
	public void done() {
		TaxArt21PayrollComponentTreePanelPlus payCompTree=new TaxArt21PayrollComponentTreePanelPlus(GumundaMainFrame.getMainFrame(),conn,sessionid);
		payCompTree.setVisible(true);
		if(payCompTree.getResponse()== JOptionPane.OK_OPTION){
			m_payComp=payCompTree.getPayrollComponent();
			setObject(m_payComp);
		}
	}
	public PayrollComponent getPayrollComponent(){
		return m_payComp;
	}

}
