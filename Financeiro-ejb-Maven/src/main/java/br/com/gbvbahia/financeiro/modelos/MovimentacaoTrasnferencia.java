/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.modelos.superclass.MovimentacaoFinanceira;
import java.math.BigDecimal;
import javax.persistence.*;

/**
 * Representa uma movimentação financeira entre uma conta e outra conta, uma
 * conta é debitada e outra creditada.
 *
 * @since v.3 03/06/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_movimentacao_financeira_trasnferencia")
@DiscriminatorValue("TRANSFERENCIA")
@NamedQueries({
    @NamedQuery(name = "MovimentacaoTrasnferencia.selectMovimentacao",
    query = "Select distinct m From MovimentacaoTrasnferencia m "
    + " WHERE (:dataMovimentacao2 = 'todos' OR m.dataMovimentacao = :dataMovimentacao) "
    + " AND (:contaBancariaDebitada2 = 'todos' OR m.contaBancariaDebitada = :contaBancariaDebitada) "
    + " AND (:contaBancariaTransferida2 = 'todos' OR m.contaBancariaTransferida = :contaBancariaTransferida) "
    + " AND (m.contaBancariaDebitada.usuario = :usuario OR m.contaBancariaDebitada.usuario.conjuge = :usuario) "
    + " ORDER BY m.dataMovimentacao desc "),
    @NamedQuery(name = "MovimentacaoTrasnferencia.countMovimentacao",
    query = "Select count(distinct m) From MovimentacaoTrasnferencia m "
    + " WHERE (:dataMovimentacao2 = 'todos' OR m.dataMovimentacao = :dataMovimentacao) "
    + " AND (:contaBancariaDebitada2 = 'todos' OR m.contaBancariaDebitada = :contaBancariaDebitada) "
    + " AND (:contaBancariaTransferida2 = 'todos' OR m.contaBancariaTransferida = :contaBancariaTransferida) "
    + " AND (m.contaBancariaDebitada.usuario = :usuario OR m.contaBancariaDebitada.usuario.conjuge = :usuario) ")
})
public class MovimentacaoTrasnferencia extends MovimentacaoFinanceira {

    /**
     * O saldo anterior da conta que recebeu a transferencia.
     */
    @Column(name = "vl_saldo_transferida_anterior")
    private BigDecimal saldoTransferidaAnterior;
    /**
     * O saldo posterior da conta que recebeu a trsnferência.
     */
    @Column(name = "vl_saldo_transferida_posterior")
    private BigDecimal saldoTransferidaPosterior;
    /**
     * A conta que recebeu a trsnferência.
     */
    @ManyToOne
    @JoinColumn(name = "fk_conta_bancaria_transferida_id")
    private ContaBancaria contaBancariaTransferida;
    /**
     * 
     */
    @Transient
    private boolean marcadoTransient;

    @Override
    public String getLabel() {
        return getContaBancariaDebitada().getLabel()
                + " => "
                + contaBancariaTransferida.getLabel();
    }

    /**
     * A conta que recebeu a trsnferência.
     *
     * @return A conta que recebeu a trsnferência.
     */
    public ContaBancaria getContaBancariaTransferida() {
        return contaBancariaTransferida;
    }

    /**
     * A conta que recebeu a trsnferência.
     *
     * @param vContaTrans A conta que recebeu a trsnferência.
     */
    public void setContaBancariaTransferida(final ContaBancaria vContaTrans) {
        this.contaBancariaTransferida = vContaTrans;
    }

    /**
     * O saldo anterior da conta que recebeu a transferencia.
     *
     * @return O saldo anterior da conta que recebeu a transferencia.
     */
    public BigDecimal getSaldoTransferidaAnterior() {
        return saldoTransferidaAnterior;
    }

    /**
     * O saldo anterior da conta que recebeu a transferencia.
     *
     * @param vSaldoAnterior O saldo anterior da conta que recebeu a
     * transferencia.
     */
    public void setSaldoTransferidaAnterior(final BigDecimal vSaldoAnterior) {
        this.saldoTransferidaAnterior = vSaldoAnterior;
    }

    /**
     * O saldo posterior da conta que recebeu a trsnferência.
     *
     * @return O saldo posterior da conta que recebeu a trsnferência.
     */
    public BigDecimal getSaldoTransferidaPosterior() {
        return saldoTransferidaPosterior;
    }

    /**
     * O saldo posterior da conta que recebeu a trsnferência.
     *
     * @param vSaldoPosterior O saldo posterior da conta que recebeu a
     * trsnferência.
     */
    public void setSaldoTransferidaPosterior(final BigDecimal vSaldoPosterior) {
        this.saldoTransferidaPosterior = vSaldoPosterior;
    }
    
    @Override
    public boolean isMarcadoTransient() {
        return marcadoTransient;
    }
    
    @Override
    public void setMarcadoTransient(boolean marcadoTransient) {
        this.marcadoTransient = marcadoTransient;
    }
}
