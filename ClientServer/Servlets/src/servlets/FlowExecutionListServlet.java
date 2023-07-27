package servlets;

import com.google.gson.Gson;
import dto.DTOFlowExeInfo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import user.UserManager;
import utils.ServletUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@WebServlet(name = "FlowExecutionListServlet", urlPatterns = "/get-flow-execution-list")
public class FlowExecutionListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            List<DTOFlowExeInfo> flowExeInfoList = system.getDTOFlowsExecutionList();
            String json = gson.toJson(flowExeInfoList);
            out.println(json);
            out.flush();
        }
    }
}
