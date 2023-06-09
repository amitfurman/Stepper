package servlets.fileupload;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import com.google.gson.Gson;
import exceptions.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;
import systemengine.systemengine;
import utils.ServletUtils;

import javax.xml.bind.JAXBException;

@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/xml");
        Collection<Part> parts = request.getParts();

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            fileContent.append(readFromInputStream(part.getInputStream()));
            systemengine systemEngine = ServletUtils.getSystemEngine(getServletContext());

            try {
                systemEngine.cratingFlowFromXml(part.getInputStream());
            }catch (DuplicateFlowsNames | UnExistsStep | OutputsWithSameName | MandatoryInputsIsntUserFriendly |
                    UnExistsData | SourceStepBeforeTargetStep | TheSameDD | UnExistsOutput |
                    FreeInputsWithSameNameAndDifferentType | InitialInputIsNotExist | FileNotFoundException |
                    JAXBException | UnExistsFlow | UnExistsDataInTargetFlow | FileNotExistsException | FileIsNotXmlTypeException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getOutputStream().print(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
/*    private void printFileContent(String content, PrintWriter out) {
        out.println("File content:");
        System.out.println(content);
        out.println(content);
    }*/
    /*    private void printPart(Part part, PrintWriter out) {
            StringBuilder sb = new StringBuilder();
            sb
                .append("Parameter Name: ").append(part.getName()).append("\n")
                .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
                .append("Size (of the file): ").append(part.getSize()).append("\n")
                .append("Part Headers:").append("\n");

            for (String header : part.getHeaderNames()) {
                sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
            }

            out.println(sb.toString());
        }*/



/*    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        Part part = request.getPart("xmlFile");
        String filePath = request.getParameter("filePath");
        systemengine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        try {
            systemEngine.cratingFlowFromXml(filePath,part.getInputStream());
        } catch (DuplicateFlowsNames | UnExistsStep | OutputsWithSameName | MandatoryInputsIsntUserFriendly |
                 UnExistsData | SourceStepBeforeTargetStep | TheSameDD | UnExistsOutput |
                 FreeInputsWithSameNameAndDifferentType | InitialInputIsNotExist | FileNotFoundException |
                 JAXBException | UnExistsFlow | UnExistsDataInTargetFlow | FileNotExistsException | FileIsNotXmlTypeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getOutputStream().print(e.getMessage());
        }
        response.setStatus(HttpServletResponse.SC_OK);

    }*/
