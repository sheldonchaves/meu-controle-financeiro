/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans;

import br.com.financeiro.ejbbeans.interfaces.ContaPagarReceberLocal;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.enums.StatusPagamento;
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
public class ContaPagarReceberBean implements ContaPagarReceberLocal {

    @PersistenceContext(unitName = "financeiro")
    private EntityManager em;

    @Override
    public void salvarContaPagarReceber(ContaPagar contaPagar) throws ContaPagarReceberValueException {
        if (contaPagar.getId() == null) {
            em.persist(contaPagar);
        } else {
            em.merge(contaPagar);
        }
        em.flush();
    }

    @Override
    public List<ContaPagar> buscarContasNaoPagas(User user, Date init, int maxRows) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarContasPorData3");
            q.setParameter("statusPagamento", StatusPagamento.NAO_PAGA);
            q.setParameter("user", user);
            q.setParameter("dataVencimento", init, TemporalType.DATE);
            q.setMaxResults(maxRows);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarContasPorData3Conjuge");
            q.setParameter("statusPagamento", StatusPagamento.NAO_PAGA);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            q.setParameter("dataVencimento", init, TemporalType.DATE);
            q.setMaxResults(maxRows);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    @Override
    public List<ContaPagar> buscarContasPagas(User user, Date init, int maxRows) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarContasPorData3");
            q.setParameter("statusPagamento", StatusPagamento.PAGA);
            q.setParameter("user", user);
            q.setParameter("dataVencimento", init, TemporalType.DATE);
            q.setMaxResults(maxRows);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarContasPorData3Conjuge");
            q.setParameter("statusPagamento", StatusPagamento.PAGA);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            q.setParameter("dataVencimento", init, TemporalType.DATE);
            q.setMaxResults(maxRows);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    @Override
    public List<ContaPagar> buscarContasPorData(Date dataInit, Date dataFim, StatusPagamento statusPagamento, User user) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarContasPorData");
            q.setParameter("dataInit", dataInit, TemporalType.DATE);
            q.setParameter("dataFim", dataFim, TemporalType.DATE);
            q.setParameter("statusPagamento", statusPagamento);
            q.setParameter("user", user);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarContasPorDataConjuge");
            q.setParameter("dataInit", dataInit, TemporalType.DATE);
            q.setParameter("dataFim", dataFim, TemporalType.DATE);
            q.setParameter("statusPagamento", statusPagamento);
            q.setParameter("user", user);
            q.setParameter("userConjuge", user.getConjugeUser());
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    @Override
    public List<ContaPagar> buscarContasPorData(Date dataInit, Date dataFim, User user) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarContasPorData2");
            q.setParameter("dataInit", dataInit, TemporalType.DATE);
            q.setParameter("dataFim", dataFim, TemporalType.DATE);
            q.setParameter("user", user);
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarContasPorData2Conjuge");
            q.setParameter("dataInit", dataInit, TemporalType.DATE);
            q.setParameter("dataFim", dataFim, TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            List resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    @Override
    public void deletarContaPagarReceber(ContaPagar contaPagarReceber) throws ContaPagarReceberValueException {
        contaPagarReceber = em.find(ContaPagar.class, contaPagarReceber.getId());
        em.remove(contaPagarReceber);
        em.flush();
    }

    @Override
    public void deletarContaPagarReceberPorIdentificador(String identificador, int parcela) {
        if (identificador == null) {
            throw new ContaPagarReceberValueException("O identificador não pode ser nulo!");
        }
        Query q = em.createNamedQuery("deletarContaPagarReceberPorIdentificador");
        q.setParameter("identificador", identificador);
        q.setParameter("statusPagamento", StatusPagamento.NAO_PAGA);
        q.setParameter("parcelaAtual", parcela);
        q.executeUpdate();
    }

    @Override
    public List<ContaPagar> buscarContasPagarObservacao(String observacao, Date inicial, User user) {
        Query q = em.createQuery(getObsQuery(user.getConjugeUser() == null ? false : true).replace("*", observacao));
        q.setParameter("dataI", inicial, TemporalType.DATE);
        q.setParameter("user", user);
        if (user.getConjugeUser() != null) {
            q.setParameter("conjugeUser", user.getConjugeUser());
        }
        q.setMaxResults(60);
        List resultList = q.getResultList();
        return resultList;
    }

    private String getObsQuery(boolean conjuge) {
        String q = "Select cp From ContaPagar cp";
        q += " Where cp.dataVencimento >= :dataI";
        if (conjuge) {
            q += " AND (cp.user = :user OR cp.user = :conjugeUser)";
        } else {
            q += " AND (cp.user = :user)";
        }
        q += " AND cp.observacao like '%*%'";
        q += " Order by cp.dataVencimento, cp.valor DESC";
        return q;
    }
}
