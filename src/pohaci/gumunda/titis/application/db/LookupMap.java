package pohaci.gumunda.titis.application.db;

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

public class LookupMap {
	Class clazz;
	Hashtable map = new Hashtable();
	public LookupMap(Class clazz) {
		super();
		
		this.clazz = clazz;
	}
	boolean loaded;
	Object lookupById(Object id)
	{
		checkLoaded();
		return map.get(id);
	}
	private void checkLoaded() {
		if (!loaded)
		{
			GenericMapper m = MasterMap.obtainMapperFor(clazz);
			m.setActiveConn(MasterMap.obtainLookupConnection());
			Iterator iter = m.doSelectAll().iterator();
			while (iter.hasNext())
			{
				Object obj = iter.next();
				try {
					Object objId = PropertyUtils.getProperty(obj,
							m.mapping.thePK.propName);
					map.put(objId,obj);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("cannot obtain ID while loading lookup for " + clazz.getName());
				}
			}
			MasterMap.closeLookupConnection();
			loaded=true;
		}
	}
	
}
