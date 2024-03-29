/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.money.modelos;

import br.com.money.modelos.commons.EntityInterface;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name="money_role")
public class Role implements EntityInterface<Role> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true)
    private Integer id;

    @NotNull
    @Size(max= 15)
    @Column(name="ds_name", length=15, unique=true,nullable=false)
    private String groupName;

    @NotNull
    @Size(max= 255)
    @Column(name="ds_desc", length=255,nullable=false)
    private String groupDesc;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.financeiro.entidades.Grups[id=" + id + "]";
    }

    @Override
    public String getLabel() {
        return this.groupName + " "
                + this.groupDesc;
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(Role o) {
        return this.groupName.compareTo(o.getGroupName());
    }

}
