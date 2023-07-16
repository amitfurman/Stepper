package servlets;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

@WebServlet("/new-role")
public class addNewRoleServlet extends HttpServlet  {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        Collection<Part> parts = request.getParts();

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            systemengine systemEngine = ServletUtils.getSystemEngine(getServletContext());
            systemEngine.addNewRole();
        }
    }
}
