package pohaci.gumunda.titis.hrm.cgui;

import java.util.Hashtable;
import java.util.Vector;

public class PayrollColumnHelper {
	private Hashtable payrollIndexToColumnIndex = new Hashtable();
	private Hashtable payrollComponent = new Hashtable();
	private Hashtable cumulatedValue = new Hashtable();
	
	public PayrollColumnHelper() {
	}
	/**
	 * Persiapkan helper dengan array PayrollComponent 
	 * yang digunakan untuk pemetaan nomor kolom 
	 * @param comps array PayrollComponent untuk pemetaan
	 */
	public void prepareColumnHelper(PayrollComponent[] comps)
	{
		payrollIndexToColumnIndex.clear();
		int i;
		for (i=0; i< comps.length; i++){	
			Long payIn = new Long(comps[i].getIndex());
			payrollIndexToColumnIndex.put(new Long(comps[i].getIndex()),new Long(i));
			payrollComponent.put(payIn,comps[i]);
			cumulatedValue.put(payIn, new Double(0.0));
		}
	}
	public void prepareColumnHelper(TaxArt21Component[] comps)
	{
		payrollIndexToColumnIndex.clear();
		int i;
		for (i=0; i< comps.length; i++){	
			Long payIn = new Long(comps[i].getIndex());
			payrollIndexToColumnIndex.put(new Long(comps[i].getIndex()),new Long(i));
			payrollComponent.put(payIn,comps[i]);
			cumulatedValue.put(payIn,new Double(0.0));
		}
	}
	/**
	 * teuing atuh hehehe
	 */
	public double getValue(long index){
		Long key = new Long(index);
		Double data = (Double) cumulatedValue.get(key);
		return data.doubleValue();
	}
	public void putValue(long index,Double value){
		cumulatedValue.put(new Long(index),value);
	}
	
	public PayrollComponent getPayrollComponent(long index){
		Long key = new Long(index);
		PayrollComponent data = (PayrollComponent)payrollComponent.get(key);
		return data;
	}
	
	/**
	 * Mencari kolom no berapakah si payrollComponent dengan index tsb
	 * @param payrollComponentIndex
	 * @return nomor kolom (relatif terhadap kolom terkiri yg digunakan payrollComponent)
	 */
	public int getColumnIndex(long payrollComponentIndex)
	{
		Long key = new Long(payrollComponentIndex);
		Long data = (Long)payrollIndexToColumnIndex.get(key);
		//	System.out.println(data.intValue());
		if(data!=null)
			return data.intValue();
		else
			return -1;
	}
	/**
	 * adds data into vector, extends the vector if col is beyond vector size
	 * uses empty string for padding
	 * @param v The vector
	 * @param col Which column to put the data into
	 * @param obj Object to put into the vector
	 */
	public void addDataAtColumn(Vector v, int col, Object obj)
	{
		while (v.size() <= col)
			v.add("");
		v.setElementAt(obj,col);
	}

	public int getColumnCount() {
		return payrollComponent.size();		
	}
}
