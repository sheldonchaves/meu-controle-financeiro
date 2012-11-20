/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.SchedulerFacade;
import br.com.gbvbahia.financeiro.modelos.Scheduler;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Usuário do Windows
 */
@Stateless
@RolesAllowed({"admin", "user","SYSTEM"})
public class SchedulerBean extends AbstractFacade<Scheduler, Long> implements SchedulerFacade {

    /**
     * Unidade de persistência <i>jdbc/money</i>.
     */
    @PersistenceContext(unitName = UtilBeans.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SchedulerBean() {
        super(Scheduler.class);
    }

    @Override
    public Scheduler buscarSchedulerPorUsuario(Usuario usuario) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("user", usuario);
        return pesqParam("SchedulerBean.buscarSchedulerPorUsuario", parans);
    }

    @Override
    public List<Scheduler> buscarTodosSchelersPorStatus(final boolean status) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("status", status);
        return listPesqParam("SchedulerBean.buscarTodosSchelersPorStatus",
                parans);
    }

    @Override
    public void create(Scheduler entity) throws NegocioException {
        if (entity.getId() == null) {
            super.create(entity);
        } else {
            super.update(entity);
        }
    }
}
