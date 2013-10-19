/**
 * 
 */
package pohaci.gumunda.titis.appmanager.entity;

/**
 * @author irwan
 *
 */
public class RoleMap {
	private Role role;
	private String application;
	private String function;
	
	public static final String APP_ACCOUNTING = "Accounting";
	public static final String APP_HRM = "HRM";
	public static final String APP_PROJECT = "Project";
	
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	
}
