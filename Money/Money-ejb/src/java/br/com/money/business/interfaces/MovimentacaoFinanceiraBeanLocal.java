/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.MovimentacaoFinanceira;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.modelos.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface MovimentacaoFinanceiraBeanLocal {
    /**
     * Cria a movimentação financeira para a conta e a receita passada.
     * @param contaBancaria
     * @param receitaDivida 
     */
    public void salvarMovimentacaoFinanceira(ContaBancaria contaBancaria, ReceitaDivida receitaDivida) throws ValidacaoException;
    /**
     * Retornar as movimentações financeiras de um usuário ou conjuge do mesmo.
     * Páginada, por ser um grande volume de dados
     * @param posicaoInicial
     * @param tamanho Quantidade máxima de registros que serão retornados.
     * @param usuario
     * @return 
     */
    public List<MovimentacaoFinanceira> buscarMovimentacaoPorUsuarioStatusPaginada(int posicaoInicial, int tamanho, Usuario usuario);
    
    /**
     * Retorna a quantidade de registros de movimentação financeira de um determinado usuário.
     * Necessário para controle da paginação
     * @param usuario
     * @return 
     */
    public Integer buscarQtdadeMovimentacaoPorUsuarioStatusPaginada(Usuario usuario);
    
    /**
     * Desfaz a movimentação financeira, credita o valor pago a conta, ou desconta o valor recebido, seta a conta como não paga.
     * @param movimentacaoFinanceira
     * @throws ValidacaoException 
     */
     public void desfazerMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) throws ValidacaoException;
}
