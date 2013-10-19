package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class IncomeStatementAccountLabelDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel panelLabel = null;
	private JPanel panelLabelInfo = null;
	private JLabel labelParentName = null;
	private JTextField textFieldParentLabelName = null;
	private JLabel labelName = null;
	private JTextField textFieldLabelName = null;
	private JCheckBox checkBoxGroup = null;
	private JPanel panelConfig = null;
	private JPanel panelStyle = null;
	private JCheckBox checkBoxShowValue = null;
	private JLabel labelFontStyle = null;
	private JPanel panelButton = null;
	private JToggleButton toggleButtonBold = null;
	private JToggleButton toggleButtonItalic = null;
	private JCheckBox checkBoxUsedInValue = null;
	private JLabel labelAlignment = null;
	private JComboBox comboBoxAlignment = null;
	private JLabel labelIdentation = null;
	private JTextField textFieldIdentation = null;
	private JLabel labelPixel = null;
	private JPanel jPanel = null;
	private JButton buttonSave = null;
	private JButton buttonCancel = null;
	private String[] alignments = new String[]{"Left", "Right", "Center"};
	
	public IncomeStatementAccountLabelDialog(JFrame owner, String title) {
		super(owner, title, true);
		initialize();
	}
	
	private void initialize() {
		this.setSize(365, 374);
		this.setName("incomeStatementAccountLabelDialog");
		this.setContentPane(getJContentPane());
	}
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPanelLabel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	private JPanel getPanelLabel() {
		if (panelLabel == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(5);
			borderLayout.setVgap(5);
			panelLabel = new JPanel();
			panelLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(7,7,7,7));
			panelLabel.setLayout(borderLayout);
			panelLabel.add(getPanelLabelInfo(), java.awt.BorderLayout.NORTH);
			panelLabel.add(getPanelConfig(), java.awt.BorderLayout.CENTER);
			panelLabel.add(getJPanel(), java.awt.BorderLayout.SOUTH);
		}
		return panelLabel;
	}
	
	private JPanel getPanelLabelInfo() {
		if (panelLabelInfo == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(2,5,2,2);
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(2,5,2,5);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new java.awt.Insets(2,5,2,2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			labelName = new JLabel();
			labelName.setText("Label Name");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.insets = new java.awt.Insets(2,5,2,5);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2,5,2,2);
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			labelParentName = new JLabel();
			labelParentName.setText("Parent Label Name");
			panelLabelInfo = new JPanel();
			panelLabelInfo.setLayout(new GridBagLayout());
			panelLabelInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Label Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(51,51,51)));
			panelLabelInfo.add(labelParentName, gridBagConstraints);
			panelLabelInfo.add(getTextFieldParentLabelName(), gridBagConstraints1);
			panelLabelInfo.add(labelName, gridBagConstraints2);
			panelLabelInfo.add(getTextFieldLabelName(), gridBagConstraints3);
			panelLabelInfo.add(getCheckBoxGroup(), gridBagConstraints4);
		}
		return panelLabelInfo;
	}
	
	private JTextField getTextFieldParentLabelName() {
		if (textFieldParentLabelName == null) {
			textFieldParentLabelName = new JTextField();
			textFieldParentLabelName.setEditable(false);
		}
		return textFieldParentLabelName;
	}
	
	private JTextField getTextFieldLabelName() {
		if (textFieldLabelName == null) {
			textFieldLabelName = new JTextField();
		}
		return textFieldLabelName;
	}
	
	private JCheckBox getCheckBoxGroup() {
		if (checkBoxGroup == null) {
			checkBoxGroup = new JCheckBox();
			checkBoxGroup.setText("Label Group");
		}
		return checkBoxGroup;
	}
	
	private JPanel getPanelConfig() {
		if (panelConfig == null) {
			panelConfig = new JPanel();
			panelConfig.setLayout(new BorderLayout());
			panelConfig.add(getPanelStyle(), java.awt.BorderLayout.NORTH);
		}
		return panelConfig;
	}
	
	private JPanel getPanelStyle() {
		if (panelStyle == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 2;
			gridBagConstraints13.insets = new java.awt.Insets(2,5,2,5);
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 4;
			labelPixel = new JLabel();
			labelPixel.setText("pixel(s)");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints12.gridy = 4;
			gridBagConstraints12.weightx = 0.0;
			gridBagConstraints12.insets = new java.awt.Insets(2,5,2,5);
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.ipadx = 0;
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2,5,2,5);
			gridBagConstraints11.gridy = 4;
			labelIdentation = new JLabel();
			labelIdentation.setText("Identation At");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 3;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.insets = new java.awt.Insets(2,5,2,5);
			gridBagConstraints10.gridwidth = 2;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(2,5,2,5);
			gridBagConstraints9.gridy = 3;
			labelAlignment = new JLabel();
			labelAlignment.setText("Alignment");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2,5,2,2);
			gridBagConstraints8.gridwidth = 2;
			gridBagConstraints8.gridy = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.insets = new java.awt.Insets(2,5,2,2);
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.gridy = 1;
			labelFontStyle = new JLabel();
			labelFontStyle.setText("Font Style");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new java.awt.Insets(2,5,2,2);
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints5.gridx = 0;
			panelStyle = new JPanel();
			panelStyle.setLayout(new GridBagLayout());
			panelStyle.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Label Style", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(51,51,51)));
			panelStyle.setComponentOrientation(java.awt.ComponentOrientation.UNKNOWN);
			panelStyle.add(getCheckBoxShowValue(), gridBagConstraints5);
			panelStyle.add(labelFontStyle, gridBagConstraints6);
			panelStyle.add(getPanelButton(), gridBagConstraints7);
			panelStyle.add(getCheckBoxUsedInValue(), gridBagConstraints8);
			panelStyle.add(labelAlignment, gridBagConstraints9);
			panelStyle.add(getComboBoxAlignment(), gridBagConstraints10);
			panelStyle.add(labelIdentation, gridBagConstraints11);
			panelStyle.add(getTextFieldIdentation(), gridBagConstraints12);
			panelStyle.add(labelPixel, gridBagConstraints13);
		}
		return panelStyle;
	}
	
	private JCheckBox getCheckBoxShowValue() {
		if (checkBoxShowValue == null) {
			checkBoxShowValue = new JCheckBox();
			checkBoxShowValue.setText("Show Value");
		}
		return checkBoxShowValue;
	}
	
	private JPanel getPanelButton() {
		if (panelButton == null) {
			panelButton = new JPanel();
			panelButton.add(getToggleButtonBold(), null);
			panelButton.add(getToggleButtonItalic(), null);
		}
		return panelButton;
	}
	
	private JToggleButton getToggleButtonBold() {
		if (toggleButtonBold == null) {
			toggleButtonBold = new javax.swing.JToggleButton();
			toggleButtonBold.setText(" B ");
			toggleButtonBold.setPreferredSize(new java.awt.Dimension(48,28));
		}
		return toggleButtonBold;
	}
	
	private JToggleButton getToggleButtonItalic() {
		if (toggleButtonItalic == null) {
			toggleButtonItalic = new javax.swing.JToggleButton();
			toggleButtonItalic.setText(" I ");
			toggleButtonItalic.setPreferredSize(new java.awt.Dimension(48,28));
			toggleButtonItalic.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
		}
		return toggleButtonItalic;
	}
	
	private JCheckBox getCheckBoxUsedInValue() {
		if (checkBoxUsedInValue == null) {
			checkBoxUsedInValue = new JCheckBox();
			checkBoxUsedInValue.setText("Also Used in Value");
		}
		return checkBoxUsedInValue;
	}
	
	private JComboBox getComboBoxAlignment() {
		if (comboBoxAlignment == null) {
			comboBoxAlignment = new JComboBox(alignments);
		}
		return comboBoxAlignment;
	}
	
	private JTextField getTextFieldIdentation() {
		if (textFieldIdentation == null) {
			textFieldIdentation = new JTextField();
			textFieldIdentation.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return textFieldIdentation;
	}
	
	private JPanel getJPanel() {
		if (jPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			jPanel = new JPanel();
			jPanel.setName("panelButton");
			jPanel.setLayout(flowLayout);
			jPanel.add(getButtonSave(), null);
			jPanel.add(getButtonCancel(), null);
		}
		return jPanel;
	}
	
	private JButton getButtonSave() {
		if (buttonSave == null) {
			buttonSave = new JButton();
			buttonSave.setText("Save");
		}
		return buttonSave;
	}
	
	private JButton getButtonCancel() {
		if (buttonCancel == null) {
			buttonCancel = new JButton();
			buttonCancel.setText("Cancel");
		}
		return buttonCancel;
	}
} 
