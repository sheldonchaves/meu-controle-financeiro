/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.exceptions;

import javax.ejb.ApplicationException;

/**
 *
 * @author Guilherme
 */
@ApplicationException(rollback = true)
public class ContaBancariaException extends ValidacaoException{

    public ContaBancariaException(String msg) {
        super(msg);
    }

    public ContaBancariaException(String message, String atributoName) {
        super(message, atributoName);
    }
    
}
