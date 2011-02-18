/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

import javax.ejb.ApplicationException;
/**
 *
 * @author gbvbahia
 */
@ApplicationException(rollback=true)
public class ContaPagarReceberValueException extends RuntimeException {

    /**
     * Constructs an instance of <code>ContaPagarReceberValueException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ContaPagarReceberValueException(String msg) {
        super(msg);
    }
}
