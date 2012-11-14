/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.commons;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.ConstraintViolation;

/**
 * Classe abstrata que realiza as tarefas básicas para os beans que a
 * estendem. <br> Métodos sem JavaDoc possuem a documentação na
 * interface InterfaceFacade. <br> T = Entidade que o bean representa,
 * Usuario, Órgão, etc.<br>ID = Tipo de variável que foi anotada com
 * '@'Id na classe T.
 *
 * @param <T> T Classe de Entidade.
 * @param <ID> ID O tipo de ID da classe de entidade, um Long,
 * Integer, IdServidor...
 * @author Guilherme Braga
 * @since 2012/02/20
 */
@RolesAllowed({"admin", "user"})
public abstract class AbstractFacade<T extends EntityInterface, ID extends Serializable> implements InterfaceFacade<T, ID> {

    /**
     * Devolve um MAP para inserir os parâmetros nomes com parâmetros
     * valores.
     *
     * @return java.util.HashMap
     */
    public static Map<String, Object> getMapParans() {
        return new HashMap<String, Object>();
    }
    /**
     * Classe de entidade que representa a implementação.
     */
    private final Class<T> entityClass;

    /**
     * Recebe o tipo de classe de entidade que quem o implementa
     * representa.
     *
     * @param entityClasss Classe de Entidate que o bean irá
     * representar.
     */
    public AbstractFacade(final Class<T> entityClasss) {
        this.entityClass = entityClasss;
    }

    /**
     * A classe que implementa deve prover um EntityManager.
     *
     * @return EntityManager
     */
    protected abstract EntityManager getEntityManager();

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void create(final T entity) throws NegocioException {
        UtilBeans.checkNull(entity);
        if (entity.verificarId()) {
            if (entity.getId() == null) {
                throw new NegocioException("AbstractFacade"
                        + ".entityIdVerificarNull",
                        new String[]{entity.getLabel()});
            }
            if (find((ID) entity.getId()) != null) {
                throw new NegocioException("AbstractFacade.entityExiste",
                        new String[]{entity.getLabel()});
            }
        }
        try {
            getEntityManager().persist(entity);
            getEntityManager().flush();
        } catch (javax.validation.ConstraintViolationException e) {
            this.validadionException(e.getConstraintViolations());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(final T entity) throws NegocioException {
        UtilBeans.checkNull(entity);
        try {
            getEntityManager().merge(entity);
            getEntityManager().flush();
        } catch (javax.validation.ConstraintViolationException e) {
            this.validadionException(e.getConstraintViolations());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(final String namedQuery,
            final Map<String, Object> params)
            throws NegocioException {
        UtilBeans.checkNull(namedQuery);
        Query q = getEntityManager().createNamedQuery(namedQuery);
        if (params != null) {
            for (String chave : params.keySet()) {
                q.setParameter(chave, params.get(chave));
            }
        }
        try {
            q.executeUpdate();
            getEntityManager().flush();
        } catch (javax.validation.ConstraintViolationException e) {
            this.validadionException(e.getConstraintViolations());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void remove(final T entity) throws NegocioException {
        UtilBeans.checkNull(entity);
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
            getEntityManager().flush();
        } catch (PersistenceException p) {
            Logger.getLogger(this.getClass().getName()).log(
                    Level.WARNING,
                    "Problema ao remover: " + entity.toString(), p);
            throw new NegocioException("AbstractFacade.entityRemoveErro",
                    new String[]{entity.getLabel(), p.getMessage()});
        } catch (javax.validation.ConstraintViolationException e) {
            this.validadionException(e.getConstraintViolations());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T find(final ID id) {
        UtilBeans.checkNull(id);
        try {
            return getEntityManager().find(entityClass, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq =
                getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> findRange(final int[] range) {
        UtilBeans.checkNull(range);
        javax.persistence.criteria.CriteriaQuery cq =
                getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass)).distinct(true);
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq =
                getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        CriteriaBuilder criteriaBuilder =
                getEntityManager().getCriteriaBuilder();
        cq.select(criteriaBuilder.count(rt)).distinct(true);
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> listPesq(final String namedQuery) {
        UtilBeans.checkNull(namedQuery);
        Query q = getEntityManager().createNamedQuery(namedQuery);
        return q.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> listPesqParam(final String namedQuery,
            final Map<String, Object> params) {
        UtilBeans.checkNull(namedQuery, params);
        Query q = getEntityManager().createNamedQuery(namedQuery);
        for (String chave : params.keySet()) {
            q.setParameter(chave, params.get(chave));
        }
        return q.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> listPesqParam(final String namedQuery,
            final Map<String, Object> params, final int max,
            final int atual) {
        UtilBeans.checkNull(namedQuery, params);
        Query q = getEntityManager().createNamedQuery(namedQuery).setMaxResults(max).setFirstResult(atual);
        for (String chave : params.keySet()) {
            q.setParameter(chave, params.get(chave));
        }
        return q.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T pesqParam(final String namedQuery,
            final Map<String, Object> params) {
        UtilBeans.checkNull(namedQuery, params);
        Query q = getEntityManager().createNamedQuery(namedQuery);
        for (String chave : params.keySet()) {
            q.setParameter(chave, params.get(chave));
        }
        try {
            return (T) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T pesqParam(final String namedQuery) {
        UtilBeans.checkNull(namedQuery);
        Query q = getEntityManager().createNamedQuery(namedQuery);
        try {
            return (T) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Override
    public Long pesqCount(final String queryName,
            final Map<String, Object> params) {
        UtilBeans.checkNull(queryName);
        Query q = getEntityManager().createNamedQuery(queryName);
        if (params != null) {
            for (String chave : params.keySet()) {
                q.setParameter(chave, params.get(chave));
            }
        }
        try {
            return (Long) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Transforma um set de erros em uma Excecao NegocioException para
     * ser lancada na camada de negocio.
     *
     * @param erros Set de erros.
     * @throws NegocioException ConstraintViolation em
     * NegocioException.
     */
    protected void validadionException(
            final Set<ConstraintViolation<?>> erros)
            throws NegocioException {
        String erro = "";
        for (ConstraintViolation cv : erros) {
            erro += cv.getMessage() + " ";
        }
        throw new NegocioException(erro);
    }
}
