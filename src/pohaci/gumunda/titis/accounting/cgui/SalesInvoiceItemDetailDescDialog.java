package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;

public class SalesInvoiceItemDetailDescDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;	
	private ItemTable DetailItemTable;
	private JButton AddButton;
	private JButton DeleteButton;
	private JButton SaveButton;
	private JButton CancelButton;
	private ArrayList detailDescList = new ArrayList();
	private int response;
	
	public SalesInvoiceItemDetailDescDialog(JFrame owner, Connection conn, long sessionId, ArrayList list) {
		super(owner, "Sales Invoice Detail Description", true);		
		this.detailDescList = list;
		this.response = JOptionPane.CANCEL_OPTION;
		setSize(500, 300);
		constructComponents();
		addListeners();
		initData();
	}
	
	public void initData(){
		if(detailDescList!=null && detailDescList.size()!=0){
			DetailItemTable.clear();
			
			Iterator iter = this.detailDescList.iterator();
			while(iter.hasNext()){
				Object[] objs = (Object[]) iter.next();
				DetailItemTable.addRow(objs);
			}
			DetailItemTable.upadateSummary();
		}
	}

	public int getResponse() {
		return response;
	}
	
	public ArrayList getDetailDescList() {
		return detailDescList;
	}

	public void setDetailDescList(ArrayList detailDescList) {
		this.detailDescList = detailDescList;
	}

	private void constructComponents() {
		DetailItemTable = new ItemTable();
		AddButton = new JButton("Add");
		DeleteButton = new JButton("Delete");
		SaveButton = new JButton("Save");
		CancelButton = new JButton("Cancel");
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(AddButton);
		buttonPanel.add(DeleteButton);
		buttonPanel.add(SaveButton);
		buttonPanel.add(CancelButton);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(buttonPanel, BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(DetailItemTable), BorderLayout.CENTER);
	}
	
	private void addListeners() {
		AddButton.addActionListener(this);
		DeleteButton.addActionListener(this);
		SaveButton.addActionListener(this);
		CancelButton.addActionListener(this);
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==AddButton){
			doAdd();
		}
		if(e.getSource()==DeleteButton){
			doDelete();	
		}
		if(e.getSource()==SaveButton){
			doSave();
		}
		if(e.getSource()==CancelButton){
			doCancel();
		}
	}

	private void doCancel() {
		DetailItemTable.stopCellEditing();
		this.response = JOptionPane.CANCEL_OPTION;
		dispose();
	}

	private void doSave() {
		DetailItemTable.stopCellEditing();
		DetailItemTable.getItemList();
		this.response = JOptionPane.OK_OPTION;
		dispose();
	}

	private void doDelete() {
		int row = -1;
		row = DetailItemTable.getSelectedRow();
		if(row>-1){
			DetailItemTable.deleteRow(row);	
		}
	}

	private void doAdd() {
		DetailItemTable.addRow(new Object[]{null, null, null, null, null, null,null});
	}
	
	protected class MyTableModelListener implements TableModelListener{

		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			Object obj = e.getSource();
			if(obj instanceof DefaultTableModel){
				DefaultTableModel model = (DefaultTableModel) obj;
				
				if((row>-1)&&((col==3)||(col==4))){
					Double qty = (Double) model.getValueAt(row, 3);
					Double unitPrice = (Double) model.getValueAt(row, 4);
					if((qty!=null)&&(unitPrice!=null)){
						double amount = qty.doubleValue() * unitPrice.doubleValue();
						model.setValueAt(new Double(amount), row, 5);
						DetailItemTable.upadateSummary();
					}
				}
			}
		}
		
	}
	
	protected class ItemTable extends JTable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int lastNo;
		int lastRow;
		
		public ItemTable() {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("No");
			model.addColumn("Description");
			model.addColumn("Specification");
			model.addColumn("Qty");
			model.addColumn("Unit Price");
			model.addColumn("Amount");

			setModel(model);
			
			getColumnModel().getColumn(0).setPreferredWidth(30);
	    	getColumnModel().getColumn(0).setMinWidth(30);
	    	getColumnModel().getColumn(0).setMaxWidth(30);
	    	
	    	clear();
	    	getModel().addTableModelListener(new MyTableModelListener());
		}
		
		public void getItemList() {
			detailDescList = new ArrayList();
			int max = getRowCount() - 2;
			for(int i=0; i<max; i++){
				ArrayList list = new ArrayList();
				for(int j=0; j<=5; j++){
					Object data = getValueAt(i, j);
					list.add(data);
				}
				Object[] rowData = (Object[]) list.toArray(new Object[list.size()]);
				detailDescList.add(rowData);
			}
		}

		public void clear(){
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
			
			model.addRow(new Object[]{null, null, null, null, null, null});
			model.addRow(new Object[]{null, null, null, null, "TOTAL", new Double(0)});
			
			this.lastNo = 0;
			lastRow = 0;
		}
		
		public void addRow(Object[] data){
			int oldRow = lastRow;
			DefaultTableModel model = (DefaultTableModel) getModel();
			lastNo++;
			lastRow++;
			data = addNo(lastNo, data);
			model.insertRow(oldRow, data);
			
		}
		
		public void deleteRow(int row) {
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);
			this.lastNo--;
			lastRow--;
			//updateNumbering();
		}

		private Object[] addNo(int no, Object[] data) {
			ArrayList list = new ArrayList();
			for(int i=0; i<data.length; i++){
				list.add(data[i]);
			}
			return (Object[]) list.toArray(new Object[list.size()]);
		}
		
		/*private void updateNumbering() {
			for(int i=0; i<this.lastNo; i++){
				if(((Integer)getValueAt(i,0)).intValue()!=i)
					setValueAt(new Integer(i+1), i, 0);
			}
		}*/
		
		public TableCellEditor getCellEditor(int row, int column) {
			if(column==3)
				//return new DblCellEditor();
				return new FormattedDoubleCellEditor("###,##0.0000;(###,##0.0000)", JLabel.RIGHT);
			if(column==4)
				//return new DblCellEditor();
				return new FormattedDoubleCellEditor("###,##0.0000;(###,##0.0000)", JLabel.RIGHT);
			return super.getCellEditor(row, column);
		}

		public TableCellRenderer getCellRenderer(int row, int column) {
			if(column>=0){
				if(!isSummaryRow(row)){
					if(column==3 || column==4)
						return new FormattedDoubleCellRenderer("###,##0.0000;(###,##0.0000)", JLabel.RIGHT, Font.PLAIN);
					if(column==5)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
				}else
					return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
			}
			return new FormattedStandardCellRenderer(Font.PLAIN, JLabel.LEFT);
		}

		public boolean isCellEditable(int row, int column) {
			if (!isSummaryRow(row)) {
				if (column == 5)
					return false;
				return true;
			}
			return false;
		}
		
		private boolean isSummaryRow(int row) {
			int maxRow = getRowCount()-1;
			return ((row>=(maxRow-1))&&(row<=maxRow));
		}
		
		public void upadateSummary(){
			int maxRow = getRowCount()-1;
			double total = 0;
			
			for(int i=0; i<maxRow-1; i++){
				Double amt = (Double) getValueAt(i, 5);
				total += amt.doubleValue();
			}
			
			setValueAt(new Double(total), maxRow, 5);
		}
		
		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}
	}
}
