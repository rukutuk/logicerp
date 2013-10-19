package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.reportdesign.BalanceSheetDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IncomeStatementDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IndirectCashFlowStatementDesign;
import pohaci.gumunda.titis.application.LanguagePack;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class ReportDesignDlg extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Connection connection = null;

	//private long sessionId = -1;

	private JFrame mainframe;

	private JTextField nameTextField, titleTextField;

	private JComboBox languageComboBox, positiveBalanceComboBox;

	private JButton okButton, cancelButton;

	private int response = JOptionPane.CANCEL_OPTION;

	private JournalTypeTable table;

	private Design design = null;

	private Class clazz;

	public ReportDesignDlg(JFrame owner, Connection conn, long sessionid,
			String title, Class clazz) {
		super(owner, title, true);
		setSize(300, 300);
		mainframe = owner;
		connection = conn;
		//sessionId = sessionid;
		this.clazz = clazz;
		constructComponent();
		initTable();
	}

	void constructComponent() {
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		JPanel formPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		JLabel nameLabel = new JLabel("Name");
		JLabel titleLabel = new JLabel("Title");
		JLabel languageLabel = new JLabel("Language");
		JLabel positiveLabel = new JLabel("Positive Balance");
		JLabel journalLabel = new JLabel("Journal Type");

		nameTextField = new JTextField();
		titleTextField = new JTextField();
		languageComboBox = new JComboBox(LanguagePack.pack());
		positiveBalanceComboBox = new JComboBox(new Object[] {
				Account.STR_DEBET, Account.STR_CREDIT });

		table = new JournalTypeTable();

		formPanel.setLayout(new GridBagLayout());
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(2, 0, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		formPanel.add(nameLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		formPanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		formPanel.add(nameTextField, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		formPanel.add(titleLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		formPanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		formPanel.add(titleTextField, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		formPanel.add(languageLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		formPanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		formPanel.add(languageComboBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		formPanel.add(positiveLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		formPanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		formPanel.add(positiveBalanceComboBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 0.0;
		formPanel.add(journalLabel, gridBagConstraints);

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		centerPanel.setPreferredSize(new Dimension(100, 210));
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(formPanel, BorderLayout.NORTH);
		centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel endPanel = new JPanel();
		endPanel.setLayout(new BorderLayout());
		endPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		endPanel.add(centerPanel, BorderLayout.NORTH);
		endPanel.add(buttonPanel);

		getContentPane().add(endPanel, BorderLayout.CENTER);
	}

	public void setVisible(boolean flag) {
		Rectangle rc = mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int) (rc.getWidth() - rcthis.getWidth()) / 2 + rc.x,
				(int) (rc.getHeight() - rcthis.getHeight()) / 2 + rc.y,
				(int) rcthis.getWidth(), (int) rcthis.getHeight());

		super.setVisible(flag);
	}

	public void initTable() {
		GenericMapper mapper = MasterMap.obtainMapperFor(Journal.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectAll();

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Journal journalType = (Journal) iterator.next();
			model.addRow(new Object[] { new Boolean(false), journalType });
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			doOK();
		} else if (e.getSource() == cancelButton) {
			doCancel();
		}
	}

	private void doCancel() {
		response = JOptionPane.CANCEL_OPTION;
		dispose();
	}

	private void doOK() {
		if (!isEntryValid())
			return;

		Design design = entity2gui();
		setDesign(design);

		response = JOptionPane.OK_OPTION;
		dispose();
	}

	protected Design entity2gui() {
		Design d = design;
		if (design == null) {
			if (clazz == IncomeStatementDesign.class)
				d = new IncomeStatementDesign();
			else if (clazz == BalanceSheetDesign.class)
				d = new BalanceSheetDesign();
			else if (clazz == IndirectCashFlowStatementDesign.class)
				d = new IndirectCashFlowStatementDesign();
		}

		d.setName(nameTextField.getText());
		d.setTitle(titleTextField.getText());
		d.setJournals(getJournalTypes());
		d.setLanguage(((LanguagePack) languageComboBox.getSelectedItem())
				.toString());
		d
				.setPositiveBalance((short) positiveBalanceComboBox
						.getSelectedIndex());

		return d;
	}

	public int getResponse() {
		return response;
	}

	private static class JournalTypeTable extends JTable {
		private static final long serialVersionUID = 1L;

		protected JournalTypeTable() {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn(" ");
			model.addColumn("Journal Type");
			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(40);
			getColumnModel().getColumn(0).setMaxWidth(40);
		}

		public TableCellRenderer getCellRenderer(int row, int col) {
			if (col == 0)
				return super.getDefaultRenderer(Boolean.class);
			return new DefaultTableCellRenderer();
		}

		public TableCellEditor getCellEditor(int row, int column) {
			if (column == 0)
				return super.getDefaultEditor(Boolean.class);
			return super.getCellEditor();
		}

		public boolean isCellEditable(int row, int column) {
			if (column == 0)
				return true;
			return false;
		}
	}

	public Design getDesign() {
		return design;
	}

	public void setDesign(Design design) {
		this.design = design;
		showData();
	}

	private void showData() {
		if (design == null)
			return;

		nameTextField.setText(design.getName());
		titleTextField.setText(design.getTitle());
		if (design.getLanguage().equals(LanguagePack.ENGLISH.toString()))
			languageComboBox.setSelectedItem(LanguagePack.ENGLISH);
		else
			languageComboBox.setSelectedItem(LanguagePack.INDONESIAN);
		positiveBalanceComboBox.setSelectedIndex(design.getPositiveBalance());

		Journal[] journals = design.getJournals();

		if (journals != null) {
			int maxRow = table.getRowCount();
			for (int i = 0; i < maxRow; i++) {
				Journal journalFromList = (Journal) table.getValueAt(i, 1);

				boolean found = false;
				for (int j = 0; j < journals.length; j++) {
					if (journalFromList.getIndex() == journals[j].getIndex()) {
						found = true;
						break;
					}
				}
				if (found) {
					table.setValueAt(new Boolean(true), i, 0);
				}
			}
		}
	}

	private boolean isEntryValid() {
		ArrayList msgs = new ArrayList();
		if (nameTextField.getText().equals(""))
			msgs.add("Name must be inserted");
		if (titleTextField.getText().equals(""))
			msgs.add("Title must be inserted");
		if (getJournalTypes().length == 0)
			msgs.add("Journal Type must be selected");

		if (msgs.size() > 0) {
			StringBuffer result = new StringBuffer();
			Iterator iter = msgs.iterator();
			while (iter.hasNext()) {
				String o = iter.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this, result);
			return false;
		}

		return true;
	}

	private Journal[] getJournalTypes() {
		Vector vector = new Vector();

		int maxRow = table.getRowCount();
		for (int i = 0; i < maxRow; i++) {
			if (((Boolean) table.getValueAt(i, 0)).booleanValue()) {
				Journal journalType = (Journal) table.getValueAt(i, 1);
				vector.addElement(journalType);
			}
		}

		Journal[] journals = (Journal[]) vector.toArray(new Journal[vector
				.size()]);

		return journals;
	}
}