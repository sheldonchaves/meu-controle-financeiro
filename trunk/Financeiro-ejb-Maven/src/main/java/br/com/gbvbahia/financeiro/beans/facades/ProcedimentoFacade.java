/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.Procedimento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * Interface da entidade Procedimento e suas subclasses.
 *
 * @since 13/04/2012
 * @author Guilherme
 */
@Local
public interface ProcedimentoFacade
        extends InterfaceFacade<Procedimento, Long> {

    /**
     * Busca todas as DespesasProcedimento por filtros.
     * @param cartao Cartão de Crédito, se null traz todos.
     * @param status SatusPagamento, se null traz todos.
     * @param usuario Usuário ou Conjuge responsavel, deve ser informado.
     * @return List&lt;DespesaProcedimento&gt;
     */
    List<DespesaProcedimento> buscarDespesaProcedimento(
            final CartaoCredito cartao, final StatusPagamento status,
            final Usuario usuario);
}
