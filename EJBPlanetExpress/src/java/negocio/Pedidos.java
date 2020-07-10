/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dao.DAOpedidos;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelPedidos;
import model.ModelProdutos;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Matheus M. Burin
 */
@Stateless
public class Pedidos {

    public String diminuiQTDE(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id_usuario = 0;
            int produto = 0;
            boolean existe = false;

            if (request.getParameter("id_produto") != null) {
                produto = Integer.parseInt(request.getParameter("id_produto"));
            }

            if (request.getParameter("id_user") != null) {
                id_usuario = Integer.parseInt(request.getParameter("id_user"));
            }

            int id_pedido = new DAOpedidos().buscarPedidoA(id_usuario);

            ModelPedidos pd = new ModelPedidos();
            pd.setId_pedido(id_pedido);

            ModelProdutos mp = new ModelProdutos();
            mp.setId(produto);
            pd.setProduto(mp);

            try {
                if (new DAOpedidos().verificaProdutos(pd)) {
                    existe = true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (existe) {
                ModelPedidos pe = new ModelPedidos();
                pe.setId_pedido(id_pedido);

                ModelProdutos mps = new ModelProdutos();
                mps.setId(produto);

                pe.setProduto(mps);
                new DAOpedidos().diminuiQTDE(pe);

                atualizaValor(id_pedido, id_usuario);
                response.setStatus(201);
                return ("{\"alerta\":\"Ação realizada com sucesso!\"}");
            } else {
                response.setStatus(400);
                return ("{\"erro\":\"O Produto não está no carrinho!\"}");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);

            response.setStatus(400);
            return ("{\"erro\":\"Erro ao realizar esta ação!\"}");
        }

    }

    public String addProduto(HttpServletRequest request, HttpServletResponse response) {
        try {

            int id_usuario = 0;
            int produto = 0;
            boolean existe = false;

            if (request.getParameter("id_user") != null) {
                id_usuario = Integer.parseInt(request.getParameter("id_user"));
            }

            int id_pedido = new DAOpedidos().buscarPedidoA(id_usuario);

            if (request.getParameter("id_produto") != null) {
                produto = Integer.parseInt(request.getParameter("id_produto"));
            }

            if (id_pedido == 0) {
                LocalDateTime agora = LocalDateTime.now();

                DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String data = formatterData.format(agora);

                DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");
                String hora = formatterHora.format(agora);

                DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
                java.sql.Date datasql = null;

                DateFormat fmt1 = new SimpleDateFormat("HH:mm:ss");
                java.sql.Time horasql = null;

                try {
                    datasql = new java.sql.Date(fmt.parse(data).getTime());
                    horasql = new java.sql.Time(fmt1.parse(hora).getTime());

                    ModelPedidos p = new ModelPedidos();
                    p.setId_usuario(id_usuario);
                    p.setData(datasql);
                    p.setHora(horasql);

                    id_pedido = new DAOpedidos().gerarPedido(p);
                } catch (ParseException ex) {
                    Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                ModelPedidos pd = new ModelPedidos();
                pd.setId_pedido(id_pedido);

                ModelProdutos mp = new ModelProdutos();
                mp.setId(produto);

                pd.setProduto(mp);
                try {
                    if (new DAOpedidos().verificaProdutos(pd)) {
                        existe = true;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (existe) {
                ModelPedidos pe = new ModelPedidos();
                pe.setId_pedido(id_pedido);

                ModelProdutos mp = new ModelProdutos();
                mp.setId(produto);

                pe.setProduto(mp);
                new DAOpedidos().addQTDE(pe);
            } else {
                ModelPedidos pe = new ModelPedidos();
                pe.setId_pedido(id_pedido);

                ModelProdutos mp = new ModelProdutos();
                mp.setId(produto);

                pe.setProduto(mp);
                new DAOpedidos().addProdutos(pe);
            }

            atualizaValor(id_pedido, id_usuario);

            response.setStatus(201);
            return ("{\"alerta\":\"Ação realizada com sucesso!\"}");
        } catch (SQLException ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao realizar esta ação!\"}");
        }
    }

    public String removerProduto(HttpServletRequest request, HttpServletResponse response) {
        try {

            int id_usuario = 0;
            int produto = 0;
            boolean existe = false;

            if (request.getParameter("id_user") != null) {
                id_usuario = Integer.parseInt(request.getParameter("id_user"));
            }

            int id_pedido = new DAOpedidos().buscarPedidoA(id_usuario);

            if (request.getParameter("id_produto") != null) {
                produto = Integer.parseInt(request.getParameter("id_produto"));
            }

            ModelPedidos pd = new ModelPedidos();
            pd.setId_pedido(id_pedido);

            ModelProdutos m = new ModelProdutos();
            m.setId(produto);

            pd.setProduto(m);
            try {
                if (new DAOpedidos().verificaProdutos(pd)) {
                    existe = true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (existe) {
                new DAOpedidos().removeProdutos(pd);

                atualizaValor(id_pedido, id_usuario);

                response.setStatus(201);
                return ("{\"alerta\":\"Ação realizada com sucesso!\"}");
            } else {
                response.setStatus(400);
                return ("{\"erro\":\"O produto não está vinculado ao seu carrinho de compras!\"}");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao realizar esta ação!\"}");
        }
    }

    public String listarPedidosFinalizados(HttpServletRequest request, HttpServletResponse response) {
        try {

            List<ModelPedidos> pedidos = new DAOpedidos().buscarPedidoF(Integer.parseInt(request.getParameter("id_user")));

            JSONObject retorno = new JSONObject();

            JSONArray arrayRetorno = new JSONArray();
            JSONObject json;
            for (ModelPedidos pedido : pedidos) {

                json = new JSONObject();

                json.put("id", pedido.getId_pedido());
                json.put("data", pedido.getData());
                json.put("hora", pedido.getHora());
                json.put("valor", pedido.getValor());

                arrayRetorno.put(json);
            }

            retorno.put("titulo", "Pedidos Finalizados");
            retorno.put("pedidos", arrayRetorno);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao listar!\"}");
        }

    }

    public String listarProdutosPedidoA(HttpServletRequest request, HttpServletResponse response) {
        try {

            int idpedido = new DAOpedidos().buscarPedidoA(Integer.parseInt(request.getParameter("id_user")));

            List<ModelPedidos> pedidos = new DAOpedidos().buscarPedidoProdutos(Integer.parseInt(request.getParameter("id_user")), idpedido);

            JSONObject retorno = new JSONObject();

            JSONArray arrayRetorno = new JSONArray();
            JSONObject json;
            for (ModelPedidos pedido : pedidos) {

                json = new JSONObject();

                json.put("id_pedido", pedido.getId_pedido());
                json.put("qtde", pedido.getQtde());

                json.put("id", pedido.getProduto().getId());
                json.put("nome", pedido.getProduto().getNome());
                json.put("marca", pedido.getProduto().getMarca());
                json.put("valor", pedido.getProduto().getValor());
                json.put("desconto", pedido.getProduto().getDesconto());
                json.put("img", pedido.getProduto().getImg());

                arrayRetorno.put(json);
            }

            retorno.put("titulo", "Carrinho de Compras");
            retorno.put("produtos", arrayRetorno);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            return ("{\"erro\":\"Erro ao listar!\"}");
        }

    }

    public String listarProdutosPedidoF(HttpServletRequest request, HttpServletResponse response) {
        try {

            List<ModelPedidos> pedidos = new DAOpedidos().buscarPedidoProdutos(Integer.parseInt(request.getParameter("id_user")), Integer.parseInt(request.getParameter("id_pedido")));

            JSONObject retorno = new JSONObject();

            JSONArray arrayRetorno = new JSONArray();
            JSONObject json;
            for (ModelPedidos pedido : pedidos) {

                json = new JSONObject();

                json.put("id_pedido", pedido.getId_pedido());
                json.put("qtde", pedido.getQtde());

                json.put("id", pedido.getProduto().getId());
                json.put("nome", pedido.getProduto().getNome());
                json.put("marca", pedido.getProduto().getMarca());
                json.put("valor", pedido.getProduto().getValor());
                json.put("desconto", pedido.getProduto().getDesconto());
                json.put("img", pedido.getProduto().getImg());

                arrayRetorno.put(json);
            }

            retorno.put("titulo", "Produtos Comprados");
            retorno.put("produtos", arrayRetorno);

            response.setStatus(200);
            return retorno.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            return ("{\"erro\":\"Erro ao listar!\"}");
        }

    }

    public void atualizaValor(int idpedido, int iduser) {
        try {

            double valorTotal = 0.0;
            double valorSomar = 0.0;
            double desconto;

            List<ModelPedidos> produtos = new DAOpedidos().pegaValor(idpedido, iduser);

            for (ModelPedidos produto : produtos) {
                desconto = produto.getProduto().getDesconto();

                valorSomar = ((produto.getProduto().getValor() - (produto.getProduto().getValor() * (desconto / 100))) * produto.getQtde());

                valorTotal += valorSomar;
            }

            new DAOpedidos().atualizaValor(valorTotal, idpedido, iduser);

        } catch (SQLException ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String finalizaPedido(HttpServletRequest request, HttpServletResponse response) {
        try {

            int id_usuario = 0;

            if (request.getParameter("id_user") != null) {
                id_usuario = Integer.parseInt(request.getParameter("id_user"));
            }

            int id_pedido = new DAOpedidos().buscarPedidoA(id_usuario);

            LocalDateTime agora = LocalDateTime.now();

            DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String data = formatterData.format(agora);

            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");
            String hora = formatterHora.format(agora);

            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            java.sql.Date datasql = null;

            DateFormat fmt1 = new SimpleDateFormat("HH:mm:ss");
            java.sql.Time horasql = null;

            datasql = new java.sql.Date(fmt.parse(data).getTime());
            horasql = new java.sql.Time(fmt1.parse(hora).getTime());

            ModelPedidos p = new ModelPedidos();
            p.setId_pedido(id_pedido);
            p.setData(datasql);
            p.setHora(horasql);
            
            if (id_pedido != 0) {
                new DAOpedidos().mudaStatusPedido(p);
                response.setStatus(201);
                return ("{\"alerta\":\"Ação realizada com sucesso!\"}");
            } else {
                response.setStatus(400);
                return ("{\"erro\":\"Nenhum pedido está associado a sua conta!\"}");
            }

        } catch (Exception ex) {
            Logger.getLogger(Pedidos.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao realizar esta ação!\"}");
        }
    }
}
