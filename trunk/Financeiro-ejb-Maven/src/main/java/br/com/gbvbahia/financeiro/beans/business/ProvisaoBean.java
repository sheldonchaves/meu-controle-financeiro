/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.business.interfaces.ProvisaoBusiness;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.ClassificacaoProcedimento;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.utils.I18N;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Privisão bean é um bean responsavel pelo privisionamento das despesas
 * fixas. Ele é o encarregago de chamar a AgendaProcedimentoFixo e criar as
 * Contas/Receitas para pagamento/recebimento.<br> Todo o trabalho deve ser
 * realizado com associação de beans de entidas por ser um bean de negócio,
 * provisão bean não pode ter um EntityManager, conceitualmente
 * falando.<br>
 *
 * @since 2012/04/14
 * @author Guilherme
 */
@Stateless
@Interceptors({LogTime.class})
public class ProvisaoBean implements ProvisaoBusiness {

    /**
     * Define em anos o quanto de provisão deve ser realizado.
     */
    protected static final int PROVISAO_ANOS = 1;
    /**
     * Bean da Entidade Procedimento.
     */
    @EJB
    private ProcedimentoFacade procedimentoBean;
    /**
     * Bean da Entidade AgendaProcedimentoFixo.
     */
    @EJB
    private AgendaProcedimentoFixoFacade agendaBean;

    @Override
    public void criarAgendaEProvisionar(AgendaProcedimentoFixo agenda) throws NegocioException {
        if (agenda.getDataPrimeiroVencimento().compareTo(br.com.gbvbahia.financeiro.utils.DateUtils.zerarHora(new Date())) < 0) {
            throw new NegocioException("DataInicialMenorHoje",
                    new String[]{br.com.gbvbahia.financeiro.utils.DateUtils.getDateToString(agenda.getDataPrimeiroVencimento()),
                        br.com.gbvbahia.financeiro.utils.DateUtils.getDateToString(new Date())});
        }
        agendaBean.create(agenda);
        if (agenda.isAtiva()) {
            provisionar(agenda);
        }
    }

    @Override
    public void atualizarProvisao(AgendaProcedimentoFixo agenda) throws NegocioException {
        agendaBean.update(agenda);
        if (agenda.isAtiva()) {
            procedimentoBean.removerProcedimentos(agenda);
            provisionar(agenda);
        }
    }

    @Override
    public void alterarStatusProvisao(AgendaProcedimentoFixo agenda) throws NegocioException {
        agenda.setAtiva(!agenda.isAtiva());
        agendaBean.update(agenda);
        if (!agenda.isAtiva()) {
            procedimentoBean.removerProcedimentos(agenda);
        } else {
            provisionar(agenda);
        }
    }

    @Override
    public void provisionar(final AgendaProcedimentoFixo agenda) {
        UtilBeans.checkNull(agenda);
        Date lastDate = agendaBean.buscarUltimaData(agenda);
        if (lastDate == null
                || lastDate.before(br.com.gbvbahia.financeiro.utils.DateUtils.zerarHora(new Date()))) {
            lastDate = agenda.getDataPrimeiroVencimento();
        } else {
            lastDate = incrementarData(agenda, lastDate);
        }
        lastDate = garanteDataFutura(lastDate, agenda);
        Procedimento procedimento = makeProcedimento(agenda, lastDate);
        try {
            criarProvisoes(procedimento, agenda);
        } catch (NegocioException e) {
            //Aqui negócio exception seria por erro de desenvolvimento.
            Logger.getLogger(this.getClass().getName()).log(
                    Level.WARNING, "*** Erro de Desenvolvimento ***");
            throw new RuntimeException(e);
        }
    }

