/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.adm;

import br.com.financeiro.ejbbeans.interfaces.UserLocal;
import br.com.financeiro.entidades.Grups;
import br.com.financeiro.entidades.User;
import br.com.financeiro.excecoes.ProprietarioException;
import br.com.financeiro.excecoes.ProprietarioLoginException;
import br.com.financeiro.excecoes.ProprietarioNomeException;
import br.com.financeiro.excecoes.ProprietarioSenhaException;
import br.com.financeiro.utils.UtilMetodos;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class UsuarioFaces {

    @EJB
    private UserLocal userBean;
    private User proprietario;
    private DataModel dataModel;

    /** Creates a new instance of UsuarioFaces */
    public UsuarioFaces() {
        clean(null);
    }

    public void resetarSenha(ActionEvent event) {
        try {
            this.proprietario = (User) this.dataModel.getRowData();
            userBean.alteraSenhaUser(proprietario, getURLFinanceiro(), true);
            String senha = proprietario.getEmail();
            clean(event);
            UtilMetodos.messageFactoringFull("senhaAlterada", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            String emailSenha = UtilMetodos.getResourceBundle("emailSenhaEnviado", FacesContext.getCurrentInstance());
            FacesMessage msg = new FacesMessage(emailSenha + " " + senha);
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (ProprietarioSenhaException ex) {
            FacesMessage msg = new FacesMessage(ex.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } catch (ProprietarioNomeException ex) {
            FacesMessage msg = new FacesMessage(ex.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } catch (ProprietarioLoginException ex) {
            FacesMessage msg = new FacesMessage(ex.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } catch (ProprietarioException pe) {
            FacesMessage msg = new FacesMessage(pe.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
    }

    public void salvaUser(ActionEvent event) {
        try {
            this.userBean.salvarproprietario(proprietario, getURLFinanceiro());
        } catch (ProprietarioSenhaException ex) {
            UtilMetodos.messageFactoringFull("ProprietarioSenhaException", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
            Logger.getLogger(UsuarioFaces.class.getName()).log(Level.INFO, "Uma senha deve ser informada ao usuário");
            return;
        } catch (ProprietarioNomeException ex) {
            UtilMetodos.messageFactoringFull("ProprietarioNomeException", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
            Logger.getLogger(UsuarioFaces.class.getName()).log(Level.INFO, "Um nome deve ser informad ao usuário");
            return;
        } catch (ProprietarioLoginException ex) {
            UtilMetodos.messageFactoringFull("ProprietarioLoginException", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
            Logger.getLogger(UsuarioFaces.class.getName()).log(Level.INFO, "Um login deve ser informado ao usuário");
            return;
        } catch (ProprietarioException pe) {
            FacesMessage msg = new FacesMessage(pe.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        UtilMetodos.messageFactoringFull("usuarioSalvo", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        if (proprietario.getPassword() == null || proprietario.getPassword().equals("")) {
            String emailSenha = UtilMetodos.getResourceBundle("emailSenhaEnviado", FacesContext.getCurrentInstance());
            FacesMessage msg = new FacesMessage(emailSenha + " " + proprietario.getEmail());
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        clean(null);
    }

    private String getURLFinanceiro() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return UtilMetodos.getURLFinanceiro(session, FacesContext.getCurrentInstance());
    }

    public void pegaUser(ActionEvent event) {
        this.proprietario = (User) this.dataModel.getRowData();
    }

    public void clean(ActionEvent event) {
        this.proprietario = new User();
    }

    public DataModel getDataModel() {
        this.dataModel = new ListDataModel(this.userBean.todosProprietarios());
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public User getProprietario() {
        return proprietario;
    }

    public void setProprietario(User proprietario) {
        this.proprietario = proprietario;
    }

    public List<SelectItem> getRoles() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (Grups grups : this.userBean.todosGrups()) {
            toReturn.add(new SelectItem(grups, grups.getGroupName()));
        }
        return toReturn;
    }

    public List<Grups> getGrups() {
        List<Grups> grups = new ArrayList<Grups>(this.proprietario.getGrups());
        return grups;
    }

    public void setGrups(List<Grups> grups) {
        this.proprietario.setGrups(new HashSet<Grups>(grups));
    }
}
