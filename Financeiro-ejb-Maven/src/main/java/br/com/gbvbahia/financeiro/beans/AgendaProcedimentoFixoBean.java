/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Bean de entidade a AgendaProcedimentoFixo.
 *
 * @author Guilherme
 * @since 01/04/2012
 */
@Stateless
//@RolesAllowed({ "admin", "user" })
public class AgendaProcedimentoFixoBean
        extends AbstractFacade<AgendaProcedimentoFixo, Long>
        implements AgendaProcedimentoFixoFacade {

    /**
     * Construtor padrão que passa o tipo de classe para
     * AbstractFacade.
     */
    public AgendaProcedimentoFixoBean() {
        super(AgendaProcedimentoFixo.class);
    }
    /**
     * Unidade de persistência <i>jdbc/money</i>.
     */
    @PersistenceContext(unitName = UtilBeans.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
