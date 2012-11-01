/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.ReceitaProcedimento;
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
     *
     * @param cartao Cartão de Crédito, se null traz todos.
     * @param status SatusPagamento, se null traz todos.
     * @param usuario Usuário ou Conjuge responsavel, deve ser
     * informado.
     * @return List&lt;DespesaProcedimento&gt;
     */
    List<DespesaProcedimento> buscarDespesaProcedimento(
            final CartaoCredito cartao, final StatusPagamento status,
            final Usuario usuario);

    /**
     * Cria contas parceladas a partir de qualquer Procedimento
     * passado.
     *
     * @param entity Procedimento.
     * @param parTotal Quantidade de parcelas do parcelamento.
     * Obrigatório.
     * @param parAtual Parcela atual do parcelamento. Obrigatório.
     * @param cartao Cartão de crédito onde foi parcelado. Opcional.
     * @throws NegocioException Caso alguma validação seja violada.
     */
    void create(final Procedimento entity,
            final int parTotal, final int parAtual,
            final CartaoCredito cartao) throws NegocioException;

    /**
     * Busca contas pela Enum TipoProcedimento<br> DESPESA_FINANCEIRA
     * Despesa, gasto, saída de dinheiro.<br> RECEITA_FINANCEIRA
     * Receita, lucro, entrada de dinheiro.
     *
     * @param user Usuário responsável. Obrigatório.
     * @param tipo TipoProcedimento. Obrigatório.
     * @return Lista de Procedimentos no perfil solicitado. Ou lista
     * vazia se não encontrar.
     */
    List<Procedimento> buscarPorTipoProcedimento(
            final Usuario user, final TipoProcedimento tipo);

    /**
     * Busca todas as ReceitasProcedimento por filtros.
     *
     * @param status SatusPagamento, se null traz todos.
     * @param usuario Usuário ou Conjuge responsavel, deve ser
     * informado.
     * @return List&lt;ReceitaProcedimento&gt;
     */
    List<ReceitaProcedimento> buscarReceitaProcedimento(
            final StatusPagamento status, final Usuario usuario);
}
