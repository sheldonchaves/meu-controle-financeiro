/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */

public class ContaBancariaTipoContaException extends Exception {

    /**
     * Constructs an instance of <code>ContaBancariaTipoContaException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ContaBancariaTipoContaException(String msg) {
        super(msg);
    }
}