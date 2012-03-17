/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.UsuarioBeanLocal;
import br.com.money.business.jms.jmsEmailUtilitarios.SimpleEmail;
import br.com.money.business.jms.jmsEmailUtilitarios.interfaces.EmailSendLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.Role;
import br.com.money.modelos.Usuario;
import br.com.money.utils.Criptografia;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
@Stateless
public class UsuarioBean extends AbstractFacade<Usuario, Long> 
                                implements UsuarioBeanLocal {

    public UsuarioBean() {
        super(Usuario.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.manager;
    }

    @Override
    protected ValidadorInterface getValidador() {
        return usuarioValidador;
    }
    
    @EJB(beanName="usuarioValidador")
    private ValidadorInterface usuarioValidador;

    @EJB
    private EmailSendLocal emailSendBean;
    
    @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;

    
    @Override
    public Role buscarRoleByName(String nomeRole) {
        Query q = manager.createNamedQuery("UsuarioBean.buscarRoleByName");
        q.setParameter("groupName", nomeRole);
        try {
            final Role toReturn = (Role) q.getSingleResult();
            return toReturn;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    public Usuario buscarUsuarioByLogin(String login) {
        Query q = manager.createNamedQuery("UsuarioBean.buscarUsuarioByLogin");
        q.setParameter("login", login);
        try {
            return (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Usuario buscarUsuarioByEmail(String email) {
        Query q = manager.createNamedQuery("UsuarioBean.buscarUsuarioByEmail");
        q.setParameter("email", email);
        try {
            return (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    
    @Override
    public void criarRole(Role role) {
        if(role.getId() == null){
            manager.persist(role);
        }else{
            manager.merge(role);
        }
        manager.flush();
    }

    @Override
    public void criarUsuario(final Usuario usuario) throws ValidacaoException {
        String newPass = null;
        if (StringUtils.isBlank(usuario.getPassword())) {
            newPass = this.geraSenha();
            usuario.setPassword(Criptografia.encodePassword(newPass,
                    usuario.stringAMIN()));
        }
        usuarioValidador.validar(usuario, this, null);
        if (usuario.getId() == null) {
            manager.persist(usuario);
        } else {
            manager.merge(usuario);
        }
        manager.flush();
        if (newPass != null) {
            avisaSenhaEmail(newPass, "http://sabercertificacao.com.br/money",
                    usuario);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "E-mail enviado ao usuário: {0} {1} para: {2}",
                    new Object[]{usuario.getFirstName(), usuario.getLastName(),
                        usuario.getEmail()});
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "Não foi enviado e-mail ao usuário: {0} {1} para: {2}",
                    new Object[]{usuario.getFirstName(),
                        usuario.getLastName(), usuario.getEmail()});
        }
    }

    @Override
    public String criptografarSenha(String senha, String role) {
        return Criptografia.encodePassword(senha, role);
    }


    /**
     * Gera uma senha randomica de 7 caracteres.<br>
     * Letras, números e alguns caracteres especiais.
     * @return String com 7 caracteres.
     */
    private String geraSenha() {
        Random r = new Random();
        String[] a = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String[] b = {"!", "#", "@", "&", "$"};
        String[] c = {"A", "a", "B", "b", "C", "c", "D", "d", "E",
            "e", "F", "f", "G", "g", "H", "h", "I", "i", "J", "j",
            "K", "k", "L", "l", "Z", "z"};
        String[] d = {"K", "k", "L", "l", "M", "m", "N", "n", "O",
            "o", "P", "p", "Q", "q", "R", "r", "S", "s", "T", "t",
            "U", "u", "V", "v", "X", "x", "Z", "z"};
        int aa = r.nextInt(a.length);
        int aaa = r.nextInt(a.length);
        int bb = r.nextInt(b.length);
        int cc = r.nextInt(c.length);
        int ccc = r.nextInt(c.length);
        int dd = r.nextInt(d.length);
        int ddd = r.nextInt(d.length);
        String senha = "";
        senha += c[cc];
        senha += d[dd];
        senha += b[bb];
        senha += a[aaa];
        senha += c[ccc];
        senha += d[ddd];
        senha += a[aa];
        return senha;
    }

    /**
     * Envia a senha para o usuário via e-mail.
     */
    private void avisaSenhaEmail(final String newPass, 
            final String url, final Usuario usuario) {
        String body = this.bodyEmail(newPass, url, usuario);
        SimpleEmail simple = new SimpleEmail();
        simple.setBody(body);
        simple.setEmail(usuario.getEmail());
        simple.setSubject("Money :: Controle de Acesso");
        this.emailSendBean.enviarEmailJMS(simple);
    }

    /**
     * Cria o corpo do e-mail que será enviado.
     * @param pass Senha sem estar criptografada.
     * @param url URL de acesso ao site atual.
     * @param user Usuário que irá receber o e-mail.
     * @return String para ser anexada como corpo do e-mail.
     */
    private String bodyEmail(final String pass, final String url,
            final Usuario user) {
        String toReturn = "";
        String quebra = "<br>";
        toReturn += "<h3>Bem vindo ao Money!</h3>";
        toReturn += "Seu login é: ";
        toReturn += "<strong>" + user.getLogin() + "</strong>";
        toReturn += quebra;
        toReturn += "Sua senha é: ";
        toReturn += "<strong>" + pass + "</strong>";
        toReturn += quebra;
        toReturn += "Você pode alterar sua senha após o 1º login!";
        toReturn += quebra;
        toReturn += "Dúvidas: gbvbahia01@gmail.com";
        toReturn += quebra;
        toReturn += url;
        return toReturn;
    }
}
