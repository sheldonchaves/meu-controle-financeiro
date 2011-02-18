/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.enxerto;

import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.ContaBancariaLocal;
import br.com.financeiro.entidades.User;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class EnxertoContasGerencial implements Observer{

    @EJB
    private ContaBancariaLocal contaBancariaBean;
    private DataModel contasDataModel;
    private Locale locale = new Locale("pt", "BR");
    private User proprietario;

    /** Creates a new instance of EnxertoContasGerencial */
    public EnxertoContasGerencial() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        ControleObserver.addBeanObserver(proprietario, this);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.contasDataModel = new ListDataModel(this.contaBancariaBean.contasProprietario(proprietario));
    }
    
    public DataModel getContasDataModel() {
        if(this.contasDataModel == null){
        this.contasDataModel = new ListDataModel(this.contaBancariaBean.contasProprietario(proprietario));
        }
        return contasDataModel;
    }

    public void setContasDataModel(DataModel contasDataModel) {
        this.contasDataModel = contasDataModel;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
}
