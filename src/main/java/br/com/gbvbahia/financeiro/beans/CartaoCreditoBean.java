/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Bean de entidade Cartão de Crédito.
 * @author Guilherme
 * @since 2012/04/13
 */
@Stateless
@RolesAllowed({ "admin", "user" })
public class CartaoCreditoBean extends AbstractFacade<CartaoCredito, Long>
        implements CartaoCreditoFacade {

    /**
     * Unidade de persistência <i>jdbc/money</i>.
     */
    @PersistenceContext(unitName = UtilBeans.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Construtor padrão que passa o tipo de classe para
     * AbstractFacade.
     */
    public CartaoCreditoBean() {
        super(CartaoCredito.class);
    }

    @Override
    public List<CartaoCredito> buscarCartoesAtivos(final Usuario usuario) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        return listPesqParam("CartaoCredito.Ativos", parans);
    }
}
