import com.google.gson.JsonObject;

/**
 * Abstract class representing Maintenance tasks. All of the different maintenance task types will inherit from this
 * class
 * @author Rajan Jassal
 */
public abstract class   MaintenanceTask {

    //static fields that will be used to determine task types
    public static final String OIL_CHANGE_TYPE = "Oil Change";
    public static final String TIRE_ROTATION_TYPE = "Tire Rotations";
    public static final String BATTERY_CHANGE_TYPE = "Electric Battery Replacement";

    protected int taskID;
    protected String startDate;
    protected String endDate;
    protected String taskType;

    /**
     * Constructor that will be used to set all of the task fields
     * @param taskID ID of the task
     * @param startDate start date of the task
     * @param endDate end date of the task
     * @param taskType type of the task. Will be set by child class constructors and can not be changed
     */
    public MaintenanceTask(int taskID, String startDate, String endDate, String taskType) {
        this.taskID = taskID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskType = taskType;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTaskType() {
        return taskType;
    }

    public abstract String returnInformation();

    /**
     * Turns the task object into a JSON object
     * @return JsonObject
     */
    public JsonObject toJSON(){
        JsonObject task = new JsonObject();
        task.addProperty("taskID", taskID);
        task.addProperty("type", taskType);
        task.addProperty("startDate", startDate);
        task.addProperty("endDate", endDate);
        return task;
    }
}
