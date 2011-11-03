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
        @JoinColumn(name="fk_conta_bancaria_id", referencedColumnName="id",nullable=false)
    private ContaBancaria contaBancaria;
    
    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_receita_divida_id", referencedColumnName = "id")
    private ReceitaDivida receitaDivida;

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
        return "br.com.money.modelos.MovimentacaoFinanceira[ id=" + id + " ]";
    }

    @Override
    public int compareTo(MovimentacaoFinanceira o) {
        int i = 0;
        if(i == 0) i = this.contaBancaria.compareTo(o.contaBancaria);
        if(i == 0) i = this.dataMovimentacao.compareTo(o.dataMovimentacao);
        if(i == 0) i = this.saldoPosterior.compareTo(o.saldoPosterior) *(-1);
        if(i == 0) i = this.id.compareTo(o.id);
        return i;
    }

    public MovimentacaoFinanceira() {
    }

    public MovimentacaoFinanceira(ContaBancaria contaBancaria, ReceitaDivida receitaDivida) {
        this.contaBancaria = contaBancaria;
        this.receitaDivida = receitaDivida;
        this.saldoAnterior = contaBancaria.getSaldo();
        this.saldoPosterior = contaBancaria.getSaldo() + receitaDivida.getValorParaCalculoDireto();
    }
    
}
