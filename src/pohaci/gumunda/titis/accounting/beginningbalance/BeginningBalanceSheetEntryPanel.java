package pohaci.gumunda.titis.accounting.beginningbalance;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance.NotBalancedException;
import pohaci.gumunda.titis.accounting.cgui.AccountPicker;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardPicker;
import pohaci.gumunda.titis.accounting.cgui.MainFrame;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.AccountTree;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.DoubleAwareDefaultCellRenderer;
import pohaci.gumunda.titis.project.cgui.Customer;
import ca.odell.glazedlists.DefaultExternalExpansionModel;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.TreeList.ExpansionModel;
import ca.odell.glazedlists.TreeList.Format;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.impl.sort.ComparableComparator;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.TreeTableSupport;
import pohaci.gumunda.titis.application.DatePicker;

public class BeginningBalanceSheetEntryPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JTable m_table;
	JScrollPane m_scrollPane;
	Hashtable m_treeMap = new Hashtable();
    transient long sessionid;
    transient Connection conn;
	private JButton addButton = null;
	private JButton deleteButton = null;
	private JPanel buttonPanel = null;
	private JButton editButton = null;
	private JTextField filterTextField = null;
	private JPanel filterPanel = null;
	private JLabel filterLabel = null;
	private transient TreeTableSupport installedTreeSupport;
	private transient EventSelectionModel tableSelectionModel;
	public BeginningBalanceSheetEntryPanel(Connection conn, long sessionid)	{
		this.conn = conn;
		this.sessionid = sessionid;
		initialize();
		m_table.setDefaultRenderer(Object.class, new DoubleAwareDefaultCellRenderer());
		if (conn!=null)
			initModel();
	}

	public void initModel()	{
		initTree(conn,sessionid);
		initTable();
		accountPicker.init(conn, sessionid);
		unitPicker.init(conn, sessionid);
		journalStandardPicker.init(conn, sessionid);
		initBalanceRoot();
	}

	void initTree(Connection conn, long sessionid){
		AccountTree accountTree = new AccountTree(conn,sessionid);
		TreeModel model = accountTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		initTreeMapForNode(root);
		Enumeration enumeration = root.breadthFirstEnumeration();
		while (enumeration.hasMoreElements()){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
			initTreeMapForNode(node);
		}
	}

	DefaultMutableTreeNode accountToNode(Account acct){
		if (m_treeMap.containsKey(acct.getCode())){
			return (DefaultMutableTreeNode) m_treeMap.get(acct.getCode());
		}
		else
			return null;
	}

	private void initTreeMapForNode(DefaultMutableTreeNode node) {
		if (node.getUserObject() instanceof Account){
			Account account = (Account) node.getUserObject();
			m_treeMap.put(account.getCode(),node);
		}
		else{
			System.out.println(node.getUserObject().getClass());
		}
	}

	JTable getJTable() {
		if (m_table == null) {
			m_table = new JTable();
		}
		return m_table;
	}

	JScrollPane getJScrollPane(){
		if (m_scrollPane == null) {
			m_scrollPane = new JScrollPane();
			m_scrollPane.setPreferredSize(new Dimension(700, 300));
			m_scrollPane.setViewportView(getJTable());
		}
		return m_scrollPane;
	}

	void initialize(){
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.weightx = 1.0D;
		gridBagConstraints21.fill = GridBagConstraints.NONE;
		gridBagConstraints21.anchor = GridBagConstraints.EAST;
		gridBagConstraints21.gridy = 1;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.gridy = 4;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		//gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL; //
		gridBagConstraints3.gridy = 2;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		this.add(getJScrollPane(), gridBagConstraints);
		this.add(getButtonPanel(), gridBagConstraints3);
		this.add(getFilterPanel(), gridBagConstraints6);
		this.add(getJPanel(), gridBagConstraints21);
	}

	EventList eventList;
	private JLabel jLabel = null;
	private JPanel jPanel = null;
	private JLabel jLabel1 = null;
	private AccountPicker accountPicker = null;
	private UnitPicker unitPicker = null;
	private JLabel jLabel2 = null;
	private DatePicker datePicker = null;
	private JButton panelSaveButton = null;
	transient private BeginningBalance balance = new BeginningBalance();  //  @jve:decl-index=0:
	private JButton submitButton = null;
	transient private BeginningBalanceSheetEntry[] entries;
	private JLabel jLabel3 = null;
	transient private JournalStandardPicker journalStandardPicker = null;
	private JButton checkButton;

	void initTable(){
		if (installedTreeSupport != null)
			installedTreeSupport.uninstall();
		List contentList = initData();
		eventList = GlazedLists.eventList(contentList);
		TableFormat tableFormat = new TableFormat1();
		Format treeFormat = new TreeFormat1();

		ExpansionModel expansionModel = new DefaultExternalExpansionModel(TreeList.NODES_START_EXPANDED);
		TextFilterator filterator = new Filterator();
		//SortedList sortedList = new SortedList(eventList);
		FilterList filterList = new FilterList(eventList,
				new TextComponentMatcherEditor(getFilterTextField(),filterator ,true) );
		//TreeList treeList = new TreeList(eventList,treeFormat,expansionModel );
		TreeList treeList = new TreeList(filterList,treeFormat,expansionModel );
		EventTableModel eventTableModel = new EventTableModel(treeList,tableFormat );
		m_table.setModel(eventTableModel);
		//TableComparatorChooser.install(m_table,sortedList,TableComparatorChooser.MULTIPLE_COLUMN_KEYBOARD);
		tableSelectionModel = new EventSelectionModel(treeList);
		m_table.setSelectionModel(tableSelectionModel);
		installedTreeSupport = TreeTableSupport.install(m_table,treeList,0);
		installedTreeSupport.setShowExpanderForEmptyParent(true);
	}

	public void reload(){
		if (eventList==null)
			return;
		eventList.clear();
		eventList.addAll(initData());
	}

	private List createDummyContent() {
		ArrayList masterList = new ArrayList();

		BeginningBalanceSheetEntry entry = new BeginningBalanceSheetEntry();
		entry.setAccValue(1000);
		Currency rupiah;
		entry.setCurrency(rupiah = new Currency("Rp","IDR","Rupiah", "Rupiah", "Sen", MoneyTalk.LANGUAGE_INDONESIAN));
		entry.setType((short)0);
		entry.setAccount(new Account("1.01.04","PersediaanKu",(short)0,false,(short)1,"-",""));
		masterList.add(entry);

		entry = new BeginningBalanceSheetEntry();
		entry.setType(BeginningBalanceSheetEntry.ACCOUNTRECEIVABLE);
		Account arAccount;
		entry.setAccount(arAccount = new Account("1.01.03.01","Account Receivable",(short)0,false,(short)0,"-",""));
		BeginningBalanceSheetDetail[] details = new BeginningBalanceSheetDetail[2];
		Customer customer = new Customer("C001","Pelanggan Nomor Satu","","",0,"","","","","","","","");
		BeginningAccountReceivable ar0 = new BeginningAccountReceivable();
		details[0] = ar0;
		ar0.setCustomer(customer);
		ar0.setAccValue(150);
		ar0.setCurrency(rupiah);
		ar0.setAccount(arAccount);
		Customer customer2 = new Customer("C002","Pelanggan Nomor Dua","","",0,"","","","","","","","");
		BeginningAccountReceivable ar1 = new BeginningAccountReceivable();
		details[1] = ar1;
		ar1.setCustomer(customer2);
		ar1.setAccount(arAccount);
		ar1.setAccValue(3000);
		ar1.setCurrency(rupiah);
		entry.setDetails(details);
		masterList.add(entry);
		entry.recalculate();
		List contentList = masterToContent(masterList);
		return contentList;
	}

	List initData() {
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		try {
			entries = logic.getAllBeginningBalanceSheetEntries(this.sessionid, IDBConstants.MODUL_ACCOUNTING);
			balance.setEntries(entries);
			return masterToContent(Arrays.asList(entries));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"getAllBeginningBalanceSheetEntries failed.");
			return createDummyContent();
		}
	}

	private List masterToContent(List masterList) {
		ArrayList list = new ArrayList();
		Iterator iter = masterList.iterator();
		while (iter.hasNext()){
			BeginningBalanceSheetEntry entry = (BeginningBalanceSheetEntry) iter.next();
			list.add(entry);
			int cnt;
			if ((cnt = entry.getDetails().length)>0){
				int i;
				for (i=0; i<cnt;i++)
					list.add(entry.getDetails()[i]);
			}
		}
		return list;
	}

	/**
	 * This method initializes addButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("Add");
			addButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					BeginningBalanceAddEntryPanel panel = new BeginningBalanceAddEntryPanel(conn,sessionid, BeginningBalanceSheetEntryPanel.this);
					MainFrame.staticCreateFrameForPanel(panel,"Add Beginning Balance Sheet Entry");
				}
			});
		}
		return addButton;
	}
	/**
	 * This method initializes deleteButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton();
			deleteButton.setText("Delete");
			deleteButton.addActionListener(new ActionListener()	{
				public void actionPerformed(ActionEvent e) {
					if (tableSelectionModel==null)
						return;
					EventList selected = tableSelectionModel.getSelected();
					//String s="";
					if (selected.size()>0)
					{
						Object object = selected.get(0);
						if (object instanceof BeginningBalanceSheetDetail)
						{
							doDelete((BeginningBalanceSheetDetail)object);
						}
						if (object instanceof BeginningBalanceSheetEntry)
						{
							doDelete((BeginningBalanceSheetEntry)object);
						}
						//s = "(" + object.getClass().toString() + ")"
						// + object.toString()
						//;
					}
				}
			});
		}
		return deleteButton;
	}
	/**
	 * This method initializes buttonPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 5;
			gridBagConstraints7.insets = new Insets(0, 1, 1, 1);
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 4;
			gridBagConstraints6.insets = new Insets(0, 1, 1, 1);
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.insets = new Insets(0, 1, 1, 1);
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 3;
			gridBagConstraints2.insets = new Insets(0, 1, 1, 1);
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new Insets(0, 1, 1, 1);
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getAddButton(), gridBagConstraints1);
			buttonPanel.add(getDeleteButton(), gridBagConstraints2);
			buttonPanel.add(getEditButton(), gridBagConstraints5);
			buttonPanel.add(getSubmitButton(), gridBagConstraints6);
			buttonPanel.add(getCheckButton(), gridBagConstraints7);
		}
		return buttonPanel;
	}
	/**
	 * @return
	 */
	private JButton getCheckButton() {
		if (checkButton == null) {
			checkButton = new JButton();
			checkButton.setText("Check");
			checkButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doCheck();
				}
			});
		}
		return checkButton;
	}

	/**
	 *
	 */
	private void doCheck() {
		gui2entity();
		balance.init(conn,sessionid);

		//List txList;
		Hashtable unitToDetailList = new Hashtable();  // i add this
		ArrayList entriesWithoutUnit = new ArrayList(); // i add this
		try {
			balance.submit(conn, sessionid, unitToDetailList, entriesWithoutUnit);
			JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "Beginning balance is balanced");
			return;
		} catch (NotBalancedException e1) {
			//NumberFormat format = NumberFormat.getInstance();
			NumberFormat format = new DecimalFormat("#,##0.00");
			JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "Beginning balance is not balanced! Debit is "
					+ format.format(e1.getDebitValue()) + " and Credit is " + format.format(e1.getCreditValue()));
			return;
		}
	}

	/**
	 * This method initializes editButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getEditButton() {
		if (editButton == null) {
			editButton = new JButton();
			editButton.setText("Edit");
			editButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (tableSelectionModel==null)
						return;
					EventList selected = tableSelectionModel.getSelected();
					//String s="";
					if (selected.size()>0)
					{
						Object object = selected.get(0);
						if (object instanceof BeginningBalanceSheetDetail)
						{
							doEdit((BeginningBalanceSheetDetail)object);
						}
						if (object instanceof BeginningBalanceSheetEntry)
						{
							doEdit((BeginningBalanceSheetEntry)object);
						}
						//s = "(" + object.getClass().toString() + ")"
						// + object.toString()
						//;
					}
					//JOptionPane.showMessageDialog(BeginningBalanceSheetEntryPanel.this
					//		, Integer.toString(selected.size())+ s);
					//System.out.println("actionPerformed()");

				}

			});
		}
		return editButton;
	}
	private void doEdit(BeginningBalanceSheetEntry entry) {
		BeginningBalanceAddEntryPanel panel = new BeginningBalanceAddEntryPanel(this.conn,this.sessionid,this);
		panel.setEntity(entry);
		MainFrame.staticCreateFrameForPanel(panel,"Beginning Balance Sheet Entry" );
	}

	private void doEdit(BeginningBalanceSheetDetail detail) {
		doEdit(detail.entry());
	}
	private void doDelete(BeginningBalanceSheetDetail detail) {
		doDelete(detail.entry());
	}
	private void doDelete(BeginningBalanceSheetEntry entry) {
		int chosen = JOptionPane.showConfirmDialog(this,"Delete beginning balance for " + entry.getAccount() + "?");
		if (chosen == JOptionPane.YES_OPTION)
		{
			AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
			logic.deleteBeginningBalanceSheetEntry(this.sessionid,IDBConstants.MODUL_ACCOUNTING,
					entry.getIndex(),entry);
			this.reload();
			JOptionPane.showMessageDialog(this,"Delete completed.");

		}
	}
	/**
	 * This method initializes filterTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getFilterTextField() {
		if (filterTextField == null) {
			filterTextField = new JTextField();
			filterTextField.setColumns(3);
		}
		return filterTextField;
	}
	/**
	 * This method initializes filterPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getFilterPanel() {
		if (filterPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.insets = new Insets(2, 0, 0, 10);
			gridBagConstraints7.gridy = 0;
			filterLabel = new JLabel();
			filterLabel.setText("Filter :");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridx = 2;
			filterPanel = new JPanel();
			filterPanel.setLayout(new GridBagLayout());
			filterPanel.add(getFilterTextField(), gridBagConstraints4);
			filterPanel.add(filterLabel, gridBagConstraints7);
		}
		return filterPanel;
	}
	public static Object calcCredit(Account account, double accvalue) {
		boolean isCredit = false;
		if (account.getBalance() != 0) // Credit
			isCredit = true;
		if (accvalue < 0)
		{
			isCredit = !isCredit;
			accvalue = -accvalue;
		}
		if (isCredit)
			return new Double(accvalue);
		return null;
	}
	public static Object calcDebit(Account account, double accvalue) {
		boolean isDebit = false;
		if (account.getBalance() == 0) // Debit
			isDebit = true;
		if (accvalue < 0)
		{
			isDebit = !isDebit;
			accvalue = -accvalue;
		}
		if (isDebit)
			return new Double(accvalue);
		return null;
	}
	private class TreeFormat1 implements Format {
		public void getPath(List path, Object element) {
			if (element instanceof BeginningBalanceSheetEntry)
			{
				BeginningBalanceSheetEntry entry = (BeginningBalanceSheetEntry) element;
				addAccountPath(path,entry.getAccount());
				path.add(entry);
			}
			if (element instanceof BeginningBalanceSheetDetail)
			{
				BeginningBalanceSheetDetail detail = (BeginningBalanceSheetDetail) element;
				Account account = detail.getAccount();
				addAccountPath(path, account);
				path.add(detail.entry());
				path.add(detail);
			}
		}

		private void addAccountPath(List path, Account account) {
			DefaultMutableTreeNode node = accountToNode(account);
			if (node == null)
			{
				path.add("???");
				return;
			}
			DefaultMutableTreeNode parent2 = (DefaultMutableTreeNode) node.getParent();
			ArrayList pathReversed = new ArrayList();
			if (parent2!=null)
			{
				while (parent2!=null)
				{
					pathReversed.add(0,parent2.getUserObject());
					parent2 = (DefaultMutableTreeNode) parent2.getParent();
				}
			}
			path.addAll(pathReversed);
		}

		public boolean allowsChildren(Object element) {
			//if (element instanceof BeginningBalanceSheetDetail)
			//	return false;
			//else
			return true;
		}

		public Comparator getComparator(int depth) {
			return new ComparableComparator();
		}

	}

	private static class Filterator implements TextFilterator {
		NumberFormat numberFormat;
		Filterator()
		{
			DecimalFormat d = new DecimalFormat();
			d.setGroupingUsed(false);
			numberFormat = d;
		}
		public void getFilterStrings(List baseList, Object element) {
			if (element instanceof BeginningBalanceSheetEntry)
			{
				BeginningBalanceSheetEntry entry = (BeginningBalanceSheetEntry) element;
				baseList.add(entry.getAccount().toStringWithCode());
				baseList.add(entry.getCurrency().toString());
				baseList.add(numberFormat.format(entry.getAccValue()));
				baseList.add(numberFormat.format(entry.getExchangeRate()));
			}
			if (element instanceof BeginningBalanceSheetDetail)
			{
				BeginningBalanceSheetDetail detail = (BeginningBalanceSheetDetail) element;
				baseList.add(detail.getAccount().toStringWithCode());
				baseList.add(detail.getCurrency().toString());
				baseList.add(numberFormat.format(detail.getAccValue()));
				baseList.add(numberFormat.format(detail.getExchangeRate()));
				Object o = detail.subsidiaryStr();
				if (o!=null)
					baseList.add(o.toString());
			}
		}

	}

	private static class TableFormat1 implements TableFormat {
		NumberFormat numberFormat = NumberFormat.getInstance();
		String formatDouble(Double d)
		{
			if (d==null) return null;
			return numberFormat.format(d);
		}
		static String[] columnNames = new String[] {
			"Account code",
			"Account name",
			"Subsidiary",
			"Currency",
			"Debit",
			"Credit",
			"Exchange rate"
		};

		public int getColumnCount() {
			return columnNames.length;
		}

		public String getColumnName(int column) {
			if (column < columnNames.length)
				return columnNames[column];
			else
				return "-";
		}

		public Object getColumnValue(Object baseObject, int column) {
			if (baseObject instanceof BeginningBalanceSheetEntry)
				return getColValue((BeginningBalanceSheetEntry)baseObject,column);
			else if (baseObject instanceof BeginningBalanceSheetDetail)
				return getColValue((BeginningBalanceSheetDetail)baseObject,column);
			else if (baseObject instanceof Account)
				return getColValue((Account)baseObject,column);
			else if ((baseObject instanceof String) && (column==0))
				return baseObject;
			return null;
		}

		private Object getColValue(Account account, int column) {
			switch (column)
			{
			case 0:
				return account.getCode();
			case 1:
				return account.getName();
			case 2:
				return null;
			case 3:
				return "";
			case 4:
				return null;
			case 5:
				return null;
			case 6:
				return null;
			default:
				return null;
			}
		}

		private Object getColValue(BeginningBalanceSheetDetail detail, int column) {
			switch (column)
			{
			case 0:
				return detail.getAccount().getCode();
			case 1:
				return detail.getAccount().getName();
			case 2:
				return detail.subsidiaryStr();
			case 3:
				return detail.getCurrency().getSymbol();
			case 4:
				return (Double) BeginningBalanceSheetEntryPanel.calcDebit(detail.getAccount(),detail.getAccValue());
			case 5:
				return (Double) BeginningBalanceSheetEntryPanel.calcCredit(detail.getAccount(),detail.getAccValue());
			case 6:
				return new Double(detail.getExchangeRate());
			default:
				return null;
			}

		}

		private Object getColValue(BeginningBalanceSheetEntry detail, int column) {
			switch (column)
			{
			case 0:
				return detail.getAccount().getCode();
			case 1:
				return detail.getAccount().getName();
			case 2:
				return null;
			case 3:
				return detail.getCurrency().getSymbol();
			case 4:
				return (Double) BeginningBalanceSheetEntryPanel.calcDebit(detail.getAccount(),detail.getAccValue());
			case 5:
				return (Double) BeginningBalanceSheetEntryPanel.calcCredit(detail.getAccount(),detail.getAccValue());
			case 6:
				return new Double(detail.getExchangeRate());
			default:
				return null;
			}
		}

	}

	/**
	 * This method initializes jLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new JLabel();
			jLabel.setText("Default Unit");
		}
		return jLabel;
	}
	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 2;
			gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.weightx = 1.0D;
			gridBagConstraints17.gridy = 4;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.anchor = GridBagConstraints.EAST;
			gridBagConstraints16.gridy = 4;
			jLabel3 = new JLabel();
			jLabel3.setText("Journal Standard");
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 5;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 2;
			gridBagConstraints14.anchor = GridBagConstraints.EAST;
			gridBagConstraints14.gridy = 5;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 2;
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.weightx = 1.0D;
			gridBagConstraints13.gridy = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.fill = GridBagConstraints.NONE;
			gridBagConstraints9.anchor = GridBagConstraints.EAST;
			gridBagConstraints9.gridy = 1;
			jLabel2 = new JLabel();
			jLabel2.setText("Date of recorded Balance");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.gridy = 3;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.fill = GridBagConstraints.NONE;
			gridBagConstraints10.anchor = GridBagConstraints.EAST;
			gridBagConstraints10.gridy = 3;
			jLabel1 = new JLabel();
			jLabel1.setText("Bridge Account");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.fill = GridBagConstraints.NONE;
			gridBagConstraints8.anchor = GridBagConstraints.EAST;
			gridBagConstraints8.gridy = 2;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setPreferredSize(new Dimension(300, 130));
			jPanel.add(getJLabel(), gridBagConstraints8);
			jPanel.add(jLabel1, gridBagConstraints10);
			jPanel.add(getAccountPicker(), gridBagConstraints11);
			jPanel.add(getUnitPicker(), gridBagConstraints12);
			jPanel.add(jLabel2, gridBagConstraints9);
			jPanel.add(getDatePicker(), gridBagConstraints13);
			jPanel.add(getPanelSaveButton(), gridBagConstraints14);
			//jPanel.add(getSubmitButton(), gridBagConstraints15);
			jPanel.add(jLabel3, gridBagConstraints16);
			jPanel.add(getJournalStandardPicker(), gridBagConstraints17);
		}
		return jPanel;
	}
	/**
	 * This method initializes lookupAccountPicker1
	 *
	 * @return pohaci.gumunda.titis.accounting.cgui.LookupAccountPicker1
	 */
	private AccountPicker getAccountPicker() {
		if (accountPicker == null) {
			accountPicker = new AccountPicker();
		}
		return accountPicker;
	}
	/**
	 * This method initializes lookupUnitPicker
	 *
	 * @return pohaci.gumunda.titis.accounting.cgui.LookupUnitPicker
	 */
	private UnitPicker getUnitPicker() {
		if (unitPicker == null) {
			unitPicker = new UnitPicker();
		}
		return unitPicker;
	}
	/**
	 * This method initializes datePicker
	 *
	 * @return pohaci.gumunda.titis.application.DatePicker
	 */
	private DatePicker getDatePicker() {
		if (datePicker == null) {
			datePicker = new DatePicker();
			datePicker.setPreferredSize(new Dimension(125, 20));
		}
		return datePicker;
	}
	/**
	 * This method initializes panelSaveButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getPanelSaveButton() {
		if (panelSaveButton == null) {
			panelSaveButton = new JButton();
			panelSaveButton.setText("Save");
			panelSaveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					panelSave();
				}
			});
		}
		return panelSaveButton;
	}
	void entity2gui()
	{
		this.datePicker.setDate(balance.getBalanceDate());
		this.accountPicker.setAccount(balance.getBridgeAccount());
		this.unitPicker.setObject(balance.getDefaultUnit());
		this.journalStandardPicker.setJournalStandard(balance.getJournalStandard());
	}

	void gui2entity() {
		balance.setBalanceDate(this.datePicker.getDate());
		balance.setBridgeAccount(this.accountPicker.getAccount());
		balance.setDefaultUnit(this.unitPicker.getUnit());
		balance.setEntries(this.entries);
		balance.setJournalStandard(journalStandardPicker.getJournalStandard());
	}
	void doSubmit() {
		gui2entity();
		balance.init(conn,sessionid);

		List txList;
		Hashtable unitToDetailList = new Hashtable();  // i add this
		ArrayList entriesWithoutUnit = new ArrayList(); // i add this

		try {
			txList = balance.submit(conn, sessionid, unitToDetailList, entriesWithoutUnit);
		} catch (NotBalancedException e1) {
			//NumberFormat format = NumberFormat.getInstance();
			NumberFormat format = new DecimalFormat("#,##0.00");
			JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "Beginning balance is not balanced! Debit is "
					+ format.format(e1.getDebitValue()) + " and Credit is " + format.format(e1.getCreditValue()));
			return;
		}
		AccountingBusinessLogic logic = new AccountingBusinessLogic(this.conn);
		try {
			List list = logic.createTransactionBlock(sessionid, IDBConstants.MODUL_ACCOUNTING, txList);

			Iterator iterator = list.iterator();
			while(iterator.hasNext()) {
				Transaction transaction = (Transaction) iterator.next();

				Unit unit = transaction.getUnit();

				List transList = (List) unitToDetailList.get(unit);
				Iterator iter = transList.iterator();
				while(iter.hasNext()) {
					BeginningBalanceSheetDetail detail = (BeginningBalanceSheetDetail) iter.next();
					detail.setTrans(transaction);

					logic.updateBeginningBalanceSheetDetail(sessionid, IDBConstants.MODUL_ACCOUNTING, detail.getIndex(), detail);
				}

				if (unit.equals(balance.getDefaultUnit())) {
					iter = entriesWithoutUnit.iterator();

					while (iter.hasNext()) {
						BeginningBalanceSheetEntry entry = (BeginningBalanceSheetEntry) iter.next();

						entry.setTrans(transaction);

						logic.updateBeginningBalanceSheetEntryTrans(sessionid, IDBConstants.MODUL_ACCOUNTING, entry.getIndex(), entry);
					}
				}
			}


			JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "Transaction submission completed successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "Transaction submission failed!");
		}
	}
	private void initBalanceRoot()
	{
		AccountingBusinessLogic logic = new AccountingBusinessLogic(this.conn);
		try {
			balance = logic.getBeginningBalance(this.sessionid, IDBConstants.MODUL_ACCOUNTING, (short) 0);
			if (balance==null) balance = new BeginningBalance();
			entity2gui();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	void panelSave()
	{
		gui2entity();
		AccountingBusinessLogic logic = new AccountingBusinessLogic(this.conn);

		try {
			if (balance.getIndex()==0)
				logic.createBeginningBalance(sessionid, IDBConstants.MODUL_ACCOUNTING, balance);
			else
				logic.updateBeginningBalance(sessionid, IDBConstants.MODUL_ACCOUNTING, balance.getIndex(), balance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method initializes submitButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getSubmitButton() {
		if (submitButton == null) {
			submitButton = new JButton();
			submitButton.setText("Submit");
			submitButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doSubmit();
				}
			});
		}
		return submitButton;
	}
	/**
	 * This method initializes journalPicker
	 *
	 * @return pohaci.gumunda.titis.accounting.cgui.JournalPicker
	 */
	private JournalStandardPicker getJournalStandardPicker() {
		if (journalStandardPicker == null) {
			journalStandardPicker = new JournalStandardPicker();
		}
		return journalStandardPicker;
	}

}  //  @jve:decl-index=0:visual-constraint="20,10"
