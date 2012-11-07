/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <strong>SEGURANCA</strong> <br> RolesAllowed pode ser aplicada na classe
 * e/ou em metodos, aplicada na classe tem valor em todos os métodos, menos os
 * que a sobrescreverem ou utilizar outra aotação, como:<br> PermitAll,
 * DenyAll e RolesAllowed.<br> Sendo que esta ultima pode ser menos ou mais
 * restringida:@RolesAllowed({"admins"})<br><br>
 *
 * @author Guilherme
 * @since v.3 29/03/2012
 */
@Stateless
@DeclareRoles({"admin", "user"})
@RolesAllowed({"admin", "user"})
@Interceptors({LogTime.class})
public class UsuarioBean extends AbstractFacade<Usuario, String>
        implements UsuarioFacade {

    /**
     * Unidade de persistência <i>jdbc/money</i>.
     */
    @PersistenceContext(unitName = UtilBeans.PERSISTENCE_UNIT)
    private EntityManager em;
    /**
     * javax.ejb.SessionContext é por onde o Conteiner J2EE disponibiliza
     * informações sobre a aplicação, inclusive sobre o usuário logado.
     */
    @Resource
    private SessionContext context;

    /**
     * Construtor padrão que passa o tipo de classe para AbstractFacade.
     */
    public UsuarioBean() {
        super(Usuario.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Objeto Usuario referente ao usuario logado.
     *
     * @return br.com.gbvbahia.usuarios.modelos.Usuario
     */
    @Override
    public Usuario getUsuario() {
        return this.find(getUsuarioId());
    }

    /**
     * Login do usuário logado.
     *
     * @return Login usuario.
     */
    @Override
    public String getUsuarioId() {
        return context.getCallerPrincipal().getName();
    }

    /**
     * Verifica se o usuário logado é de um determinado grupo.
     *
     * @param grupoId String referente ao nome único do grupo que se deseja
     * verificar.
     * @return true se estiver e false se não.
     */
    @Override
    public boolean usuarioInGrupo(final String grupoId) {
        try {
            return context.isCallerInRole(grupoId);
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public void definirConjuge(Usuario usr1, Usuario usr2) throws NegocioException {
        usr1.setConjuge(usr2);
        usr2.setConjuge(usr1);
        super.update(usr2);
        super.update(usr1);
    }
}
