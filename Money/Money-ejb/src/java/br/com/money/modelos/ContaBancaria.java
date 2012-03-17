/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos;

import br.com.money.enums.TipoConta;
import br.com.money.modelos.commons.EntityInterface;
import br.com.money.vaidators.interfaces.ValidadoInterface;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name = "money_conta_bancaria",
uniqueConstraints =
@UniqueConstraint(name = "uk_nomeconta_tipoconta", columnNames = {"ds_conta", "en_tipo"}))
public class ContaBancaria implements ValidadoInterface,
                                      EntityInterface<ContaBancaria> {

    private static final long serialVersionUID = 1L;
    public static final int CARACTERES_NOME_CONTA = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    
    @Column(name = "ds_conta", nullable = false, length = CARACTERES_NOME_CONTA)
    @NotNull
    @Size(max = CARACTERES_NOME_CONTA)
    private String nomeConta;
    
    @Column(name = "en_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoConta tipoConta;
    
    @Column(name = "vl_saldo", nullable = false)
    @NotNull
    private Double saldo = 0.00;
    
    @Column(name = "fl_status", nullable = false)
    private boolean status = true;
    
    @OneToMany(mappedBy = "contaBancariaDebitada", fetch = FetchType.LAZY,
    targetEntity = br.com.money.modelos.MovimentacaoFinanceira.class, cascade = CascadeType.ALL)
    private Set<MovimentacaoFinanceira> movimentacaoFinanceira;
    
    @OneToMany(mappedBy = "contaBancariaTransferida", fetch = FetchType.LAZY,
    targetEntity = br.com.money.modelos.MovimentacaoFinanceira.class, cascade = CascadeType.ALL)
    private Set<MovimentacaoFinanceira> movimentacaoFinanceiraTransferida;
    
    @ManyToOne(targetEntity = br.com.money.modelos.Usuario.class)
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    @NotNull
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

    @Override
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

    public Set<MovimentacaoFinanceira> getMovimentacaoFinanceiraTransferida() {
        if (this.movimentacaoFinanceiraTransferida == null) {
            this.movimentacaoFinanceiraTransferida = new HashSet<MovimentacaoFinanceira>();
        }
        return movimentacaoFinanceiraTransferida;
    }

    public void setMovimentacaoFinanceiraTransferida(Set<MovimentacaoFinanceira> movimentacaoFinanceiraTransferida) {
        this.movimentacaoFinanceiraTransferida = movimentacaoFinanceiraTransferida;
    }

    @Override
    public String toString() {
        return "ContaBancaria{" + "id=" + id + ", nomeConta*=" + nomeConta
                + ", tipoConta*=" + tipoConta + ", saldo=" + saldo
                + ", status=" + status + '}';
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(ContaBancaria o) {
        int i = 0;
        if (i == 0) {
            i = this.nomeConta.compareTo(o.nomeConta);
        }
        if (i == 0) {
            i = this.tipoConta.getOrdem().compareTo(o.tipoConta.getOrdem());
        }
        if (i == 0) {
            i = this.id.compareTo(o.id);
        }
        return i;
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
        if ((this.nomeConta == null) ? (other.nomeConta != null) : !this.nomeConta.equals(other.nomeConta)) {
            return false;
        }
        if (this.tipoConta != other.tipoConta) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.tipoConta != null ? this.tipoConta.hashCode() : 0);
        return hash;
    }

    @Override
    public String getLabel() {
        return this.tipoConta.getAbreviacao() + " - " + StringUtils.substring(this.nomeConta, 0, 20);
    }
    
    public String getNomeLimitado(){
        return StringUtils.substring(this.nomeConta, 0, 20);
    }
}
