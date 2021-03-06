/*
 * MasterProjectDataPanel.java
 *
 * Created on April 22, 2007, 10:07 PM
 */

package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import pohaci.gumunda.titis.project.cgui.ProjectDataPicker;

/**
 *
 * @author  Acer_5583NWXMi
 */
public class MasterProjectDataPanel extends javax.swing.JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int lebarText,lebarLabel;
    Connection m_conn = null;
	long m_sessionid = -1;
    public MasterProjectDataPanel() {
        initComponents();
    }
    public MasterProjectDataPanel(int lebar1,int lebar2){
       lebarLabel=lebar1;
       lebarText=lebar2;
       initComponents();       
    }
    
    public MasterProjectDataPanel(int lebar1,int lebar2,Connection conn, long sessionid){
    	m_conn = conn;
    	m_sessionid = sessionid;
        lebarLabel=lebar1;
        lebarText=lebar2;
        initComponents();        
     }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        ProjectCodeLabel = new javax.swing.JLabel();
        //ProjectCodeComp = new javax.swing.JTextField();
        ProjectCodeComp = new ProjectDataPicker(m_conn, m_sessionid);
        
        CustomerText = new javax.swing.JTextField();
        CustomerLabel = new javax.swing.JLabel();
        WorkDescLabel = new javax.swing.JLabel();
        WorkDescScroll = new javax.swing.JScrollPane();
        WorkDescTextArea = new javax.swing.JTextArea();
        UnitCodeLabel = new javax.swing.JLabel();
        //UnitCodeComp = new javax.swing.JTextField();
        UnitCodeComp = new LookupUnitPicker(m_conn,m_sessionid);
        ActivityCodeText = new javax.swing.JTextField();
        ActivityCodeLabel = new javax.swing.JLabel();
        DepartmentLabel = new javax.swing.JLabel();
        //DepartmentComp = new javax.swing.JTextField();
        DepartmentComp= new LookupDepartmentPicker(m_conn,m_sessionid);
        ORNoLabel = new javax.swing.JLabel();
        ORNoText = new javax.swing.JTextField();
        ContractNoText = new javax.swing.JTextField();
        SOPOContractNo = new javax.swing.JLabel();
        IPCNoLabel = new javax.swing.JLabel();
        IPCNoText = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        ProjectCodeLabel.setText("Project Code*");
        ProjectCodeLabel.setPreferredSize(new java.awt.Dimension(lebarLabel, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(ProjectCodeLabel, gridBagConstraints);

        ProjectCodeComp.setPreferredSize(new java.awt.Dimension(lebarText, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(ProjectCodeComp, gridBagConstraints);

        CustomerText.setPreferredSize(new java.awt.Dimension(lebarText, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(CustomerText, gridBagConstraints);

        CustomerLabel.setText("Customer");
        CustomerLabel.setPreferredSize(new java.awt.Dimension(lebarLabel, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(CustomerLabel, gridBagConstraints);

        WorkDescLabel.setText("Work Desc. ");
        WorkDescLabel.setPreferredSize(new java.awt.Dimension(lebarLabel, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(WorkDescLabel, gridBagConstraints);

        WorkDescScroll.setPreferredSize(new java.awt.Dimension(lebarText, 63));
        WorkDescTextArea.setColumns(20);
        WorkDescTextArea.setLineWrap(true);
        WorkDescTextArea.setRows(5);
        WorkDescScroll.setViewportView(WorkDescTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(WorkDescScroll, gridBagConstraints);

        UnitCodeLabel.setText("Unit Code");
        UnitCodeLabel.setPreferredSize(new java.awt.Dimension(lebarLabel, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(UnitCodeLabel, gridBagConstraints);

        UnitCodeComp.setPreferredSize(new java.awt.Dimension(lebarText, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(UnitCodeComp, gridBagConstraints);

        ActivityCodeText.setPreferredSize(new java.awt.Dimension(lebarText, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(ActivityCodeText, gridBagConstraints);

        ActivityCodeLabel.setText("Activity Code");
        ActivityCodeLabel.setPreferredSize(new java.awt.Dimension(lebarLabel, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(ActivityCodeLabel, gridBagConstraints);

        DepartmentLabel.setText("Department");
        DepartmentLabel.setPreferredSize(new java.awt.Dimension(lebarLabel, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(DepartmentLabel, gridBagConstraints);

        DepartmentComp.setPreferredSize(new java.awt.Dimension(lebarText, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(DepartmentComp, gridBagConstraints);

        ORNoLabel.setText("O.R No");
        ORNoLabel.setPreferredSize(new java.awt.Dimension(lebarLabel, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(ORNoLabel, gridBagConstraints);

        ORNoText.setPreferredSize(new java.awt.Dimension(lebarText, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(ORNoText, gridBagConstraints);

        ContractNoText.setPreferredSize(new java.awt.Dimension(lebarText, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(ContractNoText, gridBagConstraints);

        SOPOContractNo.setText("SO/PO/Contract No");
        SOPOContractNo.setPreferredSize(new java.awt.Dimension(lebarLabel, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(SOPOContractNo, gridBagConstraints);

        IPCNoLabel.setText("IPC No");
        IPCNoLabel.setPreferredSize(new java.awt.Dimension(lebarLabel, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(IPCNoLabel, gridBagConstraints);

        IPCNoText.setPreferredSize(new java.awt.Dimension(lebarText, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        add(IPCNoText, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ActivityCodeLabel;
    public javax.swing.JTextField ActivityCodeText;
    public javax.swing.JTextField ContractNoText;
    private javax.swing.JLabel CustomerLabel;
    public javax.swing.JTextField CustomerText;
    //public javax.swing.JTextField DepartmentComp;
    public LookupDepartmentPicker DepartmentComp;
    private javax.swing.JLabel DepartmentLabel;
    private javax.swing.JLabel IPCNoLabel;
    public javax.swing.JTextField IPCNoText;
    private javax.swing.JLabel ORNoLabel;
    public javax.swing.JTextField ORNoText;
    //private javax.swing.JTextField ProjectCodeComp;
    public ProjectDataPicker ProjectCodeComp;
    private javax.swing.JLabel ProjectCodeLabel;
    private javax.swing.JLabel SOPOContractNo;
    //public javax.swing.JTextField UnitCodeComp;
    public LookupUnitPicker UnitCodeComp;
    //public LookupUnitPicker UnitCodeComp;
    private javax.swing.JLabel UnitCodeLabel;
    private javax.swing.JLabel WorkDescLabel;
    public javax.swing.JScrollPane WorkDescScroll;
    public javax.swing.JTextArea WorkDescTextArea;
    // End of variables declaration//GEN-END:variables
    
}
