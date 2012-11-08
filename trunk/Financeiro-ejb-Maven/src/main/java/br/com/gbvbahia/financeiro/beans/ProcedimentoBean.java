/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaParceladaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.financeiro.utils.I18N;
import br.com.gbvbahia.financeiro.utils.StringBeanUtils;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Bean de entidade para Procedimento e todas suas subclasses.
 *
 * @since v.3 08/04/2012
 * @author Guilherme
 */
@Stateless
@Interceptors({LogTime.class})
public class ProcedimentoBean
        extends AbstractFacade<Procedimento, Long>
        implements ProcedimentoFacade {

    /**
     * Unidade de persistência <i>jdbc/money</i>.
     */
    @PersistenceContext(unitName = UtilBeans.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Construtor padrão que passa o tipo de classe para AbstractFacade.
     */
    public ProcedimentoBean() {
        super(Procedimento.class);
    }

    @Override
    public void create(Procedimento entity) throws NegocioException {
        if (entity instanceof DespesaParceladaProcedimento) {
            throw new NegocioException(I18N.getMsg("despesaParProcedCreate"));
        }
        super.create(entity);
    }

    @Override
    public void create(final Procedimento entity,
            final int parTotal, final int parAtual,
            final CartaoCredito cartao) throws NegocioException {
        UtilBeans.checkNull(entity);
        validarParcelas(parTotal, parAtual);
        int parcelaAtual = parAtual;
        String idU = idParcelamento(entity);
        int incrementen = 0;
        while (parcelaAtual <= parTotal) {
            DespesaParceladaProcedimento dpp =
                    criarDespesaParcelada(entity, parcelaAtual,
                    parTotal, cartao, incrementen++);
            if (parcelaAtual++ > parAtual) {
                dpp.setStatusPagamento(StatusPagamento.NAO_PAGA);
            }
            dpp.setIdentificador(idU);
            super.create(dpp);
        }

    }

    @Override
    public List<Procedimento> buscarStatusUsrTipo(
            Usuario user, StatusPagamento status,
            TipoProcedimento tipo) {
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", user);
        parans.put("status", status == null ? StatusPagamento.NAO_PAGA : status);
        parans.put("status2", status == null ? "todos" : "filtro");
        parans.put("tipoProcedimento2", tipo == null ? "todos" : "filtro");
        parans.put("tipoProcedimento", tipo == null ? TipoProcedimento.DESPESA_FINANCEIRA : tipo);
        return listPesqParam("Procedimento.buscarStatusUsrTipoProcedimento", parans);
    }

    @Override
    public List<Procedimento> buscarPorUsuarioCartaoStatusTipo(
            Usuario user, CartaoCredito cartao, StatusPagamento status,
            TipoProcedimento tipo) {
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", user);
        parans.put("status", status == null ? StatusPagamento.NAO_PAGA : status);
        parans.put("status2", status == null ? "todos" : "filtro");
        parans.put("cartao", cartao);
        parans.put("cartao2", cartao == null ? "todos" : "filtro");
        parans.put("tipoProcedimento2", tipo == null ? "todos" : "filtro");
        parans.put("tipoProcedimento", tipo == null ? TipoProcedimento.DESPESA_FINANCEIRA : tipo);
        return listPesqParam("Procedimento.buscarCartaoStatusUsrTipoProcedimento", parans);
    }

    @Override
    public List<Procedimento> buscarPorUsuarioTipoProcedimento(
            final Usuario user, final TipoProcedimento tipo) {
        UtilBeans.checkNull(user, tipo);
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", user);
        parans.put("tipoProcedimento", tipo);
        return listPesqParam("Procedimento.TipoProcedimento", parans);
    }

    /**
     * Retorna uma lista com Procedimento que são DespesaParceladaProcedimento
     * pode ser feito cast sem problemas.
     *
     * @param usuario Proprietário ou conjuge do proprietario. Obrigatório
     * @param cartao Cartao onde foi feito pagamento. Opcional.
     * @param statusPagamento Status do pagamento. Opcional
     * @param dataI Data Inicial do vendimento. Opcional
     * @param dataF Data Final do vendimento. Opcional
     * @return DespesasParceladas no perfil ou uma lista vazia se não
     * encontrar.
     */
    @Override
    public List<Procedimento> buscarPorUsuarioCartaoStatusData(
            final Usuario usuario, final CartaoCredito cartao,
            final StatusPagamento statusPagamento,
            final Date dataI, final Date dataF) {
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", usuario);
        parans.put("status", statusPagamento == null ? StatusPagamento.NAO_PAGA : statusPagamento);
        parans.put("status2", statusPagamento == null ? "todos" : "filtro");
        parans.put("cartao", cartao);
        parans.put("cartao2", cartao == null ? "todos" : "filtro");
        parans.put("dataI", DateUtils.zerarHora(dataI));
        parans.put("dataI2", dataI == null ? "todos" : "filtro");
        parans.put("dataF", DateUtils.zerarHora(dataF));
        parans.put("dataF2", dataF == null ? "todos" : "filtro");
        return listPesqParam("DespesaParcelada.cartaoStatusUsuarioData", parans);
    }

    /**
     * Valida as parcelas:<br> Total de parcelas não pode ser menor que 2.<br>
     * A parcela atual não pode ser menor que 1.<br> O total de parcelas não
     * pode ser menor que a parcela atual.<br>
     *
     * @param parAtual Parcela atual.
     * @param parTotal Parcela total.
     * @throws NegocioException se as parcelas não estiverem em conformidade.
     */
    private void validarParcelas(final int parTotal,
            final int parAtual) throws NegocioException {
        if (parTotal < 2 || parAtual < 1 || parTotal < parAtual) {
            throw new NegocioException("parcelasInvalidas",
                    new String[]{parAtual + "", parTotal + ""});
        }
    }

    /**
     * Cria um ID único para as parcelas geradas.
     *
     * @param entity
     * @return String Id unico.
     */
    private String idParcelamento(final Procedimento entity) {
        final String idU =
                StringBeanUtils.getIdentificadorUnico(
                entity.getUsuario().getUserId(),
                entity.getDataVencimento());
        return idU;
    }

    /**
     * Cria a despesa parcelada de acordo com os parâmetros.
     *
     * @param entity Procedimento de origem. Obrigatório.
     * @param parAtual Parcela atual. Obrigatório.
     * @param parTotal Parcela total. Obrigatório.
     * @param cartao Cartão onde parcelamento foi realizado. Opcional.
     * @return DespesaParceladaProcedimento crada.
     */
    private DespesaParceladaProcedimento criarDespesaParcelada(
            final Procedimento entity, final int parAtual,
            final int parTotal, final CartaoCredito cartao,
            final int incrementData) {
        DespesaParceladaProcedimento dpp =
                new DespesaParceladaProcedimento(entity);
        dpp.setParcelaAtual(parAtual);
        dpp.setParcelaTotal(parTotal);
        if (cartao != null) {
            dpp.setCartaoCredito(cartao);
        }
        dpp.setDataVencimento(DateUtils.incrementar(dpp.getDataVencimento(),
                incrementData, Calendar.MONTH));
        return dpp;
    }
}
