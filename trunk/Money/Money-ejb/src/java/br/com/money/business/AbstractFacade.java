/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.InterfaceFacade;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.commons.EntityInterface;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;

/**
 *
 * @author Guilherme
 */
public abstract class AbstractFacade<T extends EntityInterface,
        ID extends Serializable> implements InterfaceFacade<T, ID> {

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

    /**
     * Interface de validação de entidade a serem persistidas.
     *
     * @return ValidadorInterface que responda em validar a entidade
     * que implementa EntityInterface.
     */
    protected abstract ValidadorInterface getValidador();

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void create(final T entity) throws ValidacaoException {
        if (entity.verificarId()) {
            if (find((ID) entity.getId()) != null) {
                throw new ValidacaoException("entityExiste",
                        new String[]{entity.getLabel()});
            }
        }
        getValidador().validar(entity, this, null);
        try {
            getEntityManager().persist(entity);
            getEntityManager().flush();
        } catch (javax.validation.ConstraintViolationException e) {
            this.validadionException(e.getConstraintViolations());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(final T entity) throws ValidacaoException {
        getValidador().validar(entity, this, null);
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
            throws ValidacaoException {
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
    public void remove(final T entity) throws ValidacaoException {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
            getEntityManager().flush();
        } catch (PersistenceException p) {
            throw new ValidacaoException("entityRemoveErro",
                    new String[]{entity.getLabel(), p.getMessage()});
        } catch (javax.validation.ConstraintViolationException e) {
            this.validadionException(e.getConstraintViolations());
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T find(final ID id) {
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
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq =
                getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
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
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> listPesq(final String namedQuery) {
        Query q = getEntityManager().createNamedQuery(namedQuery);
        List<T> list = q.getResultList();
        return list;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> listPesqParam(final String namedQuery,
            final Map<String, Object> params) {
        Query q = getEntityManager().createNamedQuery(namedQuery);
        for (String chave : params.keySet()) {
            q.setParameter(chave, params.get(chave));
        }
        List<T> list = q.getResultList();
        return list;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> listPesqParam(final String namedQuery,
            final Map<String, Object> params, final int max,
            final int atual) {
        Query q = getEntityManager().createNamedQuery(namedQuery)
                .setMaxResults(max).setFirstResult(atual);
        for (String chave : params.keySet()) {
            q.setParameter(chave, params.get(chave));
        }
        List<T> list = q.getResultList();
        return list;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T pesqParam(final String namedQuery,
            final Map<String, Object> params) {
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
        Query q = getEntityManager().createNamedQuery(namedQuery);
        try {
            T t = (T) q.getSingleResult();
            return t;
        } catch (NoResultException e) {
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Override
    public Long pesqCount(final String queryName,
    final Map<String, Object> params) {
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
     * Devolve um MAP para inserir os parâmetros nomes com parâmetros
     * valores.
     *
     * @return java.util.HashMap
     */
    public static Map<String, Object> getMapParans() {
        Map<String, Object> params = new HashMap<String, Object>();
        return params;
    }

    /**
     * Transforma um set de erros em uma Excecao NegocioException para
     * ser lancada na camada de negocio.
     *
     * @param erros Set de erros.
     * @throws ValidacaoException ConstraintViolation em
     * ValidacaoException.
     */
    protected void validadionException(final Set<ConstraintViolation<?>> erros)
            throws ValidacaoException {
        String erro = "";
        for (ConstraintViolation cv : erros) {
            erro += cv.getMessage() + " ";
        }
        throw new ValidacaoException(erro);
    }
}
