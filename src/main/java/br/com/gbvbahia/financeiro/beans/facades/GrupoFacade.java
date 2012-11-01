/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.modelos.Grupo;
import javax.ejb.Local;

/**
 * @since v.3 29/03/2012
 * @author Guilherme
 */
@Local
public interface GrupoFacade extends InterfaceFacade<Grupo, String> {

}
