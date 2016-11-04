import com.google.gson.JsonArray;
import com.sun.javaws.Main;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * AutomobileMaintenanceTracker is the class that represent a car
 * maintenance task tracking system. It will hold all the cars and
 * maintenance tasks in the system and allow for adding,updating and deleting
 * of cars and tasks. The class will also make sure all car and task IDs in the
 * system are unique
 *
 * @author Rajan Jassal
 */
public class AutomobileMaintenanceTracker {

    //Data structures that will hold car and task information
    private ArrayList<Car> carList = new ArrayList<Car>();

    //A set of task ID's are kept inorder to ensure no duplicates task ID in the system
    private HashSet<Integer> taskIDs = new HashSet<Integer>();

    //Factories used to create cars and maintenance task
    private CarFactory carFactory = new CarFactory();
    private MaintenanceTaskFactory maintenanceTaskFactory = new MaintenanceTaskFactory();


    /**
     * Allows you to add new cars to the system by supplying all the car fields. The fields will be checked for validity
     * in the method and a boolean will be returned indicating if the car could be added to the system. A Car factory
     * will be used to create the car in the method
     * @param carID ID of the new car to add
     * @param make make of the new car to add
     * @param model model of the new car to add
     * @param year year of the new car to add
     * @param odometerReading odometer of the new car to add
     * @param carType type of the new car to add. Must be a car type that is available as a static field in the car class
     * @return boolean indicating if the car was able to be added to the system
     */
    public boolean addCar(int carID, String make, String model, int year, int odometerReading, String carType) {
        //Check to see if the car ID is in the system of not
        if (carIDExists(carID)) {
            return false;
        }

        //Making the new car to add. Null will be returned if an invalid type is given
        Car newCar = carFactory.getCar(carID, make, model, year, odometerReading, carType);

        //If the the car was not a valid car the method will return false
        if (newCar == null) {
            return false;
        }

        //Adding the car if it is valid
        carList.add(newCar);
        return true;
    }

    /**
     * Will add a given car to the system after checking it is valid to add to the system
     * @param car car object to add
     * @return boolean indicating if the car could be added to the system
     */
    public boolean addCar(Car car) {
        //Check to see if the car ID is in the system of not
        if (carIDExists(car.getCarID())) {
            return false;
        }

        //Checking to see if all taskIDs associated with the car are unique
        for (MaintenanceTask task : car.getTaskList()) {
            if (taskIDs.contains(task.getTaskID())) {
                return false;
            }
        }

        carList.add(car);

        //Adding all the tasks IDs associated with the car to the system
        for (MaintenanceTask task : car.getTaskList()) {
            taskIDs.add(task.getTaskID());
        }

        return true;
    }

    /**
     * Allows you to add new tasks to the system by supplying all the task fields and the carID associated with
     * the task. The fields will be checked for validity in the method and a boolean will be returned indicating if the
     * task could be added to the system. A Task factory will be used to create the task in the method
     * @param carID carID to add the task to
     * @param taskID taskID for the new task
     * @param startDate start date for the new task
     * @param endDate end date for the new task
     * @param taskType type of the new task
     * @return boolean indicating if the task could be added to the system
     */
    public boolean addMaintenanceTask(int carID, int taskID, String startDate, String endDate, String taskType) {
        //Checking that the car to add the task to is already in the system
        //and checking the new task ID is unique to the system
        if ((!carIDExists(carID)) || taskIDs.contains(taskID)) {
            return false;
        }

        //Creating the new task. Null will be returned if an invalid task type is given
        MaintenanceTask newTask = maintenanceTaskFactory.getTask(taskID, startDate, endDate, taskType);
        Car currentCar = retrieveCar(carID);

        //Failing if the either the car of the task are not correct
        if (newTask == null || currentCar == null) {
            return false;
        }

        //Checking if the car could be added to the system
        if(currentCar.addTask(newTask)){
            taskIDs.add(newTask.getTaskID());
            return true;
        }

         return false;
    }

    /**
     * Allows you to add new tasks to the system by supplying the task and the carID associated with
     * the task. The fields will be checked for validity in the method and a boolean will be returned indicating if the
     * task could be added to the system.
     * @param carID carID to add the task to
     * @param task task to add to system
     * @return boolean indicating if the task could be added to the system
     */
    public boolean addMaintenanceTask(int carID, MaintenanceTask task){
        //Checking that the car to add the task to is already in the system
        //and checking the new task ID is unique to the system
        if ((!carIDExists(carID)) || taskIDs.contains(task.getTaskID())) {
            return false;
        }

        Car currentCar = retrieveCar(carID);

        //Failing if the either the car of the task are not correct
        if (currentCar == null) {
            return false;
        }

        //Checking if the car could be added to the system
        if(currentCar.addTask(task)){
            taskIDs.add(task.getTaskID());
            return true;
        }

        return false;
    }

