/**
 * 
 */
package pohaci.gumunda.titis.appmanager.cgui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.appmanager.entity.Role;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;

/**
 * @author dark-knight
 *
 */
public class RoleAdministrationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSplitPane splitPane = null;
	private JPanel leftPanel = null;
	private JPanel northLeftPanel = null;
	private JButton addButton = null;
	private JButton deleteButton = null;
	private PrivilegePanel rightPanel;
	
	private Connection connection;
	private JPanel centerPanel = null;
	private JList roleList = null;
	private DefaultListModel roleListModel;
	private JScrollPane roleScrollPane = null;
	private JButton editButton = null;
	/**
	 * This is the default constructor
	 * @param connection 
	 */
	public RoleAdministrationPanel(Connection connection) {
		super();
		this.connection = connection;
		initialize();
		initializeRoleList();
	}

	private void initializeRoleList() {
		refreshRoleList();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(550, 367);
		this.add(getSplitPane(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * This method initializes splitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setDividerLocation(200);
			splitPane.setOneTouchExpandable(true);
			splitPane.setLeftComponent(getLeftPanel());
			splitPane.setRightComponent(getRightPanel());
		}
		return splitPane;
	}

	private Component getRightPanel() {
		if(rightPanel==null){
			rightPanel = new PrivilegePanel(connection);
		}
		return rightPanel;
	}

	/**
	 * This method initializes leftPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new JPanel();
			leftPanel.setLayout(new BorderLayout());
			leftPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
			leftPanel.add(getNorthLeftPanel(), java.awt.BorderLayout.NORTH);
			leftPanel.add(getCenterPanel(), java.awt.BorderLayout.CENTER);
		}
		return leftPanel;
	}

	/**
	 * This method initializes northLeftPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNorthLeftPanel() {
		if (northLeftPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			northLeftPanel = new JPanel();
			northLeftPanel.setLayout(flowLayout1);
			northLeftPanel.add(getAddButton(), null);
			northLeftPanel.add(getEditButton(), null);
			northLeftPanel.add(getDeleteButton(), null);
		}
		return northLeftPanel;
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
					onAdd();
				}
			});
		}
		return addButton;
	}

	protected void onAdd() {
		RoleDlg dlg = new RoleDlg(GumundaMainFrame.getMainFrame(), "Add/Edit Role");
		dlg.setVisible(true);
		if(dlg.getResponse()==JOptionPane.OK_OPTION){
			Role role = dlg.getRole();
			
			try {
				saveNewRole(role);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Failed to create new role.\n" + e.getMessage());
			}
			
			refreshRoleList();
		}
	}

	private void refreshRoleList() {
		AppManagerLogic logic = new AppManagerLogic(connection);
		try {
			Role[] roles = logic.getRoles();
			getRoleListModel().clear();
			for(int i=0; i<roles.length; i++){
				Role role = roles[i];
				getRoleListModel().addElement(role);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to load all roles");
		}
	}

	private void saveNewRole(Role role) throws Exception {
		AppManagerLogic logic = new AppManagerLogic(connection);
		logic.addNewRole(role);
	}

	/**
	 * This method initializes deleteButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton();
			deleteButton.setText("Delete");
			deleteButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onDelete();
				}
			});
		}
		return deleteButton;
	}

	protected void onDelete() {
		Role role = (Role) getRoleList().getSelectedValue();
		int response = JOptionPane.showConfirmDialog(this, "Are you sure to delete this role?", "Confirmation", JOptionPane.OK_CANCEL_OPTION);
		if(response==JOptionPane.OK_OPTION){
			try {
				deleteRole(role);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Failed to delete this role.\n" + e.getMessage());
			}
			
			refreshRoleList();
		}
		
	}

	private void deleteRole(Role role) throws Exception {
		AppManagerLogic logic = new AppManagerLogic(connection);
		
		logic.deleteRole(role);
	}

	/**
	 * This method initializes centerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
			centerPanel.add(getRoleScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return centerPanel;
	}

	/**
	 * This method initializes roleList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getRoleList() {
		if (roleList == null) {
			roleList = new JList();
			roleList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			roleList.setVisibleRowCount(5);
			roleList.setModel(getRoleListModel());
			roleList
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(javax.swing.event.ListSelectionEvent e) {
							if(!e.getValueIsAdjusting())
								onListSelectionChanged();
						}
					});
		}
		return roleList;
	}

	protected void onListSelectionChanged() {
		Role role = (Role) getRoleList().getSelectedValue();
		((PrivilegePanel)rightPanel).setRole(role);
		((PrivilegePanel)rightPanel).loadData();
	}

	private DefaultListModel getRoleListModel() {
		if(roleListModel==null){
			roleListModel = new DefaultListModel();
		}
		return roleListModel;
	}

	/**
	 * This method initializes roleScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getRoleScrollPane() {
		if (roleScrollPane == null) {
			roleScrollPane = new JScrollPane();
			roleScrollPane.setViewportView(getRoleList());
		}
		return roleScrollPane;
	}

	/**
	 * This method initializes editButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getEditButton() {
		if (editButton == null) {
			editButton = new JButton();
			editButton.setText("Edit");
			editButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onEdit();
				}
			});
		}
		return editButton;
	}

	protected void onEdit() {
		Role role = (Role) getRoleList().getSelectedValue();
		RoleDlg dlg = new RoleDlg(GumundaMainFrame.getMainFrame(), "Add/Edit Role");
		dlg.setRole(role);
		dlg.setVisible(true);
		if(dlg.getResponse()==JOptionPane.OK_OPTION){
			role = dlg.getRole();
			
			try {
				updateRole(role);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Failed to update selected role.\n" + e.getMessage());
			}
			
			refreshRoleList();
		}
	}

	private void updateRole(Role role) throws Exception {
		AppManagerLogic logic = new AppManagerLogic(connection);
		
		logic.updateRole(role);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
