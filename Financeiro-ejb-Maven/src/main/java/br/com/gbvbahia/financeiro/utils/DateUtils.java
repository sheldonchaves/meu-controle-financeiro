/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe auxiliar para trabalhar com Datas.
 *
 * @since v.1 22/03/2012
 * @author Guilherme Braga
 */
public final class DateUtils {

    /**
     * Classe exclusiva de métodos estaticos, não pode ser instânciada.
     */
    private DateUtils() {
    }

    /**
     * Retorna uma data no formato de String DD/MM/AAAA.
     *
     * @param date java.util.Date
     * @return java.lang.String
     */
    public static String getDateDiaMesAno(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sd.format(date);
    }

    /**
     * Formata a data do dia, formato AAAAMMDD, para ser incluida na
     * nomenclatura de um método.
     *
     * @return java.lang.String yyyMMdd
     */
    public static String getDateToNameFile() {
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        return sd.format(date);
    }
}
