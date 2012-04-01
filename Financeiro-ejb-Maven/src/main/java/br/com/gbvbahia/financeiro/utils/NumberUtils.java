/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Classe auxiliar para trabalhar com Números.
 *
 * @since v.1 22/03/2012
 * @author Guilherme Braga
 */
public final class NumberUtils {

    /**
     * Classe exclusiva de métodos estaticos, não pode ser instânciada.
     */
    private NumberUtils() {
    }

    /**
     * Retorna null se nulo ou String do número. Evita nullPointerException
     *
     * @param numero Numero a ser tratado
     * @return null se nulo ou String de um numero: Exp: 2 retorna "2" (Sem
     * aspas).
     */
    public static String numeroString(final Integer numero) {
        String anoString = numero == null ? "" : numero.toString();
        return anoString;
    }

    /**
     * Formata o valor passado para moeda Brasileira. 1,000.00 = R$ 1.000,00
     *
     * @param valor java.lang.Double a ser formatado.
     * @return java.lang.String valor formatado.
     */
    public static String formatCurrency(final Double valor) {
        NumberFormat nf = getCurencyNumberFormat();
        return nf.format(valor);
    }

    /**
     * Retorna um formatador para exibição de números financeiro:<br> Ex:
     * 65.7654 formata para R$ 65,77<br> Altera sua formatação será de acordo
     * com o padrão Brasileiro<br> Ex: 1,000.98 converte para 1.000,98.
     *
     * @return NumberFormat utilize o método format.
     */
    public static NumberFormat getCurencyNumberFormat() {
        NumberFormat nf =
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return nf;
    }

    /**
     * Retorna um valor na forma monetária, BR.
     *
     * @param n Número a ser formatado.
     * @return String R$ #.###,##
     */
    public static String currencyFormat(final Double n) {
         NumberFormat nf = getCurencyNumberFormat();
        if (n == null) {
            return nf.format(0.00);
        }
        return nf.format(n);
    }

    /**
     * Para exibir informações do progresso é necessário um calculo do atual
     * pelo total, como os valores são inteiros é perdido as casas decimais,
     * esse método é para tornar essa tarefa transparente no negócio.
     *
     * @param atual
     * @param total
     * @return java.lang.Integer
     */
    public static Integer calculaProgresso(Integer atual, Integer total) {
        return atual * 100 / total;
    }

}
