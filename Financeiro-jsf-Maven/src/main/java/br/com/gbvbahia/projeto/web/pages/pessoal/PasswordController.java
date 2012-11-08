/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.pessoal;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.Base64Encoder;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.common.LoginController;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.utils.MensagemUtils;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 * Bean para alteração de senha do usuário.
 *
 * @since v.1 19/05/2012
 * @author Guilherme
 */
@ManagedBean
@RequestScoped
public class PasswordController {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(PasswordController.class);
    /**
     * Facade Usuario para CRUD.
     */
    @EJB
    private UsuarioFacade usuarioFacade;
    /**
     * Componente que contém o usuário logado.
     */
    @ManagedProperty("#{loginController}")
    private LoginController loginController;
    /**
     * Recebe a nova senha digitada.
     */
    private String senha;
    /**
     * Senha atual para confirmação.
     */
    private String senhaAtual;

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...");
    }

    /**
     * Creates a new instance of PasswordController.
     */
    public PasswordController() {
    }

    //====================
    //Métodos de Ação
    //====================
    /**
     * Altera a senha atual do usuário.
     *
     * @return Fluxo de exibição.
     */
    public String alterarSenha() {
        Usuario userLogado = this.loginController.getUsuario();
        if (!Base64Encoder.encryptPassword(senhaAtual).equals(userLogado.
                getPass())) {
            logger.warn(I18nLogger.getMsg("passwordErrorChange", userLogado.
                    getUserId()));
            MensagemUtils.messageFactoringFull("PasswordError",
                    new Object[]{userLogado.getFirstName()},
                    FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            return JsfUtil.MANTEM;
        }
        try {
            this.usuarioFacade.alterarSenha(userLogado, senha);
            MensagemUtils.messageFactoringFull("PasswordUpdated",
                    new Object[]{userLogado.getFirstName()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            return JsfUtil.PAGES_PRINCIPAL_NR;
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("updateError", userLogado.
                    getUserId()));
            return JsfUtil.MANTEM;
        }
    }

    //====================
    //Getters AND Setters
    //====================
    /**
     * Recupera a senha.
     *
     * @return Senha.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha.
     *
     * @param newSenha Senha;
     */
    public void setSenha(final String newSenha) {
        this.senha = newSenha;
    }

    /**
     * Informa senha atual.
     *
     * @return Senha atual
     */
    public String getSenhaAtual() {
        return senhaAtual;
    }

    /**
     * Informa senha atual.
     *
     * @param sAtual Senha atual
     */
    public void setSenhaAtual(final String sAtual) {
        this.senhaAtual = sAtual;
    }

    /**
     * Informa controller atual.
     *
     * @param varLoginController controller
     */
    public void setLoginController(final LoginController varLoginController) {
        this.loginController = varLoginController;
    }
}
