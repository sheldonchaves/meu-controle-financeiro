/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.pesquisa;

import br.com.gbvbahia.financeiro.beans.facades.MovimentacaoFinanceiraFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.projeto.web.pages.report.utils.ConjugeMakeBar;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.chart.CartesianChartModel;

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class ConjugePesquisa {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(ConjugePesquisa.class);
    @EJB
    private MovimentacaoFinanceiraFacade movimentacaoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    //Grafico
    private CartesianChartModel bar;

    /**
     * Creates a new instance of ConjugePesquisa
     */
    public ConjugePesquisa() {
    }

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...ConjugePesquisa");
        dataListener();
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...ConjugePesquisa");
    }

     public void dataListener() {
         bar =  new ConjugeMakeBar(movimentacaoFacade, usuarioFacade).makeBar();
     }
    
    public CartesianChartModel getBar() {
        return bar;
    }
}
