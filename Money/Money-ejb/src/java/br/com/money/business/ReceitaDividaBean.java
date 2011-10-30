/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*;
/**
 *
 * @author Guilherme
 */
@Stateless
public class ReceitaDividaBean implements ReceitaDividaBeanLocal {
    
    @EJB(beanName="receitaDividaValidador")
    private ValidadorInterface receitaDividaValidador;

     @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;
     
    @Override
    public void salvarReceitaDivida(ReceitaDivida conta) throws ValidacaoException{
        this.receitaDividaValidador.validar(conta, this, null);
        if(conta.getId() == null){
            manager.persist(conta);
        }else{
            manager.merge(conta);
        }
        manager.flush();
    }
    
}
