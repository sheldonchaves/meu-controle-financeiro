/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades;

import br.com.financeiro.entidades.enums.TipoMovimentacao;
import br.com.financeiro.excecoes.MovimentacaoFinanceiraException;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *  Representa uma movimentação, uma conta que foi paga ou um salário recebido
 * @author Guilherme
 */
@Entity
@Table(name = "movimentacoes_financeiras")
public class MovimentacaoFinanceira implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",nullable=false,unique=true)
    private Integer id;
    /**
     * Crédito ou Débito
     */
    @Column(name="tipo_movimentacao", nullable=false)
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipoMovimentacao;

    @Column(name="data_movimentacao", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date dataMovimentacao;

    @Column(name="saldo_anterior", nullable=false)
    private Double saldoAnterior;

    @Column(name="saldo_posterior", nullable=false)
    private Double saldoPosterior;
    /**
     * A conta bancária que sofreu a movimentação
     */
    @ManyToOne(targetEntity=br.com.financeiro.entidades.ContaBancaria.class)
    @JoinColumns({
        @JoinColumn(name="agencia_id", referencedColumnName="agencia_id",nullable=false),
        @JoinColumn(name="numero_conta_id", referencedColumnName="numero_conta",nullable=false)
    })
    private ContaBancaria contaBancaria;

   @OneToOne(mappedBy = "movimentacaoFinanceira", targetEntity = br.com.financeiro.entidades.ContaPagar.class)
    private ContaPagar contaPagar;

   @OneToOne(mappedBy = "movimentacaoFinanceira", targetEntity = br.com.financeiro.entidades.ContaReceber.class)
    private ContaReceber contaReceber;

    @PostPersist
    @PostUpdate
    private void verificaConsistenciaMovimentacaoFinanceira() throws MovimentacaoFinanceiraException{
        if(this.tipoMovimentacao == null){
            throw new MovimentacaoFinanceiraException("Tipo de Movimentação deve ser informado!");
        }
        if(dataMovimentacao == null){
            throw new MovimentacaoFinanceiraException("A data da Movimentação deve ser informada!");
        }
        if(this.saldoAnterior == null){
            throw new MovimentacaoFinanceiraException("O saldo anterior deve ser informado!");
        }
        if(this.saldoPosterior == null){
            throw new MovimentacaoFinanceiraException("O saldo posterior deve ser informado!");
        }
        if(this.contaBancaria == null){
            throw new MovimentacaoFinanceiraException("A conta bancária deve ser informada!");
        }
        if(this.contaPagar == null && this.contaReceber == null){
            throw new MovimentacaoFinanceiraException("Uma movimentação de pagamento ou recebimento deve ser informada!");
        }else if(this.contaPagar != null && this.contaReceber != null){
            throw new MovimentacaoFinanceiraException("Uma movimentação pode conter uma conta a pagar ou uma receita financeira, nunca as duas!");
        }
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovimentacaoFinanceira other = (MovimentacaoFinanceira) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    public Double getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(Double saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public Double getSaldoPosterior() {
        return saldoPosterior;
    }

    public void setSaldoPosterior(Double saldoPosterior) {
        this.saldoPosterior = saldoPosterior;
    }

    public ContaPagar getContaPagar() {
        return contaPagar;
    }

    public void setContaPagar(ContaPagar contaPagar) {
        this.contaPagar = contaPagar;
    }

    public ContaReceber getContaReceber() {
        return contaReceber;
    }

    public void setContaReceber(ContaReceber contaReceber) {
        this.contaReceber = contaReceber;
    }



}
