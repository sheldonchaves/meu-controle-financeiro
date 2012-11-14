/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report;

import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Guilherme
 */
@ManagedBean
@ViewScoped
public class DisponivelReport implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(DisponivelReport.class);
    @EJB
    private ContaBancariaFacade disponivelFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    private DataModel<ContaBancaria> disponiveis;

    /**
     * Creates a new instance of DisponivelReport
     */
    public DisponivelReport() {
    }

    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...DisponivelReport");
    }

    /**
     * Recria a tabela com informações das contas.
     */
    public void atualizarContas(){
        disponiveis = null;
    }
    
    private void criateContas() {
        this.disponiveis = new ListDataModel<ContaBancaria>(disponivelFacade.findAll(usuarioFacade.getUsuario(), Boolean.TRUE));
    }

    public DataModel<ContaBancaria> getDisponiveis() {
        if (disponiveis == null) {
            criateContas();
        }
        return disponiveis;
    }
}
