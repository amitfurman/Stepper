package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.DTOFlowExeInfo;
import dto.DTOFlowExecution;
import dto.DTOFlowsDefinitionInRoles;
import dto.DTOStepsInFlow;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.DTOFlowsDefinitionInRolesDeserializer;
import systemengine.systemengine;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "GetDTOFlowExecutionServlet", urlPatterns = "/flow-execution-data")
public class GetDTOFlowExecutionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID uuid = UUID.fromString(request.getParameter("uuid"));
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            DTOFlowExeInfo dtoFlowExecution = system.getAllFlowExecutionData(uuid);
            Gson gson = new Gson();
            String json = gson.toJson(dtoFlowExecution);
            out.println(json);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
