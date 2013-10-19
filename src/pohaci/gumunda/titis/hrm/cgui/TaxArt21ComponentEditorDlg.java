package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.tree.*;

import com.jgoodies.binding.formatter.EmptyNumberFormatter;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import pohaci.gumunda.titis.accounting.cgui.*;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.FormulaEntity;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class TaxArt21ComponentEditorDlg extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFrame m_mainframe;

	JTextField m_parentTextField, m_codeTextField, m_descriptionTextField;

	JRadioButton m_groupRadioButton, m_nongroupRadioButton;

	AccountPicker m_accountPicker;

	//TaxArt21PayrollComponentPicker payrollComponentPicker;

	JComboBox m_paymentComboBox;// , m_submitComboBox;

	// PaychequeLabelPicker m_labelPicker;

	JButton m_saveBt, m_cancelBt;

	JLabel formulalabel = new JLabel("Formula");

	DefaultMutableTreeNode m_parent = null;

	TaxArt21Component m_component = null;

	int m_iResponse = JOptionPane.NO_OPTION;

	Connection m_conn = null;

	long m_sessionid = -1;

	private TaxArt21FormulaPicker taxArt21FormulaPicker;
	
	JPanel formulaAttributePanel = new JPanel();
	
	/**
	 * rounding
	 */
	JCheckBox m_roundCheckBox = new JCheckBox("Round Value");

	JComboBox m_roundingModeComboBox = null;

	JFormattedTextField m_precisionFormattedTextField = null;
	
	/**
	 * negative value
	 */
	JCheckBox m_negativeValueCheckBox = new JCheckBox("Negative Value");
	
	/**
	 * comparable value
	 */
	JCheckBox m_comparationCheckBox = new JCheckBox("Compare with Fixed Value");
	
	JComboBox m_comparationModeComboBox = null;
	
	JFormattedTextField m_fixedValueFormattedTextField = null;
	
	public TaxArt21ComponentEditorDlg(JFrame mainframe, Connection conn,
			long sessionid, DefaultMutableTreeNode parent) {
		super(mainframe, "Add Tax Art 21 Component", true);
		setSize(350, 430);
		m_mainframe = mainframe;
		m_conn = conn;
		m_sessionid = sessionid;
		m_parent = parent;

		constructComponent();
		this.initData();
	}

	public TaxArt21ComponentEditorDlg(JFrame mainframe, Connection conn,
			long sessionid, DefaultMutableTreeNode parent,
			TaxArt21Component component) {
		super(mainframe, "Edit Tax Art 21 Component", true);
		setSize(350, 430);
		m_mainframe = mainframe;
		m_conn = conn;
		m_sessionid = sessionid;
		m_parent = parent;
		m_component = component;
	/*	System.out.println(m_component +" ; "+ m_component.getDescription()+" , "+m_component.getIndex()
				+ " formula = "+ m_component.getFormulaEntity().getFormulaCode());*/

		constructComponent();
		this.initData();
	}

	void constructComponent() {
		m_parentTextField = new JTextField();
		m_parentTextField.setEditable(false);
		m_codeTextField = new JTextField();
		m_descriptionTextField = new JTextField();

		m_groupRadioButton = new JRadioButton("Group");
		m_groupRadioButton.addActionListener(this);
		m_nongroupRadioButton = new JRadioButton("Non Group");
		m_nongroupRadioButton.setSelected(true);
		m_nongroupRadioButton.addActionListener(this);
		m_accountPicker = new AccountPicker(m_conn, m_sessionid);
		m_paymentComboBox = new JComboBox(PayrollComponent.m_payments);
		m_paymentComboBox.insertItemAt("", 0);
		m_paymentComboBox.setSelectedIndex(0);

		/*payrollComponentPicker = new TaxArt21PayrollComponentPicker(m_conn,
				m_sessionid);*/
		taxArt21FormulaPicker = new TaxArt21FormulaPicker(m_conn, m_sessionid);
		
		/**
		 * rounding mode
		 */
		m_roundCheckBox.addActionListener(this);
	    m_roundingModeComboBox = new JComboBox(NumberRounding.ROUNDING_MODE);
	    
	    m_precisionFormattedTextField = new JFormattedTextField(createNumberFormat("0"));
	    m_precisionFormattedTextField.setHorizontalAlignment(JTextField.RIGHT);
	    
	    setRoundEnabled(false);
	    
	    /**
	     * comparable
	     */
	    m_comparationCheckBox.addActionListener(this);
	    m_comparationModeComboBox = new JComboBox(new String[]{"MAXIMUM", "MINIMUM"});
	    m_fixedValueFormattedTextField = new JFormattedTextField(createNumberFormat("#,##0.00"));
	    m_fixedValueFormattedTextField.setHorizontalAlignment(JTextField.RIGHT);
	    
	    setComparationEnabled(false);
	    

		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);

		JLabel parentLabel = new JLabel("Parent");
		JLabel codeLabel = new JLabel("Code");
		JLabel desciptionLabel = new JLabel("Description");
		JLabel typeLabel = new JLabel("Type");
		JLabel accountLabel = new JLabel("Tax Account");

