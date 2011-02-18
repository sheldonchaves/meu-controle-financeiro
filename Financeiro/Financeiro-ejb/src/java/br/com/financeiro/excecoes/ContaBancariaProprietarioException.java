/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */

public class ContaBancariaProprietarioException extends Exception {

    /**
     * Constructs an instance of <code>ContaBancariaProprietarioException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ContaBancariaProprietarioException(String msg) {
        super(msg);
    }
}
