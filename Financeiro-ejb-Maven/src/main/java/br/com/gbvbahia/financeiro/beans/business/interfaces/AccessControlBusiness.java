/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.interfaces;

import br.com.gbvbahia.financeiro.modelos.Usuario;
import javax.ejb.Local;

/**
 * Realiza controle de acesso do usuário ao sistema. Inicialmente essa classe
 * parece simples, mas as regras de validação e segurança vão aumentando, de
 * acordo com o crescimento do sistema, esse bean foi criado para cuidar desta
 * parte do modo mais transparente possível para o restante da aplicação.
 * 
 * @since v.1 01/06/2012
 * @author Guilherme Braga
 */
@Local
public interface AccessControlBusiness {
    /**
     * Retorna o ID do usuário logado no sistema.
     *
     * @return java.lang.String
     */
    String getUsuarioId();

    /**
     * Retorna o Usuario logado no sistema.
     *
     * @return Usuario
     */
    Usuario getUsuarioLogado();

    /**
     * Retorna um booleano referente ao grupo passado.
     *
     * @param grupoId java.lang.String
     * @return boolean true se estiver e false se não.
     */
    boolean usuarioInGrupo(String grupoId);

}
