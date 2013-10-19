package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import pohaci.gumunda.cgui.DoubleCellRenderer;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.TransactionPostingLogic;
import pohaci.gumunda.titis.accounting.logic.TransactionStateReversalLogic;
import pohaci.gumunda.titis.accounting.logic.TransactionVoidLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

public class PostingPanel extends JPanel implements ActionListener,
		PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	Connection conn = null;

	long sessionid = -1;

	JButton postingButton, viewButton, closeButton;

	JournalStandardPicker journalStandardPicker;

	JTextField descriptionTextField;

	UnitPicker unitPicker;

	DatePicker dateFromPicker, dateToPicker;

	JComboBox statusComboBox;

	TransactionTable table;

	ArrayList rendererRowList;

	ArrayList editorRowList;

	ArrayList totalRowList;

	ArrayList unBalanceRowList;

	static public final boolean SHOW_IN_BASE_CURRENCY = true;

	private Currency baseCurrency;

	private JPanel itself = this;

	private JPopupMenu reversePopupMenu;

	private JMenuItem reverseMenuItem;

	private JPopupMenu voidPopupMenu;

	private JMenuItem voidMenuItmm;

	private JTextField filterTextField;

	private JButton filterButton;

	private BasicEventList eventList;

	private FilterList filterList;

	private static final boolean showVoid = true;

	public PostingPanel(Connection conn, long sessionid) {
		this.conn = conn;
		this.sessionid = sessionid;
		constructComponent();
		baseCurrency = BaseCurrency.createBaseCurrency(this.conn,
				this.sessionid);
		prepareDefaultFromValue();
	}

	private void prepareDefaultFromValue() {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		Date date = calendar.getTime();
		dateFromPicker.setDate(date);
		dateToPicker.setDate(date);
	}

	private void constructComponent() {
		reversePopupMenu = new JPopupMenu();
		reverseMenuItem = new JMenuItem("Revert State");
		reverseMenuItem.addActionListener(this);
		reversePopupMenu.add(reverseMenuItem);

		voidPopupMenu = new JPopupMenu();
		voidMenuItmm = new JMenuItem("Void Transaction");
		voidMenuItmm.addActionListener(this);
		voidPopupMenu.add(voidMenuItmm);

		postingButton = new JButton("Post");
		postingButton.addActionListener(this);
		viewButton = new JButton("View");
		viewButton.addActionListener(this);

		journalStandardPicker = new JournalStandardPicker(conn, sessionid);
		journalStandardPicker
				.addPropertyChangeListener("journalStandard", this);
		unitPicker = new UnitPicker(conn, sessionid);
		dateFromPicker = new DatePicker();
		dateToPicker = new DatePicker();
		descriptionTextField = new JTextField();
		descriptionTextField.setPreferredSize(new Dimension(200, 18));
		descriptionTextField.setEditable(false);
		statusComboBox = new JComboBox(new String[] { "Submitted", "Posted",
				"All" });
		statusComboBox.setPreferredSize(new Dimension(200, 18));

		JPanel topPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel formPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel middlePanel = new JPanel();
		JPanel tablePanel = new JPanel();
		JPanel datePanel = new JPanel();
		JPanel filterPanel = new JPanel();

		northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(viewButton);
		topPanel.add(postingButton);

		middlePanel.setLayout(new GridBagLayout());
		formPanel.setLayout(new GridBagLayout());
		filterPanel.setLayout(new GridBagLayout());

		// button panel
		northPanel.add(formPanel);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		Misc.setGridBagConstraints(formPanel, topPanel, gridBagConstraints, 0,
				0, GridBagConstraints.HORIZONTAL, 2, 1, 1.0, 1.0, new Insets(1,
						1, 1, 1));

		Misc.setGridBagConstraints(formPanel, middlePanel, gridBagConstraints,
				0, 1, GridBagConstraints.REMAINDER, 2, 1, 1.0, 1.0, new Insets(
						1, 1, 1, 1));

		gridBagConstraints = new GridBagConstraints();
		Misc.setGridBagConstraints(middlePanel, new JLabel("Code"),
				gridBagConstraints, 0, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(5, 3, 1, 1));

		Misc.setGridBagConstraints(middlePanel, new JLabel(" "),
				gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(5, 1, 1, 1));

		Misc.setGridBagConstraints(middlePanel, journalStandardPicker,
				gridBagConstraints, 2, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				3.0, 0.0, new Insets(5, 1, 1, 1));

		Misc.setGridBagConstraints(middlePanel, new JLabel("Description"),
				gridBagConstraints, 0, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(middlePanel, new JLabel(" "),
				gridBagConstraints, 1, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(middlePanel, descriptionTextField,
				gridBagConstraints, 2, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				3.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(middlePanel, new JLabel("Unit Code"),
				gridBagConstraints, 0, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 3, 1, 1));

		Misc.setGridBagConstraints(middlePanel, new JLabel(" "),
				gridBagConstraints, 1, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(middlePanel, unitPicker, gridBagConstraints,
				2, 2, GridBagConstraints.HORIZONTAL, 1, 1, 3.0, 0.0,
				new Insets(1, 1, 1, 1));

		// --> divider

		Misc.setGridBagConstraints(middlePanel, new JLabel(" "),
				gridBagConstraints, 3, 0, GridBagConstraints.HORIZONTAL, 1, 3,
				1.0, 1.0, new Insets(5, 1, 1, 1));

		datePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JPanel detailDatePanel = new JPanel();
		detailDatePanel.setLayout(new GridBagLayout());
		datePanel.add(detailDatePanel);

		gridBagConstraints = new GridBagConstraints();

		Misc.setGridBagConstraints(detailDatePanel, new JLabel("From"),
				gridBagConstraints, 0, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(detailDatePanel, new JLabel(" "),
				gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(detailDatePanel, dateFromPicker,
				gridBagConstraints, 2, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				3.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(detailDatePanel, new JLabel(" "),
				gridBagConstraints, 3, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(detailDatePanel, new JLabel("To"),
				gridBagConstraints, 4, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(detailDatePanel, new JLabel(" "),
				gridBagConstraints, 5, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(detailDatePanel, dateToPicker,
				gridBagConstraints, 6, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				3.0, 0.0, new Insets(1, 1, 1, 1));

		detailDatePanel.setPreferredSize(new Dimension(280, 20));

		datePanel.setBorder(BorderFactory
				.createTitledBorder("Transaction Date"));

		gridBagConstraints = new GridBagConstraints();
		Misc.setGridBagConstraints(middlePanel, datePanel, gridBagConstraints,
				4, 0, GridBagConstraints.HORIZONTAL, 3, 2, 0.0, 0.0,
				new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(middlePanel, new JLabel("Status"),
				gridBagConstraints, 4, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 7, 1, 1));

		Misc.setGridBagConstraints(middlePanel, new JLabel(" "),
				gridBagConstraints, 5, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 7, 1, 7));

		Misc.setGridBagConstraints(middlePanel, statusComboBox,
				gridBagConstraints, 6, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 7));

		setLayout(new BorderLayout());

		JLabel filterLabel = new JLabel("Filter: ");
		filterTextField = new JTextField();
		filterTextField.setSize(250, filterTextField.getHeight());

		filterButton = new JButton("Filter");
		filterButton.addActionListener(this);

		gridBagConstraints = new GridBagConstraints();
		Misc.setGridBagConstraints(filterPanel, filterLabel,
				gridBagConstraints, 0, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(filterPanel, filterTextField,
				gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				2.0, 0.0, new Insets(1, 1, 1, 1));

		Misc.setGridBagConstraints(filterPanel, filterButton,
				gridBagConstraints, 2, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 1, 1));



		table = new TransactionTable();
		table.addMouseListener(new PostingMouseListener());
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createEtchedBorder());
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

		centerPanel.setLayout(new BorderLayout());
		TitledBorder border = BorderFactory.createTitledBorder("Journal List");
/*		Font font = border.getTitleFont();
		font = font.deriveFont(Font.BOLD);
		border.setTitleFont(font);*/
		centerPanel.setBorder(border);
		centerPanel.add(filterPanel, BorderLayout.NORTH);
		centerPanel.add(tablePanel, BorderLayout.CENTER);

		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.viewButton) {
			doView();
		} else if (e.getSource() == this.postingButton) {
			doPost();
			doView();
		} else if (e.getSource() == this.reverseMenuItem) {
			doRevert();
			doView();
		} else if (e.getSource() == this.voidMenuItmm) {
			doVoid();
			doView();
		} else if (e.getSource() == this.filterButton) {
			doFilter();
		}
	}

	private void doFilter() {
		table.clear();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		doViewTransactionInTable(model, filterList);
	}

	private void doPost() {
		// get the Transaction List to post
		ArrayList transactionToPostList = new ArrayList();
		if (editorRowList.size() > 0) {
			Iterator iterator = editorRowList.iterator();
			while (iterator.hasNext()) {
				int row = ((Integer) iterator.next()).intValue();
				Boolean isPosted = (Boolean) table.getValueAt(row, 1); // to
				// post
				// or
				// not
				if (isPosted.booleanValue()) {
					Transaction transactionToPost = (Transaction) table
							.getValueAt(row, 6);
					transactionToPostList.add(transactionToPost);
				}
			}
		}

		if (!checkBalance(transactionToPostList))
			return;

		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		Date postingDate = calendar.getTime();

		int numPosted = 0;
		if (transactionToPostList.size() > 0) {
			Iterator iterator = transactionToPostList.iterator();
			while (iterator.hasNext()) {
				Transaction trans = (Transaction) iterator.next();
				if (trans.getStatus() == 2) {
					TransactionPostingLogic logic = new TransactionPostingLogic(
							conn);
					try {
						logic.post(sessionid, IDBConstants.MODUL_ACCOUNTING,
								trans, postingDate);
						numPosted++;
					} catch (Exception e) {
						JOptionPane.showMessageDialog(this, e.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}

				}
			}
			if (numPosted > 0)
				JOptionPane.showMessageDialog(this, "Successfully post "
						+ numPosted + " transaction(s)", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this,
						"Unsuccesfully post any transactions", "Error",
						JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this,
					"There is no transaction to post.", "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}

	private boolean checkBalance(ArrayList transactionToPostList) {
		int errorNum = 0;
		if (transactionToPostList.size() > 0) {
			Iterator iterator = transactionToPostList.iterator();
			while (iterator.hasNext()) {
				Transaction transaction = (Transaction) iterator.next();
				if (transaction.getStatus() == 2) {
					if (!isBalance(transaction)) {
						errorNum++;
					}
				}
			}
		}
		if (errorNum > 0) {
			JOptionPane.showMessageDialog(this,
					"There is one or more unbalance transaction.");
			return false;
		}
		return true;
	}

	private boolean isBalance(Transaction transaction) {
		TransactionDetail[] details = transaction.getTransactionDetail();
		List list = Arrays.asList(details);

		double[] total = { 0, 0 };
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			TransactionDetail detail = (TransactionDetail) iterator.next();

			total = getTotalValue(total, detail);
		}

		NumberRounding nr = new NumberRounding(
				NumberRounding.NUMBERROUNDING_ROUND, 2);
		total[0] = nr.round(total[0]);
		total[1] = nr.round(total[1]);
		if (total[0] == total[1])
			return true;

		return false;
	}

	private void doView() {
		ArrayList list = new ArrayList();
		JournalStandard journalStandard = journalStandardPicker
				.getJournalStandard();
		Unit unit = unitPicker.getUnit();
		int status = statusComboBox.getSelectedIndex() + 2;
		Date dateFrom = dateFromPicker.getDate();
		if (dateFrom == null)
			list.add("Transaction Date From");
		Date dateTo = dateToPicker.getDate();
		if (dateTo == null)
			list.add("Transaction Date To");
		String strexc = "Please insert :\n";
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if (exception.length > 0) {
			for (int i = 0; i < exception.length; i++)
				strexc += exception[i] + "\n";
			JOptionPane.showMessageDialog(this, strexc, "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		table.clear();

		DefaultTableModel model = (DefaultTableModel) table.getModel();

		ArrayList transactionList = (ArrayList) getTransactionList(
				journalStandard, status, dateFrom, dateTo, unit);



		TextFilterator filterator = new TextFilterator() {

			public void getFilterStrings(List list, Object obj) {
				Transaction trans = (Transaction) obj;
				list.add(trans.getReference());
				list.add(trans.getDescription());
				list.add(trans.getJournalStandard().getCode());
				list.add(trans.getUnit().toString());
				list.add(trans.getTransDate().toString());
			}

		};


		eventList = new BasicEventList();
		eventList.addAll(transactionList);


		TextComponentMatcherEditor matcherEditor = new TextComponentMatcherEditor(filterTextField, filterator);
		filterList = new FilterList(eventList, matcherEditor);

		doViewTransactionInTable(model, filterList);
	}

	private void doViewTransactionInTable(DefaultTableModel model,
			FilterList transactionList) {
		int i = 1;
		int row = 0;
		Iterator iter = transactionList.iterator();
		rendererRowList = new ArrayList();
		editorRowList = new ArrayList();
		totalRowList = new ArrayList();
		unBalanceRowList = new ArrayList();
		while (iter.hasNext()) {
			Transaction originalTransaction = (Transaction) iter.next();

			Transaction transaction = null;
			try {
				transaction = (Transaction) originalTransaction.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

			if (transaction != null) {
				// view the transaction
				model.addRow(prepareTransactionDataForView(i, originalTransaction));
				rendererRowList.add(new Integer(row));
				if (transaction.getStatus() == 2) {
					editorRowList.add(new Integer(row));
				}
				row++;

				/*Transaction cloneTransaction = null;
				try {
					cloneTransaction = (Transaction) transaction.clone();
				} catch (CloneNotSupportedException e) {
					//
					e.printStackTrace();
				}*/

				TransactionDetail[] details = transaction
						.getTransactionDetail();
				List detailList = Arrays.asList(details);

				// let me sort the details
				detailList = sortTransactionDetail(detailList);

				Iterator detailIter = detailList.iterator();

				int j = 0;
				double[] total = { 0, 0 };

				boolean showTotal = false;
				while (detailIter.hasNext()) {
					TransactionDetail detail = (TransactionDetail) detailIter
							.next();

					total = getTotalValue(total, detail);
					model.addRow(prepareTransactionDetailDataForView(detail));

					row++;
					j++;
					showTotal = true;
				}
				i++;

				// total
				NumberRounding nr = new NumberRounding(
						NumberRounding.NUMBERROUNDING_ROUND, 2);
				total[0] = nr.round(total[0]);
				total[1] = nr.round(total[1]);

				if (showTotal) {
					model.addRow(new Object[] { "", "", "", "", "", "", "",
							"Total", "", new Double(total[0]),
							new Double(total[1]), "" });
					totalRowList.add(new Integer(row));
					if (total[0] != total[1])
						unBalanceRowList.add(new Integer(row));
					row++;
				}
			}
		}
		table.setModel(model);
	}

	private List sortTransactionDetail(List detailList) {
		List list = new ArrayList();
		List debet = new ArrayList();
		List credit = new ArrayList();

		Collections.sort(detailList, new Comparator() {

			public int compare(Object o1, Object o2) {
				return compareDetails(o1, o2);
			}

		});
		//detailList = merge(detailList);
		Iterator iter = detailList.iterator();


		while (iter.hasNext()) {
			TransactionDetail detail = (TransactionDetail) iter.next();
			Account account = (Account) detail.getAccount();

			if (((account.getBalance() == 0) && (detail.getValue() >= 0))
					|| ((account.getBalance() == 1) && (detail.getValue() < 0))) {
				// debet
				debet.add(detail);
			} else {
				// credit
				credit.add(detail);
			}
		}
		Collections.sort(debet, new Comparator() {

			public int compare(Object o1, Object o2) {
				return compareDetails(o1, o2);
			}

		});
		//debet = merge(debet);
		Collections.sort(credit, new Comparator() {

			public int compare(Object o1, Object o2) {
				return compareDetails(o1, o2);
			}

		});
		//credit = merge(credit);

		list.addAll(debet);
		list.addAll(credit);

		return list;
	}

	/**
	 * @param debet
	 */
	// JANGAN DIHAPUS
	/*private List merge(List list) {
		HashMap map = new HashMap();

		Iterator iterator = list.iterator();
		while(iterator.hasNext()) {
			TransactionDetail detail = (TransactionDetail) iterator.next();

			Long key = new Long(detail.getAccount().getIndex());
			if (map.containsKey(key)) {
				TransactionDetail old = (TransactionDetail) map.get(key);
				if (old.getCurrency().getIndex() != baseCurrency.getIndex()) {
					old = new TransactionDetail(old.getAccount(), old
							.getValue()
							* old.getExchangeRate(), baseCurrency, 1, old
							.getUnit(), old.getSubsidiaryAccount());
				}
				old.setValue(old.getValue() + (detail.getValue() * detail.getExchangeRate()));
			} else {
				map.put(key, detail);
			}
		}

		Collection coll = map.values();
		TransactionDetail[] details = (TransactionDetail[]) coll.toArray(new TransactionDetail[coll.size()]);

		return  Arrays.asList(details);
	}
*/
	/*
	 * private class MyComparator implements Comparator {
	 *
	 * public int compare(Object arg0, Object arg1) { int selisih = 0; if((arg0
	 * instanceof TransactionDetail)&&(arg1 instanceof TransactionDetail)){ long
	 * long0 = ((TransactionDetail)arg0).getAccount().getIndex(); long long1 =
	 * ((TransactionDetail)arg1).getAccount().getIndex(); selisih = (int) (long0 -
	 * long1); } return selisih; //return 0; } }
	 */
	private double[] getTotalValue(double[] total, TransactionDetail detail) {
		double amount = getAmount(detail);
		double value = Math.abs(amount);
		/*NumberRounding nr = new NumberRounding(
				NumberRounding.NUMBERROUNDING_ROUND, 2);*/
		//pilihannya: dibulatin disinikah? perlukah?
		//value = nr.round(value);
		if (((detail.getAccount().getBalance() == 0) && (detail.getValue() >= 0))
				|| ((detail.getAccount().getBalance() == 1) && (detail
						.getValue() < 0))) {
			// debet
			total[0] += value;
		} else {
			// credit
			total[1] += value;
		}
		return total;
	}

	private Vector prepareTransactionDataForView(int no, Transaction transaction) {
		Vector result = new Vector();
		result.add(new Integer(no));
		result.add(Boolean.valueOf(transaction.getStatus() == 3));
		result.add(transaction.getTransDate());
		result.add(transaction.getPostedDate());
		result.add(transaction.getJournalStandard().getCode());
		result.add(transaction.getUnit());
		result.add(transaction);
		result.add(transaction.getReference());
		result.add("");
		result.add("");
		result.add("");
		if (transaction.isVoid())
			result.add("VOID");
		else
			result.add("");
		return result;
	}

	private Vector prepareTransactionDetailDataForView(TransactionDetail detail) {
		Vector result = new Vector();
		Account account = (Account) detail.getAccount();
		result.add("");
		result.add("");
		result.add("");
		result.add("");
		result.add("");
		result.add("");

		if (((account.getBalance() == 0) && (detail.getValue() >= 0))
				|| ((account.getBalance() == 1) && (detail.getValue() < 0))) {
			// debet
			result.add(account.getName());
			result.add("");
			result.add(account.getCode());

			double amount = getAmount(detail);

			result.add(new Double(Math.abs(amount)));
			result.add("");
			result.add("");
		} else {
			// credit
			result.add("        " + account.getName());
			result.add("");
			result.add(account.getCode());
			result.add("");
			double amount = getAmount(detail);
			result.add(new Double(Math.abs(amount)));
			result.add("");
		}
		return result;
	}

	private double getAmount(TransactionDetail detail) {
		double amount;
		if (SHOW_IN_BASE_CURRENCY)
			amount = ConvertIntoBaseCurrency(detail.getValue(), detail
					.getCurrency(), detail.getExchangeRate());
		else
			amount = detail.getValue();
		return amount;
	}

	private double ConvertIntoBaseCurrency(double value, Currency currency,
			double exchangeRate) {
		double val = 0;

		if (currency.getIndex() == baseCurrency.getIndex()) {
			val = value;
		} else {
			val = value * exchangeRate;
		}

		return val;
	}

	private List getTransactionList(JournalStandard journalStandard,
			int status, Date dateFrom, Date dateTo, Unit unit) {
		// get the transaction
		GenericMapper mapper = MasterMap.obtainMapperFor(Transaction.class);
		mapper.setActiveConn(this.conn);

		String whereClausa = "";
		// journal standard
		if (journalStandard != null)
			whereClausa = IDBConstants.ATTR_TRANSACTION_CODE + "="
					+ journalStandard.getIndex();
		// status
		if (whereClausa == "")
			if (status != 4)
				whereClausa = IDBConstants.ATTR_STATUS + "=" + status;
			else
				whereClausa = IDBConstants.ATTR_STATUS + ">=2";

		// whereClausa = IDBConstants.ATTR_STATUS + " IN (2,3)";
		else if (status != 4)
			whereClausa += " AND " + IDBConstants.ATTR_STATUS + "=" + status;
		else
			whereClausa += " AND " + IDBConstants.ATTR_STATUS + ">=2";
		// whereClausa = " AND " + IDBConstants.ATTR_STATUS + " IN (2,3)";
		// date
		if ((dateFrom != null) && (dateTo != null)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateFromS = dateFormat.format(dateFrom);
			String dateToS = dateFormat.format(dateTo);
			if (whereClausa == "")
				whereClausa = IDBConstants.ATTR_TRANSACTION_DATE + " BETWEEN '"
						+ dateFromS + "' AND '" + dateToS + "'";
			else
				whereClausa += " AND (" + IDBConstants.ATTR_TRANSACTION_DATE
						+ " BETWEEN '" + dateFromS + "' AND '" + dateToS + "')";
		}
		// unit
		if (unit != null) {
			if (whereClausa == "")
				whereClausa = IDBConstants.ATTR_UNIT + "=" + unit.getIndex();
			else
				whereClausa += " AND " + IDBConstants.ATTR_UNIT + "="
						+ unit.getIndex();
		}

		System.out.println(whereClausa);
		ArrayList transactions = (ArrayList) mapper.doSelectWhere(whereClausa);

		Collections.sort(transactions);

		// and now get the details
		Iterator iter = transactions.iterator();
		while (iter.hasNext()) {
			Transaction transaction = (Transaction) iter.next();

			mapper = MasterMap.obtainMapperFor(TransactionDetail.class);
			mapper.setActiveConn(this.conn);

			ArrayList detailList = (ArrayList) mapper
					.doSelectWhere(IDBConstants.ATTR_TRANSACTION + "="
							+ transaction.getIndex());
			TransactionDetail[] details = (TransactionDetail[]) detailList
					.toArray(new TransactionDetail[detailList.size()]);

			transaction.setTransactionDetail(details);
		}

		return transactions;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == this.journalStandardPicker) {
			if (this.journalStandardPicker != null)
				if (this.journalStandardPicker.getJournalStandard() != null)
					this.descriptionTextField
							.setText(this.journalStandardPicker
									.getJournalStandard().getDescription());
				else
					this.descriptionTextField.setText("");
			else
				this.descriptionTextField.setText("");
		}
	}

	private class TransactionTable extends JTable implements ItemListener {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		protected TransactionTable() {
			DefaultTableModel model = new DefaultTableModel();
			// model.setColumnCount(10);
			model.addColumn("No.");
			model.addColumn(" ");
			model.addColumn("Trans. Date");
			model.addColumn("Posted Date");
			model.addColumn("Trans. Code");
			model.addColumn("Unit");
			model.addColumn("Description");
			model.addColumn("Ref. No.");
			model.addColumn("Account");
			model.addColumn("Debet");
			model.addColumn("Credit");
			model.addColumn("Is Void");
			setModel(model);

			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);

			getColumnModel().getColumn(1).setPreferredWidth(30);
			getColumnModel().getColumn(1).setMinWidth(30);
			getColumnModel().getColumn(1).setMaxWidth(30);
			getColumnModel().getColumn(2).setPreferredWidth(70);
			getColumnModel().getColumn(3).setPreferredWidth(70);
			getColumnModel().getColumn(4).setPreferredWidth(70);
			getColumnModel().getColumn(4).setPreferredWidth(70);
			getColumnModel().getColumn(6).setPreferredWidth(250);

			getColumnModel().getColumn(11).setPreferredWidth(50);
			getColumnModel().getColumn(11).setMinWidth(50);
			getColumnModel().getColumn(11).setMaxWidth(50);

			getColumnModel().getColumn(1).setHeaderRenderer(
					new CheckBoxHeader(this));

			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}

		public void clear() {
			stopCellEditing();
			((DefaultTableModel) getModel()).setRowCount(0);
		}

		public void stopCellEditing() {
			TableCellEditor editor;
			if ((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}

		public TableCellRenderer getCellRenderer(int row, int col) {
			if (inTotalRow(row)) {
				if ((col == 9) || (col == 10)) {
					if (inUnbalanceRow(row))
						return new BoldableDoubleCellRenderer(JLabel.RIGHT,
								true, true, Color.RED, Color.WHITE);
					return new BoldableDoubleCellRenderer(JLabel.RIGHT, true,
							true);
				}
				return new StandardFormatCellRenderer(Font.BOLD, JLabel.LEFT,
						true);
			} else if (inRendererRow(row)) {
				if (col == 0)
					return new StandardFormatCellRenderer(Font.PLAIN,
							JLabel.RIGHT);
				else if (col == 1)
					return new CheckBoxRenderer();
				else if (col == 2 || col == 3 || col == 4 || col == 5
						|| col == 7 || col == 8)
					return new StandardFormatCellRenderer(Font.PLAIN,
							JLabel.CENTER);
				else if (col == 11)
					return new StandardFormatCellRenderer(Font.PLAIN,
							JLabel.CENTER);
				return new StandardFormatCellRenderer(Font.PLAIN, JLabel.LEFT);
			} else {
				if (inEditorRow(row)) {
					if (col == 0)
						return new StandardFormatCellRenderer(Font.PLAIN,
								JLabel.RIGHT);
					else if (col == 2 || col == 3 || col == 4 || col == 5
							|| col == 7 || col == 8)
						return new StandardFormatCellRenderer(Font.PLAIN,
								JLabel.CENTER);
					else if ((col == 9) || (col == 10))
						return new BoldableDoubleCellRenderer(JLabel.RIGHT,
								true);

				} else {
					if (col == 0)
						return new StandardFormatCellRenderer(Font.PLAIN,
								JLabel.RIGHT, true);
					else if (col == 2 || col == 3 || col == 4 || col == 5
							|| col == 7 || col == 8)
						return new StandardFormatCellRenderer(Font.PLAIN,
								JLabel.CENTER, true);
					else if ((col == 9) || (col == 10))
						return new BoldableDoubleCellRenderer(JLabel.RIGHT,
								true, true);
				}
			}
			return new StandardFormatCellRenderer(Font.PLAIN, JLabel.LEFT, true);
		}

		private boolean inUnbalanceRow(int row) {
			Iterator iter = unBalanceRowList.iterator();
			while (iter.hasNext()) {
				int rowUnbalance = ((Integer) iter.next()).intValue();
				if (rowUnbalance == row)
					return true;
			}
			return false;
		}

		private boolean inTotalRow(int row) {
			Iterator iter = totalRowList.iterator();
			while (iter.hasNext()) {
				int rowTotal = ((Integer) iter.next()).intValue();
				if (rowTotal == row)
					return true;
			}
			return false;
		}

		public boolean isCellEditable(int row, int column) {
			if ((column == 1) && (inEditorRow(row)))
				return true;
			return false;
		}

		protected boolean inEditorRow(int row) {
			Iterator iter = editorRowList.iterator();
			while (iter.hasNext()) {
				int rowData = ((Integer) iter.next()).intValue();
				if (rowData == row)
					return true;
			}
			return false;
		}

		protected boolean inRendererRow(int row) {
			Iterator iter = rendererRowList.iterator();
			while (iter.hasNext()) {
				int rowData = ((Integer) iter.next()).intValue();
				if (rowData == row)
					return true;
			}
			return false;
		}

		public void itemStateChanged(ItemEvent e) {
			// cause only for checkbox header
			// if(e.getClass()==CheckBoxHeader.class){
			Iterator iter = editorRowList.iterator();
			while (iter.hasNext()) {
				int row = ((Integer) iter.next()).intValue();
				Boolean value = new Boolean(
						e.getStateChange() == ItemEvent.SELECTED);
				setValueAt(value, row, 1);
			}
			// }
		}

		public TableCellEditor getCellEditor(int row, int column) {
			if ((column == 1) && inEditorRow(row))
				return new CheckBoxCellEditor();
			// return super.getCellEditor(row, column);
			return null;
		}
	}

	private static class BoldableDoubleCellRenderer extends DoubleCellRenderer {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private boolean isBold = false;

		private Color color = Color.BLACK;

		private Color selectedColor = Color.WHITE;

		private boolean noSelection = false;

		public BoldableDoubleCellRenderer(int arg0, boolean isBold) {
			super(arg0);
			this.isBold = isBold;
		}

		public BoldableDoubleCellRenderer(int arg0, boolean isBold,
				boolean noSelection) {
			super(arg0);
			this.isBold = isBold;
			this.noSelection = noSelection;
		}

		public BoldableDoubleCellRenderer(int arg0, boolean isBold,
				Color color, Color selectedColor) {
			super(arg0);
			this.isBold = isBold;
			this.color = color;
			this.selectedColor = selectedColor;
		}

		public BoldableDoubleCellRenderer(int arg0, boolean isBold,
				boolean noSelection, Color color, Color selectedColor) {
			super(arg0);
			this.isBold = isBold;
			this.color = color;
			this.selectedColor = selectedColor;
			this.noSelection = noSelection;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (!this.noSelection)
				super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
			else
				super.getTableCellRendererComponent(table, value, false, false,
						row, column);

			if (isSelected && (!this.noSelection))
				setForeground(this.selectedColor);
			else
				setForeground(this.color);
			if (this.isBold) {
				Font font = getFont();
				setFont(font.deriveFont(Font.BOLD));
			}
			return this;
		}
	}

	private static class StandardFormatCellRenderer extends
			DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		private int fontStyle = Font.PLAIN;

		private int horizontalAlignment = JLabel.LEFT;

		private Color fontColor = Color.BLACK;

		private Color selectedColor = Color.WHITE;

		private boolean noSelection = false;

		public StandardFormatCellRenderer(int fontStyle, int horizontalAlignment) {
			this.fontStyle = fontStyle;
			this.horizontalAlignment = horizontalAlignment;
		}

		public StandardFormatCellRenderer(int fontStyle,
				int horizontalAlignment, boolean noSelection) {
			this.fontStyle = fontStyle;
			this.horizontalAlignment = horizontalAlignment;
			this.noSelection = noSelection;
		}

		public StandardFormatCellRenderer(int fontStyle,
				int horizontalAlignment, Color fontColor, Color selectedColor) {
			this.fontStyle = fontStyle;
			this.horizontalAlignment = horizontalAlignment;
			this.fontColor = fontColor;
			this.selectedColor = selectedColor;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (!this.noSelection)
				super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
			else
				super.getTableCellRendererComponent(table, value, false, false,
						row, column);

			setText((value == null) ? "    " : value.toString());
			setHorizontalAlignment(this.horizontalAlignment);

			Font font = getFont();
			if (isSelected && (!this.noSelection))
				setForeground(this.selectedColor);
			else
				setForeground(this.fontColor);
			setFont(font.deriveFont(this.fontStyle));
			return this;
		}
	}

	private static class CheckBoxRenderer extends JCheckBox implements
			TableCellRenderer {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (value instanceof Boolean) {
				setSelected(((Boolean) value).booleanValue());
				setHorizontalAlignment(JCheckBox.CENTER);
			}
			return this;
		}
	}

	private static class CheckBoxHeader extends JCheckBox implements
			TableCellRenderer, MouseListener {
		private static final long serialVersionUID = 1L;

		protected CheckBoxHeader rendererComponent;

		protected int column;

		protected boolean mousePressed = false;

		public CheckBoxHeader(ItemListener itemListener) {
			rendererComponent = this;
			rendererComponent.addItemListener(itemListener);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (table != null) {
				JTableHeader header = table.getTableHeader();
				if (header != null) {
					rendererComponent.setHorizontalAlignment(JCheckBox.CENTER);
					header.addMouseListener(rendererComponent);
				}
			}
			setColumn(column);
			return rendererComponent;
		}

		/**
		 * @param column
		 *            to which the CheckBox belongs to
		 */
		protected void setColumn(int column) {
			this.column = column;
		}

		/** @return the column to which the CheckBox belongs to */
		public int getColumn() {
			return column;
		}

		/** ************** Implementation of MouseListener ***************** */

		/**
		 * Calls doClick(), because the CheckBox doesn't receive any mouseevents
		 * itself. (because it is in a CellRendererPane).
		 */
		protected void handleClickEvent(MouseEvent e) {
			// Workaround: dozens of mouseevents occur for only one mouse click.
			// First MousePressedEvents, then MouseReleasedEvents, (then
			// MouseClickedEvents).
			// The boolean flag 'mousePressed' is set to make sure
			// that the action is performed only once.
			if (mousePressed) {
				mousePressed = false;

				JTableHeader header = (JTableHeader) (e.getSource());
				JTable tableView = header.getTable();
				TableColumnModel columnModel = tableView.getColumnModel();
				int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				int column = tableView.convertColumnIndexToModel(viewColumn);

				if (viewColumn == this.column && e.getClickCount() == 1
						&& column != -1) {
					doClick();
				}
			}
		}

		public void mouseClicked(MouseEvent e) {
			handleClickEvent(e);
			// Header doesn't repaint itself properly
			((JTableHeader) e.getSource()).repaint();
		}

		public void mousePressed(MouseEvent e) {
			mousePressed = true;
		}

		public void mouseReleased(MouseEvent e) {
			// works - problem: works even if column is dragged or resized ...
			// handleClickEvent(e);
			// properly repainting by the Header
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	private static class CheckBoxCellEditor extends JCheckBox implements
			TableCellEditor, ActionListener {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		CellEditorListener listener = null;

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			// if(value instanceof Boolean){
			setSelected(((Boolean) value).booleanValue());
			setHorizontalAlignment(JCheckBox.CENTER);
			// }
			this.addActionListener(this);

			return this;
		}

		public Object getCellEditorValue() {
			return new Boolean(this.isSelected());
		}

		public boolean isCellEditable(EventObject anEvent) {
			if (anEvent.getClass() == MouseEvent.class) {
				return true;
			}
			return false;
		}

		public boolean shouldSelectCell(EventObject anEvent) {
			return false;
		}

		public boolean stopCellEditing() {
			return true;
		}

		public void cancelCellEditing() {

		}

		public void addCellEditorListener(CellEditorListener l) {
			listener = l;
		}

		public void removeCellEditorListener(CellEditorListener l) {
			listener = null;
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == this) {
				listener.editingStopped(new ChangeEvent(this));
			}
		}
	}

	private class PostingMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				int[] rows = table.getSelectedRows();
				if (rows.length > 0) {
					if (table.inRendererRow(rows[0])) {
						Transaction transaction = (Transaction) table
								.getValueAt(rows[0], 6);
						TransactionPanelLoader loader = new TransactionPanelLoader(
								conn, itself, sessionid);
						try {
							loader.openPanel(transaction);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(itself, e1
									.getMessage(), "Error on Openning Panel",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				int[] rows = table.getSelectedRows();
				if (rows.length > 0) {
					int onSelRow = table.rowAtPoint(e.getPoint());
					int row = rows[0];
					if (onSelRow == row) {
						if (table.inEditorRow(rows[0])) {
							reversePopupMenu.show(table, e.getX(), e.getY());
						} else {
							if (showVoid)
								voidPopupMenu.show(table, e.getX(), e.getY());
						}
					}
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

	}

	private void doRevert() {
		int[] rows = table.getSelectedRows();
		if (rows.length > 0) {
			if (table.inEditorRow(rows[0])) {
				// get the transaction selected
				Transaction transaction = (Transaction) table.getValueAt(
						rows[0], 6);
				if (transaction.getStatus() == 3)
					return;

				String refNo = transaction.getReference();
				int response = JOptionPane.showConfirmDialog(this,
						"Are you sure to revert this transaction: " + refNo
								+ "?", "Confirmation",
						JOptionPane.YES_NO_OPTION);

				if (response == JOptionPane.YES_OPTION) {
					TransactionStateReversalLogic logic = new TransactionStateReversalLogic(
							conn);
					logic.revertState(transaction);

					JOptionPane.showMessageDialog(this,
							"Successfully reverting transaction: " + refNo);
				}
			}
		}
	}

	private void doVoid() {
		int[] rows = table.getSelectedRows();
		if (rows.length > 0) {
			if (!table.inEditorRow(rows[0])) {
				// get the transaction selected
				Transaction transaction = (Transaction) table.getValueAt(
						rows[0], 6);

				TransactionVoidLogic logic = new TransactionVoidLogic(conn,
						IDBConstants.MODUL_ACCOUNTING, sessionid);

				JournalStandard js = logic.getVoidJournalStandard();
				if ((transaction.getJournalStandard().getIndex() == js
						.getIndex())
						|| (transaction.getStatus() < 3)
						|| (transaction.isVoid() == true)) {
					JOptionPane
							.showMessageDialog(this,
									"Only posted and non-void transaction could be voided");
					return;
				}

				String refNo = transaction.getReference();
				int response = JOptionPane
						.showConfirmDialog(this,
								"Are you sure to void this transaction: "
										+ refNo + "?", "Confirmation",
								JOptionPane.YES_NO_OPTION);

				if (response == JOptionPane.YES_OPTION) {
					String description = JOptionPane.showInputDialog(this,
							"Please, insert description for void transaction of "
									+ refNo, "Transaction Description",
							JOptionPane.QUESTION_MESSAGE);

					if (description != null) {

						if (!description.equals("")) {

							try {
								logic.voidTransaction(transaction, description);

								JOptionPane.showMessageDialog(this,
										"Successfully voiding transaction: "
												+ refNo);
							} catch (Exception e) {
								JOptionPane
										.showMessageDialog(
												this,
												"Failed while voiding transaction: "
														+ refNo
														+ ".\n"
														+ "Probably it caused by unknown journal standard.\n"
														+ "Please contact your administrator.",
												"Error",
												JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(this,
									"Voiding transaction is cancelled.",
									"Information",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(this,
								"Voiding transaction is cancelled.",
								"Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
	}

	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareDetails(Object o1, Object o2) {
		if ((o1 instanceof TransactionDetail) && (o2 instanceof TransactionDetail)) {
			TransactionDetail d1 = (TransactionDetail) o1;
			TransactionDetail d2 = (TransactionDetail) o2;

			return d1.getAccount().getCode().compareTo(d2.getAccount().getCode());
		}

		return -1;
	}

}
