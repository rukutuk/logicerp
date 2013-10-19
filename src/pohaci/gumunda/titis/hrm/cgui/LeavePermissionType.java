package pohaci.gumunda.titis.hrm.cgui;

public class LeavePermissionType extends AbsenceAttribute {

	private short m_type = -1;
	public static final short CODE = 0;
	public static final short DESCRIPTION = 1;
	public static final short CODE_DESCRIPTION = 2;
	
	
	public LeavePermissionType(short type, long index, String code, String description) {
		super(index, code, description);
		this.m_type = type;
	}

	public LeavePermissionType(long index, LeavePermissionType type) {
	    super(index, type.getCode(), type.getDescription());
	    this.m_type = type.getTypeAsShort();
	}

	public LeavePermissionType(LeavePermissionType type, short status) {
	    super(type.getIndex(), type.getCode(), type.getDescription());
	    m_status = status;
	    this.m_type = type.getTypeAsShort();
	}
	
	public String toString() {
	    if(m_status == CODE)
	    	return m_code;
	    else if(m_status == DESCRIPTION)
	    	return m_description;
	    else if(m_status == CODE_DESCRIPTION)
	    	return m_code + " " + m_description;
	    return super.toString();
	}
	
	public String getType(){
		if(m_type==0)
			return "Leave";
		else if(m_type==1)
			return "Permission";
		return "";
	}
	
	public short getTypeAsShort(){
		return m_type;
	}
}
