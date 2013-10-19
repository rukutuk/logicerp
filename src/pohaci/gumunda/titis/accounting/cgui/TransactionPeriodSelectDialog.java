package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TransactionPeriodSelectDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JFrame m_mainFrame = null;
	//private Connection m_conn;
	//private long m_sessionId = -1;
	private String m_fromDate = "";
	private String m_toDate = "";
	
	private JPanel m_tablePanel = null;
	private JButton m_submitBtn = null;
	private JButton m_cancelBtn = null;
	private JScrollPane m_scrollPane = null;
	private JTable m_table = null;
	private DefaultTableModel m_dataModel = null;
	
	public TransactionPeriodSelectDialog(JFrame frame, String title,Connection conn, long sessionId,
			JTable table){
		super(frame, ( title == null ) ? "Select Transaction Period" : title, true);
		this.m_mainFrame = frame;
		//this.m_conn = conn;
		//this.m_sessionId = sessionId;
		this.m_dataModel = (DefaultTableModel) table.getModel();
		setSize(430, 300);
		setTable();
		constructComponent();
	}
	
	public void constructComponent(){
				
		this.m_submitBtn = new JButton("Submit");
		this.m_submitBtn.addActionListener(this);
		this.m_cancelBtn = new JButton("Cancel");
		this.m_cancelBtn.addActionListener(this);
				
		this.m_tablePanel = new JPanel();
		this.m_scrollPane = new JScrollPane();
		this.m_scrollPane.setPreferredSize(new Dimension(100, 230));
		this.m_scrollPane.getViewport().add(this.m_table);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(this.m_submitBtn);
		buttonPanel.add(this.m_cancelBtn);
		
		this.m_tablePanel.setLayout(new BorderLayout());
		this.m_tablePanel.add(this.m_scrollPane,BorderLayout.NORTH);
		this.m_tablePanel.add(buttonPanel,BorderLayout.SOUTH);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(this.m_tablePanel, BorderLayout.NORTH);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.m_submitBtn){
			setSelectedDate();
			dispose();
		}
		else if(e.getSource() == this.m_cancelBtn)
			dispose();
	}
	
	public void setVisible( boolean flag ){
		Rectangle rc = m_mainFrame.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());
		
		super.setVisible(flag);
	}
	
	/*private void setTableHeader(){
		this.m_table = new JTable();
		this.m_dataModel = new DefaultTableModel();
		this.m_dataModel.addColumn("From Date");
		this.m_dataModel.addColumn("To Date");
		this.m_dataModel.addColumn("Status");
				
		this.m_table.setModel(this.m_dataModel);
		this.m_table.getColumnModel().getColumn(0).setPreferredWidth(100);
		this.m_table.getColumnModel().getColumn(0).setMaxWidth(100);
		this.m_table.getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
	}*/
		
	public void setTable(){
		this.m_table = new JTable(){
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		m_table.setModel(m_dataModel);
	}

	public void setSelectedDate(){
		int row = m_table.getSelectedRow();
		m_fromDate = (String) m_table.getValueAt(row,1);
		m_toDate = (String) m_table.getValueAt(row,2);
	}
	
	public String getFromDate(){
		return m_fromDate;
	}
	
	public String getToDate(){
		return m_toDate;
	}
}
