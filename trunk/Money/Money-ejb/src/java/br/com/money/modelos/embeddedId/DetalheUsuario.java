/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos.embeddedId;

import br.com.money.modelos.DetalheMovimentacao;
import br.com.money.modelos.Usuario;
import br.com.money.modelos.embeddedId.classid.IdDetalheUsuarioPk;
import br.com.money.vaidators.interfaces.ValidadoInterface;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name = "money_detalhe_usuario",
uniqueConstraints =
@UniqueConstraint(name = "uk_detalhe_usuario", columnNames = {"fk_detalhe_movimentacao_id", "fk_usuario_id"}))
public class DetalheUsuario implements ValidadoInterface {

    @EmbeddedId
    private IdDetalheUsuarioPk id = new IdDetalheUsuarioPk();

    @ManyToOne
    @JoinColumn(name = "fk_usuario_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "fk_detalhe_movimentacao_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DetalheMovimentacao detalheMovimentacao;

    public DetalheUsuario() {
    }

    public DetalheUsuario(DetalheMovimentacao detalheMovimentacao, Usuario usuario) {
        this.usuario = usuario;
        this.detalheMovimentacao = detalheMovimentacao;
        this.id = new IdDetalheUsuarioPk(detalheMovimentacao.getId(), usuario.getId());
    }

    public DetalheMovimentacao getDetalheMovimentacao() {
        return detalheMovimentacao;
    }

    public void setDetalheMovimentacao(DetalheMovimentacao detalheMovimentacao) {
        this.detalheMovimentacao = detalheMovimentacao;
    }

    public IdDetalheUsuarioPk getId() {
        return id;
    }

    public void setId(IdDetalheUsuarioPk id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetalheUsuario other = (DetalheUsuario) obj;
        if (this.usuario != other.usuario && (this.usuario == null || !this.usuario.equals(other.usuario))) {
            return false;
        }
        if (this.detalheMovimentacao != other.detalheMovimentacao && (this.detalheMovimentacao == null || !this.detalheMovimentacao.equals(other.detalheMovimentacao))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.usuario != null ? this.usuario.hashCode() : 0);
        hash = 17 * hash + (this.detalheMovimentacao != null ? this.detalheMovimentacao.hashCode() : 0);
        return hash;
    }
}
