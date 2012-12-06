/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report.comparator;

/**
 *
 * @author Guilherme
 */
public class DetalheValorComparator implements Comparable<DetalheValorComparator> {

    private String detalhe;
    private double valor;

    public DetalheValorComparator(String detalhe, double valor) {
        this.detalhe = detalhe;
        this.valor = valor;
    }

    public void addValor(double valor) {
        this.valor = this.valor + valor;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.detalhe != null ? this.detalhe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetalheValorComparator other = (DetalheValorComparator) obj;
        if ((this.detalhe == null) ? (other.detalhe != null) : !this.detalhe.equals(other.detalhe)) {
            return false;
        }
        return true;
    }

    public String getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public int compareTo(DetalheValorComparator o) {
        return Double.compare(valor, o.valor) * (-1);
    }
}
