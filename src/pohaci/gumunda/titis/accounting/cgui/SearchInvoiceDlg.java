package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;
import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.entity.InvoiceLoader;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.project.cgui.InvoicePanel;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.InvoicePanel.InvoiceTable;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;


public class SearchInvoiceDlg extends JDialog implements ActionListener{
	
		private static final long serialVersionUID = 1L;
		SearchTable m_searchTable;
		JButton m_selectBt;
		int m_iResponse = JOptionPane.NO_OPTION;
		JFrame m_mainframe;
		InvoicePanel m_panel;
		ProjectData m_project;
		InvoiceTable m_invtable;
		JRadioButton m_andRadioBt, m_orRadioBt;
		JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
		JButton m_findBt, m_closeBt, m_clearBt;
		
		Connection m_conn = null;
		long m_sessionid = -1;
		InvoiceLoader loader;
		private JComboBox m_statusComboBox; 
		          	
		
		public SearchInvoiceDlg(JFrame owner, String title, Connection conn, long sessionid,InvoiceTable table,
				ProjectData project){
			super(owner, ( title == null ) ? "Search Invoice" : title, true);
			m_invtable = table;
			m_project = project;
			m_mainframe = owner;
			m_conn = conn;
			m_sessionid = sessionid;
			setSize(380, 360);    
			constructComponent();
		}
		
		void constructComponent() {
			m_statusComboBox = new JComboBox(new Object[]{"Not Submitted","Submitted","Posted","All"});//0,1,3,*
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(criteriaPanel(), BorderLayout.NORTH);
		}
		
		JPanel criteriaPanel() {
			m_searchTable = new SearchTable();
			m_andRadioBt = new JRadioButton("and");
			m_andRadioBt.setSelected(true);
			m_orRadioBt = new JRadioButton("or");
			
			m_containsRadioBt = new JRadioButton("Text Contains Criteria");
			m_containsRadioBt.setSelected(true);
			m_matchRadioBt = new JRadioButton("Match Case");
			m_wholeRadioBt = new JRadioButton("Find Whole Words only");
			m_findBt = new JButton("Find");
			m_findBt.addActionListener(this);
			m_closeBt = new JButton("Cancel");
			m_closeBt.addActionListener(this);
			m_clearBt = new JButton("Clear");
			m_clearBt.addActionListener(this);
			
			ButtonGroup bg = new ButtonGroup();
			bg.add(m_andRadioBt);
			bg.add(m_orRadioBt);
			
			ButtonGroup bg2 = new ButtonGroup();
			bg2.add(m_containsRadioBt);
			bg2.add(m_matchRadioBt);
			bg2.add(m_wholeRadioBt);
			
			JPanel centerPanel = new JPanel();
			JPanel criteriaPanel = new JPanel();
			JPanel buttonPanel = new JPanel();
			JPanel operatorPanel = new JPanel();
			JPanel optionPanel = new JPanel();
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gridBagConstraints;
			
			scrollPane.setPreferredSize(new Dimension(100, 120));
			scrollPane.getViewport().add(m_searchTable);
			
			operatorPanel.setLayout(new GridLayout(2, 1));
			operatorPanel.add(m_andRadioBt);
			operatorPanel.add(m_orRadioBt);
			
			optionPanel.setLayout(new GridLayout(3, 1));
			optionPanel.add(m_containsRadioBt);
			optionPanel.add(m_matchRadioBt);
			optionPanel.add(m_wholeRadioBt);
			
			criteriaPanel.setLayout(new GridBagLayout());
			gridBagConstraints = new GridBagConstraints();
			
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets = new Insets(1, 0, 2, 0);
			gridBagConstraints.weightx = 1.0;
			criteriaPanel.add(new JLabel("Search Criteria"), gridBagConstraints);
			
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new Insets(1, 0, 5, 0);
			criteriaPanel.add(scrollPane, gridBagConstraints);
			
			gridBagConstraints.gridy = 2;
			gridBagConstraints.insets = new Insets(1, 0, 2, 0);
			criteriaPanel.add(new JLabel("Search Operator"), gridBagConstraints);
			
			gridBagConstraints.gridy = 3;
			gridBagConstraints.insets = new Insets(1, 0, 5, 0);
			criteriaPanel.add(operatorPanel, gridBagConstraints);
			
			gridBagConstraints.gridy = 4;
			gridBagConstraints.insets = new Insets(1, 0, 2, 0);
			criteriaPanel.add(new JLabel("Option"), gridBagConstraints);
			
			gridBagConstraints.gridy = 5;
			criteriaPanel.add(optionPanel, gridBagConstraints);
			
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPanel.add(m_findBt);
			buttonPanel.add(m_clearBt);
			buttonPanel.add(m_closeBt);
			
			centerPanel.setLayout(new BorderLayout());
			centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
			centerPanel.add(criteriaPanel, BorderLayout.NORTH);
			centerPanel.add(buttonPanel, BorderLayout.SOUTH);
			
			return centerPanel;
		}
		
