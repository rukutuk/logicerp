package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.titis.accounting.cgui.SearchInvoiceDlg;
import pohaci.gumunda.titis.accounting.entity.InvoiceLoader;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.report.ProjectMonitoringInvoiceJasper;

public class InvoicePanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	InvoiceLoader m_loader = null;
	JButton m_excellBt, m_filterBt, m_refreshBt;
	InvoiceTable m_table;
	DecimalFormat m_desimalFormat;
	SimpleDateFormat m_dateFormat;
	SimpleDateFormat m_dateFormat2;
	ProjectData m_project = null;
	boolean m_show = false;
	
	
  public InvoicePanel(Connection conn, long sessionid) {
	m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    setEditable(false);
    m_desimalFormat =  new DecimalFormat("#,##0.00");
	m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
  }

  void constructComponent() {
    m_table = new InvoiceTable();
    m_excellBt = new JButton(new ImageIcon("../images/excell.gif"));
    m_excellBt.addActionListener(this);
    m_filterBt = new JButton(new ImageIcon("../images/filter2.gif"));
    m_filterBt.addActionListener(this);
    m_refreshBt = new JButton(new ImageIcon("../images/refresh.gif"));
    m_refreshBt.addActionListener(this);

    JToolBar northbar = new JToolBar();
//    JPanel buttonPanel = new JPanel();

    northbar.setFloatable(false);
    northbar.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    northbar.add(m_excellBt, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northbar.add(m_filterBt, gridBagConstraints);

    gridBagConstraints.gridx = 2;
    northbar.add(m_refreshBt, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northbar.add(new JPanel(), gridBagConstraints);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    add(northbar, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }
  
  public void setProjectData (ProjectData project){
	  m_project = project;
  }
  
  public void setEditable(boolean editable) {
	    m_excellBt.setEnabled(editable);
	    m_filterBt.setEnabled(editable);
	    m_refreshBt.setEnabled(editable);
  }
  
  public void actionPerformed(ActionEvent e){
    if (e.getSource() == m_filterBt){
    	if(!m_show){
    		m_show = true;
    		SearchInvoiceDlg invoice = new SearchInvoiceDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
    			"Search Invoice", m_conn, m_sessionid, m_table, m_project);
    		invoice.setVisible(true);
    		m_show = false;
    	}
    }
    else if(e.getSource()== m_excellBt){
  		new ProjectMonitoringInvoiceJasper(m_conn, m_project, true);
  	}
  	else refresh();
  }
  
  public void setInvoice(ProjectData project){
	  	GenericMapper mapper = MasterMap.obtainMapperFor(SalesInvoice.class);
		mapper.setActiveConn(m_conn);
		
		//String query = "customer = (select customer  from projectdata where code = '"+ project.getCode() +"')";
		String query = "project = " + project.getIndex();
		/*String query = IDBConstants.ATTR_CUSTOMER+ " = (select "+ IDBConstants.ATTR_CUSTOMER +"  from " +
		IDBConstants.TABLE_PROJECT_DATA+ " where " + IDBConstants.ATTR_CODE + " = ' " + project.getCode() +" ')";
		*/
		System.err.println(query);
		List list = mapper.doSelectWhere(query);
		
		InvoiceTableModel model = (InvoiceTableModel) m_table.getModel();
		model.setRowCount(0);
		
		Iterator iter = list.iterator();
		int i=1;
		double totamount = 0;
		double totppn = 0;
		String curr = "";
		while(iter.hasNext()){
			SalesInvoice inv = (SalesInvoice) iter.next();
			model.addRow(new Object[]{String.valueOf(i++),inv.getReferenceNo(),m_dateFormat.format(inv.getTransactionDate()),
				inv.getCustomer().getName(),inv.getCustomer().getAddress(),inv.getDescription(), 
				inv.getSalesCurr().getSymbol()+ " " + m_desimalFormat.format(inv.getSalesAmount()),
				inv.getVatCurr().getSymbol() + " " + m_desimalFormat.format(inv.getVatAmount()), 
				getStatus(inv.getStatus())});
			totamount +=inv.getSalesAmount();
			totppn +=inv.getVatAmount();
			curr = inv.getVatCurr().getSymbol();
		}
		if (model.getRowCount()>0){
			model.addRow(new Object[]{"","","","","","","","",""});
			model.addRow(new Object[]{"","TOTAL","","","","",curr + " " + m_desimalFormat.format(totamount),
					curr + " " + m_desimalFormat.format(totppn),""});
		}
  }
  
  private String getStatus(long status){
	  String  strStatus = "";
	  if (status==0)
		  strStatus = "Not Submitted";
	  else if (status==1 || status==2)
		  strStatus = "Submitted";
	  else 
		  strStatus = "Posted";
	  return strStatus;
  }
  
  void refresh() { 
	  setInvoice(m_project); 
  }
  
  /**
   *
   */
  public class InvoiceTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvoiceTable() {
      InvoiceTableModel model = new InvoiceTableModel();
      model.addColumn("No.");
      model.addColumn("Invoice No.");
      model.addColumn("Invoice Date");
      model.addColumn("Customer");
      model.addColumn("Invoicing Address");
      model.addColumn("Description");
      model.addColumn("Total Amount");
      model.addColumn("PPN");
      model.addColumn("Status");
      setModel(model);
    }
	
	public TableCellRenderer getCellRenderer(int row, int column) {
		if(column>=0){
			if(isSummaryRow(row)){
				if(column==1)
					return new FormattedStandardCellRenderer(Font.BOLD, JLabel.RIGHT);
				if(column==6 || column==7)
					return new FormattedDoubleCellRenderer("###,##0.00;(###,##0.00)", JLabel.RIGHT, Font.BOLD);
			}else{
				if (column==6 || column==7)
					return new FormattedDoubleCellRenderer("###,##0.00;(###,##0.00)", JLabel.RIGHT, Font.PLAIN);
			}
		}
		return new FormattedStandardCellRenderer(Font.PLAIN, JLabel.LEFT);
	}
	
	private boolean isSummaryRow(int row) {
		int maxRow = getRowCount()-1;
		return (row==maxRow);
	}

  }

  /**
   *
   */
  class InvoiceTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
  
}