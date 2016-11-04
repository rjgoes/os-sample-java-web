import com.google.gson.JsonObject;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * UpdateTaskServlet is the servlet that will handle updating all the task information in the system as requested
 * by the website
 * @author Rajan Jassa;
 */
public class UpdateTaskServlet extends HttpServlet {
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
        System.out.println("Received update task request");
        String carID = request.getParameter("carID");
        String ID = request.getParameter("ID");
        String newID = request.getParameter("newID");
        String requestType = request.getParameter("type");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        //Setting up all the variables to store the converted information
        JsonObject jsonResponse = new JsonObject();
        int carIDData;
        int IDData;
        int newIDData;
        String type = null;

        //Checking if the car type sent by the request is valid
        switch (requestType){
            case "Electric Battery Replacement":
                type = MaintenanceTask.BATTERY_CHANGE_TYPE;
                break;
            case "Tire Rotations":
                type = MaintenanceTask.TIRE_ROTATION_TYPE;
                break;
            case "Oil Change":
                type = MaintenanceTask.OIL_CHANGE_TYPE;
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

        //Parsing the task ID to see if it is a valid number
        try {
            IDData = Integer.parseInt(ID);
        }catch(NumberFormatException e){
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "taskID NaN");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Parsing the new task ID to see if it is a valid number
        try {
            newIDData = Integer.parseInt(newID);
        }catch(NumberFormatException e){
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "new taskID NaN");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Parsing the car ID to see if it is a valid number
        try {
            carIDData = Integer.parseInt(carID);
        }catch(NumberFormatException e){
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "carID NaN");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Updating the task information
        if(!tracker.updateTaskInfo(carIDData,IDData,newIDData,type,startDate,endDate)){
            //If the task update failed finding the reason
            jsonResponse.addProperty("status", "failed");

            //Case that the task ID requested is not in the system
            if(!tracker.taskIDExists(IDData)){
                jsonResponse.addProperty("reason", "taskID not in system");

            //Case that the task ID is changed and the new task ID is already in the system
            } else if(tracker.taskIDExists(newIDData) && IDData != newIDData) {
                jsonResponse.addProperty("reason", "new taskID exists");
                out.print(jsonResponse.toString());
            //Case that the task type is changed and the swap is not valid
            }else if (!tracker.validTaskTypeChange(carIDData,IDData, type)){
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