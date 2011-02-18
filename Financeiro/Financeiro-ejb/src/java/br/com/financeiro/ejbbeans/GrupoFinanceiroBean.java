/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans;

import br.com.financeiro.ejbbeans.interfaces.GrupoFinanceiroLocal;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoGasto;
import br.com.financeiro.entidades.detalhes.GrupoReceita;
import br.com.financeiro.excecoes.GrupoFinanceiroDescricaoException;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author gbvbahia
 */
@Stateless
public class GrupoFinanceiroBean implements GrupoFinanceiroLocal {

    @PersistenceContext(unitName = "financeiro")
    private EntityManager em;

    /**
     * Retorna todos os grupos gasto idependete do status.
     * @param user
     * @return
     */
    @Override
    public List<GrupoGasto> buscarGrupoGastoParaEdicao(User user) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarGrupoGastoEdicao");
            q.setParameter("user", user);
            List<GrupoGasto> resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarGrupoGastoEdicaoConjuge");
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            List<GrupoGasto> resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    /**
     * Retorna todos os grupos de receita independete do status
     * @param user
     * @return
     */
    @Override
    public List<GrupoReceita> buscarGrupoReceitaParaEdicao(User user) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarGrupoReceitaEdicao");
            q.setParameter("user", user);
            List<GrupoReceita> resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarGrupoReceitaEdicaoConjuge");
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            List<GrupoReceita> resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public GrupoGasto buscarGrupoGastoPorNome(String nomeGasto) {
        Query q = em.createNamedQuery("buscarGrupoGastoPorNome");
        q.setParameter("grupoGasto", nomeGasto);
        try {
            return (GrupoGasto) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public GrupoReceita buscarGrupoReceitaPorNome(String nomeReceita) {
        Query q = em.createNamedQuery("buscarGrupoReceitaPorNome");
        q.setParameter("grupoReceita", nomeReceita);
        try {
            return (GrupoReceita) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<GrupoGasto> buscarGrupoGastoParaSelecao(User user) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarGrupoGastoSelecao");
            q.setParameter("user", user);
            q.setParameter("status", true);
            List<GrupoGasto> resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarGrupoGastoSelecaoConjuge");
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            q.setParameter("status", true);
            List<GrupoGasto> resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    @Override
    public List<GrupoReceita> buscarGrupoReceitaParaSelecao(User user) {
        if (user.getConjugeUser() == null) {
            Query q = em.createNamedQuery("buscarGrupoReceitaSelecao");
            q.setParameter("user", user);
            q.setParameter("status", true);
            List<GrupoReceita> resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        } else {
            Query q = em.createNamedQuery("buscarGrupoReceitaSelecaoConjuge");
            q.setParameter("user", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            q.setParameter("status", true);
            List<GrupoReceita> resultList = q.getResultList();
            Collections.sort(resultList);
            return resultList;
        }
    }

    @Override
    public void cadastrarGrupoGasto(GrupoGasto grupoGasto) throws GrupoFinanceiroDescricaoException {
        if (grupoGasto.getId() == null) {
            if (validaGGasto(grupoGasto, "SALVAR")) {
                em.persist(grupoGasto);
            } else {
                throw new GrupoFinanceiroDescricaoException("Já existe um grupo de gasto com esse nome!");
            }
        } else {
            if (validaGGasto(grupoGasto, "ATUALIZAR")) {
                em.merge(grupoGasto);
            } else {
                throw new GrupoFinanceiroDescricaoException("Já existe um grupo de gasto com esse nome!");
            }
        }
    }

    @Override
    public void cadastrarGrupoReceita(GrupoReceita grupoReceita) throws GrupoFinanceiroDescricaoException {
        if (grupoReceita.getId() == null) {
            if (validaGReceita(grupoReceita, "SALVAR")) {
                em.persist(grupoReceita);
            } else {
                throw new GrupoFinanceiroDescricaoException("Já existe um grupo de receita com esse nome!");
            }
        } else {
            if (validaGReceita(grupoReceita, "ATUALIZAR")) {
                em.merge(grupoReceita);
            } else {
                throw new GrupoFinanceiroDescricaoException("Já existe um grupo de receita com esse nome!");
            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private boolean validaGGasto(GrupoGasto gGasto, String acao) {
        Query q = em.createNamedQuery("buscaGrupoGastoDescricao");
        q.setParameter("grupoGasto", gGasto.getGrupoGasto());
        q.setParameter("user", gGasto.getUser());
        try {
            GrupoGasto gg = (GrupoGasto) q.getSingleResult();
            if (acao.equals("SALVAR")) {
                return false;
            }
            if (gg.getId().equals(gGasto.getId())) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException e) {
            return true;
        } catch (NonUniqueResultException e) {
            return false;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private boolean validaGReceita(GrupoReceita gReceita, String acao) {
        Query q = em.createNamedQuery("buscaGrupoReceitaDescricao");
        q.setParameter("grupoReceita", gReceita.getGrupoReceita());
        q.setParameter("user", gReceita.getUser());
        try {
            GrupoReceita gr = (GrupoReceita) q.getSingleResult();
            if (acao.equals("SALVAR")) {
                return false;
            }
            if (gr.getId().equals(gReceita.getId())) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException e) {
            return true;
        } catch (NonUniqueResultException e) {
            return false;
        }
    }
}