//		JLabel paychequeLabel = new JLabel("Formula");

		JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		ButtonGroup bg = new ButtonGroup();
		bg.add(m_groupRadioButton);
		bg.add(m_nongroupRadioButton);
		radioPanel.add(m_groupRadioButton);
		radioPanel.add(m_nongroupRadioButton);

		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		centerPanel.add(parentLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(m_parentTextField, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		centerPanel.add(codeLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(m_codeTextField, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		centerPanel.add(desciptionLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(m_descriptionTextField, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		centerPanel.add(typeLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(radioPanel, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		centerPanel.add(accountLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(m_accountPicker, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		//centerPanel.add(payrollCompLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		//centerPanel.add(new JLabel(" "), gridBagConstraints);

		// /payrollComponentPickerScroll
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

		// gridBagConstraints.ipady=200;
		//payrollComponentPicker.setPreferredSize(new Dimension(80,100));
		//centerPanel.add(payrollComponentPicker, gridBagConstraints);//

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		centerPanel.add(formulalabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);

		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(taxArt21FormulaPicker, gridBagConstraints);
		
		// begin disini
		/**
		 * buat naruh attribute dari formula
		 */
		
		
		formulaAttributePanel.setLayout(new GridBagLayout());
		formulaAttributePanel.setBorder(BorderFactory.createTitledBorder("Formula Attributes"));
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weightx = 0.0;
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.insets = new Insets(5, 5, 0, 0);
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		formulaAttributePanel.add(m_roundCheckBox, gridBagConstraints2);
		
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 0.0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.insets = new Insets(1, 5, 0, 5);
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		formulaAttributePanel.add(new JLabel("Rounding Mode"), gridBagConstraints2);
		
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1;
		gridBagConstraints2.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints2.insets = new Insets(1, 5, 0, 5);
		formulaAttributePanel.add(m_roundingModeComboBox, gridBagConstraints2);
		
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 2;
		gridBagConstraints2.weightx = 0.0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.insets = new Insets(1, 5, 0, 5);
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		formulaAttributePanel.add(new JLabel("Precision"), gridBagConstraints2);
		
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 2;
		gridBagConstraints2.weightx = 1;
		gridBagConstraints2.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints2.insets = new Insets(1, 5, 0, 5);
		formulaAttributePanel.add(m_precisionFormattedTextField, gridBagConstraints2);
		
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 3;
		gridBagConstraints2.weightx = 0.0;
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.insets = new Insets(7, 5, 0, 0);
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		formulaAttributePanel.add(m_negativeValueCheckBox, gridBagConstraints2);
		
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 4;
		gridBagConstraints2.weightx = 0.0;
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.insets = new Insets(7, 5, 0, 0);
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		formulaAttributePanel.add(m_comparationCheckBox, gridBagConstraints2);
		
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 5;
		gridBagConstraints2.weightx = 0.0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.insets = new Insets(1, 5, 0, 5);
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		formulaAttributePanel.add(new JLabel("Comparation Mode"), gridBagConstraints2);
		
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 5;
		gridBagConstraints2.weightx = 1;
		gridBagConstraints2.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints2.insets = new Insets(1, 5, 0, 5);
		formulaAttributePanel.add(m_comparationModeComboBox, gridBagConstraints2);
		
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 6;
		gridBagConstraints2.weightx = 0.0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.insets = new Insets(1, 5, 10, 5);
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		formulaAttributePanel.add(new JLabel("Comparation Value"), gridBagConstraints2);
		
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 6;
		gridBagConstraints2.weightx = 1;
		gridBagConstraints2.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints2.insets = new Insets(1, 5, 10, 5);
		formulaAttributePanel.add(m_fixedValueFormattedTextField, gridBagConstraints2);
		
		
		// taruh
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		centerPanel.add(formulaAttributePanel, gridBagConstraints);
		
		// end disini

		/*gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);*/

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(m_saveBt);
		buttonPanel.add(m_cancelBt);

		JPanel mergePanel = new JPanel(new BorderLayout());
		mergePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		mergePanel.add(centerPanel, BorderLayout.CENTER);
		mergePanel.add(buttonPanel, BorderLayout.SOUTH);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mergePanel, BorderLayout.CENTER);
	}

	private void setComparationEnabled(boolean enable) {
		m_comparationModeComboBox.setEnabled(enable);
		m_fixedValueFormattedTextField.setEditable(enable);
		m_fixedValueFormattedTextField.setEnabled(enable);
	}

	private EmptyNumberFormatter createNumberFormat(String pattern) {
		EmptyNumberFormatter formatter = new EmptyNumberFormatter(new Double(0));
		formatter.setAllowsInvalid(false);
		DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
		decformat.applyPattern(pattern);
		decformat.setMaximumFractionDigits(2);
		formatter.setFormat(decformat);
		return formatter;
	}

	private void setRoundEnabled(boolean value) {
		m_roundingModeComboBox.setEnabled(value);
		m_precisionFormattedTextField.setEditable(value);
		m_precisionFormattedTextField.setEnabled(value);
	}

	private void initData() {
		if (m_parent != null) {
			m_parentTextField.setText(((TaxArt21Component) m_parent
					.getUserObject()).getDescription());
		//	payrollComponentPicker
			//		.setTaxArt21Component((TaxArt21Component) m_parent
				//			.getUserObject());
			//payrollComponentPicker.drawPayrollComponent();

		}
		
		if (m_component != null) {
			m_codeTextField.setText(m_component.getCode());
			m_descriptionTextField.setText(m_component.getDescription());
			System.out.println(m_component+ " , "+m_component.getIndex());
			
			/*try {
				payrollComponentPicker.setTaxArt21Component(m_component);
				payrollComponentPicker.drawPayrollComponent();
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		
			if (m_component.isGroup()) {
				m_groupRadioButton.setSelected(true);
				ifGroup();
			} else {
				m_nongroupRadioButton.setSelected(true);
				IfNonGroup();
			}
			m_accountPicker.setAccount(m_component.getAccount());
			taxArt21FormulaPicker.setFormulaEntity(m_component.getFormulaEntity());

			m_roundCheckBox.setSelected(m_component.isRounded());
			if(m_component.isRounded()){
				m_roundingModeComboBox.setSelectedIndex(m_component.getRoundedValue());
				m_precisionFormattedTextField.setValue(new Integer(m_component.getPrecision()));
			}else{
				m_roundingModeComboBox.setSelectedIndex(0);
				m_precisionFormattedTextField.setValue(null);
			}
			setRoundEnabled(m_component.isRounded());
			m_negativeValueCheckBox.setSelected(m_component.isNegative());
			m_comparationCheckBox.setSelected(m_component.isComparable());
			if(m_component.isComparable()){
				m_comparationModeComboBox.setSelectedItem(m_component.getComparationMode());
				m_fixedValueFormattedTextField.setValue(new Double(m_component.getComparatorValue()));
			}else{
				m_comparationModeComboBox.setSelectedIndex(0);
				m_fixedValueFormattedTextField.setValue(null);
			}
			setComparationEnabled(m_component.isComparable());
		}
	}

	void onSave() {
		TaxArt21Component component;

		try {
			component = this.getDefineTaxArt21Component();
			if (m_parent != null)
				component.setParent((TaxArt21Component) m_parent
						.getUserObject());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

			if (m_component == null) {
				component = logic.createTaxArt21Component(m_sessionid,
						IDBConstants.MODUL_MASTER_DATA, component);
			} else {
				/*PayrollComponent[] pp = payrollComponentPicker.getPayrollComponent();
				for(int i=0;i<pp.length;i++){
					//String s = pp[i].m_description;
				}*/
				
				component.setIndex(m_component.getIndex());
				component=logic.updateTaxArt21Component(m_sessionid,
						IDBConstants.MODUL_MASTER_DATA,
						component);
				 
				 /*logic.updateTaxArt21Payrol(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
							m_component,payrollComponentPicker.getPayrollComponent());
				
				payrollComponentPicker.clear();*/
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		m_component = component;
		m_iResponse = JOptionPane.OK_OPTION;
		dispose();
	}

	public TaxArt21Component getTaxArt21Component() {
		return m_component;
	}

	private TaxArt21Component getDefineTaxArt21Component() throws Exception {
		java.util.ArrayList list = new java.util.ArrayList();
		String code = m_codeTextField.getText().trim();
		String description = m_descriptionTextField.getText().trim();
		boolean isgroup = m_groupRadioButton.isSelected();
		Account account = m_accountPicker.getAccount();
		FormulaEntity formulaEntity =taxArt21FormulaPicker.getFormulaEntity();
		if (code.equals(""))
			list.add("Code");
		if (description.equals(""))
			list.add("Description");
		if(!m_groupRadioButton.isSelected() && account==null)
			list.add("Tax Account");
		if (m_nongroupRadioButton.isSelected() && formulaEntity==null)
			list.add("Formula");
		
		boolean isRounded = m_roundCheckBox.isSelected();
		int roundingMode = -1;
		int precision = 0;
		if(isRounded){
			roundingMode = m_roundingModeComboBox.getSelectedIndex();
			precision = ((Number)m_precisionFormattedTextField.getValue()).intValue();
		}
		boolean isNegative = m_negativeValueCheckBox.isSelected();
		boolean isComparative = m_comparationCheckBox.isSelected();
		String comparationMode = null;
		double comparator = -1;
		if(isComparative){
			comparationMode = (String) m_comparationModeComboBox.getSelectedItem();
			comparator = ((Number)m_fixedValueFormattedTextField.getValue()).doubleValue();
		}
		
		String strexc = "Please insert :\n";
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if (exception.length > 0) {
			for (int i = 0; i < exception.length; i++)
				strexc += exception[i] + "\n";
			throw new Exception(strexc);
		}

		return new TaxArt21Component(code, description, isgroup, account,
				formulaEntity, "note", isRounded, roundingMode, precision, isNegative,
				isComparative, comparationMode, comparator);
	}

	public int getResponse() {
		return m_iResponse;
	}

	public void setVisible(boolean flag) {
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int) (rc.getWidth() - rcthis.getWidth()) / 2 + rc.x,
				(int) (rc.getHeight() - rcthis.getHeight()) / 2 + rc.y,
				(int) rcthis.getWidth(), (int) rcthis.getHeight());
		super.setVisible(flag);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_saveBt) {
			onSave();
		} else if (e.getSource() == m_cancelBt) {
			dispose();
		} else if (e.getSource() == m_groupRadioButton) {
			if (m_groupRadioButton.isSelected()) {
				ifGroup();
			}
		} else if (e.getSource() == m_nongroupRadioButton) {
			if (m_nongroupRadioButton.isSelected()) {
				IfNonGroup();
			}
		} else if (e.getSource() == m_roundCheckBox) {
			setRoundEnabled(m_roundCheckBox.isSelected());
		} else if (e.getSource() == m_comparationCheckBox){
			setComparationEnabled(m_comparationCheckBox.isSelected());
		}
	}

	private void IfNonGroup() {
		m_accountPicker.setEnabled(true);
		//payrollComponentPicker.setEnabled(true);
		taxArt21FormulaPicker.setEnabled(true);
		
		m_roundCheckBox.setEnabled(true);
		m_negativeValueCheckBox.setEnabled(true);
		m_comparationCheckBox.setEnabled(true);
		
		setRoundEnabled(m_roundCheckBox.isSelected());
		setComparationEnabled(m_comparationCheckBox.isSelected());
	}

	private void ifGroup() {
		m_accountPicker.setEnabled(false);
		m_accountPicker.setAccount(null);
		taxArt21FormulaPicker.setEnabled(false);
		taxArt21FormulaPicker.setFormulaEntity(null);
		
		m_roundCheckBox.setEnabled(false);
		m_negativeValueCheckBox.setEnabled(false);
		m_comparationCheckBox.setEnabled(false);
		
		setRoundEnabled(false);
		setComparationEnabled(false);
		/*payrollComponentPicker.clear();
		payrollComponentPicker.setEnabled(false);*/
	}

}