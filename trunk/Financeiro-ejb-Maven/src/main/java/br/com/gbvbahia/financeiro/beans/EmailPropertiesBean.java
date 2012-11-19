/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.EmailPropertiesFacade;
import br.com.gbvbahia.financeiro.modelos.EmailProperties;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Guilherme
 */
@Stateless
public class EmailPropertiesBean extends AbstractFacade<EmailProperties, Long>
        implements EmailPropertiesFacade {

    /**
     * Unidade de persistência <i>jdbc/money</i>.
     */
    @PersistenceContext(unitName = UtilBeans.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmailPropertiesBean() {
        super(EmailProperties.class);
    }
}
