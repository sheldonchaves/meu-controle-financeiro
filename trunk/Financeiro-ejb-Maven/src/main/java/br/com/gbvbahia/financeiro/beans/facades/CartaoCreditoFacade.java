/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface responsável pela entidade cartão de crédito.
 * @since 13/04/2012
 * @author Guilherme
 */
@Local
public interface CartaoCreditoFacade extends
        InterfaceFacade<CartaoCredito, Long> {
    /**
     * Busca todos os cartões ativos de um usuário.
     * @param usuario Usuário ou conjuge proprietário.
     * @return List&lt;CartaoCredito&gt;
     */
    List<CartaoCredito> buscarCartoesAtivos(Usuario usuario);
}
