package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import pohaci.gumunda.titis.application.AttributePicker;
import pohaci.gumunda.titis.application.DoubleAwareDefaultCellRenderer;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

public abstract class LookupPicker extends AttributePicker implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JTable m_table;
  JButton m_okBt, m_cancelBt;
  LookupDlg m_lookupdlg;

  String m_title = "";
  Dimension m_dimension = new Dimension(300, 300);
  DefaultTableModelAdapter m_model = new DefaultTableModelAdapter();
  JTextField filterField = new JTextField(10);
  private String filterText;
  
  public LookupPicker(Connection conn, long sessionid, String title) {
    super(conn, sessionid);
    m_title = title;
    m_okBt = new JButton("Select");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    m_table = new JTable();
    m_table.setDefaultRenderer(Object.class, new DoubleAwareDefaultCellRenderer());
  }
  
  public LookupPicker(JFrame m_mainframe,Connection conn, long sessionid) {
	    super(conn, sessionid);
	    m_okBt = new JButton("Select");
	    m_okBt.addActionListener(this);
	    m_cancelBt = new JButton("Cancel");
	    m_cancelBt.addActionListener(this);
	    m_table = new JTable();
	  }
  
  public LookupPicker(Connection conn, long sessionid, String title,String filterText) {
	    super(conn, sessionid);
	    m_title = title;
	    m_okBt = new JButton("Select");
	    m_okBt.addActionListener(this);
	    m_cancelBt = new JButton("Cancel");
	    m_cancelBt.addActionListener(this);
	    m_table = new JTable();
	    this.filterText=filterText;
	    filterField.setText(this.filterText);
	  }
  
  public void setSize(int width, int height) {
    m_dimension = new Dimension(width, height);
  }

  public DefaultTableModelAdapter getModel() {
    return m_model;
  }

  public JTable getTable() {
    return m_table;
  }
  
  public void setFilterText(String filterText){
	  filterField.setText(filterText);
  }
  private boolean builtTableModel = false;
  public void done() {
	if (!builtTableModel)
	{
		m_table.setModel(m_model.buildTableModel(filterField));
		//TableComparatorChooser comparatorChooser = 
		//new TableComparatorChooser(m_table,m_model.sortedList,true);
		
		m_table.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
              if(e.getClickCount() >= 2) {
                select();
                m_lookupdlg.dispose();
              }
			}
		});
		builtTableModel = true;
		//Tambahan cok gung biar bisa ngatur ukuran kolom
		aturKolom();
	}
    m_lookupdlg = new LookupDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
    m_lookupdlg.setVisible(true);
  }
  //Tambahan biar bisa ngatur ukuran Kolom
  public void aturKolom(){
	  m_table.getColumnModel().getColumn(0).setMaxWidth(40);
	  m_table.getColumnModel().getColumn(0).setPreferredWidth(40);
	  
  }
  public void done1() {
		if (!builtTableModel)
		{
			m_table.setModel(m_model.buildTableModel(filterField));
			//TableComparatorChooser.install(m_table, m_model.sortedList, AbstractTableComparatorChooser.MULTIPLE_COLUMN_KEYBOARD);
			m_table.addMouseListener(new MouseAdapter()  {
				public void mouseClicked(MouseEvent e) {
	              if(e.getClickCount() >= 2) {
	                select();
	                m_lookupdlg.dispose();
	              }
				}
			});
			builtTableModel = true;
			aturKolom();
		}
	    m_lookupdlg = new LookupDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),1);
	    m_lookupdlg.setVisible(true);
	  }
  public void done2() {
	  if (!builtTableModel)
	  {
		  m_table.setModel(m_model.buildTableModel(filterField));
		  //TableComparatorChooser.install(m_table, m_model.sortedList, AbstractTableComparatorChooser.MULTIPLE_COLUMN_KEYBOARD);
		  m_table.addMouseListener(new MouseAdapter()  {
			  public void mouseClicked(MouseEvent e) {
				  if(e.getClickCount() >= 2) {
					  select();
					  m_lookupdlg.dispose();
				  }
			  }
		  });
		  builtTableModel = true;
		  aturKolom();
	  }
	  m_lookupdlg = new LookupDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),"");
	  m_lookupdlg.setVisible(true);
  }

  abstract void select();

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if(e.getSource() == m_okBt) {
      select();
      m_lookupdlg.dispose();
    }
    else if(e.getSource() == m_cancelBt) {
      m_lookupdlg.dispose();
    }
  }

  /**
   *
   */
  class LookupDlg extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame mainframe;
    JPanel buttonPanel;
    JPanel searchPanel;
    public LookupDlg(JFrame owner) {
      super(owner, m_title, true);
      this.setSize(m_dimension);
      mainframe = owner;
      //JPanel upperPanel = new JPanel();
     // JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      buttonPanel.add(m_okBt);
      buttonPanel.add(m_cancelBt);
      searchPanel = new JPanel(new BorderLayout());
      searchPanel.add(new JLabel("Filter: "),BorderLayout.WEST);
      searchPanel.add(filterField,BorderLayout.CENTER);
      Box vbox = new Box(BoxLayout.Y_AXIS);
      vbox.add(buttonPanel);
      vbox.add(searchPanel);
      getContentPane().setLayout(new BorderLayout());
      //getContentPane().add(buttonPanel, BorderLayout.NORTH);
      getContentPane().add(vbox, BorderLayout.NORTH);
      getContentPane().add(new JScrollPane(m_table), BorderLayout.CENTER);
    }
