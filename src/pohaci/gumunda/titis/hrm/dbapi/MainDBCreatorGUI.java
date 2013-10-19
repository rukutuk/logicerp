package pohaci.gumunda.titis.hrm.dbapi;

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

import javax.swing.*;

import com.pohaci.titis.testconnection.MyConnection;

import java.sql.*;

import pohaci.cgui.TabelModel;
import pohaci.gumunda.aas.dbapi.ConnectionManager;
import pohaci.gumunda.titis.hrm.cgui.IConstants;

public class MainDBCreatorGUI extends JFrame implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JTable m_table;
	JButton m_btCreate = new JButton("Create"), m_btInit = new JButton("Init"),
	m_btDrop = new JButton("Drop"), m_btdeInit = new JButton("deInit");
	ThisTableModel tModel = new ThisTableModel();
	Connection m_conn = null;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	JButton m_btCheck = new JButton("Pilih Semua"), m_btUnCheck = new JButton(
	"Semua Tidak");

  public MainDBCreatorGUI() {
    super("HRM DBCreator");

    setSize(400, 500);
    setLocation((dim.width / 2) - (getWidth() / 2), (dim.height / 2)
                - (getHeight() / 2));
    construct();
    try {
      MyConnection m_connectionManager = new MyConnection();
      //ConnectionManager m_connectionManager = new ConnectionManager("sampurna");
      m_conn = m_connectionManager.getConnection();
      setVisible(true);
    } catch (Exception ex) {
      javax.swing.JOptionPane.showMessageDialog(null, ex.toString());
      System.err.println(ex);
      System.exit(0);
    }
  }

  public static void main(String[] args) {
	  try {
		  /*javax.swing.UIManager
		   .setLookAndFeel("com.digitprop.tonic.TonicLookAndFeel");*/
		  UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
		  //javax.swing.UIManager.setLookAndFeel("net.sourceforge.napkinlaf.NapkinLookAndFeel");
		  javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource( "Tahoma", 0, 11);
		  java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
		  while (keys.hasMoreElements()) {
			  Object key = keys.nextElement();
			  Object value = javax.swing.UIManager.get(key);
			  if (value instanceof javax.swing.plaf.FontUIResource)
				  javax.swing.UIManager.put(key, f);
		  }

	  } catch (Exception ex) {
		  System.out.println(ex);
	  }
	  new MainDBCreatorGUI();
  }



  void construct() {
    JPanel p = new JPanel();
    p.add(m_btCheck);
    m_btCheck.addActionListener(this);
    p.add(m_btUnCheck);
    m_btUnCheck.addActionListener(this);
    p.setBorder(BorderFactory.createEtchedBorder());

    JPanel pcheck = new JPanel();
    pcheck.add(p);

    m_table = new JTable();
    m_table.setModel(tModel);
    JScrollPane scroll = new JScrollPane(m_table);
    m_table.getTableHeader().setReorderingAllowed(false);

    JPanel pbt = new JPanel();
    pbt.add(m_btCreate);
    m_btCreate.addActionListener(this);
    pbt.add(m_btInit);
    m_btInit.addActionListener(this);
    pbt.add(m_btdeInit);
    m_btdeInit.addActionListener(this);
    pbt.add(m_btDrop);
    m_btDrop.addActionListener(this);

    JPanel centerpanel = new JPanel(new GridLayout(2, 1));
    centerpanel.add(pcheck);
    centerpanel.add(pbt);
    centerpanel.setBorder(BorderFactory.createEtchedBorder());

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(centerpanel, "South");
    getContentPane().add(scroll, "Center");
    setData();
  }

  public void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING)
      System.exit(0);
  }

  boolean isOk(int row) {
    Boolean bl = (Boolean) m_table.getValueAt(row, 1);
    return bl.booleanValue();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == m_btCreate) {
      try {
        m_conn.setAutoCommit(false);
        Statement stm = m_conn.createStatement();

        if (isOk(0))
          DBCreatorSAP.createOrganizationTable(stm);
        if (isOk(1))
          DBCreatorSAP.createOrganizationStructureTable(stm);
        if (isOk(2))
          DBCreatorSAP.createQualificationTable(stm);
        if (isOk(3))
          DBCreatorSAP.createJobTitleTable(stm);

        if (isOk(4))
          DBCreatorSAP.createWorkAgreementTable(stm);
        if (isOk(5))
          DBCreatorSAP.createEducationTable(stm);
        if (isOk(6))
          DBCreatorSAP.createReligionTable(stm);

        if (isOk(7))
          DBCreatorSAP.createSexTypeTable(stm);
        if (isOk(8))
          DBCreatorSAP.createFamilyRelationTable(stm);
        if (isOk(9))
          DBCreatorSAP.createMaritalStatusTable(stm);

        if (isOk(10))
          DBCreatorSAP.createLeaveTypeTable(stm);
        if (isOk(11))
          DBCreatorSAP.createPermitionTypeTable(stm);
        if (isOk(12))
          DBCreatorSAP.createOfficeHourPermitionTable(stm);

        if (isOk(13))
          DBCreatorSAP.createAllowenceMultiplierTable(stm);
        if (isOk(14))
          DBCreatorSAP.createPTKPTable(stm);
        if (isOk(15))
          DBCreatorSAP.createTaxArt21TariffTable(stm);

        if (isOk(16))
          DBCreatorSAP.createEmployeeTable(stm);
        if (isOk(17))
          DBCreatorSAP.createEmployeeQualificationTable(stm);
        if (isOk(18))
          DBCreatorSAP.createEmployeeEmploymentTable(stm);
        if (isOk(19))
          DBCreatorSAP.createEmployeeEducationTable(stm);
        if (isOk(20))
          DBCreatorSAP.createEmployeeCertificationTable(stm);
        if (isOk(21))
          DBCreatorSAP.createEmployeeFamilyTable(stm);
        if (isOk(22))
          DBCreatorSAP.createEmployeeAccountTable(stm);
        if (isOk(23))
          DBCreatorSAP.createEmployeeRetirementTable(stm);

        if (isOk(24))
          DBCreatorSAP.createDefaultWorkingDayTable(stm);
        if (isOk(25))
          DBCreatorSAP.createDefaultWorkingTimeTable(stm);
        if (isOk(26))
          DBCreatorSAP.createHolidayTable(stm);

        if (isOk(27))
          DBCreatorSAP.createAnnualLeaveRightTable(stm);
        if (isOk(28))
          DBCreatorSAP.createOvertimeMultiplierTable(stm);
        if (isOk(29))
          DBCreatorSAP.createPaychequeLabelTable(stm);
        if (isOk(30))
          DBCreatorSAP.createPaychequeLabelStructureTable(stm);
        if (isOk(31))
          DBCreatorSAP.createNonPaychequePeriodeTable(stm);

        if (isOk(32))
          DBCreatorSAP.createEmployeeLeaveTable(stm);
        if (isOk(33))
          DBCreatorSAP.createEmployeePermitionTable(stm);
        if (isOk(34))
          DBCreatorSAP.createEmployeeOfficePermitionTable(stm);
        if (isOk(35))
          DBCreatorSAP.createEmpAbsOffWorkTimeTable(stm);

        if (isOk(36))
          DBCreatorSAP.createPayrollComponentTable(stm);
        if (isOk(37))
          DBCreatorSAP.createPayrollComponentStructureTable(stm);
        if (isOk(38))
          DBCreatorSAP.createPayrollCategoryTable(stm);
        if (isOk(39))
          DBCreatorSAP.createPayrollCategoryComponentTable(stm);

        if (isOk(40))
          DBCreatorSAP.createPayrollCategoryEmployeeTable(stm);
        if (isOk(41))
            DBCreatorSAP.createEmployeePayrollSubmit(stm);
        if (isOk(42))
            DBCreatorSAP.createEmployeePayrollDetail(stm);
        if (isOk(43))
            DBCreatorSAP.createEmployeeMealAllowanceAttribute(stm);
        if (isOk(44))
            DBCreatorSAP.createTransportationAllowanceAttribute(stm);
        if (isOk(45))
            DBCreatorSAP.createOvertimeAttribute(stm);

        if (isOk(46))
        	DBCreatorSAP.createEmployeePayrollComponentTable(stm);

        if (isOk(47))
        	DBCreatorSAP.createTaxArt21ComponentTable(stm);
        if (isOk(48))
        	DBCreatorSAP.createTaxArt21ComponentStructureTable(stm);

        if (isOk(49))
        	DBCreatorSAP.createTaxArt21Submit(stm);
        if (isOk(50))
        	DBCreatorSAP.createTaxArt21SubmitEmployeeDetail(stm);
        if (isOk(51))
        	DBCreatorSAP.createTaxArt21SubmitComponentDetail(stm);

        if (isOk(52))
        	DBCreatorSAP.createOvertime(stm);

        if (isOk(53))
        	DBCreatorSAP.createPayrollCategoryBackup(stm);

        m_conn.commit();
        m_conn.setAutoCommit(true);
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, ex.toString());
        try {
          m_conn.rollback();
          m_conn.setAutoCommit(true);
        } catch (Exception exc) {
        }
      }
    } else if (e.getSource() == m_btDrop) {
      try {
        m_conn.setAutoCommit(false);
        Statement stm = m_conn.createStatement();

        if (isOk(0))
          DBCreatorSAP.deleteOrganizationTable(stm);
        if (isOk(1))
          DBCreatorSAP.deleteOrganizationStructureTable(stm);
        if (isOk(2))
          DBCreatorSAP.deleteQualificationTable(stm);
        if (isOk(3))
          DBCreatorSAP.deleteJobTitleTable(stm);

        if (isOk(4))
          DBCreatorSAP.deleteWorkAgreementTable(stm);
        if (isOk(5))
          DBCreatorSAP.deleteEducationTable(stm);
        if (isOk(6))
          DBCreatorSAP.deleteReligionTable(stm);

        if (isOk(7))
          DBCreatorSAP.deleteSexTypeTable(stm);
        if (isOk(8))
          DBCreatorSAP.deleteFamilyRelationTable(stm);
        if (isOk(9))
          DBCreatorSAP.deleteMaritalStatusTable(stm);

        if (isOk(10))
          DBCreatorSAP.deleteLeaveTypeTable(stm);
        if (isOk(11))
          DBCreatorSAP.deletePermitionTypeTable(stm);
        if (isOk(12))
          DBCreatorSAP.deleteOfficeHourPermitionTable(stm);

        if (isOk(13))
          DBCreatorSAP.deleteAllowenceMultiplierTable(stm);
        if (isOk(14))
          DBCreatorSAP.deletePTKPTable(stm);
        if (isOk(15))
          DBCreatorSAP.deleteTaxArt21TariffTable(stm);

        if (isOk(16))
          DBCreatorSAP.deleteEmployeeTable(stm);
        if (isOk(17))
          DBCreatorSAP.deleteEmployeeQualificationTable(stm);
        if (isOk(18))
          DBCreatorSAP.deleteEmployeeEmploymentTable(stm);
        if (isOk(19))
          DBCreatorSAP.deleteEmployeeEducationTable(stm);
        if (isOk(20))
          DBCreatorSAP.deleteEmployeeCertificationTable(stm);
        if (isOk(21))
          DBCreatorSAP.deleteEmployeeFamilyTable(stm);
        if (isOk(22))
          DBCreatorSAP.deleteEmployeeAccountTable(stm);
        if (isOk(23))
          DBCreatorSAP.deleteEmployeeRetirementTable(stm);

        if (isOk(24))
          DBCreatorSAP.deleteDefaultWorkingDayTable(stm);
        if (isOk(25))
          DBCreatorSAP.deleteDefaultWorkingTimeTable(stm);
        if (isOk(26))
          DBCreatorSAP.deleteHolidayTable(stm);

        if (isOk(27))
          DBCreatorSAP.deleteAnnualLeaveRightTable(stm);
        if (isOk(28))
          DBCreatorSAP.deleteOvertimeMultiplierTable(stm);
        if (isOk(29))
          DBCreatorSAP.deletePaychequeLabelTable(stm);
        if (isOk(30))
          DBCreatorSAP.deletePaychequeLabelStructureTable(stm);
        if (isOk(31))
          DBCreatorSAP.deleteNonPaychequePeriodeTable(stm);

        if (isOk(32))
          DBCreatorSAP.deleteEmployeeLeaveTable(stm);
        if (isOk(33))
          DBCreatorSAP.deleteEmployeePermitionTable(stm);
        if (isOk(34))
          DBCreatorSAP.deleteEmployeeOfficePermitionTable(stm);
        if (isOk(35))
          DBCreatorSAP.deleteEmpAbsOffWorkTimeTable(stm);

        if (isOk(36))
          DBCreatorSAP.deletePayrollComponentTable(stm);
        if (isOk(37))
          DBCreatorSAP.deletePayrollComponentStructureTable(stm);
        if (isOk(38))
          DBCreatorSAP.deletePayrollCategoryTable(stm);
        if (isOk(39))
          DBCreatorSAP.deletePayrollCategoryComponentTable(stm);

        if (isOk(40))
          DBCreatorSAP.deletePayrollCategoryEmployeeTable(stm);
        if (isOk(41))
            DBCreatorSAP.deleteEmployeePayroll(stm);
        if (isOk(42))
            DBCreatorSAP.deleteEmployeePayrollDetail(stm);
        if (isOk(43))
            DBCreatorSAP.deleteEmployeeMealAllowanceAttribute(stm);
        if (isOk(44))
            DBCreatorSAP.deleteTransportationAllowanceAttribute(stm);
        if (isOk(45))
            DBCreatorSAP.deleteOvertimeAttribute(stm);

        if (isOk(46))
        	DBCreatorSAP.deleteEmployeePayrollComponentTable(stm);

        if (isOk(47))
        	DBCreatorSAP.deleteTaxArt21ComponentTable(stm);
        if (isOk(48))
        	DBCreatorSAP.deleteTaxart21ComponentStructureTable(stm);

        if (isOk(49))
        	DBCreatorSAP.deleteTaxArt21Submit(stm);
        if (isOk(50))
        	DBCreatorSAP.deleteTaxArt21SubmitEmployeeDetail(stm);
        if (isOk(51))
        	DBCreatorSAP.deleteTaxArt21SubmitComponentDetail(stm);

        if (isOk(52))
        	DBCreatorSAP.deleteOvertime(stm);

        if (isOk(53))
        	DBCreatorSAP.deletePayrollCategoryBackup(stm);

        m_conn.commit();
        m_conn.setAutoCommit(true);
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, ex.toString());
        try {
          m_conn.rollback();
          m_conn.setAutoCommit(true);
        } catch (Exception exc) {
        }
      }
    } else if (e.getSource() == m_btInit) {
      try {
        Statement stm = m_conn.createStatement();
        m_conn.setAutoCommit(false);

        initApplicationTable(stm);
        initModulTable(stm);

        m_conn.commit();
        m_conn.setAutoCommit(true);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, ex.toString());
        try {
          m_conn.rollback();
          m_conn.setAutoCommit(true);
        } catch (Exception exc) {
        }
      }
    } else if (e.getSource() == m_btdeInit) {
      try {
        Statement stm = m_conn.createStatement();
        m_conn.setAutoCommit(false);

        deInitModulTable(stm);
        deInitApplicationTable(stm);

        m_conn.commit();
        m_conn.setAutoCommit(true);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, ex.toString());
        try {
          m_conn.rollback();
          m_conn.setAutoCommit(true);
        } catch (Exception exc) {
        }
      }
    } else if (e.getSource() == m_btCheck) {
      for (int i = 0; i < m_table.getRowCount(); i++)
        m_table.setValueAt(new Boolean("true"), i, 1);
    } else if (e.getSource() == m_btUnCheck) {
      for (int i = 0; i < m_table.getRowCount(); i++)
        m_table.setValueAt(new Boolean("false"), i, 1);
    }
  }

  void setData() {
    tModel.addRow(new Object[] { "Department", new Boolean(true) });
    tModel.addRow(new Object[] { "Department Structure", new Boolean(true) });
    tModel.addRow(new Object[] { "Qualification", new Boolean(true) });
    tModel.addRow(new Object[] { "Job Title", new Boolean(true) });

    tModel.addRow(new Object[] { "Work Agreement", new Boolean(true) });
    tModel.addRow(new Object[] { "Education", new Boolean(true) });
    tModel.addRow(new Object[] { "Religion", new Boolean(true) });

    tModel.addRow(new Object[] { "Sex Type", new Boolean(true) });
    tModel.addRow(new Object[] { "Family Relation", new Boolean(true) });
    tModel.addRow(new Object[] { "Marital Status", new Boolean(true) });

    tModel.addRow(new Object[] { "Leave Type", new Boolean(true) });
    tModel.addRow(new Object[] { "Permition Type", new Boolean(true) });
    tModel.addRow(new Object[] { "Office Hour Permition", new Boolean(true) });

    tModel.addRow(new Object[] { "Field Allowence Multiplier", new Boolean(true) });
    tModel.addRow(new Object[] { "PTKP", new Boolean(true) });
    tModel.addRow(new Object[] { "Tax Art 21 Tariff", new Boolean(true) });

    tModel.addRow(new Object[] { "Employee", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Qualification", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Employment", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Education", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Certification", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Family", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Account", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Retirement", new Boolean(true) });

    tModel.addRow(new Object[] { "Default Working Day", new Boolean(true) });
    tModel.addRow(new Object[] { "Default Working Time", new Boolean(true) });
    tModel.addRow(new Object[] { "Holiday", new Boolean(true) });
    tModel.addRow(new Object[] { "Annual Leave Right", new Boolean(true) });
    tModel.addRow(new Object[] { "Overtime Multiplier", new Boolean(true) });
    tModel.addRow(new Object[] { "Paycheque Label", new Boolean(true) });
    tModel.addRow(new Object[] { "Paycheque Label Structure", new Boolean(true) });
    tModel.addRow(new Object[] { "Non Paycheque Period", new Boolean(true) });

    tModel.addRow(new Object[] { "Employee Leave", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Permition", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Office Hour Permition", new Boolean(true) });
    tModel.addRow(new Object[] { "Emp Abs Off Work Time", new Boolean(true) });

    tModel.addRow(new Object[] { "Payroll Component", new Boolean(true) });
    tModel.addRow(new Object[] { "Payroll Component Structure", new Boolean(true) });
    tModel.addRow(new Object[] { "Payroll Category", new Boolean(true) });
    tModel.addRow(new Object[] { "Payroll Category Component", new Boolean(true) });
    tModel.addRow(new Object[] { "Payroll Category Employee", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Payroll", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Payroll Detail", new Boolean(true) });
    tModel.addRow(new Object[] { "Employee Meal Allowance Attribute", new Boolean(true) });
    tModel.addRow(new Object[] { "Transportation Allowance Attribute", new Boolean(true) });
    tModel.addRow(new Object[] { "Overtime Attribute", new Boolean(true) });

    tModel.addRow(new Object[] { "Employee Payroll Component", new Boolean(true) });

    tModel.addRow(new Object[] { "Tax Art 21 Component", new Boolean(true) });
    tModel.addRow(new Object[] { "Tax Art 21 Component Structure", new Boolean(true) });

    tModel.addRow(new Object[] { "Tax Art 21 Submit", new Boolean(true) });
    tModel.addRow(new Object[] { "Tax Art 21 Submit Employee Detail", new Boolean(true) });
    tModel.addRow(new Object[] { "Tax Art 21 Submit Component Detail", new Boolean(true) });

    tModel.addRow(new Object[] { "Overtime", new Boolean(true) });

    tModel.addRow(new Object[] { "Payroll Category History (package)", new Boolean(true) });
  }

  class ThisTableModel extends TabelModel {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ThisTableModel() {
      addColumn("Table");
      addColumn("Pilih");
    }

    public Class getColumnClass(int columnindex) {
      if (columnindex == 1)
        return Boolean.class;
      return super.getColumnClass(columnindex);
    }
  }

  static void initApplicationTable(Statement stm) throws SQLException {
    stm.executeUpdate("INSERT INTO "
                      + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_APPLICATION
                      + " VALUES('" + IConstants.APP_HRM + "')");
    System.out.println("init application ok ");
  }

  static void deInitApplicationTable(Statement stm) throws SQLException {
    stm.executeUpdate("DELETE FROM "
                      + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_APPLICATION
                      + " WHERE " + pohaci.gumunda.aas.dbapi.IDBConstants.ATT_NAME
                      + " = '" + IConstants.APP_HRM + "'");
    System.out.println("deinit application ok ");

  }

  static void initModulTable(Statement stm) throws SQLException {
    //stm.executeUpdate("INSERT INTO " + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_MODUL + " values('" +
    // IDBConstants.MODUL_PROJECT_MANAGEMENT + "')");
    System.out.println("init modul ok ");
  }

  static void deInitModulTable(Statement stm) throws SQLException {
    // stm.executeUpdate("DELETE FROM " +
    // pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_MODUL + " WHERE " +
    // pohaci.gumunda.aas.dbapi.IDBConstants.ATT_NAME + " = '" +
    // IDBConstants.MODUL_PROJECT_MANAGEMENT + "'");
    System.out.println("deinit modul ok ");
  }
}