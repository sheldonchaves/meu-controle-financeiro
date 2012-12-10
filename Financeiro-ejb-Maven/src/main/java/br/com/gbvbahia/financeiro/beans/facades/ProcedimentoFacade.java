/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.constantes.DetalheTipoProcedimento;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaParceladaProcedimento;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.dto.MinMaxDateDTO;
import java.util.Date;
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
     * Cria contas parceladas a partir do DespesaParceladaProcedimento passado. As
     * contas terão as datas de vencimento incrementadas em um mês.
     *
     * @param entity DespesaParceladaProcedimento.
     * @param parTotal Quantidade de parcelas do parcelamento. Obrigatório.
     * @param parAtual Parcela atual do parcelamento. Obrigatório.
     * @param cartao Cartão de crédito onde foi parcelado. Opcional.
     * @throws NegocioException Caso alguma validação seja violada.
     */
    void create(final DespesaParceladaProcedimento entity,
            final int parTotal, final int parAtual,
            final CartaoCredito cartao) throws NegocioException;

    /**
     * Busca por filtros passados, somente Usuario é obrigatorio.
     *
     * @param user Usuario responsavel. Obrigatorio.
     * @param cartao Cartão que efetuou pagamento, Opcional.
     * @param status Status Paga ou Nao Paga, Opcional.
     * @param tipo Tipo DESPESA_FINANCEIRA ou RECEITA_FINANCEIRA, Opcional.
     * @return Lista com procedimentos no perfil, se não encontrar retorna
     * lista vazia.
     */
    List<Procedimento> buscarPorUsuarioCartaoStatusTipo(
            Usuario user, CartaoCredito cartao, StatusPagamento status,
            TipoProcedimento tipo);

    /**
     * Busca contas/receitas por tipo e usuario
     *
     * @param user Usuario obrigatorio Obrigatorio
     * @param tipo Tipo DESPESA_FINANCEIRA ou RECEITA_FINANCEIRA,
     * Obrigatorio
     * @return Lista com contas no padrao, lista vazia se nao achar.
     */
    List<Procedimento> buscarPorUsuarioTipoProcedimento(
            final Usuario user, final TipoProcedimento tipo);

    /**
     * Retorna uma lista com Procedimento que são
     * DespesaParceladaProcedimento pode ser feito cast sem problemas.
     *
     * @param usuario Proprietário ou conjuge do proprietario. Obrigatório
     * @param cartao Cartao onde foi feito pagamento. Opcional.
     * @param statusPagamento Status do pagamento. Opcional
     * @param dataI Data Inicial do vendimento. Opcional
     * @param dataF Data Final do vendimento. Opcional
     * @return DespesasParceladas no perfil ou uma lista vazia se não
     * encontrar.
     */
    List<Procedimento> buscarPorUsuarioCartaoStatusData(
            final Usuario usuario, final CartaoCredito cartao,
            final StatusPagamento statusPagamento,
            final Date dataI, final Date dataF);

    /**
     * Retorna uma lista com Procedimento.
     *
     * @param usuario Proprietário ou conjuge do proprietario. Obrigatório
     * @param statusPagamento Status do pagamento. Opcional
     * @param dataI Data Inicial do vendimento. Opcional
     * @param dataF Data Final do vendimento. Opcional
     * @return Procedimento no perfil ou uma lista vazia se não encontrar.
     */
    List<Procedimento> buscarStatusUsrTipo(Usuario user, StatusPagamento status,
            TipoProcedimento tipo);

    /**
     * Atualiza o procedimento que tenha sido criado pela agenda informada.
     * Somente procedimento não pagos serão atualizados. Todos os campos
     * serão utilizados.
     *
     * @param agenda Filtro
     * @throws NegocioException
     */
    void removerProcedimentos(final AgendaProcedimentoFixo agenda) throws NegocioException;

    /**
     * Conta procedimetos de acordo com filtros.
     *
     * @param usr Obrigatório.
     * @param detalhe Opcional.
     * @param status Opcional
     * @param observacao Opcional.
     * @param dataMovimentacao Opcional.
     * @return
     */
    Long contarProcedimentos(final Usuario usr, final DetalheTipoProcedimento detalhe,
            final StatusPagamento status, String observacao, Date dataMovimentacao);

    /**
     * Busca procedimentos de acordo com pefil, paginado.
     *
     * @param usr obrigatorio.
     * @param detalhe Opcional.
     * @param status Opcional.
     * @param observacao Opcional.
     * @param dataMovimentacao Opcional.
     * @param range obrigatorio.
     * @return Lista com procedimentos, vazia se não achar.
     */
    List<Procedimento> buscarProcedimentos(final Usuario usr, final DetalheTipoProcedimento detalhe,
            final StatusPagamento status, String observacao, Date dataMovimentacao, int[] range);

    /**
     * Remove todos os parcelamentos com ID informado.
     *
     * @param idParcelamento Obrigatorio.
     * @throws NegocioException
     */
    void removerParcelamento(String idParcelamento) throws NegocioException;

    /**
     * Conta procedimetos de acordo com filtros. Sem o que for vinculado a
     * um cartão de crédito.
     *
     * @param usr Obrigatório.
     * @param detalhe Opcional.
     * @param status Opcional
     * @param observacao Opcional.
     * @param dataMovimentacao Opcional.
     * @return
     */
    Long contarProcedimentosSemCartao(final Usuario usr, final DetalheTipoProcedimento detalhe,
            final StatusPagamento status, String observacao, Date dataMovimentacao);

    /**
     * Busca procedimentos de acordo com pefil, paginado. Sem o que for
     * vinculado a um cartão de crédito.
     *
     * @param usr obrigatorio.
     * @param detalhe Opcional.
     * @param status Opcional.
     * @param observacao Opcional.
     * @param dataMovimentacao Opcional.
     * @param range obrigatorio.
     * @return Lista com procedimentos, vazia se não achar.
     */
    List<Procedimento> buscarProcedimentosSemCartao(final Usuario usr, final DetalheTipoProcedimento detalhe,
            final StatusPagamento status, String observacao, Date dataMovimentacao, int[] range);

    /**
     * Retorna um MinMaxDateDTO para intervalo de datas.
     *
     * @param cartao Opcional.
     * @param status Opcional.
     * @param usr Obrigatorio.
     * @return
     */
    MinMaxDateDTO buscarIntervalodDatas(final CartaoCredito cartao,
            final StatusPagamento status, final Usuario usr);

    /**
     * Busca Despesas por intervalo de datas.
     *
     * @param usr obrigatorio
     * @param cartao obrigatorio
     * @param status opcional
     * @param intervalo obrigatorio intervalo[0] inicial intervalo[1] final
     * @return
     */
    List<DespesaProcedimento> buscarDespesaIntervalo(final Usuario usr,
            final CartaoCredito cartao, final StatusPagamento status,
            final Date[] intervalo);

    /**
     * Busca despesas por filtros paginado.
     *
     * @param usr Obrigatorio
     * @param detalhe Opcional
     * @param status Opcional
     * @param observacao Opcional
     * @param dataMovimentacao Opcional
     * @param cartao Opcional
     * @param range Obrigatorio.
     * @return
     */
    List<DespesaProcedimento> buscarDespesas(final Usuario usr, final DetalheTipoProcedimento detalhe,
            final StatusPagamento status, String observacao, Date dataMovimentacao,
            final CartaoCredito cartao, int[] range);

    /**
     * Conta as despesas por filtros.
     *
     * @param usr Obrigatorio
     * @param detalhe Opcional
     * @param status Opcional
     * @param observacao Opcional
     * @param dataMovimentacao Opcional
     * @param cartao Opcional.
     * @return
     */
    Long contarDespesas(final Usuario usr, final DetalheTipoProcedimento detalhe,
            final StatusPagamento status, String observacao, Date dataMovimentacao,
            CartaoCredito cartao);

    /**
     * Busca as despesas de um usuário que foram realizadas em um cartão de
     * crédito.
     *
     * @param usr Obrigatorio
     * @param dataI Obrigatorio
     * @param dataF Obrigatorio
     * @return Lista de despesas realizas em cartões no periodo solicitado.
     */
    List<DespesaProcedimento> buscarDespesasCartao(final Usuario usr,
            final Date dataI, final Date dataF);
    
    /**
     * Busca despesas com filtros passados.
     * @param usr Obrigatório
     * @param intervalo Obrigatorio, duas datas, posicao 0 ini posicao 1 fim.
     * @param detalhe Opcional, se nul trará todos.
     * @return 
     */
    public List<DespesaProcedimento> pesquisaDetalheProcedimento(final Usuario usr,
            final Date[] intervalo, DetalheProcedimento detalhe);
}
