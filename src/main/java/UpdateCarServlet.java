import com.google.gson.JsonObject;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * UpdateCarServlet is the servlet that will handle updating all the car information in the system as requested
 * by the website
 * @author Rajan Jassa;
 */
public class UpdateCarServlet extends HttpServlet {
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
        //Getting all the request information
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        System.out.println("Received update car request");
        String ID = request.getParameter("ID");
        String newID = request.getParameter("newID");
        String requestType = request.getParameter("type");
        String make = request.getParameter("make");
        String model = request.getParameter("model");
        String year = request.getParameter("year");
        String odometer = request.getParameter("odometer");

        //Setting up all the variables to store the converted information
        JsonObject jsonResponse = new JsonObject();
        int IDData;
        int newIDData;
        int yearData;
        int odometerData;

        //Checking if the car type sent by the request is valid
        String type = null;
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
            jsonResponse.addProperty("reason", "invalid type");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Parsing the car ID to see if it is a valid number
        try {
            IDData = Integer.parseInt(ID);
            newIDData = Integer.parseInt(newID);
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


        //Updating the car
        if(!tracker.updateCarInfo(IDData,newIDData,type,make,model,yearData,odometerData)){
            //If the car update failed finding the reason
            jsonResponse.addProperty("status", "failed");

            //Case that the car ID requested is not in the system
            if(!tracker.carIDExists(IDData)){
                jsonResponse.addProperty("reason", "carID not in system");

            //Case that the car ID is changed and the new car ID is already in the system
            } else if(tracker.carIDExists(newIDData) && IDData != newIDData) {
                jsonResponse.addProperty("reason", "carID exists");
                out.print(jsonResponse.toString());

            //Case that the car type is changed and the swap is not valid
            }else if (!tracker.validTypeChange(IDData, type)){
                jsonResponse.addProperty("reason", "Invalid type change");
                out.print(jsonResponse.toString());
            } else{
                jsonResponse.addProperty("reason", "general");
                out.print(jsonResponse.toString());
            }
            out.close();
            return;
        }

        //Success response set
        jsonResponse.addProperty("status", "success");
        response.setStatus(200);
        out.print(jsonResponse.toString());

        out.close();
    }
}