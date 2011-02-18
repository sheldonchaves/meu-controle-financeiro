/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.entidades.enums;

/**
 *
 * @author gbvbahia
 */
public enum FormaPagamento {
CARTAO_DE_CREDITO("Cartão de Crédito"), DEBITO_EM_CONTA("Débito em Conta"),SAQUE_CAIXA_ELETRONICO("Saque Caixa Eletrônico"),
TRASFERENCIA_ELETRONICA("Trasferência Eletrônica"), CHEQUE("Cheque");

private String formaPagamento;

    private FormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    @Override
    public String toString() {
        return getFormaPagamento();
    }


}
