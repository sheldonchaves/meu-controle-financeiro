/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.DetalheMovimentacao;
import br.com.money.modelos.Usuario;
import br.com.money.modelos.embeddedId.DetalheUsuario;
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

    @EJB(beanName = "detalheUsuarioValidador")
    private ValidadorInterface detalheUsuarioValidador;

    @PersistenceContext(name="jdbc/money")
    private EntityManager manager;

    /**
     * Apaga todos os relacionamentos entre usuario e DetalheMovimentacao,
     * que é DetalheUsuario
     * @param usuario 
     */
    @Override
    public void apagarTodosDetalheUsuarioPorUsuario(Usuario usuario) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.apagarTodosDetalheUsuarioPorUsuario");
        q.setParameter("usuario", usuario);
        q.executeUpdate();
    }

    /**
     * Devolve uma lista com todos os Detalhes Movimentação de um usuário
     * @param usuario
     * @return 
     */
    @Override
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoPorUsuario(Usuario usuario) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.buscarDetalheMovimentacaoPorUsuario");
        q.setParameter("usuario", usuario);
        return q.getResultList();
    }

    /**
     * Devolve uma lista com todos Detalhes Movimentação que não são vinculados ao
     * usuário passado como argumento
     * @param usuario
     * @return 
     */
    @Override
    public List<DetalheMovimentacao> buscarDetalheMovimentacaoNaoUtilizadaPorUsuario(Usuario usuario) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.buscarDetalheMovimentacaoNaoUtilizadaPorUsuario");
        q.setParameter("usuario", usuario);

        return q.getResultList();
    }

    /**
     * Busca DetalheMovimentacao pelo atributo detalhe passado como parâmetro.
     * Utilizado na VALIDAÇÃO NÃO INSERIR FILTROS
     * @param detalhe
     * @return DetalheMovimentacao ou nulo se não encontrar.
     */
    @Override
    public DetalheMovimentacao buscarDetalheMovimentacaoPorDetalhe(String detalhe) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.buscarDetalheMovimentacaoPorDetalhe");
        q.setParameter("detalhe", detalhe);
        try {
            return (DetalheMovimentacao) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Busca um DetalheUsuario pelo Usuario e DetalheMovimentacao
     * @param detalheUsuario
     * @param usuario
     * @return 
     */
    @Override
    public DetalheUsuario buscarDetalheUsuarioPorDetalheMovimentacaoUsuario(DetalheMovimentacao Detalhemovimentacao, Usuario usuario) {
        Query q = manager.createNamedQuery("DetalheUsuarioBean.buscarDetalheUsuarioPorDetalheMovimentacaoUsuario");
        q.setParameter("detalheMovimentacao", Detalhemovimentacao);
        q.setParameter("usuario", usuario);
        try {
            return (DetalheUsuario) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Restaura os vinculos do usuario com DetalheMovimentação para
     * os DetalheMovimentação flegado geral para true.
     * @param usuario 
     */
    @Override
    public void criarVinculoPadraoDetalheMovimentacaoComUsuario(Usuario usuario) throws ValidacaoException {
        apagarTodosDetalheUsuarioPorUsuario(usuario);
        Query q = manager.createNamedQuery("DetalheUsuarioBean.criarVinculoPadraoDetalheMovimentacaoComUsuario");
        q.setParameter("geral", true);
        List<DetalheMovimentacao> detalhes = q.getResultList();
        for (DetalheMovimentacao det : detalhes) {
            DetalheUsuario du = new DetalheUsuario(det, usuario);
            salvarDetalheUsuario(du);
        }
    }

    @Override
    public void salvarDetalheUsuario(DetalheUsuario detalheUsuario) throws ValidacaoException {
        detalheUsuarioValidador.validar(detalheUsuario, this, null);
        manager.persist(detalheUsuario);
        manager.flush();
    }

    @Override
    public void salvarDetalheMovimentacao(DetalheMovimentacao detalheMovimentacao, Usuario usuairo) throws ValidacaoException {
        detalheMobvimentacaoValidador.validar(detalheMovimentacao, this, null);
        if (detalheMovimentacao.getId() == null) {
            manager.persist(detalheMovimentacao);
            manager.flush();
            salvarDetalheUsuario(new DetalheUsuario(detalheMovimentacao, usuairo));
        } else {
            manager.merge(detalheMovimentacao);
        }
        manager.flush();
    }
}
