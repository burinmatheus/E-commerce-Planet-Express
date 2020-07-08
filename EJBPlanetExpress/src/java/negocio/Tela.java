package negocio;

import dao.DAOtela;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelEndereco;
import model.ModelTela;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Matheus M. Burin
 */
@Stateless
public class Tela {

    public String listaSegCat(HttpServletRequest request, HttpServletResponse response) {
        try {

            List<ModelTela> segmentos = new DAOtela().buscarSegmentos();

            JSONObject retorno = new JSONObject();

            JSONArray arraysegmentos = new JSONArray();
            JSONObject jseg;
            for (ModelTela segmento : segmentos) {

                jseg = new JSONObject();

                jseg.put("idS", segmento.getId_segmento());
                jseg.put("nomeS", segmento.getSegmento());
                jseg.put("imgS", segmento.getSegmentoImg());

                JSONArray arraycategoria = new JSONArray();
                JSONObject jcat;

                List<ModelTela> categorias = new DAOtela().buscarCategorias(segmento.getId_segmento());

                for (ModelTela categoria : categorias) {

                    jcat = new JSONObject();

                    jcat.put("idC", categoria.getId_categoria());
                    jcat.put("nomeC", categoria.getCategoria());

                    arraycategoria.put(jcat);
                }
                jseg.put("categorias", arraycategoria);

                arraysegmentos.put(jseg);
            }

            retorno.put("titulo", "segmentos");
            retorno.put("segmentos", arraysegmentos);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao Carregar Segmentos!\"}");
        }

    }
    
    public String listarEstados(HttpServletRequest request, HttpServletResponse response) {
        try {

            List<ModelEndereco> estados = new DAOtela().buscarEstados();

            JSONObject retorno = new JSONObject();

            JSONArray arrayRetorno = new JSONArray();
            JSONObject json;
            for (ModelEndereco estado : estados) {

                json = new JSONObject();

                json.put("id", estado.getEstado());
                json.put("nome", estado.getEstado_nome());
                json.put("sigla", estado.getEstado_sigla());

                arrayRetorno.put(json);
            }

            retorno.put("titulo", "estados");
            retorno.put("estados", arrayRetorno);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao listar estados!\"}");
        }

    }

}
