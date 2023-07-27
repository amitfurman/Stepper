package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.DTOFlowDefinitionInRoles;
import dto.DTOFlowsDefinitionInRoles;
import dto.DTORolesList;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.DTOFlowsDefinitionInRolesDeserializer;
import systemengine.systemengine;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "FlowsInRolesServlet", urlPatterns = "/flows-in-roles")
public class FlowsInRolesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("in servlet");
        String roles = request.getParameter("roles_list");
        System.out.println(roles + " in servlet");
        Boolean isManager = Boolean.parseBoolean(request.getParameter("is_manager"));
        System.out.println("is manager in servlet: " + isManager);
        response.setContentType("application/json");
       // if (roles!="") {
            List<String> rolesList = Arrays.asList(roles.split(","));
            try (PrintWriter out = response.getWriter()) {

                systemengine system = ServletUtils.getSystemEngine(getServletContext());
                DTOFlowsDefinitionInRoles dtoFlowDefinitionInRoles = system.getDtoFlowsDefinition(isManager,rolesList);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(DTOFlowsDefinitionInRoles.class, new DTOFlowsDefinitionInRolesDeserializer())
                        .setPrettyPrinting()
                        .create();

                String json = gson.toJson(dtoFlowDefinitionInRoles);
                out.println(json);
                out.flush();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
       // }
    }
}
