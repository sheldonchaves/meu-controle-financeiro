/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author gbvbahia
 */
public class CartaoCreditoNumeroExisteException extends Exception {

    /**
     * Constructs an instance of <code>CartaoCreditoNumeroExisteException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CartaoCreditoNumeroExisteException(String msg) {
        super(msg);
    }
}
