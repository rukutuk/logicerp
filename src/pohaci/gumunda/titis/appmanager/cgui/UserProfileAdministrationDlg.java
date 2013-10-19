package pohaci.gumunda.titis.appmanager.cgui;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Rectangle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JList;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.appmanager.entity.Role;
import pohaci.gumunda.titis.appmanager.entity.UserProfile;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;
import pohaci.gumunda.util.Crypto;

import java.awt.Insets;
import javax.swing.BorderFactory;

/**
 * @author dark-knight
 *
 */
public class UserProfileAdministrationDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTabbedPane tabbedPane = null;
	private JPanel panel = null;
	private JButton cancelButton = null;
	private JButton saveButton = null;
	private JPanel profilePanel = null;
	private JPanel rolePanel = null;
	private JPanel northProfilePanel = null;
	private JLabel usernameLabel = null;
	private JTextField usernameTextField = null;
	private JLabel passwordLabel = null;
	private JPasswordField passwordField = null;
	private JCheckBox createNewPasswordCheckBox = null;
	private JLabel newPasswordLabel = null;
	private JPasswordField newPasswordField = null;
	private JLabel confirmLabel = null;
	private JPasswordField confirmPasswordField = null;
	private JLabel fullnameLabel = null;
	private JTextField fullnameTextField = null;
	private JLabel noteLabel = null;
	private JTextField noteTextField = null;
	private JScrollPane scrollPane = null;
	private Connection connection = null;
	private JList list = null;
	private int response = JOptionPane.CANCEL_OPTION;
	private UserProfile user = null;  //  @jve:decl-index=0:
	private JPanel northPanel = null;
	private JButton addButton = null;
	private JButton removeButton = null;
	private DefaultListModel defaultListModel;
	private Frame owner = null;
	private boolean isNew = true;
	
	/**
	 * This is the default constructor
	 */
	public UserProfileAdministrationDlg(Frame owner, String title, Connection connection) {
		super(owner, title);
		this.owner = owner;
		this.connection = connection;
		setModal(true);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(375, 315);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getProfileTabbedPane(), java.awt.BorderLayout.CENTER);
			jContentPane.add(getPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes profileTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getProfileTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("User Profile", null, getProfilePanel(), null);
			tabbedPane.addTab("Roles", null, getRolePanel(), null);
		}
		return tabbedPane;
	}

	/**
	 * This method initializes panel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanel() {
		if (panel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			panel = new JPanel();
			panel.setLayout(flowLayout);
			panel.add(getSaveButton(), null);
			panel.add(getCancelButton(), null);
		}
		return panel;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onCancel();
				}
			});
		}
		return cancelButton;
	}

	protected void onCancel() {
		setResponse(JOptionPane.CANCEL_OPTION);
		setVisible(false);
	}

	/**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("Save");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onSave();
				}
			});
		}
		return saveButton;
	}

	protected void onSave() {
		if(!isDataValid()){
			return;
		}
		
		UserProfile userProfile = getUserProfile();
		Role[] roles = getRoleSelected();
		
		AppManagerLogic logic = new AppManagerLogic(connection);
		
		try {
			if (isNew)
				userProfile = logic.createUserRoles(userProfile, roles);
			else
				userProfile = logic.updateUserRoles(userProfile, roles);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setUser(userProfile);
		setResponse(JOptionPane.OK_OPTION);
		setVisible(false);
	}

	private Role[] getRoleSelected() {
		Role[] roles = new Role[getListModel().size()];
		
		getListModel().copyInto(roles);
		
		return roles;
	}

	private UserProfile getUserProfile() {
		String username = getUsernameTextField().getText();
		String fullname = getFullnameTextField().getText();
		String note = getNoteTextField().getText();
		
		char[] password = null;
		byte[] passwordByte = null;
		if(getCreateNewPasswordCheckBox().isSelected()){
			password = getNewPasswordField().getPassword();
			passwordByte = getEncryptedPassword(password);
		}else{
			password = getPasswordField().getPassword();
			passwordByte = getPasswordByte(password);
		}

		return new UserProfile(username, passwordByte, note, fullname);
	}

	private byte[] getPasswordByte(char[] password) {
		byte[] passwordByte = new byte[password.length];
		for(int i=0; i<password.length; i++){
			passwordByte[i] = (byte)password[i];
		}
		return passwordByte;
	}

	private byte[] getEncryptedPassword(char[] password) {
		byte[] passwordByte = getPasswordByte(password);
		
		try {
			passwordByte = Crypto.encrypt(passwordByte);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return passwordByte;
	}

	private boolean isDataValid() {
		ArrayList errorList = new ArrayList();
		if(getUsernameTextField().getText().equals(""))
			errorList.add("Username must be inserted.");
		if(getFullnameTextField().getText().equals(""))
			errorList.add("Fullname must be inserted");
		if(getCreateNewPasswordCheckBox().isSelected()){
			if(getNewPasswordField().getPassword().length==0)
				errorList.add("New password must be inserted");
			if(getConfirmPasswordField().getPassword().length==0)
				errorList.add("Confirmation password must be inserted");
			if((getNewPasswordField().getPassword().length>0)&&
				(getConfirmPasswordField().getPassword().length>0)){
				String newPassword = String.copyValueOf(getNewPasswordField().getPassword());
				String confirm = String.copyValueOf(getConfirmPasswordField().getPassword());
				if(!newPassword.equals(confirm))
					errorList.add("Confirmation password should be same with new password");
			}
		}else{
			if(getPasswordField().getPassword().length==0)
				errorList.add("Password must be inserted");
		}
		
		if (errorList.size()>0){
			StringBuffer result = new StringBuffer();
			Iterator iter = errorList.iterator();
			while (iter.hasNext()){
				String o = iter.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this,result);
			return false;
		}
		return true;
	}

	/**
	 * This method initializes profilePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getProfilePanel() {
		if (profilePanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints1.gridx = 1;
			profilePanel = new JPanel();
			profilePanel.setLayout(new BorderLayout());
			profilePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
			profilePanel.add(getNorthProfilePanel(), java.awt.BorderLayout.NORTH);
		}
		return profilePanel;
	}

	/**
	 * This method initializes memberPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRolePanel() {
		if (rolePanel == null) {
			rolePanel = new JPanel();
			rolePanel.setLayout(new BorderLayout());
			rolePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			rolePanel.add(getScrollPane(), java.awt.BorderLayout.CENTER);
			rolePanel.add(getNorthPanel(), BorderLayout.NORTH);
		}
		return rolePanel;
	}

	/**
	 * This method initializes northProfilePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNorthProfilePanel() {
		if (northProfilePanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints10.gridy = 2;
			noteLabel = new JLabel();
			noteLabel.setText("Note");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints8.fill = java.awt.GridBagConstraints.VERTICAL;
			gridBagConstraints8.gridy = 1;
			fullnameLabel = new JLabel();
			fullnameLabel.setText("Fullname");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 6;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.gridx = 0;
			gridBagConstraints61.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints61.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints61.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints61.gridy = 6;
			confirmLabel = new JLabel();
			confirmLabel.setText("Confirmation");
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints51.gridy = 5;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints51.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints51.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints6.gridy = 5;
			newPasswordLabel = new JLabel();
			newPasswordLabel.setText("New Password");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints5.gridy = 4;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 3;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(5,5,0,0);
			gridBagConstraints3.gridy = 3;
			passwordLabel = new JLabel();
			passwordLabel.setText("Password");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new java.awt.Insets(5,5,0,5);
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			usernameLabel = new JLabel();
			usernameLabel.setText("Username");
			northProfilePanel = new JPanel();
			northProfilePanel.setLayout(new GridBagLayout());
			northProfilePanel.add(usernameLabel, gridBagConstraints);
			northProfilePanel.add(getUsernameTextField(), gridBagConstraints2);
			northProfilePanel.add(passwordLabel, gridBagConstraints3);
			northProfilePanel.add(getPasswordField(), gridBagConstraints4);
			northProfilePanel.add(getCreateNewPasswordCheckBox(), gridBagConstraints5);
			northProfilePanel.add(newPasswordLabel, gridBagConstraints6);
			northProfilePanel.add(getNewPasswordField(), gridBagConstraints51);
			northProfilePanel.add(confirmLabel, gridBagConstraints61);
			northProfilePanel.add(getConfirmPasswordField(), gridBagConstraints7);
			northProfilePanel.add(fullnameLabel, gridBagConstraints8);
			northProfilePanel.add(getFullnameTextField(), gridBagConstraints9);
			northProfilePanel.add(noteLabel, gridBagConstraints10);
			northProfilePanel.add(getNoteTextField(), gridBagConstraints11);
		}
		return northProfilePanel;
	}

	/**
	 * This method initializes usernameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUsernameTextField() {
		if (usernameTextField == null) {
			usernameTextField = new JTextField();
		}
		return usernameTextField;
	}

	/**
	 * This method initializes passwordField	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getPasswordField() {
		if (passwordField == null) {
			passwordField = new JPasswordField();
			passwordField.setEditable(false);
		}
		return passwordField;
	}

	/**
	 * This method initializes createNewPasswordCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCreateNewPasswordCheckBox() {
		if (createNewPasswordCheckBox == null) {
			createNewPasswordCheckBox = new JCheckBox();
			createNewPasswordCheckBox.setText("Create New Password");
			createNewPasswordCheckBox
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(javax.swing.event.ChangeEvent e) {
							onCheckBoxSelected();
						}
					});
			createNewPasswordCheckBox.setSelected(false);
			onCheckBoxSelected();
		}
		return createNewPasswordCheckBox;
	}

	protected void onCheckBoxSelected() {
		boolean selected = getCreateNewPasswordCheckBox().isSelected();
		newPasswordLabel.setEnabled(selected);
		getNewPasswordField().setEditable(selected);
		confirmLabel.setEnabled(selected);
		getConfirmPasswordField().setEditable(selected);
		
		if(!selected){
			getNewPasswordField().setText("");
			getConfirmPasswordField().setText("");
		}
	}

	/**
	 * This method initializes newPasswordField	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getNewPasswordField() {
		if (newPasswordField == null) {
			newPasswordField = new JPasswordField();
		}
		return newPasswordField;
	}

	/**
	 * This method initializes confirmPasswordField	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getConfirmPasswordField() {
		if (confirmPasswordField == null) {
			confirmPasswordField = new JPasswordField();
		}
		return confirmPasswordField;
	}

	/**
	 * This method initializes fullnameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFullnameTextField() {
		if (fullnameTextField == null) {
			fullnameTextField = new JTextField();
		}
		return fullnameTextField;
	}

	/**
	 * This method initializes noteTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNoteTextField() {
		if (noteTextField == null) {
			noteTextField = new JTextField();
		}
		return noteTextField;
	}

	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getList());
		}
		return scrollPane;
	}

	/**
	 * This method initializes list	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getList() {
		if (list == null) {
			list = new JList();
			list.setModel(getListModel());
		}
		return list;
	}

	private DefaultListModel getListModel() {
		if(defaultListModel==null){
			defaultListModel = new DefaultListModel();
		}
		return defaultListModel;
	}

	public int getResponse() {
		return response;
	}

	private void setResponse(int response) {
		this.response = response;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	/**
	 * This method initializes northPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(FlowLayout.LEFT);
			northPanel = new JPanel();
			northPanel.setLayout(flowLayout1);
			northPanel.add(getAddButton(), null);
			northPanel.add(getRemoveButton(), null);
		}
		return northPanel;
	}

	/**
	 * This method initializes addButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("Add");
			addButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onAddRole();
				}
			});
		}
		return addButton;
	}

	protected void onAddRole() {
		RoleSelectionDlg dlg = new RoleSelectionDlg(GumundaMainFrame.getMainFrame(), "Role Selection", connection);
		dlg.setVisible(true);
		if(dlg.getResponse()==JOptionPane.OK_OPTION){
			Role[] roles = dlg.getRoles();
			for(int i=0; i<roles.length; i++){
				if(!contains(roles[i]))
					getListModel().addElement(roles[i]);
			}
		}
	}

	private boolean contains(Role role) {
		Enumeration enumeration = getListModel().elements();
		while(enumeration.hasMoreElements()){
			Role inList = (Role) enumeration.nextElement();
			if(inList.getAutoindex()==role.getAutoindex())
				return true;
		}
		return false;
	}

	/**
	 * This method initializes removeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveButton() {
		if (removeButton == null) {
			removeButton = new JButton();
			removeButton.setText("Remove");
			removeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onRemoveRole();
				}
			});
		}
		return removeButton;
	}

	protected void onRemoveRole() {
		int i = getList().getSelectedIndex();
		getListModel().removeElementAt(i);
	}
	
	public void setVisible(boolean flag) {
		Rectangle rc = owner.getBounds();
	    Rectangle rcthis = getBounds();
	    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
	              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
	              (int)rcthis.getWidth(), (int)rcthis.getHeight());
	    
	    if(getUser()==null)
	    	isNew = true;
	    else{
	    	isNew = false;
	    	initializeData();
	    }

	    super.setVisible(flag);
	}

	private void initializeData() {
		getUsernameTextField().setText(getUser().getUsername());
		getFullnameTextField().setText(getUser().getFullname());
		getNoteTextField().setText(getUser().getNote());
		getPasswordField().setText(getUser().getPasswordAsString());
		
		initializeRole();
	}

	private void initializeRole() {
		AppManagerLogic logic = new AppManagerLogic(connection);
		try {
			Role[] roles = logic.getRolesByUser(getUser());
			for(int i=0; i<roles.length; i++){
				getListModel().addElement(roles[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
