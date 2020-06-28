
package model;

/**
 *
 * @author Matheus M. Burin
 */

public class ModelEndereco {
    private int id;
    private String cidade;
    private String estado_nome;
    private String estado_sigla;
    private String cep;
    private String bairro;
    private String rua;
    private int numero;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado_nome() {
        return estado_nome;
    }

    public void setEstado_nome(String estado_nome) {
        this.estado_nome = estado_nome;
    }

    public String getEstado_sigla() {
        return estado_sigla;
    }

    public void setEstado_sigla(String estado_sigla) {
        this.estado_sigla = estado_sigla;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    
}
