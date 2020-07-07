package controller;

import ejbconnection.EJBConnection;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.JWTAuth;

/**
 *
 * @author Matheus M. Burin
 */
public class Auth {

    protected void encaminha(HttpServletRequest request, HttpServletResponse response) {

    }

    public void auth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(JWTAuth.INFO_HEADER);
        String resposta;
        if (token == null || token.trim().isEmpty()) {
            resposta = "Token vazio!";
        } else {
            Jws<Claims> jws = JWTAuth.decode(token);
            Claims dados = jws.getBody();
            resposta = "ID: " + dados.getId()
                    + " Usuário: " + dados.getSubject()
                    + " Expira: " + dados.getExpiration();
        }

    }
}
