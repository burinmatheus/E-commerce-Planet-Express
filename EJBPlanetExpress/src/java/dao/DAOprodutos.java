/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dbconnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ModelProdutos;

/**
 *
 * @author Matheus M. Burin
 */
public class DAOprodutos {
    Connection con;

    public DAOprodutos() {
        con = DatabaseConnection.getInstance().getConnection();
    }
    
    public List<ModelProdutos> listarProdutosTodos(int pg) throws SQLException {
        List<ModelProdutos> produtos = new ArrayList<>();
 
        String sql = "SELECT FIRST 15 SKIP " + (15 * pg) + " P.ID_PRODUTO, P.MARCA_ID, M.ID_MARCA, M.NOME AS MARCA, P.NOME, P.VALOR, P.DESCONTO " +
                        "FROM produtos P " +
                            "JOIN marcas M ON P.MARCA_ID = M.ID_MARCA ";

        PreparedStatement ps = con.prepareStatement(sql);
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
    
    public List<ModelProdutos> listarProdutosCategoria(int categoria_id, int pg) throws SQLException {
        List<ModelProdutos> produtos = new ArrayList<>();

        String sql = "SELECT FIRST 15 SKIP " + (15 * pg) + " P.ID_PRODUTO, P.MARCA_ID, M.ID_MARCA, M.NOME AS MARCA, P.NOME, P.VALOR, P.DESCONTO, C.NOME AS CATEGORIA " +
                        "FROM produtos P " +
                            "JOIN marcas M ON P.MARCA_ID = M.ID_MARCA " +
                                "JOIN categorias_produto CP ON P.ID_PRODUTO = CP.PRODUTO_ID " +
                                    "JOIN categorias C ON CP.CATEGORIA_ID = C.ID_CATEGORIA " +
                                        "WHERE C.ID_CATEGORIA = "+ categoria_id +"; ";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelProdutos produto = new ModelProdutos();

            produto.setId(rs.getInt("ID_PRODUTO"));
            produto.setNome(rs.getString("NOME"));
            produto.setMarca(rs.getString("MARCA"));
            produto.setValor(rs.getDouble("VALOR"));
            produto.setDesconto(rs.getInt("DESCONTO"));
            produto.setCategoria(rs.getString("CATEGORIA"));
            produto.setImg("p" + rs.getInt("ID_PRODUTO") + "img1");
                    
                    
            produtos.add(produto);
        }
        rs.close();
        ps.close();

        return produtos;
    }
    
    public List<ModelProdutos> listarProdutosSegmentos(int segmento_id, int pg) throws SQLException {
        List<ModelProdutos> produtos = new ArrayList<>();

        String sql = "SELECT FIRST 15 SKIP " + (15 * pg) + " P.ID_PRODUTO, P.MARCA_ID, M.ID_MARCA, M.NOME AS MARCA, P.NOME, P.VALOR, P.DESCONTO, S.NOME AS SEGMENTO " +
                        "FROM produtos P " +
                            "JOIN marcas M ON P.MARCA_ID = M.ID_MARCA " +
                                "JOIN categorias_produto CP ON P.ID_PRODUTO = CP.PRODUTO_ID " +
                                    "JOIN categorias C ON CP.CATEGORIA_ID = C.ID_CATEGORIA " +
                                        "JOIN segmentos S ON C.SEGMENTO_ID = S.ID_SEGMENTO\n" +
                                            "WHERE S.ID_SEGMENTO = "+ segmento_id +";";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelProdutos produto = new ModelProdutos();

            produto.setId(rs.getInt("ID_PRODUTO"));
            produto.setNome(rs.getString("NOME"));
            produto.setMarca(rs.getString("MARCA"));
            produto.setValor(rs.getDouble("VALOR"));
            produto.setDesconto(rs.getInt("DESCONTO"));
            produto.setSegmento(rs.getString("SEGMENTO"));
            produto.setImg("p" + rs.getInt("ID_PRODUTO") + "img1");
                    
                    
            produtos.add(produto);
        }
        
        rs.close();
        ps.close();

        return produtos;
    }
    
    public List<ModelProdutos> listarProdutosPesquisa(int pg, String pesq) throws SQLException {
        List<ModelProdutos> produtos = new ArrayList<>();
 
        String sql = "SELECT FIRST 15 SKIP " + (15 * pg) + " P.ID_PRODUTO, P.MARCA_ID, M.ID_MARCA, M.NOME AS MARCA, P.NOME, P.VALOR, P.DESCONTO " +
                        "FROM produtos P " +
                            "JOIN marcas M ON P.MARCA_ID = M.ID_MARCA " + 
                                "WHERE UPPER(P.NOME) LIKE UPPER('%"+ pesq +"%'); ";

        PreparedStatement ps = con.prepareStatement(sql);
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
