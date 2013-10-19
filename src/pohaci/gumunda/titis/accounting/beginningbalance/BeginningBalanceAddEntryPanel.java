package pohaci.gumunda.titis.accounting.beginningbalance;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.titis.accounting.cgui.AccountPicker;
import pohaci.gumunda.titis.accounting.cgui.BankCellEditor;
import pohaci.gumunda.titis.accounting.cgui.CashCellEditor;
import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.CurrencyCellEditor;
import pohaci.gumunda.titis.accounting.cgui.CurrencyPicker;
import pohaci.gumunda.titis.accounting.cgui.CustomerCellEditor;
import pohaci.gumunda.titis.accounting.cgui.LoanCellEditor;
import pohaci.gumunda.titis.accounting.cgui.QuickPickComboModel;
import pohaci.gumunda.titis.accounting.cgui.RevTransactionPanel;
import pohaci.gumunda.titis.accounting.cgui.TableRowNumRenderer;
import pohaci.gumunda.titis.accounting.cgui.TableRowNumbererColumnClass;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.DblCellEditor;
import pohaci.gumunda.titis.application.DoubleAwareDefaultCellRenderer;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeeCellEditor;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.PartnerCellEditor;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.ProjectDataCellEditor;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;

public class BeginningBalanceAddEntryPanel extends JPanel implements PropertyChangeListener {
	static class FormatterFactory extends AbstractFormatterFactory {

