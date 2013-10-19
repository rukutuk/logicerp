/**
 * 
 */
package pohaci.gumunda.titis.accounting.entity;

/**
 * @author dark-knight
 *
 */
public class TransactionPanelMap {
	private String application;
	private Class panelClass;
	private String title;
	private int width;
	private int height;
	
	public TransactionPanelMap(String application, Class panelClass, String title, int width, int height) {
		this.application = application;
		this.panelClass = panelClass;
		this.title = title;
		this.width = width;
		this.height = height;
	}

	public String getApplication() {
		return application;
	}

	public int getHeight() {
		return height;
	}

	public Class getPanelClass() {
		return panelClass;
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}
}
