/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.modelos.EmailProperties;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface EmailPropertiesFacade extends InterfaceFacade<EmailProperties, Long> {
    /**
     * Busca as propriedades de envio de e-mails ativo.
     * Se houve mais de uma traz a primeira.
     * Se não houver nenhum retorna null.
     * Em todas as duas situações um log é escrito no sistema.
     * @return 
     */
    public EmailProperties buscarEmailAtivo();
}
