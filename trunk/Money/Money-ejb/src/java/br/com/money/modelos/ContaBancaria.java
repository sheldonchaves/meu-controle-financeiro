/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos;

import br.com.money.enums.TipoConta;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name = "money_conta_bancaria",
uniqueConstraints =
@UniqueConstraint(name = "uk_nomeconta_tipoconta", columnNames = {"ds_conta", "tipoConta"}))
public class ContaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false,unique=true)
    private Long id;

    @Column(name = "ds_conta", nullable = false, length = 100, unique=true)
    private String nomeConta;

    @Column(name = "en_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @Column(name = "vl_saldo", nullable = false)
    private Double saldo = 0.00;

    @Column(name = "fl_status", nullable = false)
    private boolean status = true;

    @OneToMany(mappedBy = "contaBancaria", fetch = FetchType.LAZY, 
            targetEntity = br.com.money.modelos.MovimentacaoFinanceira.class, cascade = CascadeType.ALL)
    private Set<MovimentacaoFinanceira> movimentacaoFinanceira;

    @ManyToOne(targetEntity = br.com.money.modelos.Usuario.class)
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    private Usuario user;



    public Set<MovimentacaoFinanceira> getMovimentacaoFinanceira() {
        if (this.movimentacaoFinanceira == null) {
            this.movimentacaoFinanceira = new HashSet<MovimentacaoFinanceira>();
        }
        return movimentacaoFinanceira;
    }

    public void setMovimentacaoFinanceira(Set<MovimentacaoFinanceira> movimentacaoFinanceira) {
        this.movimentacaoFinanceira = movimentacaoFinanceira;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeConta() {
        return nomeConta;
    }

    public void setNomeConta(String nomeConta) {
        this.nomeConta = nomeConta;
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

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "br.com.money.modelos.ContaBancaria[ id=" + id + " ]";
    }
}
