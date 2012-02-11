/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.exceptions.MovimentacaoFinanceiraException;
import br.com.money.exceptions.ReceitaDividaException;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.modelos.Usuario;
import br.com.money.utils.UtilBeans;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*;

/**
 *
 * @author Guilherme
 */
@Stateless
public class ReceitaDividaBean extends AbstractFacade<ReceitaDivida> implements ReceitaDividaBeanLocal {

    @Override
    protected EntityManager getEntityManager() {
        return this.manager;
    }

    public ReceitaDividaBean() {
        super(ReceitaDivida.class);
    }

    @EJB
    private MovimentacaoFinanceiraBeanLocal movimentacaoFinanceiraBean;
    @EJB(beanName = "receitaDividaValidador")
    private ValidadorInterface receitaDividaValidador;
    @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;

    @Override
    public void salvarReceitaDivida(ReceitaDivida conta) throws ValidacaoException {
        if (conta.getIdentificador() == null) {
            String identificador = UtilBeans.getIdentificadorUnico(conta.getUsuario().getId(), conta.getDataVencimento());
            conta.setIdentificador(identificador);
        }
        this.receitaDividaValidador.validar(conta, this, null);
        if (conta.getId() == null) {
            manager.persist(conta);
        } else {
            manager.merge(conta);
        }
        manager.flush();
    }

    /**
     * Salva a conta e suas as parcelas
     * @param ReceitaDivida conta Uma receitaDivida válida
     * @param parcelas Um valor maior ou igual a 1.
     * @param contaBancaria
     * @param statusParcelas
     * @throws ValidacaoException 
     */
    @Override
    public void salvarReceitaDivida(ReceitaDivida conta, int parcelas, boolean salvarParcelas, ContaBancaria contaBancaria, StatusPagamento statusParcelas) throws ValidacaoException {
        if (salvarParcelas && parcelas < 1) {
            throw new MovimentacaoFinanceiraException("movimentacaoFinanceiraqtdadeParcelasError", "Parcelas");
        }
        if ((statusParcelas.equals(StatusPagamento.PAGA) || conta.getStatusPagamento().equals(StatusPagamento.PAGA)) && contaBancaria == null) {
            throw new MovimentacaoFinanceiraException("movimentacaoFinanceiraContaMovimentadaNula", "Conta Bancaria");
        }
        String identificador = UtilBeans.getIdentificadorUnico(conta.getUsuario().getId(), conta.getDataVencimento());
        conta.setIdentificador(identificador);
        this.salvarReceitaDivida(conta);
        if (conta.getStatusPagamento().equals(StatusPagamento.PAGA)) {
            this.movimentacaoFinanceiraBean.salvarMovimentacaoFinanceira(contaBancaria, conta);
        }
        if (salvarParcelas) {
            ReceitaDivida rd = null;
            for (rd = conta; (rd = aumentaParcela(rd, statusParcelas)) != null;) {
                this.salvarReceitaDivida(rd);
                if (rd.getStatusPagamento().equals(StatusPagamento.PAGA)) {
                    this.movimentacaoFinanceiraBean.salvarMovimentacaoFinanceira(contaBancaria, rd);
                }
            }
        }
    }

    /**
     *Default, somente mesmo pacote.
     * @param contaPagar
     * @return
     */
    private ReceitaDivida aumentaParcela(ReceitaDivida contaPagar, StatusPagamento status) {
        if (contaPagar.getParcelaTotal() > contaPagar.getParcelaAtual()) {
            ReceitaDivida toReturn = new ReceitaDivida();
            toReturn.setDataVencimento(UtilBeans.aumentaMesDate(contaPagar.getDataVencimento(), 1));
            toReturn.setJuros(contaPagar.getJuros());
            toReturn.setObservacao(contaPagar.getObservacao());
            toReturn.setParcelaAtual(contaPagar.getParcelaAtual() + 1);
            toReturn.setParcelaTotal(contaPagar.getParcelaTotal());
            toReturn.setStatusPagamento(status);
            toReturn.setUsuario(contaPagar.getUsuario());
            toReturn.setValor(contaPagar.getValor());
            toReturn.setIdentificador(contaPagar.getIdentificador());
            toReturn.setStatusPagamento(contaPagar.getStatusPagamento());
            toReturn.setTipoMovimentacao(contaPagar.getTipoMovimentacao());
            toReturn.setDetalheMovimentacao(contaPagar.getDetalheMovimentacao());
            return toReturn;
        } else {
            return null;
        }
    }

