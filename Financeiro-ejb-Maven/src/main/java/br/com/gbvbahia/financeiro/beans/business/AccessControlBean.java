/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.business.interfaces.AccessControlBusiness;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.apache.log4j.Logger;

/**
 * Realiza controle de acesso do usuário ao sistema. Inicialmente essa classe
 * parece simples, mas as regras de validação e segurança vão aumentando, de
 * acordo com o crescimento do sistema, esse bean foi criado para cuidar desta
 * parte do modo mais transparente possível para o restante da aplicação.
 * 
 * @since v.1 01/06/2012
 * @author Guilherme Braga
 */
@Stateless
@Interceptors({LogTime.class})
public class AccessControlBean implements AccessControlBusiness {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(AccessControlBean.class);
    @EJB
    private UsuarioFacade usuarioFacade;
    /**
     * javax.ejb.SessionContext é por onde o Conteiner J2EE disponibiliza
     * informações sobre a aplicação, inclusive sobre o usuário logado.
     */
    @Resource
    private SessionContext context;

    @Override
    public String getUsuarioId() {
        return context.getCallerPrincipal().getName();
    }

    @Override
    public boolean usuarioInGrupo(final String grupoId) {
        try {
            return context.isCallerInRole(grupoId);
        } catch (IllegalStateException e) {
            logger.info("Usuário não está no grupo: " + grupoId);
            return false;
        }
    }

    @Override
    public Usuario getUsuarioLogado() {
        return usuarioFacade.find(getUsuarioId());
    }
}
