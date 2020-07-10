package dao;

import dbconnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ModelPedidos;
import model.ModelProdutos;

/**
 *
 * @author Matheus M. Burin
 */
public class DAOpedidos {

    Connection con;

    public DAOpedidos() {
        con = DatabaseConnection.getInstance().getConnection();
    }

    public int gerarPedido(ModelPedidos p) throws SQLException {
        String sql = "INSERT INTO PEDIDOS (USUARIO_ID, STATUS, DATA, HORA, VALOR) "
                + "VALUES (?, ?, ?, ?, ?) "
                + "RETURNING ID_PEDIDOS  ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, p.getId_usuario());
        ps.setInt(2, 1);
        ps.setDate(3, p.getData());
        ps.setTime(4, p.getHora());
        ps.setDouble(5, 0);
        ResultSet rs = ps.executeQuery();

        int id = 0;

        while (rs.next()) {
            id = rs.getInt("ID_PEDIDOS");
        }
        rs.close();
        ps.close();

        return id;
    } //CRIA UM NOVO PEDIDO CASO O USUARIO AINDA NÃO TENHA NENHUM EM ABERTO

    public boolean verificaProdutos(ModelPedidos p) throws SQLException {
        String sql = "SELECT 1 "
                + "FROM rdb$database "
                + "WHERE EXISTS(SELECT * FROM pedido_produtos WHERE PEDIDO_ID = ? AND PRODUTO_ID = ?) ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, p.getId_pedido());
        ps.setInt(2, p.getProduto().getId());
        ResultSet rs = ps.executeQuery();

        boolean existe = false;
        while (rs.next()) {
            if (rs.getInt("CONSTANT") != 0) {
                existe = true;
            }
        }

        rs.close();
        ps.close();

        return existe;

    } //VERIFICA SE O PRODUTO JÁ EXISTE NO PEDIDO

    public void addQTDE(ModelPedidos p) throws SQLException {
        String sql = "UPDATE PEDIDO_PRODUTOS "
                + "SET QTDE = QTDE + 1 "
                + "WHERE PEDIDO_ID = ? AND PRODUTO_ID = ? ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, p.getId_pedido());
        ps.setInt(2, p.getProduto().getId());
        ps.execute();
        ps.close();

    } //ATUALIZA A QTDE DE PRODUTOS

    public void diminuiQTDE(ModelPedidos p) throws SQLException {
        String sql = "UPDATE PEDIDO_PRODUTOS "
                + "SET QTDE = QTDE - 1 "
                + "WHERE PEDIDO_ID = ? AND PRODUTO_ID = ? ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, p.getId_pedido());
        ps.setInt(2, p.getProduto().getId());
        ps.execute();
        ps.close();

    } //ATUALIZA A QTDE DE PRODUTOS

    public void addProdutos(ModelPedidos p) throws SQLException {
        String sql = "INSERT INTO PEDIDO_PRODUTOS (PEDIDO_ID, PRODUTO_ID, QTDE) "
                + "VALUES ( ?, ?, 1) ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, p.getId_pedido());
        ps.setInt(2, p.getProduto().getId());
        ps.execute();
        ps.close();

    } //ADICIONA PRODUTOS AO PEDIDO ABERTO

    public void removeProdutos(ModelPedidos p) throws SQLException {
        String sql = "DELETE FROM PEDIDO_PRODUTOS "
                + "WHERE PEDIDO_ID = ? AND PRODUTO_ID = ? ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, p.getId_pedido());
        ps.setInt(2, p.getProduto().getId());
        ps.execute();
        ps.close();

    } //REMOVE PRODUTOS DO PEDIDO ABERTO

    public void mudaStatusPedido(ModelPedidos p) throws SQLException {
        String sql = "UPDATE PEDIDOS " +
                        "SET STATUS = 2 , DATA = ?, HORA = ? " +
                            "WHERE ID_PEDIDOS = ? ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDate(1, p.getData());
        ps.setTime(2, p.getHora());
        ps.setInt(3, p.getId_pedido());
        ps.execute();
        ps.close();

    } // MUDA STATUS DO PEDIDO PARA FINALIZADO

    public int buscarPedidoA(int user) throws SQLException {

        //STATUS = 1 -> Aberto (Carrinho)
        //STATUS = 2 -> Finalizado (Histórico de compras)
        String sql = "SELECT ID_PEDIDOS "
                + "FROM PEDIDOS "
                + "WHERE USUARIO_ID = ? AND STATUS = 1 ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, user);
        ResultSet rs = ps.executeQuery();

        int id = 0;

        while (rs.next()) {
            id = rs.getInt("ID_PEDIDOS");
        }
        rs.close();
        ps.close();

        return id;
    } //BUSCA OS PEDIDOS ABERTOS (CARRINHO DE COMPRAS)

