/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.classes.comparators;

import br.com.financeiro.auxjsf.classes.TipoValores;
import java.util.Comparator;

/**
 *
 * @author gbvbahia
 */
public class TipoValoresComparator implements Comparator<TipoValores>{

    public int compare(TipoValores o1, TipoValores o2) {
        Double um = somaValores(o1);
        Double dois = somaValores(o2);
        return um.compareTo(dois) * (-1);
   }

    private Double somaValores(TipoValores tvs){
        Double toReturn = 0.0;
        for(int i = 0; i < tvs.getValores().size(); i++){
            if(tvs.getValores().get(i) != null && tvs.getComparar().get(i)){
            toReturn += tvs.getValores().get(i);
            }
        }
        return toReturn;
    }

}
