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
import org.kohsuke.rngom.parse.Parseable;

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
        ModelUser user = new ModelUser();

        String sql = "SELECT ID_USUARIO, NOME, IMG "
                + "FROM USUARIOS "
                + "WHERE CPF = "+login+" ";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            user.setId(rs.getInt("ID_USUARIO"));
            user.setImg(rs.getString("IMG"));
            user.setNome(rs.getString("NOME"));
        }
        rs.close();
        ps.close();

        return user;
    }

    public String pegashash(int iduser) throws SQLException {
        String sql = "SELECT SHASH as senha "
                + "FROM usuarios "
                + "WHERE ID_USUARIO = ? ; ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, iduser);
        ResultSet rs = ps.executeQuery();

        String shash = null;
        if (rs.next()) {
            shash = rs.getString("senha");
        }
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

    public int buscaEndereco(int id, String cpf) throws SQLException {
        String sql1 = "SELECT ENDERECO_ID "
                + "FROM usuarios "
                + "WHERE CPF = " + cpf + " AND ID_USUARIO = " + id + " ;";

        PreparedStatement pso = con.prepareStatement(sql1);
        ResultSet rs = pso.executeQuery();

        int id_endereco = 0;

        if (rs.next()) {
            id_endereco = rs.getInt("ENDERECO_ID");
        }
        rs.close();
        pso.close();

        return id_endereco;
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

    public void modificarSenha(ModelUser user) throws SQLException {
        String sql = "UPDATE usuarios "
                + "SET SHASH = ? "
                + "WHERE ID_USUARIO = ? AND CPF = ? ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getShash());
            ps.setInt(2, user.getId());
            ps.setString(3, user.getCpf());
            ps.execute();
            ps.close();
        }
    }

    public void modificaUsuario(ModelUser user) throws SQLException {
        String sql = "UPDATE usuarios "
                + "SET NOME = ?, SOBRENOME = ?, IMG = ?, EMAIL = ?, TELEFONE = ? "
                + "WHERE CPF = ? and ID_USUARIO = ? ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getNome());
            ps.setString(2, user.getSobrenome());
            ps.setString(3, user.getImg());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getTelefone());
            ps.setString(6, user.getCpf());
            ps.setInt(7, user.getId());
            ps.execute();
            ps.close();
        }
    }

    public void modificaEndereco(ModelEndereco end, int id, String cpf) throws SQLException {

        int id_endereco = buscaEndereco(id, cpf);

        String sql = "UPDATE ENDERECOS "
                + "SET ESTADO_ID = ?, CIDADE = ?, CEP = ?, BAIRRO = ?, RUA = ?, NUMERO = ? "
                + "WHERE ID_ENDERECO = ? ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, end.getEstado());
        ps.setString(2, end.getCidade());
        ps.setString(3, end.getCep());
        ps.setString(4, end.getBairro());
        ps.setString(5, end.getRua());
        ps.setInt(6, end.getNumero());
        ps.setInt(7, id_endereco);
        ps.execute();
        ps.close();

    }
}
