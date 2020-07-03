
package model;

import java.util.List;

/**
 *
 * @author Matheus M. Burin
 */
public class ModelTamanhos {
    private int id;
    private String tamanho;
    private List<ModelCores> cor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public List<ModelCores> getCor() {
        return cor;
    }

    public void setCor(List<ModelCores> cor) {
        this.cor = cor;
    }

    
    
}
