package pohaci.gumunda.titis.accounting.helper;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

/**
 * Menghasilkan Reference No.
 * Gunakan method yang sesuai. Jangan salah.
 * Kesalahan yang disebabkan oleh kekeliruan penggunaan di luar tanggung jawab author :)
 * @author dark-knight
 *
 */
public class ReferenceNoGeneratorHelper {
	private Connection connection;
	private long sessionId = -1;
	private String modul;
	public static final String BANK_IN = "BI";
	public static final String BANK_OUT = "BO";
	public static final String CASH_IN = "CI";
	public static final String CASH_OUT = "CO";
	private static final String I_OWE_YOU = "OU";
	private static final String INVOICE = "INV";
	private static final String PURCHASE = "P";
	private static final String MEMORIAL_JOURNAL = "MJ";
	private static final String EXPENSE_SHEET = "ESP";
	private static final String CLOSING = "CT";
	private static final String BEGINNING_BALANCE = "BB";

	private String oldReference = "";

	public ReferenceNoGeneratorHelper(Connection connection, long sessionId, String modul, String oldReference) {
		this.connection = connection;
		this.sessionId = sessionId;
		this.modul = modul;
		this.oldReference = oldReference;
	}

	/**
	 * Menghasilkan Reference No untuk Void Transaction
	 * Nomor dihasilkan berdasarkan nomor yang lama
	 * @return
	 */
	public String createVoidReferenceNo(String standardJournalCode){
		return standardJournalCode + " " + oldReference;
	}

	/**
	 * Menghasilkan Reference No untuk fungsi-fungsi Payroll Verification
	 * @param standardJournalCode kode jurnal standar untuk payroll: yang PR01 s/d PR06 kali :)
	 * @param transactionDate tanggal transaksi dalam java.util.Date
	 * @return
	 */
	public String createPayrollReferenceNo(String standardJournalCode, Date transactionDate){
		String no = standardJournalCode;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		String period = simpleDateFormat.format(transactionDate);
		no += (" " + period);

		boolean found = findOldReference(no);

		if(!found)
			return this.oldReference;

		int newCountNo = getNewNo(no);

		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("000");
		newNo += df.format(newCountNo);

		return newNo;
	}

	/**
	 * Cek apakah old reference ada atau tidak?
	 * Jika true = ketemu maka harus diganti dengan yang baru
	 * True bisa berarti: ketemu, atau prefixnya beda
	 * @return
	 */
	private boolean findOldReference(String prefix) {
		// cek prefix-nya dulu
		if (this.oldReference.indexOf(prefix)==-1)
			return true;

		if (this.oldReference.equals(""))
			return true; // khusus untuk old reference yang belum diisi berarti harus buat baru...

		boolean found = findNo(this.oldReference);

		return found;
	}

