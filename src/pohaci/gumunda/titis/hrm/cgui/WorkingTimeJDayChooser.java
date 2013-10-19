package pohaci.gumunda.titis.hrm.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;

import java.util.*;

import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class WorkingTimeJDayChooser extends JDayChooser implements
		FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Color redColor = Color.red;

	// HolidayPanel m_panel;

	WorkingTimePanel m_workingTimePanel;

	Connection m_conn = null;

	long m_sessionid = -1;

	Holiday holidayMinusSatu;

	private Vector vectorState;

	private Vector vectorStateObject;

//	private String overtime;

	public WorkingTimeJDayChooser(WorkingTimePanel workingtimepanel,
			Connection conn, long sessionid) {
		m_workingTimePanel = workingtimepanel;
		// m_panel = panel;
		m_conn = conn;
		m_sessionid = sessionid;

		vectorState = new Vector();
		vectorStateObject = new Vector();

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

	public WorkingTimeJDayChooser(Connection m_conn2, long m_sessionid2) {
		m_conn = m_conn2;
		m_sessionid = m_sessionid2;
		vectorState = new Vector();
		vectorStateObject = new Vector();
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
		try {
			super.focusGained(e);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void focusLost(FocusEvent e) {

		try {
			System.out.println("FOKUS LOST m_panel.getHoliday()= "
					+ calendar.getTime());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	Date holidaySelectedDate = null;

	boolean databaseLoaded = false;

	public void setDay(int d) {
		/*
		 * Date oldDate = holidaySelectedDate;
		 * 
		 * if (holidaySelectedDate != null) periksaDanSave(holidaySelectedDate);
		 * super.setDay(d); // selectedDate diisi di sini redrawDay(d); Holiday
		 * h = null; if (!databaseLoaded) return; holidaySelectedDate =
		 * selectedDate; Date newDate = holidaySelectedDate;
		 * System.out.println("olddate " + oldDate + " newdate " + newDate); if
		 * (isHoliday(holidaySelectedDate)) // selected Date baru lho h =
		 * findHolidayAt(holidaySelectedDate);
		 * 
		 * holidayMinusSatu = h;
		 */
		super.setDay(d); // selectedDate diisi di sini
		redrawDay(d);
	}

	public void setMonth(int month) {
		super.setMonth(month);
		initHoliday();
		if (m_workingTimePanel.getEmployee()!=null)
			initEmployeeOfficeWorkingTime(m_workingTimePanel.getEmployee());

	}

	public void setYear(int year) {
		super.setYear(year);
		initHoliday();
		if (m_workingTimePanel.getEmployee()!=null)
			initEmployeeOfficeWorkingTime(m_workingTimePanel.getEmployee());

	}

	void commitEdits() {
		/*
		 * if (holidayMinusSatu != null) { Holiday h = holidayMinusSatu; String
		 * tmpDesc = m_panel.m_descriptionTextField.getText().trim(); if
		 * (!tmpDesc.equals(h.getDescription())) { h.setDescription(tmpDesc); if
		 * (!h.inserted) { h.updated = true; } } }
		 */
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		m_workingTimePanel.clear();
		m_workingTimePanel.setSelectedRadioButton(getState(selectedDate),
				getOvertimeValue(selectedDate));
	}

	public void insertDescriptionToLastHoliday(Holiday holiday,
			String lastDescription) {
		if (holiday != null)
			if (findHolidayAt(holiday.getDate()) != null) {
				Holiday h = findHolidayAt(holiday.getDate());
				if (!lastDescription.equals(h.getDescription())) {
					h.setDescription(lastDescription);

					if (h.inserted) {
						// nothing
					} else {
						h.updated = true;
					}
				}
			}
		System.out.println(" TEst euy = " + holiday.getDescription());
	}

	public void SetStatusSelectedDate(int state) {
		// addWorkingTimeState(m_workingTimePanel.getWorkingTimeState());

		periksaDanSave(m_workingTimePanel.getWorkingTimeState());
		redrawDay(0);
	}

	// *************************************************************
	public void periksaDanSave(WorkingTimeState workingTimeState) {
		Date date = workingTimeState.getDate();
		if (date == null)
			return;
		// executed only and if only edit button was killed
		if (!m_workingTimePanel.m_editBt.isEnabled()) {

			if (adaDiVectorGaYa(date)) {

				// ada donks
				WorkingTimeState wts = findWorkingTimeState(date);
				System.out.println("--         adaDiVectorGaYa ??  ada donks "
						+ workingTimeState.getDate() + " , new state= "
						+ workingTimeState.getState());

				if (workingTimeState.getState() != 4 && !wts.inserted) {
					removeWorkingTimeState(date);
					workingTimeState.updated = true;
					addWorkingTimeState(workingTimeState);
				} else if (workingTimeState.getState() == 4 && wts.inserted) {
					removeWorkingTimeState(date);
				} else if (workingTimeState.getState() != 4 && wts.inserted) {
					wts.setOverTime(workingTimeState.getOverTime()) ;
					wts.inserted = true;
				} else if (workingTimeState.getState() == 4 && !wts.inserted) {
					workingTimeState.deleted = true;
					removeWorkingTimeState(date);
					addWorkingTimeState(workingTimeState);
				}
				System.out
				.println("workingTimeState.getOverTime = " + workingTimeState.getOverTime());
				System.out
				.println("wts.getOverTime()            = " + wts.getOverTime());

				if (!(workingTimeState.getOverTime() == wts.getOverTime())) {
					wts = findWorkingTimeState(date);
					if (!wts.deleted &&workingTimeState.getState()!=4) {
						wts.setOverTime(workingTimeState.getOverTime());
						workingTimeState=wts;
						if(!wts.inserted){
							workingTimeState.updated=true;
						}
						workingTimeState.setDate(date);
						removeWorkingTimeState(date);
						addWorkingTimeState(workingTimeState);
					}
				}
			

			} else {
				// ga ada euy !!
				System.out
						.println("--         adaDiVectorGaYa ??  ga ada euy !!");
				workingTimeState.inserted = true;
				addWorkingTimeState(workingTimeState);
			}
		}
	}

	public void removeWorkingTimeState(Date date) {
		date = roundToDate(date);
		int index = vectorState.indexOf(date);
		if (index != -1) {
			vectorState.remove(index);
			vectorStateObject.remove(index);
		}
	}

	public boolean adaDiVectorGaYa(Date date) {
		date = roundToDate(date);
		if (vectorState != null) {
			for(int i=0;i<vectorState.size();i++){
				System.out
				.println("--         adaDiVectorGaYa ??  ga ada euy !!" + vectorState.get(i));
			}
			
			return vectorState.contains(date);
		}
		return false;
	}

	public WorkingTimeState findWorkingTimeState(Date date) {
		date = roundToDate(date);
		int index = vectorState.indexOf(date);
		if (index != -1) {
			return (WorkingTimeState) vectorStateObject.elementAt(index);
		}
		return null;
	}

	// ******************************************************************

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

	public void initEmployeeOfficeWorkingTime(Employee emp) {
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

			WorkingTimeState[] wts = logic.getEmployeeOfficeWorkingTime(
					m_sessionid, IDBConstants.MODUL_MASTER_DATA, emp.getIndex());
			vectorState.clear();
			vectorStateObject.clear();
			for (int i = 0; i < wts.length; i++) {
				addWorkingTimeState2(wts[i]);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		} finally {
			setDay(0);
			redrawDay(0);
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
				}
				if (ItHasState(date)) {
					System.out.println("ItHasState(date)");
					switch (getState(date)) {
					case 0:
						days[i].setBackground(WorkingTimeState.PresentNotLate);
						break;
					case 1:
						days[i].setBackground(WorkingTimeState.PresentLate);
						break;
					case 2:
						days[i].setBackground(WorkingTimeState.Absent);
						break;
					case 3:
						days[i].setBackground(WorkingTimeState.FieldVisit);
						break;
					case 5:
						//five for other meanwhile four for clear, because five was added at the last time
						days[i].setBackground(WorkingTimeState.Other);
						break;

					}
				}
			}
		}
	}

	// TAKEN FROM DATABASE
	private void addHoliday(Holiday holiday) {
		
		Date d = roundToDateForAddHoliday(holiday.getDate());
		
		// preventing of doubleing data
		if (!tempVector.contains(d)) {
			tempVector.add(d);
			objecTempVector.addElement(holiday);
		}
	}

	// TAKEN FROM HOLIDAYPANEL (BASED ON USER SELECTED)
//	private void addHoliday2(Holiday holiday) {
//		Date d = roundToDate(holiday.getDate());
//		System.out.println("roundToDate" + d
//				+ " -------------------------------- addHoliday 2 ");
//		// preventing of doubleing data, replace by the new holiday
//		if (tempVector.contains(d)) {
//			removeHolidayAtDate(d);
//		}
//		tempVector.add(d);
//		objecTempVector.addElement(holiday);
//	}

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

	public boolean ItHasState(Date date) {
		Date d = roundToDate(date);

		if (vectorState != null) {
			return vectorState.contains(d);
		} else {
			return false;
		}

	}

	public int getState(Date date) {
		date = roundToDate(date);
		int index = vectorState.indexOf(date);
		if (index != -1) {
			WorkingTimeState workingTimeState = (WorkingTimeState) vectorStateObject
					.elementAt(index);
			return workingTimeState.getState();
		}
		return -1;
	}

	public String getOvertimeValue(Date date) {
		date = roundToDate(date);
		int index = vectorState.indexOf(date);
		if (index != -1) {
			WorkingTimeState workingTimeState = (WorkingTimeState) vectorStateObject
					.elementAt(index);
			float valueFloat = workingTimeState.getOverTime();
			String valueString = new Float(valueFloat).toString();
			return valueString;
		}
		return null;
	}

	// this is for @workingTimeState based on user selected
	public void addWorkingTimeState(WorkingTimeState workingTimeState) {
		Date d = roundToDate(workingTimeState.getDate());

		// preventing of doubleing data,
		/*
		 * int index = vectorState.indexOf(d); if (index != -1) {
		 * vectorState.remove(index); vectorStateObject.remove(index); }
		 */
		vectorState.add(d);
		vectorStateObject.addElement(workingTimeState);

		System.out
				.println("------------------------------ 9999999999999988888888888    addWorkingTimeState");
	}

	// this is for @workingTimeState taken from database
	public void addWorkingTimeState2(WorkingTimeState workingTimeState) {
		Date d = roundToDateForAddHoliday(workingTimeState.getDate());
		vectorState.add(d);
		vectorStateObject.addElement(workingTimeState);

		System.out
				.println("------------------------------ 9999999999999988888888888    addWorkingTimeState");
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

	public WorkingTimeState[] getWorkingTimeState() {

		WorkingTimeState[] workingTimeState = new WorkingTimeState[vectorStateObject
				.size()];
		vectorStateObject.copyInto(workingTimeState);

		return workingTimeState;
	}

	public void clearVector() {
		objecTempVector.clear();
		tempVector.clear();
		vectorState.clear();
		vectorStateObject.clear();
	}

}