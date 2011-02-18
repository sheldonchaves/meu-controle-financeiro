/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */

public class ContaBancariaNumeroContaException extends Exception {


    /**
     * Constructs an instance of <code>ContaBancariaNumeroContaException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ContaBancariaNumeroContaException(String msg) {
        super(msg);
    }
}
