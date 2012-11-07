/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.interfaces;

import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import javax.ejb.Local;

/**
 * Representa interface de negócio de Provisão.
 * @since 14/04/2012
 * @author Guilherme
 */
@Local
public interface ProvisaoFacade {
    /**
     * Cria provisões com base em uma agenda, não duplica, pois
     * verifica a ultima data provisionada e acrescenta somente
     * provisões posteriores a esta data.
     * @param agenda Agenda a ser provisionada.
     */
    void provisionar(AgendaProcedimentoFixo agenda);
}
