/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans;

import br.com.financeiro.ejbbeans.interfaces.ContaPagarReceberLocal;
import br.com.financeiro.ejbbeans.interfaces.GrupoFinanceiroLocal;
import br.com.financeiro.ejbbeans.interfaces.MovimentacaoFinanceiraLocal;
import br.com.financeiro.ejbbeans.interfaces.ReceitaFinanceiraLocal;
import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.ContaReceber;
import br.com.financeiro.entidades.MovimentacaoFinanceira;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.comparator.ContaComparator;
import br.com.financeiro.entidades.enums.FormaRecebimento;
import br.com.financeiro.entidades.enums.StatusPagamento;
import br.com.financeiro.entidades.enums.StatusReceita;
import br.com.financeiro.entidades.enums.TipoMovimentacao;
import br.com.financeiro.entidades.interfaces.Conta;
import br.com.financeiro.excecoes.MovimentacaoFinanceiraException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Caso futuramente seja criado uma nova implementação para a Interface Conta
 * Sendo um novo tipo de conta, que não seja receita nem pagamento
 * Aconselho a fazer refatoramento do código atual.
 * Acrescentar alguns setter na interface Conta
 * E realizar toda a atualização a nível de interface!
 * @author gbvbahia
 */
@Stateless
public class MovimentacaoFinanceiraBean implements MovimentacaoFinanceiraLocal {
    @EJB
    private GrupoFinanceiroLocal grupoFinanceiroBean;

    @EJB
    private ContaPagarReceberLocal contaPagarReceberBean;
    @EJB
    private ReceitaFinanceiraLocal receitaFinanceiraBean;
    @PersistenceContext(name = "financeiro")
    private EntityManager em;

    @Override
    public List<Conta> buscarContasNaoVencidas(User user, Date init, int maxRows) {
        List<Conta> toReturn = new ArrayList<Conta>();
        toReturn.addAll(contaPagarReceberBean.buscarContasNaoPagas(user, init, maxRows));
        toReturn.addAll(receitaFinanceiraBean.buscarReceitasNaoRecebidas(user, init, maxRows));
        Collections.sort(toReturn, new ContaComparator());
        return toReturn;
    }

    @Override
    public List<Conta> buscarContasPagas(User user, Date dataInit, Date dataFim) {
        List<Conta> toReturn = new ArrayList<Conta>();
        toReturn.addAll(contaPagarReceberBean.buscarContasPorData(dataInit, dataFim, StatusPagamento.PAGA, user));
        toReturn.addAll(receitaFinanceiraBean.buscarReceitasPorData(user, dataInit, dataFim, StatusReceita.RECEBIDA));
        Collections.sort(toReturn, new ContaComparator());
        return toReturn;
    }

    @Override
    public List<Conta> buscarContasPagas(User user, Date dataInit, int maxResult) {
        List<Conta> toReturn = new ArrayList<Conta>();
        toReturn.addAll(contaPagarReceberBean.buscarContasPagas(user, dataInit, maxResult));
        toReturn.addAll(receitaFinanceiraBean.buscarReceitasRecebidas(user, dataInit, maxResult));
        Collections.sort(toReturn, new ContaComparator());
        return toReturn;
    }


