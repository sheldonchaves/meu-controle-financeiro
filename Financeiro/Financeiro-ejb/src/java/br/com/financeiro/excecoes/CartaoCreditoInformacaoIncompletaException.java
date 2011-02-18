/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 * Lançada para casos em que o cartão de crétido não venha com informações
 * obrigatórias
 * @author gbvbahia
 */

public class CartaoCreditoInformacaoIncompletaException extends Exception {

   /**
     * Constructs an instance of <code>CartaoCreditoInformacaoIncompletaException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CartaoCreditoInformacaoIncompletaException(String msg) {
        super(msg);
    }
}
