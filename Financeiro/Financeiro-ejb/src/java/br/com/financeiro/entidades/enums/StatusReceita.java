/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.entidades.enums;

/**
 *
 * @author gbvbahia
 */
public enum StatusReceita {

    RECEBIDA("Recebida"), NAO_RECEBIDA("Não Recebida");

    private String statusString;

    public String getStatusString() {
        return statusString;
    }

    private StatusReceita(String statusString) {
        this.statusString = statusString;
    }

    @Override
    public String toString() {
        return this.getStatusString();
    }

}
