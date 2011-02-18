/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.beansjsf.cadastro;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.auxjsf.comparators.TipoContaComparator;
import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.ejbbeans.interfaces.ContaBancariaLocal;
import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.enums.TipoConta;
import br.com.financeiro.excecoes.ContaBancariaAgenciaException;
import br.com.financeiro.excecoes.ContaBancariaDelecaoException;
import br.com.financeiro.excecoes.ContaBancariaExistenteException;
import br.com.financeiro.excecoes.ContaBancariaNomeBancoException;
import br.com.financeiro.excecoes.ContaBancariaNumeroContaException;
import br.com.financeiro.excecoes.ContaBancariaObservacaoException;
import br.com.financeiro.excecoes.ContaBancariaProprietarioException;
import br.com.financeiro.excecoes.ContaBancariaSaldoException;
import br.com.financeiro.excecoes.ContaBancariaTipoContaException;
import br.com.financeiro.utils.UtilMetodos;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @author Guilherme
 */

public class ContaBancariaFaces {
    @EJB
    private ContaBancariaLocal contaBancariaBean;

    private User proprietario;
    private ContaBancaria contaBancaria;
    private DataModel dataModel;

    /** Creates a new instance of ContaBancariaFaces */
    public ContaBancariaFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        clean(null);
    }

    public void clean(ActionEvent event){
        this.contaBancaria = new ContaBancaria();
    }

    public void deletarConta(ActionEvent event){
        ContaBancaria contaToDelet = (ContaBancaria) this.dataModel.getRowData();
        try {
            this.contaBancariaBean.apagarContaBancaria(contaToDelet);
            ControleObserver.notificaObservers(proprietario);
            UtilMetodos.messageFactoringFull("contaRemovida", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        } catch (ContaBancariaDelecaoException ex) {
            UtilMetodos.messageFactoringFull("ContaBancariaDelecaoException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaDelecaoException", ex);
            return;
        }catch(ContaBancariaExistenteException ex){
            UtilMetodos.messageFactoringFull("ContaBancariaExistenteException2", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaExistenteException", ex);
            return;
        }catch(RuntimeException e){
            UtilMetodos.messageFactoringFull("ContaBancariaExistenteException3", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, e.getMessage());
        }
    }

    public void salvarContaBancaria(ActionEvent event){
        try{
            contaBancaria.setUser(proprietario);
            contaBancariaBean.salvaContaBancaria(contaBancaria);
            ControleObserver.notificaObservers(proprietario);
        }catch(ContaBancariaAgenciaException cba){
            UtilMetodos.messageFactoringFull("ContaBancariaAgenciaException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaAgenciaException", cba);
            return;
        }catch(ContaBancariaNomeBancoException cbnb){
            UtilMetodos.messageFactoringFull("ContaBancariaNomeBancoException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaNomeBancoException", cbnb);
            return;
        }catch(ContaBancariaNumeroContaException cbnc){
            UtilMetodos.messageFactoringFull("ContaBancariaNumeroContaException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaNumeroContaException", cbnc);
            return;
        }catch(ContaBancariaProprietarioException cbp){
            UtilMetodos.messageFactoringFull("ContaBancariaProprietarioException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaProprietarioException", cbp);
            return;
        }catch(ContaBancariaSaldoException cbs){
            UtilMetodos.messageFactoringFull("ContaBancariaSaldoException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaSaldoException", cbs);
            return;
        }catch(ContaBancariaTipoContaException cbtc){
            UtilMetodos.messageFactoringFull("ContaBancariaTipoContaException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaTipoContaException", cbtc);
            return;
        }catch(ContaBancariaObservacaoException cbo){
            UtilMetodos.messageFactoringFull("ContaBancariaObservacaoException", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaObservacaoException", cbo);
            return;
        }catch(ContaBancariaExistenteException cbo){
            UtilMetodos.messageFactoringFull("ContaBancariaExistenteException", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
            Logger.getLogger(ContaBancariaFaces.class.getName()).log(Level.INFO, "ContaBancariaExistenteException", cbo);
            return;
        }
        UtilMetodos.messageFactoringFull("contSalva", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        clean(null);
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

      public List<SelectItem> getEnunsTipoConta() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        TipoConta[] enumTipoConta = TipoConta.values();
        Arrays.sort(enumTipoConta, new TipoContaComparator());
        for (int i = 0; i < enumTipoConta.length; i++) {
                SelectItem s = new SelectItem();
                s.setValue(enumTipoConta[i]);
                s.setLabel(enumTipoConta[i].toString());
                list.add(s);
        }
        return list;
    }

    public DataModel getDataModel() {
        List<ContaBancaria> list = contaBancariaBean.contasProprietario(proprietario);
        this.dataModel = new ListDataModel(list);
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }


}
