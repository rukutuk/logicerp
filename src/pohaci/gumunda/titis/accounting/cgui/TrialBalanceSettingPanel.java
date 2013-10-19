/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.TrialBalanceAccountTypeSetting;
import pohaci.gumunda.titis.accounting.entity.TrialBalanceJournalTypeSetting;
import pohaci.gumunda.titis.accounting.logic.TrialBalanceSettingBusinessLogic;

/**
 * @author dark-knight
 *
 */
public class TrialBalanceSettingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel centerPanel = null;
	private JScrollPane journalScrollPane = null;
	private JTable journalTable = null;
	private JScrollPane accountScrollPane = null;
	private JTable accountTable = null;
	private JPanel southPanel = null;
	private JButton editButton = null;
	private JButton saveButton = null;
	private JButton cancelButton = null;
	
	private Connection connection;
	private long sessionId = -1;
	
	
	/**
	 * This is the default constructor
	 */
	public TrialBalanceSettingPanel(Connection connection, long sessionId) {
		super();
		this.connection = connection;
		this.sessionId = sessionId;

		initialize();
		setInEditMode(false);
		loadData();
	}

	private void loadData() {
		TrialBalanceSettingBusinessLogic logic =
			new TrialBalanceSettingBusinessLogic(this.connection, this.sessionId);
		
		List journalList = logic.getTrialBalanceJournalTypeSettingList();
		List accountList = logic.getTrialBalanceAccountTypeSettingList();
		List journal = logic.getJournalList();
		
		viewInJournalTable(journalList, journal);
		viewInAccountTable(accountList);
	}

	private void viewInAccountTable(List accountList) {
		((AttributeSettingTable)accountTable).clearRows();
		
		String[] categories = Account.getCategories();
		
		// buat hashtable
		Hashtable table = new Hashtable();
		for(int i = 0; i < categories.length; i++){
			List list = new ArrayList();
			table.put(new Long(i), list);
		}
		
		// masukkan ke hashtable
		Iterator iterator = accountList.iterator();
		while(iterator.hasNext()){
			TrialBalanceAccountTypeSetting setting = 
				(TrialBalanceAccountTypeSetting) iterator.next();
			
			// cari key di hashtable
			Long key = new Long(setting.getCategory());
			if(table.containsKey(key)){
				// ambil listnya
				List list = (List) table.get(key);
				// tambahkan yang baru
				list.add(setting);
			}
		}
		
		// baca lagi dan gambarkan di table
		for(int i = 0; i < categories.length; i++){
			String category = categories[i];
			
			List list = (List) table.get(new Long(i));
			Boolean falseBool = new Boolean(false);
			Object[] objects = new Object[]{category, falseBool, falseBool, falseBool, falseBool, falseBool};
			List resultList = Arrays.asList(objects);
			
			Iterator iter = list.iterator();
			while(iter.hasNext()){
				TrialBalanceAccountTypeSetting setting =
					(TrialBalanceAccountTypeSetting) iter.next();
				
				if(setting.getColumnName().equalsIgnoreCase(TrialBalanceAccountTypeSetting.BEGINNING_BALANCE)){
					resultList.set(1, new Boolean(setting.isUsed()));
				}else if(setting.getColumnName().equalsIgnoreCase(TrialBalanceAccountTypeSetting.TRANSACTION)){
					resultList.set(2, new Boolean(setting.isUsed()));
				}else if(setting.getColumnName().equalsIgnoreCase(TrialBalanceAccountTypeSetting.UNADJUSTED)){
					resultList.set(3, new Boolean(setting.isUsed()));
				}else if(setting.getColumnName().equalsIgnoreCase(TrialBalanceAccountTypeSetting.ADJUSTMENT)){
					resultList.set(4, new Boolean(setting.isUsed()));
				}else if(setting.getColumnName().equalsIgnoreCase(TrialBalanceAccountTypeSetting.ADJUSTED)){
					resultList.set(5, new Boolean(setting.isUsed()));
				}
			}
			
			objects =  (Object[]) resultList.toArray(new Object[resultList.size()]);
			((AttributeSettingTable)accountTable).addRow(objects);
		}
	}

	private void viewInJournalTable(List journalList, List journals) {
		((AttributeSettingTable)journalTable).clearRows();
		
		// buat hashtable
		Hashtable table = new Hashtable();
		Iterator iterator = journals.iterator();
		while(iterator.hasNext()){
			Journal journal = (Journal) iterator.next();
			
			List list = new ArrayList();
			table.put(new Long(journal.getIndex()), list);
		}
		
		// masukkan ke hashtable
		iterator = journalList.iterator();
		while(iterator.hasNext()){
			TrialBalanceJournalTypeSetting setting = 
				(TrialBalanceJournalTypeSetting) iterator.next();
			
			// cari key di hashtable
			Long key = new Long(setting.getJournal().getIndex());
			if(table.containsKey(key)){
				// ambil listnya
				List list = (List) table.get(key);
				// tambahkan yang baru
				list.add(setting);
			}
		}
		
		// baca lagi dan gambarkan di table
		iterator = journals.iterator();
		while(iterator.hasNext()){
			Journal journal = (Journal) iterator.next();
			
			List list = (List) table.get(new Long(journal.getIndex()));
			Boolean falseBool = new Boolean(false);
			Object[] objects = new Object[]{journal, falseBool, falseBool, falseBool, falseBool, falseBool};
			List resultList = Arrays.asList(objects);
			
			Iterator iter = list.iterator();
			while(iter.hasNext()){
				TrialBalanceJournalTypeSetting setting =
					(TrialBalanceJournalTypeSetting) iter.next();
				
				if(setting.getColumnName().equalsIgnoreCase(TrialBalanceJournalTypeSetting.BEGINNING_BALANCE)){
					resultList.set(1, new Boolean(setting.isUsed()));
				}else if(setting.getColumnName().equalsIgnoreCase(TrialBalanceJournalTypeSetting.TRANSACTION)){
					resultList.set(2, new Boolean(setting.isUsed()));
				}else if(setting.getColumnName().equalsIgnoreCase(TrialBalanceJournalTypeSetting.UNADJUSTED)){
					resultList.set(3, new Boolean(setting.isUsed()));
				}else if(setting.getColumnName().equalsIgnoreCase(TrialBalanceJournalTypeSetting.ADJUSTMENT)){
					resultList.set(4, new Boolean(setting.isUsed()));
				}else if(setting.getColumnName().equalsIgnoreCase(TrialBalanceJournalTypeSetting.ADJUSTED)){
					resultList.set(5, new Boolean(setting.isUsed()));
				}
			}
			
			objects =  (Object[]) resultList.toArray(new Object[resultList.size()]);
			((AttributeSettingTable)journalTable).addRow(objects);
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(557, 374);
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
		this.add(getCenterPanel(), java.awt.BorderLayout.CENTER);
		this.add(getSouthPanel(), java.awt.BorderLayout.SOUTH);
	}

	/**
	 * This method initializes centerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(2);
			gridLayout.setVgap(5);
			centerPanel = new JPanel();
			centerPanel.setLayout(gridLayout);
			centerPanel.add(getJournalScrollPane(), null);
			centerPanel.add(getAccountScrollPane(), null);
		}
		return centerPanel;
	}

	/**
	 * This method initializes journalScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJournalScrollPane() {
		if (journalScrollPane == null) {
			journalScrollPane = new JScrollPane();
			journalScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Journal Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), null));
			journalScrollPane.setViewportView(getJournalTable());
		}
		return journalScrollPane;
	}

	/**
	 * This method initializes journalTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJournalTable() {
		if (journalTable == null) {
			journalTable = new AttributeSettingTable();
		}
		return journalTable;
	}

	/**
	 * This method initializes accountScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getAccountScrollPane() {
		if (accountScrollPane == null) {
			accountScrollPane = new JScrollPane();
			accountScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Account Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), null));
			accountScrollPane.setViewportView(getAccountTable());
		}
		return accountScrollPane;
	}

	/**
	 * This method initializes accountTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getAccountTable() {
		if (accountTable == null) {
			accountTable = new AttributeSettingTable();
		}
		return accountTable;
	}

	/**
	 * This method initializes southPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSouthPanel() {
		if (southPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			southPanel = new JPanel();
			southPanel.setLayout(flowLayout);
			southPanel.add(getEditButton(), null);
			southPanel.add(getSaveButton(), null);
			southPanel.add(getCancelButton(), null);
		}
		return southPanel;
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
					onEdit();
				}
			});
		}
		return editButton;
	}

	protected void onEdit() {
		setInEditMode(true);
	}

	private void setInEditMode(boolean isInEditMode) {
		journalTable.setEnabled(isInEditMode);
		accountTable.setEnabled(isInEditMode);
		editButton.setEnabled(!isInEditMode);
		saveButton.setEnabled(isInEditMode);
		cancelButton.setEnabled(isInEditMode);
	}

	/**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("Save");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onSave();
				}
			});
		}
		return saveButton;
	}

	protected void onSave() {
		stopCellEditing();
		doSave();
		loadData();
		setInEditMode(false);
	}

	private void stopCellEditing() {
		((AttributeSettingTable)journalTable).stopCellEditing();
		((AttributeSettingTable)accountTable).stopCellEditing();
	}

	private void doSave() {
		List journalList = getJournalTypeSettingList();
		List accountList = getAccountTypeSettingList();
		
		TrialBalanceSettingBusinessLogic logic = new TrialBalanceSettingBusinessLogic(connection, sessionId);
		logic.saveTrialBalanceJournalTypeSettingList(journalList);
		logic.saveTrialBalanceAccountTypeSettingList(accountList);
	}

	private List getAccountTypeSettingList() {
		List result = new ArrayList();
		
		int rows = accountTable.getRowCount();
		for(int i = 0; i < rows; i++){
			long category = i;
			
			boolean isUsed;
			TrialBalanceAccountTypeSetting setting;
			String columnName;
			
			// 1
			isUsed = ((Boolean)accountTable.getValueAt(i, 1)).booleanValue();
			columnName = TrialBalanceAccountTypeSetting.BEGINNING_BALANCE;
			setting = createNewAccountTypeSetting(category, columnName, isUsed);
			result.add(setting);
			
			// 2
			isUsed = ((Boolean)accountTable.getValueAt(i, 2)).booleanValue();
			columnName = TrialBalanceAccountTypeSetting.TRANSACTION;
			setting = createNewAccountTypeSetting(category, columnName, isUsed);
			result.add(setting);
			
			// 3
			isUsed = ((Boolean)accountTable.getValueAt(i, 3)).booleanValue();
			columnName = TrialBalanceAccountTypeSetting.UNADJUSTED;
			setting = createNewAccountTypeSetting(category, columnName, isUsed);
			result.add(setting);

			// 4
			isUsed = ((Boolean)accountTable.getValueAt(i, 4)).booleanValue();
			columnName = TrialBalanceAccountTypeSetting.ADJUSTMENT;
			setting = createNewAccountTypeSetting(category, columnName, isUsed);
			result.add(setting);
			
			// 5
			isUsed = ((Boolean)accountTable.getValueAt(i, 5)).booleanValue();
			columnName = TrialBalanceAccountTypeSetting.ADJUSTED;
			setting = createNewAccountTypeSetting(category, columnName, isUsed);
			result.add(setting);
		}
		
		return result;
	}

	private TrialBalanceAccountTypeSetting createNewAccountTypeSetting(long category, String columnName, boolean isUsed) {
		TrialBalanceAccountTypeSetting setting = 
			new TrialBalanceAccountTypeSetting();
		
		setting.setCategory(category);
		setting.setColumnName(columnName);
		setting.setUsed(isUsed);
		
		return setting;
	}

	private List getJournalTypeSettingList() {
		List result = new ArrayList();
		
		int rows = journalTable.getRowCount();
		for(int i = 0; i < rows; i++){
			Journal journal = (Journal) journalTable.getValueAt(i, 0);
			
			boolean isUsed;
			TrialBalanceJournalTypeSetting setting;
			String columnName;
			
			// 1
			isUsed = ((Boolean)journalTable.getValueAt(i, 1)).booleanValue();
			columnName = TrialBalanceJournalTypeSetting.BEGINNING_BALANCE;
			setting = createNewJournalTypeSetting(journal, columnName, isUsed);
			result.add(setting);
			
			// 2
			isUsed = ((Boolean)journalTable.getValueAt(i, 2)).booleanValue();
			columnName = TrialBalanceJournalTypeSetting.TRANSACTION;
			setting = createNewJournalTypeSetting(journal, columnName, isUsed);
			result.add(setting);
			
			// 3
			isUsed = ((Boolean)journalTable.getValueAt(i, 3)).booleanValue();
			columnName = TrialBalanceJournalTypeSetting.UNADJUSTED;
			setting = createNewJournalTypeSetting(journal, columnName, isUsed);
			result.add(setting);

			// 4
			isUsed = ((Boolean)journalTable.getValueAt(i, 4)).booleanValue();
			columnName = TrialBalanceJournalTypeSetting.ADJUSTMENT;
			setting = createNewJournalTypeSetting(journal, columnName, isUsed);
			result.add(setting);
			
			// 5
			isUsed = ((Boolean)journalTable.getValueAt(i, 5)).booleanValue();
			columnName = TrialBalanceJournalTypeSetting.ADJUSTED;
			setting = createNewJournalTypeSetting(journal, columnName, isUsed);
			result.add(setting);
		}
		
		return result;
	}

	private TrialBalanceJournalTypeSetting createNewJournalTypeSetting(Journal journal, String columnName, boolean isUsed) {
		TrialBalanceJournalTypeSetting setting = 
			new TrialBalanceJournalTypeSetting();
		
		setting.setJournal(journal);
		setting.setColumnName(columnName);
		setting.setUsed(isUsed);
		
		return setting;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onCancel();
				}
			});
		}
		return cancelButton;
	}

	protected void onCancel() {
		loadData();
		setInEditMode(false);
	}
	
	private static class AttributeSettingTable extends JTable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public AttributeSettingTable(){
			DefaultTableModel model = new DefaultTableModel();
			
			model.addColumn("Attribute");
			model.addColumn("Beginning Balance");
			model.addColumn("On Period Transaction");
			model.addColumn("Unadjusted Balance");
			model.addColumn("Adjustments");
			model.addColumn("Adjusted Balance");
			
			setModel(model);
			
			clearRows();
		}

		public void clearRows() {
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
		}

		public boolean isCellEditable(int row, int column) {
			if(column==0)
				return false;
			return true;
		}

		public TableCellRenderer getCellRenderer(int row, int column) {
			if(column==0)
				return new BaseTableCellRenderer();
			return super.getDefaultRenderer(Boolean.class);
		}

		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}
		
		public void addRow(Object[] objects){
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.addRow(objects);
		}

		public TableCellEditor getCellEditor(int row, int column) {
			if(column==0)
				return super.getCellEditor();
			return super.getDefaultEditor(Boolean.class);
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
