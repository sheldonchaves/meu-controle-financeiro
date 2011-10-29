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

    POUPANCA("Poupança","POU",2), 
    CONTA_CORRENTE("Conta Corrente","CC",1), 
    INVESTIMENTO("Investimento","INV",3),
    CARTAO_DE_CREDITO("Cartão de Crédito","CRE",4);

    private String tipoContaString;
    private String abreviacao;
    private Integer ordem;
    
    public String getTipoContaString() {
        return tipoContaString;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    private TipoConta(String tipoContaString, String abreviacao, Integer ordem) {
        this.tipoContaString = tipoContaString;
        this.abreviacao = abreviacao;
        this.ordem = ordem;
    }

    @Override
    public String toString(){
        return this.getTipoContaString();
    }
}
