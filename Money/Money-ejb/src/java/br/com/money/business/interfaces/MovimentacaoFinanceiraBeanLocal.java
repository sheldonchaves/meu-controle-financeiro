/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.ReceitaDivida;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface MovimentacaoFinanceiraBeanLocal {
    public void salvarMovimentacaoFinanceira(ContaBancaria contaBancaria, ReceitaDivida receitaDivida);
}
