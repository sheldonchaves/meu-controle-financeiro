/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */

public class ProprietarioNomeException extends Exception {

    /**
     * Constructs an instance of <code>ProprietarioNomeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProprietarioNomeException(String msg) {
        super(msg);
    }
}
