/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ReceitaDivida;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface ReceitaDividaBeanLocal {
    
    /**
     * Salva uma ReceitaDivida
     * @param conta
     * @throws ValidacaoException 
     */
    public void salvarReceitaDivida(ReceitaDivida conta) throws ValidacaoException;
}
