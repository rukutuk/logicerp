package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */


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
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

public class BalanceSheetDesignAccountDlg extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	JFrame m_mainframe;	
	JTextField m_nameTxt,m_parentTxt,m_fontSizeTxt,m_indentationTxt;
	JComboBox m_languageCmb,m_positiveCmb;
	JButton m_saveBt, m_cancelBt;
	JToggleButton m_boldBt, m_italicBt, m_underlineBt;
	JRadioButton m_accountRb, m_labelRb;
	JCheckBox m_group, m_showValues,m_usedFont;
	JComboBox m_alignment;	
	
	
	public BalanceSheetDesignAccountDlg(JFrame owner, Connection conn, long sessionid) {
		super(owner, "Balance Sheet Account Label", true);
		setSize(300, 350);
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
	}
	

	void constructComponent() {		  
		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);		
		
		m_boldBt = new JToggleButton("<html><b>B</b></html>");
		m_boldBt.addActionListener(this);
		m_boldBt.setPreferredSize(new Dimension(38,30));		
		m_italicBt = new JToggleButton("<html><i>I</i></html>");
		m_italicBt.addActionListener(this);
		m_italicBt.setPreferredSize(new Dimension(38,30));
		m_underlineBt = new JToggleButton("<html><u>U</u></html>");
		m_underlineBt.addActionListener(this);
		m_underlineBt.setPreferredSize(new Dimension(38,30));
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(m_boldBt);
		bg.add(m_italicBt);
		bg.add(m_underlineBt);
		
		JPanel informationPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		JLabel parentLabel = new JLabel("Parent");
		JLabel nameLabel = new JLabel("Name");	
		JLabel fontstyleLabel = new JLabel("Font Style");
		JLabel fontsizeLabel = new JLabel("Font Size");
		JLabel alignmentlable = new JLabel("Alignment");
		JLabel indentationLabel = new JLabel("Indentation");
		
		m_nameTxt = new JTextField();
		m_parentTxt = new JTextField();		
		m_fontSizeTxt = new JTextField();	
		//m_fontSizeTxt.setSize(new Dimension(10,10));
		m_indentationTxt = new JTextField();
		
		m_group = new JCheckBox("Group");
		m_showValues = new JCheckBox("Show Values");
		m_usedFont = new JCheckBox("Used Font Style and Font Size in Values");
		
		m_alignment = new JComboBox(new Object[]{"Left","Right"});		
		
		informationPanel.setLayout(new GridBagLayout());
		TitledBorder border = BorderFactory.createTitledBorder("Label Information");
		Font font = border.getTitleFont();
		font = font.deriveFont(Font.BOLD);
		border.setTitleFont(font);
		informationPanel.setBorder(border);
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(2, 2, 1, 2);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		informationPanel.add(parentLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		informationPanel.add(new JLabel("   "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;		
		informationPanel.add(m_parentTxt, gridBagConstraints);	
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(2, 2, 1, 2);		
		informationPanel.add(nameLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		informationPanel.add(new JLabel("   "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;		
		informationPanel.add(m_nameTxt, gridBagConstraints);

		gridBagConstraints.gridy = 2;		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		informationPanel.add(m_group, gridBagConstraints);		
		
		JPanel buttonFontPanel = new JPanel();
		buttonFontPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(0,1,0,0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;				
		buttonFontPanel.add(m_boldBt, gridBagConstraints);
		gridBagConstraints.gridx = 1;
		buttonFontPanel.add(m_italicBt, gridBagConstraints);
		gridBagConstraints.gridx = 2;		
		buttonFontPanel.add(m_underlineBt, gridBagConstraints);
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
		gridBagConstraints.gridwidth =3;
		gridBagConstraints.insets = new Insets(2, 2, 1, 2);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;		
		stylePanel.add(m_showValues, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth =1;
		gridBagConstraints.insets = new Insets(2, 2, 1, 2);
		stylePanel.add(fontstyleLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;		
		stylePanel.add(new JLabel(""), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth =2;
		stylePanel.add(buttonFontPanel, gridBagConstraints);		
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth =1;
		gridBagConstraints.insets = new Insets(2, 2, 1, 2);
		stylePanel.add(fontsizeLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;		
		stylePanel.add(new JLabel(""), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;		
		stylePanel.add(m_fontSizeTxt, gridBagConstraints);
			
		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 1.0;
		stylePanel.add(new JLabel(""), gridBagConstraints);		
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth =4;
		gridBagConstraints.insets = new Insets(2, 2, 1, 2);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;		
		stylePanel.add(m_usedFont, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth =1;
		gridBagConstraints.insets = new Insets(2, 2, 1, 2);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;		
		stylePanel.add(alignmentlable, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;		
		stylePanel.add(new JLabel(""), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;	
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth =2;
		stylePanel.add(m_alignment, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;		
		gridBagConstraints.gridwidth =1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(2, 2, 1, 2);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;		
		stylePanel.add(indentationLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;		
		stylePanel.add(new JLabel(""), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;	
		gridBagConstraints.weightx = 1.0;		
		stylePanel.add(m_indentationTxt, gridBagConstraints);
		
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(m_saveBt);
		buttonPanel.add(m_cancelBt);
		
		centerPanel.setPreferredSize(new Dimension(100,260));
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(informationPanel, BorderLayout.NORTH);
		centerPanel.add(stylePanel, BorderLayout.CENTER);
		
		JPanel endPanel = new JPanel();
		endPanel.setLayout(new BorderLayout());
		endPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		endPanel.add(centerPanel,BorderLayout.NORTH);
		endPanel.add(buttonPanel);
		
		getContentPane().add(endPanel, BorderLayout.CENTER);
	}
	
	public void setVisible( boolean flag ){
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());
		
		super.setVisible(flag);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_saveBt) {			
		}
		else if(e.getSource() == m_cancelBt) {
			/*if(!Misc.getConfirmation())
				return;*/
			dispose();
		}
	} 
	
			
}