/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.exibicao;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "fluxoMenu")
@SessionScoped
public class Menu implements Serializable{

   public String principal = "active";
   public String contas = "";
   public String contasBancarias = "";
   
   private enum MenuAtivo{
       PRINCIPAL, CONTAS, CONTAS_BANCARIAS;
   } 
   
   public String telaPrincipal(){
       defineMenuAtivo(MenuAtivo.PRINCIPAL);
       return "principal";
   }
    
   public String telaContas(){
       defineMenuAtivo(MenuAtivo.CONTAS);
       return "contas";
   }
   
   public String telaContasBancarias(){
       defineMenuAtivo(MenuAtivo.CONTAS_BANCARIAS);
       return "contasbancarias";
   }
   
    private void defineMenuAtivo(MenuAtivo menuAtivo){
        if (menuAtivo.equals(MenuAtivo.PRINCIPAL)) {principal = "active";} else{ principal = "";}
        if (menuAtivo.equals(MenuAtivo.CONTAS)) {contas = "active";} else{ contas = "";}
        if (menuAtivo.equals(MenuAtivo.CONTAS_BANCARIAS)) {contasBancarias = "active";} else{ contasBancarias = "";}
    }
    
    //=========================
    //Getters AND Setters
    //=========================  

    public String getContas() {
        return contas;
    }

    public void setContas(String contas) {
        this.contas = contas;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getContasBancarias() {
        return contasBancarias;
    }

    public void setContasBancarias(String contasBancarias) {
        this.contasBancarias = contasBancarias;
    }
}
