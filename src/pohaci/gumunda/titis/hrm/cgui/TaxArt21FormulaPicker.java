package pohaci.gumunda.titis.hrm.cgui;

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

import pohaci.gumunda.titis.application.FormulaEntity;

public class TaxArt21FormulaPicker extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextField m_formulaTextField;

	JButton m_browseBt = new JButton("...");

	Connection m_conn = null;

	long m_sessionid = -1;

	private FormulaEntity formulaEntity;

	public TaxArt21FormulaPicker(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;

		m_browseBt.setPreferredSize(new Dimension(22, 18));
		m_browseBt.addActionListener(this);

		m_formulaTextField = new JTextField() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void addMouseListener(MouseListener l) {
			}

			public boolean isFocusTraversable() {
				return false;
			}
		};

		setLayout(new BorderLayout(3, 1));
		add(m_formulaTextField, BorderLayout.CENTER);
		add(m_browseBt, BorderLayout.EAST);
	}

	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		m_formulaTextField.setEditable(enable);
		m_browseBt.setEnabled(enable);
	}

	public void setFormulaEntity(FormulaEntity formulaEntity) {
		this.formulaEntity = formulaEntity;
		if (formulaEntity != null)
			m_formulaTextField.setText(formulaEntity.toString());
		else
			m_formulaTextField.setText("");
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_browseBt) {
			PayrollCategoryFormulaDlg dlg = new PayrollCategoryFormulaDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					m_conn, m_sessionid, formulaEntity, false);
			dlg.setVisible(true);
			
			if (dlg.getResponse() == JOptionPane.OK_OPTION) {
				setFormulaEntity(dlg.getFormulaEntity());
			} else
				setFormulaEntity(null);
		}
	}

	public FormulaEntity getFormulaEntity() {
		return formulaEntity;
	}
}