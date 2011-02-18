/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.interfaces;

import br.com.financeiro.entidades.ContaReceber;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.enums.StatusReceita;
import br.com.financeiro.excecoes.ContaPagarReceberValueException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface ReceitaFinanceiraLocal {

    void salvarReceitaFinanceira(ContaReceber contaReceber) throws ContaPagarReceberValueException;

    void apagarReceitaFinanceira(ContaReceber contaReceber) throws ContaPagarReceberValueException;

    List<ContaReceber> buscarReceitasNaoRecebidas(User user,Date init, int maxRows);
    List<ContaReceber> buscarReceitasRecebidas(User user,Date init, int maxRows);

    List<ContaReceber> buscarReceitasPorData(User user, Date dataInit, Date dataFim, StatusReceita statusReceita);

    List<ContaReceber> buscarReceitasPorData(User user, Date dataInt, Date dataFim);
    
}
