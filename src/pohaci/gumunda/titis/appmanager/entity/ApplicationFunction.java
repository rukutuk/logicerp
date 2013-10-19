/**
 * 
 */
package pohaci.gumunda.titis.appmanager.entity;

/**
 * @author irwan
 *
 */
public class ApplicationFunction {
	private long autoindex;
	private String application;
	private String functionName;
	private String path;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public long getAutoindex() {
		return autoindex;
	}
	public void setAutoindex(long autoindex) {
		this.autoindex = autoindex;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String toString() {
		return functionName;
	}
}
