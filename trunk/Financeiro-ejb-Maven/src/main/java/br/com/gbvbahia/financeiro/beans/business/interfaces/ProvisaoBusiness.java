/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.interfaces;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import javax.ejb.Local;

/**
 * Representa interface de negócio de Provisão.
 * @since 14/04/2012
 * @author Guilherme
 */
@Local
public interface ProvisaoBusiness {
    /**
     * Cria provisões com base em uma agenda, não duplica, pois
     * verifica a ultima data provisionada e acrescenta somente
     * provisões posteriores a esta data.
     * @param agenda Agenda a ser provisionada.
     */
    void provisionar(AgendaProcedimentoFixo agenda);
    /**
     * Cria a agenda e realiza a provisão das contas.
     * @param agenda
     * @throws NegocioException 
     */
    void criarAgendaEProvisionar(AgendaProcedimentoFixo agenda) throws NegocioException;
    /**
     * Atauliza a agenda e as provisão não pagas geradas com a mesma.
     * @param agenda
     * @throws NegocioException 
     */
    void atualizarProvisao(AgendaProcedimentoFixo agenda) throws NegocioException;
    
    /**
     * Altera o status da agenda.
     * Se false: remove todas as provisões não pagas
     * Se true: realiza o provisionamento.
     * @param agenda
     * @throws NegocioException 
     */
    void alterarStatusProvisao(AgendaProcedimentoFixo agenda) throws NegocioException;
}
