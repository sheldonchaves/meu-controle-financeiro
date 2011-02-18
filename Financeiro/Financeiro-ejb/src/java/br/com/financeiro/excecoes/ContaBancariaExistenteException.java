/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.excecoes;

/**
 *
 * @author Guilherme
 */

public class ContaBancariaExistenteException extends Exception{

    public ContaBancariaExistenteException(String message) {
        super(message);
    }
}
