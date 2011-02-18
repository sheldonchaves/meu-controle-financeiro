/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.interfaces;

import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.enums.StatusPagamento;
import br.com.financeiro.excecoes.ContaPagarReceberValueException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface ContaPagarReceberLocal {

    void salvarContaPagarReceber(ContaPagar contaPagarReceber) throws ContaPagarReceberValueException;

    List<ContaPagar> buscarContasPorData(Date dataInit, Date dataFim, StatusPagamento statusPagamento, User user);

    List<ContaPagar> buscarContasPorData(Date dataInit, Date dataFim, User user);

    void deletarContaPagarReceber(ContaPagar contaPagarReceber) throws ContaPagarReceberValueException;

    List<ContaPagar> buscarContasNaoPagas(User user, Date init, int maxRows);
    List<ContaPagar> buscarContasPagas(User user, Date init, int maxRows);

    void deletarContaPagarReceberPorIdentificador(String identificador, int parcela);

    List<ContaPagar> buscarContasPagarObservacao(String observacao, Date inicial, User user);


    
}