    @Override
    public void criarMovimentacaoFinanceira(Conta conta) throws MovimentacaoFinanceiraException {
        MovimentacaoFinanceira movimentacaoFinanceira = conta.getContaMovimentacaoFinanceira();
        ContaBancaria contaBancaria = movimentacaoFinanceira.getContaBancaria();
        movimentacaoFinanceira.setDataMovimentacao(Calendar.getInstance().getTime());
        movimentacaoFinanceira.setSaldoAnterior(contaBancaria.getSaldo());
        ContaPagar contaPagar = null;
        ContaReceber contaReceber = null;
        if (conta.getContaTipoMovimentacao().equals(TipoMovimentacao.DEPOSITO)) {
            contaReceber = (ContaReceber) conta;
            contaBancaria.setSaldo(contaBancaria.getSaldo() + conta.getContaValor());
            movimentacaoFinanceira.setSaldoPosterior(contaBancaria.getSaldo());
            movimentacaoFinanceira.setTipoMovimentacao(conta.getContaTipoMovimentacao());
            movimentacaoFinanceira.setContaReceber(contaReceber);
            movimentacaoFinanceira.setContaBancaria(contaBancaria);
            contaReceber.setMovimentacaoFinanceira(movimentacaoFinanceira);
            contaReceber.setStatusReceita(StatusReceita.RECEBIDA);
            em.merge(contaReceber);
            em.merge(contaBancaria);
        } else if (conta.getContaTipoMovimentacao().equals(TipoMovimentacao.RETIRADA)) {
            contaPagar = (ContaPagar) conta;
            contaBancaria.setSaldo(contaBancaria.getSaldo() - conta.getContaValor());
            movimentacaoFinanceira.setSaldoPosterior(contaBancaria.getSaldo());
            movimentacaoFinanceira.setTipoMovimentacao(conta.getContaTipoMovimentacao());
            movimentacaoFinanceira.setContaPagar(contaPagar);
            movimentacaoFinanceira.setContaBancaria(contaBancaria);
            contaPagar.setMovimentacaoFinanceira(movimentacaoFinanceira);
            contaPagar.setStatusPagamento(StatusPagamento.PAGA);
            em.merge(contaPagar);
            em.merge(contaBancaria);
            if(contaPagar.getContaPara() != null){
                if(contaPagar.getContaPara().equals(contaBancaria)){
                    throw new MovimentacaoFinanceiraException("Você está tentando retirar dinheiro de uma conta e transferindo para a mesma conta. Recadastre a conta informando uma outra conta de origem ou de destino.");
                }
                this.completaTrasnferencia(contaPagar.getContaPara(), contaPagar);
            }
        }
        em.flush();
    }
    //Grupo = Transferência Automática
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    private void completaTrasnferencia(ContaBancaria contaBancaria, ContaPagar contaPagar){
        ContaReceber contaReceber = new ContaReceber();
        contaReceber.setContaValor(contaPagar.getContaValor());
        contaReceber.setDataPagamento(new Date());
        contaReceber.setFormaRecebimento(FormaRecebimento.TRASFERENCIA_ELETRONICA);
        contaReceber.setGrupoReceita(grupoFinanceiroBean.buscarGrupoReceitaPorNome("Transferência Automática", null));
        contaReceber.setObservacao(contaPagar.getObservacao());
        contaReceber.setParcelaAtual(contaPagar.getContaParcelaAtual());
        contaReceber.setParcelaTotal(contaPagar.getContaParcelaTotal());
        contaReceber.setStatusReceita(StatusReceita.NAO_RECEBIDA);
        contaReceber.setUser(contaPagar.getUser());
        this.receitaFinanceiraBean.salvarReceitaFinanceira(contaReceber);
        MovimentacaoFinanceira movimentacaoFinanceira = new MovimentacaoFinanceira();
        movimentacaoFinanceira.setContaBancaria(contaBancaria);
        contaReceber.setMovimentacaoFinanceira(movimentacaoFinanceira);
        this.criarMovimentacaoFinanceira(contaReceber);
    }

    @Override
    public void removerMovimentacaoFinanceira(Conta conta) throws MovimentacaoFinanceiraException {
        MovimentacaoFinanceira movimentacaoFinanceira = conta.getContaMovimentacaoFinanceira();
        ContaBancaria contaBancaria = movimentacaoFinanceira.getContaBancaria();
        if (conta.getContaTipoMovimentacao().equals(TipoMovimentacao.DEPOSITO)) {
            contaBancaria.setSaldo(contaBancaria.getSaldo() - conta.getContaValor());
            ContaReceber contaReceber = (ContaReceber) conta;
            contaReceber.setMovimentacaoFinanceira(null);
            contaReceber.setStatusReceita(StatusReceita.NAO_RECEBIDA);
            em.merge(contaReceber);
            em.merge(contaBancaria);
            movimentacaoFinanceira = em.merge(movimentacaoFinanceira);
            em.remove(movimentacaoFinanceira);
        } else if (conta.getContaTipoMovimentacao().equals(TipoMovimentacao.RETIRADA)) {
            contaBancaria.setSaldo(contaBancaria.getSaldo() + conta.getContaValor());
            ContaPagar contaPagar = (ContaPagar) conta;
            contaPagar.setMovimentacaoFinanceira(null);
            contaPagar.setStatusPagamento(StatusPagamento.NAO_PAGA);
            em.merge(contaPagar);
            em.merge(contaBancaria);
            movimentacaoFinanceira = em.merge(movimentacaoFinanceira);
            em.remove(movimentacaoFinanceira);
        }
        em.flush();
    }
}
