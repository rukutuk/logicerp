package pohaci.gumunda.titis.application.db;

public interface HydrationProvider {

	Object hydrateBySelect(Object pkValue, Class associatedClass);

	Object hydrateByRefLookup(Object pkValue, Class associatedClass);

}
