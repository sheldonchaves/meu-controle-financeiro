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
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    public void salvarMovimentacaoFinanceira(ContaBancaria contaBancaria, ReceitaDivida receitaDivida){
        ContaBancaria cb = manager.find(ContaBancaria.class, contaBancaria.getId());
        ReceitaDivida rd = manager.find(ReceitaDivida.class, receitaDivida.getId());
        rd.setValor(receitaDivida.getValor());
        MovimentacaoFinanceira mf = new MovimentacaoFinanceira(cb, rd);
        cb.setSaldo(cb.getSaldo() + rd.getValorParaCalculoDireto());
        rd.setStatusPagamento(StatusPagamento.PAGA);
        this.movimentacaoFinanceiraValidador.validar(mf, this, null);
        
        if(mf.getId() == null){
            manager.persist(mf);
        }else{
            manager.merge(mf);
        }
        manager.flush();
    }
}
