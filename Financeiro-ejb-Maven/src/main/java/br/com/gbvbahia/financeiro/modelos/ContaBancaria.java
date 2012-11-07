/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 * Representa uma conta bancária ou qualquer tipo de local onde deseja
 * armazenar e controlar dinheiro/saldo.<br> Não pode existir um conta para
 * o mesmo usuário, com mesmo tipo e mesmo nome, existe uma restrição a
 * nível de BD.
 *
 * @since v.1 31/03/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_conta_bancaria", uniqueConstraints =
@UniqueConstraint(name = "uk_id_desc_tipo",
columnNames = {"id", "ds_conta", "en_tipo"}))
@NamedQueries({
    @NamedQuery(name = "ContaBancaria.findAll",
    query = "SELECT distinct a FROM ContaBancaria a "
    + " WHERE (a.usuario = :usuario OR a.usuario.conjuge = :usuario) "
    + " AND (:status2 = 'todos' OR a.status = :status) "),
    @NamedQuery(name = "ContaBancaria.findTipoConta",
    query = "SELECT distinct a FROM ContaBancaria a "
    + " WHERE a.tipoConta = :tipoConta "
    + " AND (:status2 = 'todos' OR a.status = :status) "
    + " AND (a.usuario = :usuario OR a.usuario.conjuge = :usuario) ")
})
public class ContaBancaria implements EntityInterface<ContaBancaria>,
        Serializable {

    /**
     * Limite máximo de caracteres para nome da conta.
     */
    public static final int CARACTERES_MAX_NOME_CONTA = 100;
    /**
     * Limite minimo de caracteres para nome da conta.
     */
    public static final int CARACTERES_MIN_NOME_CONTA = 3;
    /**
     * Identificador no BD.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long codigo;
    /**
     * Nome de identificação da conta bancária.<br> Não pode existir um
     * conta para o mesmo usuário, com mesmo tipo e mesmo nome, existe uma
     * restrição a nível de BD.
     */
    @Column(name = "ds_conta", nullable = false,
    length = CARACTERES_MAX_NOME_CONTA)
    @NotNull
    @Size(max = CARACTERES_MAX_NOME_CONTA,
    min = CARACTERES_MIN_NOME_CONTA)
    private String nomeConta;
    /**
     * Tipo da conta bancária.<br> Não pode existir um conta para o mesmo
     * usuário, com mesmo tipo e mesmo nome, existe uma restrição a nível
     * de BD.
     */
    @Column(name = "en_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoConta tipoConta;
    /**
     * Salda conta, valor modificado a medida que a conta sofre alterações.
     */
    @Column(name = "vl_saldo", nullable = false)
    @NotNull
    @Digits(fraction = 2, integer = 12)
    private BigDecimal saldo = BigDecimal.ZERO;
    /**
     * Status da conta, True = Conta em movimentação, False = Desconciderar
     * conta.
     */
    @Column(name = "fl_status", nullable = false)
    private boolean status = true;
    /**
     * Usuario responsavel pela conta bancária.<br> Não pode existir um
     * conta para o mesmo usuário, com mesmo tipo e mesmo nome, existe uma
     * restrição a nível de BD.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id",
    referencedColumnName = "user_id",
    nullable = false)
    @NotNull
    private Usuario usuario;

    @Override
    public Serializable getId() {
        return this.codigo;
    }

    @Override
    public String getLabel() {
        return StringUtils.rightPad(tipoConta.getAbreviacao(), 3)
                + " | " + StringUtils.substring(this.nomeConta, 0,
                CARACTERES_LABEL);
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(final ContaBancaria o) {
        int i = 0;
        i = tipoConta.getOrdem().compareTo(o.tipoConta.getOrdem());
        if (i == 0) {
            i = nomeConta.compareTo(o.nomeConta);
        }
        return i;
    }

    /**
     * Identificador no BD.
     *
     * @return Long
     */
    public Long getCodigo() {
        return codigo;
    }

    /**
     * Identificador no BD.
     *
     * @param idBanco ID
     */
    public void setCodigo(final Long idBanco) {
        this.codigo = idBanco;
    }

    /**
     * Nome de identificação da conta bancária.
     *
     * @return String
     */
    public String getNomeConta() {
        return nomeConta;
    }

    /**
     * Nome de identificação da conta bancária.
     *
     * @param nome String
     */
    public void setNomeConta(final String nome) {
        this.nomeConta = nome;
    }

    /**
     * Saldo atual da conta.
     *
     * @return Double.
     */
    public BigDecimal getSaldo() {
        return saldo;
    }

    /**
     * Saldo atual da conta.
     *
     * @param saldoConta Double.
     */
    public void setSaldo(final BigDecimal saldoConta) {
        this.saldo = saldoConta;
    }

    /**
     * Ativa = True<br> Cancelada = False.
     *
     * @return boolean
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Ativa = True<br> Cancelada = False.
     *
     * @param statusConta boolean
     */
    public void setStatus(final boolean statusConta) {
        this.status = statusConta;
    }

    /**
     * Conta de movimentação financeira. CORRENTE<br> Conta onde o dinheiro
     * deve ficar parado recebendo rendimentos. POUPANCA<br> Conta fixa,
     * onde o dinehrio não deve ou não pode ser resgatado a qualquer hora,
     * como um titulo de capitalização ou aponsetadoria privada.
     * INVESTIMENTO;<br>
     *
     * @return TipoConta
     */
    public TipoConta getTipoConta() {
        return tipoConta;
    }

    /**
     * Conta de movimentação financeira. CORRENTE("Conta Corrente", "CC",
     * 1), Conta onde o dinheiro deve ficar parado recebendo rendimentos.
     * POUPANCA("Poupança", "POU", 2), Conta fixa, onde o dinehrio não deve
     * ou não pode ser resgatado a qualquer hora, como um titulo de
     * capitalização ou aponsetadoria privada. INVESTIMENTO("Investimento",
     * "INV", 3);
     *
     * @param tipoConta
     */
    public void setTipoConta(final TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContaBancaria other = (ContaBancaria) obj;
        if (this.codigo != other.codigo
                && (this.codigo == null
                || !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.codigo != null
                ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ContaBancaria{" + "codigo=" + codigo + ", nomeConta="
                + nomeConta + ", tipoConta=" + tipoConta + ", saldo="
                + saldo + ", status=" + status + '}';
    }

    /**
     * Usuario responsavel pela conta bancária.
     *
     * @return Usuario.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Usuario responsavel pela conta bancária.
     *
     * @param user Usuario.
     */
    public void setUsuario(final Usuario user) {
        this.usuario = user;
    }
}
