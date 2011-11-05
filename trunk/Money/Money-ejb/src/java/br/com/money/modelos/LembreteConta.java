/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name = "money_lembrete_contas")
public class LembreteConta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "nm_dias_vencimento", nullable = false)
    private int dias = 0;

    @Column(name = "ds_email_contato", nullable = false,length=255)
    private String email;

    @Column(name = "fl_status_aviso", nullable = false)
    private boolean status = true;

    @OneToOne(targetEntity = br.com.money.modelos.Usuario.class)
    @JoinColumn(name="fk_user_id", referencedColumnName="id", nullable=false)
    private Usuario user;

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
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
        if (!(object instanceof LembreteConta)) {
            return false;
        }
        LembreteConta other = (LembreteConta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LembreteConta{" + "id=" + id + ", dias=" + dias + ", email=" + email + ", status=" + status + '}';
    }
}
