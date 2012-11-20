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
import br.com.gbvbahia.financeiro.utils.Base64Encoder;
import br.com.gbvbahia.financeiro.utils.StringBeanUtils;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.List;
import java.util.Map;
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
 * restringida:
 *
 * @RolesAllowed({"admins"})<br><br>
 *
 * @author Guilherme
 * @since v.3 29/03/2012
 */
@Stateless
@DeclareRoles({"admin", "user", "SYSTEM"})
@RolesAllowed({"admin", "user", "SYSTEM"})
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

    @Override
    public void alterarSenha(final Usuario usuario,
            final String novaSenha) throws NegocioException {
        usuario.setPass(Base64Encoder.encryptPassword(novaSenha));
        super.update(usuario);
    }
    
        @Interceptors({LogTime.class})
    public List<Usuario> buscarUsuarioPorNomeLogin(final String nome,
            final String login, final int[] range) {
        Map<String, Object> parans = getMapParans();
        parans.put("firstName", acertaNomeMeio(nome));
        parans.put("userId", acertaNomeMeio(login));
        return listPesqParam("Usuario.findByNameOrLoginOrAll",
                parans, range[1] - range[0], range[0]);
    }

    @Interceptors({LogTime.class})
    public Integer contarPorNomeLogin(final String nome, final String login) {
        Map<String, Object> parans = getMapParans();
        parans.put("firstName", acertaNomeMeio(nome));
        parans.put("userId", acertaNomeMeio(login));
        return this.pesqCount("Usuario.countByNameOrLoginOrAll",
                parans).intValue();
    }
    
        /**
     * Insere % no inicio e final, mas se for null ou vazio faz tratamento
     * para buscar todos na consulta.
     *
     * @param nome java.lang.String com o valor a ser buscado, pode ser null
     * ou vazio.
     * @return java.lang.String tratada para SQL like.
     */
    private String acertaNomeMeio(final String nome) {
        return StringBeanUtils.acertaNomeParaLike(nome,
                StringBeanUtils.LIKE_MIDDLE);
    }
}
