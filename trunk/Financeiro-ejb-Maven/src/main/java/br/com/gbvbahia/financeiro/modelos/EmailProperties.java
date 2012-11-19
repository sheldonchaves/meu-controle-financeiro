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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Guilherme
 */
@Entity
@Table(name = "fin_email")
public class EmailProperties implements EntityInterface<EmailProperties>,
        Serializable {

    /**
     * ID único no banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * Gmail: gmail-smtp.l.google.com
     */
    @NotNull
    @Column(name = "host_name", nullable = false, length = 100)
    private String hostName = "gmail-smtp.l.google.com";
    /**
     * Enconding da msg
     */
    @NotNull
    @Column(name = "encoding", nullable = false, length = 100)
    private String characterCoding = "ISO-8859-1";
    /**
     * Porta SMTP gmail = 465
     */
    @NotNull
    @Column(name = "smtp_port", nullable = false)
    private Integer smtpPort = 465;
    /**
     * Login email:
     */
    @NotNull
    @Column(name = "login", nullable = false, length = 255)
    private String loginEmail;
    @NotNull
    @Column(name = "pass", nullable = false, length = 255)
    private String senhaEmail;
    /**
     * Gmail = true;
     */
    @NotNull
    @Column(name = "tls", nullable = false, length = 255)
    private boolean tls = true;
    /**
     * Email de quem envia.
     */
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`"
    + "{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:"
    + "[a-z0-9-]*[a-z0-9])?",
    message = "{email.invalido}")
    @NotNull
    @Column(name = "from_email", nullable = false, length = 255)
    private String fromEmail;
    /**
     * Assunto default utilizado.
     */
    @NotNull
    @Column(name = "from_assunto_default", nullable = false, length = 255)
    private String assuntoDefault = "Money";
    /**
     * Utilizar conexão segura. gmail = true.
     */
    @NotNull
    @Column(name = "ssl_status", nullable = false, length = 255)
    private boolean ssl = true;
    /**
     * Porta SSL, Gmail = 465
     */
    @NotNull
    @Column(name = "ssl_port", nullable = false, length = 255)
    private String sslSmtpPort = "465";
    /**
     * Determina se e para utilizar esta conta de e-mail para envio de
     * informacao. Havendo mais de uma conta ativa, a primeira será
     * utilizada.
     */
    @NotNull
    @Column(name = "status", nullable = false, length = 255)
    private boolean contaAtiva = true;

    public EmailProperties() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getCharacterCoding() {
        return characterCoding;
    }

    public void setCharacterCoding(String characterCoding) {
        this.characterCoding = characterCoding;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getSenhaEmail() {
        return senhaEmail;
    }

    public void setSenhaEmail(String senhaEmail) {
        this.senhaEmail = senhaEmail;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getAssuntoDefault() {
        return assuntoDefault;
    }

    public void setAssuntoDefault(String assuntoDefault) {
        this.assuntoDefault = assuntoDefault;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getSslSmtpPort() {
        return sslSmtpPort;
    }

    public void setSslSmtpPort(String sslSmtpPort) {
        this.sslSmtpPort = sslSmtpPort;
    }

    public boolean isContaAtiva() {
        return contaAtiva;
    }

    public void setContaAtiva(boolean contaAtiva) {
        this.contaAtiva = contaAtiva;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final EmailProperties other = (EmailProperties) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String getLabel() {
        return this.fromEmail;
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(EmailProperties o) {
        return this.fromEmail.compareToIgnoreCase(o.fromEmail);
    }
}