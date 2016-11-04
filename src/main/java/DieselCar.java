import java.util.ArrayList;
import java.util.Arrays;

/**
 * Diesel Car is a subclass of Car and represents a different type of car. This class can only contain
 * Maintenance Tasks that have the type of oil change or tire rotation
 * @author Rajan Jassal
 */
public class DieselCar extends Car {

    public DieselCar(int carID, String make, String model, int year, int odometerReading) {
        super(carID, make, model, year, odometerReading, DIESEL_TYPE);
    }
    //List used to determine valid task types that can be added to this car type. To add more valid types simply add to the list.
    public static ArrayList<String> validTaskTypes = new ArrayList<String>(Arrays.asList(MaintenanceTask.OIL_CHANGE_TYPE, MaintenanceTask.TIRE_ROTATION_TYPE));

    /**
     * Adds a task to be associated with the car. Only valid task types will be able to be added to the car
     * @param task task to be added
     * @return boolean indicating if the task was added to the car
     */
    @Override
    public boolean addTask(MaintenanceTask task) {
        if(!DieselCar.validTaskTypes.contains(task.getTaskType())){
            return false;
        }else if(taskIDExists(task.taskID)){
            return false;
        }else{
            taskList.add(task);
            return true;
        }
    }

    @Override
    public String toString() {
        return "Car Type:Diesel, ID:" + carID +
                ", Make:" + make +
                ", Model:" +model +
                ", year:" + year +
                ", odometerReading:" + odometerReading;
    }
}
