/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import javax.ejb.Local;

/**
 * @since v.3 29/03/2012
 * @author Guilherme
 */
@Local
public interface UsuarioFacade extends InterfaceFacade<Usuario, String> {

    /**
     * Objeto Usuario referente ao usuario logado.
     * @return  br.com.gbvbahia.usuarios.modelos.Usuario
     */
    Usuario getUsuario();

    /**
     * Login do usuário logado.
     * @return Login usuario.
     */
    String getUsuarioId();

    /**
     * Verifica se o usuário logado é de um determinado grupo.
     * @param grupoId String referente ao nome único do grupo
     * que se deseja verificar.
     * @return true se estiver e false se não.
     */
    boolean usuarioInGrupo(final String grupoId);
    
}
