/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.entidades.enums;

/**
 *
 * @author Guilherme
 */
public enum StatusPagamento {

    PAGA("Paga"), NAO_PAGA("Não Paga");

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