		public void setVisible( boolean flag ){
			Rectangle rc = m_mainframe.getBounds();
			Rectangle rcthis = getBounds();
			setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
					(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
					(int)rcthis.getWidth(), (int)rcthis.getHeight());
			
			super.setVisible(flag);
		}
		
		public int getResponse() {
			return m_iResponse;
		}
		
		void find() {
			String query = "";
		    try {
		    	query = getCriterion();
		    }
		    catch(Exception ex) {
		      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                      JOptionPane.WARNING_MESSAGE);
		      return;
		    }

		    try {
		    	DecimalFormat desimalFormat = new DecimalFormat("#,##0.00");
		    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		    	GenericMapper mapper = MasterMap.obtainMapperFor(SalesInvoice.class);
				mapper.setActiveConn(m_conn);
				
				System.err.println(query);
				List list = mapper.doSelectWhere(query);
				
				DefaultTableModel model = (DefaultTableModel) m_invtable.getModel();
				model.setRowCount(0);
				
				Iterator iter = list.iterator();
				int i=1;
				while(iter.hasNext()){
					SalesInvoice inv = (SalesInvoice) iter.next();
					model.addRow(new Object[]{String.valueOf(i++),inv.getReferenceNo(),dateFormat.format(inv.getTransactionDate()),
						inv.getCustomer().getName(),inv.getCustomer().getAddress(),inv.getDescription(), 
						inv.getSalesCurr().getSymbol()+ " " + desimalFormat.format(inv.getSalesAmount()),
						inv.getVatCurr().getSymbol() + " " + desimalFormat.format(inv.getVatAmount()), 
						getStatus(inv.getStatus())});
				}
		    }
		    catch(Exception ex) {
		      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
		                                    JOptionPane.WARNING_MESSAGE);
		    }
		}
		
		void clear() {
			m_searchTable.clear();
		}
		
		private String getStatus(long status){
			  String  strStatus = "";
			  if (status==0)
				  strStatus = "Not Submitted";
			  else if (status==1 || status==2)
				  strStatus = "Submitted";
			  else 
				  strStatus = "Posted";
			  return strStatus;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == m_findBt) {
				find();    	
			}else if (e.getSource()==m_clearBt){
				m_searchTable.clear();
			}else if (e.getSource()==m_closeBt){
				dispose();
			}
			
		}
		
		public String getCriterion() throws Exception{
			String operator, equality , criteria = "",temp="";
	        int row = 0;      
	        boolean first = true;
	        
	        String strquery = IDBConstants.ATTR_CUSTOMER + " = (SELECT " + 
        			IDBConstants.ATTR_CUSTOMER + " FROM " +  IDBConstants.TABLE_PROJECT_DATA  + 
        			" WHERE " + IDBConstants.ATTR_CODE + " = '";
			String strcode = m_project.getCode() + "') ";
	        
			String querytable = strquery + strcode;
			
	        if(m_andRadioBt.isSelected())
	          operator = " and ";
	        else
	          operator = " or ";
	        
	        if(m_containsRadioBt.isSelected()){
	        	equality = " like ";
	        }
	        else equality = " = ";

	        String value = "";
	        java.util.Date date = null;
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        System.err.println(value);
	        java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd-MM-yyyy"),
	        sf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
	       
	        try {
	        	if(!value.equals("")) {
	        		date = sf.parse(value);
	        		value = sf2.format(date);
	        	}
	        }
	        catch(Exception ex) {
	        	throw new Exception(ex.getMessage());
	        }
	        
	        // Invoice Date      
	        if(!value.equals("")){    	  
	      	  	if (first){       	  
	            	temp = "";
	            	first = false;
	            }else {
	            	temp = operator;        	
	            }
	            criteria += temp + " transactiondate = '" + value + "'";
	        }
	        
	        //Invoice No
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        if(!value.equals("")){
	        	if(m_containsRadioBt.isSelected())
	          	  	value = "%" + value + "%";
	        	if (first){       	  
	        		temp = "";
	        		first = false;
	        	}else {
	        		temp = operator;        	
	        	}            
	            criteria += temp + " referenceno " + equality + "'" + value + "'";
	        }

	        // Customer
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        System.err.println(value);
	        if(!value.equals("")){
	        	if(m_containsRadioBt.isSelected())
	          	  	value = "%" + value + "%";
	        	if (first){       	  
	        		temp = "";
	        		first = false;
	        	}else {
	        		temp = operator;        	
	        	}            
	            criteria += temp + " customer = (select autoindex from customer where name "+ equality + " '" + value + "')";
	        }
	        
	        //Invoicing Address
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        if(!value.equals("")){
	        	if(m_containsRadioBt.isSelected())
	          	  	value = "%" + value + "%";
	        	if (first){       	  
	        		temp = "";
	        		first = false;
	        	}else {
	        		temp = operator;        	
	        	}            
	            criteria += temp + " customer = (select autoindex from customer where street "+ equality + " '" + value + "')";
	        }
	        
	        // description
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        if(!value.equals("")){
	        	if(m_containsRadioBt.isSelected())
	          	  	value = "%" + value + "%";
	        	if (first){       	  
	        		temp = "";
	        		first = false;
	        	}else {
	        		temp = operator;        	
	        	}            
	            criteria += temp + IDBConstants.ATTR_DESCRIPTION + equality + " '" + value + "'";
	        }
	        
	        //Status
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        if(!value.equals("All")){
	        	if(value.equals("Submitted")){
	        		value = "1 or " + IDBConstants.ATTR_STATUS + " = 2";
		        }else if(value.equals("Not Submitted")){
		        	value = "0";
		        }else if(value.equals("Posted"))
		        	value = "3";
		        
		        if (first){       	  
	        		temp = "";
	        		first = false;
	        	}else {
	        		temp = operator;        	
	        	}
	        	criteria += temp + "( " + IDBConstants.ATTR_STATUS + " = " + value + ")";
	        }
	        
	        String status = (String)m_searchTable.getValueAt(5, 1);
	        if(criteria.equals("")|| (status == "All" && m_orRadioBt.isSelected()))
	        	return querytable ;
	        else{
	        	System.err.println("query select invoice by criteria : " + querytable + " and " + criteria );
	        	return querytable + " and " + criteria;
	        }
		}
	
		public class SearchTable extends JTable {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
			public SearchTable() {
						
				SearchTableModel model = new SearchTableModel();
				model.addColumn("Attribute");
				model.addColumn("Description");
				
				model.addRow(new Object[]{"Invoice Date", ""});/*dateformat.format(new Date())*/
				model.addRow(new Object[]{"Invoice No", ""});
				model.addRow(new Object[]{"Customer", ""});      
				model.addRow(new Object[]{"Invoicing Address", ""});
				model.addRow(new Object[]{"Description", ""});
				model.addRow(new Object[]{"Status", "All"});
				
				
				setModel(model);
				getColumnModel().getColumn(0).setPreferredWidth(100);
				getColumnModel().getColumn(0).setMaxWidth(100);
				getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
			}
			
			public TableCellEditor getCellEditor(int row, int col) {
				if((row == 0 ) && col == 1)
					return new DateCellEditor(GumundaMainFrame.getMainFrame());
				/*else if(row == 2 && col == 1)
					return new ProjectDataCellEditor(GumundaMainFrame.getMainFrame(),
							//m_conn, m_sessionid);*/
				else if(row == 5 && col == 1)
					return new DefaultCellEditor(m_statusComboBox);
				return new BaseTableCellEditor();     
			}
			
			public void stopCellEditing() {
				TableCellEditor editor;
				if((editor = getCellEditor()) != null)
					editor.stopCellEditing();
			}
			
			public void clear() {
				stopCellEditing();
				int row = getRowCount();
				for(int i = 0; i < row; i ++)
					if (i==0)
						setValueAt(dateformat.format(new Date()), i, 1);
					else if (i==5)
						setValueAt("All", i, 1);
					else 
						setValueAt("", i, 1);
				}   
			
		}
		
		class SearchTableModel extends DefaultTableModel {
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int col) {
				if(col == 0)
					return false;
				return true;
			}
		}
		
	}
