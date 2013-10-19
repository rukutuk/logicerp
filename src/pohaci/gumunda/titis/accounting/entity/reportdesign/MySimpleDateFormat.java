package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

/**
 * @author dark-knight
 *
 */
public class MySimpleDateFormat {

	private String code = "";
	private String format = "";
	private static String[] id = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
			"Juli", "Agustus", "September", "Oktober", "November", "Desember"};
	private static String[] en = {"January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December"};
	private Hashtable month = new Hashtable();
	
	public static final MySimpleDateFormat INDONESIAN = new MySimpleDateFormat("id", "d m y");
	public static final MySimpleDateFormat ENGLISH = new MySimpleDateFormat("en", "m d, y");
	/**
	 * 
	 */
	protected MySimpleDateFormat(String code, String format) {
		this.code = code;
		this.format = format;
		month.put("id", id);
		month.put("en", en);
	}

	public String format(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int d = cal.get(Calendar.DATE);
		int m = cal.get(Calendar.MONTH);
		int y = cal.get(Calendar.YEAR);
		
		String ret = this.format;
		String[] ms = (String[]) month.get(this.code);
		
		ret = ret.replaceFirst("d", String.valueOf(d));
		ret = ret.replaceFirst("m", ms[m]);
		ret = ret.replaceFirst("y", String.valueOf(y));
		
		return ret;
	}
}
