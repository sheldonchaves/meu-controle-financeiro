/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidade que representa um usuário logado, utilizada pelo
 * Container, GlassFish para validação de login e segurança de acesso
 * aos beans e páginas da aplicação.<br> Acesse:
 * http://www.hildeberto.com/2010/05/yasmim-security-part-1-user.html
 * Para ver um tutorial de como configurar a segurança no GlassFish.
 *
 * @author Guilherme
 * @since 2012/02/25
 */
@Entity
@Table(name = "fin_usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll",
    query = " SELECT u FROM Usuario u "),
    @NamedQuery(name = "Usuario.findByUserId",
    query = " SELECT u FROM Usuario u "
    + " WHERE u.userId = :userId "),
    @NamedQuery(name = "Usuario.findByBlocked",
    query = " SELECT u FROM Usuario u "
    + " WHERE u.blocked = :blocked "),
    @NamedQuery(name = "Usuario.findByEmail",
    query = " SELECT u FROM Usuario u "
    + " WHERE u.email = :email "),
    @NamedQuery(name = "Usuario.findByFirstName",
    query = " SELECT u FROM Usuario u "
    + " WHERE u.firstName = :firstName ")
})
@XmlRootElement
public class Usuario implements EntityInterface<Usuario>, Serializable {

    /**
     * Quantidade máxima de caracteres no Login.
     */
    public static final int LIMIT_MAX_CARACTERES_LOGIN_ID = 50;
    /**
     * Quantidade minima de caracteres no login.
     */
    public static final int LIMIT_MIN_CARACTERES_LOGIN_ID = 4;
    /**
     * Quantidade máxima de caracteres nos Nomes, 1º e 2º.
     */
    public static final int LIMIT_MAX_CARACTERES_NOMES = 50;
    /**
     * Quantidade máxima de caracteres nos Nomes, 1º e 2º.
     */
    public static final int LIMIT_MIN_CARACTERES_NOMES = 3;
    /**
     * Único por usuário.<br> Não utiliza sequence.<br> Não pode ser
     * nulo.
     */
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = LIMIT_MIN_CARACTERES_LOGIN_ID,
    max = LIMIT_MAX_CARACTERES_LOGIN_ID)
    @Column(name = "user_id", nullable = false,
    length = LIMIT_MAX_CARACTERES_LOGIN_ID)
    private String userId;
    /**
     * Senha criptografada utilizando: <br>Algoritmo: MD5 <br>
     * Encoding: Base64<br> Não pode ser nulo.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "pass", nullable = false, length = 50)
    private String pass;
    /**
     * Define se usuário está liberado para login.<br> <strong>Não
     * existe implementação para verificar essa informação no
     * momento.</strong>
     */
    @Basic(optional = false)
    @NotNull
    @Column(name = "blocked", nullable = false)
    private boolean blocked = false;
    /**
     * Email do usuário, recebe validação regex:<br>
     * [a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`
     * {|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.+[a-z0-9](?:
     * [a-z0-9-]*[a-z0-9])?<br> Não pode ser nulo.
     */
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`"
    + "{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:"
    + "[a-z0-9-]*[a-z0-9])?",
    message = "{email.invalido}")
    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;
    /**
     * Nome do usuário. Não pode ser nulo.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = LIMIT_MIN_CARACTERES_NOMES,
    max = LIMIT_MAX_CARACTERES_NOMES)
    @Column(name = "first_name", nullable = false,
    length = LIMIT_MAX_CARACTERES_NOMES)
    private String firstName;
    /**
     * Sobre nome do usuário. Não pode ser nulo.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = LIMIT_MIN_CARACTERES_NOMES,
    max = LIMIT_MAX_CARACTERES_NOMES)
    @Column(name = "last_name", nullable = false,
    length = LIMIT_MAX_CARACTERES_NOMES)
    private String lastName;
    /**
     * Não possui implementação atualmente, pode ser utilizado para
     * criar código de recuperação de senha.
     */
    @Size(max = 32)
    @Column(name = "confirmation_code", length = 32)
    private String confirmationCode;
    /**
     * Grupos aos quais o usuário está vinculado.<br>
     * FetchType.EAGER<br> Tabela Intermediária: user_group
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "fin_usuario_grupo",
    joinColumns = {
        @JoinColumn(name = "user_id",
        referencedColumnName = "user_id")},
    inverseJoinColumns =
    @JoinColumn(name = "grupo_id",
    referencedColumnName = "grupo_id"))
    private Set<Grupo> grupos;
    /**
     * Representa o marido/esposa que pode ver/alterar/cadastrar novas
     * informações, cada um possui um usuário, mas pode ver tudo do
     * outro usuário e realizar movimentações nas contas do outro
     * usuário.
     */
    @OneToOne(optional = true)
    @JoinColumn(name = "fk_conjuge", referencedColumnName = "user_id",
    nullable = true)
    private Usuario conjuge;

    /**
     * Construtor Padrão.
     */
    public Usuario() {
    }

    /**
     * Define o login do usuário, não pode ser alterado após salvo no
     * banco de dados.
     *
     * @param login java.lang.String.
     */
    public Usuario(final String login) {
        this.userId = login;
    }

    /**
     * Construtor com principais valores.
     *
     * @param login Id usuário, nunca poderá ser alterado após salvo
     * no BD.
     * @param password Senha já criptrografada do usuário.
     * @param block Se usuário está bloqueado.
     * @param firstN Primeiro nome do usuário.
     * @param lastN Ultimo nome do usuário.
     */
    public Usuario(final String login, final String password,
            final boolean block, final String firstN,
            final String lastN) {
        this.userId = login;
        this.pass = password;
        this.blocked = block;
        this.firstName = firstN;
        this.lastName = lastN;
    }

    /**
     * Login do usuário.
     *
     * @return java.lang.String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Altera o login, se já estiver salvo no banco de dados esse
     * valor não pode ser alterado.
     *
     * @param login java.lang.String
     */
    public void setUserId(String login) {
        this.userId = login;
    }

    /**
     * Senha criptografada do usuário. <br>Algoritmo: MD5 <br>
     * Encoding: Base64<br> Não pode ser nulo.
     *
     * @return java.lang.String
     */
    public String getPass() {
        return pass;
    }

    /**
     * Altera a senha do usuário. <br>Algoritmo: MD5 <br> Encoding:
     * Base64<br> Não pode ser nulo.
     *
     * @param password já criptrografado do usuário.
     */
    public void setPass(final String password) {
        this.pass = password;
    }

    /**
     * Status usuário. <strong>Não implementado.</strong>
     *
     * @return boolean
     */
    public boolean getBlocked() {
        return blocked;
    }

    /**
     * Status usuário. <strong>Não implementado.</strong>
     *
     * @param block boolean
     */
    public void setBlocked(final boolean block) {
        this.blocked = block;
    }

    /**
     * Email usuário.
     *
     * @return java.lang.String
     */
    public String getEmail() {
        return email;
    }

    /**
     * É validado por regex. Não pode ser nulo.
     *
     * @param mail java.lang.String
     */
    public void setEmail(final String mail) {
        this.email = mail;
    }

    /**
     * Nome usuário.<br> Não pode ser nulo.
     *
     * @return java.lang.String.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Altrera nome do usuário.<br> Nome pode ser null.
     *
     * @param firstN java.lang.String
     */
    public void setFirstName(final String firstN) {
        this.firstName = firstN;
    }

    /**
     * Segundo nome usuário.
     *
     * @return java.lang.String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Segundo nome usuário. Não pode ser null.
     *
     * @param lastN java.lang.String
     */
    public void setLastName(final String lastN) {
        this.lastName = lastN;
    }

    /**
     * <strong>Não implementado.</strong>
     *
     * @return null
     */
    public String getConfirmationCode() {
        return confirmationCode;
    }

    /**
     * <strong>Não implementado.</strong>
     *
     * @param confCode java.lang.String
     */
    public void setConfirmationCode(final String confCode) {
        this.confirmationCode = confCode;
    }

    /**
     * Grupos que o usuário participa.<br> FetchType.EAGER
     *
     * @return java.util.Set&ltGrupo&gt
     */
    public Set<Grupo> getGrupos() {
        if (grupos == null) {
            grupos = new HashSet<Grupo>();
        }
        return grupos;
    }

    /**
     * Altera os grupos do usuário.
     *
     * @param grups java.util.Set&ltGrupo&gt
     */
    public void setGrupos(final Set<Grupo> grups) {
        this.grupos = grups;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.userId == null && other.userId != null)
                || (this.userId != null
                && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return userId;
    }

    @Override
    public Serializable getId() {
        return this.getUserId();
    }

    @Override
    public String getLabel() {
        return this.getUserId()
                + " : "
                + this.getFirstName()
                + " "
                + this.getLastName();
    }

    @Override
    public int compareTo(final Usuario o) {
        return this.getUserId().compareTo(o.getUserId());
    }

    @Override
    public boolean verificarId() {
        return true;
    }

    /**
     * Representa o marido/esposa que pode ver/alterar/cadastrar novas
     * informações, cada um possui um usuário, mas pode ver tudo do
     * outro usuário e realizar movimentações nas contas do outro
     * usuário.
     *
     * @return Usuario conjuge.
     */
    public Usuario getConjuge() {
        return conjuge;
    }

    /**
     * Representa o marido/esposa que pode ver/alterar/cadastrar novas
     * informações, cada um possui um usuário, mas pode ver tudo do
     * outro usuário e realizar movimentações nas contas do outro
     * usuário.
     *
     * @param usuarioConjuge Usuario
     */
    public void setConjuge(final Usuario usuarioConjuge) {
        this.conjuge = usuarioConjuge;
    }
}
