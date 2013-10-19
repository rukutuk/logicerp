/**
 *
 */
package pohaci.gumunda.titis.appmanager.cgui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.FlowLayout;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.appmanager.entity.UserProfile;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;

/**
 * @author dark-knight
 *
 */
public class UserAdministrationPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane = null;
	private JTable table = null;
	private JPanel northPanel = null;
	private JButton addButton = null;
	private JButton editButton = null;
	private JButton deleteButton = null;
	private DefaultTableModel tableModel;
	protected boolean isEditing = false;
	private int editedRow;
	private Connection connection = null;

	private final static boolean PASSWORD_MODE = true;

	/**
	 * This is the default constructor
	 */
	public UserAdministrationPanel(Connection connection) {
		super();
		this.connection = connection;
		initialize();
		initializeDB();
	}

	private void initializeDB() {
		AppManagerLogic logic = new AppManagerLogic(connection);
		UserProfile[] userProfiles = null;
		try {
			userProfiles = logic.getUserProfiles();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(userProfiles!=null){
			for(int i=0; i<userProfiles.length; i++){
				addRow(userProfiles[i]);
			}
		}
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(382, 100);
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
		this.add(getScrollPane(), java.awt.BorderLayout.CENTER);
		this.add(getNorthPanel(), java.awt.BorderLayout.NORTH);
		setEditingMode(false);
	}

	/**
	 * This method initializes scrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}

	/**
	 * This method initializes table
	 *
	 * @return javax.swing.JTable
	 */
	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			table.setModel(getTableModel());
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						int row = ((ListSelectionModel) e.getSource()).getMinSelectionIndex();

						if (isEditing) {
							if (row != editedRow)
								getTable().getSelectionModel().setSelectionInterval(editedRow, editedRow);
						} else {
							setEditingMode(false);
						}
					}
				}
			});
		}
		return table;
	}

	private DefaultTableModel getTableModel() {
		if(tableModel==null){
			tableModel = new DefaultTableModel() {

				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return false;
				}

			};
			tableModel.addColumn("User Name");
			if(PASSWORD_MODE)
				tableModel.addColumn("Password");
			tableModel.addColumn("Full Name");
			tableModel.addColumn("Note");
		}
		return tableModel;
	}

	/**
	 * This method initializes northPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			northPanel = new JPanel();
			northPanel.setLayout(flowLayout);
			northPanel.add(getAddButton(), null);
			northPanel.add(getEditButton(), null);
			northPanel.add(getDeleteButton(), null);
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
					onAdd();
				}
			});
		}
		return addButton;
	}

	protected void onAdd() {
		UserProfileAdministrationDlg dlg = new UserProfileAdministrationDlg(GumundaMainFrame.getMainFrame(), "User Profile", connection);
		dlg.setVisible(true);
		if(dlg.getResponse()==JOptionPane.OK_OPTION){
			UserProfile userProfile = dlg.getUser();
			addRow(userProfile);
		}
	}

	private void addRow(UserProfile userProfile) {
		if (PASSWORD_MODE)
			getTableModel().addRow(
					new Object[] { userProfile,
							userProfile.getDecryptedPassword(),
							userProfile.getFullname(), userProfile.getNote() });
		else
			getTableModel().addRow(
					new Object[] { userProfile, userProfile.getFullname(),
							userProfile.getNote() });
	}

	private void setEditingMode(boolean isEditing) {
		this.isEditing = isEditing;

		getAddButton().setEnabled(!isEditing);
		getEditButton().setEnabled(!isEditing);
		getDeleteButton().setEnabled(!isEditing);
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
		int row = getTable().getSelectedRow();
		if(row==-1)
			return;

		UserProfile userProfile = (UserProfile) getTable().getValueAt(row, 0);

		UserProfileAdministrationDlg dlg = new UserProfileAdministrationDlg(GumundaMainFrame.getMainFrame(), "User Profile", connection);
		dlg.setUser(userProfile);
		dlg.setVisible(true);
		if(dlg.getResponse()==JOptionPane.OK_OPTION){
			userProfile = dlg.getUser();
			if(PASSWORD_MODE){
				getTable().setValueAt(userProfile, row, 0);
				getTable().setValueAt(userProfile.getDecryptedPassword(), row, 1);
				getTable().setValueAt(userProfile.getFullname(), row, 2);
				getTable().setValueAt(userProfile.getNote(), row, 3);
			}else{
				getTable().setValueAt(userProfile, row, 0);
				getTable().setValueAt(userProfile.getFullname(), row, 1);
				getTable().setValueAt(userProfile.getNote(), row, 2);
			}
		}
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
		int row = getTable().getSelectedRow();
		if(row==-1)
			return;

		int response = JOptionPane.showConfirmDialog(this, "Are you sure to delete this user", "Confirmation", JOptionPane.YES_NO_OPTION);
		if(response==JOptionPane.YES_OPTION){
			UserProfile userProfile = (UserProfile) getTable().getValueAt(row, 0);

			AppManagerLogic logic = new AppManagerLogic(connection);
			try {
				logic.removeUserRoles(userProfile);
				getTableModel().removeRow(row);
			} catch (Exception e) {
				e.printStackTrace();
			}


		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
