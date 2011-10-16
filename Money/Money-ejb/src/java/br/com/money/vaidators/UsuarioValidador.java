/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.vaidators;

import br.com.money.business.interfaces.UsuarioBeanLocal;
import br.com.money.exceptions.UsuarioException;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.Usuario;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;

/**
 *
 * @author gbvbahia
 */
@Stateless(name="usuarioValidador")
public class UsuarioValidador implements ValidadorInterface<Usuario,UsuarioBeanLocal> {

    private void validar(Usuario entidade, UsuarioBeanLocal bean) throws ValidacaoException {

        if(entidade == null){
            lancarException(("usuarioNulo"),"Usuário");
        }
        if (StringUtils.isBlank(entidade.getLogin())) {
            lancarException(("usuarioLoginNulo"),"Login");
        }
        if (entidade.getLogin().length() > 10) {
            lancarException(("usuarioLoginLong"),"Login");
        }
        if (StringUtils.isBlank(entidade.getPassword())) {
            lancarException(("usuarioSenhaNulo"),"Senha");
        }
        if (entidade.getPassword().length() > 50) {
            lancarException(("usuarioSenhaLong"),"Senha");
        }
        if (StringUtils.isBlank(entidade.getFirstName())) {
            lancarException(("usuarioNomeNulo"),"Nome");
        }
        if (entidade.getFirstName().length() > 30) {
            lancarException(("usuarioNomeLong"),"Nome");
        }
        if (StringUtils.isBlank(entidade.getLastName())) {
            lancarException(("usuarioSobreNomeNulo"),"Último Nome");
        }
        if (entidade.getLastName().length() > 30) {
            lancarException(("usuarioSobreNomeLong"),"Último Nome");
        }
        if (StringUtils.isBlank(entidade.getEmail())) {
            lancarException(("usuarioEmailNulo"),"E-mail");
        }
        if (entidade.getEmail().length() > 100) {
            lancarException(("usuarioEmailLong"),"E-mail");
        }
        if (!isValidEmail(entidade.getEmail())) {
            lancarException(("usuarioEmailErro"),"E-mail");
        }
        if (entidade.getRoles() == null || entidade.getRoles().isEmpty()) {
            lancarException(("usuarioRolesEmpty"),"Nível de Acesso");
        }
        if (entidade.getId() == null) {
            if (bean.buscarUsuarioByLogin(entidade.getLogin()) != null) {
                lancarException(("usuarioLoginExistente"), "Login");
            }
            if (bean.buscarUsuarioByEmail(entidade.getEmail()) != null) {
                lancarException(("usuarioEmailExistente"), "E-mail");
            }
        } else {
            Usuario temp = bean.buscarUsuarioByLogin(entidade.getLogin());
            if (temp != null && !temp.getId().equals(entidade.getId())) {
                lancarException(("usuarioLoginExistente"), "Login");
            }
            temp = bean.buscarUsuarioByEmail(entidade.getEmail());
            if (temp != null && !temp.getId().equals(entidade.getId())) {
                lancarException(("usuarioEmailExistente"), "E-mail");
            }
        }
    }

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    private void lancarException(String msg, String atributo) {
        UsuarioException ue = new UsuarioException(msg);
        ue.setAtributoName(atributo);
        throw ue;
    }

    @Override
    public void validar(Usuario entidade, UsuarioBeanLocal bean, Object object) throws ValidacaoException {
        this.validar(entidade, bean);
    }
}
