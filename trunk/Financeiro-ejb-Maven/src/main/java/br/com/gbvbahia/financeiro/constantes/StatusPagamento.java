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

    PAGA("Paga"), 
    NAO_PAGA("Não Paga");

    private String statusString;

    public String getStatusString() {
        return statusString;
    }

    private StatusPagamento(String statusString) {
        this.statusString = statusString;
    }

    @Override
    public String toString() {
        return this.getStatusString();
    }


}
