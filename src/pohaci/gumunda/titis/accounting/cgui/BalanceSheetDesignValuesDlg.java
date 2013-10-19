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
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import pohaci.gumunda.titis.application.DatePicker;

public class BalanceSheetDesignValuesDlg extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	JFrame m_mainframe;
	DatePicker m_datePicker;
	JTextField m_nameTxt,m_titleTxt;
	JComboBox m_languageCmb,m_positiveCmb;
	JButton m_saveBt, m_cancelBt;
	JRadioButton m_accountRb, m_labelRb;
	
	//int m_iResponse = JOptionPane.NO_OPTION;
	buttonTable m_table=null;
	
	
	public BalanceSheetDesignValuesDlg(JFrame owner, Connection conn, long sessionid) {
		super(owner, "Balance Sheet Design", true);
		setSize(300, 300);
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
		initTable();
	}
	
	void constructComponent() {
		m_datePicker = new DatePicker();    
		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		JPanel formPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		JLabel nameLabel = new JLabel("Name");	
		m_nameTxt = new JTextField();		
	
		m_accountRb = new JRadioButton("Account");
		m_labelRb = new JRadioButton("Labels");
		ButtonGroup bg = new ButtonGroup();
		bg.add(m_accountRb);
		bg.add(m_labelRb);
		
		m_accountRb.setSelected(true);		
		
		m_table = new buttonTable();
		
		formPanel.setLayout(new GridBagLayout());
		TitledBorder border = BorderFactory.createTitledBorder("Label");
		Font font = border.getTitleFont();
		font = font.deriveFont(Font.BOLD);
		border.setTitleFont(font);
		formPanel.setBorder(border);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(2, 0, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		formPanel.add(nameLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		formPanel.add(new JLabel("   "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		formPanel.add(m_nameTxt, gridBagConstraints);
		
		JPanel cekPanel = new JPanel();
		cekPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		cekPanel.add(m_accountRb, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		cekPanel.add(m_labelRb, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.REMAINDER;
		cekPanel.add(new JLabel(" "), gridBagConstraints);
		
		
		JPanel tablePanel = new JPanel();		
		tablePanel.setLayout(new BorderLayout());
		border = BorderFactory.createTitledBorder("Value");
		font = border.getTitleFont();
		font = font.deriveFont(Font.BOLD);
		border.setTitleFont(font);
		tablePanel.setBorder(border);
		tablePanel.add(cekPanel,BorderLayout.NORTH);
		tablePanel.add(new JScrollPane(m_table));	
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(m_saveBt);
		buttonPanel.add(m_cancelBt);
		
		centerPanel.setPreferredSize(new Dimension(100,210));
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(formPanel, BorderLayout.NORTH);
		centerPanel.add(tablePanel, BorderLayout.CENTER);
		
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
	
	String[] m_isiTable = {"Begining Balance","General Journal","Adjustment Journal"}; 
	public void initTable(){
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		model.setRowCount(0);
		for (int i=0;i<m_isiTable.length;i++){
			model.addRow(new Object[]{new Boolean(false),m_isiTable[i]});
		}		
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
	
	class buttonTable extends JTable {
		private static final long serialVersionUID = 1L;
		buttomTabbleRcvOthersModel model = new buttomTabbleRcvOthersModel();
		ArrayList listData=new ArrayList();
		protected buttonTable() {
			model.addColumn("Account");						 
			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(40);
						
		}
		
		/*public TableCellRenderer getCellRenderer(int row, int col) {
			if(col==0)				
				return super.getDefaultRenderer(Boolean.class);
			return new DefaultTableCellRenderer();			
		}
		
		public TableCellEditor getCellEditor(int row, int column) {
			if(column==0)
				return super.getDefaultEditor(Boolean.class);
			return super.getCellEditor();			
		}*/
	}	
	
	class buttomTabbleRcvOthersModel extends DefaultTableModel{
		private static final long serialVersionUID = 1L;		
		public boolean isCellEditable(int row, int col) {
			if (col==0)
				return true;
			return false;
		}
	}		
}