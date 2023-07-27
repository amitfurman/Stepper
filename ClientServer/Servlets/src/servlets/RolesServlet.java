package servlets;

import com.google.gson.Gson;
import dto.DTORolesList;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import user.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@WebServlet(name = "RolesServlet", urlPatterns = "/roles")
public class RolesServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            DTORolesList rolesList = system.getDTORolesList();
            System.out.println("innnnnnnnnnnnnnn ");
            rolesList.getRoles().stream().forEach(i-> System.out.println(i.getUsers()));
            String json = gson.toJson(rolesList);
            out.println(json);
            out.flush();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
