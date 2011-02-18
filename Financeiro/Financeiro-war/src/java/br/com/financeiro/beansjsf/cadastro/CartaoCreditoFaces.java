/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.beansjsf.cadastro;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.CartaoCreditoLocal;
import br.com.financeiro.entidades.CartaoCreditoUnico;
import br.com.financeiro.entidades.User;
import br.com.financeiro.excecoes.CartaoCreditoInformacaoIncompletaException;
import br.com.financeiro.excecoes.CartaoCreditoNumeroExisteException;
import br.com.financeiro.excecoes.CartaoCreditoNumeroInvalidoException;
import br.com.financeiro.utils.UtilMetodos;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;


/**
 *
 * @author gbvbahia
 */
public class CartaoCreditoFaces {
    @EJB
    private CartaoCreditoLocal cartaoCreditoBean;
    private User proprietario;
    private DataModel dataModel;
    private CartaoCreditoUnico cartaoCreditoUnico;


    /** Creates a new instance of CartaoCreditoFaces */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public CartaoCreditoFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        clean(null);
    }

    public void salvaCartaoCredito(ActionEvent event){
        try {
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

    public void pegarCartao(ActionEvent event){
        this.cartaoCreditoUnico = (CartaoCreditoUnico) this.dataModel.getRowData();
    }

    public void clean(ActionEvent event){
        this.cartaoCreditoUnico = new CartaoCreditoUnico();
    }

    //Getters Setters
    public DataModel getDataModel() {
        this.dataModel = new ListDataModel(this.cartaoCreditoBean.buscaTodosCartoes(proprietario));
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public CartaoCreditoUnico getCartaoCreditoUnico() {
        return cartaoCreditoUnico;
    }

    public void setCartaoCreditoUnico(CartaoCreditoUnico cartaoCreditoUnico) {
        this.cartaoCreditoUnico = cartaoCreditoUnico;
    }

}
