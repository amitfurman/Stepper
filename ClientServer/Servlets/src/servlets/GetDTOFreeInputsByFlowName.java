package servlets;

import com.google.gson.Gson;
import dto.DTOFlowFreeInputs;
import dto.DTOInput;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetDTOFreeInputsByFlowName", urlPatterns = "/dto-free-inputs-by-flow-name")
public class GetDTOFreeInputsByFlowName extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String flowName = request.getParameter("flow_name");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            List<DTOInput> flowFreeInputs = system.getDTOFlowFreeInputsToSrevlet(flowName);
            String json = gson.toJson(flowFreeInputs);
            out.println(json);
            out.flush();
        }
    }
}
