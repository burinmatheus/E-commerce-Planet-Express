
package negocio;

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
public class Produtos {
    public String listarProdutosTodos(HttpServletRequest request, HttpServletResponse response) throws SQLException {
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
            
            arrayRetorno.put(json);
        }

        retorno.put("produtos", arrayRetorno);

        return retorno.toString();

    }
    
    public String listarProdutosCategoria(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        List<ModelProdutos> produtos = new DAOprodutos().listarProdutosCategoria( Integer.parseInt(request.getParameter("categoria")),Integer.parseInt(request.getParameter("pg")));

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
            json.put("categoria", produto.getCategoria());
            
            arrayRetorno.put(json);
        }

        retorno.put("produtos", arrayRetorno);

        return retorno.toString();

    }
    
    public String listarProdutosSegmentos(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        List<ModelProdutos> produtos = new DAOprodutos().listarProdutosSegmentos( Integer.parseInt(request.getParameter("segmento")),Integer.parseInt(request.getParameter("pg")));

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
            json.put("segmento", produto.getSegmento());
            
            arrayRetorno.put(json);
        }

        retorno.put("produtos", arrayRetorno);

        return retorno.toString();

    }
    
    public String listarProdutosPesquisa(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        List<ModelProdutos> produtos = new DAOprodutos().listarProdutosPesquisa( Integer.parseInt(request.getParameter("pg")),request.getParameter("pesquisa"));

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

        retorno.put("produtos", arrayRetorno);

        return retorno.toString();

    }
}
