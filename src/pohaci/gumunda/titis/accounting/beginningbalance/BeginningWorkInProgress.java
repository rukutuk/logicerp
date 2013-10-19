/**
 * 
 */
package pohaci.gumunda.titis.accounting.beginningbalance;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.project.cgui.ProjectData;

/**
 * @author dark-knight
 *
 */
public class BeginningWorkInProgress extends BeginningBalanceSheetDetail {
	protected ProjectData project;
	

	/**
	 * @return Returns the project.
	 */
	public ProjectData getProject() {
		return project;
	}


	/**
	 * @param project The project to set.
	 */
	public void setProject(ProjectData project) {
		this.project = project;
	}


	protected Unit unit() {
		return project.getUnit();
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetDetail#subsidiaryStr()
	 */
	public Object subsidiaryStr() {
		if (project==null) return null;
		return project.toString();
	}


	protected long indexSubsidiary() {
		if (project==null) return -1;
		return project.getIndex();
	}

}
