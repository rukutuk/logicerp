package com.gumunda.titis.appication.additional;

import javax.swing.JPanel;


public class MainframeDepertment extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainframeDepertment(String app) {
		setSize(750, 600);
		Departementframe frame = new Departementframe();
		frame.adjustPosition();		
		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MainframeDepertment("");
	}
}
