package pohaci.gumunda.titis.appmanager.cgui;
import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import pohaci.gumunda.titis.appmanager.entity.Role;

public class RoleDlg extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel centerPanel = null;

	private JPanel buttonPanel = null;

	private JButton cancelButton = null;

	private JButton saveButton = null;

	private JLabel roleLabel = null;

	private JTextField roleTextField = null;

	private JLabel descriptionLabel = null;

	private JTextField descriptionTextField = null;
	
	private Role role = null;  //  @jve:decl-index=0:
	
	private int response; 
	
	private Frame owner;

	/**
	 * @param owner
	 */
	public RoleDlg(Frame owner, String title) {
		super(owner, title);
		this.owner = owner;
		setModal(true);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 145);
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
			jContentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jContentPane.add(getCenterPanel(), BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes centerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 1;
			descriptionLabel = new JLabel();
			descriptionLabel.setText("Description");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.gridy = 0;
			roleLabel = new JLabel();
			roleLabel.setText("Role");
			centerPanel = new JPanel();
			centerPanel.setLayout(new GridBagLayout());
			centerPanel.add(roleLabel, gridBagConstraints);
			centerPanel.add(getRoleTextField(), gridBagConstraints1);
			centerPanel.add(descriptionLabel, gridBagConstraints2);
			centerPanel.add(getDescriptionTextField(), gridBagConstraints3);
		}
		return centerPanel;
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(flowLayout);
			buttonPanel.add(getSaveButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
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
		String roleName = roleTextField.getText();
		String description = descriptionTextField.getText();
		
		if(roleName.equals("")){
			JOptionPane.showMessageDialog(this, "Role Name must be inserted");
			return;
		}
		
		Role newRole = getRole();
		
		newRole.setRoleName(roleName);
		newRole.setDescription(description);
		
		setRole(role);
		
		setResponse(JOptionPane.OK_OPTION);
		
		setVisible(false);
	}

	/**
	 * This method initializes roleTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getRoleTextField() {
		if (roleTextField == null) {
			roleTextField = new JTextField();
		}
		return roleTextField;
	}

	/**
	 * This method initializes descriptionTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDescriptionTextField() {
		if (descriptionTextField == null) {
			descriptionTextField = new JTextField();
		}
		return descriptionTextField;
	}

	public Role getRole() {
		if(role==null){
			role = new Role();
		}
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public int getResponse() {
		return response;
	}

	private void setResponse(int response) {
		this.response = response;
	}

	public void setVisible(boolean flag) {
		getRoleTextField().setText(getRole().getRoleName());
		getDescriptionTextField().setText(getRole().getDescription());
		
		Rectangle rc = owner.getBounds();
	    Rectangle rcthis = getBounds();
	    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
	              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
	              (int)rcthis.getWidth(), (int)rcthis.getHeight());

	    super.setVisible(flag);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
