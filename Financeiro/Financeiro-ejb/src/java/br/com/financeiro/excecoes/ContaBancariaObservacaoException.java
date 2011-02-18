/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */

public class ContaBancariaObservacaoException extends Exception {

    /**
     * Constructs an instance of <code>ContaBancariaObservacaoException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ContaBancariaObservacaoException(String msg) {
        super(msg);
    }
}
