/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
     * Retorna uma data no formato de String DD/MM/AAAA HH:mm:ss.
     *
     * @param date java.util.Date
     * @return java.lang.String
     */
    public static String getDateDiaMesAno(final Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (date == null) {
            return sd.format(new Date());
        }
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

    /**
     * Formata a data, formato AAAAMMDD, para ser incluida na nomenclatura
     * de um método.
     *
     * @return java.lang.String yyyMMdd
     */
    public static String getDateDirect(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        if (date == null) {
            return sd.format(new Date());
        }
        return sd.format(date);
    }

    /**
     * Formata a data do dia, formato DD/MM/AA.
     *
     * @param date Data a ser formatada.
     * @return java.lang.String DD/MM/AA
     */
    public static String getDateToString(final Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yy");
        return sd.format(date);
    }

    /**
     * Converte uma data em String para um objeto date.
     * @param date A data a ser convertida.
     * @param pattern O formato da data passada, dd/MM/yyyy ou MM/dd/yy...
     * @return Date referente a data passada.
     */
    public static Date convertStringToCalendar(String date, String pattern) {
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        try {
            return sd.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