    public List<ModelPedidos> buscarPedidoF(int id) throws SQLException {

        //STATUS = 1 -> Aberto (Carrinho)
        //STATUS = 2 -> Finalizado (Histórico de compras)
        List<ModelPedidos> pedidos = new ArrayList<>();

        String sql = "SELECT ID_PEDIDOS, DATA, HORA, VALOR "
                + "FROM PEDIDOS "
                + "WHERE USUARIO_ID = ? AND STATUS = 2 ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelPedidos pedido = new ModelPedidos();

            pedido.setId_pedido(rs.getInt("ID_PEDIDOS"));
            pedido.setData(rs.getDate("DATA"));
            pedido.setHora(rs.getTime("HORA"));
            pedido.setValor(rs.getDouble("VALOR"));

            pedidos.add(pedido);
        }

        rs.close();
        ps.close();

        return pedidos;
    } //BUSCA OS PEDIDOS FINALIZADOS

    public List<ModelPedidos> buscarPedidoProdutos(int iduser, int idpedido) throws SQLException {
        List<ModelPedidos> pedidos = new ArrayList<>();

        String sql = "SELECT P.ID_PEDIDOS, O.QTDE, R.ID_PRODUTO, M.NOME AS MARCA, R.NOME, R.VALOR, R.DESCONTO "
                + "FROM PEDIDOS P "
                + "JOIN PEDIDO_PRODUTOS O ON P.ID_PEDIDOS = O.PEDIDO_ID "
                + "JOIN PRODUTOS R ON O.PRODUTO_ID = R.ID_PRODUTO "
                + "JOIN MARCAS M ON R.MARCA_ID = M.ID_MARCA "
                + "WHERE P.USUARIO_ID = ? AND P.ID_PEDIDOS = ? ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, iduser);
        ps.setInt(2, idpedido);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelPedidos pedido = new ModelPedidos();

            pedido.setId_pedido(rs.getInt("ID_PEDIDOS"));
            pedido.setQtde(rs.getInt("QTDE"));

            ModelProdutos produto = new ModelProdutos();

            produto.setId(rs.getInt("ID_PRODUTO"));
            produto.setMarca(rs.getString("MARCA"));
            produto.setNome(rs.getString("NOME"));
            produto.setValor(rs.getDouble("VALOR"));
            produto.setDesconto(rs.getInt("DESCONTO"));
            produto.setImg("p" + rs.getInt("ID_PRODUTO") + "img1");

            pedido.setProduto(produto);
            pedidos.add(pedido);
        }

        rs.close();
        ps.close();

        return pedidos;
    } //BUSCA OS PODUTOS DE UM PEDIDO

    public List<ModelPedidos> pegaValor(int idpedido, int iduser) throws SQLException {
        List<ModelPedidos> pedidos = new ArrayList<>();

        String sql = "SELECT PR.VALOR, PR.DESCONTO, PE.QTDE "
                + "FROM PRODUTOS PR "
                + "JOIN PEDIDO_PRODUTOS PE ON PR.ID_PRODUTO = PE.PRODUTO_ID "
                + "JOIN PEDIDOS PD ON PE.PEDIDO_ID = PD.ID_PEDIDOS "
                + "WHERE PE.PEDIDO_ID = ? AND PD.USUARIO_ID = ? ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, idpedido);
        ps.setInt(2, iduser);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelPedidos pedido = new ModelPedidos();

            pedido.setQtde(rs.getInt("QTDE"));

            ModelProdutos produto = new ModelProdutos();

            produto.setValor(rs.getDouble("VALOR"));
            produto.setDesconto(rs.getInt("DESCONTO"));

            pedido.setProduto(produto);

            pedidos.add(pedido);
        }

        rs.close();
        ps.close();
        return pedidos;
    } //PEGA OS VALORES QTDE E DESCONTO DE CADA PRODUTO

    public void atualizaValor(double valor, int idpedido, int iduser) throws SQLException {
        String sql = "UPDATE PEDIDOS "
                + "SET VALOR = ? "
                + "WHERE ID_PEDIDOS = ? AND USUARIO_ID = ? ";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setDouble(1, valor);
        ps.setInt(2, idpedido);
        ps.setInt(3, iduser);
        ps.execute();
        ps.close();

    } //ATUALIZA O VALOR DO PEDIDO
}
