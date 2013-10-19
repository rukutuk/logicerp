package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import pohaci.gumunda.titis.application.DatePicker;

public class TransactionPeriodAddDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -6652293424928525782L;
	private JFrame m_mainframe;
	//private Connection m_conn = null;
	//private long m_sessionId = -1;
	
	private JTable table;
	private JButton m_okBtn;
	private JButton m_cancelBtn;
	private DatePicker m_fromDate;
	private DatePicker m_toDate;
	private SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private String action = "";
	
	public TransactionPeriodAddDialog(JFrame frame, String title,Connection conn, long sessionId, 
			JTable table, Date fromDate, Date toDate){
		super(frame, ( title == null ) ? "Edit Transaction Period" : title, true);
		this.m_mainframe = frame;
		//this.m_conn = conn;
		//this.m_sessionId = sessionId;
		this.table = table;
		action = "edit";
		
		setSize(280, 150);
		constructComponent();
		if(fromDate != null && toDate != null){			
			
			this.m_fromDate.setDate(fromDate);
			this.m_toDate.setDate(toDate);
		}
		
		//if(m_status.equals("open"))
			m_fromDate.setEditable(false);
	
	}
	
	// Constructor buat Add
	public TransactionPeriodAddDialog(JFrame frame, String title, JTable table, Connection conn, long sessionId){
		super(frame, ( title == null ) ? "Add Transaction Period" : title, true);
		this.m_mainframe = frame;
		//this.m_conn = conn;
		//this.m_sessionId = sessionId;
		this.table = table;
		action = "add";
		setSize(280, 150);
		constructComponent();
	}
	
	public void setVisible( boolean flag ){
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());
		
		super.setVisible(flag);
	}
	
	public void constructComponent(){
		JLabel fromLbl = new JLabel ("From Date");
		JLabel toLbl = new JLabel ("To Date");
		
		this.m_fromDate = new DatePicker();
		this.m_fromDate.setDate(new Date());
		this.m_toDate = new DatePicker();
		this.m_toDate.setDate(new Date());
		this.m_okBtn = new JButton("OK");
		this.m_okBtn.addActionListener(this);
		this.m_cancelBtn = new JButton("Cancel");
		this.m_cancelBtn.addActionListener(this);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(this.m_okBtn);
		buttonPanel.add(this.m_cancelBtn);
					
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setPreferredSize(new Dimension(100,80));
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 6, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		panel.add(fromLbl, gridBagConstraints);
					
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(1, 6, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		panel.add(toLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panel.add(this.m_fromDate , gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panel.add(this.m_toDate , gridBagConstraints);
					
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.NORTH);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.m_okBtn){
			if(isValid())
				dispose();
			else JOptionPane.showMessageDialog(this,"From date or To date is not valid");
		}
		else {
			m_fromDate.setDate(null);
			m_toDate.setDate(null);
			dispose();
		}
	}

	public Date getFromDate(){
		return m_fromDate.getDate();
	}
	
	public Date getToDate(){
		return m_toDate.getDate();
	}
	
	public boolean isValid(){
		int row = table.getRowCount();
		Date fromDate = getFromDate();
		Date toDate = getToDate();
		boolean status = false;
		
		if(action.equals("add")){
			if(row > 0){
				
				Date toDateBefore = (Date) table.getValueAt(row-1,2);;		
				
				if(isEqual(toDate,fromDate) >= 0 && isEqual(fromDate,toDateBefore) == 1) 
					status = true;
			}
			else if(isEqual(toDate,fromDate) >= 0) 
				status = true;
		}
		else{
			int sRow = table.getSelectedRow();
			if(sRow > 0){
				String strDate = (String) table.getValueAt(sRow-1,2);
				Date toDateBefore = null;		
				try {
					toDateBefore = m_dateFormat.parse(strDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(isEqual(toDate,fromDate) >= 0 && isEqual(fromDate,toDateBefore) == 1) 
					status = true;
			}
			else if(isEqual(toDate,fromDate) >= 0) 
				status = true;
		}
		return status;
	}
	
	public int isEqual(Date d1, Date d2){
		int status = -1;
		/*if(d1.getYear() >= d2.getYear() && d1.getMonth() >= d2.getMonth()&&
				d1.getDate() > d2.getDate()){
			return 1;// lebih besar
		}
		else if(d1.equals(d2))
			return 0; // sama dengan
		else return -1; // lebih kecil*/
		if(d1 != null && d2!=null){
			if(d1.compareTo(d2)==1){
				status = 1;
			}
			else if(d1.compareTo(d2)==0){
				status = 0;
			}
		}
		return status;
	}
	
}