	/**
	 * Pencarian no old reference
	 * @param string refNo
	 * @return
	 */
	private boolean findNo(String refNo) {
		boolean found = false;
		AccountingBusinessLogic logic = new AccountingBusinessLogic(connection);
		try {
			found = logic.findReferenceNo(sessionId, modul, refNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

	/**
	 * Menghasilkan Reference No untuk voucher/receipt dari bank in, bank out, atau cash in, cash out
	 * @param type bisa BANK_IN, BANK_OUT, CASH_IN, atau CASH_OUT
	 * @param code remember: untuk tipe BANK_IN dan BANK_OUT gunakan bank code; untuk tipe CASH_IN dan CASH_OUT gunakan unit code;
	 * jika ada kesalahan pemakaian bukan tanggung jawab aku :)
	 * @param transactionDate tanggal transaksi dalam java.util.Date
	 * @return
	 */
	public String createCashOrBankReferenceNo(String type, String code, Date transactionDate){
		String no = type + " " + code;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMM");
		String period = simpleDateFormat.format(transactionDate);
		no += (" " + period);

		if(!((type.equals(BANK_IN))||(type.equals(BANK_OUT))||(type.equals(CASH_IN))||(type.equals(CASH_OUT))))
			return null;

		boolean found = findOldReference(no);

		if(!found)
			return this.oldReference;

		int newCountNo = getNewNo(no);

		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("000");
		newNo += df.format(newCountNo);

		return newNo;
	}

	/**
	 * Menghasilkan Reference No untuk i owe you
	 * @param unitCode kode unit dalam String
	 * @param transactionDate tanggal transaksi dalam java.util.Date
	 * @return
	 */
	public String createIOweYouReferenceNo(String unitCode, Date transactionDate){
		String type = I_OWE_YOU;

		String no = type + " " + unitCode;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMM");
		String period = simpleDateFormat.format(transactionDate);
		no += (" " + period);

		boolean found = findOldReference(no);

		if(!found)
			return this.oldReference;

		int newCountNo = getNewNo(no);

		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("000");
		newNo += df.format(newCountNo);

		return newNo;
	}

	/**
	 * Menghasilkan Reference No untuk memorial journal
	 * @param transactionDate tanggal transaksi dalam java.util.Date
	 * @return
	 */
	public String createMemorialJournalReferenceNo(Date transactionDate){
		String type = MEMORIAL_JOURNAL;

		String no = type;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMM");
		String period = simpleDateFormat.format(transactionDate);
		no += (" " + period);

		boolean found = findOldReference(no);

		if(!found)
			return this.oldReference;

		int newCountNo = getNewNo(no);

		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("000");
		newNo += df.format(newCountNo);

		return newNo;
	}

	/**
	 * Menghasilkan Reference No untuk invoice
	 * @param unitCode kode unit dalam String
	 * @param transactionDate tanggal transaksi dalam java.util.Date
	 * @return
	 */
	public String createInvoiceReferenceNo(String unitCode, Date transactionDate){
		String type = INVOICE;

		String no = type + " " + unitCode;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(transactionDate);

		boolean found = findOldReference(no); // semoga tempatnya benar

		if(!found)
			return this.oldReference;

		int year = calendar.get(Calendar.YEAR);

		int newCountNo = getNewNoByYear(year, no, 5);

		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("00000");
		newNo += df.format(newCountNo);

		return newNo;
	}

	/**
	 * Menghasilkan Reference No untuk purchase receipt
	 * @param unitCode kode unit dalam String
	 * @param transactionDate tanggal transaksi dalam java.util.Date
	 * @return
	 */
	public String createPurchaseReceiptReferenceNo(String unitCode, Date transactionDate){
		String type = PURCHASE;

		String no = type + " " + unitCode;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(transactionDate);

		boolean found = findOldReference(no);

		if(!found)
			return this.oldReference;

		int year = calendar.get(Calendar.YEAR);

		int newCountNo = getNewNoByYear(year, no, 3);

		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("000");
		newNo += (" " + df.format(newCountNo));

		return newNo;
	}

	/**
	 * Menghasilkan Reference No untuk purchase receipt
	 * @param transactionDate tanggal transaksi dalam java.util.Date
	 * @return
	 */
	public String createExpenseSheetReferenceNo(Date transactionDate){
		String type = EXPENSE_SHEET;

		String no = type;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMM");
		String period = simpleDateFormat.format(transactionDate);
		no += (" " + period);

		boolean found = findOldReference(no);

		if(!found)
			return this.oldReference;

		int newCountNo = getNewNo(no);

		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("0000");
		newNo += df.format(newCountNo);

		return newNo;
	}

	/**
	 * Menghasilkan Reference No untuk beginning balance
	 * @param unitCode kode Unit dalam String
	 * @param transactionDate tanggal transaksi dalam java.util.Date
	 * @return
	 */
	public String createBeginningBalanceReferenceNo(String unitCode, Date transactionDate){
		String type = BEGINNING_BALANCE;

		String no = type + " " + unitCode;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String period = simpleDateFormat.format(transactionDate);
		no += (" " + period);

		boolean found = findOldReference(no);

		if(!found)
			return this.oldReference;

		int newCountNo = getNewNo(no);

		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("00");
		newNo += (" " + df.format(newCountNo));

		return newNo;
	}

	/**
	 * Menghasilkan Reference No untuk closing
	 * @param transactionDate tanggal transaksi dalam java.util.Date
	 * @return
	 */
	public String createClosingTransactionReferenceNo(Date transactionDate){
		String type = CLOSING;

		String no = type;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String period = simpleDateFormat.format(transactionDate);
		no += (" " + period);

		boolean found = findOldReference(no);

		if(!found)
			return this.oldReference;

		int newCountNo = getNewNo(no);

		// create referenceNo
		String newNo = no;
		DecimalFormat df = new DecimalFormat("00");
		newNo += df.format(newCountNo);

		return newNo;
	}

	/**
	 * Menghasilkan nomor referensi baru berdasarkan sebuah pola nomor yang menjadi kriteria pencariannya
	 * Method ini dapat digunakan untuk nomor-nomor yang memiliki pola tertentu di depannya, perulangannya
	 * sesuai dengan pola tersebut, dan nomor counter terdiri dari 3 digit
	 * @param no pola nomor yang menjadi kriteria pencarian
	 * @return
	 */
	private int getNewNo(String no) {
		//	get the lastest one
		String getNo = "";
		AccountingBusinessLogic logic = new AccountingBusinessLogic(connection);
		try {
			getNo = logic.getLastReferenceNo(sessionId, modul, no);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//	parsing
		int lastNo = 0;
		if (getNo != null) {
			if (getNo.length() > no.length()) {
				try {
					String nos = getNo.substring(getNo.length() - 3);
					lastNo = Integer.parseInt(nos);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// adding one
		lastNo++;
		return lastNo;
	}

	/**
	 * Menghasilkan nomor referensi baru berdasarkan sebuah pola nomor yang menjadi kriteria pencariannya
	 * Method ini dapat digunakan untuk nomor-nomor yang memiliki pola tertentu di depannya, perulangannya
	 * sesuai dengan pola tersebut, tahun, dan nomor counter terdiri dari n digit
	 * @param year tahun
	 * @param no pola nomor yang menjadi kriteria pencarian
	 * @param counterDigit jumlah digit
	 * @return
	 */
	private int getNewNoByYear(int year, String no, int counterDigit) {
		//	get the lastest one
		String getNo = "";
		AccountingBusinessLogic logic = new AccountingBusinessLogic(connection);
		try {
			getNo = logic.getLastReferenceNoByYear(sessionId, modul, year, no);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//	parsing
		int lastNo = 0;
		if (getNo != null) {
			if (getNo.length() > no.length()) {
				try {
					lastNo = Integer.parseInt(getNo.substring(getNo.length() - counterDigit));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// adding one
		lastNo++;
		return lastNo;
	}
}
