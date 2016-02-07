package image.web;

import image.processing.ConvexHull;
import image.processing.ImageProcessor;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rakib on 23-Jan-16.
 */

@MultipartConfig
public class HomeServlet extends HttpServlet {
    /**
     * Processes the posted data
     *
     * @param request  The request sent to server
     * @param response The response sent to client
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("imageFile");
        if (filePart.getSize() > 0) {
            InputStream fileContent = filePart.getInputStream();
            Image img = ImageIO.read(fileContent);
            request.getSession().setAttribute("image", img);
        }
        request.getSession().setAttribute("color", request.getParameter("color"));
        request.getSession().setAttribute("tolerance", request.getParameter("tolerance"));

        Image sessionImage = (Image) request.getSession().getAttribute("image");
        String sessionColor = (String) request.getSession().getAttribute("color");
        String sessionTolerance = (String) request.getSession().getAttribute("tolerance");

        if (sessionImage == null) {
            request.setAttribute("errorMessage", "Please upload an image");
            request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
        }

        Color color = null;
        try {
            color = Color.decode(sessionColor);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Please give a valid hex for color");
            request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
        }

        ImageProcessor ipInput = new ImageProcessor(sessionImage);
        ImageProcessor ipOutput = new ImageProcessor(sessionImage);

        java.util.List<Point> lst = ipOutput.getPixelsByColor(color,
                Float.parseFloat(sessionTolerance));
        //java.util.List<Point> lst = ipOutput.getEdges(0.007f);

        List<Point> convexHull = ConvexHull.generateConvexHull((ArrayList<Point>) lst);
        ipOutput.drawLine(convexHull, new Color(0, 255, 25));

        request.setAttribute("inputImage", ipInput.getSrcData());
        request.setAttribute("outputImage", ipOutput.getSrcData());

        request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
    }

    /**
     * Initiates the page
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute("color", "#000000");
        request.getSession().setAttribute("tolerance", 5f);
        request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
    }
}
