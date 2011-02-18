/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans;

import br.com.financeiro.ejbbeans.interfaces.CartaoCreditoLocal;
import br.com.financeiro.entidades.CartaoCreditoUnico;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.comparator.CartaoCreditoUnicoComparator;
import br.com.financeiro.excecoes.CartaoCreditoInformacaoIncompletaException;
import br.com.financeiro.excecoes.CartaoCreditoNumeroExisteException;
import br.com.financeiro.excecoes.CartaoCreditoNumeroInvalidoException;
import br.com.financeiro.utils.UtilBeans;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
@Stateless
public class CartaoCreditoBean implements CartaoCreditoLocal {

    @PersistenceContext(unitName = "financeiro")
    private EntityManager manager;

    /**
     * Utilizado para salvar e atualizar o cartão de credito
     * Se o cartão não tiver id e seu número já estiver cadastrado no bando de
     * dados uma exceção CartaoCreditoNumeroExisteException será lançada.
     * Se o número do cartão não for um número válido um exceção será lançada.
     * @param cartaoCreditoUnico
     * @param proprietario
     * @exception CartaoCreditoNumeroExisteException
     */
    public void salvarCartaoCredito(CartaoCreditoUnico cartaoCreditoUnico, User user) throws CartaoCreditoNumeroExisteException, CartaoCreditoNumeroInvalidoException, CartaoCreditoInformacaoIncompletaException {
        if (!validaCartaoCredito(cartaoCreditoUnico)) {
            throw new CartaoCreditoInformacaoIncompletaException("Algumas iformações do cartão estão incompletas, cartão não foi salvo.");
        }
        if (!validaNumeroCartao(cartaoCreditoUnico.getNumeroCartao())) {
            throw new CartaoCreditoNumeroInvalidoException("Número do cartão de crédito não é válido.");
        }
        cartaoCreditoUnico.setUser(user);
        if (cartaoCreditoUnico.getId() != null) {
            //1º Bloqueia os dependetes
            if(!cartaoCreditoUnico.isStatusCartao()){
                List<CartaoCreditoUnico> listToBloq = buscarCartoesDependentes(cartaoCreditoUnico);
                for(CartaoCreditoUnico toBloq : listToBloq){
                    toBloq.setStatusCartao(false);
                    manager.merge(toBloq);
                }
            }
            manager.merge(cartaoCreditoUnico);
            manager.flush();
        } else {
            if (buscaCartaoPorNumero(cartaoCreditoUnico.getNumeroCartao()) != null) {
                throw new CartaoCreditoNumeroExisteException("Já existe um cartão de crédito com esse número cadastrado.");
            }
            manager.persist(cartaoCreditoUnico);
            manager.flush();
        }
    }

    /**
     * ATENÇÃO: Retorna cartões dependentes!
     * @param user
     * @return
     */
    public List<CartaoCreditoUnico> buscaTodosCartoes(User user) {
        if (user.getConjugeUser() == null) {
            Query q = manager.createNamedQuery("buscaCartaoCreditoProprietario");
            q.setParameter("proprietario", user);
            List<CartaoCreditoUnico> toReturn = q.getResultList();
            List<CartaoCreditoUnico> toApend = new ArrayList<CartaoCreditoUnico>();
            for (CartaoCreditoUnico ccu : toReturn) {
                toApend.addAll(this.buscarCartoesDependentes(ccu));
            }
            toReturn.addAll(toApend);
            Collections.sort(toReturn, new CartaoCreditoUnicoComparator());
            return toReturn;
        } else {
            Query q = manager.createNamedQuery("buscaCartaoCreditoProprietarioConjuge");
            q.setParameter("proprietario", user);
            q.setParameter("userConjuge", user.getConjugeUser());
            List<CartaoCreditoUnico> toReturn = q.getResultList();
            List<CartaoCreditoUnico> toApend = new ArrayList<CartaoCreditoUnico>();
            for (CartaoCreditoUnico ccu : toReturn) {
                List<CartaoCreditoUnico> tempList = this.buscarCartoesDependentes(ccu);
                for (CartaoCreditoUnico ccuT : tempList) {
                    if (!toReturn.contains(ccuT) && !toApend.contains(ccuT)) {
                        toApend.add(ccuT);
                    }
                }
            }
            toReturn.addAll(toApend);
            Collections.sort(toReturn, new CartaoCreditoUnicoComparator());
            return toReturn;
        }
    }

