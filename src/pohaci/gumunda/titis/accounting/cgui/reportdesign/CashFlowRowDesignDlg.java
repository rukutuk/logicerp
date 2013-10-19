/**
 *
 */
package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import com.jgoodies.binding.formatter.EmptyNumberFormatter;

import pohaci.gumunda.titis.accounting.cgui.AccountPicker;
import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IncomeStatementDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportAccountValue;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportAltAccountValue;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportEmptyRow;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportLink;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportSubtotal;
import pohaci.gumunda.titis.accounting.logic.reportdesign.IncomeStatementLogic;

/**
 * @author dark-knight
 *
 */
public class CashFlowRowDesignDlg extends RowDesignDlg {

	public CashFlowRowDesignDlg(JFrame owner, Connection conn, long sessionid,
			ReportRow parent, String title, boolean debitIsPositiveBalance) {
		super(owner, conn, sessionid, parent, title, debitIsPositiveBalance);
		setSize(300, 560);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected JComboBox periodPositionComboBox;

	protected JComboBox designComboBox;

	protected ReportRowPicker rowPicker;

	void constructComponent() {
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		boldIcon = new ImageIcon("../images/bold.gif");
		boldToggleButton = new JToggleButton(boldIcon);
		boldToggleButton.addActionListener(this);
		boldToggleButton.setSize(10, 10);
		italicIcon = new ImageIcon("../images/italic.gif");
		italicToggleButton = new JToggleButton(italicIcon);
		italicToggleButton.addActionListener(this);
		italicToggleButton.setSize(10, 10);
		underlineIcon = new ImageIcon("../images/underline.gif");
		underlineToggleButton = new JToggleButton(underlineIcon);
		underlineToggleButton.addActionListener(this);
		underlineToggleButton.setSize(10, 10);

		JPanel informationPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southCenterPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		JLabel parentLabel = new JLabel("Parent");
		JLabel nameLabel = new JLabel("Label");
		JLabel fontstyleLabel = new JLabel("Font Style");
		JLabel fontsizeLabel = new JLabel("Font Size");
		JLabel alignmentlable = new JLabel("Alignment");
		JLabel indentationLabel = new JLabel("Indentation");
		JLabel typeLabel = new JLabel("Type");
		JLabel accountLabel = new JLabel("Account");
		JLabel periodPositionLabel = new JLabel("Date Position");
		JLabel designLabel = new JLabel("I/S Design");
		JLabel rowLabel = new JLabel("I/S Row");

		labelTextField = new JTextField();
		parentTextField = new JTextField(parent.getLabel());
		parentTextField.setEditable(false);
		fontSizeComboBox = new JComboBox(new Object[] { "Small Size (8 pt)",
				"Medium Size (10 pt)", "Large Size (12 pt)" });

		EmptyNumberFormatter formatter = new EmptyNumberFormatter(
				new Integer(0));
		formatter.setAllowsInvalid(false);
		DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
		decformat.applyPattern("#");
		decformat.setMaximumFractionDigits(0);
		formatter.setFormat(decformat);
		indentationTextField = new JFormattedTextField(formatter);
		indentationTextField.setValue(new Long(0));

		typeComboBox = new JComboBox(new String[] { "Group", "Value",
				"Alt Value", "Link", "Subtotal", "Empty" });
		typeComboBox.addActionListener(this);

		designComboBox = new JComboBox();
		initDesignList();

		viewValueCheckBox = new JCheckBox("Show Value");
		viewValueCheckBox.setSelected(true);
		invisibleCheckBox = new JCheckBox("Invisible");
		invisibleCheckBox.setSelected(false);
		negatePositiveBalanceCheckBox = new JCheckBox("Negate Positive Balance");
		negatePositiveBalanceCheckBox.setSelected(false);
		usedFontStyleCheckBox = new JCheckBox(
				"Used Font Style and Font Size in Values");
		usedFontStyleCheckBox.setSelected(true);

		alignmentComboBox = new JComboBox(new Object[] { "Left", "Right" });

		accountPicker = new AccountPicker(connection, sessionId, true);
		accountPicker.setEnabled(false);

		periodPositionComboBox = new JComboBox(new Object[] { "", "Start Date",
				"End Date" });

		informationPanel.setLayout(new GridBagLayout());

		periodPositionComboBox.setEnabled(false);
		designComboBox.setEnabled(false);
		designComboBox.addActionListener(this);

		rowPicker = new ReportRowPicker((Design) designComboBox
				.getSelectedItem());
		rowPicker.setEnabled(false);

		TitledBorder border = BorderFactory
				.createTitledBorder("Label Information");
		Font font = border.getTitleFont();
		font = font.deriveFont(Font.BOLD);
		border.setTitleFont(font);
		informationPanel.setBorder(border);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(3, 5, 1, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		informationPanel.add(parentLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		informationPanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		informationPanel.add(parentTextField, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(3, 5, 1, 5);
		informationPanel.add(typeLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		informationPanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		informationPanel.add(typeComboBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(3, 5, 5, 5);
		informationPanel.add(nameLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		informationPanel.add(new JLabel("   "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		informationPanel.add(labelTextField, gridBagConstraints);

		JPanel buttonFontPanel = new JPanel();
		buttonFontPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(0, 1, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		buttonFontPanel.add(boldToggleButton, gridBagConstraints);
		gridBagConstraints.gridx = 1;
		buttonFontPanel.add(italicToggleButton, gridBagConstraints);
		gridBagConstraints.gridx = 2;
		buttonFontPanel.add(underlineToggleButton, gridBagConstraints);
		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.REMAINDER;
		buttonFontPanel.add(new JLabel(""), gridBagConstraints);

		JPanel stylePanel = new JPanel();
		stylePanel.setLayout(new GridBagLayout());
		border = BorderFactory.createTitledBorder("Style");
		font = border.getTitleFont();
		font = font.deriveFont(Font.BOLD);
		border.setTitleFont(font);
		stylePanel.setBorder(border);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(1, 5, 1, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		stylePanel.add(invisibleCheckBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(1, 5, 1, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		stylePanel.add(viewValueCheckBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(1, 5, 1, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		stylePanel.add(negatePositiveBalanceCheckBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(3, 5, 1, 5);
		stylePanel.add(fontstyleLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		stylePanel.add(new JLabel(""), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 2;
		stylePanel.add(buttonFontPanel, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(3, 5, 1, 5);
		stylePanel.add(fontsizeLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		stylePanel.add(new JLabel(""), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		stylePanel.add(fontSizeComboBox, gridBagConstraints);

		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 1.0;
		stylePanel.add(new JLabel(""), gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.insets = new Insets(3, 5, 1, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		stylePanel.add(usedFontStyleCheckBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(3, 5, 1, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		stylePanel.add(alignmentlable, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		stylePanel.add(new JLabel(""), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 1;
		stylePanel.add(alignmentComboBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(3, 5, 1, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		stylePanel.add(indentationLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		stylePanel.add(new JLabel(""), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		stylePanel.add(indentationTextField, gridBagConstraints);

		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new GridBagLayout());
		border = BorderFactory.createTitledBorder("Value");
		font = border.getTitleFont();
		font = font.deriveFont(Font.BOLD);
		border.setTitleFont(font);
		valuePanel.setBorder(border);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(3, 5, 5, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		valuePanel.add(accountLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		valuePanel.add(new JLabel(""), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 7.0;
		valuePanel.add(accountPicker, gridBagConstraints);

		// --->

		JPanel propertyPanel = new JPanel();
		propertyPanel.setLayout(new GridBagLayout());
		border = BorderFactory.createTitledBorder("Property");
		font = border.getTitleFont();
		font = font.deriveFont(Font.BOLD);
		border.setTitleFont(font);
		propertyPanel.setBorder(border);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(3, 5, 5, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		propertyPanel.add(periodPositionLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		propertyPanel.add(new JLabel(""), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 7.0;
		propertyPanel.add(periodPositionComboBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(3, 5, 5, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;

		propertyPanel.add(designLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		propertyPanel.add(new JLabel(""), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 7.0;
		propertyPanel.add(designComboBox, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(3, 5, 5, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;

		propertyPanel.add(rowLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		propertyPanel.add(new JLabel(""), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 7.0;
		propertyPanel.add(rowPicker, gridBagConstraints);

		// -->

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		//centerPanel.setPreferredSize(new Dimension(100, 540));
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(informationPanel, BorderLayout.NORTH);
		centerPanel.add(stylePanel, BorderLayout.CENTER);

		southCenterPanel.setLayout(new BorderLayout());
		southCenterPanel.add(valuePanel, BorderLayout.CENTER);
		southCenterPanel.add(propertyPanel, BorderLayout.SOUTH);

		centerPanel.add(southCenterPanel, BorderLayout.SOUTH);

		JPanel endPanel = new JPanel();
		endPanel.setLayout(new BorderLayout());
		endPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		endPanel.add(centerPanel, BorderLayout.NORTH);
		endPanel.add(buttonPanel);

		getContentPane().add(endPanel, BorderLayout.CENTER);
	}

	private void initDesignList() {
		IncomeStatementLogic logic = new IncomeStatementLogic(connection,
				sessionId);
		List list = logic.getDesignList();
		list.add(0, new IncomeStatementDesign());

		IncomeStatementDesign[] designs = (IncomeStatementDesign[]) list
				.toArray(new IncomeStatementDesign[list.size()]);

		DefaultComboBoxModel model = new DefaultComboBoxModel(designs);
		designComboBox.setModel(model);
	}

	protected void onAlternativeSelected() {
		String selected = (String) typeComboBox.getSelectedItem();
		periodPositionComboBox.setSelectedIndex(0);
		if (selected.equals("Alt Value")) {
			periodPositionComboBox.setEnabled(true);
		} else {
			periodPositionComboBox.setEnabled(false);
		}

		designComboBox.setSelectedIndex(0);
		if (selected.equals("Link")) {
			designComboBox.setEnabled(true);
			rowPicker.setEnabled(true);
		} else {
			designComboBox.setEnabled(false);
			rowPicker.setEnabled(false);
		}
	}


	protected void fromLink(ReportRow r) {
		ReportLink l = (ReportLink) r;
		designComboBox.setSelectedItem(l.getDesignLink());
		rowPicker.setDesign(l.getDesignLink());
		rowPicker.setRow(l.getLink());
	}

	protected ReportRow link(ReportRow r) {
		ReportLink l = (ReportLink) r;
		l.setDesignLink(rowPicker.getDesign());
		l.setLink(rowPicker.getRow());

		return l;
	}

	protected ReportRow gui2alt(ReportRow r) {
		((ReportAltAccountValue) r).setOnStart(periodPositionComboBox
				.getSelectedItem().equals("Start Date"));
		return r;
	}

	protected void alt2gui(ReportRow r) {
		ReportAltAccountValue a = (ReportAltAccountValue) r;
		periodPositionComboBox.setSelectedItem(a.isOnStart() ? "Start Date"
				: "End Date");
	}

	protected void isAltEntryValid(ArrayList msgs) {
		String selected = (String) typeComboBox.getSelectedItem();
		if (selected.equals("Alt Value"))
			if (periodPositionComboBox.getSelectedItem().equals(""))
				msgs.add("Period Position must be inserted");
	}

	protected void changeCombo() {
		if (reportRow instanceof ReportGroup)
			typeComboBox.setSelectedItem("Group");
		else if (reportRow instanceof ReportAccountValue) {
			if (reportRow instanceof ReportAltAccountValue)
				typeComboBox.setSelectedItem("Alt Value");
			else
				typeComboBox.setSelectedItem("Value");
		} else if (reportRow instanceof ReportSubtotal)
			typeComboBox.setSelectedItem("Subtotal");
		else if (reportRow instanceof ReportEmptyRow)
			typeComboBox.setSelectedItem("Empty");
		else if (reportRow instanceof ReportLink)
			typeComboBox.setSelectedItem("Link");
		else
			System.err.println("ga kenal");
	}

	protected void onOtherSelected(ActionEvent e) {
		if (e.getSource() == designComboBox) {
			Design d = (Design) designComboBox.getSelectedItem();
			if(d.toString().equals(""))
				return;

			rowPicker.setDesign(d);
		}
	}
}
