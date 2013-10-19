package pohaci.gumunda.titis.accounting.logic;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.application.NumberRounding;

public class MoneyTalk {

	private static MoneyTalk me = new MoneyTalk(); // ini namanya singleton

	public static final int INDONESIAN = 0;
	public static final int ENGLISH = 1;

	public static final String LANGUAGE_INDONESIAN = "Indonesian";
	public static final String LANGUAGE_ENGLISH = "English";

	public static final String[] LANGUAGES = {LANGUAGE_INDONESIAN, LANGUAGE_ENGLISH};

	public static final int CAMEL_CASE = 0;
	public static final int LOWER_CASE = 1;
	public static final int UPPER_CASE = 2;
	public static final int SENTENCE_CASE = 3;

	public static final int PRESERVE_NONE = 0;
	public static final int PRESERVE_CURRENCY = 1;
	public static final int PRESERVE_CENT = 2;
	public static final int PRESERVE_BOTH = 3;

	// ENGLISH
	private static final String[] decimalsEn =
	{"", "One", "Two", "Three", "Four",
		"Five", "Six", "Seven", "Eight", "Nine"};

	private static final String[] oneTensEn =
	{"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",
		"Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};

	private static final String[] twoTensEn =
	{"", "", "Twenty", "Thirty", "Forty",
		"Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

	private static final String[] hundredsEn =
	{"", "One Hundred", "Two Hundred", "Three Hundred", "Four Hundred",
		"Five Hundred", "Six Hundred", "Seven Hundred", "Eight Hundred", "Nine Hundred"};

	private static final String[] thousandsEn =
	{"", "Thousand", "Million", "Billion", "Trillion"};

	// INDONESIAN
	private static final String[] decimalsIn =
	{"", "Satu", "Dua", "Tiga", "Empat",
		"Lima", "Enam", "Tujuh", "Delapan", "Sembilan"};

	private static final String[] oneTensIn =
	{"Sepuluh", "Sebelas", "Dua Belas", "Tiga Belas", "Empat Belas",
		"Lima Belas", "Enam Belas", "Tujuh Belas", "Delapan Belas", "Sembilan Belas"};

	private static final String[] twoTensIn =
	{"", "", "Dua Puluh", "Tiga Puluh", "Empat Puluh",
		"Lima Puluh", "Enam Puluh", "Tujuh Puluh", "Delapan Puluh", "Sembilan Puluh"};

	private static final String[] hundredsIn =
	{"", "Seratus", "Dua Ratus", "Tiga Ratus", "Empat Ratus",
		"Lima Ratus", "Enam Ratus", "Tujuh Ratus", "Delapan Ratus", "Sembilan Ratus"};

	private static final String[] thousandsIn =
	{"", "Ribu", "Juta", "Milyar", "Triliun"};

	private String toHundreds(int language, int index){
		if(language==ENGLISH){
			return hundredsEn[index];
		}
		return hundredsIn[index];
	}

	private String toOneTens(int language, int index) {
		if(language==ENGLISH){
			return oneTensEn[index];
		}
		return oneTensIn[index];
	}

	private String toTwoTens(int language, int index) {
		if(language==ENGLISH){
			return twoTensEn[index];
		}
		return twoTensIn[index];
	}

	private String toDecimals(int language, int index){
		if(language==ENGLISH){
			return decimalsEn[index];
		}
		return decimalsIn[index];
	}

	private String toThousands(int language, int index){
		if(language==ENGLISH){
			return thousandsEn[index];
		}
		return thousandsIn[index];
	}

	private String toLetter(long value, int language){
		String letter = "";

		long number = value;
		int million = 0;

		while(number>0){
			String tempLetter = "";

			int units = (int) (number % 10);
			int tens = (int) ((number / 10) % 10);
			int hundreds = (int) ((number / 100) % 10);

			// hundreds
			if(hundreds!=0){
				if((hundreds==1)&&(tens+units==0)){
					tempLetter += (toHundreds(language, 1) + " ");
				}else {
					tempLetter += (toHundreds(language, hundreds) + " ");
				}
			}

			// tens
			if(tens!=0){
				if(tens==1){
					tempLetter += (toOneTens(language, units) + " ");
				}else{
					tempLetter += (toTwoTens(language, tens) + " ");
				}
			}

			// units
			if(units!=0){
				if(tens!=1){
					if((units==1)&&(million!=0)){
						tempLetter += (toDecimals(language, 1) + " ");
					}else{
						tempLetter += (toDecimals(language, units) + " ");
					}
				}
			}

			// thousands
			if(hundreds+tens+units!=0){
				tempLetter += (toThousands(language, million) + " ");
				// DO NOT REMOVE THESE LINES
				/*if((hundreds+tens==0)&&(units==1)&&((million==2)||(million==4))){
					tempLetter += (toThousands(language, million) + " ");
				}else{
					tempLetter += (toThousands(language, million) + " ");
				}*/
			}

			letter = tempLetter + letter;
			number = number / 1000;
			million++;
		}


		return letter;
	}

	private String convertStyle(int style, String said) {
		String ret = said;
		if(style==LOWER_CASE){
			return ret.toLowerCase();
		}else if(style==UPPER_CASE){
			return ret.toUpperCase();
		}else if(style==SENTENCE_CASE){
			ret = ret.toLowerCase();
			String firstChar = ret.substring(0,1);
			String firstCharUp = firstChar.toUpperCase();
			ret = ret.replaceFirst(firstChar, firstCharUp);
			return ret;
		}
		return ret;
	}

	public String sayInWords(double value, String currency, String cents, String strlanguage, int style, int preserve, boolean centInWords){
		String said = "";
		String decimals = "";
		String andWord = "";
		int language=INDONESIAN;
		if (strlanguage.trim().equals(LANGUAGE_INDONESIAN))
			language = INDONESIAN;
		else if (strlanguage.trim().equals(LANGUAGE_ENGLISH))
			language = ENGLISH;

		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		value = nr.round(value);

		long numberInt = (long)value;
		said = toLetter(numberInt, language);

		if(language==INDONESIAN){
			if(numberInt % 1000 == 1)
				said = said.replaceFirst("Satu Ribu", "Seribu");
		}
		said = said.trim();
		said = currency + ", " + said; // currency di depan

		int perseratusan = (int)((value - numberInt + 0.005) * 100);

		if(language==ENGLISH)
			andWord = "and";
		else
			andWord = "dan";

		if (perseratusan != 0) {
			if (centInWords) {
				decimals = toLetter(perseratusan, language);
				decimals = decimals.trim();
				decimals += (" " + andWord + " " + cents);

				said += (" " + decimals);
			}else{
				decimals = String.valueOf(perseratusan) + "/100";

				said += (" " + andWord + " " + decimals);
			}
		}

		String only = "";
		if (language == ENGLISH)
			only = "only";
		else
			only = "saja";
		said += " " + only;

		said = convertStyle(style, said);

		String currrencyStyle;
		String centStyle;
		if(style==SENTENCE_CASE){
			currrencyStyle = convertStyle(style, currency);
			centStyle = convertStyle(LOWER_CASE, cents);
		}else{
			currrencyStyle = convertStyle(style, currency);
			centStyle = convertStyle(style, cents);
		}
		if(preserve==PRESERVE_CURRENCY){
			said = said.replaceFirst(currrencyStyle, currency);
		}
		if(preserve==PRESERVE_CENT){
			said = said.replaceFirst(centStyle, cents);
		}
		if(preserve==PRESERVE_BOTH){
			said = said.replaceFirst(currrencyStyle, currency);
			said = said.replaceFirst(centStyle, cents);
		}

		return said;
	}

	public static String say(double value, String currency, String cents, String language, int style, int preserve, boolean centInWords){
		return me.sayInWords(value, currency, cents, language, style, preserve, centInWords);
	}
	
	public static String say(double value, Currency currency,  int style, int preserve, boolean centInWords){
		String curr=currency.getSay();
		String cents = currency.getCent();
		String language = currency.getLanguage();
		return me.sayInWords(value, curr, cents, language, style, preserve, centInWords);
	}
}
