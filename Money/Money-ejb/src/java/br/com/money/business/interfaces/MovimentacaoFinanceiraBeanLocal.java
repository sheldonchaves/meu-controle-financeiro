/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.enums.TipoConta;
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
public interface MovimentacaoFinanceiraBeanLocal
extends InterfaceFacade<MovimentacaoFinanceira, Long> {
    /**
     * Cria a movimentação financeira para a conta e a receita passada.
     * @param contaBancaria
     * @param receitaDivida 
     */
    public void salvarMovimentacaoFinanceira(ContaBancaria contaBancaria, ReceitaDivida receitaDivida) throws ValidacaoException;
    
    /**
     * LIMITADO A TRAZER SOMENTE MOVIMENTAÇÕES DE PAGAMENTOS E RECEITAS, QUE TENHAM ReceitaDivida NÃO NULO<BR>
     * Retornar as movimentações financeiras de um usuário ou conjuge do mesmo.
     * Páginada, por ser um grande volume de dados
     * @param posicaoInicial
     * @param tamanho Quantidade máxima de registros que serão retornados.
     * @param usuario
     * @return 
     */
    public List<MovimentacaoFinanceira> buscarMovimentacaoPorUsuarioStatusPaginada(int posicaoInicial, int tamanho, Usuario usuario, Long idContaBancaria);
    
    /**
     * LIMITADO A TRAZER SOMENTE MOVIMENTAÇÕES DE PAGAMENTOS E RECEITAS, QUE TENHAM ReceitaDivida NÃO NULO<BR>
     * Retorna a quantidade de registros de movimentação financeira de um determinado usuário.
     * Necessário para controle da paginação
     * @param usuario
     * @return 
     */
    public Integer buscarQtdadeMovimentacaoPorUsuarioStatusPaginada(Usuario usuario, Long idContaBancaria);
    
    /**
     * UTILIAR APENAS PARA DESFAZER AS MOVIMENTAÇÕES QUE ENVOLVAM ReceitaDivida, TRANSFERENCIA ENTRE CONTAS AINDA NÃO EXISTE COMO DESFAZER<BR>
     * Desfaz a movimentação financeira, credita o valor pago a conta, ou desconta o valor recebido, seta a conta como não paga.
     * @param movimentacaoFinanceira
     * @throws ValidacaoException 
     */
     public void desfazerMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) throws ValidacaoException;
     
     /**
     * UTILIZE PARA TRANSFERÊNCIAS ENTRE CONTAS
     * Cria uma movimentação fianceira que referencia uma transferência entre contas bancárias
     * @param contaDe
     * @param contaPara
     * @param valor
     * @throws ValidacaoException 
     */
    public void realizarTransferenciaEntreContas(ContaBancaria contaDe, ContaBancaria contaPara, double valor) throws ValidacaoException;
    
    /**
     * LIMITADO A TRAZER SOMENTE MOVIMENTAÇÕES DE PAGAMENTOS E RECEITAS, QUE TENHAM ReceitaDivida NÃO NULO COM O TIPO DE CONTA SELECIONADO<BR>
     * Retornar as movimentações financeiras de um usuário ou conjuge do mesmo.
     * Páginada, por ser um grande volume de dados
     * @param posicaoInicial
     * @param tamanho
     * @param usuario
     * @param tipoConta
     * @return 
     */
    public List<MovimentacaoFinanceira> buscarMovimentacaoPorUsuarioContaPaginada(int posicaoInicial, int tamanho, Usuario usuario, TipoConta tipoConta);
    
    /**
     * LIMITADO A TRAZER SOMENTE QUANTIDADES DE PAGAMENTOS E RECEITAS, QUE TENHAM ReceitaDivida NÃO NULO COM O TIPO DE CONTA SELECIONADO<BR>
     * Retornar as movimentações financeiras de um usuário ou conjuge do mesmo.
     * Páginada, por ser um grande volume de dados
     * @param usuario
     * @param tipoConta
     * @return 
     */
    public Integer buscarQtdadeMovimentacaoPorUsuarioContaPaginada(Usuario usuario, TipoConta tipoConta);

    /**
     * Busca a quantidade de transferências entres contas de um usuário e seu conjuge.
     * @param usuario Requerido
     * @return 
     */
    public Integer buscarQtdadeTodasTransferenciasEntreContasPaginada(Usuario usuario);

    /**
     * Recupera as transferências realizadas por um usuário ou seu conjuge.
     * 
     * @param posicaoInicial Requerido
     * @param tamanho Requerido
     * @param usuario Requerido
     * @return 
     */
    public List<MovimentacaoFinanceira> buscarTodasTransferenciasEntreContasPaginada(int posicaoInicial, int tamanho, Usuario usuario);

    public java.util.List<br.com.money.modelos.MovimentacaoFinanceira> buscarMovimentacaoFinanceiraPorUsuarioPeriodo(br.com.money.modelos.Usuario usuario, TipoConta tipoConta,java.util.Date ini, java.util.Date fim);
}
