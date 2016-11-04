import com.google.gson.JsonObject;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Servlet used to add a task to the tracker system
 * @author Rajan Jassal
 */
public class AddTaskServlet extends HttpServlet {
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
        //Getting all the car and task information from the website request
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        System.out.println("Received task car request");
        String carID = request.getParameter("carID");
        String taskID = request.getParameter("taskID");
        String requestType = request.getParameter("type");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        //Setting up all the variables to store the converted information
        JsonObject jsonResponse = new JsonObject();
        int carIDData;
        int taskIDData;
        String type = null;

        //Checking if the task type sent by the request is valid
        switch (requestType) {
            case "Tire Rotations":
                type = MaintenanceTask.TIRE_ROTATION_TYPE;
                break;
            case "Oil Change":
                type = MaintenanceTask.OIL_CHANGE_TYPE;
                break;
            case "Electric Battery Replacement":
                type = MaintenanceTask.BATTERY_CHANGE_TYPE;
                break;
        }


        //case of an invalid type in the request
        if (type == null) {
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "type");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Parsing the car ID to see if it is a valid number
        try {
            carIDData = Integer.parseInt(carID);
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "carID NaN");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Parsing the task ID to see if it is a valid number
        try {
            taskIDData = Integer.parseInt(taskID);
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "taskID NaN");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Adding the task to the system
        if (!tracker.addMaintenanceTask(carIDData, taskIDData, startDate, endDate, type)) {

            //In the case the task could not be added to the system, finding
            //the reason for the failure and sending it to the website
            jsonResponse.addProperty("status", "failed");
            if (!tracker.carIDExists(carIDData)) {
                jsonResponse.addProperty("reason", "carID does not exist");
                out.print(jsonResponse.toString());
            } else if (tracker.taskIDExists(taskIDData)) {
                jsonResponse.addProperty("reason", "taskID exists");
                out.print(jsonResponse.toString());
            } else {
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