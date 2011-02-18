/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 *  Entidade anotada por XML
 * @author Guilherme
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "user_name", nullable = false, length = 10, unique = true)
    private String login;

    @Column(name = "first_name", nullable = false, length = 15)
    private String first_name;

    @Column(name = "last_name", nullable = false, length = 20)
    private String last_name;

    @Column(name = "password", nullable = false, length = 35)
    private String password;

    @Column(name = "user_email", nullable = false, length = 100)
    private String email;

    @OneToOne(targetEntity=br.com.financeiro.entidades.User.class,optional=true)
    @JoinColumn(name="conjuge",referencedColumnName="user_id",nullable=true)
    private User conjugeUser;

    @OneToMany(mappedBy = "user", targetEntity = br.com.financeiro.entidades.ContaBancaria.class)
    private Set<ContaBancaria> contasBancarias;

    @ManyToMany(targetEntity = br.com.financeiro.entidades.Grups.class, fetch = FetchType.EAGER)
    @JoinTable(name = "users_groups",
    joinColumns =
    @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
    inverseJoinColumns =
    @JoinColumn(name = "group_id", referencedColumnName = "group_id"))
    private Set<Grups> grups;

    public String stringAMIN() {
        for (Grups grup : grups) {
            if (grup.getGroupName().equals("ADMIN")) {
                return "ADMIN";
            }
        }
        return "NAO";
    }

    public Set<ContaBancaria> getContasBancarias() {
        if (this.contasBancarias == null) {
            this.contasBancarias = new HashSet<ContaBancaria>();
        }
        return contasBancarias;
    }

    public void setContasBancarias(Set<ContaBancaria> contasBancarias) {
        this.contasBancarias = contasBancarias;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public Set<Grups> getGrups() {
        if (this.grups == null) {
            this.grups = new HashSet<Grups>();
        }
        return grups;
    }

    public void setGrups(Set<Grups> grups) {
        this.grups = grups;
    }

    @Override
    public String toString() {
        return this.first_name + " " + this.last_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getConjugeUser() {
        return conjugeUser;
    }

    public void setConjugeUser(User conjugeUser) {
        this.conjugeUser = conjugeUser;
    }


}
