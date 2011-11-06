/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.Scheduler;
import br.com.money.modelos.Usuario;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface SchedulerBeanLocal {
 
    /**
     * Salva ou atualiza u Scheduler existente.
     * @param scheduler 
     * @throws ValidacaoException
     */
    public void salvarScheduler(Scheduler scheduler) throws ValidacaoException;
    
    /**
     * Retorna o Scheduler do usuário do parâmetro ou nulo se o mesmo não tiver.
     * @param usuario
     * @return 
     */
    public Scheduler buscarSchedulerPorUsuario(Usuario usuario);
}
