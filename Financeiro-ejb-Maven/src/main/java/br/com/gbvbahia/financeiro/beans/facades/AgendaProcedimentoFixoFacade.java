/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.constantes.Periodo;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 * @since 01/04/2012
 * @author Guilherme
 */
@Local
public interface AgendaProcedimentoFixoFacade
        extends InterfaceFacade<AgendaProcedimentoFixo, Long> {

    /**
     * Retorna a maior data de vencimento das contas gravadas derivadas de uma
     * AgendaProcedimentoFIxo.
     *
     * @param agenda Agenda utilizada para realizar os agendamentos.
     * Obrigatório.
     * @return Maior data de vencimento ou null se não encontrar.
     */
    Date buscarUltimaData(AgendaProcedimentoFixo agenda);

    /**
     * Conta agendas com parâmetros.
     *
     * @param user proprietário. Obrigatório
     * @param detalhe Detalhe da agenda. Opcional.
     * @param observacao Inicia com. Opcional.
     * @param tipo DESPESA RECEITA. Opcional.
     * @return Quantidade de agenda dentro do perfil.
     */
    Long countarAgendaPorUserDetalheObservacaoTipo(final Usuario user,
            final DetalheProcedimento detalhe,
            final String observacao, final TipoProcedimento tipo);

    /**
     *
     * @param user proprietário. Obrigatório
     * @param detalhe Detalhe da agenda. Opcional.
     * @param observacao Inicia com. Opcional.
     * @param tipo DESPESA RECEITA. Opcional.
     * @param range Intervalo de pesquisa.
     * @return Agendas no perfil. Lista vazia se não achar.
     */
    List<AgendaProcedimentoFixo> buscarAgendaPorUserDetalheObservacaoTipoPaginado(
            final Usuario user, final DetalheProcedimento detalhe,
            final String observacao, final TipoProcedimento tipo,
            final int[] range);
}
