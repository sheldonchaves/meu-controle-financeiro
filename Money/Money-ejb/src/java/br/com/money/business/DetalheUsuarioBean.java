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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author gbvbahia
 */
@Stateless
public class DetalheUsuarioBean implements DetalheUsuarioBeanLocal {

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
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuarioFlag(Usuario usuario, boolean flag) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.buscarDetalheMovimentacaoPorUsuarioFlag");
        q.setParameter("usuario", usuario);
        q.setParameter("ativo", flag);
        return q.getResultList();
    }

        /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @param ativo Se o detalhe está ativado pelo usuario
     * @return 
     * @NamedQuary true
     */
    @Override
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuarioFlagTipoMovimentacao(Usuario usuario, boolean flag, TipoMovimentacao tipoMovimentacao) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.buscarDetalheMovimentacaoPorUsuarioFlagTipoMovimentacao");
        q.setParameter("usuario", usuario);
        q.setParameter("ativo", flag);
         q.setParameter("tipoMovimentacao", tipoMovimentacao);
        return q.getResultList();
    }
    
    /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @return 
     * @NamedQuary true
     */
    @Override
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuario(Usuario usuario) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.buscarDetalheMovimentacaoPorUsuario");
        q.setParameter("usuario", usuario);
        return q.getResultList();
    }

        /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @return 
     * @NamedQuary true
     */
    @Override
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuarioTipoMovimentacao(Usuario usuario, TipoMovimentacao tipoMovimentacao) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.buscarDetalheMovimentacaoPorUsuarioTipoMovimentacao");
        q.setParameter("usuario", usuario);
        q.setParameter("tipoMovimentacao", tipoMovimentacao);
        return q.getResultList();
    }
    
    /**
     * Busca DetalheMovimentacao pelo atributo detalhe passado como parâmetro.
     * Utilizado na VALIDAÇÃO NÃO INSERIR FILTROS
     * @param detalhe
     * @return DetalheMovimentacao ou nulo se não encontrar.
     * @NamedQuary true
     */
    @Override
    public DetalheMovimentacao buscarDetalheMovimentacaoPorDetalheUsuario(String detalhe, Usuario usuario) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.buscarDetalheMovimentacaoPorDetalheUsuario");
        q.setParameter("detalhe", detalhe);
        q.setParameter("usuario", usuario);
        try {
            return (DetalheMovimentacao) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Buscar detalhe movimentação por Id
     * @param id
     * @return Detalhe Movimentação com mesmo id
     * @NamedQuary false
     */
    @Override
    public DetalheMovimentacao buscarDetalheMovimentacaoPorId(long id) {
        return manager.find(DetalheMovimentacao.class, id);
    }

    /**
     * Salva ou Atualiza o Detalhe Movimentação Passado
     * @param detalheMovimentacao
     * @throws ValidacaoException
     * @NamedQuary false
     */
    @Override
    public void salvarDetalheMovimentacao(DetalheMovimentacao detalheMovimentacao) throws ValidacaoException {
        this.detalheMobvimentacaoValidador.validar(detalheMovimentacao, this, null);
        if (detalheMovimentacao.getId() == null) {
            this.manager.persist(detalheMovimentacao);
        } else {
            this.manager.merge(detalheMovimentacao);
        }
        manager.flush();
    }
}
