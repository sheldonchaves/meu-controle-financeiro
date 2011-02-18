/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans;

import br.com.financeiro.ejbbeans.interfaces.RelatoriosLocal;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoGasto;
import br.com.financeiro.entidades.enums.FormaPagamento;
import br.com.financeiro.entidades.enums.StatusPagamento;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author gbvbahia
 */
@Stateless
public class RelatoriosBean implements RelatoriosLocal {

    @PersistenceContext(name = "financeiro")
    private EntityManager em;

    @Override
    public Double pagamentoAcumuladoMes(Calendar mesAno, User user) {
        if (user.getConjugeUser() == null) {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("reportPagamentoAcumuladoMes");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            Double toReturn = (Double) q.getSingleResult();
            if (toReturn == null) {
                toReturn = 0.00;
            }
            return toReturn;
        } else {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("reportPagamentoAcumuladoMesConjuge");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            Double toReturn = (Double) q.getSingleResult();
            if (toReturn == null) {
                toReturn = 0.00;
            }
            return toReturn;
        }
    }

    @Override
    public Double receitaAcumuladaMes(Calendar mesAno, User user) {
        if (user.getConjugeUser() == null) {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("reportReceitaAcumuladaMes");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            Double toReturn = (Double) q.getSingleResult();
            if (toReturn == null) {
                toReturn = 0.00;
            }
            return toReturn;
        } else {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("reportReceitaAcumuladaMesConjuge");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            Double toReturn = (Double) q.getSingleResult();
            if (toReturn == null) {
                toReturn = 0.00;
            }
            return toReturn;
        }
    }

    @Override
    public List<Object[]> contaPagarFormaPagto(FormaPagamento formaPagamento, User user, Calendar mesAno) {
        if (user.getConjugeUser() == null) {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("reportContaPagarFormaPagto");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("formaPagamento", formaPagamento);
            return q.getResultList();
        } else {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("reportContaPagarFormaPagtoConjuge");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            q.setParameter("formaPagamento", formaPagamento);
            return q.getResultList();
        }
    }

    /**
     * Retorna Array de Objetos contendo dois valores Double
     * 1º Total Contas Não Pagas
     * 2º Total Contas Pagas
     * @param user
     * @param data
     * @return
     */
    @Override
    public Double[] buscarNPagoPago(User user, Calendar mesAno) {
        Double[] toReturn = new Double[2];
        toReturn[0] = this.buscarValoresStatus(user, mesAno, StatusPagamento.NAO_PAGA);
        toReturn[1] = this.buscarValoresStatus(user, mesAno, StatusPagamento.PAGA);
        return toReturn;
    }

    @Override
    public Double buscarValoresStatus(User user, Calendar mesAno, StatusPagamento status) {
        if (user.getConjugeUser() == null) {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("reportBuscarNPagoPago");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("statusPagamento", status);
            try {
                return (Double) q.getSingleResult();
            } catch (NoResultException e) {
                return 0.00;
            }
        } else {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("reportBuscarNPagoPagoConjuge");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            q.setParameter("statusPagamento", status);
            try {
                return (Double) q.getSingleResult();
            } catch (NoResultException e) {
                return 0.00;
            }
        }
    }

    /**
     * Retorna uma lista contendo:
     * [0]String: nome do grupo gasto
     * [1]Double: valor gasto no periódo selecionado
     * @param mesAno
     * @param statusPagamento
     * @param user
     * @return
     */
    @Override
    public List<Object[]> buscarGastoPorGrupoGasto(Calendar mesAno, StatusPagamento statusPagamento, User user) {
        if (user.getConjugeUser() == null) {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("buscarGastoPorGrupoGasto");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("statusPagamento", statusPagamento);
            return q.getResultList();
        } else {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("buscarGastoPorGrupoGastoConjuge");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            q.setParameter("statusPagamento", statusPagamento);
            return q.getResultList();
        }
    }

    /**
     * Retorna uma lista contendo:
     * [0]String: nome do grupo gasto
     * [1]Double: valor gasto no periódo selecionado
     * @param mesAno
     * @param statusPagamento
     * @param user
     * @return
     */
    @Override
    public List<Object[]> buscarGastoPorGrupoGasto(Calendar mesAno, User user) {
        if (user.getConjugeUser() == null) {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("buscarGastoPorGrupoGasto2");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            return q.getResultList();
        } else {
            Date[] d = inicialFinal(mesAno);
            Query q = em.createNamedQuery("buscarGastoPorGrupoGasto2Conjuge");
            q.setParameter("dataI", d[0], TemporalType.DATE);
            q.setParameter("dataF", d[1], TemporalType.DATE);
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            return q.getResultList();
        }
    }

    /**
     * Retorna uma lista com todas as contas que vencem no mês/ano da data passada, do usuário informado
     * filtrando pelo grupo gasto.
     * @param user
     * @param mesAno
     * @param grupoGasto
     * @return
     */
    @Override
    public List<ContaPagar> buscarContasPorGrupoGasto(User user, Calendar mesAno, GrupoGasto grupoGasto) {
        if(user.getConjugeUser() == null){
        Date[] d = inicialFinal(mesAno);
        Query q = em.createNamedQuery("buscarContasPorGrupoGasto");
        q.setParameter("dataI", d[0], TemporalType.DATE);
        q.setParameter("dataF", d[1], TemporalType.DATE);
        q.setParameter("user", user);
        q.setParameter("grupoGasto", grupoGasto);
        return q.getResultList();
        }else{
        Date[] d = inicialFinal(mesAno);
        Query q = em.createNamedQuery("buscarContasPorGrupoGastoConjuge");
        q.setParameter("dataI", d[0], TemporalType.DATE);
        q.setParameter("dataF", d[1], TemporalType.DATE);
        q.setParameter("user", user);
        q.setParameter("conjugeUser", user.getConjugeUser());
        q.setParameter("grupoGasto", grupoGasto);
        return q.getResultList();
        }
    }

    private Date[] inicialFinal(Calendar mesAno) {
        Date[] toReturn = new Date[2];
        mesAno.set(Calendar.DAY_OF_MONTH, 1);
        toReturn[0] = mesAno.getTime();
        mesAno.set(Calendar.DAY_OF_MONTH, mesAno.getActualMaximum(Calendar.DAY_OF_MONTH));
        toReturn[1] = mesAno.getTime();
        return toReturn;
    }
}
