import java.util.ArrayList;
import java.util.Arrays;

/**
 * Electric Car is a subclass of Car and represents a different type of car. This class can only contain
 * Maintenance tasks that have the type of battery change or tire rotation
 * @author Rajan Jassal
 */
public class ElectricCar extends Car {

    public ElectricCar(int carID, String make, String model, int year, int odometerReading) {
        super(carID, make, model, year, odometerReading, ELECTRIC_TYPE);
    }

    //List used to determine valid task types that can be added to this car type. To add more valid types simply add to the list.
    public static ArrayList<String> validTaskTypes = new ArrayList<String>(Arrays.asList(MaintenanceTask.BATTERY_CHANGE_TYPE, MaintenanceTask.TIRE_ROTATION_TYPE));


    /**
     * Adds a task to be associated with the car. Only valid task types will be able to be added to the car
     * @param task task to be added
     * @return boolean indicating if the task was added to the car
     */
    @Override
    public boolean addTask(MaintenanceTask task){
        if(!ElectricCar.validTaskTypes.contains(task.getTaskType())){
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
        return "Car Type:Electric, ID:" + carID +
                ", Make:" + make +
                ", Model:" +model +
                ", year:" + year +
                ", odometerReading:" + odometerReading;
    }


}