    /**
     * Will remove a car and all of its maintenance tasks from
     * the tracking system
     * @param carID carID to remove from the system
     * @return boolean indicating if car was removed
     */
    public boolean removeCar(int carID) {
        if (!carIDExists(carID)) {
            return false;
        }

        Car carToRemove = retrieveCar(carID);

        //Removing the task IDs associated with the car from the system
        for(MaintenanceTask task: carToRemove.getTaskList()){
            taskIDs.remove(task.getTaskID());
        }
        carList.remove(carToRemove);
        return true;
    }


    /**
     * Will remove a maintenance task from the tracker system
     * @param carID car ID of the task to remove
     * @param taskID task ID of the task to remove
     * @return boolean indicating if car was removed
     */
    public boolean removeMaintenanceTask(int carID, int taskID) {
        if ((!((carIDExists(carID)) && taskIDs.contains(taskID)))) {
            return false;
        }
        taskIDs.remove(taskID);
        return retrieveCar(carID).removeTask(taskID);

    }

    /**
     * Method that will list the cars in the system, along with their information, in string form
     * @return a string containing all information of all the cars in the system separated  by newlines
     */
    public String listCars() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Car car : carList) {
            stringBuilder.append(car.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Method that will list the tasks in the system, along with their information, in string form
     * @return a string containing all information of all the tasks in the system separated  by newlines
     */
    public String listTasks() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Car car : carList) {
            stringBuilder.append(car.listTasks());
        }
        return stringBuilder.toString();
    }

    /**
     * Method that will list the cars in the system along with their tasks in string form
     * @return a string containing all information of all the cars and tasks in the system separated by newlines
     */
    public String listCarsAndTasks() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Car car : carList) {
            stringBuilder.append(car.toString());
            stringBuilder.append("\n");
            stringBuilder.append("Maintenance Tasks:");
            stringBuilder.append("\n");
            stringBuilder.append(car.listTasks());

        }

        return stringBuilder.toString();
    }

    /**
     * Return a JSON array of all the cars in the system
     * @return JsonArray of all the cars in the tracking system
     */
    public JsonArray JSONListCars() {
        JsonArray carArray = new JsonArray();
        for (Car car : carList) {
            carArray.add(car.toJSON());
        }
        return carArray;
    }

    /**
     * Return a JSON array of all the maintenance tasks associated with a car
     * @param carID ID of the car to retrieve maintenance tasks from
     * @return JsonArray of all the tasks assoicated with that car
     */
    public JsonArray JSONListCarTasks(int carID){
        if (!carIDExists(carID)) {
            return null;
        }
        JsonArray taskArray = new JsonArray();
        Car car = retrieveCar(carID);

        for(MaintenanceTask task: car.getTaskList()){
            taskArray.add(task.toJSON());
        }
        return taskArray;
    }

    /**
     * Will update a car based on the new parameters provided.
     * If the type of car is changed then a new car object of the appropriate
     * type will be created to replace the old car
     *
     * @param carID car ID of the car to update
     * @param newCarID new car ID to update
     * @param type type value to update
     * @param make make value to update
     * @param model model value to update
     * @param year year value to update
     * @param odometerReading odometer reading value to update
     * @return boolean representing if the car was successfully updated
     */
    public boolean updateCarInfo(int carID, int newCarID, String type, String make, String model, int year, int odometerReading) {
        //Checking if the car exists in the tracker
        if (!carIDExists(carID)) {
            return false;
        }

        //If there is a new ID, checking to see if it is already in the system
        if (carID != newCarID && carIDExists(newCarID)) {
            return false;
        }


        Car car = retrieveCar(carID);

        //Creating a new car object if the type has changed
        if (car.getCarType() != type) {
            //Checking if the type swap is valid
            if(validTypeChange(carID,type)){
                ArrayList<MaintenanceTask> taskList = car.getTaskList();
                Car newCar = carFactory.getCar(newCarID,make, model, year, odometerReading, type);

                if(newCar==null)
                    return false;

                //Adding the new car and removing the old car
                newCar.setTaskList(taskList);
                carList.remove(car);
                carList.add(newCar);
                return  true;
            }else{
                return false;
            }
        }
        //Updating fields if type has not switched
        car.setCarID(newCarID);
        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setOdometerReading(odometerReading);
        return true;
    }

    /**
     * Will update a task based on the new parameters provided.
     * If the type of task is changed then a new task object of the appropriate
     * type will be created to replace the old task if the task type swap is valid
     * @param carID car ID of the task to update
     * @param taskID task ID of the task to update
     * @param newTaskID new taskID value
     * @param type new type value
     * @param startDate new start date value
     * @param endDate new end date value
     * @return
     */
    public boolean updateTaskInfo(int carID, int taskID, int newTaskID,String type, String startDate, String endDate) {
        //Checking if the car exists in the tracker
        if (!carIDExists(carID)) {
            return false;
        }

        Car car = retrieveCar(carID);

        //Checking if the task exists in the car
        if(!car.taskIDExists(taskID)) {
            return false;
        }

        MaintenanceTask task = car.getTask(taskID);

        //If there is a new taskID value to update, making sure it is not already in the system
        if(task.getTaskID() != newTaskID && taskIDs.contains(newTaskID)){
            return false;
        }


        //If there is a type change, checking if the type swap is valid
        if(task.getTaskType() != type ){

            if(validTaskTypeChange(carID,taskID,type)){
                //Creating a new task of the correct type to add to the system
                MaintenanceTask newTask = maintenanceTaskFactory.getTask(newTaskID,startDate,endDate,type);
                if(newTask == null)
                    return false;

                //removing the old values
                car.removeTask(taskID);
                taskIDs.remove(taskID);

                car.addTask(newTask);
                taskIDs.add(newTaskID);
                return true;
            }else{
                return false;
            }
        }

        //Updating the task if no type swap
        task.setTaskID(newTaskID);
        taskIDs.remove(taskID);
        taskIDs.add(newTaskID);
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        return true;
    }


    /**
     * Method to retrieve a car from the system based on id
     * @param carID car ID of car to retrieve
     * @return the Car object that was retrieved. Null will be returned if there is no car ID match
     */
    public Car retrieveCar(int carID) {
        for (Car car : carList) {
            if (car.getCarID() == carID) {
                return car;
            }
        }
        return null;
    }

    /**
     * Retrieve a task based on the carID and taskID. Null is returned if the task does not exist
     * @param carID car ID of the task to retrieve
     * @param taskID task ID of the task to retrieve
     * @return the task to reutrn
     */
    public MaintenanceTask retrieveTask(int carID, int taskID){
        if (!carIDExists(carID) || !taskIDExists(taskID)) {
            return null;
        }

        return retrieveCar(carID).getTask(taskID);

    }

    /**
     * Method to check if a car ID exists in the system
     * @param carID car ID to check
     * @return boolean indicating if the car ID is already in the system
     */
    public boolean carIDExists(int carID) {
        for (Car car : carList) {
            if (car.getCarID() == carID) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Method to check if a task ID exists in the system
     * @param taskID task ID to check
     * @return boolean indicating if the task ID is already in the system
     */
    public boolean taskIDExists(int taskID){
        if(taskIDs.contains(taskID)){
            return true;
        }
        return false;
    }

    /**
     * Will return the total number of cars in the system
     * @return the number of cars in the system
     */
    public int getCarCount() {
        return carList.size();
    }

    /**
     * Will return the total number of tasks in the system
     * @return the number of tasks in the system
     */
    public int getTaskCount(){return taskIDs.size();}

    /**
     * This method is used to check if a car with the ID carID in
     * the maintenance tracker is valid to change types to the newType
     * specified. If the car contains maintenance tasks that the newType
     * is not verified to contain the swap is not valid.
     * @param carID ID of the car to check
     * @param newType String of the new car type
     * @return boolean If the change in type is valid
     */
    public boolean validTypeChange(int carID, String newType) {

        //Checking if the car is in the system
        if (!carIDExists(carID)) {
            return false;
        }

        Car car = retrieveCar(carID);

        //Check if there is no need for a type change
        if (car.getCarType().equals(newType)) {
            return true;
        }

        //Check if the new type is valid based on the maintenance tasks in the car
        switch (newType) {
            case Car.ELECTRIC_TYPE:
                for (MaintenanceTask task : car.getTaskList()) {
                    if (!ElectricCar.validTaskTypes.contains(task.getTaskType())) {
                        return false;
                    }
                }
                return true;
            case Car.DIESEL_TYPE:
                for (MaintenanceTask task : car.getTaskList()) {
                    if (!DieselCar.validTaskTypes.contains(task.getTaskType())) {
                        return false;
                    }
                }
                return true;
            case Car.GAS_TYPE:
                for (MaintenanceTask task : car.getTaskList()) {
                    if (!DieselCar.validTaskTypes.contains(task.getTaskType())) {
                        return false;
                    }
                }
                return true;

        }
        return false;
    }

    /**
     * This method is used to check if a task with the ID taskID associated with
     * a car with the ID crID in the maintenance tracker is valid to change types to the newType
     * specified. If the car containing the task is not valid to contain the new task false will be returned
     * @param carID ID of the car containing the task
     * @param taskID ID of the task to check
     * @param newType String of the new task type
     * @return boolean If the change in type is valid
     */
    public boolean validTaskTypeChange(int carID,int taskID, String newType) {
        //Checking if the car is in the system
        if (!carIDExists(carID)) {
            return false;
        }

        Car car = retrieveCar(carID);

        //Checking if the task is in the car
        if(!car.taskIDExists(taskID)){
            return false;
        }

        MaintenanceTask task = car.getTask(taskID);
        //Check if there is no need for a type change
        if (task.getTaskType().equals(newType)) {
            return true;
        }

        //Check if the new type is valid based on car type
        switch (newType) {
            case MaintenanceTask.TIRE_ROTATION_TYPE:
                return true;
            case MaintenanceTask.BATTERY_CHANGE_TYPE:
                if(car.getCarType().equals(Car.ELECTRIC_TYPE)){
                    return true;
                }else{
                    return false;
                }
            case MaintenanceTask.OIL_CHANGE_TYPE:
                if(car.getCarType().equals(Car.GAS_TYPE) || car.getCarType().equals(Car.DIESEL_TYPE)){
                    return true;
                }else{
                    return false;
                }

        }
        return false;
    }
}