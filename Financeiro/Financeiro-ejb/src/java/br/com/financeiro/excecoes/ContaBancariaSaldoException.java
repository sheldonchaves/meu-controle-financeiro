/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */

public class ContaBancariaSaldoException extends Exception {

    /**
     * Constructs an instance of <code>ContaBancariaSaldoException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ContaBancariaSaldoException(String msg) {
        super(msg);
    }
}
