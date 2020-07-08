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
import model.ModelEndereco;
import model.ModelTela;

/**
 *
 * @author Matheus M. Burin
 */
public class DAOtela {
    Connection con;

    public DAOtela() {
        con = DatabaseConnection.getInstance().getConnection();
    }
    
    public List<ModelTela> buscarCategorias(int id) throws SQLException {
        List<ModelTela> categorias = new ArrayList<>();
 
        String sql = "SELECT ID_CATEGORIA, NOME " +
                        "FROM categorias " +
                            "WHERE SEGMENTO_ID = "+id+" ; ";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelTela categoria = new ModelTela();

            categoria.setId_categoria(rs.getInt("ID_CATEGORIA"));
            categoria.setCategoria(rs.getString("NOME"));
            
            categorias.add(categoria);
        }
        rs.close();
        ps.close();

        return categorias;
    }
    
    public List<ModelTela> buscarSegmentos() throws SQLException {
        List<ModelTela> segmentos = new ArrayList<>();
 
        String sql = "SELECT ID_SEGMENTO, NOME, IMG " +
                        "FROM segmentos ";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelTela segmento = new ModelTela();

            segmento.setId_segmento(rs.getInt("ID_SEGMENTO"));
            segmento.setSegmento(rs.getString("NOME"));
            segmento.setSegmentoImg(rs.getString("IMG"));
            
            segmentos.add(segmento);
        }
        rs.close();
        ps.close();

        return segmentos;
    }
    
    public List<ModelEndereco> buscarEstados() throws SQLException {
        List<ModelEndereco> estados = new ArrayList<>();
 
        String sql = "SELECT ID_ESTADO, NOME, SIGLA " +
                        "FROM estados ";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelEndereco estado = new ModelEndereco();

            estado.setEstado(rs.getInt("ID_ESTADO"));
            estado.setEstado_nome(rs.getString("NOME"));
            estado.setEstado_sigla(rs.getString("SIGLA"));
            
            estados.add(estado);
        }
        rs.close();
        ps.close();

        return estados;
    }
    
}
