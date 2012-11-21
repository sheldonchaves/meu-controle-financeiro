package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.GrupoFacade;
import br.com.gbvbahia.financeiro.modelos.Grupo;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <strong>SEGURANCA</strong> <br> RolesAllowed pode ser aplicada na
 * classe e/ou em metodos, aplicada na classe tem valor em todos os
 * métodos, menos os que a sobrescreverem ou utilizar outra aotação,
 * como:<br> PermitAll, DenyAll e RolesAllowed.<br> Sendo que esta
 * ultima pode ser menos ou mais
 * restringida:@RolesAllowed({"admins"})<br><br>
 *
 * @since v.3 29/03/2012
 * @author Guilherme
 */
@Stateless
@Interceptors({LogTime.class})
public class GrupoBean extends AbstractFacade<Grupo, String>
        implements GrupoFacade {

    /**
     * Unidade de persistência <i>usuarios_sys</i>.
     */
    @PersistenceContext(unitName = UtilBeans.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Construtor padrão que passa o tipo de classe para
     * AbstractFacade.
     */
    public GrupoBean() {
        super(Grupo.class);
    }
}
