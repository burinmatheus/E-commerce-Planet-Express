package dao;

import dbconnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ModelProdutos;
import model.ModelUser;

/**
 *
 * @author Matheus M. Burin
 */
public class DAOfavoritos {

    Connection con;

    public DAOfavoritos() {
        con = DatabaseConnection.getInstance().getConnection();
    }

    public void adicionarFavorito(int id_user, int id_produto) throws SQLException {
        String sql = "INSERT INTO favorito (USUARIO_ID, PRODUTO_ID) "
                + "VALUES ( ?, ?) ";

        PreparedStatement ps;
        ps = con.prepareStatement(sql);

        ps.setInt(1, id_user);
        ps.setInt(2, id_produto);
        ps.execute();
        ps.close();
    }

    public void removerFavorito(int id_user, int id_produto) throws SQLException {
        String sql = "DELETE FROM favorito "
                + "WHERE USUARIO_ID = ? AND PRODUTO_ID = ? ";

        PreparedStatement ps;
        ps = con.prepareStatement(sql);
        ps.setInt(1, id_user);
        ps.setInt(2, id_produto);
        ps.execute();
        ps.close();
    }

    public List<ModelProdutos> buscarFavoritos(int pg, int id) throws SQLException {
        List<ModelProdutos> produtos = new ArrayList<>();

        String sql = "SELECT FIRST 15 SKIP " + (15 * (pg - 1)) + " P.ID_PRODUTO, P.MARCA_ID, M.ID_MARCA, M.NOME AS MARCA, P.NOME, P.VALOR, P.DESCONTO "
                + "FROM produtos P "
                + "JOIN marcas M ON P.MARCA_ID = M.ID_MARCA "
                + "JOIN favorito F ON P.ID_PRODUTO = F.PRODUTO_ID "
                + "WHERE F.usuario_id = ? ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelProdutos produto = new ModelProdutos();

            produto.setId(rs.getInt("ID_PRODUTO"));
            produto.setNome(rs.getString("NOME"));
            produto.setMarca(rs.getString("MARCA"));
            produto.setValor(rs.getDouble("VALOR"));
            produto.setDesconto(rs.getInt("DESCONTO"));
            produto.setImg("p" + rs.getInt("ID_PRODUTO") + "img1");

            produtos.add(produto);
        }
        rs.close();
        ps.close();

        return produtos;
    }
}
