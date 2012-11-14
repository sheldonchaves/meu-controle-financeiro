/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.constante;

/**
 *
 * @author Usuário do Windows
 */
public enum Meses {

    JAN(0), FEV(1), MAR(2), ABR(3), MAI(4), JUN(5), JUL(6), AGO(7),
    SET(8), OUT(9), NOV(10), DEZ(11);
    int mes;

    private Meses(int mes) {
        this.mes = mes;
    }

    public int getMes() {
        return mes;
    }
}
