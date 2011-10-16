/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.money.enums;

/**
 *
 * @author Guilherme
 */
public enum TipoConta {

    POUPANCA("Poupança"), 
    CONTA_CORRENTE("Conta Corrente"), 
    INVESTIMENTO("Investimento"),
    CARTAO_DE_CREDITO("Cartão de Crédito");

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
