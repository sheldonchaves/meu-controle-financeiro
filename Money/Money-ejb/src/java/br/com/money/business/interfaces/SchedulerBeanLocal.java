/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.Scheduler;
import br.com.money.modelos.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface SchedulerBeanLocal extends AbstractFacadeLocal<Scheduler>{
 
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
    
    /**
     * Busca todos os Scheduler que possuam o Status passado.
     * @param status
     * @return 
     */
    public List<Scheduler> buscarTodosSchelersPorStatus(boolean status);
}
