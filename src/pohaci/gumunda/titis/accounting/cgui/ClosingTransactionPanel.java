/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui;

import javax.swing.JPanel;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.ClosingTransaction;
import pohaci.gumunda.titis.accounting.entity.ClosingTransactionDetail;
import pohaci.gumunda.titis.accounting.logic.TransactionPostingLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 * 
 */
public class ClosingTransactionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel northPanel = null;
	private JPanel centerPanel = null;
	private JPanel contentPanel = null;
	private JLabel periodLabel = null;
	private JLabel formLabel = null;
	private DatePicker fromDatePicker = null;
	private JLabel toLabel = null;
	private DatePicker toDatePicker = null;
	private JButton closeButton = null;
	private JScrollPane scrollPane = null;
	private JTable table = null;
	private JButton viewButton = null;
	private JButton historicalDetailButton = null;
	private JPanel northCenterPanel = null;
	private Connection connection = null;
	private long sessionId = -1;
	private DefaultTableModel dataModel;
	private int lastRow;
	private ClosingTransaction entity;
	private Currency baseCurrency;
	private JLabel unitLabel = null;
	private UnitPicker unitPicker = null;
	/**
	 * This is the default constructor
	 */
	public ClosingTransactionPanel(Connection connection, long sessionId) {
		super();
		this.connection = connection;
		this.sessionId = sessionId;
		initialize();
		initBaseCurrency(connection, sessionId);
	}

	private void initBaseCurrency(Connection connection, long sessionId) {
		baseCurrency = BaseCurrency.createBaseCurrency(connection, sessionId);
		baseCurrency.setIsBase(true);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(490, 251);
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(getNorthPanel(), java.awt.BorderLayout.NORTH);
		this.add(getCenterPanel(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * This method initializes northPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			northPanel = new JPanel();
			northPanel.setLayout(flowLayout);
			northPanel.add(getViewButton(), null);
			northPanel.add(getCloseButton(), null);
			northPanel.add(getHistoricalDetailButton(), null);
		}
		return northPanel;
	}

	/**
	 * This method initializes centerPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(getScrollPane(), java.awt.BorderLayout.CENTER);
			centerPanel.add(getNorthCenterPanel(), java.awt.BorderLayout.NORTH);
		}
		return centerPanel;
	}

	/**
	 * This method initializes contentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridy = 1;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.insets = new java.awt.Insets(3, 1, 0, 5);
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(5, 1, 0, 5);
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 1;
			unitLabel = new JLabel();
			unitLabel.setText("Unit");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(1, 5, 0, 5);
			gridBagConstraints4.gridx = 4;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 3;
			gridBagConstraints3.insets = new java.awt.Insets(1, 5, 0, 5);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 0;
			toLabel = new JLabel();
			toLabel.setText("To");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(1, 5, 0, 5);
			gridBagConstraints2.gridx = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints1.insets = new java.awt.Insets(1, 1, 0, 5);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.insets = new java.awt.Insets(1, 5, 0, 5);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			formLabel = new JLabel();
			formLabel.setText("From");
			periodLabel = new JLabel();
			periodLabel.setText("Closing Period");
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
					5, 5, 10, 5));
			contentPanel.add(periodLabel, gridBagConstraints1);
			contentPanel.add(formLabel, gridBagConstraints);
			contentPanel.add(getFromDatePicker(), gridBagConstraints2);
			contentPanel.add(toLabel, gridBagConstraints3);
			contentPanel.add(getToDatePicker(), gridBagConstraints4);
			contentPanel.add(unitLabel, gridBagConstraints11);
			contentPanel.add(getUnitPicker(), gridBagConstraints21);
		}
		return contentPanel;
	}

	/**
	 * This method initializes fromTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private DatePicker getFromDatePicker() {
		if (fromDatePicker == null) {
			fromDatePicker = new DatePicker();
			fromDatePicker.setPreferredSize(new java.awt.Dimension(110, 20));
			fromDatePicker.setDate(new Date());
		}
		return fromDatePicker;
	}

	/**
	 * This method initializes toTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private DatePicker getToDatePicker() {
		if (toDatePicker == null) {
			toDatePicker = new DatePicker();
			toDatePicker.setPreferredSize(new java.awt.Dimension(110, 20));
			toDatePicker.setDate(new Date());
		}
		return toDatePicker;
	}

	/**
	 * This method initializes closeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText("Close");
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doClose();
				}
			});
		}
		return closeButton;
	}

	private void doClose() {
		if (!isInputValid())
			return;

		if (!isAnyAccounts())
			return;

		if (!isUnit())
			return;
		
		if (!isNotConflicted())
			return;

		int response = JOptionPane
				.showConfirmDialog(
						this,
						"It will automatically post the closing transaction. Are you sure to contiune the process?",
						"Warning", JOptionPane.YES_NO_OPTION);
		if (response != JOptionPane.YES_OPTION)
			return;

		setEntity(new ClosingTransaction());
		gui2entity();

		doSave();
		doSubmit();
	}

	private boolean isNotConflicted() {
		Date periodFrom = getFromDatePicker().getDate();
		Date periodTo = getToDatePicker().getDate();
		
		java.sql.Date sqlPeriodFrom = utilToSqlDate(periodFrom);
		java.sql.Date sqlPeriodTo = utilToSqlDate(periodTo);
		
		GenericMapper mapper = MasterMap
			.obtainMapperFor(ClosingTransaction.class);
		mapper.setActiveConn(this.connection);
		
		String whereClause = "NOT ((PERIODFROM > ? AND PERIODFROM > ?) OR (PERIODTO < ? AND PERIODTO < ?))";
		Object[] params = new Object[]{
			sqlPeriodTo, sqlPeriodFrom, sqlPeriodFrom, sqlPeriodTo	
		};
		
		List list = mapper.doSelectWhere(whereClause, params);
		if(list.size()>0){
			JOptionPane.showMessageDialog(this, "The period has been closed");
			return false;
		}
		
		return true;
	}

	private java.sql.Date utilToSqlDate(Date date) {
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
	}

	private void doSubmit() {
		Date transactionDate = new Date();

		getEntity().isetBaseCurrency(this.baseCurrency);
		getEntity().setTransactionDate(transactionDate);
		try {
			getEntity().submit(sessionId, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		GenericMapper mapper = MasterMap
				.obtainMapperFor(ClosingTransaction.class);
		mapper.setActiveConn(this.connection);

		mapper.doUpdate(getEntity());

		Transaction transaction = getEntity().getTrans();
		TransactionPostingLogic logic = new TransactionPostingLogic(connection);
		try {
			logic.postOnly(sessionId, IDBConstants.MODUL_ACCOUNTING,
					transaction, new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isUnit() {
		if (getUnitPicker().getUnit() == null) {
			JOptionPane.showMessageDialog(this, "Please insert unit");
			return false;
		}
		return true;
	}

	private void doSave() {
		GenericMapper mapper = MasterMap
				.obtainMapperFor(ClosingTransaction.class);
		mapper.setActiveConn(this.connection);

		mapper.doInsert(getEntity());

		mapper = MasterMap.obtainMapperFor(ClosingTransactionDetail.class);
		mapper.setActiveConn(this.connection);

		ClosingTransactionDetail[] details = getEntity().getDetails();
		for (int i = 0; i < details.length; i++) {
			ClosingTransactionDetail detail = details[i];
			detail.setClosingTransaction(getEntity().getAutoindex());

			mapper.doInsert(detail);
		}
	}

	private boolean isAnyAccounts() {
		int rowIncomeSummary = getDataModel().getRowCount() - 3;
		if (rowIncomeSummary == 0) {
			JOptionPane.showMessageDialog(this,
					"There are no accounts to close");
			return false;
		}

		return true;
	}

	private void gui2entity() {
		getEntity().setPeriodFrom(getFromDatePicker().getDate());
		getEntity().setPeriodTo(getToDatePicker().getDate());
		getEntity().setUnit(getUnitPicker().getUnit());

		getEntity().setDetails(getDetails());
	}

	private ClosingTransactionDetail[] getDetails() {
		Vector vector = new Vector();
		int rowIncomeSummary = getDataModel().getRowCount() - 3;
		for (int i = 0; i < rowIncomeSummary; i++) {
			ClosingTransactionDetail detail = new ClosingTransactionDetail();

			detail.setCurrency(baseCurrency);
			detail.setExchangeRate(1);

			Account account = (Account) getDataModel().getValueAt(i, 1);
			double value = 0;
			byte balanceCode = 0;
			if (getDataModel().getValueAt(i, 2) != null) {
				Double val = (Double) getDataModel().getValueAt(i, 2);
				value = val.doubleValue();
				balanceCode = 0;
			} else {
				Double val = (Double) getDataModel().getValueAt(i, 3);
				value = val.doubleValue();
				balanceCode = 1;
			}

			detail.setAccount(account);
			detail.setAccValue(value);
			detail.setBalanceCode(balanceCode);

			vector.addElement(detail);
		}

		ClosingTransactionDetail[] details = (ClosingTransactionDetail[]) vector
				.toArray(new ClosingTransactionDetail[vector.size()]);

		return details;
	}

	/**
	 * This method initializes scrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}

	/**
	 * This method initializes table
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTable() {
		if (table == null) {
			table = new JTable() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public TableCellRenderer getCellRenderer(int row, int column) {
					if (row < getDataModel().getRowCount() - 1) {
						if (column > 1)
							return new FormattedDoubleCellRenderer(
									JLabel.RIGHT, Font.PLAIN);
						else
							return super.getCellRenderer(row, column);
					} else {
						if (column == 1)
							return new FormattedStandardCellRenderer(Font.BOLD,
									JLabel.RIGHT);
						else if (column > 1)
							return new FormattedDoubleCellRenderer(
									JLabel.RIGHT, Font.BOLD);
						else
							return super.getCellRenderer(row, column);
					}
				}

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setModel(getDataModel());
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			table.getColumnModel().getColumn(0).setPreferredWidth(70);
			table.getColumnModel().getColumn(1).setPreferredWidth(200);
			table.getColumnModel().getColumn(2).setPreferredWidth(120);
			table.getColumnModel().getColumn(3).setPreferredWidth(120);

			clearRows();
		}
		return table;
	}

	private DefaultTableModel getDataModel() {
		if (dataModel == null) {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("Account No");
			model.addColumn("Account Name");
			model.addColumn("Debit");
			model.addColumn("Credit");

			dataModel = model;
		}
		return dataModel;
	}

	/**
	 * This method initializes viewButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewButton() {
		if (viewButton == null) {
			viewButton = new JButton();
			viewButton.setText("View");
			viewButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doView();
				}
			});
		}
		return viewButton;
	}

	private void doView() {
		if (!isInputValid())
			return;

		clearRows();

		Date dateFrom = getFromDatePicker().getDate();
		Date dateTo = getToDatePicker().getDate();

		List list = ClosingTransaction.igetAccount(this.connection);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			account.isetStatus(Account.DESCRIPTION);

			BigDecimal valueBD = ClosingTransaction.igetDebitValue(
					this.connection, account, dateFrom, dateTo);
			double value = valueBD.doubleValue();
			if (value > 0) {
				addRow(new Object[] { account.getCode(), account, null,
						new Double(value) });
			} else if (value < 0) {
				value = -value;
				addRow(new Object[] { account.getCode(), account,
						new Double(value), null });
			}
		}

		setIncomeSummary();
		setBalance();
	}

	private void setBalance() {
		int rowBalance = getDataModel().getRowCount() - 1;
		double debit = getTotalValue(rowBalance - 1, 2);
		double credit = getTotalValue(rowBalance - 1, 3);

		getDataModel().setValueAt(new Double(debit), rowBalance, 2);
		getDataModel().setValueAt(new Double(credit), rowBalance, 3);
	}

	private void setIncomeSummary() {
		int rowIncomeSummary = getDataModel().getRowCount() - 3;
		double debit = getTotalValue(rowIncomeSummary - 1, 2);
		double credit = getTotalValue(rowIncomeSummary - 1, 3);
		double balance = debit - credit;

		if (balance > 0) { // debit > credit
			getDataModel().setValueAt(new Double(balance), rowIncomeSummary, 3);
		} else {
			getDataModel()
					.setValueAt(new Double(-balance), rowIncomeSummary, 2);
		}
	}

	private double getTotalValue(int maxRow, int col) {
		double val = 0;
		for (int i = 0; i < maxRow + 1; i++) {
			Object obj = getDataModel().getValueAt(i, col);
			if (obj instanceof Double) {
				Double dbl = (Double) obj;
				val += dbl.doubleValue();
			}
		}
		return val;
	}

	private boolean isInputValid() {
		if ((getFromDatePicker().getDate() == null)
				|| (getToDatePicker().getDate() == null)) {
			JOptionPane.showMessageDialog(this, "Please insert closing period");
			return false;
		}

		return true;
	}

	private void addRow(Object[] objects) {
		int oldRow = lastRow;
		lastRow++;
		getDataModel().insertRow(oldRow, objects);
	}

	private void clearRows() {
		getDataModel().setRowCount(0);

		getDataModel().addRow(
				new Object[] { null, "INCOME SUMMARY", new Double(0),
						new Double(0) });
		getDataModel().addRow(new Object[] { null, null, null, null });
		getDataModel().addRow(
				new Object[] { null, "TOTAL", new Double(0), new Double(0) });

		lastRow = 0;
	}

	/**
	 * This method initializes historicalDetailButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getHistoricalDetailButton() {
		if (historicalDetailButton == null) {
			historicalDetailButton = new JButton();
			historicalDetailButton.setText("Historical Details");
			historicalDetailButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							doViewHistoricalDetails();
						}
					});
		}
		return historicalDetailButton;
	}

	private void doViewHistoricalDetails() {
		HistoricalClosingTransactionPanel dlg = new HistoricalClosingTransactionPanel(
				GumundaMainFrame.getMainFrame(),
				"Historical Closing Transaction", this.connection);
		dlg.setVisible(true);
	}

	/**
	 * This method initializes northCenterPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNorthCenterPanel() {
		if (northCenterPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			northCenterPanel = new JPanel();
			northCenterPanel.setLayout(flowLayout1);
			northCenterPanel.add(getContentPanel(), null);
		}
		return northCenterPanel;
	}

	private ClosingTransaction getEntity() {
		return entity;
	}

	private void setEntity(ClosingTransaction entity) {
		this.entity = entity;
	}

	/**
	 * This method initializes unitTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private UnitPicker getUnitPicker() {
		if (unitPicker == null) {
			unitPicker = new UnitPicker(connection, sessionId);
		}
		return unitPicker;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
