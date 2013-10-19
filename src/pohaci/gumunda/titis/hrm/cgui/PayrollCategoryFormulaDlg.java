package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.DateFormatSymbols;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pohaci.gumunda.titis.application.FormulaEntity;
import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.application.FormulaVariable;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.PayrollTokenParser;

public class PayrollCategoryFormulaDlg extends JDialog implements ActionListener, Observer {
  private static final long serialVersionUID = -3524893070318172526L;
  Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  public JList m_list;
  public JTextArea m_formulaTextArea;
  JCheckBox m_everyBt;
  JComboBox m_monthComboBox;
  JButton m_moveBt, m_saveBt, m_resetBt, m_cancelBt;

  JButton m_plus = new JButton(" + ");
  JButton m_minus = new JButton(" - ");
  JButton m_div = new JButton(" / ");
  JButton m_mul = new JButton(" * ");
  JButton m_open = new JButton(" ( ");
  JButton m_close = new JButton(" ) ");
  JButton m_comma = new JButton(" , ");
  JButton m_zero = new JButton(" 0 ");
  JButton m_one = new JButton(" 1 ");
  JButton m_two = new JButton(" 2 ");
  JButton m_three = new JButton(" 3 ");
  JButton m_four = new JButton(" 4 ");
  JButton m_five = new JButton(" 5 ");
  JButton m_six = new JButton(" 6 ");
  JButton m_seven = new JButton(" 7 ");
  JButton m_eight = new JButton(" 8 ");
  JButton m_nein = new JButton(" 9 ");
  JButton m_procent = new JButton(" % ");
  JButton m_del = new JButton(" del ");
  
  JCheckBox m_roundCheckBox = new JCheckBox("Round Value");
  JComboBox m_roundingModeComboBox = null;
  JTextField m_precisionTextField = null;

  /*public static int OPERATOR = 0;
  public static int OPEN = 1;
  public static int CLOSE = 2;
  public static int COMMA = 3;
  public static int NUMBER = 4;
  public static int ITEM = 5;
  public static int PROCENT = 6;

  public static int PRESENCE = 7;
  public static int PRESENCE_LATE = 8;
  public static int PRESENCE_NOT_LATE = 9;
  public static int FIELD_ALLOWANCE = 10;
  public static int TIME_SHEET = 11;
  public static int RECEIVABLES = 12;*/

  FormulaEntity m_formulaEntity = null;
  public Formula m_formula = new Formula(); 
  int m_iResponse = JOptionPane.NO_OPTION;
   private boolean extention;

  public PayrollCategoryFormulaDlg(JFrame owner, Connection conn, long sessionid, FormulaEntity formulaEntity,boolean extention) {
    super(owner, "Formula", true);
    setSize(640, 500);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_formulaEntity = formulaEntity;
    this.extention=extention;

    constructComponent();
    initData();
    reloadPayrollComponent();
    
    m_formula.addObserver(this);
    update(m_formula,null);
   
  }

