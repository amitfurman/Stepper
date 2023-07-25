package servlets;

import com.google.gson.Gson;
import dto.DTOUserInfo;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "IsManagerServlet", urlPatterns = "/is-manager")
public class IsManagerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getSystemEngine(getServletContext()).getUserMangerObject();
            Boolean isManager = userManager.getIfUserIsManager(userName);
            String json = gson.toJson(isManager);
            out.println(json);
            out.flush();
        }
    }

}
