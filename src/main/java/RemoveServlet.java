import com.google.gson.JsonObject;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * RemoveServlet is the servlet that will handle removing all the car and task information in the system as requested
 * by the website
 * @author Rajan Jassa;
 */
public class RemoveServlet extends HttpServlet {
    private AutomobileMaintenanceTracker tracker = AutomobileMaintenanceTrackerSingleton.getInstance();

    /**
     * Handles the request from the web front-end
     * @param request the request send from the website
     * @param response the response variable that will be used to communicate with the website
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Getting all the request information
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        System.out.println("Received remove request");
        String requestType = request.getParameter("requestType");
        String carID = request.getParameter("carID");

        JsonObject jsonResponse = new JsonObject();
        int IDData;

        //Parsing request car ID
        try {
            IDData = Integer.parseInt(carID);
        }catch(NumberFormatException e){
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("reason", "carID NaN");
            out.print(jsonResponse.toString());
            out.close();
            return;
        }

        //Handling different request types
        switch (requestType) {
            //Request asks to remove a specific car
            case "removeCar":
                if(tracker.removeCar(IDData)){
                    jsonResponse.addProperty("status", "success");
                    out.print(jsonResponse.toString());
                    out.close();
                    return;
                }else{
                    jsonResponse.addProperty("status", "failure");
                    jsonResponse.addProperty("reason", "general");
                    out.print(jsonResponse.toString());
                    out.close();
                    return;
                }

            //Request asks to remove a specific task
            case "removeTask":
                String taskID = request.getParameter("taskID");
                int taskIDData;
                try {
                    taskIDData = Integer.parseInt(taskID);
                }catch(NumberFormatException e){
                    jsonResponse.addProperty("status", "failed");
                    jsonResponse.addProperty("reason", "taskID NaN");
                    out.print(jsonResponse.toString());
                    out.close();
                    return;
                }

                if (tracker.removeMaintenanceTask(IDData,taskIDData)) {
                    jsonResponse.addProperty("status", "success");
                    out.print(jsonResponse.toString());
                    out.close();
                    return;
                }else{
                    jsonResponse.addProperty("status", "failure");
                    jsonResponse.addProperty("reason", "general");
                    out.print(jsonResponse.toString());
                    out.close();
                    return;
                }
            //failure if either if request type is not recognized
            default:
                jsonResponse.addProperty("status", "failure");
                jsonResponse.addProperty("reason", "general");
                out.print(jsonResponse.toString());
                out.close();
                return;
        }


    }
}