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
     * Formata a data, formato AAAAMMDD, para ser incluida na nomenclatura de
     * um método.
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
     *
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

    /**
     * Zera a hora, minuto e segunda de uma data.
     *
     * @param data
     * @return Data zerada nas horas, minutos e segundos ou nulo se parametro
     * for nulo.
     */
    public static Date zerarHora(Date data) {
        if (data != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(data);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTime();
        }
        return data;
    }

    /**
     * Incrementa campo solicitado com a quantidade solicitada.
     *
     * @param data Data a ser incrementada
     * @param qtdade Quantidade a ser inserido.
     * @param calendarField Campo a ser incrementado: Calendar.YEAR,
     * Calendar.HOUR_OF_DAY
     * @return Data incrementada, se data null retorna null.
     */
    public static Date incrementar(Date data, int qtdade, int calendarField) {
        if (data == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(calendarField, qtdade);
        return c.getTime();
    }

    /**
     * Retorna o valor do field solicitado.
     * @param data
     * @param calendarField Calendar.YEAR, Calendar.MONTH...
     * @return 
     */
    public static Integer getFieldDate(Date data, int calendarField) {
        if(data == null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        return c.get(calendarField);
    }
}
