package servlets;

import com.google.gson.Gson;
import dto.DTOContinuationMapping;
import dto.DTOInput;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
@WebServlet(name = "GetContinuationValuesServlet", urlPatterns = "/get-continuation-values")
public class GetContinuationValuesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sourceFlow = request.getParameter("sourceFlow");
        String targetFlow = request.getParameter("targetFlow");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            LinkedList<DTOInput> continuationValues = system.getDTOValuesListFromContinuationMap(sourceFlow, targetFlow);
            String json = gson.toJson(continuationValues);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
