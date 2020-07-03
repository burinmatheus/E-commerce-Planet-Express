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
import model.ModelEndereco;
import model.ModelUser;

/**
 *
 * @author Matheus M. Burin
 */
public class DAOuser {

    Connection con;

    public DAOuser() {
        con = DatabaseConnection.getInstance().getConnection();
    }

    public String pegashash(int iduser) throws SQLException {
        String sql = "SELECT SHASH "
                + "FROM usuarios "
                + "WHERE ID_USUARIO = " + iduser + " ;";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        return rs.getString("SHASH");
    }

    public int novoendereco(ModelEndereco end) throws SQLException {
        String sql = "INSERT INTO enderecos (estado_id, cidade, cep, bairro, rua, numero ) "
                + "VALUES (?, ?, ?, ?, ?, ?) "
                + "returning id_endereco ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, end.getEstado());
        ps.setString(2, end.getCidade());
        ps.setString(3, end.getCep());
        ps.setString(4, end.getBairro());
        ps.setString(5, end.getRua());
        ps.setInt(6, end.getNumero());
        ResultSet rs = ps.executeQuery();
        
        if(rs.next()){
         return rs.getInt("ID_ENDERECO");
        }else{
        return 0;
        }

    }

    public String novoUsuario(ModelUser user) throws SQLException {
        String sql = "INSERT INTO usuarios (NOME, SOBRENOME, IMG, EMAIL, SHASH, TELEFONE, CPF, RG, ENDERECO_ID) "
                + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "returning nome ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getNome());
        ps.setString(2, user.getSobrenome());
        ps.setString(3, user.getImg());
        ps.setString(4, user.getEmail());
        ps.setString(5, user.getShash());
        ps.setString(6, user.getTelefone());
        ps.setString(7, user.getCpf());
        ps.setString(8, user.getRg());
        ps.setInt(9, user.getEndereco());
        ResultSet rs = ps.executeQuery();
        
        if(rs.next()){
         return rs.getString("nome");
        }else{
        return "ERROR";
        }
    }

}
