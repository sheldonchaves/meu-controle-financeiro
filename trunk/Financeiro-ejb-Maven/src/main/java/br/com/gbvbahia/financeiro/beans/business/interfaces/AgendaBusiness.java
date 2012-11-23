/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.interfaces;

import javax.ejb.Local;

/**
 *
 * @author Usuário do Windows
 */
@Local
public interface AgendaBusiness {
    /**
     * Buscas as agendas ativas e provisiona as mesmas.
     */
    public void provisionarAgendas();
}
