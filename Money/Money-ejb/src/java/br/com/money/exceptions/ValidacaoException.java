/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.exceptions;

import javax.ejb.ApplicationException;

/**
 *
 * @author gbvbahia
 */
@ApplicationException(rollback = true)
public abstract class ValidacaoException extends RuntimeException {

    private String atributoName;
    private Object[] variacoes;
    public ValidacaoException(String message, String atributoName) {
        super(message);
        this.atributoName = atributoName;
    }

    public ValidacaoException(String msg) {
        super(msg);
    }

    public String getAtributoName() {
        return atributoName;
    }

    public void setAtributoName(String atributoName) {
        this.atributoName = atributoName;
    }

    public Object[] getVariacoes() {
        return variacoes;
    }

    public void setVariacoes(Object[] variacoes) {
        this.variacoes = variacoes;
    }
}
