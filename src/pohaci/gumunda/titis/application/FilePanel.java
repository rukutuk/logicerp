package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

public class FilePanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JTextField m_nameTextField, m_pathTextField;
  JButton m_downloadBt, m_deleteBt, m_browseBt, m_uploadBt;

  File m_file = null;
  String m_title = "";
  String m_filename = "";
  byte[] m_filebyte = null;

  public FilePanel(String title) {
    m_title = title;
    constructComponent(title);
  }

  void constructComponent(String title) {
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title + ":",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
    
    m_nameTextField = new JTextField();
    m_nameTextField.setRequestFocusEnabled(false);
    m_pathTextField = new JTextField();
    m_pathTextField.setRequestFocusEnabled(false);
    m_downloadBt = new JButton("Downloads");
    m_downloadBt.setPreferredSize(new Dimension(90, 22));
    m_downloadBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.setPreferredSize(new Dimension(70, 22));
    m_deleteBt.addActionListener(this);
    m_browseBt = new JButton("...");
    m_browseBt.setPreferredSize(new Dimension(20, 22));
    m_browseBt.addActionListener(this);
    m_uploadBt = new JButton("Uploads");
    m_uploadBt.setPreferredSize(new Dimension(80, 22));
    m_uploadBt.addActionListener(this);

    JLabel nameLabel = new JLabel("File Name");
    nameLabel.setPreferredSize(new Dimension(50, 22));
    JLabel pathLabel = new JLabel("File Path");
    pathLabel.setPreferredSize(new Dimension(50, 22));
    JPanel namePanel = new JPanel();
    JPanel pathPanel = new JPanel();

    namePanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new Insets(2, 1, 10, 1);
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    namePanel.add(nameLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    namePanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    namePanel.add(m_nameTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    namePanel.add(m_downloadBt, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    namePanel.add(m_deleteBt, gridBagConstraints);

    pathPanel.setLayout(new GridBagLayout());

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new Insets(2, 1, 2, 1);
    pathPanel.add(pathLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    pathPanel.add(new JLabel("  "), gridBagConstraints);

    gridBagConstraints.gridx = 2;
    gridBagConstraints.weightx = 1.0;
    pathPanel.add(m_pathTextField, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.gridwidth = 1;
    pathPanel.add(m_browseBt, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    pathPanel.add(m_uploadBt, gridBagConstraints);

    setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
    add(pathPanel, gridBagConstraints);

    gridBagConstraints.gridy = 1;
    add(namePanel, gridBagConstraints);
  }

  public void setEditable(boolean editable) {
    //m_nameTextField.setRequestFocusEnabled(editable);      
    //m_pathTextField.setRequestFocusEnabled(editable);
    m_downloadBt.setEnabled(editable);
    m_deleteBt.setEnabled(editable);
    m_browseBt.setEnabled(editable);
    m_uploadBt.setEnabled(editable);
  }

  public void clear() {
    m_filebyte = null;
    m_filename = "";
    m_pathTextField.setText("");
    m_nameTextField.setText("");
  }

  public void setFileByte(byte[] filebyte) {
    m_filebyte = filebyte;
  }

  public byte[] getFileByte() {
    return m_filebyte;
  }

  public void setFileName(String name) {
    m_filename = name;        
    m_nameTextField.setText(m_filename);    
  }

  public String getFileName() {
    return m_filename;
  }

  void browse() {
    JFileChooser jfc = new JFileChooser();
    jfc.setDialogTitle(m_title);
    jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
      public boolean accept(java.io.File file) {
        if(file.isDirectory())
          return true;
        return true;
      }
      public String getDescription() {
        return m_title;
      }
    });

    jfc.setMultiSelectionEnabled(false);
    jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
    if  (jfc.showOpenDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame()) == javax.swing.JOptionPane.OK_OPTION) {
      m_file = jfc.getSelectedFile();
      try {
        int index = m_file.getAbsolutePath().indexOf(m_file.getName());
        String filepath = m_file.getAbsolutePath().substring(0, index);
        m_filename = m_file.getName();

        m_pathTextField.setText(filepath);
        m_nameTextField.setText(m_filename);
        System.err.println("file name browses :" + m_filename);
      }
      catch (Exception ex){
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                      "Information", JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  public void download() {
    if(m_filebyte == null)
      return;

    try {
      java.util.Properties prop = System.getProperties();
      String sdir = prop.getProperty("user.dir");
      java.io.File file = new java.io.File( sdir + "\\document\\" + m_filename.trim() );
      java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
      fos.write(m_filebyte);
      fos.flush();
      fos.close();

      JOptionPane.showMessageDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                    m_filename + " have been copied to " + sdir);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void upload() {
    if(m_file == null)
      return;

    if(!Misc.getConfirmation())
       return;

    try {
      FileInputStream istream = new FileInputStream(m_file);
      byte imgbyte[] = new byte[(int)m_file.length()];
      istream.read(imgbyte);
      m_filebyte = imgbyte;
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void delete() {
    if(!Misc.getConfirmation())
       return;

    m_filebyte = null;
    m_filename = "";
    m_pathTextField.setText("");
    m_nameTextField.setText("");
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_browseBt) {
      browse();
    }
    else if(e.getSource() == m_deleteBt) {
      delete();
    }
    else if(e.getSource() == m_uploadBt) {
      upload();
    }
    else if(e.getSource() == m_downloadBt) {
      download();
    }
  }
}