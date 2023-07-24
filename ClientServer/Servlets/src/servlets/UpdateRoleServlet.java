package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.DTORole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import utils.ServletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet("/update-role")
public class UpdateRoleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        StringBuilder payloadBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            payloadBuilder.append(line);
        }

        String jsonPayload = payloadBuilder.toString();
        JsonObject jsonObject = new Gson().fromJson(jsonPayload, JsonObject.class);

        Gson gson = new Gson();
        DTORole currentRole = gson.fromJson(jsonPayload, DTORole.class);

        String name = currentRole.getName();
        String description =  currentRole.getDescription();
        Set<String> chosenFlows =  currentRole.getFlowsInRole();
        List<String> chosenUsers =  currentRole.getUsers();
        systemengine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        systemEngine.updateFlowsInRole(new DTORole(name, description, chosenFlows, chosenUsers));

    }


}