		public AbstractFormatter getFormatter(JFormattedTextField tf) {
			return RevTransactionPanel.createNumberFormatA();
		}

	}
	private static final long serialVersionUID = 1L;
	private JComboBox comboTypeCode = null;
	private AccountPicker accountPicker = null;
	private JLabel accountLabel = null;
	private JLabel quickAccountLabel = null;
	private JLabel balanceSideLabel = null;
	private JLabel balanceSide = null;
	private JLabel jLabel = null;
	private CurrencyPicker currencyPicker = null;
	private JLabel currencyLabel = null;
	private JLabel valueLabe = null;
	private JFormattedTextField accValueFormattedTextField = null;
	private JPanel childPanel = null;
	private JButton saveButton = null;
	private JPanel buttonPanel = null;
	private JScrollPane tableScrollPane = null;
	private JTable table = null;
	private Double nol = new Double(0);
	/**
	 * This is the default constructor - for VE only
	 */
	public BeginningBalanceAddEntryPanel() {
		super();
		initialize();
		installTableColumnEditorRenderers();
		installNumberFormatter();
	}
	private void installNumberFormatter() {
		FormatterFactory factory = new FormatterFactory();
		accValueFormattedTextField.setFormatterFactory(factory);
		exchangeRateTextField.setFormatterFactory(factory);
		
	}
	long session;
	transient Connection conn;
	private JPanel childButtonPanel = null;
	private JButton childAddButton = null;
	private JButton childDelButton = null;
	private JTextField jTextField = null;
    transient BeginningBalanceSheetEntryPanel parentPanel;
	public BeginningBalanceAddEntryPanel(Connection conn, long session, BeginningBalanceSheetEntryPanel parentPanel)
    {
    	this.conn = conn;
    	this.session=session;
    	this.parentPanel = parentPanel;
    	initialize();
    	installNumberFormatter();
    	installTableColumnEditorRenderers();
        secondInit();	
    }
	private QuickPickComboModel quickPickComboModel;
    private void secondInit()
    {
    	accValueFormattedTextField.setValue(nol);
    	quickPickComboModel = new QuickPickComboModel(conn,session);
		comboTypeCode.setModel(quickPickComboModel);
		quickPickComboModel.addListDataListener(new ListDataListener()
		{

			public void contentsChanged(ListDataEvent e) {
				comboContentsChanged();
			}

			public void intervalAdded(ListDataEvent e) {
			}
			public void intervalRemoved(ListDataEvent e) {
			}
		});
		logic = new AccountingBusinessLogic(conn);
    }
	protected void comboContentsChanged() {
		if (quickPickComboModel==null)
			return;
		Object selected = quickPickComboModel.getSelectedItem();
		if (selected instanceof SubsidiaryAccountSetting)
		{
		 SubsidiaryAccountSetting setting = (SubsidiaryAccountSetting) selected;
		 accountPicker.setAccount(setting.getAccount());
		}
		
	}
	private void installTableColumnEditorRenderers() {
		JFrame mainFrame = pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame();
		table.setDefaultEditor(CashAccount.class,new CashCellEditor(mainFrame,conn,session));
		table.setDefaultEditor(Double.class, new DblCellEditor());
		table.setDefaultEditor(BankAccount.class,new BankCellEditor(mainFrame,conn,session));
		table.setDefaultEditor(Customer.class, new CustomerCellEditor(mainFrame,conn,session));
		table.setDefaultEditor(ProjectData.class, new ProjectDataCellEditor(mainFrame,conn,session));
		table.setDefaultEditor(Partner.class, new PartnerCellEditor(mainFrame,conn,session));
		table.setDefaultEditor(CompanyLoan.class, new LoanCellEditor(mainFrame,conn,session));//akutambahin
		table.setDefaultEditor(Employee.class, new EmployeeCellEditor(mainFrame,conn,session));
		table.setDefaultEditor(Currency.class, new CurrencyCellEditor(mainFrame,"Employee Receivable Currency" , conn,session));
		table.setDefaultRenderer(TableRowNumbererColumnClass.class,
		new TableRowNumRenderer());
		table.setDefaultRenderer(Double.class, new DoubleAwareDefaultCellRenderer());
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 7;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.anchor = GridBagConstraints.CENTER;
		gridBagConstraints2.insets = new Insets(0, 2, 0, 2);
		gridBagConstraints2.gridx = 6;
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 5;
		gridBagConstraints15.gridy = 7;
		labelExchangeRate = new JLabel();
		labelExchangeRate.setText("Exchange Rate");
		GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
		gridBagConstraints29.fill = GridBagConstraints.BOTH;
		gridBagConstraints29.gridx = 0;
		gridBagConstraints29.gridy = 1;
		gridBagConstraints29.weightx = 1.0;
		gridBagConstraints29.weighty = 1.0;
		gridBagConstraints29.gridwidth = 4;
		GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
		gridBagConstraints24.gridx = 4;
		gridBagConstraints24.gridwidth = 6;
		gridBagConstraints24.anchor = GridBagConstraints.EAST;
		gridBagConstraints24.gridy = 9;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 1;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.gridwidth = 9;
		gridBagConstraints21.insets = new Insets(2, 0, 0, 0);
		gridBagConstraints21.gridheight = 1;
		gridBagConstraints21.ipadx = 0;
		gridBagConstraints21.ipady = 0;
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.weighty = 1.0;
		gridBagConstraints21.gridy = 8;
		GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
		gridBagConstraints20.fill = GridBagConstraints.BOTH;
		gridBagConstraints20.gridy = 7;
		gridBagConstraints20.weightx = 1.0;
		gridBagConstraints20.anchor = GridBagConstraints.WEST;
		gridBagConstraints20.gridheight = 1;
		gridBagConstraints20.ipadx = 2;
		gridBagConstraints20.insets = new Insets(0, 2, 0, 2);
		gridBagConstraints20.gridx = 4;
		GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
		gridBagConstraints19.gridx = 1;
		gridBagConstraints19.anchor = GridBagConstraints.EAST;
		gridBagConstraints19.gridy = 7;
		valueLabe = new JLabel();
		valueLabe.setText("Amount");
		GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
		gridBagConstraints16.gridx = 1;
		gridBagConstraints16.anchor = GridBagConstraints.EAST;
		gridBagConstraints16.gridy = 6;
		currencyLabel = new JLabel();
		currencyLabel.setText("Currency");
		GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
		gridBagConstraints14.gridx = 4;
		gridBagConstraints14.anchor = GridBagConstraints.WEST;
		gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints14.gridy = 6;
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 3;
		gridBagConstraints13.ipadx = 5;
		gridBagConstraints13.ipady = 5;
		gridBagConstraints13.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("");
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 4;
		gridBagConstraints12.anchor = GridBagConstraints.WEST;
		gridBagConstraints12.gridy = 4;
		balanceSide = new JLabel();
		balanceSide.setText("Debit");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 1;
		gridBagConstraints11.anchor = GridBagConstraints.EAST;
		gridBagConstraints11.gridy = 4;
		balanceSideLabel = new JLabel();
		balanceSideLabel.setText("Balance Side");
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 1;
		gridBagConstraints10.anchor = GridBagConstraints.EAST;
		gridBagConstraints10.gridy = 0;
		quickAccountLabel = new JLabel();
		quickAccountLabel.setText("Quick pick");
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 1;
		gridBagConstraints9.anchor = GridBagConstraints.EAST;
		gridBagConstraints9.gridy = 2;
		accountLabel = new JLabel();
		accountLabel.setText("Account");
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 4;
		gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints8.gridy = 2;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 4;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		this.setSize(new Dimension(554, 373));
		this.add(getComboTypeCode(), gridBagConstraints);
		this.add(getAccountPicker(), gridBagConstraints8);
		this.add(accountLabel, gridBagConstraints9);
		this.add(quickAccountLabel, gridBagConstraints10);
		this.add(balanceSideLabel, gridBagConstraints11);
		this.add(balanceSide, gridBagConstraints12);
		this.add(jLabel, gridBagConstraints13);
		this.add(getCurrencyPicker(), gridBagConstraints14);
		this.add(currencyLabel, gridBagConstraints16);
		this.add(valueLabe, gridBagConstraints19);
		this.add(getAccValueFormattedTextField(), gridBagConstraints20);
		this.add(getChildPanel(), gridBagConstraints21);
		this.add(getButtonPanel(), gridBagConstraints24);
		this.add(labelExchangeRate, gridBagConstraints15);
		this.add(getExchangeRateTextField(), gridBagConstraints2);
	}

