/**
 * A subclass of MaintenanceTask that represents an oil change task. This task can only be associated with gas and
 * diesel cars
 * @author Rajan Jassal
 */
public class OilChange extends MaintenanceTask {

    /**
     * Constructor that will set all task information. Type will automatically be set
     * @param taskID
     * @param startDate
     * @param endDate
     */
    public OilChange(int taskID, String startDate, String endDate) {
        super(taskID, startDate, endDate, OIL_CHANGE_TYPE);
    }

    /**
     * Returns a string representing the task information
     * @return String containing all the task information
     */
    @Override
    public String returnInformation() {
        return "Oil Change Information- ID:" + getTaskID() +
                ", Start Date:" + getStartDate() +
                ", End Date:" + getEndDate();
    }
}
