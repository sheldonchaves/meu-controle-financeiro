/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades;

import br.com.financeiro.excecoes.LembreteContasException;
import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.validator.EmailValidator;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name = "lembrete_contas")
public class LembreteConta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "dias_vencimento", nullable = false)
    private int dias = 0;

    @Column(name = "email_contato", nullable = false,length=255)
    private String email;

    @Column(name = "status_aviso", nullable = false)
    private boolean status = true;

    @OneToOne(targetEntity = br.com.financeiro.entidades.User.class)
    @JoinColumn(name="user_id", referencedColumnName="user_id", nullable=false)
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
        return "br.com.financeiro.entidades.LembreteContas[id=" + id + "]";
    }

    @PrePersist
    @PreUpdate
    private void vailidaLembreteContas() {
        if (this.email == null) {
            throw new LembreteContasException("Um e-mail deve ser informado!");
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new LembreteContasException("Email inválido!");
        }
        if (this.dias > 5) {
            throw new LembreteContasException("Quantidade de dias: " + dias + " inválido, limite de 5!");
        }
        if(this.user == null){
            throw new LembreteContasException("Um usuário deve ser informado!");
        }
    }
}
