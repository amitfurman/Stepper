package servlets;

import com.google.gson.Gson;
import dto.DTOFlowExeInfo;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(name = "GetDTOFlowExecutionByNameServlet", urlPatterns = "/get-flow-execution-by-name")
public class GetDTOFlowExecutionByNameServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String flowName =request.getParameter("flowName");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            DTOFlowExeInfo dtoFlowExecution = system.getAllFlowExecutionDataNyName(flowName);
            Gson gson = new Gson();
            String json = gson.toJson(dtoFlowExecution);
            out.println(json);
            out.flush();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
