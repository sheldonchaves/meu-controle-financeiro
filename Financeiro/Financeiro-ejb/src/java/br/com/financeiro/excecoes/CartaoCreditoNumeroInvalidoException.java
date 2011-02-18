/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *  Utilizada caso número do cartão de credito seja inválido
 * @author gbvbahia
 */
public class CartaoCreditoNumeroInvalidoException extends Exception {

    /**
     * Creates a new instance of <code>CartaoCreditoNumeroInvalidoException</code> without detail message.
     */
    public CartaoCreditoNumeroInvalidoException() {
    }


    /**
     * Constructs an instance of <code>CartaoCreditoNumeroInvalidoException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CartaoCreditoNumeroInvalidoException(String msg) {
        super(msg);
    }
}
