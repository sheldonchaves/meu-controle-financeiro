/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */

public class ProprietarioSenhaException extends Exception {

    /**
     * Constructs an instance of <code>ProprietarioSenhaException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProprietarioSenhaException(String msg) {
        super(msg);
    }
}
