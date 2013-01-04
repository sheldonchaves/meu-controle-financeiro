/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business;

import br.com.gbvbahia.financeiro.beans.business.interfaces.RegistroBusiness;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.GrupoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.beans.jms.interfaces.EmailSendBusiness;
import br.com.gbvbahia.financeiro.beans.jms.interfaces.EmailSendInterface;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.Base64Encoder;
import java.util.Random;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Guilherme
 */
@Stateless
@RunAs("sys")
public class RegistroBean implements RegistroBusiness {

    @EJB
    private EmailSendBusiness emailSendBean;
    @EJB
    private GrupoFacade grupoBean;
    @EJB
    private UsuarioFacade usuarioBean;

    @Override
    public void registroUsuario(String login, String email) throws NegocioException {
        Usuario usuario = new Usuario();
        usuario.setUserId(login);
        usuario.setEmail(email);
        usuario.setFirstName(login);
        usuario.setLastName(login);
        usuario.setBlocked(false);
        usuario.getGrupos().add(grupoBean.find("users"));
        final String senha = geraSenha();
        usuario.setPass(Base64Encoder.encryptPassword(senha));
        usuarioBean.create(usuario);
        PassEmail emailSend = new PassEmail(email, login, senha, false);
        emailSendBean.enviarEmailJMSAsynchronous(emailSend);
    }

    @Override
    public void recuperarSenha(String email) throws NegocioException {
        Usuario usr = usuarioBean.buscarPorEmail(email);
        if (usr == null) {
            throw new NegocioException("userEmailNotFound", new String[]{email});
        }
        final String senha = geraSenha();
        usr.setPass(Base64Encoder.encryptPassword(senha));
        usuarioBean.update(usr);
        PassEmail emailSend = new PassEmail(email, usr.getUserId(), senha, true);
        emailSendBean.enviarEmailJMSAsynchronous(emailSend);
    }

    /**
     * Gera uma senha de forma aleatoria.
     *
     * @return
     */
    private String geraSenha() {
        Random r = new Random();
        String[] a = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] b = {"2", "4", "6", "8", "0"};
        String[] c = {"1", "3", "5", "7", "9"};
        String[] d = {"9", "8", "7", "6", "5", "4", "3", "2", "1", "0"};
        int A = r.nextInt(a.length);
        int A2 = r.nextInt(a.length);
        int B = r.nextInt(b.length);
        int C = r.nextInt(c.length);
        int C2 = r.nextInt(c.length);
        int D = r.nextInt(d.length);
        int D2 = r.nextInt(d.length);
        String senha = "";
        senha += c[C];
        senha += d[D];
        senha += a[A2];
        senha += c[C2];
        senha += b[B];
        senha += d[D2];
        senha += a[A];
        return senha;
    }

    private class PassEmail implements EmailSendInterface {

        private String email;
        private String login;
        private String senha;
        private boolean recover = false;

        public PassEmail(String email, String login, String senha, boolean recover) {
            this.email = email;
            this.login = login;
            this.senha = senha;
            this.recover = recover;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getSubject() {
            return "Money :: Access";
        }

        @Override
        public boolean addUrlBody() {
            return true;
        }

        @Override
        public String getBody() {
            if (!recover) {
                return "Bem vindo ao controle financeiro pessoal, seus dados de acesso são:" + "<br></br>"
                        + "Welcome to my personal financial control, your data access:" + "<br></br><br></br>"
                        + "Login:    " + login + "<br></br>"
                        + "Password: " + senha + "<br></br><br></br>";
            } else {
                return "Oi, seus dados de acesso são:" + "<br></br>"
                        + "Hi, your data access:" + "<br></br><br></br>"
                        + "Login:    " + login + "<br></br>"
                        + "Password: " + senha + "<br></br><br></br>";
                       
            }
        }
    }
}
