/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.List;
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
    
    /**
     * Faz amarração entre os usuários como conjuges.
     * @param usr1 Usuario a ser atrelado ao segundo.
     * @param usr2 Usuario a ser atrelado ao primeiro.
     * @throws NegocioException se algo estiver invalido.
     */
    void definirConjuge(Usuario usr1, Usuario usr2) throws NegocioException;
    
    /**
     * Alterar senha do usuário
     * @param usuario
     * @param novaSenha
     * @throws NegocioException 
     */
    public void alterarSenha(final Usuario usuario,
            final String novaSenha) throws NegocioException;
    /**
     * Parametros são opcionais.
     * @param nome
     * @param login
     * @return 
     */
     public Integer contarPorNomeLogin(final String nome, final String login);
     /**
      * Parametros são opcionais
      * @param nome
      * @param login
      * @param range
      * @return 
      */
     public List<Usuario> buscarUsuarioPorNomeLogin(final String nome,
            final String login, final int[] range);
     
     /**
      * Busca usuário com e-mail informado.
      * @param email Obrigatório.
      * @return 
      */
     public Usuario buscarPorEmail(String email);
}
