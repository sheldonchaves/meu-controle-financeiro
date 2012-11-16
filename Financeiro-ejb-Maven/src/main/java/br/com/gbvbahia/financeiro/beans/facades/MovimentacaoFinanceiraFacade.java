/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoProcedimento;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoTrasnferencia;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.MovimentacaoFinanceira;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Usuário do Windows
 */
@Local
public interface MovimentacaoFinanceiraFacade extends InterfaceFacade<MovimentacaoFinanceira, Long> {

    /**
     * Busca a movimentação procedimento que possui relacionamento com o
     * procedimento encontrado.
     *
     * @param procedimento obrigatorio.
     * @return Movimentação encontrado, null se não encontrar.
     */
    MovimentacaoProcedimento buscarPorProcedimento(Procedimento procedimento);

    /**
     * Conta as transferencias de um usuario.
     *
     * @param usr Obrigatorio
     * @param data Opcional
     * @param debitada Opcional
     * @param creditada Opcional
     * @return
     */
    Long contarTransferencias(Usuario usr,
            Date data, ContaBancaria debitada, ContaBancaria creditada);

    /**
     * Busca as transferencias de um usuario de modo paginado.
     *
     * @param usr Obrigatorio
     * @param data Opcional
     * @param debitada Opcional
     * @param creditada Opcional
     * @param range Obrigatorio
     * @return
     */
    List<MovimentacaoTrasnferencia> buscarTransferencias(Usuario usr,
            Date data, ContaBancaria debitada, ContaBancaria creditada,
            int[] range);
}
