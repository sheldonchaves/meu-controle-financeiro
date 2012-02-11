/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.utils;

/**
 *
 * @author Guilherme
 */
public class ValidacaoException  extends br.com.money.exceptions.ValidacaoException{

        public ValidacaoException(String msg) {
            super(msg);
        }

        public ValidacaoException(String message, String atributoName) {
            super(message, atributoName);
        }
       
   
    
}