    /**
     * Garante que a data de vencimento do primeiro procedimento será
     * futura.
     *
     * @param dataMovimentacao Data vencimento da conta.
     * @param agenda Agenda Procedimento.
     * @return Data no futuro.
     */
    private Date garanteDataFutura(final Date dataMovimentacao,
            final AgendaProcedimentoFixo agenda) {
        Date agora = new Date();
        agora = br.com.gbvbahia.financeiro.utils.DateUtils.zerarHora(agora);
        Date toReturn = dataMovimentacao;
        while (toReturn.before(agora)) {
            toReturn = incrementarData(agenda, toReturn);
        }
        return toReturn;
    }

    /**
     * Incrementa a dataControle de acordo com a agenda.
     *
     * @param agenda Agenda que sabe o quanto incrementar.
     * @param dataControle Data que será incrementada. Um novo objeto será
     * retornado.
     * @return Nova data incrementada.
     * @throws IllegalArgumentException Se agenda não tiver informações
     * corretas sobre o Periodo.
     */
    private Date incrementarData(final AgendaProcedimentoFixo agenda,
            final Date dataControle) throws IllegalArgumentException {
        Date toReturn = null;
        switch (agenda.getPeriodo()) {
            case ANOS:
                toReturn = DateUtils.addYears(dataControle,
                        agenda.getQuantidadePeriodo());
                break;
            case MESES:
                toReturn = DateUtils.addMonths(dataControle,
                        agenda.getQuantidadePeriodo());
                break;
            case DIAS:
                toReturn = DateUtils.addDays(dataControle,
                        agenda.getQuantidadePeriodo());
                break;
            default:
                throw new IllegalArgumentException(
                        I18N.getMsg("peridoInvalido",
                        "ProvisaoBean.criarProvisoes"));
        }
        return toReturn;
    }

    /**
     * Cria o Procedimento, Receita ou Despesa com base na agenda criada.
     *
     * @param agenda Agenda que está gerando o procedimento.
     * @param dataMovimentacao Data do vencimento do procedimento.
     * @return Procedimento pronto para ser gravado no BD.
     */
    private Procedimento makeProcedimento(AgendaProcedimentoFixo agenda,
            Date dataMovimentacao) {
        Procedimento toReturn;
        if (agenda.getDetalhe().getTipo().equals(TipoProcedimento.DESPESA_FINANCEIRA)) {
            toReturn = new DespesaProcedimento(agenda.getCartaoCredito(), dataMovimentacao);
        } else {
            toReturn = new Procedimento();
        }
        toReturn.setAgenda(agenda);
        toReturn.setClassificacaoProcedimento(ClassificacaoProcedimento.FIXA);
        toReturn.setDataMovimentacao(dataMovimentacao);
        toReturn.setDetalhe(agenda.getDetalhe());
        toReturn.setObservacao(agenda.getObservacao());
        toReturn.setStatusPagamento(StatusPagamento.NAO_PAGA);
        toReturn.setUsuario(agenda.getUsuario());
        toReturn.setValorEstimado(agenda.getValorFixo());
        return toReturn;
    }

    /**
     * Responsável por criar os procedimentos, ele não realiza nenhuma
     * validação, ou checagem de pré-existência, somente cria.<br> Antes de
     * realizar a solicitação de criação todos os checks devem ser
     * realizado.s
     *
     * @param procedimento O primeiro Procedimento a ser gravado.
     * @param agenda A agenda que contém a periodicidade.
     * @throws NegocioException Caso ao salvar o Procedimento alguma
     * violação as regras ocorra.
     */
    private void criarProvisoes(final Procedimento procedimento,
            final AgendaProcedimentoFixo agenda)
            throws NegocioException {
        Procedimento toCreate = procedimento;
        Date HOJE_MAIS_PROVISAO_ANOS = DateUtils.addYears(new Date(),
                PROVISAO_ANOS);
        Date dataControle = toCreate.getDataMovimentacao();
        while (dataControle.before(HOJE_MAIS_PROVISAO_ANOS)) {
            procedimentoBean.create(toCreate);
            dataControle = incrementarData(agenda, dataControle);
            toCreate = makeProcedimento(agenda, dataControle);
        }
    }
}
