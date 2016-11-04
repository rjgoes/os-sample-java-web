import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Abstract class that all the real car types will inherit. Sets base methods that all the cars will need
 * @author Rajan Jassal
 */
public abstract class Car {

    //static fields that will be used to determine car types
    public static final String ELECTRIC_TYPE = "Electric Car";
    public static final String GAS_TYPE = "Gas Car";
    public static final String DIESEL_TYPE = "Diesel Car";

    protected String make;
    protected String model;
    protected int year;
    protected int odometerReading;
    protected int carID;
    protected String carType;
    protected ArrayList<MaintenanceTask> taskList = new ArrayList<MaintenanceTask>();

    /**
     * Constructor that will set all the attributes of the Car object
     * @param carID
     * @param make
     * @param model
     * @param year
     * @param odometerReading
     * @param carType
     */
    public Car(int carID, String make, String model, int year, int odometerReading, String carType) {
        this.carID = carID;
        this.make = make;
        this.model = model;
        this.year = year;

        this.odometerReading = odometerReading;
        this.carType = carType;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public String getCarType(){
        return this.carType;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getOdometerReading() {
        return odometerReading;
    }

    public void setOdometerReading(int odometerReading) {
        this.odometerReading = odometerReading;
    }


    public int getCarID() { return carID; }


    public void setCarID(int carID) { this.carID = carID; }

    public ArrayList<MaintenanceTask> getTaskList() {
        return taskList;
    }


    public void setTaskList(ArrayList<MaintenanceTask> taskList) {
        this.taskList = taskList;
    }

    /**
     * Adds a maintenance task to be associated with the car. The task's taskID must not
     * already be in the task list
     * @param task task to be added
     * @return boolean indicating if the task could be added
     */
    public boolean addTask(MaintenanceTask task){
        if(taskIDExists(task.taskID)){
            return false;
        }

        taskList.add(task);
        return true;
    }

    /**
     * Method will remove a task associated with the Car object
     * @param taskID taskID to remove
     * @return boolean indicating if the task was successfully removed
     */
    public boolean removeTask(int taskID){
        Iterator<MaintenanceTask> iterator = taskList.iterator();
        while(iterator.hasNext()){
            MaintenanceTask task = iterator.next();
            if(taskID ==  task.taskID){
                iterator.remove();
                return true;
            }
        }
        return  false;
    }


    /**
     * Lists all the tasks associated with the car in a string format
     * @return String containing all the car's tasks(and their information) separated by newlines
     */
    public String listTasks(){
        StringBuilder stringBuilder = new StringBuilder();
        for(MaintenanceTask task : taskList){
            stringBuilder.append(task.returnInformation());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Checks if a taskID is already associated with the car object
     * @param taskID taskID to check
     * @return boolean indicating if the taskID is already used by a task connected to the car object
     */
    public boolean taskIDExists(int taskID){
        Iterator<MaintenanceTask> iterator = taskList.iterator();
        while(iterator.hasNext()){
            MaintenanceTask currentTask = iterator.next();
            if(currentTask.taskID ==  taskID){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a task associated with the car that has a taskID specified. If there is no match null is returned
     * @param taskID taskID of the task to retrieve
     * @return MaintenanceTask of the task matching the taskID given. If no match null is reuturned
     */
    public MaintenanceTask getTask(int taskID){
        for(MaintenanceTask task : taskList){
            if(task.getTaskID() == taskID){
                return task;
            }
        }

        return null;
    }

    /**
     * Returns a JSON object containing all the car information and an array with all the tasks associated with the Car
     * @return JSON object containing all information and tasks associated with the car
     */
    public JsonObject toJSON(){
        JsonObject car = new JsonObject();
        car.addProperty("carID", carID);
        car.addProperty("type", carType);
        car.addProperty("make", make);
        car.addProperty("model", model);
        car.addProperty("year", year);
        car.addProperty("odometer", odometerReading);
        return car;
    }



}
