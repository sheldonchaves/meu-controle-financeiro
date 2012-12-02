/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report;

import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    private List<ContaBancaria> disponiveis;

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
        this.disponiveis = disponivelFacade.findAll(usuarioFacade.getUsuario(), Boolean.TRUE);
    }

    public List<ContaBancaria> getDisponiveis() {
        if (disponiveis == null) {
            criateContas();
        }
        Collections.sort(disponiveis);
        return disponiveis;
    }
}
