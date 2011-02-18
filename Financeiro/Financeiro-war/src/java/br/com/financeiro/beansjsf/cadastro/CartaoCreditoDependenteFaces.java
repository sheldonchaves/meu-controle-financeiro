/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.cadastro;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.CartaoCreditoLocal;
import br.com.financeiro.ejbbeans.interfaces.UserLocal;
import br.com.financeiro.entidades.CartaoCreditoUnico;
import br.com.financeiro.entidades.User;
import br.com.financeiro.excecoes.CartaoCreditoInformacaoIncompletaException;
import br.com.financeiro.excecoes.CartaoCreditoNumeroExisteException;
import br.com.financeiro.excecoes.CartaoCreditoNumeroInvalidoException;
import br.com.financeiro.utils.UtilMetodos;
import java.util.ArrayList;
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
public class CartaoCreditoDependenteFaces {

    @EJB
    private UserLocal proprietarioBean;

    @EJB
    private CartaoCreditoLocal cartaoCreditoBean;

    private User proprietario;

    private User titularCC;

    private CartaoCreditoUnico cartaoCreditoUnico;

    private DataModel dataModel;

    private String loginTitular;

    private String senhaTitular;

    private boolean titularLiberado = false;

    /** Creates a new instance of CartaoCreditoDependenteFaces */
    public CartaoCreditoDependenteFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        clean(null);
    }

    public void validarTitular(ActionEvent event) {
        if(this.proprietario.getLogin().equalsIgnoreCase(this.loginTitular)){
            UtilMetodos.messageFactoringFull("loginInvalidoCC2", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
            return;
        }
        User prop = proprietarioBean.buscaProprietarioLogin(this.loginTitular, false);
        if (prop != null && prop.getPassword().equals(this.proprietarioBean.criptografarSenha(this.senhaTitular, prop.stringAMIN()))) {
            titularLiberado = true;
            this.titularCC = prop;
            loginTitular = null;
            senhaTitular = null;
            return;
        } else {
            UtilMetodos.messageFactoringFull("loginInvalidoCC", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(LoginCT.class.getName()).log(Level.WARNING, "Tentativa de liberar cartão de crédito do: " + this.loginTitular + " sem sucesso!\nTentado por: " + this.proprietario.getLogin());
            titularLiberado = false;
            this.titularCC = null;
            senhaTitular = null;
            return;
        }
    }

      public void salvaCartaoCredito(ActionEvent event){
        try {
            cartaoCreditoUnico.setDiaMesmoMes(cartaoCreditoUnico.getCartaoCreditoTitular().getDiaMesmoMes());
            cartaoCreditoUnico.setDiaVencimento(cartaoCreditoUnico.getCartaoCreditoTitular().getDiaVencimento());
            cartaoCreditoUnico.setEmpresaCartao(cartaoCreditoUnico.getCartaoCreditoTitular().getEmpresaCartao());
            this.cartaoCreditoBean.salvarCartaoCredito(cartaoCreditoUnico, proprietario);
        } catch (CartaoCreditoNumeroExisteException ex) {
            UtilMetodos.messageFactoringFull("CartaoCreditoNumeroExisteException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(CartaoCreditoFaces.class.getName()).log(Level.INFO, "CartaoCreditoNumeroExisteException", ex);
            return;
        } catch (CartaoCreditoNumeroInvalidoException ex) {
            UtilMetodos.messageFactoringFull("CartaoCreditoNumeroInvalidoException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(CartaoCreditoFaces.class.getName()).log(Level.INFO, "CartaoCreditoNumeroInvalidoException", ex);
            return;
        } catch (CartaoCreditoInformacaoIncompletaException ex) {
            UtilMetodos.messageFactoringFull("CartaoCreditoInformacaoIncompletaException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(CartaoCreditoFaces.class.getName()).log(Level.INFO, "CartaoCreditoInformacaoIncompletaException", ex);
            return;
        }
        UtilMetodos.messageFactoringFull("cartaoSalvo", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        clean(null);
    }

    public List<SelectItem> getCartaoCreditoItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        toReturn.add(new SelectItem("Selecione", "Selecione"));
        for (CartaoCreditoUnico cc : this.cartaoCreditoBean.buscaCartaoPorStatus(true, titularCC)) {
            toReturn.add(new SelectItem(cc, cc.getLabelCartao()));
        }
        return toReturn;
    }



    public void pegarCartao(ActionEvent event){
        this.cartaoCreditoUnico = (CartaoCreditoUnico) this.dataModel.getRowData();
        this.titularCC = this.cartaoCreditoBean.buscarUserCartao(cartaoCreditoUnico);
        this.titularLiberado = true;
    }


      public void clean(ActionEvent event){
        this.cartaoCreditoUnico = new CartaoCreditoUnico();
    }
    

    public boolean isTitularLiberado() {
        return titularLiberado;
    }

    public void setTitularLiberado(boolean titularLiberado) {
        this.titularLiberado = titularLiberado;
    }

    public String getLoginTitular() {
        return loginTitular;
    }

    public void setLoginTitular(String loginTitular) {
        this.loginTitular = loginTitular;
    }

    public String getSenhaTitular() {
        return senhaTitular;
    }

    public void setSenhaTitular(String senhaTitular) {
        this.senhaTitular = senhaTitular;
    }

    public CartaoCreditoUnico getCartaoCreditoUnico() {
        return cartaoCreditoUnico;
    }

    public void setCartaoCreditoUnico(CartaoCreditoUnico cartaoCreditoUnico) {
        this.cartaoCreditoUnico = cartaoCreditoUnico;
    }

    public DataModel getDataModel() {
        this.dataModel = new ListDataModel(this.cartaoCreditoBean.buscaTodosCartoes(proprietario));
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }



    
}
