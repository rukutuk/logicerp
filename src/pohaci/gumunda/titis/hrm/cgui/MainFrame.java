package pohaci.gumunda.titis.hrm.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.swing.*;

import pohaci.gumunda.aas.logic.LoginBusinessLogic;
import pohaci.gumunda.titis.accounting.cgui.AccountTreePanel;
import pohaci.gumunda.titis.accounting.cgui.ExpenseSheetPanel;
import pohaci.gumunda.titis.accounting.cgui.bypass.ManagerLoginDlg;
import pohaci.gumunda.titis.hrm.cgui.IConstants;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.appmanager.entity.ApplicationFunction;
import pohaci.gumunda.titis.appmanager.entity.UserProfile;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;
import pohaci.gumunda.titis.hrm.cgui.report.*;
import pohaci.gumunda.titis.project.cgui.report.RptFieldAllowances;

public class MainFrame extends FrameMain implements ActionListener {

    private static final long serialVersionUID = 1L;
    protected static final String HELP_FILENAME = "hrm.chm";
    protected static final String HELP_FOLDER = "..\\helps\\";
    DefaultWorkingTimeDlg dlg;
    JMenuItem mi_logout = new JMenuItem("Logout");
    JMenuItem mi_exit = new JMenuItem("Exit");
    JMenuItem mi_organization = new JMenuItem("Department");
    JMenuItem mi_qualification = new JMenuItem("Qualification");
    JMenuItem mi_jobtitle = new JMenuItem("Job Title");
    JMenuItem mi_workagreement = new JMenuItem("Work Agreement");
    JMenuItem mi_education = new JMenuItem("Education");
    JMenuItem mi_religion = new JMenuItem("Religion");
    JMenuItem mi_sex = new JMenuItem("Sex Type");
    JMenuItem mi_family = new JMenuItem("Family Relation");
    JMenuItem mi_marital = new JMenuItem("Marital Status");
    JMenuItem mi_payrollcomponent = new JMenuItem("Payroll Component");
    JMenuItem mi_payrollcategory = new JMenuItem("Payroll Category");
    JMenuItem mi_multiplier = new JMenuItem("Field Allowence Multiplier");
    JMenuItem mi_ptkp = new JMenuItem("PTKP");
    JMenuItem mi_tariff = new JMenuItem("Tax Art 21 Tariff");
    JMenuItem mi_account = new JMenuItem("Chart of Account");
    JMenuItem mi_21Component = new JMenuItem("Tax Art 21 Component");
    JMenuItem mi_leavetype = new JMenuItem("Leave Type");
    JMenuItem mi_permitiontype = new JMenuItem("Permission Type");
    JMenuItem mi_officehour = new JMenuItem("Office Hr Perm. Type");
    JMenuItem mi_employeedata = new JMenuItem("Data");
    JMenuItem mi_leavepermition = new JMenuItem("Leave and Permission");
    JMenuItem mi_officeworkingtime = new JMenuItem("Office Working Time");
    JMenuItem mi_overtime = new JMenuItem("Overtime Data");
    JMenuItem mi_overtimemultiplier = new JMenuItem("Overtime Multiplier");
    JMenuItem mi_workingtime = new JMenuItem("Default Working Time");
    JMenuItem mi_holiday = new JMenuItem("Holiday");
    JMenuItem mi_annualleaveright = new JMenuItem("Annual Leave Right");
    JMenuItem mi_paychequelabel = new JMenuItem("Paycheque Label");
    JMenuItem mi_nonpaychequeperiod = new JMenuItem("Non Paycheque Period");
    //JMenuItem mi_helpContentsMenu = new JMenuItem("Help Contents");
    JMenuItem mi_MealAllowance = new JMenuItem("Meal Allowance");
    JMenuItem mi_TransportationAllowance = new JMenuItem("Transportation Allowance");
    JMenuItem mi_expensesheet = new JMenuItem("Expense Sheet");
    JMenuItem mi_Overtime = new JMenuItem("Overtime");
    JMenuItem mi_OtherAllowance = new JMenuItem("Other Allowance");
    JMenuItem mi_InsuranceAllowance = new JMenuItem("Insurance Allowance");
    JMenuItem mi_PaychequeSubmit = new JMenuItem("Paycheque Submit");
    JMenuItem mi_TaxArt21Submit = new JMenuItem("Tax Art 21 Submit");
    // report
    JMenuItem mi_paycheques = new JMenuItem("Paycheques");
    JMenuItem mi_fieldAllowance = new JMenuItem("Field Allowance Summary");
    JMenuItem mi_EmployeePresence = new JMenuItem("Employee Presence Summary");
    JMenuItem mi_employeeProfile = new JMenuItem("Employee Profile");
    JMenuItem mi_employeeList = new JMenuItem("Employee List");
    private UserProfile m_userProfile;
    private Hashtable hashtable = new Hashtable();
    private static final boolean MODE_ADMIN = false;

