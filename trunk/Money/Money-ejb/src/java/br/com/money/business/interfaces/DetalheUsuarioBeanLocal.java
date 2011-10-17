/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.DetalheMovimentacao;
import br.com.money.modelos.Usuario;
import br.com.money.modelos.embeddedId.DetalheUsuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface DetalheUsuarioBeanLocal {

    /**
     * Busca um DetalheUsuario pelo Usuario e DetalheMovimentacao
     * @param detalheUsuario
     * @param usuario
     * @return 
     */
    public DetalheUsuario buscarDetalheUsuarioPorDetalheMovimentacaoUsuario(DetalheMovimentacao Detalhemovimentacao, Usuario usuario);

    /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @return 
     */
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuario(Usuario usuario);

    /**
     * Devolve uma lista com todos Detalhes Movimentação que não são vinculados ao
     * usuário passado como argumento
     * @param usuario
     * @return 
     */
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoNaoUtilizadaPorUsuario(Usuario usuario);
    
    /**
     * Apaga todos os relacionamentos entre usuario e DetalheMovimentacao,
     * que é DetalheUsuario
     * @param usuario 
     */
    public void apagarTodosDetalheUsuarioPorUsuario(Usuario usuario);
    
    /**
     * Restaura os vinculos do usuario com DetalheMovimentação para
     * os DetalheMovimentação flegado geral para true.
     * @param usuario 
     * @throws ValidacaoException
     */
    public void criarVinculoPadraoDetalheMovimentacaoComUsuario(Usuario usuario) throws ValidacaoException;
    
    /**
     * Salva um DetalheUsuario no Banco de dados
     * @param detalheUsuario
     * @throws ValidacaoException 
     */
    public void salvarDetalheUsuario(DetalheUsuario detalheUsuario) throws ValidacaoException;
    
    /**
     * Busca DetalheMovimentacao pelo atributo detalhe passado como parâmetro.
     * @param detalhe
     * @return DetalheMovimentacao ou nulo se não encontrar.
     */
    public DetalheMovimentacao buscarDetalheMovimentacaoPorDetalhe(String detalhe);
    
    /**
     * Salva ou Atualiza o Detalhe Movimentação Passado
     * @param detalheMovimentacao
     * @throws ValidacaoException 
     */
    public void salvarDetalheMovimentacao(DetalheMovimentacao detalheMovimentacao,  Usuario usuairo) throws ValidacaoException;
}
