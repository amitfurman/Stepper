package servlets;

import constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.stream.Collectors;

import static constants.Constants.USERNAME;

@WebServlet(name = "UsersRolesServlet", urlPatterns = "/usersRoles")
public class UsersRolesServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
       /* UserManager userManager = ServletUtils.getUserManager(getServletContext());


        if(!userManager.isUserExists(usernameFromSession)) {
            //return error
        }
*/


    }
}