    public MainFrame() {
        super(IConstants.APP_HRM);
        setTitle("HRM");
        setSize(750, 600);
        //setIconImage(Toolkit.getDefaultToolkit().getImage("../images/titis.gif"));
        setIconImage(Toolkit.getDefaultToolkit().getImage(".."));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (m_sessionid != -1) {
                    logout();
                }
                System.exit(0);
            }
        });

        init();
        constructComponent();
        createMenu();
        populateMenu(); // JANGAN DIHAPUS MESKI CUMAN DI-COMMENT

        adjustPosition();
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        login();

        if (!MODE_ADMIN) {
            getAllGrantedFunctions();
            enableMenu();
            deleteSeparator();
        }
    }

    // METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
    private void getAllGrantedFunctions() {
        AppManagerLogic logic = new AppManagerLogic(m_conn);
        try {
            ApplicationFunction[] functions = logic.getGrantedFunctionForUser(m_userProfile, "HRM");
            hashtable.clear();
            for (int i = 0; i < functions.length; i++) {
                hashtable.put(functions[i].getPath(), functions[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
    private void deleteSeparator() {
        JMenuBar menuBar = getJMenuBar();
        int menuCount = menuBar.getMenuCount();
        for (int i = 0; i < menuCount; i++) {
            JMenu menu = menuBar.getMenu(i);
            if ((menu.isEnabled()) && (menu.isVisible())) {
                removeSeparatorInMenu(menu);
            }
        }
    }

    // METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
    private void removeSeparatorInMenu(JMenu menu) {
        int menuCount = menu.getMenuComponentCount();
        for (int i = 0; i < menuCount; i++) {
            Component menuComponent = menu.getMenuComponent(i);
            if (menuComponent != null) {
                if (menuComponent instanceof JMenu) {
                    removeSeparatorInMenu((JMenu) menuComponent);
                } else if (menuComponent instanceof JSeparator) {
                    boolean isBeforeEnabled = false;
                    if (i - 1 >= 0) {
                        isBeforeEnabled = checkMenuComponent(menu, i - 1);
                    }
                    boolean isAfterEnabled = false;
                    if (i + 1 <= menuCount) {
                        isAfterEnabled = checkMenuComponent(menu, i + 1);
                    }

                    menuComponent.setEnabled(isBeforeEnabled && isAfterEnabled);
                    menuComponent.setVisible(isBeforeEnabled && isAfterEnabled);
                } else {
                    // do nothing
                }
            }
        }
    }

    // METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
    private boolean checkMenuComponent(JMenu menu, int i) {
        Component componentChecked = menu.getMenuComponent(i);
        return ((componentChecked.isEnabled()) && (componentChecked.isVisible()));
    }

    // METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
    private void enableMenu() {
        JMenuBar menuBar = getJMenuBar();
        int menuCount = menuBar.getMenuCount();
        for (int i = 0; i < menuCount; i++) {
            JMenu menu = menuBar.getMenu(i);
            if (!(menu.getText().equalsIgnoreCase("File") || menu.getText().equalsIgnoreCase("Window") || menu.getText().equalsIgnoreCase("Help"))) {
                String text = menu.getText();
                String path = text + "\\";

                if (isGranted(path)) {
                    System.err.println("true: " + path);
                    menu.setEnabled(true);
                    menu.setVisible(true);
                } else {
                    System.err.println("false: " + path);
                    menu.setEnabled(false);
                    menu.setVisible(false);
                }
                enableMenuItems(menu, text);
            }
        }
    }

    // METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
    private boolean isGranted(String path) {
        return hashtable.containsKey(path);
    }

    // METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
    private void enableMenuItems(JMenu menu, String text) {
        int menuCount = menu.getItemCount();
        for (int i = 0; i < menuCount; i++) {
            JMenuItem menuItem = menu.getItem(i);
            if (menuItem != null) {
                String newText = text + "\\" + menuItem.getText();
                if (menuItem instanceof JMenu) {
                    String path = newText + "\\";
                    if (isGranted(path)) {
                        System.err.println("true: " + path);
                        menuItem.setEnabled(true);
                        menuItem.setVisible(true);
                    } else {
                        System.err.println("false: " + path);
                        menuItem.setEnabled(false);
                        menuItem.setVisible(false);
                    }
                    enableMenuItems((JMenu) menuItem, newText);
                } else {
                    String path = newText + "\\";
                    if (isGranted(path)) {
                        System.err.println("true: " + path);
                        menuItem.setEnabled(true);
                        menuItem.setVisible(true);
                    } else {
                        System.err.println("false: " + path);
                        menuItem.setEnabled(false);
                        menuItem.setVisible(false);
                    }
                }
            }
        }
    }

    // METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
    private void populateMenu() {
        JMenuBar menuBar = getJMenuBar();
        int menuCount = menuBar.getMenuCount();
        for (int i = 0; i < menuCount; i++) {
            JMenu menu = menuBar.getMenu(i);
            if (!(menu.getText().equalsIgnoreCase("File") || menu.getText().equalsIgnoreCase("Window") || menu.getText().equalsIgnoreCase("Help"))) {
                String text = menu.getText();
                String toPrint =
                        "INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
                        + "VALUES ('HRM', '" + menu.getText() + "', '"
                        + text + "\\')" + "\n//";
                System.err.println(toPrint.replace("\\", "\\\\"));
                populateMenuItems(menu, text);
            }
        }
    }

    // METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
    // TERMASUK SQL STATEMENT INSERT BLA..BLA..BLA..
    private void populateMenuItems(JMenu menu, String text) {
        // maxdb
		/*insert into functionstructure as
         select a.autoindex superfunction, b.autoindex subfunction from function a,
         (select autoindex, application, substr(treepath,1,index(treepath,functionname)-1) parent from function) b
         where a.treepath=b.parent and a.application=b.application*/

        /*mysql
         insert into functionstructure (superfunction, subfunction) select a.autoindex superfunction, b.autoindex subfunction from function a, (select autoindex, application, substring_index(treepath,functionname,1) parent from function) b where a.treepath=b.parent and a.application=b.application		 
         */

        /*insert into userdetail (username, fullname) (
         select username, username fullname from uaccount)*/
        int menuCount = menu.getItemCount();
        for (int i = 0; i < menuCount; i++) {
            JMenuItem menuItem = menu.getItem(i);
            if (menuItem != null) {
                String newText = text + "\\" + menuItem.getText();
                if (menuItem instanceof JMenu) {
                    String toPrint =
                            "INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
                            + "VALUES ('HRM', '" + menuItem.getText() + "', '"
                            + newText + "\\')" + "\n//";
                    System.err.println(toPrint.replace("\\", "\\\\"));
                    populateMenuItems((JMenu) menuItem, newText);
                } else {
                    String toPrint =
                            "INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
                            + "VALUES ('HRM', '" + menuItem.getText() + "', '"
                            + newText + "\\')" + "\n//";
                    System.err.println(toPrint.replace("\\", "\\\\"));
                }
            }
        }
    }

    void adjustPosition() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);
    }

    void login() {
        /*pohaci.gumunda.titis.accounting.cgui.bypasss.ManagerLoginDlg logindlg = new
         pohaci.gumunda.titis.accounting.cgui.bypasss.ManagerLoginDlg(this, IConstants.APP_HRM);*/
        ManagerLoginDlg logindlg = new ManagerLoginDlg(this, IConstants.APP_HRM);
        logindlg.setVisible(true);
        //logindlg.onOk();
        if (logindlg.m_sessionid == -2) {
            System.exit(0);
        }

        m_sessionid = logindlg.m_sessionid;
        m_userProfile = logindlg.getUserProfile();
    }

    void logout() {
        try {
            LoginBusinessLogic loginbl = new LoginBusinessLogic(getConnection());
            loginbl.logout(m_sessionid);
            m_sessionid = -1;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    void createMenu() {
        JMenuBar mbar = new JMenuBar();
        setJMenuBar(mbar);

        JMenu mfile = new JMenu("File");
        mbar.add(mfile);
        mi_logout.addActionListener(this);
        mfile.add(mi_logout);
        mi_exit.addActionListener(this);
        mfile.add(mi_exit);

        JMenu memployee = new JMenu("Employee");
        mbar.add(memployee);
        mi_employeedata.addActionListener(this);
        memployee.add(mi_employeedata);
        mi_expensesheet.addActionListener(this);
        memployee.add(mi_expensesheet);

        JMenu mabsence = new JMenu("Absence");
        mbar.add(mabsence);
        mi_leavepermition.addActionListener(this);
        mabsence.add(mi_leavepermition);
        mi_officeworkingtime.addActionListener(this);
        mabsence.add(mi_officeworkingtime);
        mi_overtime.addActionListener(this);
        mabsence.add(mi_overtime);




        JMenu mPayroll = new JMenu("Payroll");
        mbar.add(mPayroll);

        mi_PaychequeSubmit.addActionListener(this);
        mPayroll.add(mi_PaychequeSubmit);

        JMenu m_NonPaychequeSubmit = new JMenu("Non Paycheque Submit");
        mPayroll.add(m_NonPaychequeSubmit);

        m_NonPaychequeSubmit.add(mi_MealAllowance);
        mi_MealAllowance.addActionListener(this);
        m_NonPaychequeSubmit.add(mi_TransportationAllowance);
        mi_TransportationAllowance.addActionListener(this);
        m_NonPaychequeSubmit.add(mi_Overtime);
        mi_Overtime.addActionListener(this);

        JMenu m_NonPaymentSubmit = new JMenu("Non Payment Submit");
        mPayroll.add(m_NonPaymentSubmit);

        m_NonPaymentSubmit.add(mi_OtherAllowance);
        mi_OtherAllowance.addActionListener(this);
        m_NonPaymentSubmit.add(mi_InsuranceAllowance);
        mi_InsuranceAllowance.addActionListener(this);

        mPayroll.addSeparator();

        mi_TaxArt21Submit.addActionListener(this);
        mPayroll.add(mi_TaxArt21Submit);

        JMenu mmreport = new JMenu("Report");
        mbar.add(mmreport);
        mi_paycheques.addActionListener(this);
        mmreport.add(mi_paycheques);
        mi_fieldAllowance.addActionListener(this);
        mmreport.add(mi_fieldAllowance);
        mi_EmployeePresence.addActionListener(this);
        mmreport.add(mi_EmployeePresence);
        mi_employeeProfile.addActionListener(this);
        mmreport.add(mi_employeeProfile);

        mi_employeeList.addActionListener(this);
        mmreport.add(mi_employeeList);

        JMenu mmaster = new JMenu("Master Data");
        mbar.add(mmaster);
        JMenu memployeeattr = new JMenu("Employee Attribute");
        mmaster.add(memployeeattr);
        mi_account.addActionListener(this);
        mi_organization.addActionListener(this);
        memployeeattr.add(mi_organization);
        memployeeattr.add(mi_account);
        mi_qualification.addActionListener(this);
         memployeeattr.add(mi_qualification);
         mi_jobtitle.addActionListener(this);
         memployeeattr.add(mi_jobtitle);
         mi_workagreement.addActionListener(this);		
         memployeeattr.add(mi_workagreement);
		
         memployeeattr.addSeparator();
         mi_education.addActionListener(this);
         memployeeattr.add(mi_education);
         mi_religion.addActionListener(this);
         memployeeattr.add(mi_religion);
         memployeeattr.addSeparator();
         mi_sex.addActionListener(this);
         memployeeattr.add(mi_sex);
         mi_family.addActionListener(this);
         memployeeattr.add(mi_family);
         mi_marital.addActionListener(this);
         memployeeattr.add(mi_marital);

        JMenu mpayroll = new JMenu("Payroll");
        mmaster.add(mpayroll);
        mi_payrollcomponent.addActionListener(this);
        mpayroll.add(mi_payrollcomponent);
        mi_payrollcategory.addActionListener(this);
        mpayroll.add(mi_payrollcategory);

        mi_multiplier.addActionListener(this);
        mpayroll.add(mi_multiplier);
        mpayroll.addSeparator();
        mi_ptkp.addActionListener(this);
        mi_21Component.addActionListener(this);
        mpayroll.add(mi_21Component);
        mpayroll.add(mi_ptkp);
        mi_tariff.addActionListener(this);
        mpayroll.add(mi_tariff);

        JMenu mmabsence = new JMenu("Absence");
        mmaster.add(mmabsence);
        mi_leavetype.addActionListener(this);
        mmabsence.add(mi_leavetype);
        mi_permitiontype.addActionListener(this);
        mmabsence.add(mi_permitiontype);
        mi_officehour.addActionListener(this);
        mmabsence.add(mi_officehour);

        JMenu msetting = new JMenu("Setting");
        mbar.add(msetting);
        mi_workingtime.addActionListener(this);
        msetting.add(mi_workingtime);

        mi_holiday.addActionListener(this);
        msetting.add(mi_holiday);

        mi_annualleaveright.addActionListener(this);
        msetting.add(mi_annualleaveright);

        mi_overtimemultiplier.addActionListener(this);
        msetting.add(mi_overtimemultiplier);
        mi_paychequelabel.addActionListener(this);
        msetting.add(mi_paychequelabel);
        mi_nonpaychequeperiod.addActionListener(this);
        msetting.add(mi_nonpaychequeperiod);
        mbar.add(m_windowMenu);

        mbar.add(m_helpMenu);
        mi_helpContentsMenu.addActionListener(this); // i change this
        m_helpMenu.add(mi_helpContentsMenu); // i add this
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mi_logout) {
            m_desktopPane.removeAll();
            m_desktopPane.repaint();
            logout();
            login();
        } else if (e.getSource() == mi_exit) {
            if (m_sessionid != -1) {
                logout();
            }
            deInit();
            System.exit(0);
        } else if (e.getSource() == mi_organization) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Department", this);
            internalFrame.setContentPane(new OrganizationTreePanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(350, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_qualification) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Qualification",
                    this);
            internalFrame.setContentPane(new QualificationPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_jobtitle) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Job Title", this);
            internalFrame
                    .setContentPane(new JobTitlePanel(m_conn, m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_workagreement) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Work Agreement",
                    this);
            internalFrame.setContentPane(new WorkAgreementPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(350, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_account) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            InternalFrame internalFrame = new InternalFrame("Chart of Account", this);
            internalFrame.setContentPane(new AccountTreePanel(m_conn, m_sessionid));
            internalFrame.setSize(800, 700);
            m_desktopPane.add(internalFrame);
            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_education) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Education", this);
            internalFrame
                    .setContentPane(new EducationPanel(m_conn, m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_religion) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Religion", this);
            internalFrame
                    .setContentPane(new ReligionPanel(m_conn, m_sessionid));
            internalFrame.setSize(350, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_sex) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Sex Type", this);
            internalFrame.setContentPane(new SexTypePanel(m_conn, m_sessionid));
            internalFrame.setSize(350, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_family) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Family Relation",
                    this);
            internalFrame.setContentPane(new FamilyRelationPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(350, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_marital) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Marital Status",
                    this);
            internalFrame.setContentPane(new MaritalStatusPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(350, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_payrollcomponent) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Payroll Component", this);
            internalFrame.setContentPane(new PayrollComponentTreePanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_payrollcategory) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Payroll Category", this);
            internalFrame.setContentPane(new PayrollCategoryPanel(m_conn, m_sessionid));
            internalFrame.setSize(800, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_multiplier) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Field Allowence Multipler", this);
            internalFrame.setContentPane(new AllowanceMultiplierPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_ptkp) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("PTKP", this);
            internalFrame.setContentPane(new PTKPPanel(m_conn, m_sessionid));
            internalFrame.setSize(600, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_tariff) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Tax Art 21 Tariff", this);
            internalFrame.setContentPane(new TaxArt21TariffPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_21Component) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Tax Art 21 Component", this);
            internalFrame.setContentPane(new TaxArt21ComponentPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_leavetype) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Leave Type", this);
            internalFrame
                    .setContentPane(new LeaveTypePanel(m_conn, m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_officeworkingtime) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Office Working Time", this);
            internalFrame.setContentPane(new WorkingTimePanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(600, 430);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_overtime) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Overtime", this);
            internalFrame.setContentPane(new OvertimeDataPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(600, 430);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_permitiontype) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Permission Type",
                    this);
            internalFrame.setContentPane(new PermitionTypePanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_officehour) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Office Hour Permission Type", this);
            internalFrame.setContentPane(new OfficeHourPermitionPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(500, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_employeedata) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Employee", this);
            internalFrame
                    .setContentPane(new EmployeePanel(m_conn, m_sessionid));
            internalFrame.setSize(800, 571);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_leavepermition) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Leave and Permission", this);
            internalFrame.setContentPane(new LeavePermitionPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(800, 650);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_workingtime) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            InternalFrame internalFrame = new InternalFrame(
                    "Office Working Time", this);
            internalFrame.setContentPane(new DefaultWorkingTimeDlg(m_conn,
                    m_sessionid));
            internalFrame.setSize(300, 500);
            m_desktopPane.add(internalFrame);
            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_holiday) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Holidays", this);
            internalFrame.setContentPane(new HolidayPanel(m_conn, m_sessionid));
            internalFrame.setSize(750, 400);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_annualleaveright) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Annual Leave Right", this);
            internalFrame.setContentPane(new AnnualLeaveRightPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(800, 600);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_overtimemultiplier) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Overtime Multiplier", this);
            internalFrame.setContentPane(new OvertimeMultiplierPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(500, 350);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_paychequelabel) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Paycheque Label",
                    this);
            internalFrame.setContentPane(new PaychequeLabelTreePanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(300, 350);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_nonpaychequeperiod) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Non Paycheque Period", this);
            internalFrame.setContentPane(new NonPaychequePeriodPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } // payroll
        else if (e.getSource() == mi_MealAllowance) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Meal Allowance Submit", this);
            internalFrame.setContentPane(new PayrollMealAllowanceSubmitPanel(
                    m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_TransportationAllowance) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Transportation Allowance Submit", this);
            internalFrame
                    .setContentPane(new PayrollTransportationAllowanceSubmitPanel(
                    m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_Overtime) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame("Overtime Submit",
                    this);
            internalFrame.setContentPane(new PayrollOvertimeSubmitPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_OtherAllowance) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            InternalFrame internalFrame = new InternalFrame("Other Allowance Submit", this);
            internalFrame.setContentPane(new PayrollOtherAllowanceSubmitPanel(m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);
            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_expensesheet) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            InternalFrame internalFrame = new InternalFrame("Expense Sheet", this);
            internalFrame.setContentPane(new ExpenseSheetPanel(m_conn, m_sessionid));
            internalFrame.setSize(1000, 725);
            m_desktopPane.add(internalFrame);
            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_InsuranceAllowance) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Insurance Allowance Submit", this);
            internalFrame.setContentPane(new PayrollInsuranceAllowanceSubmitPanel(
                    m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_TaxArt21Submit) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Tax Art 21 Submit", this);
            internalFrame.setContentPane(new PayrollTaxArt21SubmitPanel(m_conn,
                    m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_PaychequeSubmit) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Paycheque Submit", this);
            internalFrame.setContentPane(new PayrollPaychequeSubmitPanel(
                    m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } //report
        else if (e.getSource() == mi_paycheques) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Paycheques", this);
            internalFrame.setContentPane(new RptPaycheques(
                    m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_fieldAllowance) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Field Allowance Summary", this);
            internalFrame.setContentPane(new RptFieldAllowances(m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_EmployeePresence) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Employee Presence Summary", this);
            internalFrame.setContentPane(new RptEmployeePresence(
                    m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_employeeProfile) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Employee Profile Summary", this);
            internalFrame.setContentPane(new RptEmployeeProfilePanel(
                    m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (e.getSource() == mi_employeeList) {
            //blm diisi
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            InternalFrame internalFrame = new InternalFrame(
                    "Employee List", this);
            internalFrame.setContentPane(new RptEmployeeListPanel(
                    m_conn, m_sessionid));
            internalFrame.setSize(400, 150);
            m_desktopPane.add(internalFrame);

            internalFrame.setVisible(true);
            try {
                internalFrame.setMaximum(true);
                internalFrame.setSelected(true);
            } catch (Exception exception) {
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } // else if(e.getSource() == m_helpMenu) {
        // JOptionPane.showMessageDialog(this, "Test");
        // }
        else if (e.getSource() == mi_helpContentsMenu) {
            try {
                String file = HELP_FOLDER + HELP_FILENAME;
                Runtime.getRuntime().exec(
                        new String[]{"cmd.exe", "/c", file});
                // process.waitFor();
            } catch (Exception ext) {
                System.out.println(ext.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {

            javax.swing.UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
            //javax.swing.UIManager.setLookAndFeel("com.digitprop.tonic.TonicLookAndFeel");
            //javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.demo");
            //javax.swing.UIManager.setLookAndFeel("org.fife.plaf.Office2003.Office2003LookAndFeel");
            //javax.swing.UIManager.setLookAndFeel("net.sourceforge.napkinlaf.NapkinLookAndFeel");
            //javax.swing.UIManager.setLookAndFeel("org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel");
            //javax.swing.UIManager.setLookAndFeel("org.fife.plaf.OfficeXP.OfficeXPLookAndFeel");
            //UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
            javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource(
                    "Tahoma", 0, 11);
            java.util.Enumeration keys = javax.swing.UIManager.getDefaults()
                    .keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = javax.swing.UIManager.get(key);
                if (value instanceof javax.swing.plaf.FontUIResource) {
                    javax.swing.UIManager.put(key, f);
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
        new MainFrame();
    }
}
