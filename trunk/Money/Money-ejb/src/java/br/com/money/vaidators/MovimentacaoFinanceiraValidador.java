/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.vaidators;

import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
import br.com.money.exceptions.MovimentacaoFinanceiraException;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.MovimentacaoFinanceira;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.Stateless;

/**
 *
 * @author Guilherme
 */
@Stateless(name = "movimentacaoFinanceiraValidador")
public class MovimentacaoFinanceiraValidador implements ValidadorInterface<MovimentacaoFinanceira, MovimentacaoFinanceiraBeanLocal> {

    @Override
    public void validar(MovimentacaoFinanceira entidade, MovimentacaoFinanceiraBeanLocal bean, Object object) throws ValidacaoException {
        if (entidade == null) {
            lancarException("movimentacaoFinanceiraNula", "Movimentação Financeira");
        }
        if(entidade.getDataMovimentacao() == null){
            lancarException("movimentacaoFinanceiraDataNula", "Data");
        }
        if(entidade.getSaldoAnterior() == null){
            lancarException("movimentacaoFinanceiraSaldoAnteriorNula", "Saldo Anterior");
        }
        if(entidade.getSaldoPosterior() == null){
            lancarException("movimentacaoFinanceiraSaldoPosteriorNulo", "Saldo Posterior");
        }
        //REAVALIAR SE CASCATA FOR INCLUÍDO EM MOVIMENTAÇÂO
        if(entidade.getContaBancaria() == null || entidade.getContaBancaria().getId() == null){
            lancarException("movimentacaoFinanceiraContaMovimentadaNula", "Conta Movimentada");
        }
        //REAVALIAR SE CASCATA FOR INCLUÍDO EM MOVIMENTAÇÂO
        if(entidade.getReceitaDivida() == null || entidade.getReceitaDivida().getId() == null){
            lancarException("movimentacaoFinanceiraReceitaDividaNula", "Receita/Pagamento");
        }
    }

    private void lancarException(String msg, String atributo) {
        MovimentacaoFinanceiraException due = new MovimentacaoFinanceiraException(msg, atributo);
        throw due;
    }
}
