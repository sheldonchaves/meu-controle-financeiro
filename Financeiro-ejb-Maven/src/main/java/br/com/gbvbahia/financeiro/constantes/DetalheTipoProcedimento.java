/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.constantes;

/**
 * Detalhe o procedimento. TipoProcedimento é resumido em Despesa ou
 * Receita, detalhe informa o tipo de despesa e receita, separando
 * parcelamento de unico.
 *
 * @author Guilherme
 */
public enum DetalheTipoProcedimento {

    /**
     * Representa um gasto unico, saída de $.
     */
    DESPESA_UNICA,
    /**
     * Um gasto dividido em vários meses.
     */
    DESPESA_PARCELADA,
    /**
     * Representa receita, entrada de $.
     */
    RECEITA_UNICA;
}
