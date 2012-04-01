/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.constantes;

/**
 * Enum que define os tipos de contas possíveis.
 * @since v.2 31/03/2012
 * @author Guilherme
 */
public enum TipoConta  {

    /**
     * Conta de movimentação financeira.
     */
    CORRENTE("Conta Corrente", "CC", 1),
    /**
     * Conta onde o dinheiro deve ficar parado recebendo rendimentos.
     */
    POUPANCA("Poupança", "POU", 2),
    /**
     * Conta fixa, onde o dinehrio não deve ou não pode ser resgatado
     * a qualquer hora, como um titulo de capitalização ou
     * aponsetadoria privada.
     */
    INVESTIMENTO("Investimento", "INV", 3);
    /**
     * Label a ser exibida ao usuário.
     */
    private String tipoContaString;
    /**
     * Forma rezumida de demonstração.
     */
    private String abreviacao;
    /**
     * Ordem de exibição.
     */
    private Integer ordem;

    /**
     * Label a ser exibida ao usuário.
     * @return String
     */
    public String getTipoContaString() {
        return tipoContaString;
    }

    /**
     * Forma rezumida de demonstração.
     * @return String
     */
    public String getAbreviacao() {
        return abreviacao;
    }

    /**
     * Ordem de exibição.
     * @return Integer
     */
    public Integer getOrdem() {
        return ordem;
    }

    /**
     * Construtor Enum.
     * @param tipoConta Label a ser exibida ao usuário.
     * @param abr Forma rezumida de demonstração.
     * @param o Ordem de exibição.
     */
    private TipoConta(final String tipoConta,
            final String abr, final Integer o) {
        this.tipoContaString = tipoConta;
        this.abreviacao = abr;
        this.ordem = o;
    }

    @Override
    public String toString() {
        return this.getTipoContaString();
    }
}
