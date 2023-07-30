package servlets;

import com.google.gson.Gson;
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

@WebServlet(name = "UsersToEachRoleServlet", urlPatterns = "/users-ro-each-role")
public class UsersToEachRoleServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roleName = request.getParameter("roleName");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            Set<String> usersSet = system.getUsersOfRoles(roleName);
            List<String> usersList = new ArrayList<>(usersSet);
            String json = gson.toJson(usersList);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
