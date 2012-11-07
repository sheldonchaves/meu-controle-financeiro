/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos.superclass;

import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.financeiro.utils.I18N;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Representa uma movimentação financeira, uma movimentação pode ser a
 * saída de dinherio de uma conta para um procedimento (uma conta a pagar)
 * ou pode ser uma transferência de uma conta para outra.<br> Essa classe
 * abstrata contêm as informações comum de uma transferência que são comuns
 * as duas situações, cada implementação irá ter suas informações
 * pertinentes a situação que deseja representar.
 *
 * @since v.3 03/06/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_movimentacao_financeira")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo",
discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("MOVIMENTACAO")
public abstract class MovimentacaoFinanceira
        implements EntityInterface<MovimentacaoFinanceira>, Serializable {

    /**
     * ID único no banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * Data que a movimentação foi realizada.
     */
    @Column(name = "dt_movimentacao", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dataMovimentacao = new Date();
    /**
     * Salndo antes da movimentação.
     */
    @Column(name = "vl_saldo_anterior", nullable = false)
    @NotNull
    private BigDecimal saldoAnterior;
    /**
     * Saldo posterior a movimentação.
     */
    @Column(name = "vl_saldo_posterior", nullable = false)
    @NotNull
    private BigDecimal saldoPosterior;
    /**
     * A conta bancária que sofreu a movimentação.
     */
    @ManyToOne
    @JoinColumn(name = "fk_conta_bancaria_debitada_id",
    referencedColumnName = "id", nullable = false)
    @NotNull
    private ContaBancaria contaBancariaDebitada;
    /**
     * A classe que estende Movimentação deverá passar uma string para
     * identificação.
     */
    @Transient
    private final String tipoMovimentacao;

    /**
     * Somente pode ser instânciada se o tipo que a extende se identificar.
     *
     * @param vTipoMovimentacao String que representa o tipo de
     * movimentação.
     */
    public MovimentacaoFinanceira(final String vTipoMovimentacao) {
        this.tipoMovimentacao = vTipoMovimentacao;
    }

    /**
     * Construtor nunca executado, se for uma runtime será lançada.
     */
    public MovimentacaoFinanceira() {
        throw new IllegalArgumentException(
                I18N.getMsg("MovimentacaoConstrutorErro"));
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(final MovimentacaoFinanceira o) {
        int i = 0;
        i = this.dataMovimentacao.compareTo(o.dataMovimentacao);
        if (i == 0) {
            i = this.contaBancariaDebitada.compareTo(o.contaBancariaDebitada);
        }
        return i;
    }

    /**
     * A conta bancária que sofreu a movimentação.
     * @return Conta Movimentada.
     */
    public ContaBancaria getContaBancariaDebitada() {
        return contaBancariaDebitada;
    }

    /**
     * A conta bancária que sofreu a movimentação.
     * @param vContaDebitada Conta Movimentada.
     */
    public void setContaBancariaDebitada(final ContaBancaria vContaDebitada) {
        this.contaBancariaDebitada = vContaDebitada;
    }

    /**
     * Data em que a movimentação ocorreu.
     * @return Data Movimentação.
     */
    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    /**
     * Data em que a movimentação ocorreu.
     * @param vDataMovimentacao Data Movimentação.
     */
    public void setDataMovimentacao(final Date vDataMovimentacao) {
        this.dataMovimentacao = vDataMovimentacao;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * ID único do BD.
     * @param vId único do BD
     */
    public void setId(final Long vId) {
        this.id = vId;
    }

    /**
     * Saldo da conta debitada antes da movimentação.
     * @return Saldo da conta antes.
     */
    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    /**
     * Saldo da conta debitada antes da movimentação.
     * @param vSaldoAnterior Saldo da conta antes.
     */
    public void setSaldoAnterior(final BigDecimal vSaldoAnterior) {
        this.saldoAnterior = vSaldoAnterior;
    }

    /**
     * Saldo após a movimentação.
     * @return Saldo posterior a movimentação.
     */
    public BigDecimal getSaldoPosterior() {
        return saldoPosterior;
    }

    /**
     * Saldo após a movimentação.
     * @param vSaldoPosterior Saldo posterior a movimentação.
     */
    public void setSaldoPosterior(final BigDecimal vSaldoPosterior) {
        this.saldoPosterior = vSaldoPosterior;
    }

    /**
     * Uma String que representa a classe que implementa.
     * @return TIPO
     */
    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }
}