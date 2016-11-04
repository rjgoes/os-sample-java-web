/**
 * Implementation of the factory pattern used to create different types of maintenance
 * tasks(child classes of the abstract maintenance tasks class)
 * @author Rajan Jassal
 */
public class MaintenanceTaskFactory {
    /**
     * Creates a new task with the given fields. taskType will be used to determine what subclass of MaintenanceTask
     * is being created. If type is not valid null will be returned
     * @param taskID
     * @param startDate
     * @param endDate
     * @param taskType
     * @return a new Task object with the given fields. The task subclass will be determined by the taskType parameter
     */
    public MaintenanceTask getTask(int taskID, String startDate, String endDate, String taskType){
        if(taskType.equals(MaintenanceTask.OIL_CHANGE_TYPE)){
            return new OilChange(taskID,startDate,endDate);
        }else if(taskType.equals(MaintenanceTask.TIRE_ROTATION_TYPE)){
            return new TireRotations(taskID,startDate,endDate);
        }else if(taskType.equals(MaintenanceTask.BATTERY_CHANGE_TYPE)){
            return new ElectricBatteryReplacement(taskID,startDate,endDate);
        }

        return null;
    }
}
