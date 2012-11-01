/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.constantes;

import java.util.Calendar;

/**
 * Enum que define o periodo de um agendamento, se DIAS o agendamento
 * será inserido considerando a quantidade informada como dias, se
 * MESES quantidade será relativo a meses e ANOS será por anos. <br>
 * Ex: calendar.add(Calendar.MONTH, qutdade);
 *
 * @since v.3 01/04/2012
 * @author Guilherme
 */
public enum Periodo {

    /**
     * A quantidade será recalculada em dias do mês.
     */
    DIAS(Calendar.DAY_OF_MONTH, "Dias"),
    /**
     * A quantidade será calculada em meses do ano.
     */
    MESES(Calendar.MONTH, "Meses"),
    /**
     * A quantidade será calculada em anos.
     */
    ANOS(Calendar.YEAR, "Anos");
    /**
     * Um inteiro representado das constantes de Calendar.
     */
    private int periodo;
    /**
     * Forma de exibição ao usuário.
     */
    private String label;

    /**
     * Construtor padrão enum.
     *
     * @param periodoTemporal Um inteiro representado das constantes
     * de Calendar.
     * @param labelPerido Forma de exibição ao usuário.
     */
    private Periodo(final int periodoTemporal, final String labelPerido) {
        this.periodo = periodoTemporal;
        this.label = labelPerido;
    }

    /**
     * A forma de calculo considerada para quantidade.
     *
     * @return Um iteiro das contantes de Calendar.
     */
    public int getPeriodo() {
        return periodo;
    }

    /**
     * Forma de exibição ao usuário.
     * @return String
     */
    public String getLabel() {
        return label;
    }
    
}
