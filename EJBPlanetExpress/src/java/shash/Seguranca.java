package shash;

import dao.DAOuser;
import java.sql.SQLException;

/**
 *
 * @author Matheus M. Burin
 */
public class Seguranca {

    public String gerar(String senha) {

        // Gera um sal aleatório
        String salGerado = BCrypt.gensalt();

        // Gera a senha hasheada utilizando o sal gerado
        String senhaHasheada = BCrypt.hashpw(senha, salGerado);

        // Retorna shash
        return senhaHasheada;
    }

    public boolean autentica(String senha, int iduser) {

        try {
            String senhaDoCandidato = senha; // Essa senha está em texto puro, sem hash.

            // Essa senha está hasheada.
            String senhaDoBanco;

            senhaDoBanco = new DAOuser().pegashash(iduser);

            // Usa o BCrypt para verificar se a senha passada está correta.
            // Isso envolve ler a senhaDoBanco, separar o que é sal e o que é hash
            // usar o sal para criar um hash da senhaDoCandidato e, por fim
            // verificar se os hashes gerados são iguais.
            boolean autenticacaoBateu = BCrypt.checkpw(senhaDoCandidato, senhaDoBanco);

            return autenticacaoBateu;
        } catch (SQLException ex) {
            return false;
        }
    }
}
