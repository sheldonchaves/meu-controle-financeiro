/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.SchedulerBeanLocal;
import br.com.money.business.jms.jmsEmailUtilitarios.interfaces.EmailSendLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.Scheduler;
import br.com.money.modelos.Usuario;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Guilherme
 */
@Stateless
public class SchedulerBean implements SchedulerBeanLocal {

    @EJB(beanName = "schedulerValidador")
    private ValidadorInterface schedulerValidador;
    @EJB
    private EmailSendLocal emailSendBean;
    @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;

    @Override
    public void salvarScheduler(Scheduler scheduler) throws ValidacaoException {
        schedulerValidador.validar(scheduler, this, null);
        if (scheduler.getId() == null) {
            manager.persist(scheduler);
        } else {
            manager.merge(scheduler);
        }
        manager.flush();
    }

    @Override
    public Scheduler buscarSchedulerPorUsuario(Usuario usuario) {
        Query q = manager.createNamedQuery("SchedulerBean.buscarSchedulerPorUsuario");
        q.setParameter("user", usuario);
        try {
            return (Scheduler) q.getSingleResult();
        } catch (NoResultException nr) {
            return null;
        }
    }
}
