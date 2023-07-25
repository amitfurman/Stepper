package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.DTOFlowExecution;
import dto.DTOFlowID;
import dto.DTOFreeInputsFromUser;
import dto.DTORole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import utils.ServletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet("/activate-flow")
public class activateFlowServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        StringBuilder payloadBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            payloadBuilder.append(line);
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(payloadBuilder.toString(), JsonObject.class);
        String flowName = jsonObject.get("flowName").getAsString();
        DTOFreeInputsFromUser freeInputs = gson.fromJson(jsonObject.get("freeInputs"), DTOFreeInputsFromUser.class);

        System.out.println("flowName: " + flowName);
        System.out.println("freeInputs: ");
        freeInputs.getFreeInputMap().forEach((key, value) -> System.out.println(value));

        systemengine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        //DTOFlowExecution flowExecution = systemEngine.activateFlowByName(flowName, freeInputs);
        DTOFlowID flowExecution = systemEngine.activateFlowByName(flowName, freeInputs);
        String json = gson.toJson(flowExecution);
        response.getWriter().println(json);
        response.getWriter().flush();/**/
    }

}