    @Override
    public List<ReceitaDivida> buscarReceitaDividasPorUsuarioStatusPaginada(int posicaoInicial, int tamanho,
            Usuario usuario, StatusPagamento statusPagamento, TipoMovimentacao tipoMovimentacao) {
        Query q = manager.createNamedQuery("ReceitaDividaBean.buscarReceitaDividasPorUsuarioStatusPaginada");
        q.setMaxResults(tamanho);
        q.setFirstResult(posicaoInicial);
        q.setParameter("usuario", usuario);
        q.setParameter("statusPagamento", statusPagamento);
        q.setParameter("tipoMovimentacao", tipoMovimentacao);
        return q.getResultList();
    }

    @Override
    public List<ReceitaDivida> buscarReceitaDividasPorUsuarioStatusPaginada(int posicaoInicial, int tamanho,
            Usuario usuario, StatusPagamento statusPagamento) {
        Query q = manager.createNamedQuery("ReceitaDividaBean.buscarReceitaDividasPorUsuarioStatusPaginada2");
        q.setMaxResults(tamanho);
        q.setFirstResult(posicaoInicial);
        q.setParameter("usuario", usuario);
        q.setParameter("statusPagamento", statusPagamento);
        return q.getResultList();
    }

    @Override
    public Integer buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(Usuario usuario, StatusPagamento statusPagamento) {
        Query q = manager.createNamedQuery("ReceitaDividaBean.buscarQutdadeReceitaDividasPorUsuarioStatusPaginada2");
        q.setParameter("usuario", usuario);
        q.setParameter("statusPagamento", statusPagamento);
        Long toReturn = (Long) q.getSingleResult();
        return toReturn.intValue();
    }

    @Override
    public Integer buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(Usuario usuario, StatusPagamento statusPagamento, TipoMovimentacao tipoMovimentacao) {
        Query q = manager.createNamedQuery("ReceitaDividaBean.buscarQutdadeReceitaDividasPorUsuarioStatusPaginada");
        q.setParameter("usuario", usuario);
        q.setParameter("statusPagamento", statusPagamento);
        q.setParameter("tipoMovimentacao", tipoMovimentacao);
        Long toReturn = (Long) q.getSingleResult();
        return toReturn.intValue();
    }

    @Override
    public void apagarReceitaDivida(ReceitaDivida receitaDivida, boolean deleteParcelas) {
        ReceitaDivida rd = manager.find(ReceitaDivida.class, receitaDivida.getId());
        if (rd.getStatusPagamento().equals(StatusPagamento.PAGA)) {
            throw new ReceitaDividaException("deleteReceitaDividaPaga");
        }
        manager.remove(rd);
        if (deleteParcelas) {
            Query q = manager.createNamedQuery("ReceitaDividaBean.apagarReceitaDivida");
            q.setParameter("usuario", rd.getUsuario());
            q.setParameter("statusPagamento", StatusPagamento.NAO_PAGA);
            q.setParameter("identificador", rd.getIdentificador());
            q.setParameter("parcelaAtual", rd.getParcelaAtual());
            q.executeUpdate();
        }
        manager.flush();
    }

    @Override
    public List<ReceitaDivida> buscarReceitaDividasPorDataUsuarioStatusTipoMovimentacao(Date ini, Date fim,
            Usuario usuario, StatusPagamento status, TipoMovimentacao tipo) {
        Query q = manager.createNamedQuery("ReceitaDividaBean.buscarReceitaDividasPorDataUsuarioStatusTipoMovimentacao");
        q.setParameter("usuario", usuario);
        q.setParameter("dataI", ini);
        q.setParameter("dataF", fim);
        q.setParameter("statusPagamento", status);
        q.setParameter("tipoMovimentacao", tipo);
        return q.getResultList();
    }

    @Override
    public List<ReceitaDivida> findRange(int[] range, Usuario usuarioProprietario) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
