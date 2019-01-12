package ru.javaops.masterjava.servlet;

import ru.javaops.masterjava.dto.UserDto;
import ru.javaops.masterjava.xml.util.DtoUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@WebServlet("/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    private static String saveDir;

    @Override
    public void init() {
        saveDir = getServletContext().getRealPath("uploaded-files");
        Path fileDir = Paths.get(saveDir);
        if (!Files.exists(fileDir)) {
            try {
                Files.createDirectory(fileDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/upload.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part file = req.getPart("file");
        String filePath = saveDir + File.separator + file.getSubmittedFileName();
        file.write(filePath);
        try {
            Set<UserDto> users = DtoUtils.getUsersFromXml(filePath);
            req.setAttribute("users", users);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("result.jsp").forward(req, resp);
    }
}
