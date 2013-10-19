package com.gumunda.titis.appication.additional;


import javax.swing.JPanel;



public class MainframeReferenceno extends JPanel{

	public MainframeReferenceno(String app) {		
		Referencenoupdateframe frame = new Referencenoupdateframe();
		frame.setSize(860, 600);
		frame.setVisible(true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MainframeReferenceno("");		
	}
	

}
