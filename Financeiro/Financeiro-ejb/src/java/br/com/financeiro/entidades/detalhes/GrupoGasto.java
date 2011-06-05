/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades.detalhes;

import br.com.financeiro.entidades.User;
import java.io.Serializable;
import javax.persistence.*;
/**
 *  Detalhes que são informados nas contas
 * @author Guilherme
 */
@Entity
@Table(name="grupo_gasto")
public class GrupoGasto implements Serializable, Comparable<GrupoGasto> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",nullable=false, unique=true)
    private Integer id;

    @Column(name="grupo_gasto", length=255, unique=false, nullable=false)
    private String grupoGasto;

    @Column(name="status", nullable=false)
    private boolean status = true;

    @ManyToOne(optional=true,targetEntity=br.com.financeiro.entidades.User.class, fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="user_id")
    private User user;

    public String getGrupoGasto() {
        return grupoGasto;
    }

    public void setGrupoGasto(String grupoGasto) {
        this.grupoGasto = grupoGasto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrupoGasto other = (GrupoGasto) obj;
        if ((this.grupoGasto == null) ? (other.grupoGasto != null) : !this.grupoGasto.equals(other.grupoGasto)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (this.grupoGasto != null ? this.grupoGasto.hashCode() : 0);
        return hash;
    }

    public int compareTo(GrupoGasto o) {
        return this.grupoGasto.compareTo(o.getGrupoGasto());
    }

    @Override
    public String toString() {
        return grupoGasto + (this.status ? "" : "(D)");
    }

    
    
}
