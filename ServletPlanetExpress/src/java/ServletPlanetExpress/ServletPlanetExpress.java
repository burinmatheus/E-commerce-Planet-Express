
package ServletPlanetExpress;

import ejbconnection.EJBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Matheus M. Burin
 */
@WebServlet(name = "ServletPlanetExpress", urlPatterns = {"/ServletPlanetExpress"})
public class ServletPlanetExpress extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter saida = response.getWriter()) {

            try {
                saida.print(EJBConnection.execMethod("EJBAuthModule/Auth", "encaminha", request, response));
            } catch (Exception ex) {
                try (PrintWriter out = response.getWriter()) {
                    response.setStatus(500);
                    out.print("{\"erro\":\"Falha interna\"}");
                    Logger.getLogger(ServletPlanetExpress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
