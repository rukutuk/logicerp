package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.DateCellEditor;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.LookupPicker.DefaultTableModelAdapter;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.GeneralVoucher;
import pohaci.gumunda.titis.accounting.entity.GeneralVoucherToGetTotal;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCost;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCostDetail;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCostDetail;
import pohaci.gumunda.titis.accounting.entity.VoucherLoader;
import pohaci.gumunda.titis.hrm.cgui.EmployeeCellEditor;

public class SearchVoucherDialog extends JDialog implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	SearchTable m_searchTable;
	VoucherListTable m_voucherListTable;
	JButton m_selectBt;
	int m_iResponse = JOptionPane.NO_OPTION;
	JFrame m_mainframe;
	JRadioButton m_andRadioBt, m_orRadioBt;
	JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
	JButton m_findBt, m_closeBt, m_clearBt;
	private JTextField filterField = new JTextField(10);

	Connection m_conn = null;
	long m_sessionid = -1;

	VoucherLoader loader;
	Object[] m_voucherList;
	private JComboBox m_statusComboBox;
	private JComboBox m_paymentSourceComboBox;
	private String m_type;
	boolean m_sign = false;
	private HashMap childrenMap = new HashMap();
	private HashMap journalStandardAccountsMap = new HashMap();

	public SearchVoucherDialog(JFrame owner, String title, Connection conn, long sessionid, VoucherLoader loader){
		super(owner, ( title == null ) ? "Search Voucher" : title, true);
		this.loader = loader;
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		setSize(1000, 700);
		m_sign = false;
		constructComponent();
		find();
	}
	public SearchVoucherDialog(JFrame owner, String title, Connection conn, long sessionid, VoucherLoader loader,String type){
		super(owner, ( title == null ) ? "Search Voucher" : title, true);
		this.loader = loader;
		System.err.println("masuk sini");
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		this.m_type=type;
		setSize(800, 700);
		m_sign = false;
		constructComponent();
		find();
	}

	public SearchVoucherDialog(JFrame owner, String title, Connection conn, long sessionid, VoucherLoader loader,String type,
			boolean sign){
		super(owner, ( title == null ) ? "Search Voucher" : title, true);
		this.loader = loader;
		System.err.println("masuk sini");
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		this.m_type=type;
		setSize(800, 700);
		this.m_sign = sign;
		constructComponent();
		find();
	}
	void constructComponent() {
		m_statusComboBox = new JComboBox(new Object[]{"Not Submitted","Submitted","Posted","All"});//0,1,3,*
		m_paymentSourceComboBox = new JComboBox(new Object[]{"Bank","Cash","Bank/Cash"});
		m_voucherListTable = new VoucherListTable();
		if(m_sign)
			m_voucherListTable = new VoucherListTable(m_sign,this.m_conn);

		m_voucherListTable.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					onSelect();
				}
			}
		});
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		m_selectBt = new JButton("Select");
		m_selectBt.addActionListener(this);
		buttonPanel.add(m_selectBt);

		centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Voucher List",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
		Box vertBox  = Box.createVerticalBox();
		vertBox.add(filterField);
		vertBox.add(new JScrollPane(m_voucherListTable));
		centerPanel.add(vertBox, BorderLayout.CENTER);
		centerPanel.add(buttonPanel, BorderLayout.NORTH);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(criteriaPanel(), BorderLayout.NORTH);
		getContentPane().add(centerPanel, BorderLayout.CENTER);
	}
	private void setBorder(JPanel panel,String theme){
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), theme ,
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
	}

	JPanel criteriaPanel() {
		m_searchTable = new SearchTable();
		m_andRadioBt = new JRadioButton("and");
		m_andRadioBt.setSelected(true);
		m_orRadioBt = new JRadioButton("or");

		m_containsRadioBt = new JRadioButton("Text Contains Criteria");
		m_containsRadioBt.setSelected(true);
		m_matchRadioBt = new JRadioButton("Match Case");
		m_wholeRadioBt = new JRadioButton("Find Whole Words only");
		m_findBt = new JButton("Find");
		m_findBt.addActionListener(this);
		m_closeBt = new JButton("Cancel");
		m_closeBt.addActionListener(this);
		m_clearBt = new JButton("Clear");
		m_clearBt.addActionListener(this);

		ButtonGroup bg = new ButtonGroup();
		bg.add(m_andRadioBt);
		bg.add(m_orRadioBt);

		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(m_containsRadioBt);
		bg2.add(m_matchRadioBt);
		bg2.add(m_wholeRadioBt);

		JPanel centerPanel = new JPanel();
		JPanel criteriaPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel operatorPanel = new JPanel();
		JPanel optionPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gridBagConstraints;

		scrollPane.setPreferredSize(new Dimension(100, 179));
		scrollPane.getViewport().add(m_searchTable);

		operatorPanel.setLayout(new GridLayout(2, 1));
		operatorPanel.add(m_andRadioBt);
		operatorPanel.add(m_orRadioBt);
		setBorder(operatorPanel,"Search Operator");

		optionPanel.setLayout(new GridLayout(3, 1));
		optionPanel.add(m_containsRadioBt);
		optionPanel.add(m_matchRadioBt);
		optionPanel.add(m_wholeRadioBt);
		setBorder(optionPanel,"Option");

		criteriaPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		gridBagConstraints.weightx = 1.0;
		//  criteriaPanel.add(new JLabel("Search Criteria"), gridBagConstraints);
		setBorder(criteriaPanel,"Search Criteria");

		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(1, 0, 5, 0);
		criteriaPanel.add(scrollPane, gridBagConstraints);

		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		//criteriaPanel.add(new JLabel("Search Operator"), gridBagConstraints);

		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new Insets(1, 0, 5, 0);
		criteriaPanel.add(operatorPanel, gridBagConstraints);

		gridBagConstraints.gridy = 4;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		//criteriaPanel.add(new JLabel("Option"), gridBagConstraints);

		gridBagConstraints.gridy = 5;
		criteriaPanel.add(optionPanel, gridBagConstraints);

		gridBagConstraints.gridy = 6;
		criteriaPanel.add(buttonPanel, gridBagConstraints);

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(m_findBt);
		buttonPanel.add(m_clearBt);
		buttonPanel.add(m_closeBt);

		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(criteriaPanel, BorderLayout.NORTH);

		return centerPanel;
	}

	public void setVisible( boolean flag ){
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());

		super.setVisible(flag);
	}

	public int getResponse() {
		return m_iResponse;
	}

	void find() {
		String criterion;
		String operator=" and ";
		if(m_orRadioBt.isSelected())
			operator =" or ";
		criterion = loader.getCriterion(m_searchTable,operator);
		m_voucherList = loader.find(criterion);
		if(m_sign) {
			//prepareChildren(criterion);
			//prepareJournalStandardAccounts();

			m_voucherListTable.setReceiptList(m_voucherList,this.m_type);
		} else
			m_voucherListTable.setReceiptList(m_voucherList);
	}	
	/**
	 * @return
	 */
	/*private String getIndexes() {
		String indexes = "";

		for (int i = 0; i < m_voucherList.length; i++) {
			long index = -1;
			if (m_type.equalsIgnoreCase("Project Cost")) {
				PmtProjectCost obj = (PmtProjectCost) m_voucherList[i];
				index = obj.getIndex();
			} else {
				PmtOperationalCost obj = (PmtOperationalCost) m_voucherList[i];
				index = obj.getIndex();
			}
			if (index > 0) {
				if (indexes.equals("")) {
					indexes = String.valueOf(index);
				} else {
					indexes += (", " + String.valueOf(index));
				}
			}
		}
		return indexes;
	}*/
	/**
	 *
	 */
	/*private void prepareJournalStandardAccounts() {
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(m_conn,m_sessionid,
					IDBConstants.MODUL_ACCOUNTING);

		List journalStdList = null;

		if (m_type.equalsIgnoreCase("Project Cost")) {
			journalStdList = helper
					.getJournalStandardSettingWithAccount(IConstants.PAYMENT_PROJECT_COST);
		} else {
			journalStdList = helper
					.getJournalStandardSettingWithAccount(IConstants.PAYMENT_OPERASIONAL_COST);
		}

		journalStandardAccountsMap = new HashMap();
		Iterator iterator = journalStdList.iterator();
		while(iterator.hasNext()) {
			JournalStandardSetting setting = (JournalStandardSetting) iterator.next();

			String key = setting.getAttribute();
			if (journalStandardAccountsMap.containsKey(key)) {

			} else {
				JournalStandard journal = setting.getJournalStandard();
				JournalStandardAccount[] jsAcc = journal.getJournalStandardAccount();

				journalStandardAccountsMap.put(key, jsAcc);
			}
		}



		//journalStandardAccountsMap
	}*/
	/**
	 * @param string
	 *
	 */
	/*private void prepareChildren(String indexes) {
		GenericMapper childrenMapper = null;

		String field = "";
		if(m_type.equalsIgnoreCase("Project Cost")){
			childrenMapper = MasterMap.obtainMapperFor(PmtProjectCostDetail.class);
			field = "PmtProjectCost";
		} else {
			childrenMapper = MasterMap.obtainMapperFor(PmtOperationalCostDetail.class);
			field = "PmtOperationalCost";
		}

		childrenMapper.setActiveConn(m_conn);
		String clause = "1=1";
		if (!indexes.equals(""))
			clause = field + " in (" + indexes + ")";

		if (!indexes.equals("")) {
			int idx = indexes.indexOf("ORDER BY");
			indexes = indexes.substring(0, idx);
			clause = field + " in (select autoindex from " + field + " where " + indexes + ")";
		}
		List childrenList = childrenMapper.doSelectWhere(clause);

		childrenMap = new HashMap();
		Iterator iterator = childrenList.iterator();
		while(iterator.hasNext()) {
			if(m_type.equalsIgnoreCase("Project Cost")){
				PmtProjectCostDetail det = (PmtProjectCostDetail) iterator.next();

				Long key = new Long(det.getPmtProjectCost().getIndex());
				if (childrenMap.containsKey(key)){
					List dets = (List) childrenMap.get(key);
					dets.add(det);
				} else {
					List dets = new ArrayList();
					dets.add(det);
					childrenMap.put(key, dets);
				}
			} else {
				PmtOperationalCostDetail det = (PmtOperationalCostDetail) iterator.next();

				Long key = new Long(det.getPmtOperationalCost().getIndex());
				if (childrenMap.containsKey(key)){
					List dets = (List) childrenMap.get(key);
					dets.add(det);
				} else {
					List dets = new ArrayList();
					dets.add(det);
					childrenMap.put(key, dets);
				}
			}
		}
	}*/

	void clear() {
		m_searchTable.clear();
		m_andRadioBt.setSelected(true);
		find();
	}

	Object selectedObj = null;
	void onSelect(){
		m_iResponse = JOptionPane.OK_OPTION;
		int selectedRow = m_voucherListTable.getSelectedRow();
		if (selectedRow > -1)
			selectedObj = m_voucherListTable.getRow(selectedRow);
		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_selectBt) {
			onSelect();
		}else if(e.getSource() == m_findBt) {
			find();
		}else if(e.getSource() == m_clearBt) {
			clear();
		}else if(e.getSource() == m_closeBt) {
			dispose();
		}
	}


	public class SearchTable extends JTable  {

		private static final long serialVersionUID = 1L;
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		public SearchTable() {
			SearchTableModel model = new SearchTableModel();
			model.addColumn("Attribute");
			model.addColumn("Description");
			model.addRow(new Object[]{"Payment Source","Bank/Cash"});
			model.addRow(new Object[]{"Voucher Type", m_type});
			model.addRow(new Object[]{"Voucher No", ""});
			model.addRow(new Object[]{"Voucher Date", dateformat.format(new Date())});
			model.addRow(new Object[]{"Originator", ""});
			model.addRow(new Object[]{"Approved By", ""});
			model.addRow(new Object[]{"Received By", ""});
			model.addRow(new Object[]{"Payment Source Unit Code", ""});
			model.addRow(new Object[]{"Status", "All"});
			model.addRow(new Object[]{"Submitted Date", ""});
			model.addRow(new Object[]{"Total", ""});

			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(140);
			getColumnModel().getColumn(0).setMaxWidth(140);
			getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());

		}

		public TableCellEditor getCellEditor(int row, int col) {
			if( col == 1){
				if(row == 7)
					return new UnitCellEditor(GumundaMainFrame.getMainFrame(),
							"Unit Code", m_conn, m_sessionid);
				if(row == 4 || row == 5 || row == 6 )
					return new EmployeeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
							m_conn, m_sessionid);
				if(row == 3 || row == 9 )
					return new DateCellEditor(GumundaMainFrame.getMainFrame());
				if(row == 8 )
					return new DefaultCellEditor(m_statusComboBox);
				if(row == 0 )
					return new DefaultCellEditor(m_paymentSourceComboBox);
			}
			return new BaseTableCellEditor();
		}

		public boolean isCellEditable(int row, int column) {
			if(row==1&&column==1)return false;
			return super.isCellEditable(row, column);
		}

		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}

		public void clear() {
			stopCellEditing();
			int row = getRowCount();
			for(int i = 0; i < row; i ++){
				if(i==0)
					setValueAt("Bank/Cash", i, 1);
				else if (i==1)
					setValueAt(m_type, i, 1);
				else if(i==3)
					setValueAt(dateformat.format(new Date()), i, 1);
				else if(i==8)
					setValueAt("All", i, 1);
				else setValueAt("", i, 1);
			}
		}

	}

	class SearchTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col) {
			if(col == 0)
				return false;
			return true;
		}
	}

	class VoucherListTable extends JTable {
		private static final long serialVersionUID = 1L;
		DefaultTableModelAdapter model =new DefaultTableModelAdapter();
		public VoucherListTable() {
			model.addColumn("No");
			model.addColumn("Payment Source");
			model.addColumn("Voucher Type");
			model.addColumn("Voucher No");
			model.addColumn("Voucher Date");
			model.addColumn("Pay To");
			model.addColumn("Originator");
			model.addColumn("Approved By");
			model.addColumn("Received By");
			model.addColumn("Unit Code");
			model.addColumn("Status");
			model.addColumn("Submitted Date");
			setModel(model.buildTableModel(filterField));
			getColumnModel().getColumn(0).setPreferredWidth(25);
			getColumnModel().getColumn(0).setMaxWidth(25);
		}
		Connection vConn;
		public VoucherListTable(boolean sign,Connection conn) {
			vConn=conn;
			model.addColumn("No");
			model.addColumn("Payment Source");
			model.addColumn("Voucher Type");
			model.addColumn("Voucher No");
			model.addColumn("Voucher Date");
			model.addColumn("Pay To");
			model.addColumn("Originator");
			model.addColumn("Approved By");
			model.addColumn("Received By");
			model.addColumn("Unit Code");
			model.addColumn("Status");
			model.addColumn("Submitted Date");
			model.addColumn("Total");
			setModel(model.buildTableModel(filterField));
			getColumnModel().getColumn(0).setPreferredWidth(25);
			getColumnModel().getColumn(0).setMaxWidth(25);
		}
		Object[] objRows;
		public Object getRow(int rowIdx){
			return objRows[((Integer)getValueAt(rowIdx,0)).intValue()-1];
		}

		void setReceiptList(Object[] object) {
			model.clearRows();
			objRows = object;
			for(int i = 0; i < object.length; i ++) {
				GeneralVoucher voucher = (GeneralVoucher) object[i];
				model.addRow(new Object[]{
						new Integer(i + 1),
						voucher.vgetPaymentSource(),
						voucher.vgetVoucherType(),
						voucher.vgetVoucherNo(),
						voucher.vgetVoucherDate(),
						voucher.vgetPayTo(),
						voucher.vgetOriginator(),
						voucher.vgetApprovedBy(),
						voucher.vgetReceivedBy(),
						voucher.vgetUnitCode(),
						voucher.vgetStatus(),
						voucher.vgetSubmitDate()}
				);
			}
		}

		void setReceiptList(Object[] object,String type) {
			model.clearRows();
			objRows = object;
			for(int i = 0; i < object.length; i ++) {
				GeneralVoucherToGetTotal voucher = (GeneralVoucherToGetTotal) object[i];
				if (voucher!=null)
				model.addRow(new Object[]{
						new Integer(i + 1),
						voucher.igetPaymentSource(),
						voucher.vgetVoucherType(),
						voucher.vgetVoucherNo(),
						voucher.vgetVoucherDate(),
						voucher.vgetPayTo(),
						voucher.vgetOriginator(),
						voucher.vgetApprovedBy(),
						voucher.vgetReceivedBy(),
						voucher.vgetUnitCode(),
						voucher.vgetStatus(),
						voucher.vgetSubmitDate(),
						//voucher.vgetSymbolCurrency() + " " + getTotal(voucher, type, vConn).toString()}
						voucher.vgetSymbolCurrency() + " " + voucher.vgetTotal()}
				);
			}

		}

		public String getTotal(GeneralVoucherToGetTotal voucher,String type,Connection conn) {
			double total = 0;
			DecimalFormat dm = new DecimalFormat("#,##0.00");

			if(type.equalsIgnoreCase("Project Cost")){
				/*GenericMapper mapper=MasterMap.obtainMapperFor(PmtProjectCostDetail.class);
				mapper.setActiveConn(vConn);
				String strWhere = IDBConstants.ATTR_PMT_PROJECT_COST+"="+voucher.vgetIndex();
				List rs=mapper.doSelectWhere(strWhere);
				*/
				PmtProjectCostDetail detail=null;

				Long key = new Long(voucher.vgetIndex());
				List rs = (List) childrenMap.get(key);

				String attr = "";
				if (voucher.vgetCashAccount()!=null)
					attr = IConstants.ATTR_PMT_CASH;
				else if (voucher.vgetBankAccount()!=null)
					attr = IConstants.ATTR_PMT_BANK;
				else {
					if (voucher.igetPaymentSource().equalsIgnoreCase("CASH"))
						attr = IConstants.ATTR_PMT_CASH;
					else
						attr = IConstants.ATTR_PMT_BANK;
				}
				/*JournalStandardAccount[] jsAccounts = getJournalStandardAccount(attr);*/

				JournalStandardAccount[] jsAccounts = (JournalStandardAccount[]) journalStandardAccountsMap.get(attr);

				if (rs!=null)
				for(int i=0;i<rs.size();i++){
					detail=(PmtProjectCostDetail)rs.get(i);
					for (int j=0;j<jsAccounts.length;j++)
						if (detail.getAccount().getIndex() == jsAccounts[j].getAccount().getIndex())
							if (!jsAccounts[j].isCalculate() && !jsAccounts[j].isHidden()){
								JournalStandardAccount jsAccount = jsAccounts[j];
								double amount = detail.getaccValue();
								if (jsAccount.getBalance()==1)
									amount= -detail.getaccValue();
								total +=amount;
							}
				}
			}else if(type.equalsIgnoreCase("Operational Cost")){
				if(voucher != null){
					/*GenericMapper mapper=MasterMap.obtainMapperFor(PmtOperationalCostDetail.class);
					mapper.setActiveConn(conn);
					List detailList=mapper.doSelectWhere(IDBConstants.ATTR_PMT_OPERASIONAL_COST+"="+voucher.vgetIndex());*/


					Long key = new Long(voucher.vgetIndex());
					List detailList = (List) childrenMap.get(key);

					PmtOperationalCostDetail detail;
					int length = detailList.size();
					if (length>0){
						/*JournalStandardAccount[] jsAccounts = getJournalStandardAccount((PmtOperationalCost) voucher);*/

						PmtOperationalCost pmt = (PmtOperationalCost) voucher;

						String attr = "";
						if (pmt.getCashAccount()!=null) {
							attr = IConstants.ATTR_PMT_CASH;
						}else if (pmt.getBankAccount()!=null) {
							attr = IConstants.ATTR_PMT_BANK;
						}else if (pmt.getPaymentSource().equalsIgnoreCase("BANK")){
							attr = IConstants.ATTR_PMT_BANK;
						}else if (pmt.getPaymentSource().equalsIgnoreCase("CASH")){
							attr = IConstants.ATTR_PMT_CASH;
						}

						JournalStandardAccount[] jsAccounts = (JournalStandardAccount[]) journalStandardAccountsMap.get(attr);

						if (detailList!=null)
						for(int i=0;i<detailList.size();i++){
							detail=(PmtOperationalCostDetail)detailList.get(i);
							for (int j=0;j<jsAccounts.length;j++)
								if (detail.getAccount().getIndex() == jsAccounts[j].getAccount().getIndex())
									if (!jsAccounts[j].isCalculate() && !jsAccounts[j].isHidden()){
										JournalStandardAccount jsAccount = jsAccounts[j];
										double amount= detail.getAccValue();
										if (jsAccount.getBalance()==1)
											amount = -detail.getAccValue();
										total += amount;
									}
						}
					}else{
						total = 0;
					}
				}
			}
			return dm.format(total);
		}

		//Project Cost
		public JournalStandardAccount[] getJournalStandardAccount(String attr){
			JournalStandardSettingPickerHelper helper =
				new JournalStandardSettingPickerHelper(m_conn,m_sessionid,
						IDBConstants.MODUL_ACCOUNTING);
			List journalStdList =
					helper.getJournalStandardSettingWithAccount(
						IConstants.PAYMENT_PROJECT_COST,
						attr);

			JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
			JournalStandard journal = setting.getJournalStandard();
			JournalStandardAccount[] jsAcc = journal.getJournalStandardAccount();
			return jsAcc;
		}

		//Operational Cost
		public JournalStandardAccount[] getJournalStandardAccount(PmtOperationalCost entity){
			String attr = "";
			if (entity.getCashAccount()!=null) {
				attr = IConstants.ATTR_PMT_CASH;
			}else if (entity.getBankAccount()!=null) {
				attr = IConstants.ATTR_PMT_BANK;
			}else if (entity.getPaymentSource().equalsIgnoreCase("BANK")){
				attr = IConstants.ATTR_PMT_BANK;
			}else if (entity.getPaymentSource().equalsIgnoreCase("CASH")){
				attr = IConstants.ATTR_PMT_CASH;
			}
			JournalStandardSettingPickerHelper helper =
				new JournalStandardSettingPickerHelper(m_conn,m_sessionid,
						IDBConstants.MODUL_ACCOUNTING);
			List journalStdList =
				helper.getJournalStandardSettingWithAccount(
						IConstants.PAYMENT_OPERASIONAL_COST,
						attr);
			JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
			JournalStandard journal = setting.getJournalStandard();
			JournalStandardAccount[] jsAcc = journal.getJournalStandardAccount();
			return jsAcc;
		}

		public TableCellEditor getCellEditor(int row, int col) {
			return cellEditor;
		}
		void clear() {
			model.clearRows();
		}
	}
}
