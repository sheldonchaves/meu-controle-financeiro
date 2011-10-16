/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.money.enums;

/**
 * Retirada determina uma ContaPagar
 * Deposito determina uma ContaReceber
 * @author Guilherme
 */
public enum TipoMovimentacao {
RETIRADA("Pagamento"), 
DEPOSITO("Receita");

private String tipoMovimentacaoString;

    public String getTipoMovimentacaoString() {
        return tipoMovimentacaoString;
    }

    private TipoMovimentacao(String tipoMovimentacaoString) {
        this.tipoMovimentacaoString = tipoMovimentacaoString;
    }

    @Override
    public String toString(){
        return this.getTipoMovimentacaoString();
    }


}
