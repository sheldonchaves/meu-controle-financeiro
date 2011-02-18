/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades.detalhes;

import br.com.financeiro.entidades.User;
import java.io.Serializable;
import javax.persistence.*;

/**
 * Detalhes que são informados nas contas
 * @author Guilherme
 */
@Entity
@Table(name="grupo_receita")
public class GrupoReceita implements Serializable, Comparable<GrupoReceita> {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",nullable=false, unique=true)
    private Integer id;

    @Column(name="grupo_receita",length=255, unique=true, nullable=false)
    private String grupoReceita;

    @Column(name="status", nullable=false)
    private boolean status = true;

    @ManyToOne(optional=true,targetEntity=br.com.financeiro.entidades.User.class, fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="user_id")
    private User user;

    public String getGrupoReceita() {
        return grupoReceita;
    }

    public void setGrupoReceita(String grupoReceita) {
        this.grupoReceita = grupoReceita;
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
        final GrupoReceita other = (GrupoReceita) obj;
        if ((this.grupoReceita == null) ? (other.grupoReceita != null) : !this.grupoReceita.equals(other.grupoReceita)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.grupoReceita != null ? this.grupoReceita.hashCode() : 0);
        return hash;
    }

    public int compareTo(GrupoReceita o) {
        return this.grupoReceita.compareTo(o.getGrupoReceita());
    }

    
}
