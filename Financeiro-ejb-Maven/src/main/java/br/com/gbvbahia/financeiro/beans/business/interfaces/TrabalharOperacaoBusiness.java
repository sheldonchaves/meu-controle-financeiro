/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.interfaces;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import javax.ejb.Local;

/**
 *
 * @author Usuário do Windows
 */
@Local
public interface TrabalharOperacaoBusiness {

    /**
     * Marca uma conta, procedimento, como paga, cria a movimentação
     * financeira e altera o saldo da conta bancaria, disponível, debitado.
     *
     * @param procedimento Obrigatorio
     * @param disponivel Obrigatorio.
     * @throws NegocioException
     */
    public void fecharOperacao(Procedimento procedimento, ContaBancaria disponivel) throws NegocioException;

    /**
     * Marca uma conta, procedimento, como aberta, apaga a amovimentação
     * financeira e altera o saldo da conta bancária. Se não encontrar a
     * movimentação financeira, altera somente o status da conta para não
     * paga.
     *
     * @param procedimento Obrigatorio.
     * @throws NegocioException
     */
    public void abrirOperacao(Procedimento procedimento) throws NegocioException;
}
