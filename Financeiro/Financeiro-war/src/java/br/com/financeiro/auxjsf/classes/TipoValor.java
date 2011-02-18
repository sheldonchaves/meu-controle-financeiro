/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.classes;

import br.com.financeiro.auxjsf.classes.interfaces.TipoValorInterface;

/**
 *
 * @author gbvbahia
 */
public class TipoValor implements TipoValorInterface {

    private String tipo;
    private Double valor;

    public TipoValor() {
    }

    public TipoValor(String tipo, Double valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoValor other = (TipoValor) obj;
        if ((this.tipo == null) ? (other.tipo != null) : !this.tipo.equals(other.tipo)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.tipo != null ? this.tipo.hashCode() : 0);
        return hash;
    }

    
}
