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

    public ModelUser buscarUser(String login) throws SQLException {
        String sql = "SELECT ID_USUARIO, NOME, IMG " +
                        "FROM USUARIOS " +
                            "WHERE (UPPER(EMAIL) = UPPER("+login+")) OR (UPPER(CPF) = UPPER("+login+")) ; ";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ModelUser user = new ModelUser();
        user.setId(rs.getInt("ID_USUARIO"));
        user.setImg(rs.getString("IMG"));
        user.setNome(rs.getString("NOME"));

        rs.close();
        ps.close();
        
        return user;
    }

    public String pegashash(int iduser) throws SQLException {
        String sql = "SELECT SHASH "
                + "FROM usuarios "
                + "WHERE ID_USUARIO = " + iduser + " ;";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String shash = rs.getString("SHASH");
        rs.close();
        ps.close();
        return shash;
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

        int id = 0;

        if (rs.next()) {
            id = rs.getInt("ID_ENDERECO");
        }
        rs.close();
        ps.close();
        return id;
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

        String nome = null;
        if (rs.next()) {
            nome = rs.getString("nome");

        } else {
            nome = "ERROR";
        }
        rs.close();
        ps.close();
        return nome;
    }

}
