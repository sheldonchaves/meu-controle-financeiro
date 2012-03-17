/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos;

import br.com.money.modelos.commons.EntityInterface;
import br.com.money.vaidators.interfaces.ValidadoInterface;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name = "money_lembrete_contas")
public class Scheduler implements ValidadoInterface, EntityInterface<Scheduler> {
    
    public static final int QUANTIDADE_CARACTERES_EMAIL = 255;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "nm_dias_vencimento", nullable = false)
    @NotNull
    private int dias = 0;

    @Column(name = "ds_email_contato", nullable = false,
            length=QUANTIDADE_CARACTERES_EMAIL)
    @NotNull
    @Size(max=QUANTIDADE_CARACTERES_EMAIL)
    private String email;

    @Column(name = "fl_status_aviso", nullable = false)
    private boolean status = true;

    @OneToOne
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

    @Override
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
        if (!(object instanceof Scheduler)) {
            return false;
        }
        Scheduler other = (Scheduler) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LembreteConta{" + "id=" + id + ", dias=" + dias + ", email="
                + email + ", status=" + status + '}';
    }

    @Override
    public String getLabel() {
        return dias + " "
                + email + " "
                + status;
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(Scheduler o) {
        return this.email.compareTo(o.email);
    }
    
    
}
