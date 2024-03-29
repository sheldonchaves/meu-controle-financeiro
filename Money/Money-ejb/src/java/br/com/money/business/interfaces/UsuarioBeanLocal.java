/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.Role;
import br.com.money.modelos.Usuario;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface UsuarioBeanLocal extends InterfaceFacade<Usuario, Long> {

    Usuario buscarUsuarioByLogin(String login);

    Usuario buscarUsuarioByEmail(String email);

    void criarUsuario(Usuario usuario)throws ValidacaoException;
     public String criptografarSenha(String senha, String role);

    Role buscarRoleByName(String nomeRole);

    void criarRole(Role role);
}
