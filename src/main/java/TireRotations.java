/**
 * A subclass of MaintenanceTask that represents an tire rotations task. This task can be associated with all car types
 * @author Rajan Jassal
 */
public class TireRotations extends MaintenanceTask {

    /**
     * Constructor that will set all task information. Type will automatically be set
     * @param taskID
     * @param startDate
     * @param endDate
     */
    public TireRotations(int taskID, String startDate, String endDate) {
        super(taskID, startDate, endDate, TIRE_ROTATION_TYPE);
    }

    /**
     * Returns a string representing the task information
     * @return String containing all the task information
     */
    @Override
    public String returnInformation() {
        return "Tire Rotation Information- ID:" + getTaskID() +
                ", Start Date:" + getStartDate() +
                ", End Date:" + getEndDate();
    }
}
