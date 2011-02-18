/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.interfaces;

import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoGasto;
import br.com.financeiro.entidades.enums.FormaPagamento;
import br.com.financeiro.entidades.enums.StatusPagamento;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface RelatoriosLocal {

    Double pagamentoAcumuladoMes(Calendar mesAno,User user);

    Double receitaAcumuladaMes(Calendar mesAno, User user);
    public List<Object[]> contaPagarFormaPagto(FormaPagamento formaPagamento, User user, Calendar mesAno);

    /**
     * Retorna Array de Objetos contendo dois valores Double
     * 1º Total Contas Não Pagas
     * 2º Total Contas Pagas
     * @param user
     * @param data
     * @return
     */
    Double[] buscarNPagoPago(User user, Calendar mesAno);

    Double buscarValoresStatus(User user, Calendar mesAno, StatusPagamento status);

    List<Object[]> buscarGastoPorGrupoGasto(Calendar mesAno, StatusPagamento statuPagamento, User user);

    List<Object[]> buscarGastoPorGrupoGasto(Calendar mesAno, User user);

    List<ContaPagar> buscarContasPorGrupoGasto(User user, Calendar mesAno, GrupoGasto grupoGasto);
}
