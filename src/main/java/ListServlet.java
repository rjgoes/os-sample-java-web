import com.google.gson.JsonObject;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * ListServlet is the servlet that will handle returning all the car and task information to the website in JSON
 * form. On a request from the front-end the servlet will return only the requested information
 * @author Rajan Jassa;
 */
public class ListServlet extends HttpServlet {
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
        System.out.println("Received list request");
        String requestType = request.getParameter("requestType");
        JsonObject jsonResponse = new JsonObject();

        //Handling different request types
        switch (requestType){
            //Request asks for all the cars in the system
            case "listCars":
                jsonResponse.addProperty("status", "success");
                jsonResponse.add("cars", tracker.JSONListCars());
                out.print(jsonResponse.toString());
                out.close();
                return;

            //Request asks for a specific car in the system
            case "getCar":
                Car car;
                int requestID = Integer.parseInt(request.getParameter("carID"));
                if(tracker.carIDExists(requestID)){
                    car = tracker.retrieveCar(requestID);
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.add("car", car.toJSON());
                    out.print(jsonResponse.toString());
                    out.close();
                    return;
                }
            //Request asks for all the tasks in a specific car
            case "getCarTasks":
                int requestCarID = Integer.parseInt(request.getParameter("carID"));
                if(tracker.carIDExists(requestCarID)){
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.add("tasks", tracker.JSONListCarTasks(requestCarID));
                    out.print(jsonResponse.toString());
                    out.close();
                    return;
                }
        }

        //Handling the case an operation could not be performed
        jsonResponse.addProperty("status", "failed");
        jsonResponse.addProperty("reason", "general");
        out.print(jsonResponse.toString());
        out.close();
    }
}
