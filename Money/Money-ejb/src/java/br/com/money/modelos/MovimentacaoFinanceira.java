/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos;

import br.com.money.vaidators.interfaces.ValidadoInterface;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name="money_movimentacao_financeira")
public class MovimentacaoFinanceira implements ValidadoInterface, Comparable<MovimentacaoFinanceira> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false,unique=true)
    private Long id;
    
    @Column(name="dt_movimentacao", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date dataMovimentacao = new Date();

    @Column(name="vl_saldo_anterior", nullable=false)
    private Double saldoAnterior;

    @Column(name="vl_saldo_posterior", nullable=false)
    private Double saldoPosterior;
    /**
     * A conta bancária que sofreu a movimentação
     */
    @ManyToOne
    @JoinColumn(name="fk_conta_bancaria_debitada_id", referencedColumnName="id",nullable=false)
    private ContaBancaria contaBancariaDebitada;
    
    @Column(name="vl_saldo_transferida_anterior")
    private Double saldoTransferidaAnterior;

    @Column(name="vl_saldo_transferida_posterior")
    private Double saldoTransferidaPosterior;
    
    @ManyToOne
    @JoinColumn(name="fk_conta_bancaria_transferida_id")
    private ContaBancaria contaBancariaTransferida;
    
    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_receita_divida_id", referencedColumnName = "id")
    private ReceitaDivida receitaDivida;

    public ContaBancaria getContaBancariaDebitada() {
        return contaBancariaDebitada;
    }

    public void setContaBancariaDebitada(ContaBancaria contaBancaria) {
        this.contaBancariaDebitada = contaBancaria;
    }

    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public Double getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(Double saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    /**
     * Saldo, OBRIGATÓRIO, referente a conta que será debitada
     * @return 
     */
    public Double getSaldoPosterior() {
        return saldoPosterior;
    }

    /**
     * Saldo OBRIGATÓRIO, referente a conta que será debitada
     * @param saldoPosterior 
     */
    public void setSaldoPosterior(Double saldoPosterior) {
        this.saldoPosterior = saldoPosterior;
    }

    public ReceitaDivida getReceitaDivida() {
        return receitaDivida;
    }

    public void setReceitaDivida(ReceitaDivida receitaDivida) {
        this.receitaDivida = receitaDivida;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContaBancaria getContaBancariaTransferida() {
        return contaBancariaTransferida;
    }

    public void setContaBancariaTransferida(ContaBancaria contaBancariaTransferida) {
        this.contaBancariaTransferida = contaBancariaTransferida;
    }

    /**
     * Saldo não obrigatorio, referente a conta creditada, para os casos de transferencia entre contas
     * @return 
     */
    public Double getSaldoTransferidaAnterior() {
        return saldoTransferidaAnterior;
    }
    /**
     * Saldo não obrigatorio, referente a conta creditada, para os casos de transferencia entre contas
     * @param saldoTransferidaAnterior 
     */
    public void setSaldoTransferidaAnterior(Double saldoTransferidaAnterior) {
        this.saldoTransferidaAnterior = saldoTransferidaAnterior;
    }

    public Double getSaldoTransferidaPosterior() {
        return saldoTransferidaPosterior;
    }

    public void setSaldoTransferidaPosterior(Double saldoTransferidaPosterior) {
        this.saldoTransferidaPosterior = saldoTransferidaPosterior;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimentacaoFinanceira)) {
            return false;
        }
        MovimentacaoFinanceira other = (MovimentacaoFinanceira) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovimentacaoFinanceira{" + "id=" + id + ", dataMovimentacao=" + dataMovimentacao + ", saldoAnterior=" + saldoAnterior + ", saldoPosterior=" + saldoPosterior + ", saldoTransferidaAnterior=" + saldoTransferidaAnterior + ", saldoTransferidaPosterior=" + saldoTransferidaPosterior + '}';
    }

    @Override
    public int compareTo(MovimentacaoFinanceira o) {
        int i = 0;
        if(i == 0) i = this.contaBancariaDebitada.compareTo(o.contaBancariaDebitada);
        if(i == 0) i = this.dataMovimentacao.compareTo(o.dataMovimentacao);
        if(i == 0) i = this.saldoPosterior.compareTo(o.saldoPosterior) *(-1);
        if(i == 0) i = this.id.compareTo(o.id);
        return i;
    }

    public MovimentacaoFinanceira() {
    }

    /**
     * Utilize para dar baixa de em pagamentos ou receitas.<br>
     * A movimentação financeira deve ser criada antes de qualquer alteração na Conta debitada,
     * afim de não ter os valores de saldo anterior e posterior incorretos.
     * @param contaBancariaDebitada
     * @param receitaDivida 
     */
    public MovimentacaoFinanceira(ContaBancaria contaBancariaDebitada, ReceitaDivida receitaDivida) {
        this.contaBancariaDebitada = contaBancariaDebitada;
        this.receitaDivida = receitaDivida;
        this.saldoAnterior = contaBancariaDebitada.getSaldo();
        this.saldoPosterior = contaBancariaDebitada.getSaldo() + receitaDivida.getValorParaCalculoDireto();
    }

    /**
     * Utilize para quando for criar uma transferncia entre contas bancárias.<br>
     * A movimentação financeira deve ser criada antes de realizar a operação nas contas, para que as informações do saldo
     * anterior e posterior da conta Debitada fiquem corretos
     * @param contaBancariaDebitada Conta que terá o valor descontado
     * @param contaBancariaTransferida Conta que receberá o valor
     * @param valor O valor propriamente dito
     */
    public MovimentacaoFinanceira(ContaBancaria contaBancariaDebitada, ContaBancaria contaBancariaTransferida, Double valor) {
        this.contaBancariaDebitada = contaBancariaDebitada;
        this.contaBancariaTransferida = contaBancariaTransferida;
        this.saldoAnterior = contaBancariaDebitada.getSaldo();
        this.saldoPosterior = contaBancariaDebitada.getSaldo() - valor;
        this.saldoTransferidaAnterior = contaBancariaTransferida.getSaldo();
        this.saldoTransferidaPosterior = contaBancariaTransferida.getSaldo() + valor;
    }
    
}
