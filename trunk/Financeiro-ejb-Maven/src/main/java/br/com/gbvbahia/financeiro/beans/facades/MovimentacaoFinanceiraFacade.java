/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.superclass.MovimentacaoFinanceira;
import javax.ejb.Local;

/**
 *
 * @author Usuário do Windows
 */
@Local
public interface MovimentacaoFinanceiraFacade extends InterfaceFacade<MovimentacaoFinanceira, Long> {

    /**
     * Busca a movimentação procedimento que possui relacionamento com o
     * procedimento encontrado.
     *
     * @param procedimento obrigatorio.
     * @return Movimentação encontrado, null se não encontrar.
     */
    MovimentacaoProcedimento buscarPorProcedimento(Procedimento procedimento);
}
