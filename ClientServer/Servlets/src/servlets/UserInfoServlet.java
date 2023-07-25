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
import java.util.Set;

@WebServlet(name = "UserInfoServlet", urlPatterns = "/user-info")
public class UserInfoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getSystemEngine(getServletContext()).getUserMangerObject();
            DTOUserInfo userInfo = userManager.getUserInfo(userName);
            String json = gson.toJson(userInfo);
            out.println(json);
            out.flush();
        }
    }
}
