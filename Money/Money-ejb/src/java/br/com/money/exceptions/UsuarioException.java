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
public class UsuarioException extends ValidacaoException {

    public UsuarioException(String msg) {
        super(msg);
    }
}
