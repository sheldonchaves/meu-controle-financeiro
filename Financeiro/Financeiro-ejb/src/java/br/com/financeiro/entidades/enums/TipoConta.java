/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.entidades.enums;

/**
 *
 * @author Guilherme
 */
public enum TipoConta {

    POUPANCA("Poupança"), CONTA_CORRENTE("Conta Corrente"), INVESTIMENTO("Investimento");

    private String tipoContaString;

    public String getTipoContaString() {
        return tipoContaString;
    }

    private TipoConta(String tipoContaString) {
        this.tipoContaString = tipoContaString;
    }

    @Override
    public String toString(){
        return this.getTipoContaString();
    }
}
