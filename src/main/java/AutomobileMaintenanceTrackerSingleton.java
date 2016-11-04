/**
 * Implementation of the singleton pattern for the AutomobileMaintenanceTacker class. Used to keep
 * the same instance of the tracker object across servlets
 * @author Rajan Jassal
 */
public class AutomobileMaintenanceTrackerSingleton {
    private static AutomobileMaintenanceTracker instance = null;

    private AutomobileMaintenanceTrackerSingleton(){

    }

    /**
     * Returns the instance of the automobile maintenance tracker object if one exists or creates one if a instance
     * does not exist
     * @return the current instance of the automobile maintenance tracker
     */
    public static AutomobileMaintenanceTracker getInstance(){
        if(instance == null){
            instance = new AutomobileMaintenanceTracker();
        }
        return instance;
    }
}