    /**
     * Retorna o usuário proprietário do cartão passado, se o cartão for dependente, buscará o usuário titular.
     * @param cartaoCreditoUnico
     * @return
     */
    public User buscarUserCartao(CartaoCreditoUnico cartaoCreditoUnico) {
        cartaoCreditoUnico = this.manager.merge(cartaoCreditoUnico);
        User u = null;
        if (cartaoCreditoUnico.getCartaoCreditoTitular() != null) {
            u = cartaoCreditoUnico.getCartaoCreditoTitular().getUser();
        } else {
            u = cartaoCreditoUnico.getUser();
        }
        return u;
    }

    /**
     * ATENÇÃO: Não retorna cartões dependentes!
     * @param numeroCartaoCredito
     * @return
     */
    public CartaoCreditoUnico buscaCartaoPorNumero(String numeroCartaoCredito) {
        Query q = manager.createNamedQuery("buscaCartaoCreditoNumero");
        q.setParameter("numeroCartao", numeroCartaoCredito);
        try {
            return (CartaoCreditoUnico) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * ATENÇÃO: Não retorna cartões dependentes!
     * @param status
     * @param user
     * @see  buscarCartaoPorStatusTitular(boolean status, boolean dependentes, User user)
     * @return
     */
    public List<CartaoCreditoUnico> buscaCartaoPorStatus(boolean status, User user) {
        if (user.getConjugeUser() == null) {
            Query q = manager.createNamedQuery("buscaCartaoCreditoStatus");
            q.setParameter("statusCartao", status);
            q.setParameter("proprietario", user);
            return q.getResultList();
        } else {
            Query q = manager.createNamedQuery("buscaCartaoCreditoStatusConjuge");
            q.setParameter("statusCartao", status);
            q.setParameter("proprietario", user);
            q.setParameter("conjugeUser", user.getConjugeUser());
            return q.getResultList();
        }
    }

    /**
     * Retorna cartãoes que sejam titulares, se dependetes for false
     * Retorna cartãoes que sejam titulares ou depente, se dependetes for true
     * Os outros métodos foram criados antes dessa nova regra de negócio e
     * por isso receberam observação no javadoc.
     * @param status
     * @param user
     * @return
     */
    public List<CartaoCreditoUnico> buscarCartaoPorStatusTitular(boolean status, boolean dependentes, User user) {
        List<CartaoCreditoUnico> toReturn = buscaCartaoPorStatus(status, user);
        if (dependentes) {
            List<CartaoCreditoUnico> toApend = new ArrayList<CartaoCreditoUnico>();
            for (CartaoCreditoUnico ccu : toReturn) {
                toApend.addAll(buscarCartoesDependentes(ccu));
            }
            toReturn.addAll(toApend);
        }
        return null;
    }

    /**
     * Busca todos os cartões que são dependetes do cartão passado.
     * @param cartaoCreditoUnico
     * @return
     */
    public List<CartaoCreditoUnico> buscarCartoesDependentes(CartaoCreditoUnico cartaoCreditoUnico) {
        Query q = manager.createNamedQuery("buscarCartoesDependentes");
        q.setParameter("cartaoCreditoTitular", cartaoCreditoUnico);
        return q.getResultList();
    }

    /**
     * ATENÇÃO: Retorna cartões dependentes, já que é uma busca simples por ID.
     * @param id
     * @return
     */
    @Override
    public CartaoCreditoUnico buscarPorID(int id) {
        return this.manager.find(CartaoCreditoUnico.class, id);
    }

    @Override
    public List<ContaPagar> buscarContasPorCartaoCredito(CartaoCreditoUnico cartaoCreditoUnico, Date dataReferencia) {
        Date[] dates = UtilBeans.primeiroUltimoDia(dataReferencia);
        Query q = manager.createNamedQuery("buscarContasPorCartaoCredito");
        q.setParameter("cartaoCreditoUnico", cartaoCreditoUnico);
        q.setParameter("dataInit", dates[0]);
        q.setParameter("dataFim", dates[1]);
        return q.getResultList();
    }

    private boolean validaCartaoCredito(CartaoCreditoUnico cartaoCreditoUnico) {
        if (cartaoCreditoUnico == null) {
            return false;
        }
        if (!validaDiaMes(cartaoCreditoUnico.getDiaMesmoMes())) {
            return false;
        }
        if (!validaDiaMes(cartaoCreditoUnico.getDiaVencimento())) {
            return false;
        }
        if (StringUtils.isBlank(cartaoCreditoUnico.getEmpresaCartao())) {
            return false;
        }
        return true;
    }

    private boolean validaDiaMes(Integer dia) {
        if (dia == null || dia < 1 || dia > 31) {
            return false;
        }
        return true;
    }

    private boolean validaNumeroCartao(String numeroCartao) {
        if (StringUtils.isBlank(numeroCartao)) {
            return false;
        }
        return true;
    }
}
