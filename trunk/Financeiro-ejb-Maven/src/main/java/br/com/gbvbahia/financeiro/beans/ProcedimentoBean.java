/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaParceladaProcedimento;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.Procedimento;
import br.com.gbvbahia.financeiro.utils.I18N;
import br.com.gbvbahia.financeiro.utils.StringBeanUtils;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Bean de entidade para Procedimento e todas suas subclasses.
 *
 * @since v.3 08/04/2012
 * @author Guilherme
 */
@Stateless
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
     * Construtor padrão que passa o tipo de classe para
     * AbstractFacade.
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
        while (parcelaAtual <= parTotal) {
            DespesaParceladaProcedimento dpp =
                    criarDespesaParcelada(entity, parcelaAtual,
                    parTotal, cartao);
            if (parcelaAtual++ > parAtual) {
                dpp.setStatusPagamento(StatusPagamento.NAO_PAGA);
            }
            dpp.setIdentificador(idU);
            super.create(dpp);
        }

    }

    @Override
    public List<DespesaProcedimento> buscarDespesaProcedimento(
            final CartaoCredito cartao, final StatusPagamento status,
            final Usuario usuario) {
        UtilBeans.checkNull(usuario);
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", usuario);
        //Se null StatusPagamento.NAO_PAGA é ignorado
        parans.put("status", status == null
                ? StatusPagamento.NAO_PAGA : status);
        //Se null todos = todos e retorna tudo.
        parans.put("status2", status == null ? "todos" : "filtro");
        parans.put("cartao", cartao);
        List<DespesaProcedimento> toReturn =
                new ArrayList<DespesaProcedimento>();
        final List<Procedimento> despesas =
                listPesqParam("Despesa.CartaoStatusUsuario", parans);
        for (Procedimento p : despesas) {
            toReturn.add((DespesaProcedimento) p);
        }
        return toReturn;
    }

    @Override
    public List<Procedimento> buscarPorTipoProcedimento(
            final Usuario user, final TipoProcedimento tipo) {
        UtilBeans.checkNull(user, tipo);
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", user);
        parans.put("tipoProcedimento", tipo);
        return listPesqParam("Procedimento.TipoProcedimento", parans);
    }

    /**
     * Valida as parcelas:<br> Total de parcelas não pode ser menor
     * que 2.<br> A parcela atual não pode ser menor que 1.<br> O
     * total de parcelas não pode ser menor que a parcela atual.<br>
     *
     * @param parAtual Parcela atual.
     * @param parTotal Parcela total.
     * @throws NegocioException se as parcelas não estiverem em
     * conformidade.
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
            final int parTotal, final CartaoCredito cartao) {
        DespesaParceladaProcedimento dpp =
                new DespesaParceladaProcedimento(entity);
        dpp.setParcelaAtual(parAtual);
        dpp.setParcelaTotal(parTotal);
        if (cartao != null) {
            dpp.setCartaoCredito(cartao);
        }
        return dpp;
    }
}
