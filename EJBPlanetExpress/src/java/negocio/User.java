package negocio;

import dao.DAOuser;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelEndereco;
import model.ModelUser;
import org.json.JSONObject;
import utils.Seguranca;
import utils.JWTGen;

/**
 *
 * @author Matheus M. Burin
 */
@Stateless
public class User {

    public String criarUsuario(HttpServletRequest request, HttpServletResponse response) {

        String nome = null;
        String sobrenome = null;
        String email = null;
        String senha = null;
        String telefone = null;
        String cpf = null;
        String rg = null;
        String img = null;
        int endereco = 0;

        if (request.getParameter("nome") != null) {
            nome = request.getParameter("nome");
        }

        if (request.getParameter("sobrenome") != null) {
            sobrenome = request.getParameter("sobrenome");
        }

        if (request.getParameter("email") != null) {
            email = request.getParameter("email");
        }

        if (request.getParameter("senha") != null) {
            senha = new Seguranca().gerar(request.getParameter("senha"));
        }

        if (request.getParameter("telefone") != null) {
            telefone = request.getParameter("telefone");
        }

        if (request.getParameter("cpf") != null) {
            cpf = request.getParameter("cpf");
        }

        if (request.getParameter("rg") != null) {
            rg = request.getParameter("rg");
        }

        if (request.getParameter("img") != null) {
            img = request.getParameter("img");
        }

        if (request.getParameter("senha") != null) {
            endereco = novoEndereco(request, response);

            try {
                ModelUser user = new ModelUser();

                user.setNome(nome);
                user.setSobrenome(sobrenome);
                user.setEmail(email);
                user.setShash(senha);
                user.setTelefone(telefone);
                user.setCpf(cpf);
                user.setRg(rg);
                user.setImg(img);
                user.setEndereco(endereco);

                String retornonome = new DAOuser().novoUsuario(user);

                response.setStatus(201);
                return ("{\"alerta\":\"Usuário " + retornonome + " criado com sucesso!\"}");
            } catch (SQLException ex) {
                response.setStatus(400);
                return ("{\"erro\":\"Erro ao Cadastrar Usuário!\"}");
            }
        } else {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao Cadastrar Endereco!\"}");
        }
    }

    public String buscarUser(HttpServletRequest request, HttpServletResponse response) {

        String logininfo = null;

        if (request.getParameter("logininfo") != null) {
            logininfo = request.getParameter("logininfo");
        }

        try {
            ModelUser retornoinfos = new DAOuser().buscarUser(logininfo);

            JSONObject json = new JSONObject();

            json.put("id", retornoinfos.getId());
            json.put("nome", retornoinfos.getNome());
            json.put("img", retornoinfos.getImg());

            response.setStatus(202);
            return json.toString();
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao encontar Usuário!\"}");
        }
    }

    public String autenticaUser(HttpServletRequest request, HttpServletResponse response) {

        int id = 0;
        String senha = null;

        if (request.getParameter("id") != null) {
            id = Integer.parseInt(request.getParameter("id"));
        }

        if (request.getParameter("senha") != null) {
            senha = request.getParameter("senha");
        }

        boolean accept = new Seguranca().autentica(senha, id);

        String resposta;
        if (accept) {
            String token = JWTGen.gerador(request.getParameter("username"), "" + id);

            resposta = ("{\"token\":\"" + token + "\"}");
        } else {
            resposta = ("{\"erro\":\"Usuário ou senha incorretos!\"}");
        }
        return resposta;
    }

    public int novoEndereco(HttpServletRequest request, HttpServletResponse response) {
        try {
            String cidade = " ";
            int estado = 1;
            String cep = " ";
            String bairro = " ";
            String rua = " ";
            int numero = 0;

            if (request.getParameter("cidade") != null) {
                cidade = request.getParameter("cidade");
            }

            if (request.getParameter("estado") != null) {
                estado = Integer.parseInt(request.getParameter("estado"));
            }

            if (request.getParameter("cep") != null) {
                cep = request.getParameter("cep");
            }

            if (request.getParameter("bairro") != null) {
                bairro = request.getParameter("bairro");
            }

            if (request.getParameter("rua") != null) {
                rua = request.getParameter("rua");
            }

            if (request.getParameter("numero") != null) {
                numero = Integer.parseInt(request.getParameter("numero"));
            }

            ModelEndereco endereco = new ModelEndereco();
            endereco.setCidade(cidade);
            endereco.setEstado(estado);
            endereco.setBairro(bairro);
            endereco.setCep(cep);
            endereco.setRua(rua);
            endereco.setNumero(numero);

            int id = new DAOuser().novoendereco(endereco);
            return id;
        } catch (SQLException ex) {
            return 0;
        }
    }

    public String modificarUsuario(HttpServletRequest request, HttpServletResponse response, String jsonAuth) {

        JSONObject jsonA = new JSONObject(jsonAuth);

        int id = jsonA.getInt("user_id");

        String nome = null;
        String sobrenome = null;
        String email = null;
        String telefone = null;
        String cpf = null;
        String img = null;

        if (request.getParameter("nome") != null) {
            nome = request.getParameter("nome");
        }

        if (request.getParameter("sobrenome") != null) {
            sobrenome = request.getParameter("sobrenome");
        }

        if (request.getParameter("email") != null) {
            email = request.getParameter("email");
        }

        if (request.getParameter("telefone") != null) {
            telefone = request.getParameter("telefone");
        }

        if (request.getParameter("cpf") != null) {
            cpf = request.getParameter("cpf");
        }

        if (request.getParameter("img") != null) {
            img = request.getParameter("img");
        }

        try {
            ModelUser user = new ModelUser();

            user.setNome(nome);
            user.setSobrenome(sobrenome);
            user.setEmail(email);
            user.setTelefone(telefone);
            user.setCpf(cpf);
            user.setImg(img);
            user.setId(id);

            new DAOuser().modificaUsuario(user);

            response.setStatus(201);
            return ("{\"alerta\":\"Usuário editado com sucesso!\"}");
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao editar Usuário!\"}");
        }

    }

    public String modificarSenha(HttpServletRequest request, HttpServletResponse response, String jsonAuth) {

        JSONObject jsonA = new JSONObject(jsonAuth);

        int id = jsonA.getInt("user_id");

        String cpf = null;
        String senha = null;

        if (request.getParameter("senha") != null) {
            senha = new Seguranca().gerar(request.getParameter("senha"));
        }

        if (request.getParameter("cpf") != null) {
            cpf = request.getParameter("cpf");
        }

        try {
            ModelUser user = new ModelUser();

            user.setId(id);
            user.setShash(senha);
            user.setCpf(cpf);

            new DAOuser().modificarSenha(user);

            response.setStatus(201);
            return ("{\"alerta\":\"Senha editada com sucesso!\"}");
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao editar Senha!\"}");
        }
    }

    public String modificaEndereco(HttpServletRequest request, HttpServletResponse response, String jsonAuth) {
        try {

            JSONObject jsonA = new JSONObject(jsonAuth);

            int id_user = jsonA.getInt("user_id");

            String cidade = " ";
            int estado = 1;
            String cep = " ";
            String bairro = " ";
            String rua = " ";
            int numero = 0;
            String cpf = " ";

            if (request.getParameter("cidade") != null) {
                cidade = request.getParameter("cidade");
            }

            if (request.getParameter("estado") != null) {
                estado = Integer.parseInt(request.getParameter("estado"));
            }

            if (request.getParameter("cep") != null) {
                cep = request.getParameter("cep");
            }

            if (request.getParameter("bairro") != null) {
                bairro = request.getParameter("bairro");
            }

            if (request.getParameter("rua") != null) {
                rua = request.getParameter("rua");
            }

            if (request.getParameter("numero") != null) {
                numero = Integer.parseInt(request.getParameter("numero"));
            }

            if (request.getParameter("cpf") != null) {
                cpf = request.getParameter("cpf");
            }

            ModelEndereco endereco = new ModelEndereco();
            endereco.setCidade(cidade);
            endereco.setEstado(estado);
            endereco.setBairro(bairro);
            endereco.setCep(cep);
            endereco.setRua(rua);
            endereco.setNumero(numero);

            new DAOuser().modificaEndereco(endereco, id_user, cpf);

            response.setStatus(201);
            return ("{\"alerta\":\"Endereço editado com sucesso!\"}");
        } catch (SQLException ex) {
            response.setStatus(400);
            return ("{\"erro\":\"Erro ao editar endereço!\"}");
        }
    }

}
