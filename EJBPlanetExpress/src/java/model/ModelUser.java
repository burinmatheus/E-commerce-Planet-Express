
package model;

/**
 *
 * @author Matheus M. Burin
 */
public class ModelUser {
    private int id;
    private String nome;
    private String sobrenome;
    private String email;
    private String shash;
    private String telefone;
    private String cpf;
    private String rg;
    private String img;
    private ModelEndereco endereco;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShash() {
        return shash;
    }

    public void setShash(String shash) {
        this.shash = shash;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ModelEndereco getEndereco() {
        return endereco;
    }

    public void setEndereco(ModelEndereco endereco) {
        this.endereco = endereco;
    }
    
    
}
