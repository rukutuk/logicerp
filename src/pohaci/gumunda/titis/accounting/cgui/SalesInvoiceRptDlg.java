package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.JSpinField;

public class SalesInvoiceRptDlg extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	JButton m_okbt;
	//SalesInvoicePanel m_panel;
	JSpinField m_pildigit;
	JComboBox m_languageTxt;
	private int response = JOptionPane.CANCEL_OPTION;
	private int digit = 0;
	String m_moneyLanguage="";

	public SalesInvoiceRptDlg(int defaultDigit){
		super(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),"Print Option", true);
		//m_panel  = panel;
		digit = defaultDigit;
		setSize(210, 130);
		ConstructComponent();
	}

	public void ConstructComponent(){
		JLabel digitLbl = new JLabel("Decimal digits for Qty");
		JLabel languagelbl = new JLabel("Language");
		m_pildigit = new JSpinField();
		m_pildigit.setMaximum(4);
		m_pildigit.setValue(digit);
		m_languageTxt = new JComboBox(new String[]{MoneyTalk.LANGUAGE_INDONESIAN,MoneyTalk.LANGUAGE_ENGLISH});
		m_okbt = new JButton("OK");
		m_okbt.addActionListener(this);
		JPanel panel = new JPanel();

		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 0, 0, 2);
		gridBagConstraints.weightx = 1.0;
		panel.add(digitLbl,gridBagConstraints);

		gridBagConstraints.gridx = 1;
		panel.add(m_pildigit,gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		panel.add(languagelbl,gridBagConstraints);

		gridBagConstraints.gridx = 1;
		panel.add(m_languageTxt,gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.insets = new Insets(2, 0, 0, 2);
		gridBagConstraints.gridwidth =2;
		panel.add(m_okbt,gridBagConstraints);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel);
	}


	public void setVisible( boolean flag ){
		Rectangle rc = pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame().getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());

		super.setVisible(flag);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_okbt){
			digit = m_pildigit.getValue();
			response = JOptionPane.OK_OPTION;
			m_moneyLanguage=(String)m_languageTxt.getSelectedItem();
			dispose();
		}
	}

	public int getResponse() {
		return response;
	}

	public int getDigit() {
		return digit;
	}

	public String getLanguage(){
		return m_moneyLanguage;
	}
}
