/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos.dto;

import br.com.gbvbahia.financeiro.utils.DateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Utilizado para mascarar a data mínima e máxima de procedimentos.
 *
 * @author Guilherme
 */
public class MinMaxDateDTO {

    /**
     * Menor data de um intervalo.
     */
    public Date minData;
    /**
     * Maior data de um intervalo.
     */
    public Date maxData;

    /**
     * Recebe a menor e maior data de um intervalo.
     *
     * @param minData
     * @param maxDate
     */
    public MinMaxDateDTO(Date minData, Date maxData) {
        this.minData = minData;
        this.maxData = maxData;
    }

    /**
     * Recebe a menor e maior data de um intervalo.
     *
     * @param minData
     * @param maxDate
     */
    public MinMaxDateDTO(Date minData, Date minData2, Date maxData, Date maxData2) {
        this.minData = DateUtils.getMenor(minData, minData2);
        this.maxData = DateUtils.getMaior(maxData, maxData2);
    }

    public Date getMinData() {
        return minData;
    }

    public Date getMaxData() {
        return maxData;
    }

    /**
     * Devolve uma lista sem repetições de modo ordenado do menor para o
     * maior do intervalo em anos do menor para a maior data.
     *
     * @return
     */
    public List<Integer> intervaloMinMaxAnos() {
        List<Integer> toReturn = new ArrayList<Integer>();
        if (minData == null
                || maxData == null) {
            return toReturn;
        }
        Integer minAno = DateUtils.getFieldDate(minData, Calendar.YEAR);
        Integer maxAno = DateUtils.getFieldDate(maxData, Calendar.YEAR);
        Set<Integer> set = new TreeSet<Integer>();
        for (int i = minAno; i <= maxAno; i++) {
            set.add(i);
        }
        toReturn.addAll(set);
        return toReturn;
    }
}
