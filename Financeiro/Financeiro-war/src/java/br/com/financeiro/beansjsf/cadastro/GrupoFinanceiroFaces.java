/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.cadastro;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.GrupoFinanceiroLocal;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoGasto;
import br.com.financeiro.entidades.detalhes.GrupoReceita;
import br.com.financeiro.excecoes.GrupoFinanceiroDescricaoException;
import br.com.financeiro.utils.UtilMetodos;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class GrupoFinanceiroFaces {

    @EJB
    private GrupoFinanceiroLocal grupoFinanceiroBean;
    private User proprietario;
    private GrupoReceita receita;
    private GrupoGasto gasto;

    /** Creates a new instance of GrupoFinanceiroFaces */
    public GrupoFinanceiroFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        clean(null);
    }

    public void salvarGrupoReceita(ActionEvent event) {
        try {
            grupoFinanceiroBean.cadastrarGrupoReceita(receita);
        } catch (GrupoFinanceiroDescricaoException ex) {
            UtilMetodos.messageFactoringFull("GrupoFinanceiroDescricaoExceptionGR", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(GrupoFinanceiroFaces.class.getName()).log(Level.INFO, ex.getMessage());
            return;
        }
        UtilMetodos.messageFactoringFull("grupoRSalvo", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        clean(null);
    }

    public void salvarGrupoGasto(ActionEvent event) {
        try {
            grupoFinanceiroBean.cadastrarGrupoGasto(gasto);
        } catch (GrupoFinanceiroDescricaoException ex) {
            UtilMetodos.messageFactoringFull("GrupoFinanceiroDescricaoExceptionGG", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(GrupoFinanceiroFaces.class.getName()).log(Level.INFO, ex.getMessage());
            return;
        }
        UtilMetodos.messageFactoringFull("grupoGSalvo", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        clean(null);
    }

    public void clean(ActionEvent event) {
        this.gasto = new GrupoGasto();
        this.receita = new GrupoReceita();
        this.gasto.setUser(proprietario);
        this.receita.setUser(proprietario);
    }

    public List<SelectItem> getGrupoGastoItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (GrupoGasto gg : this.grupoFinanceiroBean.buscarGrupoGastoParaEdicao(proprietario)) {
            if (gg.getUser() != null) {
                toReturn.add(new SelectItem(gg, gg.getGrupoGasto()));
            }
        }
        return toReturn;
    }

    public List<SelectItem> getGrupoReceitaItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (GrupoReceita gg : this.grupoFinanceiroBean.buscarGrupoReceitaParaEdicao(proprietario)) {
            if (gg.getUser() != null) {
                toReturn.add(new SelectItem(gg, gg.getGrupoReceita()));
            }
        }
        return toReturn;
    }

    //Getters and Setters
    public GrupoGasto getGasto() {
        return gasto;
    }

    public void setGasto(GrupoGasto gasto) {
        this.gasto = gasto;
    }

    public GrupoReceita getReceita() {
        return receita;
    }

    public void setReceita(GrupoReceita receita) {
        this.receita = receita;
    }
}
