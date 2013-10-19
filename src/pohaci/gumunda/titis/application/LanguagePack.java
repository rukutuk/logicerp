/**
 * 
 */
package pohaci.gumunda.titis.application;

/**
 * @author dark-knight
 *
 */
public class LanguagePack {
	private String langId;

	public static final LanguagePack INDONESIAN = new LanguagePack("Indonesian");
	public static final LanguagePack ENGLISH = new LanguagePack("English");
	
	protected LanguagePack(String langId) {
		super();
		this.langId = langId;
	}

	public String toString() {
		return this.langId;
	}
	
	public static LanguagePack[] pack(){
		return new LanguagePack[]{INDONESIAN, ENGLISH};
	}
}
