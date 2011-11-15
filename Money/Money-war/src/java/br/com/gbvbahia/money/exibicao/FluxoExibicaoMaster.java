/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.exibicao;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *  VALIGN= MIDDLE TOP BOTTOM (horizontal)
 *  ALIGN=  LEFT - CENTER - RIGHT (alinhamento)
 * @author gbvbahia
 */
public abstract class FluxoExibicaoMaster implements Serializable {

    protected String tituloPanel = "**** NÃO DEFINIDO!!!!! ****";
    private String className;
    private String alinhamento = "left";
    private String horizontal = "top";
    
    public FluxoExibicaoMaster(String className) {
        this.className = className;
    }
    
    
    @PostConstruct
    public void init() {
        Logger.getLogger(className).log(Level.FINEST, "{0} :(init()) chamado...", className);
    }

    @PreDestroy
    public void end() {
        Logger.getLogger(className).log(Level.FINEST, "{0} : (end()) chamado...", className);
    }

    //=========================
    //Getters AND Setters
    //=========================    

    public String getTituloPanel() {
        return tituloPanel;
    }

    public void setTituloPanel(String tituloPanel) {
        this.tituloPanel = tituloPanel;
    }

    public String getAlinhamento() {
        return alinhamento;
    }

    public void setAlinhamento(String alinhamento) {
        this.alinhamento = alinhamento;
    }

    public String getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(String horizontal) {
        this.horizontal = horizontal;
    }
}
