/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.DetalheMovimentacao;
import br.com.money.modelos.Usuario;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gbvbahia
 */
@Stateless
public class DetalheUsuarioBean extends 
        AbstractFacade<DetalheMovimentacao, Long>
implements DetalheUsuarioBeanLocal {

    @Override
    protected EntityManager getEntityManager() {
        return this.manager;
    }

    @Override
    protected ValidadorInterface getValidador() {
        return this.detalheMobvimentacaoValidador;
    }

    public DetalheUsuarioBean() {
        super(DetalheMovimentacao.class);
    }

    @EJB(beanName = "detalheMobvimentacaoValidador")
    private ValidadorInterface detalheMobvimentacaoValidador;
    @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;

    /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @param ativo Se o detalhe está ativado pelo usuario
     * @return 
     * @NamedQuary true
     */
    @Override
    public List<DetalheMovimentacao> 
            buscarDetalheMovimentacaoPorUsuarioFlag(
            final Usuario usuario, final boolean flag) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("ativo", flag);
        return listPesqParam("DetalheUsuarioBean.buscarDetalhe"
                + "MovimentacaoPorUsuarioFlag", parans);
    }

        /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @param ativo Se o detalhe está ativado pelo usuario
     * @return 
     * @NamedQuary true
     */
    @Override
    public List<DetalheMovimentacao>
            buscarDetalheMovimentacaoPorUsuarioFlagTipoMovimentacao(
            final Usuario usuario, final boolean flag,
            final TipoMovimentacao tipoMovimentacao) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("ativo", flag);
        parans.put("tipoMovimentacao", tipoMovimentacao);
        return listPesqParam("DetalheUsuarioBean.buscarDetalhe"
                + "MovimentacaoPorUsuarioFlagTipoMovimentacao", parans);
    }
    
    /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @return 
     * @NamedQuary true
     */
    @Override
    public List<DetalheMovimentacao>
            buscarDetalheMovimentacaoPorUsuario(final Usuario usuario) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        return listPesqParam("DetalheUsuarioBean.buscarDetalhe"
                + "MovimentacaoPorUsuario", parans);
    }

   /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @return 
     * @NamedQuary true
     */
    @Override
    public List<DetalheMovimentacao> 
            buscarDetalheMovimentacaoPorUsuarioTipoMovimentacao(
            final Usuario usuario, 
            final TipoMovimentacao tipoMovimentacao) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("tipoMovimentacao", tipoMovimentacao);
        return listPesqParam("DetalheUsuarioBean.buscarDetalhe"
                + "MovimentacaoPorUsuarioTipoMovimentacao", parans);
    }

    /**
     * Busca DetalheMovimentacao pelo atributo detalhe passado como parâmetro.
     * Utilizado na VALIDAÇÃO NÃO INSERIR FILTROS
     * @param detalhe
     * @return DetalheMovimentacao ou nulo se não encontrar.
     * @NamedQuary true
     */
    @Override
    public DetalheMovimentacao
            buscarDetalheMovimentacaoPorDetalheUsuario(
            final String detalhe, final Usuario usuario) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("detalhe", detalhe);
        parans.put("usuario", usuario);
        return pesqParam("DetalheUsuarioBean.buscarDetalhe"
                + "MovimentacaoPorDetalheUsuario", parans);
    }

    /**
     * Salva ou Atualiza o Detalhe Movimentação Passado
     * @param detalheMovimentacao
     * @throws ValidacaoException
     * @NamedQuary false
     */
    @Override
    public void salvarDetalheMovimentacao(
            final DetalheMovimentacao detalheMovimentacao)
            throws ValidacaoException {
        if (detalheMovimentacao.getId() == null) {
            create(detalheMovimentacao);
        } else {
            update(detalheMovimentacao);
        }
    }
}
