/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *  Utilizada para passar problemas no cadastro do conjuge
 * @author gbvbahia
 */
public class ProprietarioConjugeException extends Exception {

    /**
     * Constructs an instance of <code>ProprietarioConjugeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProprietarioConjugeException(String msg) {
        super(msg);
    }
}
