/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.modelos.Scheduler;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Usuário do Windows
 */
@Local
public interface SchedulerFacade  extends InterfaceFacade<Scheduler, Long> {
    /**
     * Busca o Scheduler por usuário.
     * @param usuario Obrigatorio.
     * @return 
     */
    Scheduler buscarSchedulerPorUsuario(Usuario usuario);
    /**
     * Busca os schedulers por status.
     * @param status obrigatorio.
     * @return 
     */
    List<Scheduler> buscarTodosSchelersPorStatus(final boolean status);
}
