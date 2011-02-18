/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gbvbahia
 */
public class TipoValores {

    private String tipo;
    private List<Double> valores = new ArrayList<Double>();
    private List<Integer> meses = new ArrayList<Integer>();
    private List<Integer> anos = new ArrayList<Integer>();
    private List<Boolean> comparar = new ArrayList<Boolean>();
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void addValor(double valor){
        valores.add(valor);
    }

    public void addMes(int mes){
        this.meses.add(mes);
    }

    public void addAno(int ano){
        this.anos.add(ano);
    }

    public List<Double> getValores() {
        return valores;
    }

    public void setValores(List<Double> valores) {
        this.valores = valores;
    }

    public List<Integer> getAnos() {
        return anos;
    }

    public void setAnos(List<Integer> anos) {
        this.anos = anos;
    }

    public List<Integer> getMeses() {
        return meses;
    }

    public void setMeses(List<Integer> meses) {
        this.meses = meses;
    }

    public List<Boolean> getComparar() {
        return comparar;
    }

    public void addComparar(boolean bol){
        this.comparar.add(bol);
    }

    public void setComparar(List<Boolean> comparar) {
        this.comparar = comparar;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoValores other = (TipoValores) obj;
        if ((this.tipo == null) ? (other.tipo != null) : !this.tipo.equals(other.tipo)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.tipo != null ? this.tipo.hashCode() : 0);
        return hash;
    }

    
}
