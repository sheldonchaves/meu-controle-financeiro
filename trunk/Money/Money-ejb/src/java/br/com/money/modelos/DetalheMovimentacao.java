/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos;

import br.com.money.vaidators.interfaces.ValidadoInterface;
import javax.persistence.*;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name = "money_detalhe_movimentacao",
uniqueConstraints =
@UniqueConstraint(name = "uk_detalhe_usuario", columnNames = {"ds_detalhe", "fk_usuario"}))
public class DetalheMovimentacao implements ValidadoInterface, Comparable<DetalheMovimentacao> {

    private static final long serialVersionUID = 1L;

    public static final int QUANTIDADE_CARACTERES_DETALHE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "ds_detalhe", length = QUANTIDADE_CARACTERES_DETALHE, unique = false, nullable = false)
    private String detalhe;

    @ManyToOne
    @JoinColumn(name="fk_usuario", referencedColumnName="id")
    private Usuario usuarioProprietario;
    
    /**
     * Define se o detalhe é para todos os usuários<br>
     * Se true será utilizado por todos.<br>
     * Se false será somente para quem habilitar.
     */
    @Column(name = "fl_ativo", nullable = false)
    private boolean ativo = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Usuario getUsuarioProprietario() {
        return usuarioProprietario;
    }

    public void setUsuarioProprietario(Usuario usuarioProprietario) {
        this.usuarioProprietario = usuarioProprietario;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetalheMovimentacao other = (DetalheMovimentacao) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.detalhe == null) ? (other.detalhe != null) : !this.detalhe.equals(other.detalhe)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetalheMovimentacao{" + "id=" + id + ", detalhe=" + detalhe + ", ativo=" + ativo + '}';
    }

    @Override
    public int compareTo(DetalheMovimentacao o) {
        int i = 0;
        if (i == 0) {
            i = this.detalhe.compareTo(o.detalhe);
        }
        if (i == 0) {
            i = this.id.compareTo(o.id);
        }
        return i;
    }
}
