/**
 * A type of maintenance task that is associated with electric cars
 * @author Rajan Jassal
 */
public class ElectricBatteryReplacement extends MaintenanceTask{


    /**
     * Constructor that will set all task information. Type will automatically be set
     * @param taskID
     * @param startDate
     * @param endDate
     */
    public ElectricBatteryReplacement(int taskID, String startDate, String endDate) {
        super(taskID, startDate, endDate, MaintenanceTask.BATTERY_CHANGE_TYPE);
    }

    /**
     * Returns a string representing the task information
     * @return String containing all the task information
     */
    @Override
    public String returnInformation() {
        return "Electric Battery Replacement Information- ID:" + getTaskID() +
                ", Start Date:" + getStartDate() +
                ", End Date:" + getEndDate();
    }
}
