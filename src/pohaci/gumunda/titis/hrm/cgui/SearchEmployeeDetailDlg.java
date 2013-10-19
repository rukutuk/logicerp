package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class SearchEmployeeDetailDlg extends SearchEmployeeDlg  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
EmployeeTable m_detailtable;
  JButton m_selectBt;
  JButton m_selectNoneBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  Employee[] m_employee = new Employee[0];
private boolean allowMultiple = false;

  public SearchEmployeeDetailDlg(JFrame owner, Connection conn, long sessionid) {
  //  super(owner, null, conn, sessionid);
    super(owner,conn,sessionid);
	setModal(true);
    setSize(450, 650);
    find();
  }
  
  public SearchEmployeeDetailDlg(JFrame owner, Connection conn, long sessionid, boolean allowMultiple) {
		// super(owner, null, conn, sessionid);
		super(owner, conn, sessionid);
		this.allowMultiple = allowMultiple;
		setModal(true);
		setSize(450, 650);
		find();
	}

  void constructComponent() {
    m_detailtable = new EmployeeTable();
    m_selectBt = new JButton("Select");
    m_selectBt.addActionListener(this);
    
    m_selectNoneBt = new JButton("Select None");
    m_selectNoneBt.addActionListener(this);

    JPanel centerPanel = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_selectNoneBt);
    buttonPanel.add(m_selectBt);

    centerPanel.add(new JScrollPane(m_detailtable), BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);
   
    JPanel centerPanel_2 = new JPanel(new BorderLayout());
    centerPanel_2.setBorder(BorderFactory.createEmptyBorder(0,6,4,6));
    centerPanel_2.add(centerPanel, BorderLayout.CENTER);
   
    setBorder(centerPanel,"");

    getContentPane().add(criteriaPanel(), BorderLayout.NORTH);
    getContentPane().add(centerPanel_2, BorderLayout.CENTER);
  }

  void find() {
    String query = "";
    try {
      query = m_table.getCriterion();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn); 
      m_employee = logic.getEmployeeByCriteria(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, query);
      m_detailtable.setEmployee(m_employee);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void clear() {
    super.clear();
    m_detailtable.clear();
  }

  public int getResponse() {
    return m_iResponse;
  }

  public Employee[] getEmployee() {
    return m_employee;
  }
  
  void pilihEmployee(int empId){
  	int i;
  	boolean found=false;
	Employee emp=null;
  	for (i=0; i< m_employee.length; i++)
  	{
  		emp = m_employee[i];
  		if (emp.getIndex()==empId)
  		{
  			found=true;
  			break;
  		}
  	}
  	if (!found) throw new RuntimeException("Employee with id " + empId + " not found!");
  	m_employee = new Employee[1];
  	m_employee[0] = emp;
	m_iResponse = JOptionPane.OK_OPTION;
	try {
		EventQueue.invokeAndWait(new Runnable()
		{
			final Dialog dlg = SearchEmployeeDetailDlg.this;
			public void run() {
				dlg.setVisible(false);
				dispose();
			}
		} 
		);
	} catch (InterruptedException e) {
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		e.printStackTrace();
	}
	
  }
  void onAdd() {
	  int[] row = m_detailtable.getSelectedRows();
	  java.util.Vector vresult = new java.util.Vector();    
	  
	  if(!allowMultiple){
		  if(row.length>0){
			  Employee employee = (Employee)m_employee[row[0]];
			  vresult.addElement(new Employee(employee.getIndex(), employee));
		  }
	  } else{
		  for(int i=0; i<row.length; i++){
			  Employee employee = (Employee)m_employee[row[i]];
			  vresult.addElement(new Employee(employee.getIndex(), employee));
		  }
	  }
	  
	  m_employee = new Employee[vresult.size()];
	  vresult.copyInto(m_employee);
	  m_iResponse = JOptionPane.OK_OPTION;
	  dispose();
	  
  }

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if(e.getSource() == m_selectBt) {
      onAdd();
    } 
    if(e.getSource() == m_selectNoneBt) {
    	m_employee = null;
    	m_iResponse = JOptionPane.OK_OPTION;
    	dispose();
    }
  }

  /**
   *
   */
  class EmployeeTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeTable() {
      EmployeeTableModel model = new EmployeeTableModel();
      model.addColumn("No");
      model.addColumn("Code");
      model.addColumn("Name");
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
      
      addMouseListener(new MouseAdapter() {
          public void mouseClicked( MouseEvent e ) {
            if(e.getClickCount() >= 2) {
              onAdd();
            }
          }
        });
    }

    void setEmployee(Employee[] employee) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);      
      for(int i = 0; i < employee.length; i ++) {
        //System.err.println(employee[i].getEmployeNo());
        //OtherEmployee emp = new OtherEmployee(employee[i].getIndex(), employee[i]);
        model.addRow(new Object[]{
        String.valueOf((i + 1)), employee[i].getEmployeeNo(), employee[i].getFirstName() + " " + employee[i].getMidleName() + " " + employee[i].getLastName()
      });
      }
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }
  }

  /**
   *
   */
  class EmployeeTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }

  /**
   *
   */
  class OtherEmployee extends Employee {
    public OtherEmployee(long index, Employee employee) {
      super(index, employee);
    }

    public String toString() {
      return m_no;
    }
  }
}