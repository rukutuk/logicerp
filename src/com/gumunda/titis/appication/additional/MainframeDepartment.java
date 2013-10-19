package com.gumunda.titis.appication.additional;

import javax.swing.JPanel;


public class MainframeDepartment extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainframeDepartment(String app) {
		setSize(750, 600);
		Departementframe frame = new Departementframe();
		frame.adjustPosition();		
		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "$, name is Tamana and Tamana is a good girl.";
		String strreplace = "$";
		String result = str.replaceFirst("$", strreplace);
		System.out.println(result);
	}
}
