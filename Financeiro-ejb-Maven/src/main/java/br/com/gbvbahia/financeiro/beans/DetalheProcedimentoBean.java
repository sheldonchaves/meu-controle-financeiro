/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
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
 * Bean para trabalhar com DetalheProcedimento Receita e Despesas.
 *
 * @author Guilherme
 * @since v.3 01/04/2012
 */
@Stateless
@Interceptors({LogTime.class})
public class DetalheProcedimentoBean
        extends AbstractFacade<DetalheProcedimento, Long>
        implements DetalheProcedimentoFacade {

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
    public DetalheProcedimentoBean() {
        super(DetalheProcedimento.class);
    }

    @Override
    @Interceptors({LogTime.class})
    public List<DetalheProcedimento> findAllDetalhe(
            final Usuario user, final Boolean ativo, TipoProcedimento tipo) {
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", user);
        //Se null esse true é ignorado
        parans.put("ativo", ativo == null ? true : ativo);
        //Se null esse garante trazer todos.
        parans.put("ativo2", ativo == null ? "todos" : "filtro");
        //Se null esse true é ignorado
        parans.put("tipo", tipo);
        //Se null esse garante trazer todos.
        parans.put("tipo2", tipo == null ? "todos" : "filtro");
        return listPesqParam("DetalheProcedimento.findAllProcedimento", parans);
    }

    @Override
    @Interceptors({LogTime.class})
    public Long countarDetalhePorUsuario(final Usuario user, String detalhe) {
        Map<String, Object> parans = getMapParans();
        populateParans( parans, user, detalhe);
        return pesqCount("DetalheProcedimento.countUser", parans);
    }

    @Override
    @Interceptors({LogTime.class})
    public List<DetalheProcedimento> buscarDetalhePorUserPaginado(final Usuario user, String detalhe, int[] range) {
        Map<String, Object> parans = getMapParans();
        populateParans( parans, user, detalhe);
        return listPesqParam("DetalheProcedimento.selectUser", parans, range[1] - range[0], range[0]);
    }

    private void populateParans(Map<String, Object> parans, final Usuario user, String detalhe) {
        parans.put("usuario", user);
        parans.put("detalhe2", StringUtils.isBlank(detalhe) ? "todos" : "filtro");
        parans.put("detalhe", StringBeanUtils.acertaNomeParaLike(detalhe, StringBeanUtils.LIKE_END));
        parans.put("usuario", user);
    }
}
