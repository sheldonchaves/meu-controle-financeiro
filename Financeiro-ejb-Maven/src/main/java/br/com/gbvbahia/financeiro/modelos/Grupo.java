/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Grupo, ou papel, de segurança para as aplicações.
 *
 * @author Guilherme
 * @since 2012/02/25
 */
@Entity
@Table(name = "fin_grupo")
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll",
    query = " SELECT a FROM Grupo a "),
    @NamedQuery(name = "Grupo.findByGrupoId",
    query = " SELECT a FROM Grupo a "
    + " WHERE a.nome = :nome ")
})
@XmlRootElement
public class Grupo implements EntityInterface<Grupo>, Serializable {

    /**
     * Nome do grupo, o método de Único por grupo.<br> Não utiliza
     * sequence.<br> Não pode ser nulo.
     */
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "grupo_id", nullable = false, length = 50)
    private String nome;
    /**
     * String com pequena descrição sobre o grupo.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "description", nullable = false, length = 50)
    private String descricao;
    /**
     * Quando um usuário é cadastrado, os grupos que forem marcados como true
     * será incluídos automaticamente no usuário a ser cadastrado.<br>
     * Logicamente que no momento do cadastro esses grupos poderão ser
     * alterados conforme necessidade.<br> <strong>Não existe implementação
     * para verificar essa informação no momento.</strong>
     */
    @Basic(optional = false)
    @Column(name = "user_dafeult", nullable = false)
    private boolean usuarioPadrao = false;
    /**
     * 
     */
    @Transient
    private boolean marcadoTransient;

    /**
     * Construtor padrão.
     */
    public Grupo() {
    }

    /**
     * Construtor que recebe a String que será o Id do grupo.
     *
     * @param grupoId java.lang.String
     */
    public Grupo(String grupoId) {
        this.nome = grupoId;
    }

    /**
     * GrupoId do grupo.
     *
     * @return java.lang.String
     */
    public String getNome() {
        return nome;
    }

    /**
     * Altera o grupoId, se já estiver salvo no banco de dados esse valor não
     * pode ser alterado.<br> Campo obrigatório.
     *
     * @param grupoId java.lang.String
     */
    public void setNome(String grupoId) {
        this.nome = grupoId;
    }

    /**
     * Recupera a descrição do gruo
     *
     * @return java.lang.String
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Altera a descrição do grupo.<br> Campo obrigatório.
     *
     * @param description
     */
    public void setdDescricao(String description) {
        this.descricao = description;
    }

    /**
     * Verifica se é um grupo default.
     *
     * @return boolean.
     */
    public boolean getUsuarioPadrao() {
        return usuarioPadrao;
    }

    /**
     * Altera se o grupo é default.
     *
     * @param userDafeult boolean.
     */
    public void setUsuarioPadrao(boolean userDafeult) {
        this.usuarioPadrao = userDafeult;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nome != null ? nome.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.nome == null
                && other.nome != null)
                || (this.nome != null
                && !this.nome.equals(other.nome))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public Serializable getId() {
        return this.getNome();
    }

    @Override
    public String getLabel() {
        return this.getNome();
    }

    @Override
    public int compareTo(final Grupo o) {
        return this.getLabel().compareTo(o.getLabel());
    }

    @Override
    public boolean verificarId() {
        return true;
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
