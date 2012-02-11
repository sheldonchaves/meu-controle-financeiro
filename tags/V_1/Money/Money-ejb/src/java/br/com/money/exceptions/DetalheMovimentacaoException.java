/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.exceptions;

import javax.ejb.ApplicationException;

/**
 *
 * @author gbvbahia
 */
@ApplicationException(rollback = true)
public class DetalheMovimentacaoException extends ValidacaoException{

    public DetalheMovimentacaoException(String msg) {
        super(msg);
    }

    public DetalheMovimentacaoException(String message, String atributoName) {
        super(message, atributoName);
    }
    
}
