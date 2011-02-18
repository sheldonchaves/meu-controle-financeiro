/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.provisoes;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.GrupoFinanceiroLocal;
import br.com.financeiro.ejbbeans.interfaces.RelatoriosLocal;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoGasto;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
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
public class GrupoGastoDetalheDTOFaces {

    @EJB
    private RelatoriosLocal relatoriosBean;
    @EJB
    private GrupoFinanceiroLocal grupoFinanceiroBean;
    private Date mesAno = Calendar.getInstance().getTime();
    private User proprietario;
    private GrupoGasto grupoGasto;
    private DataModel contasModel;
    private Locale locale = new Locale("pt", "BR");
    private Double total = 0.00;

    /** Creates a new instance of GrupoGastoDetalheDTOFaces */
    public GrupoGastoDetalheDTOFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
    }

    public void totalizar(ActionEvent event) {
        if (this.contasModel != null) {
            List<ContaPagar> listMode = (List<ContaPagar>) this.contasModel.getWrappedData();
            total = 0.0;
            for (ContaPagar cp : listMode) {
                if (cp.isMarcado()) {
                    total += cp.getContaValor();
                }
            }
        }
    }

    public void totalizarAll(ActionEvent event) {
        if (this.contasModel != null) {
            List<ContaPagar> listMode = (List<ContaPagar>) this.contasModel.getWrappedData();
            for (ContaPagar cp : listMode) {
                cp.setMarcado(true);
            }
            totalizar(event);
        }
    }

    public void buscarContas(ActionEvent event) {
        total = 0.0;
        Calendar c = Calendar.getInstance();
        c.setTime(mesAno);
        List<ContaPagar> listModel = this.relatoriosBean.buscarContasPorGrupoGasto(proprietario, c, grupoGasto);
        this.contasModel = new ListDataModel(listModel);
    }

    public List<SelectItem> getGrupoGastoItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (GrupoGasto gg : this.grupoFinanceiroBean.buscarGrupoGastoParaSelecao(proprietario)) {
            toReturn.add(new SelectItem(gg, gg.getGrupoGasto()));
        }
        return toReturn;
    }

    public DataModel getContasModel() {
        return contasModel;
    }

    public void setContasModel(DataModel contasModel) {
        this.contasModel = contasModel;
    }

    public GrupoGasto getGrupoGasto() {
        return grupoGasto;
    }

    public void setGrupoGasto(GrupoGasto grupoGasto) {
        this.grupoGasto = grupoGasto;
    }

    public Date getMesAno() {
        return mesAno;
    }

    public void setMesAno(Date mesAno) {
        this.mesAno = mesAno;
    }

    public User getProprietario() {
        return proprietario;
    }

    public void setProprietario(User proprietario) {
        this.proprietario = proprietario;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
