/**
 * Implementation of the factory pattern used to create different types of Cars(child classes of the abstract Car class)
 * @author Rajan Jassal
 */
public class CarFactory {
    /**
     * Creates a new car with the given field. carType will be used to determine what subclass of Car is being created.
     * If type is not valid null will be returned
     * @param carID
     * @param make
     * @param model
     * @param year
     * @param odometerReading
     * @param carType
     * @return a new Car object with the given fields. The car subclass will be determined by the carType parameter
     */
    public Car getCar(int carID, String make, String model, int year, int odometerReading, String carType){
        if(carType.equals(Car.DIESEL_TYPE)){
            return new DieselCar(carID, make, model, year, odometerReading);
        }else if(carType.equals(Car.ELECTRIC_TYPE)){
            return new ElectricCar(carID, make, model, year, odometerReading);
        }else if(carType.equals(Car.GAS_TYPE)){
            return new GasCar(carID, make, model, year, odometerReading);
        }
        return null;
    }
}
