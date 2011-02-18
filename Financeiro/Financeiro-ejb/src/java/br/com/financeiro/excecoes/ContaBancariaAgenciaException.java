/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */
public class ContaBancariaAgenciaException extends Exception {

    /**
     * Constructs an instance of <code>ContaBancariaAgenciaException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ContaBancariaAgenciaException(String msg) {
        super(msg);
    }
}
