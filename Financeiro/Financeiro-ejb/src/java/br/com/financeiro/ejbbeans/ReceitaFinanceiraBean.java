/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans;

import br.com.financeiro.ejbbeans.interfaces.ReceitaFinanceiraLocal;
import br.com.financeiro.entidades.ContaReceber;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.enums.StatusReceita;
import br.com.financeiro.excecoes.ContaPagarReceberValueException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author gbvbahia
 */
@Stateless
public class ReceitaFinanceiraBean implements ReceitaFinanceiraLocal {

    @PersistenceContext(unitName = "financeiro")
    private EntityManager em;

    @Override
    public void salvarReceitaFinanceira(ContaReceber contaReceber) throws ContaPagarReceberValueException {
        if (contaReceber.getId() == null) {
            em.persist(contaReceber);
        } else {
            em.merge(contaReceber);
        }
        em.flush();
    }

    @Override
    public void apagarReceitaFinanceira(ContaReceber contaReceber) throws ContaPagarReceberValueException {
        contaReceber = em.find(ContaReceber.class, contaReceber.getId());
        em.remove(contaReceber);
        em.flush();
    }

    @Override
    public List<ContaReceber> buscarReceitasNaoRecebidas(User user, Date init, int maxRows) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarReceitasNaoRecebidas");
            q.setParameter("statusReceita", StatusReceita.NAO_RECEBIDA);
            q.setParameter("user", user);
            q.setParameter("dataPagamento", init, TemporalType.DATE);
            q.setMaxResults(maxRows);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarReceitasNaoRecebidasConjuge");
            q.setParameter("statusReceita", StatusReceita.NAO_RECEBIDA);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            q.setParameter("dataPagamento", init, TemporalType.DATE);
            q.setMaxResults(maxRows);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    @Override
    public List<ContaReceber> buscarReceitasRecebidas(User user, Date init, int maxRows) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarReceitasNaoRecebidas");
            q.setParameter("statusReceita", StatusReceita.RECEBIDA);
            q.setParameter("user", user);
            q.setParameter("dataPagamento", init, TemporalType.DATE);
            q.setMaxResults(maxRows);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarReceitasNaoRecebidasConjuge");
            q.setParameter("statusReceita", StatusReceita.RECEBIDA);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            q.setParameter("dataPagamento", init, TemporalType.DATE);
            q.setMaxResults(maxRows);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    @Override
    public List<ContaReceber> buscarReceitasPorData(User user, Date dataInit, Date dataFim, StatusReceita statusReceita) {
        if(user.getConjugeUser() == null){
        Query q = em.createNamedQuery("buscarReceitasPorData");
        q.setParameter("dataInit", dataInit, TemporalType.DATE);
        q.setParameter("dataFim", dataFim, TemporalType.DATE);
        q.setParameter("statusReceita", statusReceita);
        q.setParameter("user", user);
        List resultList = q.getResultList();
        Collections.sort(resultList);
        return resultList;
        }else{
        Query q = em.createNamedQuery("buscarReceitasPorDataConjuge");
        q.setParameter("dataInit", dataInit, TemporalType.DATE);
        q.setParameter("dataFim", dataFim, TemporalType.DATE);
        q.setParameter("statusReceita", statusReceita);
        q.setParameter("user", user);
        q.setParameter("conjugeUser", user.getConjugeUser());
        List resultList = q.getResultList();
        Collections.sort(resultList);
        return resultList;
        }
    }

    @Override
    public List<ContaReceber> buscarReceitasPorData(User user, Date dataInit, Date dataFim) {
        if(user.getConjugeUser() == null){
        Query q = em.createNamedQuery("buscarReceitasPorData2");
        q.setParameter("dataInit", dataInit, TemporalType.DATE);
        q.setParameter("dataFim", dataFim, TemporalType.DATE);
        q.setParameter("user", user);
        List resultList = q.getResultList();
        Collections.sort(resultList);
        return resultList;
        }else{
        Query q = em.createNamedQuery("buscarReceitasPorData2Conjuge");
        q.setParameter("dataInit", dataInit, TemporalType.DATE);
        q.setParameter("dataFim", dataFim, TemporalType.DATE);
        q.setParameter("user", user);
        q.setParameter("conjugeUser", user.getConjugeUser());
        List resultList = q.getResultList();
        Collections.sort(resultList);
        return resultList;
        }
    }
}
