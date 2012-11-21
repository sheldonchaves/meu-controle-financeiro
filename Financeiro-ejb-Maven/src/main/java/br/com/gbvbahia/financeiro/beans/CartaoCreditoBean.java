/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.StringBeanUtils;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Bean de entidade Cartão de Crédito.
 *
 * @author Guilherme
 * @since 2012/04/13
 */
@Stateless
@Interceptors({LogTime.class})
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
     * Construtor padrão que passa o tipo de classe para AbstractFacade.
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

    @Override
    public List<CartaoCredito> buscarCartoesUsuarioCartaoPaginado(final Usuario usr,
            final String cartao, int[] range) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usr);
        parans.put("cartao2", StringUtils.isBlank(cartao) ? "todos" : "filtro");
        parans.put("cartao", StringBeanUtils.acertaNomeParaLike(cartao, StringBeanUtils.LIKE_END));
        return listPesqParam("Cartao.selectUser", parans, range[1] - range[0], range[0]);
    
    }
    @Override
    public Long contarCartoesUsuarioCartao(final Usuario usr, final String cartao){
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usr);
        parans.put("cartao2", StringUtils.isBlank(cartao) ? "todos" : "filtro");
        parans.put("cartao", StringBeanUtils.acertaNomeParaLike(cartao, StringBeanUtils.LIKE_END));
        return pesqCount("Cartao.countUser", parans);
    }
}
