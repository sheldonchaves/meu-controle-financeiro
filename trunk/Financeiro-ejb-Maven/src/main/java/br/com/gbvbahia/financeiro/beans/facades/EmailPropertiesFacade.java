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
}
