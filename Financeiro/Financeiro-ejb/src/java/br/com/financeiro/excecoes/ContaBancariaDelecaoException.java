/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */

public class ContaBancariaDelecaoException extends Exception {

    /**
     * Constructs an instance of <code>ContaBancariaDelecaoException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ContaBancariaDelecaoException(String msg) {
        super(msg);
    }
}
