package servlets;

import com.google.gson.Gson;
import dto.DTOContinuationMapping;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "ContinuationServlet", urlPatterns = "/get-continuation")
public class ContinuationServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String flowName = request.getParameter("flowName");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            LinkedList<DTOContinuationMapping> continuationMappings = system.getDTOAllContinuationMappingsWithSameSourceFlow(flowName);
            String json = gson.toJson(continuationMappings);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
