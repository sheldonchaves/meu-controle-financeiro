/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.modelos.Usuario;
import java.util.*;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface ReceitaDividaBeanLocal extends AbstractFacadeLocal<ReceitaDivida> {

    /**
     * Salva uma ReceitaDivida
     * @param conta
     * @throws ValidacaoException 
     */
    public void salvarReceitaDivida(ReceitaDivida conta) throws ValidacaoException;

    /**
     * Salva a conta e suas as parcelas
     * @param ReceitaDivida conta Uma receitaDivida válida
     * @param parcelas Um valor maior ou igual a 1.
     * @param salvarParcelas
     * @param contaBancaria
     * @param statusParcelas
     * @throws ValidacaoException 
     */
    public void salvarReceitaDivida(ReceitaDivida conta, int parcelas,boolean salvarParcelas, ContaBancaria contaBancaria, StatusPagamento statusParcelas) throws ValidacaoException;

    /**
     * Consulta utilizada para paginação de contas, devido umentar cada vez mais será necessário
     * consultar de moto tardio, para que não ocorra problemas de desempenho.
     * @param posicaoInicial Onde irá iniciar a pesquisa dos registros
     * @param tamanho Quantidade de dados máximo que será retornado
     * @param usuario O proprietário das contas
     * @param statusPagamento Se pago ou não pago
     * @param tipoMovimentacao Receita ou Dívida
     * @return Uma List de ReceitaDivida
     */
    List<ReceitaDivida> buscarReceitaDividasPorUsuarioStatusPaginada(int posicaoInicial, int tamanho, Usuario usuario,
            StatusPagamento statusPagamento, TipoMovimentacao tipoMovimentacao);

    /**
     * Traz a quantidade total de itens dentro do perfil do método<br>
     * buscarReceitaDividasPorUsuarioStatusPaginada<br>
     * @param usuario
     * @param statusPagamento
     * @param tipoMovimentacao
     * @return 
     */
    Integer buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(Usuario usuario, StatusPagamento statusPagamento, TipoMovimentacao tipoMovimentacao);

    /**
     * Apaga a ReceitaDivida passada. Se quiser apagar as parcelas passar true no segundo parâmetro.
     * @param receitaDivida
     * @param deleteParcelas 
     * @throws ValidacaoException se status da conta passada  estiver como PAGA
     */
    void apagarReceitaDivida(ReceitaDivida receitaDivida, boolean deleteParcelas) throws ValidacaoException;

    /**
     * Realiza busca de Receita Divida pelos filtros passados como parâmetro
     * Ordenação de data crescente realizada.
     * @param posicaoInicial Onde inicia a consulta
     * @param tamanho Quantidade máxima de resultados
     * @param usuario O usuário proprietário das receitas ou dividas
     * @param statusPagamento Se pago ou não.
     * @return  Uma lista com os elementos ReceitaDivida dentro do perfil
     */
    public List<ReceitaDivida> buscarReceitaDividasPorUsuarioStatusPaginada(int posicaoInicial, int tamanho,
            Usuario usuario, StatusPagamento statusPagamento);

    /**
     * Retorna a quantidade de itens dentro do perfil dos parâmetros paUm iteiro que representa a quantidade de itens no perfil
     */
    public Integer buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(Usuario usuario, StatusPagamento statusPagamento);

    /**
     * Busca as receitas ou dividas de acordo com os parâmetros passados
     * @param ini Data Inicial
     * @param fim Data final
     * @param usuario Usuario proprietário das contas
     * @param status Paga ou Não Paga
     * @param tipo  Receita ou Divida
     * @return 
     */
    public List<ReceitaDivida> buscarReceitaDividasPorDataUsuarioStatusTipoMovimentacao(Date ini, Date fim,
            Usuario usuario, StatusPagamento status, TipoMovimentacao tipo);
}
