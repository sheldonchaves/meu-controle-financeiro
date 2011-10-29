/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.money.modelos.ContaBancaria;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "contaBancariaManager")
@ViewScoped
public class ContaBancariaManager implements InterfaceManager, Observer {

    
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    
    private ContaBancaria contaBancaria;
    
    /** Creates a new instance of ContaBancariaManager */
    public ContaBancariaManager() {
    }

    //====================
    // Iniciadores
    //====================
    @PreDestroy
    @Override
    public void end() {
        ControleObserver.removeBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "ContaBancariaManager.end() executado!");
    }

    @PostConstruct
    @Override
    public void init() {
        clean();
        ControleObserver.addBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "ContaBancariaManager.init() executado!");
    }

    //====================
    //Métodos de Negócio
    //====================
    @Override
    public void update(Observable o, Object arg) {
        int[] args = (int[]) arg;
        for (int i = 0; i < args.length; i++) {
            if (args[i] == ControleObserver.Eventos.CAD_DETALHE_MOVIMENTACAO) {
                //atualizarModel();
            }
        }
    }
    
    private void clean(){
        contaBancaria = new ContaBancaria();
        contaBancaria.setUser(loginManager.getUsuario());
    }
    //====================
    //Table Actions
    //====================
    //====================
    //SelectItem
    //====================
    //=========================
    //Getters AND Setters
    //=========================

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }
}
