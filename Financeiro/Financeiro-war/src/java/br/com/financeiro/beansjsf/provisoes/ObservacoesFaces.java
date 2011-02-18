/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.beansjsf.provisoes;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.ContaPagarReceberLocal;
import br.com.financeiro.entidades.User;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;


/**
 *
 * @author gbvbahia
 */
public class ObservacoesFaces {
    @EJB
    private ContaPagarReceberLocal contaPagarReceberBean;

    private Locale locale = new Locale("pt", "BR");
    private User proprietario;
    private Date dataReferencia = Calendar.getInstance().getTime();
    private String observacao;
    private DataModel contasModel;

    /** Creates a new instance of Observacoes */
    public ObservacoesFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
    }

    public void buscarContas(ActionEvent event){
        contasModel = new ListDataModel(contaPagarReceberBean.buscarContasPagarObservacao(observacao, dataReferencia, proprietario));
    }

    public Date getDataReferencia() {
        return dataReferencia;
    }

    public void setDataReferencia(Date dataReferencia) {
        this.dataReferencia = dataReferencia;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Locale getLocale() {
        return locale;
    }

    public DataModel getContasModel() {
        return contasModel;
    }

    public void setContasModel(DataModel contasModel) {
        this.contasModel = contasModel;
    }
}
