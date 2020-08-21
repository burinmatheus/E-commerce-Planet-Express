package controller;

import ejbconnection.EJBConnection;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.io.IOException;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import utils.JWTAuth;

/**
 *
 * @author Matheus M. Burin
 */
@Stateless
public class Auth {

    public String encaminha(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String classeMetodo = request.getParameter("caminho");
        String funcao = request.getParameter("funcao");
        String resultado = "{\"erro\":\"Erro ao buscar detalhes do produto!\"}";

        String authresponse;

        switch (classeMetodo) {

            case "EJBPlanetExpress/Pedidos": //BLOQUEADO
                authresponse = auth(request, response);
                if (!"A".equals(authresponse)) {
                    resultado = (String) EJBConnection.execMethod(request.getParameter("caminho"), request.getParameter("funcao"), request, response, authresponse);
                } else {
                    response.setStatus(400);
                }
                break;

            case "EJBPlanetExpress/Favoritos": //BLOQUEADO
                authresponse = auth(request, response);
                if (!"A".equals(authresponse)) {
                    resultado = (String) EJBConnection.execMethod(request.getParameter("caminho"), request.getParameter("funcao"), request, response, authresponse);
                } else {
                    response.setStatus(400);
                }
                break;

            case "EJBPlanetExpress/User": //PARCIAL
                switch (funcao) {

                    case "modificarUsuario": //BLOQUEADO
                        authresponse = auth(request, response);
                        if (!"A".equals(authresponse)) {
                            resultado = (String) EJBConnection.execMethod(request.getParameter("caminho"), request.getParameter("funcao"), request, response, authresponse);
                        } else {
                            response.setStatus(400);
                        }
                        break;

                    case "modificarSenha": //BLOQUEADO
                        authresponse = auth(request, response);
                        if (!"A".equals(authresponse)) {
                            resultado = (String) EJBConnection.execMethod(request.getParameter("caminho"), request.getParameter("funcao"), request, response, authresponse);
                        } else {
                            response.setStatus(400);
                        }
                        break;

                    case "modificaEndereco": //BLOQUEADO
                        authresponse = auth(request, response);
                        if (!"A".equals(authresponse)) {
                            resultado = (String) EJBConnection.execMethod(request.getParameter("caminho"), request.getParameter("funcao"), request, response, authresponse);
                        } else {
                            response.setStatus(400);
                        }
                        break;

                    case "dadosUsuario": //BLOQUEADO
                        authresponse = auth(request, response);
                        if (!"A".equals(authresponse)) {
                            resultado = (String) EJBConnection.execMethod(request.getParameter("caminho"), request.getParameter("funcao"), request, response, authresponse);
                        } else {
                            response.setStatus(400);
                        }
                        break;
                        
                    case "dadosEndereco": //BLOQUEADO
                        authresponse = auth(request, response);
                        if (!"A".equals(authresponse)) {
                            resultado = (String) EJBConnection.execMethod(request.getParameter("caminho"), request.getParameter("funcao"), request, response, authresponse);
                        } else {
                            response.setStatus(400);
                        }
                        break;
                        
                    default:
                        resultado = (String) EJBConnection.execMethod(request.getParameter("caminho"), request.getParameter("funcao"), request, response);
                        break;

                }
                break;

            default:
                resultado = (String) EJBConnection.execMethod(request.getParameter("caminho"), request.getParameter("funcao"), request, response);
                break;
        }

        return resultado;
    }

    public String auth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(JWTAuth.INFO_HEADER);
        String resposta;
        if (token == null || token.trim().isEmpty()) {
            resposta = "A";
        } else {
            Jws<Claims> jws = JWTAuth.decode(token);
            Claims dados = jws.getBody();

            JSONObject json = new JSONObject();

            json.put("user_id", dados.getId());
            json.put("user_name", dados.getSubject());
            json.put("expira", dados.getExpiration());

            resposta = json.toString();
        }
        return resposta;
    }
}
