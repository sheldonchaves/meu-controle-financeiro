/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report.comparator;

import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import java.util.Comparator;

/**
 *
 * @author Guilherme
 */
public class DespesaProcedimentoDataComparator implements Comparator<DespesaProcedimento> {

    @Override
    public int compare(DespesaProcedimento o1, DespesaProcedimento o2) {
        int toReturn = o1.getDate().compareTo(o2.getDate());
        if (toReturn == 0) {
            toReturn = o1.getValor().compareTo(o2.getValor()) * (-1);
        }
         if (toReturn == 0) {
            toReturn = o1.getObservacao().compareToIgnoreCase(o2.getObservacao());
        }
        return toReturn;
    }
}
