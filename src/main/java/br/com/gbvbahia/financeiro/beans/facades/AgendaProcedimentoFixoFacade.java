/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import java.util.Date;
import javax.ejb.Local;

/**
 * @since 01/04/2012
 * @author Guilherme
 */
@Local
public interface AgendaProcedimentoFixoFacade
        extends InterfaceFacade<AgendaProcedimentoFixo, Long> {

    /**
     * Retorna a maior data de vencimento das contas gravadas
     * derivadas de uma AgendaProcedimentoFIxo.
     *
     * @param agenda Agenda utilizada para realizar os agendamentos.
     * Obrigatório.
     * @return Maior data de vencimento ou null se não encontrar.
     */
    Date buscarUltimaData(AgendaProcedimentoFixo agenda);
}