	/**
	 * This method initializes comboTypeCode	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getComboTypeCode() {
		if (comboTypeCode == null) {
			comboTypeCode = new JComboBox();
			comboTypeCode.setPreferredSize(new Dimension(200, 20));
			ComboBoxModel comboModel = new DefaultComboBoxModel(
					new String [] 
{ "-","Account Receivable", "Account Payable", "Cash Advance" }					
					);
			
			comboTypeCode.setModel(comboModel);
		}
		return comboTypeCode;
	}

	/**
	 * This method initializes accountPicker	
	 * 	
	 * @return pohaci.gumunda.titis.accounting.cgui.AccountPicker	
	 */
	private AccountPicker getAccountPicker() {
		if (accountPicker == null) {
			accountPicker = new AccountPicker(conn,session);
			accountPicker.addPropertyChangeListener("account", this);
		}
		return accountPicker;
	}

	/**
	 * This method initializes currencyPicker	
	 * 	
	 * @return pohaci.gumunda.titis.accounting.cgui.CurrencyPicker	
	 */
	private CurrencyPicker getCurrencyPicker() {
		if (currencyPicker == null) {
			currencyPicker = new CurrencyPicker(conn,session);
		}
		return currencyPicker;
	}

	/**
	 * This method initializes accValueFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
	private JFormattedTextField getAccValueFormattedTextField() {
		if (accValueFormattedTextField == null) {
			accValueFormattedTextField = new JFormattedTextField();
			accValueFormattedTextField.setPreferredSize(new Dimension(110, 20));
	
		}
		return accValueFormattedTextField;
	}

	/**
	 * This method initializes childPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getChildPanel() {
		if (childPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridx = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.weighty = 1.0;
			gridBagConstraints5.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = -1;
			childPanel = new JPanel();
			childPanel.setLayout(new GridBagLayout());
			childPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			childPanel.add(getChildButtonPanel(), gridBagConstraints3);
			childPanel.add(getTableScrollPane(), gridBagConstraints5);
			childPanel.add(getJTextField(), gridBagConstraints7);
		}
		return childPanel;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource()==this.accountPicker)
		{
			entity.setAccount(accountPicker.getAccount());
			balanceSide.setText(entity.getAccount().getBalanceAsString());
		}
		
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
					doSave();
				}
			});
		}
		return saveButton;
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getSaveButton(), gridBagConstraints1);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes tableScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTableScrollPane() {
		if (tableScrollPane == null) {
			tableScrollPane = new JScrollPane();
			tableScrollPane.setPreferredSize(new Dimension(453, 170));
			tableScrollPane.setViewportView(getTable());
		}
		return tableScrollPane;
	}

	/**
	 * This method initializes table	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			//table.setPreferredSize(new Dimension(375, 100));
		}
		return table;
	}
	/**
	 * This method initializes childButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getChildButtonPanel() {
		if (childButtonPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.CENTER;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.gridx = 0;
			childButtonPanel = new JPanel();
			childButtonPanel.setLayout(new GridBagLayout());
			childButtonPanel.add(getChildAddButton(), gridBagConstraints6);
			childButtonPanel.add(getChildDelButton(), new GridBagConstraints());
		}
		return childButtonPanel;
	}
	/**
	 * This method initializes childAddButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getChildAddButton() {
		if (childAddButton == null) {
			childAddButton = new JButton();
			childAddButton.setText("Add child");
			childAddButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addChild();
				}
			});
		}
		return childAddButton;
	}
	/**
	 * This method initializes childDelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getChildDelButton() {
		if (childDelButton == null) {
			childDelButton = new JButton();
			childDelButton.setText("Delete");
			childDelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					deleteChild();
				}
			});
		}
		return childDelButton;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
		}
		return jTextField;
	}
    transient BeginningBalanceSheetEntry entity = new BeginningBalanceSheetEntry();  
	public void setEntity(BeginningBalanceSheetEntry entry) {
		this.entity = new BeginningBalanceSheetEntry(entry);
		entity2gui();
	}
	short m_typeCode;
	void entity2gui()
	{
		accountPicker.setAccount(entity.getAccount());
		currencyPicker.setObject(entity.getCurrency());
		accValueFormattedTextField.setValue(new Double(entity.getAccValue()));
		exchangeRateTextField.setValue(new Double(entity.getExchangeRate()));
		m_typeCode = entity.getType();
		TableFormat aFormat;
		try {
			if (m_typeCode != 0)
			{
				aFormat = (TableFormat) entity.createChildTableFormat();
				childList.clear();
				setFormat(aFormat);
				childList.addAll(Arrays.asList(entity.getDetails()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading Beginning Balance details");
		}
		
	}
	void gui2entity()
	{
		entity.setAccount(accountPicker.getAccount());
		entity.setCurrency((Currency) currencyPicker.getObject());
		entity.setAccValue(convert(accValueFormattedTextField));
		entity.setType(m_typeCode);
		updateAccountInDetail();
		entity.setDetails((BeginningBalanceSheetDetail[]) childList.toArray( new BeginningBalanceSheetDetail[0]));
		Number exchangeRate = (Number) exchangeRateTextField.getValue();
		entity.setExchangeRate(exchangeRate.doubleValue());
	}
	
	void updateAccountInDetail()
	{
		int i;
		for (i=0; i< childList.size();i++)
		{
			BeginningBalanceSheetDetail detail = (BeginningBalanceSheetDetail) childList.get(i);
			if (detail.getAccount()==null || detail.getAccount().getIndex()==0)
				detail.setAccount(accountPicker.getAccount());
		}
	}
	double convert(JFormattedTextField jtf)
	{
		Number n = (Number) jtf.getValue();
		return n.doubleValue();
	}
	/*class TList extends TransformedList
	{
		protected TList(EventList source) {
			super(source);
			source.addListEventListener(this);
		}

		public void listChanged(ListEvent listChanges) {
			ListEvent copyOfIt = listChanges.copy();
			updates.forwardEvent(listChanges);
			while (copyOfIt.nextBlock())
		}
	
	}*/
	transient TableFormat lastFormat;
    transient EventList childList;
//	TransformedList transformedList;
	{
		childList = new BasicEventList();
//		transformedList = new TList(childList);
	}
	
