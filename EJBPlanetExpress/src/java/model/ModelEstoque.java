
package model;

/**
 *
 * @author Matheus M. Burin
 */
public class ModelEstoque {
    private ModelCores cor;
    private ModelTamanhos tamanho;
    private int qtde;


    public ModelCores getCor() {
        return cor;
    }

    public void setCor(ModelCores cor) {
        this.cor = cor;
    }

    public ModelTamanhos getTamanho() {
        return tamanho;
    }

    public void setTamanho(ModelTamanhos tamanho) {
        this.tamanho = tamanho;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }
}
