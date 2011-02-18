/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.entidades.idcompostos;

import java.io.Serializable;

/**
 *
 * @author Guilherme
 */
public class ContaBancariaPK implements Serializable{
private String agencia;
private String numeroConta;

    public ContaBancariaPK() {
    }

    public ContaBancariaPK(String agencia, String numeroConta) {
        this.agencia = agencia;
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContaBancariaPK other = (ContaBancariaPK) obj;
        if ((this.agencia == null) ? (other.agencia != null) : !this.agencia.equals(other.agencia)) {
            return false;
        }
        if ((this.numeroConta == null) ? (other.numeroConta != null) : !this.numeroConta.equals(other.numeroConta)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.agencia != null ? this.agencia.hashCode() : 0);
        return hash;
    }

    

}
