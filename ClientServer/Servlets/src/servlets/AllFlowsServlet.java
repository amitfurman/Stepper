package servlets;

import com.google.gson.Gson;
import dto.DTOAllFlowsNames;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import user.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "AllFlowsServlet", urlPatterns = "/all-flows")
public class AllFlowsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        System.out.println("AllFlowsServlet");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            DTOAllFlowsNames allFlows = system.getAllFlowsList();
            System.out.println(allFlows.getAllFlowsNames());
            System.out.println(allFlows.getAllFlowsNames().size());
            String json = gson.toJson(allFlows);
            System.out.println(json);
            out.println(json);
            out.flush();
        }
    }
}
