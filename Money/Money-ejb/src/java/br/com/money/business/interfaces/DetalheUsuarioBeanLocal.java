/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.DetalheMovimentacao;
import br.com.money.modelos.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface DetalheUsuarioBeanLocal {

    /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário com filtro pela flag geral
     * @param usuario
     * @return 
     */
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuarioFlag(Usuario usuario, boolean flag);
    
    /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @return 
     */
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuario(Usuario usuario);
    
   
   
    /**
     * Busca DetalheMovimentacao pelo atributo detalhe passado como parâmetro.
     * @param detalhe
     * @return DetalheMovimentacao ou nulo se não encontrar.
     */
    public DetalheMovimentacao buscarDetalheMovimentacaoPorDetalheUsuario(String detalhe, Usuario usuario);
    
    /**
     * Salva ou Atualiza o Detalhe Movimentação Passado
     * @param detalheMovimentacao
     * @throws ValidacaoException 
     */
    public void salvarDetalheMovimentacao(DetalheMovimentacao detalheMovimentacao) throws ValidacaoException;

    /**
     * Buscar detalhe movimentação por Id
     * @param id
     * @return Detalhe Movimentação com mesmo id
     */
    public DetalheMovimentacao buscarDetalheMovimentacaoPorId(long id);
   
}
