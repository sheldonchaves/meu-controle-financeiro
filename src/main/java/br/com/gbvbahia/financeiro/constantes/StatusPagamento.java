/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.gbvbahia.financeiro.constantes;

/**
 * Enum que define o status de uma conta a pagar ou receber.
 * Paga ou Quitada
 * Não Paga ou Em Dêbito
 * @author Guilherme
 * @since v.2 31/03/2012
 */
public enum StatusPagamento {

    /**
     * Define que a conta está quitada.
     */
    PAGA("Paga"),
    /**
     * Define que a conta ainda está a pagar.
     */
    NAO_PAGA("Não Paga");

    /**
     * Label de exibição.
     */
    private String statusString;

    /**
     * Label de exibição.
     * @return String.
     */
    public String getStatusString() {
        return statusString;
    }

    /**
     * Construtor Enum padrão com variavel.
     * @param status Status Label.
     */
    private StatusPagamento(final String status) {
        this.statusString = status;
    }

    @Override
    public String toString() {
        return this.getStatusString();
    }


}
