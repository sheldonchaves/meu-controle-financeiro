/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.interfaces;

import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.interfaces.Conta;
import br.com.financeiro.excecoes.MovimentacaoFinanceiraException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface MovimentacaoFinanceiraLocal {

    List<Conta> buscarContasNaoVencidas(User user,Date init, int maxRows);

    void criarMovimentacaoFinanceira(Conta conta) throws MovimentacaoFinanceiraException;

    void removerMovimentacaoFinanceira(Conta conta) throws MovimentacaoFinanceiraException;

    public List<Conta> buscarContasPagas(User user, Date dataInit, int maxResult);
    public List<Conta> buscarContasPagas(User user, Date dataInit, Date dataFim);
    
}
