/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos;

import br.com.money.vaidators.interfaces.ValidadoInterface;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name = "money_usuario")
public class Usuario implements ValidadoInterface {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "ds_login", nullable = false, length = 10, unique = true)
    private String login;

    @Column(name = "ds_first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "ds_last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "ds_password", nullable = false, length = 50)
    private String password;

    @Column(name = "ds_email", nullable = false, length = 100, unique=true)
    private String email;

    @OneToOne(targetEntity = br.com.money.modelos.Usuario.class, optional = true)
    @JoinColumn(name = "fk_conjuge", referencedColumnName = "id", nullable = true)
    private Usuario conjuge;

    @OneToMany(mappedBy = "user", targetEntity = br.com.money.modelos.ContaBancaria.class)
    private Set<ContaBancaria> contaBancarias;

    @ManyToMany(targetEntity = br.com.money.modelos.Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "money_users_groups",
    joinColumns =
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id"),
    inverseJoinColumns =
    @JoinColumn(name = "fk_role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public Usuario getConjuge() {
        return conjuge;
    }

    public void setConjuge(Usuario conjuge) {
        this.conjuge = conjuge;
    }

    public Set<ContaBancaria> getContaBancarias() {
        if(contaBancarias == null) contaBancarias = new HashSet<ContaBancaria>();
        return contaBancarias;
    }

    public void setContaBancarias(Set<ContaBancaria> contasBancarias) {
        this.contaBancarias = contasBancarias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<Role>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.money.modelos.Usuario[ id=" + id + " ]";
    }
}
