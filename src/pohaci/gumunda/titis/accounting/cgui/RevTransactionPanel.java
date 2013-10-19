package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import pohaci.gumunda.titis.accounting.helper.ExchangeRateHelper;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import com.jgoodies.binding.formatter.EmptyNumberFormatter;

public abstract class RevTransactionPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected AbstractFormatter m_numberFormatter;
	protected AbstractFormatter m_numberFormatter2;
	protected AbstractFormatter m_numberFormatter3;
	protected GenericMapper m_entityMapper;
	protected Currency baseCurrency;
	protected JButton m_cancelBtn;
	protected JButton m_deleteBtn;
	protected JButton m_editBtn;
	protected JButton m_newBtn;
	protected JButton m_saveBtn;
	protected JButton m_submitBtn;
	protected Connection m_conn = null;
	protected long m_sessionid = -1;
	protected javax.swing.JButton m_searchRefNoBtn;
	protected javax.swing.JButton m_printViewRefNoBtn;
	protected EmptyNumberFormatter m_numberFormatter4;
	protected ExchangeRateHelper exchangeRateHelper;

	protected void initNumberFormats() {
		EmptyNumberFormatter formatter = createNumberFormatA();
		m_numberFormatter = formatter;
		EmptyNumberFormatter formatter2 = new EmptyNumberFormatter(new Double(0));
		formatter2.setAllowsInvalid(false);
		DecimalFormat decformat2 = (DecimalFormat) NumberFormat.getInstance();
		decformat2.applyPattern("#,##0");
		decformat2.setMaximumFractionDigits(2);
		formatter2.setFormat(decformat2);
		m_numberFormatter2 = formatter2;

		EmptyNumberFormatter formatter3 = createNumberFormatA();
		m_numberFormatter3 = formatter3;
	}

	public static EmptyNumberFormatter createNumberFormatA() {
		EmptyNumberFormatter formatter = new EmptyNumberFormatter(new Double(0));
		formatter.setAllowsInvalid(false);
		DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
		decformat.applyPattern("#,##0.00");
		decformat.setMaximumFractionDigits(2);
		formatter.setFormat(decformat);
		return formatter;
	}

	protected abstract StateTemplateEntity currentEntity();
	protected abstract boolean cekValidity();
	protected abstract void gui2entity();
	protected abstract void entity2gui();
	protected abstract void disableEditMode();
	protected abstract void enableEditMode();
	protected abstract Object createNew();
	abstract void setEntity(Object m_entity);
	void submitEntity(){
		try {
			currentEntity().isetBaseCurrency(baseCurrency);
			currentEntity().submit(m_sessionid,m_conn);
			m_entityMapper.doUpdate(currentEntity());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Submit failed. Probably, there is no standard journal found.\n" +
					"Please contact your administrator!", "Error Message", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	protected void doSave() {
		tableStopCellEditing();
		gui2entity();
		if (currentEntity().getStatus()== StateTemplateEntity.State.NEW){
			try {
				beforeSave();
				m_entityMapper.doInsert(currentEntity());
			} catch (Exception ex){
				ex.printStackTrace();
				currentEntity().setStatus(StateTemplateEntity.State.NEW);
				throw new RuntimeException("Cannot save",ex);
			}
		}else if (currentEntity().getStatus()==StateTemplateEntity.State.SAVED)
			m_entityMapper.doUpdate(currentEntity());
		setEntity(currentEntity());
		setEnabledNonSaveCancel(true);
		setEnabledSaveCancel(false);
		readEntityState();
		entity2gui();
		disableEditMode();
	}

	protected void beforeSave() {
		if (currentEntity().status == StateTemplateEntity.State.NEW){
			currentEntity().setStatus(StateTemplateEntity.State.SAVED);
		}
		currentEntity().beforeSave();
	}

	protected void doNew() {
		//clearAll();
		setEntity(createNew());
		currentEntity().isetBaseCurrency(baseCurrency);
		currentEntity().setStatus(StateTemplateEntity.State.NEW);
		entity2gui();
		enableEditMode();
		readEntityState();
	}

	protected void refreshComponents() {
	}

	protected void deleteChilds(){
	}

	protected void doDelete() {
		if (JOptionPane.showConfirmDialog(this,"Are you sure want to delete data?","Delete Confirmation Dialog",JOptionPane.YES_NO_OPTION)==0){
			deleteChilds();
			m_entityMapper.doDelete(currentEntity());
			JOptionPane.showMessageDialog(null,"Deleted succesfully");
			clearAll();
		}
	}

	protected void doSubmit(){
		if (currentEntity().state.allowSubmit()){
			gui2entity();
			boolean isOk = checkBeforeSubmit();
			if (isOk)
				submitEntity();
			entity2gui();
			disableEditMode();
		}
		else
			JOptionPane.showMessageDialog(this,"cannot submit in this state");
	}

	/**
	 *
	 */
	protected boolean checkBeforeSubmit() {
		// sengaja dikosongkan
		// digunakan jika diperlukan aja
		// jika diperlukan, overwrite method ini
		return true;
	}

	protected final void doLoad(Object selectedObj) {
		clearAll();
		setEnabledNonSaveCancel(true);
		setEnabledSaveCancel(false);
		setEntity(selectedObj);
		entity2gui();
		readEntityState();
		disableEditMode();
	}

	protected  void doCancel() {
		tableStopCellEditing();
		if(currentEntity().getStatus()==StateTemplateEntity.State.NEW)
			clearAll();
		else{
			if (currentEntity().getStatus()==0){
				setEntity((StateTemplateEntity) m_entityMapper.doSelectByIndex(new Long(currentEntity().getIndex())));
				m_entityMapper.doUpdate(currentEntity());
				entity2gui();
				readEntityState();
			}
			disableEditMode();
		}
	}

	protected void tableStopCellEditing() {
	}

	protected void clearAll() {
		setEntity(createNew());
		currentEntity().setStatus(StateTemplateEntity.State.NEW);
		stateButtonAwal();
		entity2gui();
	}

	protected void initBaseCurrency(Connection conn, long sessionid) {
		baseCurrency = BaseCurrency.createBaseCurrency(conn, sessionid);
		baseCurrency.setIsBase(true);
	}

	protected void readEntityState() {
		boolean allowDel=currentEntity().state.allowDelete();
		m_deleteBtn.setEnabled(allowDel);
		boolean allowSubmit = currentEntity().state.allowSubmit();
		m_submitBtn.setEnabled(allowSubmit);
		boolean allowEdit = currentEntity().state.allowEdit();
		m_editBtn.setEnabled(allowEdit);
	}

	public void stateButtonAwal(){
		m_newBtn.setEnabled(true);
		m_editBtn.setEnabled(false);
		m_deleteBtn.setEnabled(false);
		m_saveBtn.setEnabled(false);
		m_cancelBtn.setEnabled(false);
		m_submitBtn.setEnabled(false);
	}

	public void actionPerformedParents(ActionEvent e) {
		if (e.getSource() == m_saveBtn) {
			doSave();
		}
		else if (e.getSource() == m_cancelBtn) {
			setEnabledSaveCancel(false);
			setEnabledNonSaveCancel(true);
			doCancel();
		}
		else if (e.getSource() == m_newBtn) {
			setEnabledSaveCancel(true);
			setEnabledNonSaveCancel(false);
			doNew();
		}
		else if (e.getSource() == m_submitBtn) {
			doSubmit();
		}
		else if (e.getSource() == m_editBtn){
			doEdit();
		}
		else if (e.getSource() == m_deleteBtn){
			doDelete();
		}
	}

	public void  doEdit(){
		enableEditMode();
		setEnabledNonSaveCancel(false);
		setEnabledSaveCancel(true);
	}

	public void setEnabledNonSaveCancel(boolean bol){
		m_newBtn.setEnabled(bol);
		m_editBtn.setEnabled(bol);
		m_deleteBtn.setEnabled(bol);
		m_submitBtn.setEnabled(bol);
		m_printViewRefNoBtn.setEnabled(bol);
		m_searchRefNoBtn.setEnabled(bol);
	}

	public void setEnabledSaveCancel(boolean bol){
		m_cancelBtn.setEnabled(bol);
		m_saveBtn.setEnabled(bol);
	}
	protected void addingListenerParents(){
		m_newBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionPerformedParents(evt);
			}
		});
		m_editBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionPerformedParents(evt);
			}
		});
		m_deleteBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionPerformedParents(evt);
			}
		});
		m_saveBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionPerformedParents(evt);
			}
		});
		m_cancelBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionPerformedParents(evt);
			}
		});
		m_submitBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionPerformedParents(evt);
			}
		});

	}

	public String getEmployeeJobTitle(Employee employee){
		if(employee!=null){
			Employee newEmp = employee;
			Employment[] employs = getEmployeeEmployment(newEmp.getIndex());
			if (employs.length>0){
				Employment  employment = getMaxEmployment(employs);
				if(employment!=null){
					if(employment.getJobTitle()!=null)
						return employment.getJobTitle().getName();
					else
						return "";
				}else{
					return "";
				}
			}
		}
		return "";
	}

	private Employment[] getEmployeeEmployment(long index) {
		Employment[] employment = null;
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			employment = logic.getEmployeeEmployment(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, index);
			return employment;
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		return null;
	}

	private Employment getMaxEmployment(Employment[] employ) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		Date currentday = calendar.getTime();

		java.util.Date befdate = null, afterdate = null;
		int index = -1;

		for (int i = 0; i < employ.length; i++) {
			if (befdate == null) {
				befdate = employ[i].getTMT();
				index = i;
			}
			if (i + 1 < employ.length) {
				afterdate = employ[i + 1].getTMT();
				if(afterdate.after(currentday)) {
					break;
				}
				if (befdate.compareTo(afterdate) == -1) {
					befdate = afterdate;
					index = i + 1;
				}
			}
		}
		Employment emp = null;
		if (index != -1) {
			emp = employ[index];
		}
		return emp;
	}

	protected void setenableEditPanel(JPanel temppanel, boolean bol) {
		Component[] componentList=temppanel.getComponents();
		JTextField temptext;
		for (int i=0;i<componentList.length;i++){
			if (componentList[i] instanceof JTextField){
				temptext=(JTextField)componentList[i];
				temptext.setEditable(bol);
			}
		}
	}

	protected void initExchangeRateHelper(Connection conn, long sessionid) {
		exchangeRateHelper = new ExchangeRateHelper(conn, sessionid);
	}

	public double getDefaultExchangeRate(Currency currency, Date date) {
		Date today = new Date();

		if (date == null)
			date = today;

		if (currency == null)
			currency = baseCurrency;

		double rate = exchangeRateHelper.getExchangeRate(currency, baseCurrency, date);
		return rate;
	}
}

