package servlets;

import com.google.gson.Gson;
import dto.DTOFlowFreeInputs;
import dto.DTORolesList;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "FreeInputsByFlowName", urlPatterns = "/free-inputs-by-flow-name")
public class FreeInputsByFlowName extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String flowName = request.getParameter("flowName");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            List<DTOFlowFreeInputs> flowFreeInputs = system.getDTOFlowFreeInputs(flowName);
            String json = gson.toJson(flowFreeInputs);
            out.println(json);
            out.flush();
        }
    }
}
