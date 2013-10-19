package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.project.cgui.report.ProjectMonitoringNotesJasper;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class ProjectNotesPanel extends JPanel implements ActionListener{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JButton m_excellBt, m_filterBt, m_refreshBt;
  JButton m_addBt, m_editBt, m_deleteBt;
  NotesTable m_table;

  boolean m_show = false;
  ProjectData m_project = null;
  ProjectNotes[] m_notes =  new ProjectNotes[0];

  public ProjectNotesPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    setEditable(false);
  }

  void constructComponent() {
    m_table = new NotesTable();
    m_excellBt = new JButton(new ImageIcon("../images/excell.gif"));
    m_excellBt.addActionListener(this);
    m_filterBt = new JButton(new ImageIcon("../images/filter2.gif"));
    m_filterBt.addActionListener(this);
    m_refreshBt = new JButton(new ImageIcon("../images/refresh.gif"));
    m_refreshBt.addActionListener(this);
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);

    JToolBar northbar = new JToolBar();
    JPanel buttonPanel = new JPanel();

    northbar.setFloatable(false);
    northbar.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    northbar.add(m_excellBt, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northbar.add(m_filterBt, gridBagConstraints);

    gridBagConstraints.gridx = 2;
    northbar.add(m_refreshBt, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northbar.add(buttonPanel, gridBagConstraints);

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    add(northbar, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }

  public void setProjectData(ProjectData project) {
    m_project = project;
  }
  
  public void setEditable(boolean editable) {
	    m_addBt.setEnabled(editable);
	    m_editBt.setEnabled(false);
	    m_deleteBt.setEnabled(false);

	    m_excellBt.setEnabled(editable);
	    m_filterBt.setEnabled(editable);
	    m_refreshBt.setEnabled(editable);
  }
  
  public void setNotes(ProjectData project) {
    m_project = project;
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_notes = logic.getProjectNotes(m_sessionid,
          IDBConstants.MODUL_PROJECT_MANAGEMENT, project.getIndex());
      reset(m_notes);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }
  
  public void setM_notes(){
	  try {
		  ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
	      m_notes = logic.getProjectNotes(m_sessionid,
	          IDBConstants.MODUL_PROJECT_MANAGEMENT, m_project.getIndex());
	  } catch(Exception ex) {
          JOptionPane.showMessageDialog(this, ex.getMessage(),
                                        "Warning", JOptionPane.WARNING_MESSAGE);
        }
  }
 
  void refresh() {
    setNotes(m_project); // saat tabel berubah 
    //reset(m_notes); // saat tabel tidak berubah panggil kelas reset    
  }

  public void reset(ProjectNotes[] notes) {    
	m_notes = notes;
    m_table.setNotes(m_notes);
  }

  void onAdd() {
    if(m_project == null)
      return;

    ProjectNotesDlg dlg = new ProjectNotesDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, m_project);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      ProjectNotes notes = dlg.getNotes();
      m_table.addNotes(notes);
      setM_notes();
    }
  }

  void onEdit() {
    int row = m_table.getSelectedRow();
    ProjectNotes notes = (ProjectNotes)m_table.getValueAt(row, 1);
    ProjectNotesDlg dlg = new ProjectNotesDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, notes);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      notes = dlg.getNotes();      
      m_table.updateNotes(notes, row);
      setM_notes();
    }
    m_table.updateNumber(0);
  }

  void onDelete() {
    int row = m_table.getSelectedRow();
    ProjectNotes notes = (ProjectNotes)m_table.getValueAt(row, 1);

    if(!Misc.getConfirmation())
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteProjectNotes(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, notes.getIndex());    
      setM_notes();
      //setNotes(m_project);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    ((DefaultTableModel)m_table.getModel()).removeRow(row);
    m_table.updateNumber(0);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_excellBt) {
        new ProjectMonitoringNotesJasper(m_conn,m_notes,true);
    }
    else if(e.getSource() == m_filterBt) {
      if(!m_show) {
        m_show = true;
        SearchProjectNoteDlg dlg = new SearchProjectNoteDlg(
            pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            this, m_project, m_conn, m_sessionid);
        dlg.setVisible(true);
        m_show = false;
      }
    }
    else if(e.getSource() == m_refreshBt) {
      refresh();
    }
    else if(e.getSource() == m_addBt) {
      onAdd();
    }
    else if(e.getSource() == m_editBt) {
      onEdit();
    }
    else if(e.getSource() == m_deleteBt) {
      onDelete();
    }
  }

  /**
   *
   */
  class NotesTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotesTable() {
      NotesTableModel model = new NotesTableModel();
      model.addColumn("No");
      model.addColumn("Date");
      model.addColumn("Description");
      model.addColumn("Action");
      model.addColumn("Responsibility");
      model.addColumn("Prepared By");
      model.addColumn("Approvered By");
      model.addColumn("Remark");
      model.addColumn("Attachment");
      setModel(model);
      getSelectionModel().addListSelectionListener(model);

      addMouseListener(new MouseAdapter() {
        public void mouseClicked( MouseEvent e ) {
          if(e.getClickCount() >= 2) {
            onEdit();
          }
        }
      });
    }
	
	public void addNotes(ProjectNotes notes) {      
      DefaultTableModel model = (DefaultTableModel)getModel();      
      model.addRow(new Object[]{
        String.valueOf(getRowCount() + 1), notes, notes.getDescription(),
        notes.getAction(), notes.getResponsibility(), notes.getPreparedBy(),
        notes.getApprover(), notes.getRemark(), notes.getFile()
      });
    }

    public void updateNotes(ProjectNotes notes, int row) {      
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        String.valueOf(row + 1), notes, notes.getDescription(),
        notes.getAction(), notes.getResponsibility(), notes.getPreparedBy(),
        notes.getApprover(), notes.getRemark(), notes.getFile()
      });
      this.getSelectionModel().setSelectionInterval(row, row);
      updateNumber(0);      
    }

    public void setNotes(ProjectNotes[] notes) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      m_notes = notes;
      for(int i = 0; i < notes.length; i ++) {
        model.addRow(new Object[]{
        String.valueOf(i + 1), m_notes[i], notes[i].getDescription(),
        m_notes[i].getAction(), m_notes[i].getResponsibility(), m_notes[i].getPreparedBy(),
        m_notes[i].getApprover(), m_notes[i].getRemark(), m_notes[i].getFile()
      });
      }
    }
    
    void updateNumber(int col) {
        for(int i = 0; i < getRowCount(); i ++)
            setValueAt(String.valueOf(i + 1), i, col); 
    }
  }

  /**
   *
   */
  class NotesTableModel extends DefaultTableModel implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }

    public void valueChanged(ListSelectionEvent e) {
      if(!e.getValueIsAdjusting()){
        int iRowIndex = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();

        if(iRowIndex != -1) {
          m_addBt.setEnabled(true);
          m_editBt.setEnabled(true);
          m_deleteBt.setEnabled(true);
        }
        else{
          m_addBt.setEnabled(true);
          m_deleteBt.setEnabled(false);
          m_editBt.setEnabled(false);
        }
      }
    }
  }
}
