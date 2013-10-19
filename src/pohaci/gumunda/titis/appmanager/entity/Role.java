/**
 * 
 */
package pohaci.gumunda.titis.appmanager.entity;

/**
 * @author irwan
 *
 */
public class Role {
	private long autoindex;
	private String roleName = null;
	private String description = null;
	
	public Role(long autoindex, String roleName, String description) {
		this.autoindex = autoindex;
		this.roleName = roleName;
		this.description = description;
	}
	
	public Role() {
		
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public String getDescription() {
		return description;
	}
	public String getRoleName() {
		return roleName;
	}

	public String toString() {
		if(roleName==null){
			return "";
		}
		return roleName;
	}

	public long getAutoindex() {
		return autoindex;
	}

	public void setAutoindex(long autoindex) {
		this.autoindex = autoindex;
	}
	
	
}
