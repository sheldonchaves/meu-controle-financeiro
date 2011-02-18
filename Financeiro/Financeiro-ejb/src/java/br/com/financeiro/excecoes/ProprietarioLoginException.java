/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */


public class ProprietarioLoginException extends Exception {

    /**
     * Constructs an instance of <code>ProprietarioLoginException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProprietarioLoginException(String msg) {
        super(msg);
    }
}
