/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author Usuário do Windows
 */
@Entity
@Table(name = "fin_scheduler")
@NamedQueries({
    @NamedQuery(name = "SchedulerBean.buscarSchedulerPorUsuario",
    query = " Select s From Scheduler s "
    + " Where s.user = :user "),
    @NamedQuery(name = "SchedulerBean.buscarTodosSchelersPorStatus",
    query = " Select s From Scheduler s "
    + " Where s.status = :status ")
})
public class Scheduler implements EntityInterface<Scheduler>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    /**
     *
     */
    @Column(name = "nm_dias_vencimento", nullable = false)
    @NotNull
    @Min(1)
    @Max(5)
    private int dias = 0;
    /**
     *
     */
    @Column(name = "ds_email_contato", nullable = false, length = 255)
    @NotNull(message="{Scheduler.email.null}")
    @Size(max = 255)
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`"
    + "{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:"
    + "[a-z0-9-]*[a-z0-9])?",
    message = "{email.invalido}")
    private String email;
    /**
     *
     */
    @Column(name = "fl_status_aviso", nullable = false)
    private boolean status = true;
    /**
     *
     */
    @NotNull(message="{Scheduler.user.null}")
    @OneToOne
    @JoinColumn(name = "fk_usuario", referencedColumnName = "user_id", nullable = false)
    private Usuario user;
    /**
     * 
     */
    @Transient
    private boolean marcadoTransient;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final Scheduler other = (Scheduler) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String getLabel() {
        return dias + " | " + email;
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(Scheduler o) {
        return email.compareTo(o.email);
    }
    @Override
    public boolean isMarcadoTransient() {
        return marcadoTransient;
    }
    @Override
    public void setMarcadoTransient(boolean marcadoTransient) {
        this.marcadoTransient = marcadoTransient;
    }
}
