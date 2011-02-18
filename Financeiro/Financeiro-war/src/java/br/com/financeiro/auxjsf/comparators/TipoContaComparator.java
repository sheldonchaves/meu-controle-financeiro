/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.comparators;

import br.com.financeiro.entidades.enums.TipoConta;
import java.util.Comparator;

/**
 *
 * @author Guilherme
 */
public class TipoContaComparator implements Comparator<TipoConta>{

    public int compare(TipoConta o1, TipoConta o2) {
        return o1.getTipoContaString().compareToIgnoreCase(o2.getTipoContaString());
    }



}
