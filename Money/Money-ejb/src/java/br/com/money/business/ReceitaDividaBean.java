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
public class ReceitaDividaBean extends AbstractFacade<ReceitaDivida, Long>
implements ReceitaDividaBeanLocal {

    @Override
    protected EntityManager getEntityManager() {
        return this.manager;
    }

    @Override
    protected ValidadorInterface getValidador() {
        return this.receitaDividaValidador;
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
            String identificador = UtilBeans.getIdentificadorUnico(
                    conta.getUsuario().getId(), conta.getDataVencimento());
            conta.setIdentificador(identificador);
        }
        if (conta.getId() == null) {
            create(conta);
        } else {
            update(conta);
        }
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
            throw new MovimentacaoFinanceiraException
                    ("movimentacaoFinanceiraqtdadeParcelasError",
                    "Parcelas");
        }
        if ((statusParcelas.equals(StatusPagamento.PAGA) 
                || conta.getStatusPagamento().equals(StatusPagamento.PAGA)) 
                && contaBancaria == null) {
            throw new MovimentacaoFinanceiraException("movimentacaoFinanceiraContaMovimentadaNula",
                    "Conta Bancaria");
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
            toReturn.setDataVencimento(UtilBeans.aumentaMesDate(
                    contaPagar.getDataVencimento(), 1));
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
            toReturn.setDetalheMovimentacao(
                    contaPagar.getDetalheMovimentacao());
            return toReturn;
        } else {
            return null;
        }
    }

    @Override
    public List<ReceitaDivida> buscarReceitaDividasPorUsuarioStatusPaginada(
            final int posicaoInicial, final int tamanho,
            final Usuario usuario, final StatusPagamento statusPagamento,
            final TipoMovimentacao tipoMovimentacao,
            final Date dataVencimento, final String detalhe) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("statusPagamento", statusPagamento);
        parans.put("tipoMovimentacao", tipoMovimentacao);
        parans.put("dataV1", dataVencimento);
        parans.put("dataV", dataVencimento);
        parans.put("detalhe", UtilBeans.acertaNomeParaLike(detalhe,
                UtilBeans.LIKE_MIDDLE));
        return listPesqParam("ReceitaDividaBean.buscarReceitaDividas"
                + "PorUsuarioStatusPaginada",
                parans, tamanho, posicaoInicial);
    }

    @Override
    public List<ReceitaDivida> buscarReceitaDividasPorUsuarioStatusPaginada(
            final int posicaoInicial, final int tamanho,
            final Usuario usuario, final StatusPagamento statusPagamento) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("statusPagamento", statusPagamento);
        return listPesqParam("ReceitaDividaBean.buscarReceitaDividas"
                + "PorUsuarioStatusPaginada2",
                parans, tamanho, posicaoInicial);
    }

    @Override
    public Integer buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(
            Usuario usuario, StatusPagamento statusPagamento) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("statusPagamento", statusPagamento);
        return pesqCount("ReceitaDividaBean.buscarQutdadeReceita"
                + "DividasPorUsuarioStatusPaginada2",
                parans).intValue();
    }

    @Override
    public Integer buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(
            Usuario usuario, StatusPagamento statusPagamento,
            TipoMovimentacao tipoMovimentacao,
            final Date dataVencimento, final String detalhe) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("statusPagamento", statusPagamento);
        parans.put("tipoMovimentacao", tipoMovimentacao);
        parans.put("dataV1", dataVencimento);
        parans.put("dataV", dataVencimento);
        parans.put("detalhe", UtilBeans.acertaNomeParaLike(detalhe,
                UtilBeans.LIKE_MIDDLE));
        return pesqCount("ReceitaDividaBean.buscarQutdadeReceita"
                + "DividasPorUsuarioStatusPaginada",
                parans).intValue();
    }

    @Override
    public void apagarReceitaDivida(ReceitaDivida receitaDivida, boolean deleteParcelas) {
        ReceitaDivida rd = find(receitaDivida.getId());
        if (rd.getStatusPagamento().equals(StatusPagamento.PAGA)) {
            throw new ReceitaDividaException("deleteReceitaDividaPaga");
        }
        remove(rd);
        if (deleteParcelas) {
            Map<String, Object> parans = AbstractFacade.getMapParans();
            parans.put("usuario", rd.getUsuario());
            parans.put("statusPagamento", StatusPagamento.NAO_PAGA);
            parans.put("identificador", rd.getIdentificador());
            parans.put("parcelaAtual", rd.getParcelaAtual());
            update("ReceitaDividaBean.apagarReceitaDivida", parans);
        }
    }

    @Override
    public List<ReceitaDivida>
            buscarReceitaDividasPorDataUsuarioStatusTipoMovimentacao(
            final Date ini, final Date fim,
            final Usuario usuario, final StatusPagamento status,
            final TipoMovimentacao tipo) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("dataI", ini);
        parans.put("dataF", fim);
        parans.put("statusPagamento", status);
        parans.put("tipoMovimentacao", tipo);
        return listPesqParam("ReceitaDividaBean.buscarReceitaDividas"
                + "PorDataUsuarioStatusTipoMovimentacao", parans);
    }
}
