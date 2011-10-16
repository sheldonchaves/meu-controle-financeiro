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
@Table(name="money_detalhe_movimentacao")
public class DetalheMovimentacao implements ValidadoInterface {
    private static final long serialVersionUID = 1L;
    public static final int QUANTIDADE_CARACTERES_DETALHE = 255;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false,unique=true)
    private Long id;

    @Column(name="ds_detalhe", length=QUANTIDADE_CARACTERES_DETALHE, unique=true, nullable=false)
    private String detalhe;

    @Column(name="fl_status", nullable=false)
    private boolean status = true;
    
    /**
     * Define se o detalhe é para todos os usuários<br>
     * Se true será utilizado por todos.<br>
     * Se false será somente para quem habilitar.
     */
    @Column(name="fl_geral", nullable=false)
    private boolean geral = false;
    
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

    public boolean isGeral() {
        return geral;
    }

    public void setGeral(boolean geral) {
        this.geral = geral;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
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
        return "DetalheMovimentacao{" + "id=" + id + ", detalhe=" + detalhe + ", status=" + status + ", geral=" + geral + '}';
    }
}
