/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades;

import br.com.financeiro.entidades.enums.TipoConta;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 * Representa uma conta no banco
 * Itaú, Banco do Brasil...
 * @author Guilherme
 */
@Entity
@Table(name = "conta_bancaria")
@IdClass(br.com.financeiro.entidades.idcompostos.ContaBancariaPK.class)
public class ContaBancaria implements Serializable {

    @Id
    @Column(name = "agencia_id", nullable = false)
    private String agencia;
    @Id
    @Column(name = "numero_conta", nullable = false)
    private String numeroConta;

    @Column(name = "nome_banco", nullable = false, length = 100)
    private String nomeBanco;

    @Column(name = "tipo_conta", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @Column(name = "saldo", nullable = false)
    private Double saldo = 0.00;

    @Column(name = "observacao", nullable = true, length = 255)
    private String observacao;

    @Column(name = "status", nullable = false)
    private boolean status = true;

    @OneToMany(mappedBy = "contaBancaria", fetch = FetchType.LAZY, targetEntity = br.com.financeiro.entidades.MovimentacaoFinanceira.class, cascade = CascadeType.ALL)
    private Set<MovimentacaoFinanceira> movimentacaoFinanceira;
    
    @ManyToOne(targetEntity = br.com.financeiro.entidades.User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", columnDefinition = "integer", nullable = false)
    private User user;

    public String getLabel(){
        if(this.agencia != null && this.numeroConta != null){
            String temp = null;
            if(nomeBanco.length() < 15){
                temp = nomeBanco;
            }else{
                temp = nomeBanco.substring(0,14);
            }
            return temp + " " + this.tipoConta + " " + this.agencia + "/" + this.numeroConta;
        }else {
            return null;
        }
    }


    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public Set<MovimentacaoFinanceira> getMovimentacaoFinanceira() {
        if (this.movimentacaoFinanceira == null) {
            this.movimentacaoFinanceira = new HashSet<MovimentacaoFinanceira>();
        }
        return movimentacaoFinanceira;
    }

    public void setMovimentacaoFinanceira(Set<MovimentacaoFinanceira> movimentacaoFinanceira) {
        this.movimentacaoFinanceira = movimentacaoFinanceira;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContaBancaria other = (ContaBancaria) obj;
        if ((this.agencia == null) ? (other.agencia != null) : !this.agencia.equals(other.agencia)) {
            return false;
        }
        if ((this.numeroConta == null) ? (other.numeroConta != null) : !this.numeroConta.equals(other.numeroConta)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + (this.agencia != null ? this.agencia.hashCode() : 0);
        hash = 61 * hash + (this.numeroConta != null ? this.numeroConta.hashCode() : 0);
        return hash;
    }

    @PrePersist
    private void novaConta() {
        Logger.getLogger(ContaBancaria.class.getName()).log(Level.INFO, "Uma nova conta foi persistida!");
    }
}
