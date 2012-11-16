/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.interfaces;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoTrasnferencia;
import java.math.BigDecimal;
import javax.ejb.Local;

/**
 *
 * Negócio responsável pela transferenica entre contas.
 * @author Guilherme
 */
@Local
public interface TrabalharTransferenciaBusiness {

    /**
     * Realiza a movimentação financeira de uma conta para outra.
     *
     * @param debitada Conta a ter o valor retirado.
     * @param creditada Conta a ter o valor recebido.
     * @param valor Valor a ser transferido.
     * @throws NegocioException
     */
    void transferirEntreDisponiveis(final ContaBancaria debitada,
            final ContaBancaria creditada, final BigDecimal valor)
            throws NegocioException;

    /**
     * Cancela uma transferencia realizada. As contas terão o valor
     * inserido na debitada e retirado da creditada.
     *
     * @param mt Movimentação criada.
     * @throws NegocioException
     */
    void desfazerTransferencia(final MovimentacaoTrasnferencia mt)
            throws NegocioException;
}