//Overload constructor by cok gung untuk handle lookup tanpa tombol select nich
    public LookupDlg(JFrame owner,int i) {
        super(owner, m_title, true);
        this.setSize(m_dimension);
        mainframe = owner;
        //JPanel upperPanel = new JPanel();
       // JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(m_okBt);
        buttonPanel.add(m_cancelBt);
        searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Filter: "),BorderLayout.WEST);
        searchPanel.add(filterField,BorderLayout.CENTER);
        Box vbox = new Box(BoxLayout.Y_AXIS);
       // vbox.add(buttonPanel);
        vbox.add(searchPanel);
        getContentPane().setLayout(new BorderLayout());
        //getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(vbox, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(m_table), BorderLayout.CENTER);
      }
//  Overload constructor by popim untuk handle lookup tanpa komponen filter
    public LookupDlg(JFrame owner,String i) {
    	  super(owner, m_title, true);
          this.setSize(m_dimension);
          mainframe = owner;
          //JPanel upperPanel = new JPanel();
         // JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
          buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
          buttonPanel.add(m_okBt);
          buttonPanel.add(m_cancelBt);
          searchPanel = new JPanel(new BorderLayout());
          /*searchPanel.add(new JLabel("Filter: "),BorderLayout.WEST);
          searchPanel.add(filterField,BorderLayout.CENTER);*/
          Box vbox = new Box(BoxLayout.Y_AXIS);
          vbox.add(buttonPanel);
          vbox.add(searchPanel);
          getContentPane().setLayout(new BorderLayout());
          //getContentPane().add(buttonPanel, BorderLayout.NORTH);
          getContentPane().add(vbox, BorderLayout.NORTH);
          getContentPane().add(new JScrollPane(m_table), BorderLayout.CENTER);
    }
    
    public void setVisible( boolean flag ){
      Rectangle rc = mainframe.getBounds();
      Rectangle rcthis = getBounds();
      setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
                (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
                (int)rcthis.getWidth(), (int)rcthis.getHeight());

      super.setVisible(flag);
    }
  }

  /**
   *
   */
  static class DefaultTableModelAdapter  {
	  //	---------- part one : mandatory object composition
	  private ArrayList itemsList = new ArrayList();
	  private ArrayList itemHeaderList = new ArrayList();
	  //---------- part two : only exists after buildTableModel call
	  private EventList eventList;
	  SortedList sortedList;
	  private FilterList filterList;
	  private EventTableModel eventTableModel;
	  
	  public void addColumn(String string) {
		  itemHeaderList.add(string);
	  }
	  
	  public void addRow(Object[] objects) {
		  if (eventList==null)		
			  itemsList.add(objects);
		  else
			  eventList.add(objects);
	  }
	  public TableModel buildTableModel(JTextField filterField)
	  {
		  Comparator comparator = new Comparator() {
			  public int compare(Object o1, Object o2) {
				  Object[] oa1,oa2;
				  oa1=(Object[]) o1;
				  oa2=(Object[]) o2;
				  Comparable oa1_0 = (Comparable) oa1[0];
				  Comparable oa2_0 = (Comparable) oa2[0];
				  return oa1_0.compareTo(oa2_0);
			  }
		  };
		  
		  eventList = GlazedLists.eventList(itemsList);
		  sortedList = new SortedList(eventList,comparator);
		  TextFilterator filterator = new Filterator();
		  if (filterField!=null)
			  filterList = new FilterList(sortedList,
					  new TextComponentMatcherEditor(filterField,filterator ,true) );
		  else
			  filterList = new FilterList(sortedList);
		  TableFormat tableFormat = new TableFormat() 
		  {
			  public int getColumnCount() {
				  return itemHeaderList.size();
			  }
			  public String getColumnName(int column) {
				  return (String) itemHeaderList.get(column);
			  }
			  public Object getColumnValue(Object baseObject, int column) {
				  Object[] objArr = (Object[]) baseObject;
				  if (column>= objArr.length)
					  return null;
				  return objArr[column];
			  }
		  };
		  eventTableModel = new EventTableModel(filterList,tableFormat );
		  return eventTableModel;
	  }
	  public Object getValueAt(int rowindex, int i) {
		  if (eventTableModel!=null)
			  return eventTableModel.getValueAt(rowindex,i);
		  return null;
	  }
	  
	  public void clearRows() {
		  if (eventList==null)
			  itemsList.clear();
		  else
			  eventList.clear();
	  }
  }
  static class Filterator implements TextFilterator
  {
	  public void getFilterStrings(List baseList, Object element) {
		  Object[] oarray = (Object[]) element;
		  int i;
		  for (i=0; i<oarray.length;i++)
			  if (oarray[i]!=null)
				  baseList.add(oarray[i].toString());
	  }
	  
  }
}