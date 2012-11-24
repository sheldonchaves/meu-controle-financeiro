/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.free;

import br.com.gbvbahia.financeiro.beans.business.interfaces.RegistroBusiness;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.utils.MensagemUtils;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Guilherme
 */
@ManagedBean
@ViewScoped
public class RegisrtoController {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(RegisrtoController.class);
    @EJB
    private RegistroBusiness registroBusiness;
    private String login;
    private String email;
    private String email2;
    private String url;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest rq = (HttpServletRequest) context.getExternalContext().getRequest();
        url = "http://" + rq.getServerName() + ":" + rq.getServerPort() + rq.getContextPath() + "/pages/principal.xhtml";
    }

    /**
     * http://#{request.serverName}:#{request.serverPort}#{request.contextPath}/
     *
     * @return
     */
    public String registro() {
        if(!email.equals(email2)){
            MensagemUtils.messageFactoringFull("registroEmailNaoConfere",
                    new Object[]{email, email2},
                    FacesMessage.SEVERITY_WARN,
                    FacesContext.getCurrentInstance());
            return JsfUtil.MANTEM;
        }
        try {
            registroBusiness.registroUsuario(login, email, url);
            MensagemUtils.messageFactoringFull("registroOk",
                    new Object[]{login, email},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.warn(I18nLogger.getMsg("REGISTRO DE USUARIO ERROR.", ex));
        }
        return JsfUtil.MANTEM;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }
}
