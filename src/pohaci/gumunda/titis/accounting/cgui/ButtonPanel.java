package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */
import javax.swing.*;
import java.awt.event.*;
public class ButtonPanel extends JPanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JButton m_addBt, m_editBt, m_saveBt, m_deleteBt, m_cancelBt;
  public ButtonPanel(boolean addbt, boolean editbt, boolean savebt, boolean deletebt, boolean cancelbt){

    m_addBt = new JButton("Add");
    m_addBt.setEnabled(addbt);
    m_addBt.setToolTipText("Create new transaction");

    m_editBt = new JButton("Edit");
    m_editBt.setToolTipText("Update a transaction");
    m_editBt.setEnabled(editbt);

    m_saveBt = new JButton("Save");
    m_saveBt.setToolTipText("Save a transaction");
    m_saveBt.setEnabled(savebt);

    m_deleteBt = new JButton("Delete");
    m_deleteBt.setToolTipText("Remove / Delete a transaction");
    m_deleteBt.setEnabled(deletebt);

    m_cancelBt = new JButton("Cancel");
    m_cancelBt.setToolTipText("Cancel current transaction");
    m_cancelBt.setEnabled(cancelbt);

    add(m_addBt);
    add(m_editBt);
    add(m_saveBt);
    add(m_deleteBt);
    add(m_cancelBt);

    initButton();

  }

  public void DisabledAllButton(){
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  public void setButtonAfterNew(){
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
    m_cancelBt.setEnabled(true);
  }

  public void initButton(){
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  public void setButtonAfterSave(){
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_deleteBt.setEnabled(true);
    m_cancelBt.setEnabled(false);
  }

  public JButton getBtAdd(){
    return m_addBt;
  }

  public JButton getBtEdit(){
    return m_editBt;
  }

  public JButton getBtSave(){
    return m_saveBt;
  }

  public JButton getBtCancel(){
    return m_cancelBt;
  }

  public JButton getBtDelete(){
    return m_deleteBt;
  }

  public void setActionListener(ActionListener alistener){
    m_addBt.addActionListener(alistener);
    m_editBt.addActionListener(alistener);
    m_saveBt.addActionListener(alistener);
    m_deleteBt.addActionListener(alistener);
    m_cancelBt.addActionListener(alistener);
  }

}