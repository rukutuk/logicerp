package pohaci.gumunda.titis.project.cgui;

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
import java.sql.Connection;
import javax.swing.*;

import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class ProjectPartnerDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  JTextField m_codeTextField, m_nameTextField, m_typeTextField;
  JButton m_codeBt;
  JTextArea m_addressTextArea;
  JTextField m_cityTextField, m_postcodeTextField, m_provinceTextField, m_countryTextField;
  JTextField m_phone1TextField, m_phone2TextField, m_fax1TextField,m_fax2TextField, m_emailTextField, m_webTextField;
  JButton m_saveBt, m_cancelBt, m_closeBt;
  ProjectPartnerContactPanel m_contactpanel;

  Connection m_conn = null;
  long m_sessionid = -1;
  ProjectData m_project = null;
  Partner m_partner = null;
  ProjectPartner m_projectpartner = null;
  int m_iResponse = JOptionPane.NO_OPTION;

  public ProjectPartnerDlg(JFrame owner, Connection conn, long sessionid,
                           ProjectData project) {
    super(owner, "Project Partner", true);
    setSize(800, 500);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_project = project;
    constructComponent();
  }

  public ProjectPartnerDlg(JFrame owner, Connection conn, long sessionid,
                           ProjectPartner projectpartner) {
    super(owner, "Project Partner", true);
    setSize(800, 500);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_partner = projectpartner.getPartner();
    m_projectpartner = projectpartner;

    constructComponent();
    initData();
  }

  void constructComponent() {
    m_codeTextField = new JTextField();
    m_codeTextField.setRequestFocusEnabled(false);
    m_nameTextField = new JTextField();
    m_nameTextField.setEditable(false);
    m_typeTextField = new JTextField();
    m_typeTextField.setEditable(false);
    m_codeBt = new JButton("...");
    m_codeBt.setPreferredSize(new Dimension(22, 18));
    m_codeBt.addActionListener(this);

    m_addressTextArea = new JTextArea();
    m_addressTextArea.setEditable(false);
    m_addressTextArea.setBackground(Color.lightGray);
    m_addressTextArea.setForeground(Color.black);
    JScrollPane sp = new JScrollPane(m_addressTextArea);
    sp.setPreferredSize(new Dimension(100, 50));

    m_cityTextField = new JTextField();
    m_cityTextField.setEditable(false);
    m_postcodeTextField = new JTextField();
    m_postcodeTextField.setEditable(false);
    m_provinceTextField = new JTextField();
    m_provinceTextField.setEditable(false);
    m_countryTextField = new JTextField();
    m_countryTextField.setEditable(false);

    m_phone1TextField = new JTextField();
    m_phone1TextField.setEditable(false);
    m_phone2TextField = new JTextField();
    m_phone2TextField.setEditable(false);
    m_fax1TextField = new JTextField();
    m_fax1TextField.setEditable(false);
    m_fax2TextField = new JTextField();
    m_fax2TextField.setEditable(false);
    m_emailTextField = new JTextField();
    m_emailTextField.setEditable(false);
    m_webTextField = new JTextField();
    m_webTextField.setEditable(false);

    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    m_closeBt = new JButton("Close");
    m_closeBt.addActionListener(this);

    m_contactpanel = new ProjectPartnerContactPanel(m_conn, m_sessionid);
    m_contactpanel.setEditable(true);
    m_contactpanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

    JPanel northPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel partnerPanel = new JPanel();
    JPanel locationPanel = new JPanel();
    JPanel phonePanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    JLabel codeLabel = new JLabel("Code");
    JLabel nameLabel = new JLabel("Name");
    JLabel typeLabel = new JLabel("Type");
    JLabel addressLabel = new JLabel("Address");
    JLabel cityLabel = new JLabel("City");
    JLabel postcodeLabel = new JLabel("Postcode");
    JLabel provinceLabel = new JLabel("Provinsi");
    JLabel countryLabel = new JLabel("Country");

    JLabel phone1Label = new JLabel("Phone #1");
    JLabel phone2Label = new JLabel("Phone #2");
    JLabel fax1Label = new JLabel("Fax #1");
    JLabel fax2Label = new JLabel("Fax #2");
    JLabel emailLabel = new JLabel("Email");
    JLabel webLabel = new JLabel("Website");

    // partner panel
    partnerPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    partnerPanel.add(codeLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    partnerPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    partnerPanel.add(m_codeTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    partnerPanel.add(m_codeBt, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    partnerPanel.add(nameLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    partnerPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    partnerPanel.add(m_nameTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    partnerPanel.add(typeLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    partnerPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    partnerPanel.add(m_typeTextField, gridBagConstraints);

    // location panel
    locationPanel.setLayout(new GridBagLayout());
    locationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Location",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    locationPanel.add(addressLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    locationPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    locationPanel.add(sp, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    locationPanel.add(cityLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    partnerPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    locationPanel.add(m_cityTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    locationPanel.add(postcodeLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    locationPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    locationPanel.add(m_postcodeTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    locationPanel.add(provinceLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    locationPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    locationPanel.add(m_provinceTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    locationPanel.add(countryLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    locationPanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    locationPanel.add(m_countryTextField, gridBagConstraints);

    // phone panel
    phonePanel.setLayout(new GridBagLayout());
    phonePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Phone/Fax",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    phonePanel.add(phone1Label, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    phonePanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    phonePanel.add(m_phone1TextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    phonePanel.add(phone2Label, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    phonePanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    phonePanel.add(m_phone2TextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    phonePanel.add(fax1Label, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    phonePanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    phonePanel.add(m_fax1TextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    phonePanel.add(fax2Label, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    phonePanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    phonePanel.add(m_fax2TextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    phonePanel.add(emailLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    phonePanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    phonePanel.add(m_emailTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.insets = new Insets(2, 0, 1, 2);
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    phonePanel.add(webLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    phonePanel.add(new JLabel("   "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    phonePanel.add(m_webTextField, gridBagConstraints);

    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);
    buttonPanel.add(m_closeBt);

    JPanel gridbagPanel = new JPanel();
    gridbagPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(0, 0, 10, 0);
    gridbagPanel.add(partnerPanel, gridBagConstraints);

    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridx = 0;
    gridBagConstraints.insets = new Insets(0, 0, 0, 0);
    gridbagPanel.add(locationPanel, gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridbagPanel.add(phonePanel, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.weighty = 0.0;
    gridBagConstraints.anchor = GridBagConstraints.SOUTH;
    gridbagPanel.add(buttonPanel, gridBagConstraints);

    northPanel.setLayout(new BorderLayout());
    northPanel.add(gridbagPanel, BorderLayout.CENTER);

    centerPanel.setLayout(new BorderLayout(10, 10));
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    centerPanel.add(northPanel, BorderLayout.NORTH);
    centerPanel.add(m_contactpanel, BorderLayout.CENTER);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(centerPanel);
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  void initData() {
    m_codeTextField.setText(m_partner.getCode());
    m_nameTextField.setText(m_partner.getName());
    m_typeTextField.setText(m_partner.getCompanyToString());

    m_addressTextArea.setText(m_partner.getAddress());
    m_cityTextField.setText(m_partner.getCity());
    if(m_partner.getPostCode() > 0)
      m_postcodeTextField.setText(String.valueOf(m_partner.getPostCode()));
    m_provinceTextField.setText(m_partner.getProvince());
    m_countryTextField.setText(m_partner.getCountry());

    m_phone1TextField.setText(m_partner.getPhone1());
    m_phone2TextField.setText(m_partner.getPhone2());
    m_fax1TextField.setText(m_partner.getFax1());
    m_fax2TextField.setText(m_partner.getFax2());
    m_emailTextField.setText(m_partner.getEmail());
    m_webTextField.setText(m_partner.getWebsite());

    //m_contactpanel.setPartner(m_partner);

    if(m_projectpartner != null)
      setProjectPartnerContact(m_projectpartner);
  }

  void clear() {
    m_codeTextField.setText("");
    m_nameTextField.setText("");
    m_typeTextField.setText("");

    m_addressTextArea.setText("");
    m_cityTextField.setText("");
    m_postcodeTextField.setText("");
    m_provinceTextField.setText("");
    m_countryTextField.setText("");

    m_phone1TextField.setText("");
    m_phone2TextField.setText("");
    m_fax1TextField.setText("");
    m_fax2TextField.setText("");
    m_emailTextField.setText("");
    m_webTextField.setText("");

    m_contactpanel.clear();
  }

  public int getResponse() {
    return m_iResponse;
  }

  public ProjectPartner getProjectPartner() {
    return m_projectpartner;
  }

  public void setProjectPartnerContact(ProjectPartner projectpartner) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_contactpanel.setPersonalContact(logic.getProjectPartnerContact(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
          projectpartner.getIndex()));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }
  }

  void partner() {
    SearchPartnerDetailDlg dlg = new SearchPartnerDetailDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      Partner[] partner = dlg.getPartner();
      if(partner.length > 0) {
        m_partner = partner[0];
        initData();
      }
    }
  }

  void save() {              
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);      
      m_partner.setPersonal(m_contactpanel.getPersonalContact());            
      if(m_project != null) {
        m_projectpartner = logic.createProjectPartner(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
            m_project.getIndex(), m_partner);        
      }
      else {        
        m_projectpartner = logic.updateProjectPartner(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
            m_projectpartner.getIndex(), m_partner);
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, "Data tidak boleh kosong",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  void cancel() {
   // if(m_partner != null) // jika data yang dikirimkan tidak kosong
   //  initData();
   //else
      m_partner = null;
      clear();
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_codeBt) {
      partner();
    }
    else if(e.getSource() == m_saveBt) {
      save();
    }
    else if(e.getSource() == m_cancelBt) {
      cancel();
    }
    else if(e.getSource() == m_closeBt) {
      dispose();
    }
  }
}