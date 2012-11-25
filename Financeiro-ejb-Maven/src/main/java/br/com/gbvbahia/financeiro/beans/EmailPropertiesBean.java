/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.EmailPropertiesFacade;
import br.com.gbvbahia.financeiro.modelos.EmailProperties;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.Map;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author Guilherme
 */
@Stateless
@Interceptors({LogTime.class})
public class EmailPropertiesBean extends AbstractFacade<EmailProperties, Long>
        implements EmailPropertiesFacade {

    Logger logger = Logger.getLogger(EmailPropertiesBean.class);
    
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

    @Override
    public EmailProperties buscarEmailAtivo() {
        Map<String, Object> parans = getMapParans();
        parans.put("status", true);
        try {
            EmailProperties emailProperties = pesqParam("EmailProperties.buscarAtivos", parans);
            if(emailProperties == null){
                logger.error("*** ATENÇÃO: deveria haver um EmailProperties estar ativo. ***");
            }
            return emailProperties;
        } catch (NonUniqueResultException e) {
            logger.error("*** ATENÇÃO: somente um EmailProperties deveria estar ativo. ***",e);
            return listPesqParam("EmailProperties.buscarAtivos", parans).get(0);
        }
    }

    @Override
    public int count() {
         Query q = em.createNamedQuery("EmailProperties.count");
        return ((Long) q.getSingleResult()).intValue();
    }
}
