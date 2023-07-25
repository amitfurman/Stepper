package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import systemengine.systemengine;
import utils.ServletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

@WebServlet(name = "UpdateUserServlet", urlPatterns = "/update-user")
public class UpdateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String userName = request.getParameter("userName");
        Boolean isManager = Boolean.parseBoolean(request.getParameter("isManager"));

        StringBuilder payloadBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null) {
            payloadBuilder.append(line);
        }
        String jsonPayload = payloadBuilder.toString();
        Set<String> checkedItems = new Gson().fromJson(jsonPayload, new TypeToken<Set<String>>() {}.getType());
        System.out.println("checkedItems in servlet: " + checkedItems);
        systemengine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        systemEngine.updateUser(userName, checkedItems, isManager);

    }
}