  void constructComponent() {
    JPanel centerPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel everyPanel = new JPanel();
    JPanel controlPanel = new JPanel();
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    m_list = new JList(new DefaultListModel());
    m_formulaTextArea = new JTextArea();
    m_formulaTextArea.setEditable(false);
    m_everyBt = new JCheckBox("Every");

    m_monthComboBox = new JComboBox();
    DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
    String[] monthNames = dateFormatSymbols.getMonths();
    if (m_monthComboBox.getItemCount() == 12) {
      m_monthComboBox.removeAllItems();
    }
    for (int i = 0; i < 12; i++) {
      m_monthComboBox.addItem(monthNames[i]);
    }

    m_plus.addActionListener(this);
    m_minus.addActionListener(this);
    m_div.addActionListener(this);
    m_mul.addActionListener(this);
    m_open.addActionListener(this);
    m_close.addActionListener(this);
    m_comma.addActionListener(this);
    m_zero.addActionListener(this);
    m_one.addActionListener(this);
    m_two.addActionListener(this);
    m_three.addActionListener(this);
    m_four.addActionListener(this);
    m_five.addActionListener(this);
    m_six.addActionListener(this);
    m_seven.addActionListener(this);
    m_eight.addActionListener(this);
    m_nein.addActionListener(this);
    m_procent.addActionListener(this);
    m_del.addActionListener(this);

    m_moveBt = new JButton(" > ");
    m_moveBt.addActionListener(this);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_resetBt = new JButton("Reset");
    m_resetBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    
    if (this.extention) {
			m_roundCheckBox.addActionListener(this);
			m_roundingModeComboBox = new JComboBox(NumberRounding.ROUNDING_MODE);

			m_precisionTextField = new JTextField();
			m_precisionTextField.setText("0");
			m_precisionTextField.setHorizontalAlignment(JTextField.RIGHT);

			setRoundEnabled(false);
		}

    controlPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    Misc.setGridBagConstraints(controlPanel, m_plus, gridBagConstraints, 0, 0,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 4)); // +
    Misc.setGridBagConstraints(controlPanel, m_seven, gridBagConstraints, 1, 0,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // 7
    Misc.setGridBagConstraints(controlPanel, m_eight, gridBagConstraints, 2, 0,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // 8
    Misc.setGridBagConstraints(controlPanel, m_nein, gridBagConstraints, 3, 0,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // 9
    Misc.setGridBagConstraints(controlPanel, m_open, gridBagConstraints, 4, 0,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 4, 1, 1)); // (

    Misc.setGridBagConstraints(controlPanel, m_minus, gridBagConstraints, 0, 1,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 4)); // -
    Misc.setGridBagConstraints(controlPanel, m_four, gridBagConstraints, 1, 1,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // 4
    Misc.setGridBagConstraints(controlPanel, m_five, gridBagConstraints, 2, 1,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // 5
    Misc.setGridBagConstraints(controlPanel, m_six, gridBagConstraints, 3, 1,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // 6
    Misc.setGridBagConstraints(controlPanel, m_close, gridBagConstraints, 4, 1,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 4, 1, 1)); // )

    Misc.setGridBagConstraints(controlPanel, m_mul, gridBagConstraints, 0, 2,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 4)); // *
    Misc.setGridBagConstraints(controlPanel, m_one, gridBagConstraints, 1, 2,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // 1
    Misc.setGridBagConstraints(controlPanel, m_two, gridBagConstraints, 2, 2,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // 2
    Misc.setGridBagConstraints(controlPanel, m_three, gridBagConstraints, 3, 2,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 1.0, new Insets(1, 1, 1, 1)); // 3
    Misc.setGridBagConstraints(controlPanel, m_procent, gridBagConstraints, 4, 2,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 4, 1, 1)); // %

    Misc.setGridBagConstraints(controlPanel, m_div, gridBagConstraints, 0, 3,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 4)); // /
    Misc.setGridBagConstraints(controlPanel, m_zero, gridBagConstraints, 1, 3,
                               GridBagConstraints.HORIZONTAL, 2, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // 0
    Misc.setGridBagConstraints(controlPanel, m_comma, gridBagConstraints, 3, 3,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 1, 1, 1)); // ,
    Misc.setGridBagConstraints(controlPanel, m_del, gridBagConstraints, 4, 3,
                               GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, new Insets(1, 4, 1, 1)); // del
    
    /**
     * round value
     */
    if (this.extention) {
			Misc.setGridBagConstraints(controlPanel, m_roundCheckBox,
					gridBagConstraints, 0, 4, GridBagConstraints.HORIZONTAL, 5,
					1, 1.0, 0.0, new Insets(8, 1, 1, 4));

			Misc.setGridBagConstraints(controlPanel,
					new JLabel("Rounding Mode"), gridBagConstraints, 0, 5,
					GridBagConstraints.HORIZONTAL, 3, 1, 1.0, 0.0, new Insets(
							3, 1, 1, 4));
			Misc.setGridBagConstraints(controlPanel, m_roundingModeComboBox,
					gridBagConstraints, 3, 5, GridBagConstraints.HORIZONTAL, 2,
					1, 1.0, 0.0, new Insets(3, 1, 1, 4));

			Misc.setGridBagConstraints(controlPanel, new JLabel("Precision"),
					gridBagConstraints, 0, 6, GridBagConstraints.HORIZONTAL, 3,
					1, 1.0, 0.0, new Insets(3, 1, 1, 4));
			Misc.setGridBagConstraints(controlPanel, m_precisionTextField,
					gridBagConstraints, 3, 6, GridBagConstraints.HORIZONTAL, 1,
					1, 1.0, 0.0, new Insets(3, 1, 1, 4));
		}
    
    
    everyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    if(extention){
    	everyPanel.add(m_everyBt);
    	everyPanel.add(m_monthComboBox);
    }
    leftPanel.setLayout(new BorderLayout());
    leftPanel.add(new JScrollPane(m_list), BorderLayout.CENTER);
    leftPanel.add(everyPanel, BorderLayout.SOUTH);

    rightPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    Misc.setGridBagConstraints(rightPanel, new JScrollPane(m_formulaTextArea), gridBagConstraints, 0, 0,
                               GridBagConstraints.BOTH, 1, 1, 1.0, 1.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(rightPanel, controlPanel, gridBagConstraints, 0, 1,
                               GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(rightPanel, new JPanel(), gridBagConstraints, 0, 2,
                               GridBagConstraints.BOTH, 1, 1, 1.0, 1.0, new Insets(1, 1, 1, 1));


    centerPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    Misc.setGridBagConstraints(centerPanel, leftPanel, gridBagConstraints, 0, 0,
                               GridBagConstraints.BOTH, 1, 1, 1.0, 1.0, new Insets(1, 1, 1, 1));
    gridBagConstraints.anchor = GridBagConstraints.CENTER;
    Misc.setGridBagConstraints(centerPanel, m_moveBt, gridBagConstraints, 1, 0,
                               GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));
    Misc.setGridBagConstraints(centerPanel, rightPanel, gridBagConstraints, 2, 0,
                               GridBagConstraints.BOTH, 1, 1, 0.0, 0.0, new Insets(1, 1, 1, 1));

    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_resetBt);
    buttonPanel.add(m_cancelBt);

    getContentPane().setLayout(new BorderLayout());
    setBorder(centerPanel,"");
    getContentPane().add(centerPanel, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  }

  public void setBorder(JPanel panel,String theme){
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), theme ,
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
	}
  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  void initData() {
    if(m_formulaEntity != null) {
    	m_formula.parseFormula(m_formulaEntity,new PayrollTokenParser(m_conn));      
    	// i add this
    	if(m_formulaEntity.getEveryWhichMonth()>0){
    		m_everyBt.setSelected(true);
    		int month = ((int)m_formulaEntity.getEveryWhichMonth())-1;
 
    		m_monthComboBox.setSelectedIndex(month);
    	}
    	if (this.extention) {
				if (m_formulaEntity.getNumberRounding().getRoundingMode() > -1) {
					m_roundCheckBox.setSelected(true);
					m_roundingModeComboBox.setSelectedIndex(m_formulaEntity
							.getNumberRounding().getRoundingMode());
					m_precisionTextField.setText(String.valueOf(m_formulaEntity
							.getNumberRounding().getPrecision()));
					setRoundEnabled(true);
				}
			}
    }
  }
  
  
  private void setRoundEnabled(boolean value) {
	  m_roundingModeComboBox.setEnabled(value);
	  m_precisionTextField.setEditable(value);
	  m_precisionTextField.setEnabled(value);
  }
  

   PayrollComponent[] payrollComps;
  public void reloadPayrollComponent() {
    DefaultListModel model = (DefaultListModel)m_list.getModel();
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      PayrollComponent[] component = logic.getNonGroupPayrollComponent(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
      for(int i = 0; i < component.length; i ++) {
        model.addElement(component[i]);
      }
      payrollComps = component;
      if(extention){
    	  for (int j=0; j< PayrollTokenParser.formulaVariables.length; j++) {
    		  model.addElement(PayrollTokenParser.formulaVariables[j]);
    	  }
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public FormulaEntity getFormulaEntity(){
  	short whichMonth;
  	whichMonth = 0;
  	if (this.m_everyBt.isSelected())
  	{
  		whichMonth = (short) (m_monthComboBox.getSelectedIndex() + 1);
  	}
  	NumberRounding numberRounding  = null;
  	if (this.extention) {
			if (m_roundCheckBox.isSelected()) {
				numberRounding = new NumberRounding(
						(short) m_roundingModeComboBox.getSelectedIndex(),
						Integer.parseInt(m_precisionTextField.getText()));
			} else
				numberRounding = new NumberRounding((short) (-1), Integer
						.parseInt(m_precisionTextField.getText()));
		}
  	
  	return m_formula.createFormulaEntity(whichMonth, numberRounding);
  	/*
    Formula formula = new Formula(m_formulaBuilder.getFormulacode().replaceAll(",", "."),
                       m_formulaBuilder.getFormulaText().trim(), whichMonth);
    formula.setFormulaStatus(m_formulaBuilder.getVformula());
    return formula;
    */
  }

  public int getResponse() {
    return m_iResponse;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_plus) {
      m_formula.addOperator("+");
    }
    else if(e.getSource() == m_procent) {
    	m_formula.addProcent();
    }

    else if(e.getSource() == m_minus) {
      m_formula.addOperator("-");
    }

    else if(e.getSource() == m_mul) {
      m_formula.addOperator("*");
    }

    else if(e.getSource() == m_div) {
      m_formula.addOperator("/");
    }

    else if(e.getSource() == m_open){
      m_formula.addOpeningParentheses();
	return;
    }

    else if(e.getSource() == m_close){
      m_formula.addClosingParentheses();
	  return;
    }

    else if(e.getSource() == m_comma){
      m_formula.addComma();
      return;
    }

    else if(e.getSource() == m_one) {
      m_formula.addNumber("1");
    }

    else if(e.getSource() == m_two) {
      m_formula.addNumber("2");
    }

    else if(e.getSource() == m_three) {
      m_formula.addNumber("3");
    }

    else if(e.getSource() == m_four) {
      m_formula.addNumber("4");
    }

    else if(e.getSource() == m_five) {
      m_formula.addNumber("5");
    }

    if(e.getSource() == m_six) {
      m_formula.addNumber("6");
    }

    else if(e.getSource() == m_seven) {
      m_formula.addNumber("7");
    }

    else if(e.getSource() == m_eight) {
      m_formula.addNumber("8");
    }

    else if(e.getSource() == m_nein) {
      m_formula.addNumber("9");
    }

    else if(e.getSource() == m_zero) {
      m_formula.addNumber("0");
    }

    else if(e.getSource() == m_del) {
      m_formula.deleteElement();
    }
    else if(e.getSource() == m_moveBt) {
      Object obj = m_list.getSelectedValue();
      
      if(obj instanceof PayrollComponent) {
		PayrollComponent component = (PayrollComponent)obj;
		m_formula.addItem((int)component.getIndex(),component.toString());  
	  }
	  else if (obj instanceof FormulaVariable) {
		FormulaVariable formulaVar = (FormulaVariable)obj;
		m_formula.addFormulaVariable(formulaVar);
	  }
	  return;
    }
    else if(e.getSource() == m_saveBt) {
    	int lastFormula =m_formula.getLastElementOfFormula();
    	if(lastFormula==FormulaEntity.OPEN || lastFormula==FormulaEntity.OPERATOR
    			|| lastFormula==FormulaEntity.COMMA){
    		JOptionPane.showMessageDialog(this,"          Invalid Formula");
    		return;
    	}
    	
    	m_iResponse = JOptionPane.OK_OPTION;
    	dispose();
    }
    else if(e.getSource() == m_resetBt) {
      m_formula.reset();
    }
    else if(e.getSource() == m_cancelBt) {
      dispose();
    }
    else if(e.getSource() == m_roundCheckBox) {
    	setRoundEnabled(m_roundCheckBox.isSelected());
    }
  }

  public void update(Observable o, Object arg) {
		if (o instanceof Formula)
		{
			m_formulaTextArea.setText(m_formula.getFormulaText());
		}
	}
}
