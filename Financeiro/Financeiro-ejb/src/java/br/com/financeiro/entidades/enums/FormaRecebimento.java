/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.entidades.enums;

/**
 *
 * @author gbvbahia
 */
public enum FormaRecebimento {
TRASFERENCIA_ELETRONICA("Trasferência Eletrônica"), DEPOSITO_BANCARIO("Depósito Bancário");

private String formaRecebimento;

    private FormaRecebimento(String formaRecebimento) {
        this.formaRecebimento = formaRecebimento;
    }

    public String getFormaRecebimento() {
        return formaRecebimento;
    }

    @Override
    public String toString() {
        return getFormaRecebimento();
    }

}
