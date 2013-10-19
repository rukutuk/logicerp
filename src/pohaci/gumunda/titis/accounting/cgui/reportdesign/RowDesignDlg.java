/**
 *
 */
package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import com.jgoodies.binding.formatter.EmptyNumberFormatter;

import pohaci.gumunda.titis.accounting.cgui.AccountPicker;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportAccountValue;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportAltAccountValue;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportEmptyRow;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportLink;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportSubtotal;

/**
 * @author dark-knight
 *
 */
public class RowDesignDlg extends JDialog implements
		ActionListener {
	private static final long serialVersionUID = 1L;

	protected Connection connection = null;

	protected long sessionId = -1;

	protected JFrame mainframe;

	protected JTextField labelTextField, parentTextField;

	protected JFormattedTextField indentationTextField;

	protected JButton saveButton, cancelButton;

	protected JToggleButton boldToggleButton, italicToggleButton,
			underlineToggleButton;

	protected JCheckBox viewValueCheckBox, usedFontStyleCheckBox, invisibleCheckBox;

	protected JComboBox alignmentComboBox, fontSizeComboBox, typeComboBox;

	protected AccountPicker accountPicker;

	protected ReportRow parent;

	protected ReportRow reportRow;

	protected int response = JOptionPane.CANCEL_OPTION;

	protected boolean debitIsPositiveBalance = false;

	protected ImageIcon boldIcon, italicIcon, underlineIcon;

	protected JCheckBox negatePositiveBalanceCheckBox;

	public RowDesignDlg(JFrame owner, Connection conn,
			long sessionid, ReportRow parent, String title, boolean debitIsPositiveBalance) {
		super(owner, title, true);
		setSize(300, 460);
		mainframe = owner;
		connection = conn;
		sessionId = sessionid;
		this.parent = parent;
		this.debitIsPositiveBalance = debitIsPositiveBalance;
		constructComponent();
	}

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
		JPanel buttonPanel = new JPanel();

		JLabel parentLabel = new JLabel("Parent");
		JLabel nameLabel = new JLabel("Label");
		JLabel fontstyleLabel = new JLabel("Font Style");
		JLabel fontsizeLabel = new JLabel("Font Size");
		JLabel alignmentlable = new JLabel("Alignment");
		JLabel indentationLabel = new JLabel("Indentation");
		JLabel typeLabel = new JLabel("Type");
		JLabel accountLabel = new JLabel("Account");

		labelTextField = new JTextField();
		parentTextField = new JTextField(parent.getDisplayLabel());
		parentTextField.setEditable(false);
		fontSizeComboBox = new JComboBox(new Object[] { "Small Size (8 pt)",
				"Medium Size (10 pt)", "Large Size (12 pt)" });

		EmptyNumberFormatter formatter = new EmptyNumberFormatter(new Integer(0));
		formatter.setAllowsInvalid(false);
		DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
		decformat.applyPattern("#");
		decformat.setMaximumFractionDigits(0);
		formatter.setFormat(decformat);
		indentationTextField = new JFormattedTextField(formatter);
		indentationTextField.setValue(new Long(0));

		typeComboBox = new JComboBox(new String[] { "Group", "Value",
				"Subtotal", "Empty" });
		typeComboBox.addActionListener(this);
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

		informationPanel.setLayout(new GridBagLayout());
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

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		centerPanel.setPreferredSize(new Dimension(100, 370));
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(informationPanel, BorderLayout.NORTH);
		centerPanel.add(stylePanel, BorderLayout.CENTER);
		centerPanel.add(valuePanel, BorderLayout.SOUTH);

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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {
			doSave();
		} else if (e.getSource() == cancelButton) {
			doCancel();
		} else if (e.getSource() == typeComboBox) {
			onTypeSelected();
		}

		onOtherSelected(e);
	}

	protected void onOtherSelected(ActionEvent e) {

	}

	private void doCancel() {
		response = JOptionPane.CANCEL_OPTION;
		dispose();
	}

	private void doSave() {
		if(!isEntryValid())
			return;

		ReportRow row = gui2entity();
		setReportRow(row);

		response = JOptionPane.OK_OPTION;
		dispose();
	}

	private boolean isEntryValid() {
		ArrayList msgs = new ArrayList();
		if (labelTextField.getText().equals(""))
			msgs.add("Label must be inserted");

		isAltEntryValid(msgs);

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

	protected void isAltEntryValid(ArrayList msgs) {

	}

	private ReportRow gui2entity() {
		ReportRow r = reportRow;
		if (reportRow == null) {
			String label = labelTextField.getText();
			if(typeComboBox.getSelectedItem().equals("Group"))
				r = new ReportGroup(label, debitIsPositiveBalance);
			else if(typeComboBox.getSelectedItem().equals("Value"))
				r = new ReportAccountValue(label, debitIsPositiveBalance);
			else if(typeComboBox.getSelectedItem().equals("Alt Value"))
				r = new ReportAltAccountValue(label, debitIsPositiveBalance);
			else if(typeComboBox.getSelectedItem().equals("Subtotal"))
				r = new ReportSubtotal(label, debitIsPositiveBalance, (ReportGroup) parent);
			else if(typeComboBox.getSelectedItem().equals("Empty"))
				r = new ReportEmptyRow(label);
			else if(typeComboBox.getSelectedItem().equals("Link"))
				r = new ReportLink(label, debitIsPositiveBalance);
			else
				System.err.println("ga jelas");
		}else{
			r.setLabel(labelTextField.getText());
		}
		if(!typeComboBox.getSelectedItem().equals("Empty")){
			r.setInvisible(invisibleCheckBox.isSelected());
			r.setNegatePositiveBalance(negatePositiveBalanceCheckBox.isSelected());
			r.setBold(boldToggleButton.isSelected());
			r.setItalic(italicToggleButton.isSelected());
			r.setUnderline(underlineToggleButton.isSelected());
			if(indentationTextField.getValue()!=null)
				r.setIndent(((Number)indentationTextField.getValue()).intValue());
			r.setTextSize(fontSizeComboBox.getSelectedIndex());
			r.setViewValue(viewValueCheckBox.isSelected());
			r.setAlignment(alignmentComboBox.getSelectedIndex());
			r.setUsedStyleInValue(usedFontStyleCheckBox.isSelected());
		}
		if(typeComboBox.getSelectedItem().equals("Value"))
			((ReportAccountValue)r).setAccount(accountPicker.getAccount());
		if(typeComboBox.getSelectedItem().equals("Alt Value"))
			((ReportAltAccountValue)r).setAccount(accountPicker.getAccount());

		if(r instanceof ReportAltAccountValue)
			r = gui2alt(r);

		if(r instanceof ReportLink)
			r = link(r);

		return r;
	}

	protected ReportRow link(ReportRow r) {
		return r;
	}

	protected ReportRow gui2alt(ReportRow r) {
		return r;
	}

	protected void onTypeSelected() {
		String selected = (String) typeComboBox.getSelectedItem();

		if ((selected.equals("Value")||(selected.equals("Alt Value")))) {
			accountPicker.setEnabled(true);
		} else {
			accountPicker.setEnabled(false);
			accountPicker.setAccount(null);
		}
		if (selected.equals("Empty")){
			labelTextField.setText("Empty Row");
			labelTextField.setEditable(false);
		}else {
			labelTextField.setText("");
			labelTextField.setEditable(true);
		}
		if((selected.equals("Group"))||(selected.equals("Value")||(selected.equals("Alt Value")))){
			invisibleCheckBox.setEnabled(true);
		}else{
			invisibleCheckBox.setEnabled(false);
			invisibleCheckBox.setSelected(false);
		}

		onAlternativeSelected();
	}

	protected void onAlternativeSelected() {

	}

	public ReportRow getReportRow() {
		return reportRow;
	}

	public void setReportRow(ReportRow reportRow) {
		this.reportRow = reportRow;
		showData();
	}

	private void showData() {
		if(reportRow==null)
			return;

		typeComboBox.setEnabled(false);

		parentTextField.setText(parent.getDisplayLabel());
		changeCombo();

		labelTextField.setText(reportRow.getDisplayLabel());
		viewValueCheckBox.setSelected(reportRow.isViewValue());
		negatePositiveBalanceCheckBox.setSelected(reportRow.isNegatePositiveBalance());
		boldToggleButton.setSelected(reportRow.isBold());
		italicToggleButton.setSelected(reportRow.isItalic());
		underlineToggleButton.setSelected(reportRow.isUnderline());
		fontSizeComboBox.setSelectedIndex(reportRow.getTextSize());
		usedFontStyleCheckBox.setSelected(reportRow.isUsedStyleInValue());
		alignmentComboBox.setSelectedIndex(reportRow.getAlignment());
		indentationTextField.setValue(new Long(reportRow.getIndent()));
		invisibleCheckBox.setSelected(reportRow.isInvisible());

		if(reportRow instanceof ReportAccountValue)
			accountPicker.setAccount(((ReportAccountValue)reportRow).getAccount());

		if(reportRow instanceof ReportAltAccountValue)
			alt2gui(reportRow);

		if(reportRow instanceof ReportLink)
			fromLink(reportRow);
	}

	protected void fromLink(ReportRow reportRow2) {

	}

	protected void alt2gui(ReportRow r) {
		// TODO Auto-generated method stub

	}

	protected void changeCombo() {
		if(reportRow instanceof ReportGroup)
			typeComboBox.setSelectedItem("Group");
		else if(reportRow instanceof ReportAccountValue)
			typeComboBox.setSelectedItem("Value");
		else if(reportRow instanceof ReportSubtotal)
			typeComboBox.setSelectedItem("Subtotal");
		else if(reportRow instanceof ReportEmptyRow)
			typeComboBox.setSelectedItem("Empty");
		else
			System.err.println("ga kenal");
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

}
