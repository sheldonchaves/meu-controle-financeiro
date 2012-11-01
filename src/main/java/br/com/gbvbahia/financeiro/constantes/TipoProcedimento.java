/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.constantes;

/**
 * Retirada determina uma DESPESA.
 * Deposito determina uma RECEITA.
 * @author Guilherme
 * @since v.2 01/04/2012
 */
public enum TipoProcedimento {

    /**
     * Representa um gasto, saída de $.
     */
    DESPESA_FINANCEIRA("Pagamento"),
    /**
     * Representa receita, entrada de $.
     */
    RECEITA_FINANCEIRA("Receita");
    /**
     * Label para exibição na tela.
     */
    private String tipoMovimentacaoString;

    /**
     * Label para exibição.
     * @return String
     */
    public String getTipoMovimentacaoString() {
        return tipoMovimentacaoString;
    }

    /**
     * Construtor Enum.
     * @param tipoMovimentacao tipo de movimentação label.
     */
    private TipoProcedimento(final String tipoMovimentacao) {
        this.tipoMovimentacaoString = tipoMovimentacao;
    }

    @Override
    public String toString() {
        return this.getTipoMovimentacaoString();
    }
}
