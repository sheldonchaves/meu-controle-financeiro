/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.enums.TipoMovimentacao;
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
public interface DetalheUsuarioBeanLocal extends InterfaceFacade<DetalheMovimentacao, Long> {

    /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário com filtro pela flag geral
     * @param usuario
     * @return 
     */
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuarioFlag(Usuario usuario, boolean flag);
   
     /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário com filtro pela flag geral e o tipo
     * @param usuario
     * @param flag
     * @param tipoMovimentacao
     * @return 
     */
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuarioFlagTipoMovimentacao(Usuario usuario, boolean flag, TipoMovimentacao tipoMovimentacao);
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
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário e pelo tipo
     * @param usuario
     * @param tipoMovimentacao
     * @return 
     */
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuarioTipoMovimentacao(Usuario usuario, TipoMovimentacao tipoMovimentacao);
    /**
     * Salva ou Atualiza o Detalhe Movimentação Passado
     * @param detalheMovimentacao
     * @throws ValidacaoException 
     */
    public void salvarDetalheMovimentacao(DetalheMovimentacao detalheMovimentacao) throws ValidacaoException;
   
}
