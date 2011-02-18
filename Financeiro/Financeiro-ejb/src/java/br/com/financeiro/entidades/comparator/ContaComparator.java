/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades.comparator;

import br.com.financeiro.entidades.interfaces.Conta;
import java.util.Comparator;

/**
 *
 * @author gbvbahia
 */
public class ContaComparator implements Comparator<Conta> {

    public int compare(Conta o1, Conta o2) {
        int i = 0;
        if (i == 0) {
            i = o1.getContaDataConta().compareTo(o2.getContaDataConta());
        }
        if (i == 0) {
            i = o1.getContaValor().compareTo(o2.getContaValor()) * (-1);
        }
        return i;
    }
}