	private transient EventSelectionModel eventSelectionModel;
    
	void setFormat(TableFormat aFormat)
	{
		if (lastFormat != null)
		{
			if (lastFormat.getClass() == aFormat.getClass())
				return;
		}
		if (childList.size()!=0)
		{
			// ask user to cancel
		}
		childList.clear();
		EventTableModel eventTableModel = new EventTableModel(childList, aFormat);
		table.setModel(eventTableModel);
		eventSelectionModel = new EventSelectionModel(childList);
		table.setSelectionModel(eventSelectionModel);
		lastFormat = aFormat;
	}
	transient AccountingBusinessLogic logic;
	private JLabel labelExchangeRate = null;
	private JFormattedTextField exchangeRateTextField = null;
	
	void addChild()
    {
    	gui2entity();
    	SubsidiaryAccountSetting settings = null;
    	try {
			settings = logic.getSubsidiaryAccountSettingByIndex(this.session, IDBConstants.MODUL_ACCOUNTING,
					entity.getAccount().getIndex()
				);
		if (settings==null)
			return;
		BeginningBalanceSheetDetail newChild=null;
		TableFormat newFormat=null;
		m_typeCode = (short) settings.getTranscode();
		entity.setType(m_typeCode);
		if (m_typeCode==0) return;
		newChild = (BeginningBalanceSheetDetail) entity.createChild();
		if (newChild!=null)
		{
			newFormat = (TableFormat) entity.createChildTableFormat();
			setFormat(newFormat);
			childList.add(newChild);
		} else
			JOptionPane.showMessageDialog(this, "No known subsidiary for this account");
    	} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
	void deleteChild()
	{
		if (eventSelectionModel!=null)
		{
			EventList selected = eventSelectionModel.getSelected();
			if (selected.size()>0)
			{
				int i;
				ArrayList selecteds = new ArrayList();
				selecteds.addAll(selected);
				for (i=0; i<selecteds.size();i++)
				{
					childList.remove(selecteds.get(i));
				}
			} else
				JOptionPane.showMessageDialog(this, "Which row do you want to delete?");
		}
	}
	void doSave()
	{
		TableCellEditor cellEditor = table.getCellEditor();
		if (cellEditor !=null)
			cellEditor.stopCellEditing();
		gui2entity();
		if (entity.getAccount()==null || entity.getAccount().getIndex()==0)
		{
			JOptionPane.showMessageDialog(this,"Account must not be empty");
			return;
		}
		if (entity.getCurrency()==null || entity.getCurrency().getIndex()==0)
		{
			JOptionPane.showMessageDialog(this,"Currency must not be null");
			return;
		}
		try{
			
		if (entity.getIndex()==0)
			logic.createBeginningBalanceSheetEntry(session, IDBConstants.MODUL_ACCOUNTING, entity);
		else
			logic.updateBeginningBalanceSheetEntry(session, IDBConstants.MODUL_ACCOUNTING, entity.getIndex(), entity);
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this,ex.getMessage());
		}
		parentPanel.reload();
		JOptionPane.showMessageDialog(BeginningBalanceAddEntryPanel.this,"Save completed.");
	}
	/**
	 * This method initializes exchangeRateTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getExchangeRateTextField() {
		if (exchangeRateTextField == null) {
			exchangeRateTextField = new JFormattedTextField(NumberFormat.getInstance());
			exchangeRateTextField.setValue(new Double(1));
			exchangeRateTextField.setHorizontalAlignment(JTextField.TRAILING);
		}
		return exchangeRateTextField;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"  
