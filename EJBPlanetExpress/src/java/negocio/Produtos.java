package negocio;

import dao.DAOprodutos;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Produtos {
    
        public String listarProduto(HttpServletRequest request, HttpServletResponse response) {
        try {

            ModelProdutos produto = new DAOprodutos().listarProduto(Integer.parseInt(request.getParameter("id")));
            
            JSONObject retorno = new JSONObject();

            JSONArray arrayRetorno = new JSONArray();
            JSONObject json;

                json = new JSONObject();

                json.put("id", produto.getId());
                json.put("nome", produto.getNome());
                json.put("marca", produto.getMarca());
                json.put("valor", produto.getValor());
                json.put("desconto", produto.getDesconto());
                json.put("descricao", produto.getDescricao());

                arrayRetorno.put(json);

            retorno.put("titulo", "Todos");
            retorno.put("produtos", arrayRetorno);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao listar!\"}");
        }

    }

    public String listarProdutosTodos(HttpServletRequest request, HttpServletResponse response) {
        try {

            List<ModelProdutos> produtos = new DAOprodutos().listarProdutosTodos(Integer.parseInt(request.getParameter("pg")));

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

            retorno.put("titulo", "Todos");
            retorno.put("produtos", arrayRetorno);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao listar!\"}");
        }

    }

    public String listarProdutosCategoria(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<ModelProdutos> produtos = new DAOprodutos().listarProdutosCategoria(Integer.parseInt(request.getParameter("categoria")), Integer.parseInt(request.getParameter("pg")));

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

                arrayRetorno.put(json);
            }

            retorno.put("titulo", produtos.get(0).getCategoria());
            retorno.put("produtos", arrayRetorno);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao listar!\"}");
        }

    }

    public String listarProdutosSegmentos(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<ModelProdutos> produtos = new DAOprodutos().listarProdutosSegmentos(Integer.parseInt(request.getParameter("segmento")), Integer.parseInt(request.getParameter("pg")));

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

                arrayRetorno.put(json);
            }

            retorno.put("titulo", produtos.get(0).getSegmento());
            retorno.put("produtos", arrayRetorno);
            response.setStatus(202);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(200);
            return ("{\"erro\":\"Erro ao listar!\"}");
        }
    }

    public String listarProdutosPesquisa(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<ModelProdutos> produtos = new DAOprodutos().listarProdutosPesquisa(Integer.parseInt(request.getParameter("pg")), request.getParameter("pesquisa"));

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

                arrayRetorno.put(json);
            }

            retorno.put("titulo", "Resultado da pesquisa por <strong>" + request.getParameter("pesquisa") + "<strong> :");
            retorno.put("produtos", arrayRetorno);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao listar!\"}");
        }
    }
}
