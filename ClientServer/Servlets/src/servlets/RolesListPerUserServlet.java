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
@WebServlet(name = "RolesListPerUserServlet", urlPatterns = "/roles-list-per-user")
public class RolesListPerUserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("user");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            DTORolesList rolesList = system.getDTORolesListPerUser(userName);
            Set<String> roles = rolesList.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.toSet());
            List<String> rolesListAsArray = new ArrayList<>(roles);
            String json = gson.toJson(rolesListAsArray);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

/*    @WebServlet(name = "RolesListPerUserServlet", urlPatterns = "/roles-list-per-user")
    public class RolesListPerUserServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String userName = request.getParameter("user");
            response.setContentType("application/json");

            Gson gson = new Gson();
            systemengine system = ServletUtils.getSystemEngine(getServletContext());
            DTORolesList rolesList = system.getDTORolesListPerUser(userName);
            Set<String> roles = rolesList.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.toSet());
            List<String> rolesListAsArray = new ArrayList<>(roles);
            String json = gson.toJson(rolesListAsArray);

            try (PrintWriter out = response.getWriter()) {
                out.print(json); // Write the JSON string to the response output stream
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("RolesListPerUserServlet: " + json);
        }
    }*/

