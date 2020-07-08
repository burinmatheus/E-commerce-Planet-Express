package negocio;

import dao.DAOfavoritos;
import dao.DAOprodutos;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelProdutos;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Matheus M. Burin
 */
@Stateless
public class Favoritos {

    public String adicionarFavoritos(HttpServletRequest request, HttpServletResponse response) {

        int id_user = 0;
        int id_produto = 0;

        if (request.getParameter("id_user") != null) {
            id_user = Integer.parseInt(request.getParameter("id_user"));
        }

        if (request.getParameter("id_produto") != null) {
            id_produto = Integer.parseInt(request.getParameter("id_produto"));
        }
        try {
            new DAOfavoritos().adicionarFavorito(id_user, id_produto);
            response.setStatus(201);
            return ("{\"alerta\":\"Produto adicionado aos Favoritos!\"}");
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Não foi possível adicionar o produto aos Favoritos!\"}");
        }

    }

    public String listarFavoritos(HttpServletRequest request, HttpServletResponse response) {
        try {

            List<ModelProdutos> produtos = new DAOfavoritos().buscarFavoritos(Integer.parseInt(request.getParameter("pg")), Integer.parseInt(request.getParameter("id_user")));

            JSONObject retorno = new JSONObject();

            JSONArray arrayRetorno = new JSONArray();
            JSONObject json;
            for (ModelProdutos produto : produtos) {

                json = new JSONObject();

                json.put("id", produto.getId());
                json.put("nome", produto.getNome());
                json.put("marca", produto.getMarca());
                json.put("valor", produto.getValor());
                json.put("desconto", produto.getDesconto());
                json.put("img", produto.getImg());

                arrayRetorno.put(json);
            }

            retorno.put("titulo", "Favoritos");
            retorno.put("produtos", arrayRetorno);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao listar!\"}");
        }

    }
    
    public String removerFavoritos(HttpServletRequest request, HttpServletResponse response) {

        int id_user = 0;
        int id_produto = 0;

        if (request.getParameter("id_user") != null) {
            id_user = Integer.parseInt(request.getParameter("id_user"));
        }

        if (request.getParameter("id_produto") != null) {
            id_produto = Integer.parseInt(request.getParameter("id_produto"));
        }
        try {
            new DAOfavoritos().removerFavorito(id_user, id_produto);
            response.setStatus(201);
            return ("{\"alerta\":\"Produto removido dos Favoritos!\"}");
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Não foi possível remover o produto dos Favoritos!\"}");
        }

    }
}
