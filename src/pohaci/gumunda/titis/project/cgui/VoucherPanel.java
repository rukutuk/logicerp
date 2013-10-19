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
import java.awt.Insets;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.titis.accounting.cgui.SearchVoucherDlg;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCost;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCostDetail;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.entity.VoucherLoader;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.report.ProjectMonitoringVoucherJasper;

public class VoucherPanel extends JPanel implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JButton m_excellBt, m_filterBt, m_refreshBt;
	VoucherTable m_table;
	VoucherTableModel m_model;
	ProjectData m_project;
	Connection m_conn = null;
	long m_sessionid = -1 ;
	boolean m_show = false;
	DecimalFormat m_decimalFormat;
	SimpleDateFormat m_dateFormat;
	SimpleDateFormat m_dateFormat2;

	VoucherLoader m_loader;
	private JComboBox statusCombo;

  public VoucherPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    setEditable(false);
    m_decimalFormat =  new DecimalFormat("#,##0.00");
	m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	m_loader = null;
  }

  void constructComponent() {
    m_table = new VoucherTable();
    //m_model = new VoucherTableModel();
    m_excellBt = new JButton(new ImageIcon("../images/excell.gif"));
    m_excellBt.addActionListener(this);
    m_filterBt = new JButton(new ImageIcon("../images/filter2.gif"));
    m_filterBt.addActionListener(this);
    m_refreshBt = new JButton(new ImageIcon("../images/refresh.gif"));
    m_refreshBt.addActionListener(this);

    JToolBar northbar = new JToolBar();
    //JPanel buttonPanel = new JPanel();

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
    gridBagConstraints.insets = new Insets(0, 5, 0, 3);
    northbar.add(new JLabel("Status"), gridBagConstraints);

    statusCombo = new JComboBox(new String[] {"Not Sumbitted", "Submitted", "Posted", "All"});
    statusCombo.setSelectedItem("Posted");
    gridBagConstraints.gridx = 4;
    northbar.add(statusCombo, gridBagConstraints);


    gridBagConstraints.gridx = 5;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northbar.add(new JPanel(), gridBagConstraints);


    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    add(northbar, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
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
    		SearchVoucherDlg voucher = new SearchVoucherDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        			"Search Voucher", m_conn, m_sessionid, m_table, m_project);
    		voucher.setVisible(true);
    		m_show = false;
    	}
    }
    else if(e.getSource() == m_excellBt){
    	new ProjectMonitoringVoucherJasper(m_conn, m_model, true);
    }
    else refresh();
  }

  public void setProjectData(ProjectData project){
	  m_project = project;
  }

  public void setVoucher(ProjectData project){

	  	GenericMapper mapper = MasterMap.obtainMapperFor(PmtCAProject.class);
	  	mapper.setActiveConn(m_conn);

	  	int status = statusCombo.getSelectedIndex();
	  	if (status > 0)
	  		status += 1;

	  	String statusClausa = "";
	  	if (status < 4)
	  		statusClausa = " AND " + IDBConstants.ATTR_STATUS + "=" + status;

	  	//String strSelect = IDBConstants.ATTR_PROJECT + " = (select " + IDBConstants.ATTR_AUTOINDEX +
	  	//	" from " + " projectdata where " + IDBConstants.ATTR_CODE + " = '" + project.getCode() + "')";

	  	String strSelect = IDBConstants.ATTR_PROJECT + "=" + project.getIndex() + statusClausa;

	  	VoucherTableModel model = (VoucherTableModel) m_table.getModel();
	  	model.setRowCount(0);

	  	System.err.println(strSelect);
	  	List list = mapper.doSelectWhere(strSelect);

	  	//PmtCAProject
	  	Iterator iter = list.iterator();
	  	int i=1;
	  	double total = 0;
	  	//String curr = "";
	  	while(iter.hasNext()){
	  		PmtCAProject voucher = (PmtCAProject) iter.next();
	  		String payto = "";
	  		if (voucher.getPayTo()!=null)
	  			payto = voucher.getPayTo().getFirstName() + " " + voucher.getPayTo().getMidleName() + " " + voucher.getPayTo().getLastName();

	  		model.addRow(new Object[] {String.valueOf(i++), voucher.getReferenceNo(), m_dateFormat.format(voucher.getTransactionDate()),
	  				payto,voucher.getDescription(), getStatus(voucher.getStatus()),
	  				voucher.getEmpApproved().igetFullName(), m_dateFormat.format(voucher.getDateApproved()),
	  				voucher.getCurrency().getSymbol()+ " " + m_decimalFormat.format(voucher.getAmount()),
	  				m_decimalFormat.format(voucher.getAmount()*voucher.getExchangeRate())});
	  		//curr = voucher.getCurrency().getSymbol();
	  		total+=(voucher.getAmount()*voucher.getExchangeRate());
	  	}

	  	String str = "" ;

	  	//PmtCAIOUProject
	  	mapper = MasterMap.obtainMapperFor(PmtCAIOUProject.class);
	  	mapper.setActiveConn(m_conn);
	  	list = mapper.doSelectWhere(strSelect);
	  	System.err.println(strSelect);
	  	iter = list.iterator();
	  	while(iter.hasNext()){
	  		PmtCAIOUProject voucher = (PmtCAIOUProject) iter.next();
	  		String payto = "";
	  		if (voucher.getPayTo()!=null)
	  			payto = voucher.getPayTo().getFirstName() + " " + voucher.getPayTo().getMidleName() + " " + voucher.getPayTo().getLastName();
	  		model.addRow(new Object[]{String.valueOf(i++), voucher.getReferenceNo(), m_dateFormat.format(voucher.getTransactionDate()),
	  				payto,voucher.getDescription(), getStatus(voucher.getStatus()), voucher.getEmpApproved().igetFullName(), m_dateFormat.format(voucher.getDateApproved()),
	  				voucher.getCurrency().getSymbol()+ " " + m_decimalFormat.format(voucher.getAmount()),
	  				m_decimalFormat.format(voucher.getAmount())});
	  		//curr = voucher.getCurrency().getSymbol();
	  		total+=voucher.getAmount();
	  		//PmtCAIOUProjectInstall
		  	GenericMapper tmpMapper = MasterMap.obtainMapperFor(PmtCAIOUProjectInstall.class);
		  	tmpMapper.setActiveConn(m_conn);
		  	str = IDBConstants.ATTR_PMT_CA_IOU_PROJECT + " = " + voucher.getIndex();
		  	System.err.println(str);
		  	List tmpList = tmpMapper.doSelectWhere(str);
		  	Iterator tmpIter = tmpList.iterator();
		  	while(tmpIter.hasNext()){
		  		PmtCAIOUProjectInstall install = (PmtCAIOUProjectInstall) tmpIter.next();
		  		String empApp = "";
		  		if(!install.getEmpApproved().equals(null))
		  			empApp = install.getEmpApproved().igetFullName();
		  		model.addRow(new Object[]{String.valueOf(i++), install.getReferenceNo(),
		  			m_dateFormat.format(install.getTransactionDate()),payto,install.getDescription(),
		  			getStatus(install.getStatus()), empApp,
		  			m_dateFormat.format(install.getDateApproved()),
		  			install.getCurrency().getSymbol()+ " " + m_decimalFormat.format(install.getAmount()),
		  			m_decimalFormat.format(install.getAmount())});
		  		//curr = install.getCurrency().getSymbol();
		  		total+=install.getAmount();
		  	}

		  	//PmtCAIOUProjectReceive
		  	GenericMapper tmpMapper1 = MasterMap.obtainMapperFor(PmtCAIOUProjectReceive.class);
		  	tmpMapper1.setActiveConn(m_conn);
		  	//str = IDBConstants.ATTR_AUTOINDEX + " = ( select " + IDBConstants.ATTR_AUTOINDEX + " from " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT + " where " + strSelect + ")" ;
		  	System.err.println(str);
		  	List tmpList1 = tmpMapper1.doSelectWhere(str);
		  	Iterator tmpIter1 = tmpList1.iterator();
		  	while(tmpIter1.hasNext()){
		  		PmtCAIOUProjectReceive receive = (PmtCAIOUProjectReceive) tmpIter1.next();
		  		model.addRow(new Object[]{String.valueOf(i++), receive.getReferenceNo(),
		  			m_dateFormat.format(receive.getTransactionDate()), payto,receive.getDescription(),
		  			getStatus(receive.getStatus()), receive.getEmpApproved().igetFullName(),
		  			m_dateFormat.format(receive.getDateApproved()),
		  			receive.getCurrency().getSymbol()+ " " + m_decimalFormat.format(receive.getAmount()),
		  			m_decimalFormat.format(receive.getAmount())});
		  		//curr = receive.getCurrency().getSymbol();
		  		total+=receive.getAmount();
		  	}

		  	//PmtCAIOUProjectSettled
		  	GenericMapper tmpMapper2 = MasterMap.obtainMapperFor(PmtCAIOUProjectSettled.class);
		  	tmpMapper2.setActiveConn(m_conn);
		  	//str = IDBConstants.ATTR_AUTOINDEX + " = ( select " + IDBConstants.ATTR_AUTOINDEX + " from " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT + " where " + strSelect + ")" ;
		  	System.err.println(str);
		  	List tmpList2 = tmpMapper2.doSelectWhere(str);
		  	Iterator tmpIter2 = tmpList2.iterator();
		  	while(tmpIter2.hasNext()){
		  		PmtCAIOUProjectSettled settle = (PmtCAIOUProjectSettled) tmpIter2.next();
		  		String empApp = "";
		  		if(!settle.getEmpApproved().equals(null))
		  			empApp = settle.getEmpApproved().igetFullName();
		  		model.addRow(new Object[]{String.valueOf(i++), settle.getReferenceNo(),
		  			m_dateFormat.format(settle.getTransactionDate()),payto,
		  			settle.getDescription(),getStatus(settle.getStatus()), empApp,
		  			m_dateFormat.format(settle.getDateApproved()),
		  			settle.getCurrency().getSymbol()+ " " + m_decimalFormat.format(settle.getAmount()),
		  			m_decimalFormat.format(settle.getAmount())});
		  		//curr = settle.getCurrency().getSymbol();
		  		total+=settle.getAmount();
		  	}
	  	}

	  	//PmtProjectCost
	  	mapper = MasterMap.obtainMapperFor(PmtProjectCost.class);
	  	mapper.setActiveConn(m_conn);
	  	list = mapper.doSelectWhere(strSelect);
	  	iter = list.iterator();
	  	while(iter.hasNext()){
	  		PmtProjectCost voucher = (PmtProjectCost) iter.next();

	  		GenericMapper tmpMapper = MasterMap.obtainMapperFor(PmtProjectCostDetail.class);
		  	tmpMapper.setActiveConn(m_conn);
		  	String tmpQuery = IDBConstants.TABLE_PMT_PROJECT_COST + " = " + voucher.getIndex();
		  	System.err.println(tmpQuery);
		  	//List tmpList = tmpMapper.doSelectWhere(tmpQuery);
	  		//Iterator tmpIter = tmpList.iterator();
	  		/*double amount = 0;
	  		while(tmpIter.hasNext()){
	  			PmtProjectCostDetail tmpV = (PmtProjectCostDetail) tmpIter.next();
	  			amount = amount + tmpV.getaccValue();

	  		}*/
	  		double amount = voucher.getTotal();

	  		//if ()
	  		String payto =voucher.getPayTo();
	  		model.addRow(new Object[]{String.valueOf(i++), voucher.getReferenceNo(), m_dateFormat.format(voucher.getTransactionDate()),
	  				payto,voucher.getDescription(), getStatus(voucher.getStatus()),
	  				voucher.getEmpApproved().igetFullName(), m_dateFormat.format(voucher.getDateApproved()),
	  				voucher.getCurrency().getSymbol()+ " " + m_decimalFormat.format(amount) ,
	  				m_decimalFormat.format(amount*voucher.getExchangeRate())});
	  		//curr = voucher.getCurrency().getSymbol();
	  		total+=amount*voucher.getExchangeRate();
	  	}

	  	//PurchaseApPmt
	  	mapper = MasterMap.obtainMapperFor(PurchaseReceipt.class);
	  	mapper.setActiveConn(m_conn);
	  	list = mapper.doSelectWhere(strSelect);
	  	iter = list.iterator();
	  	while(iter.hasNext()){
	  		PurchaseReceipt purchase = (PurchaseReceipt)iter.next();

	  		GenericMapper tmpMapper = MasterMap.obtainMapperFor(PurchaseApPmt.class);
	  		tmpMapper.setActiveConn(m_conn);
	  		str = IDBConstants.ATTR_PURCHASE_RECEIPT + " = " + purchase.getIndex();
	  		System.err.println(str);
	  		List tmpList = tmpMapper.doSelectWhere(str);
	  		Iterator tmpIter = tmpList.iterator();
	  		while(tmpIter.hasNext()){
	  			PurchaseApPmt voucher = (PurchaseApPmt) tmpIter.next();
	  			String payto = "";
	  			model.addRow(new Object[]{String.valueOf(i++), voucher.getReferenceNo(),
	  				m_dateFormat.format(voucher.getTransactionDate()),payto,voucher.getDescription(),
	  				getStatus(voucher.getStatus()), voucher.getEmpApproved().igetFullName(),
	  				m_dateFormat.format(voucher.getDateApproved()),
	  				voucher.getAppMtCurr().getSymbol()+ " " + m_decimalFormat.format(voucher.getAppMtAmount()),
	  				m_decimalFormat.format(voucher.getAppMtAmount()*voucher.getAppMtExchRate())});
	  			//curr = voucher.getAppMtCurr().getSymbol();
	  			total+=voucher.getAppMtAmount()*voucher.getAppMtExchRate();
	  		}
	  	}
	  	if (m_table.getRowCount()>0){
	  		model.addRow(new Object[]{"", "","","","","","","",""});
	  		model.addRow(new Object[]{"", "","","","TOTAL","","","","",
	  				m_decimalFormat.format(total)});
	  	}
	  	m_model = model;

  }

  public void refresh(){
	  setVoucher(m_project);
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

  /**
   *
   */
  public class VoucherTable extends JTable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public VoucherTable() {
      VoucherTableModel model = new VoucherTableModel();
      model.addColumn("No");
      model.addColumn("VoucherNo");
      model.addColumn("VoucherDate");
      model.addColumn("Pay To");
      model.addColumn("Description");
      model.addColumn("Status");
      model.addColumn("ApprovedBy");
      model.addColumn("ApprovalDate");
      model.addColumn("ApprovedAmount");
      model.addColumn("ApprovedAmount (RP)");
      setModel(model);

	  getColumnModel().getColumn(0).setMaxWidth(30);
	  getColumnModel().getColumn(2).setMaxWidth(70);
	  getColumnModel().getColumn(7).setMaxWidth(90);
    }

	public TableCellRenderer getCellRenderer(int row, int column) {
		if(column>=0){
			if(isSummaryRow(row)){
				if(column==4)
					return new FormattedStandardCellRenderer(Font.BOLD, JLabel.LEFT);
				if(column==9)
					return new FormattedDoubleCellRenderer("###,##0.00;(###,##0.00)", JLabel.RIGHT, Font.BOLD);
			}
			else{
				if (column==8 || column==9)
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
  public class VoucherTableModel extends DefaultTableModel {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}