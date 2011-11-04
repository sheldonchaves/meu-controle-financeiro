/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.MovimentacaoFinanceira;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.modelos.Usuario;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Guilherme
 */
@Stateless
public class MovimentacaoFinanceiraBean implements MovimentacaoFinanceiraBeanLocal {

    @EJB
    private ReceitaDividaBeanLocal receitaDividaBean;
    @EJB
    private ContaBancariaBeanLocal contaBancariaBean;
    @EJB(beanName = "movimentacaoFinanceiraValidador")
    private ValidadorInterface movimentacaoFinanceiraValidador;
    @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;

    @Override
    public void salvarMovimentacaoFinanceira(ContaBancaria contaBancaria, ReceitaDivida receitaDivida) {
        ContaBancaria cb = manager.find(ContaBancaria.class, contaBancaria.getId());
        ReceitaDivida rd = manager.find(ReceitaDivida.class, receitaDivida.getId());
        rd.setValor(receitaDivida.getValor());
        MovimentacaoFinanceira mf = new MovimentacaoFinanceira(cb, rd);
        cb.setSaldo(cb.getSaldo() + rd.getValorParaCalculoDireto());
        rd.setStatusPagamento(StatusPagamento.PAGA);
        this.movimentacaoFinanceiraValidador.validar(mf, this, null);
        this.contaBancariaBean.salvarContaBancaria(cb);
        this.receitaDividaBean.salvarReceitaDivida(rd);
        if (mf.getId() == null) {
            manager.persist(mf);
        } else {
            manager.merge(mf);
        }
        manager.flush();
    }

    @Override
    public List<MovimentacaoFinanceira> buscarMovimentacaoPorUsuarioStatusPaginada(int posicaoInicial, int tamanho, Usuario usuario) {
        Query q = manager.createNamedQuery("ReceitaDividaBean.buscarReceitaDividasPorUsuarioStatusPaginada");
        q.setMaxResults(tamanho);
        q.setFirstResult(posicaoInicial);
        q.setParameter("usuario", usuario);
        return q.getResultList();
    }

    @Override
    public Integer buscarQtdadeMovimentacaoPorUsuarioStatusPaginada(Usuario usuario) {
        Query q = manager.createNamedQuery("ReceitaDividaBean.buscarQtdadeMovimentacaoPorUsuarioStatusPaginada");
        q.setParameter("usuario", usuario);
        Long toReturn = (Long) q.getSingleResult();
        return toReturn.intValue();
    }
}
