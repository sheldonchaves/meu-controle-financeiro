/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.interfaces;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface RegistroBusiness {
    /**
     * Registra o usuário e envia e-mail com a senha do mesmo.
     * @param login
     * @param email
     * @param url 
     */
    public void registroUsuario(String login, String email, String url) throws NegocioException;
    /**
     * Reseta a senha do usuário e envia a mesma por e-mail.
     * @param email Email do usuário
     * @param url URL do sistema
     * @throws NegocioException Se não encontrar o e-mail informado.
     */
    public void recuperarSenha(String email, String url) throws NegocioException;
}
