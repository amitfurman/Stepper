package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dto.DTORole;
import exceptions.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import systemengine.systemengine;
import utils.ServletUtils;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebServlet("/new-role")
public class addNewRoleServlet extends HttpServlet  {

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
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        String chosenItemsJson = jsonObject.get("chosenItems").getAsString();
        List<String> chosenItems = new Gson().fromJson(chosenItemsJson, new TypeToken<List<String>>(){}.getType());
        Set<String> flows =  new HashSet<>(chosenItems);
        systemengine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        systemEngine.addNewRole(new DTORole(name, description, flows, null));

    }
}
