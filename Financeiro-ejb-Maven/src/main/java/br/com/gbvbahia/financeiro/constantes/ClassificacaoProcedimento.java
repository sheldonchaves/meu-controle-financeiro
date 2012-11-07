/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.constantes;

/**
 * Define se um procedimento é:<br> Fixo: contas mansais que não tem
 * como escapar, água, luz, telefone etc.<br> Variável: despesas que
 * são aleatorias, não são obrigações mensais, como compra de um
 * computador, um sapato, etc.
 *
 * @author Guilherme
 * @since v.3 08/04/2012
 */
public enum ClassificacaoProcedimento {

    /**
     * Define as despesas/receitas fixas.<br> Receita fixa pode ser o
     * salário mensal, uma pensão, aponsentaria.
     */
    FIXA("Fixa"),
    /**
     * Define as despesas/receitas variaveis.<br> Uma receita variavel
     * pode ser um bico, um emprestimo etc.
     */
    VARIAVEL("Variável");
    /**
     * Forma de exibição em tela.
     */
    private String label;

    /**
     * Construtor enum padrão.
     * @param label Exibição em tela.
     */
    private ClassificacaoProcedimento(String label) {
        this.label = label;
    }

    /**
     * Exibição em tela.
     * @return String.
     */
    public String getLabel() {
        return label;
    }
}
