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

import java.util.*;

import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class HolidayJDayChooser extends JDayChooser implements FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Color redColor = Color.red;

	HolidayPanel m_panel;

	Connection m_conn = null;

	long m_sessionid = -1;

	Holiday holidayMinusSatu;

	public HolidayJDayChooser(HolidayPanel panel, Connection conn,
			long sessionid) {
		m_panel = panel;
		m_conn = conn;
		m_sessionid = sessionid;
		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 7; x++) {
				int index = x + 7 * y;
				if (y != 0) {
					days[index].addFocusListener(this);
				}
			}
		}

		initHoliday();
	}

	public HolidayJDayChooser(Connection m_conn2, long m_sessionid2) {
		m_conn = m_conn2;
		m_sessionid = m_sessionid2;
		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 7; x++) {
				int index = x + 7 * y;
				if (y != 0) {
					days[index].addFocusListener(this);
				}
			}
		}

		initHoliday();
	}

	public void focusGained(FocusEvent e) {
		try
		{
			super.focusGained(e);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void focusLost(FocusEvent e) {
		try
		{
		System.out.println("FOKUS LOST m_panel.getHoliday()= "
				+ calendar.getTime());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		//holidayMinusSatu = m_panel.getHoliday();
	}

	public void clear() {
		m_panel.clear();
	}
	
	Date holidaySelectedDate = null;
	
	boolean databaseLoaded = false;
	
	public void setDay(int d) {		
		Date oldDate = holidaySelectedDate;
		//if (databaseLoaded)
		//	holidaySelectedDate = selectedDate;
		if (holidaySelectedDate!=null)
			periksaDanSave(holidaySelectedDate);
		super.setDay(d); // selectedDate diisi di sini
		redrawDay(d);
		Holiday h = null;
		if (!databaseLoaded)
			return;
		holidaySelectedDate = selectedDate;
		Date newDate = holidaySelectedDate;
		System.out.println("olddate " + oldDate + " newdate " + newDate);
		if (isHoliday(holidaySelectedDate)) // selected Date baru lho
			h=findHolidayAt(holidaySelectedDate);
		m_panel.setHoliday(h);
		holidayMinusSatu = h;
	}

	public void setMonth(int month) {
		super.setMonth(month);
		initHoliday();
		// redrawDay(day);
	}

	public void setYear(int year) {
		super.setYear(year);
		initHoliday();
		// redrawDay(day);
	}
	void commitEdits()
	{
		if (holidayMinusSatu!=null) 
		{
			Holiday h = holidayMinusSatu;
			String tmpDesc = m_panel.m_descriptionTextField.getText().trim();
			if (!tmpDesc.equals(h.getDescription()))
			{
				h.setDescription(tmpDesc);
				if (!h.inserted) {
					h.updated = true;
				}
			}
		}
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		//commitEdits();
		/*System.out
				.println("-----------------------------------------BATAS ---------");
		m_panel.clear();
		if (isHoliday(selectedDate)) {
			m_panel.setHoliday(findHolidayAt(selectedDate));
			if (!m_panel.m_editBt.isEnabled()) {
				m_panel.m_descriptionTextField.setEditable(true);
			}

			System.out.println("m.actionPerformed -> setHoliday *****"
					+ selectedDate + "*********"
					+ m_panel.getHoliday().getDescription());
		} else {
			m_panel.m_descriptionTextField.setEditable(false);
			System.out.println("m.actionPerformed -> Not-Holiday *****"
					+ selectedDate + "*********");
		}*/
	}

	public void insertDescriptionToLastHoliday(Holiday holiday,
			String lastDescription) {
		if (holiday!=null) 
		if (findHolidayAt(holiday.getDate()) != null)
		{
			Holiday h = findHolidayAt(holiday.getDate());
			if (!lastDescription.equals(h.getDescription()))
			{
				h.setDescription(lastDescription);
				System.out
					.println("===================================ADA++++++++++++++++++++++++++++");
				if (h.inserted) {
					// nothing
				} else {
					h.updated = true;
				}
			}
		}
		System.out.println(" TEst euy = " + holiday.getDescription());
	}

	public void periksaDanSave(Date date) {
		if (date==null)
			return;
		// executed only and if only edit button was killed
		if (!m_panel.m_editBt.isEnabled()) {
			if (isHoliday(date)) {
				if (m_panel.m_publicCheckBox.isSelected()) {
					commitEdits();
				} else {
					//removeHolidayAtDate(holidayMinusSatu);
					if (holidayMinusSatu.inserted)
						removeHolidayAtDate(holidaySelectedDate);
					else
					{
						holidayMinusSatu.deleted = true;
					}
					/*
					System.out
							.println("---------------holiday.deleted = true;");
					holiday.inserted = false;
					addHoliday2(holiday);
					System.out
							.println("----------------------REMOVE (setSelecteddateIsHoliday)0000000000000000000    cekbokTerpilih tak terpilih");
*/
					setDay(day);
					//m_panel.setHoliday(null);
				}
			} else {
				if (m_panel.m_publicCheckBox.isSelected()) {
					//Holiday holiday = m_panel.getHoliday();
					if (holidayMinusSatu==null)
					{
						Holiday holiday = m_panel.getHoliday();
						addHoliday2(holiday);						
						holiday.inserted = true;
						setDay(day);
					}
					/*if (tempVector.contains(holiday.getDate())) {
						System.out
								.println("----AYAAAANNNNN EUY   READY TO DELETE");
						removeHolidayAtDate(holiday.getDate());
					}
					holiday.inserted = true;
					holiday.deleted = false;
					System.out
							.println("---------------holiday.deleted = false;");
					// warnai background jadi merah
					selectedDay.setBackground(redColor);
					System.out
							.println("ADDHoliday "
									+ holiday.m_description
									+ "  HolidayJDayChooser.setSelecteddateIsHoliday=====================	ADDHoliday");
					addHoliday2(holiday);*/
				} else {
					// doing nothing, of course!! dont ask me why ...
				}
			}
		}
	}

	public Date roundToDate(Date d) {
		long rounded = d.getTime() - (d.getTime() % (24L * 60L * 60 * 1000L));
		return new Date(rounded);
	}

	public void initHoliday() {
		Date beginDate = null, endDate = null;
		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);
		beginDate = roundToDate(tmpCalendar.getTime());
		tmpCalendar.add(Calendar.MONTH, 1);
		tmpCalendar.add(Calendar.DATE, -1);
		endDate = roundToDate(tmpCalendar.getTime());

		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			// ini kayanya salah
			Holiday[] holidays = logic.getHoliday(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, beginDate, endDate);
			int i;
			clearVector();
			for (i = 0; i < holidays.length; i++) {
				Holiday h = holidays[i];
				addHoliday(h);
			}
			databaseLoaded = true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		} finally {
			redrawDay(day);
		}
	}

	public void redrawDay(int day) {
		System.out.println("reddrawDay methode");
		Calendar tmpCalendar = (Calendar) calendar.clone();
		for (int i = 7; i < 49; i++) {
			if (days[i].getText().equals(Integer.toString(day))) {
				// selectedDay = days[i];
				days[i].setBackground(selectedColor);
			} else if (days[i].isVisible()) {
				tmpCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(days[i]
						.getText()));
				Date date = tmpCalendar.getTime();
				if (isHoliday(date)) {

					days[i].setBackground(redColor);
//					System.out
//							.print(" ------------------------------------------- RED RED RED , in Button number = ");
//					System.out.println(days[i].getLabel() + "  "
//							+ date.toString().substring(3, 10));

				}
			}
		}
	}

	// this methode will dissapear soon
	public void setSelecteddateIsHoliday(boolean cekbokTerpilih) {
/*		if (cekbokTerpilih) {
			periksaDanSave(selectedDate);
			// ambil obyek holiday dari view
			Holiday holiday = m_panel.getHoliday();
			// warnai background jadi merah
			selectedDay.setBackground(redColor);
			System.out
					.println("addHoliday "
							+ holiday.m_description
							+ "  HolidayJDayChooser.setSelecteddateIsHoliday=====================	m_description");
			// addHoliday2(holiday);
		} else if (cekbokTerpilih == false && isHoliday(selectedDate)) {
			System.out
					.println("----------------------REMOVE (setSelecteddateIsHoliday)0000000000000000000    cekbokTerpilih tak terpilih");
			removeHolidayAtDate(selectedDate);
			setDay(day); // biar refresh ?
		}*/
	}

	// TAKEN FROM DATABASE
	private void addHoliday(Holiday holiday) {
		System.out.println("roundDate = " + roundToDate(holiday.getDate()));
		Date d = roundToDateForAddHoliday(holiday.getDate());
		System.out.println("roundToDateForAddHoliday" + d
				+ " --------------------------------addHoliday ");
		// preventing of doubleing data
		if (!tempVector.contains(d)) {
			tempVector.add(d);
			objecTempVector.addElement(holiday);
		}
	}

	// TAKEN FROM HOLIDAYPANEL (BASED ON USER SELECTED)
	private void addHoliday2(Holiday holiday) {
		Date d = roundToDate(holiday.getDate());
		System.out.println("roundToDate" + d
				+ " -------------------------------- addHoliday 2 ");
		// preventing of doubleing data, replace by the new holiday
		if (tempVector.contains(d)) {
			removeHolidayAtDate(d);
		}
		tempVector.add(d);
		objecTempVector.addElement(holiday);

	}

	private Date roundToDateForAddHoliday(Date d) {
		long rounded = d.getTime() + (7L * 60L * 60 * 1000L);
		return new Date(rounded);
	}

	public void removeHolidayAtDate(Date d) {
		int index = tempVector.indexOf(roundToDate(d));
		tempVector.removeElementAt(index);
		objecTempVector.removeElementAt(index);
	}

	public boolean isHoliday(Date date) {
		Date d = roundToDate(date);
		if (tempVector.contains(d)) {
			Holiday holiday = findHolidayAt(date);
			if (holiday.deleted == false) {
				return true;
			}
		}
		return false;

	}

	public Holiday findHolidayAt(Date d) {
		d = roundToDate(d);
		int index = tempVector.indexOf(d);
		if (index != -1) {
			return (Holiday) objecTempVector.elementAt(index);
		}
		return null;
	}

	public Holiday[] getHoliday() {
		Holiday[] holiday = new Holiday[objecTempVector.size()];
		objecTempVector.copyInto(holiday);
		return holiday;
	}

	public void clearVector() {
		objecTempVector.clear();
		tempVector.clear();
	}

}