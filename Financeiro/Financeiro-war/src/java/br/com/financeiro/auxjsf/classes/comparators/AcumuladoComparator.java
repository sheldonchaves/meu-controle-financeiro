/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.classes.comparators;

import br.com.financeiro.auxjsf.classes.interfaces.AcumuladoInterface;
import java.util.Comparator;

/**
 *
 * @author gbvbahia
 */
public class AcumuladoComparator implements Comparator<AcumuladoInterface>{

    public int compare(AcumuladoInterface o1, AcumuladoInterface o2) {
        return o1.getMesAno().compareTo(o2.getMesAno());
    }

}
