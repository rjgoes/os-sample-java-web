import com.google.gson.JsonObject;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Servlet used to add a car to the tracker system
 * @author Rajan Jassal
 */
public class AddCarServlet extends HttpServlet {
    private AutomobileMaintenanceTracker tracker = AutomobileMaintenanceTrackerSingleton.getInstance();

    /**
     * Handles the request from the web front-end
     * @param request the request send from the website
     * @param response the response variable that will be used to communicate with the website
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Getting all the car information from the website request
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        System.out.println("Received add car request");
        String ID = request.getParameter("ID");
        String requestType = request.getParameter("type");
        String make = request.getParameter("make");
        String model = request.getParameter("model");
        String year = request.getParameter("year");
        String odometer = request.getParameter("odometer");

        //Setting up all the variables to store the converted information
        JsonObject jsonResponse = new JsonObject();
        int IDData;
        int yearData;
        int odometerData;
        String type = null;

        //Checking if the car type sent by the request is valid
        switch (requestType){
            case "Gas Car":
                type = Car.GAS_TYPE;
                break;
            case "Electric Car":
                type = Car.ELECTRIC_TYPE;
                break;
            case "Diesel Car":
                type = Car.DIESEL_TYPE;
                break;
        }

        //case of an invalid type in the request
        if(type == null){
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "type");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Parsing the car ID to see if it is a valid number
        try {
            IDData = Integer.parseInt(ID);
        }catch(NumberFormatException e){
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "carID NaN");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Parsing the year to see if it is a valid number
        try {
            yearData =Integer.parseInt(year);
        }catch(NumberFormatException e){
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "year NaN");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Parsing the odometer reading to see if it is a valid number
        try {
            odometerData = Integer.parseInt(odometer);
        }catch(NumberFormatException e){
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "odometer NaN");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Adding the car to the system
        if(!tracker.addCar(IDData,make,model,yearData,odometerData,type)){

            //In the case the car could not be added to the system, finding
            //the reason for the failure and sending it to the website
            jsonResponse.addProperty("status", "failed");
            if(tracker.carIDExists(IDData)) {
                jsonResponse.addProperty("reason", "carID exists");
                out.print(jsonResponse.toString());
            }else{
                jsonResponse.addProperty("reason", "general");
                out.print(jsonResponse.toString());
            }
            out.close();
            return;
        }

        //On success sending back the proper response
        jsonResponse.addProperty("status", "success");
        response.setStatus(200);
        out.print(jsonResponse.toString());

        out.close();
    }
}