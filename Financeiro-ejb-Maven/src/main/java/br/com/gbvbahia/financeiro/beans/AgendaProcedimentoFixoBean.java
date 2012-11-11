/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.constantes.Periodo;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.StringBeanUtils;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * Bean de entidade a AgendaProcedimentoFixo.
 *
 * @author Guilherme
 * @since 01/04/2012
 */
@Stateless
@RolesAllowed({"admin", "user"})
@Interceptors({LogTime.class})
public class AgendaProcedimentoFixoBean
        extends AbstractFacade<AgendaProcedimentoFixo, Long>
        implements AgendaProcedimentoFixoFacade {

    /**
     * Construtor padrão que passa o tipo de classe para AbstractFacade.
     */
    public AgendaProcedimentoFixoBean() {
        super(AgendaProcedimentoFixo.class);
    }
    /**
     * Unidade de persistência <i>jdbc/money</i>.
     */
    @PersistenceContext(unitName = UtilBeans.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @Interceptors({LogTime.class})
    public Date buscarUltimaData(final AgendaProcedimentoFixo agenda) {
        UtilBeans.checkNull(agenda);
        Query q = em.createNamedQuery("AgendaProcedimentoFixo.UltimaData");
        q.setParameter("agenda", agenda);
        return (Date) q.getSingleResult();
    }

    @Override
    @Interceptors({LogTime.class})
    public Long countarAgendaPorUserDetalheObservacaoTipo(final Usuario user,
            final DetalheProcedimento detalhe, final String observacao,
            final TipoProcedimento tipo) {
        Map<String, Object> parans = getMapParans();
        populateParans(parans, user, detalhe, observacao,tipo);
        return pesqCount("AgendaProcedimentoFixo.countDetalhePeriodoTipo", parans);
    }

    @Override
    @Interceptors({LogTime.class})
    public List<AgendaProcedimentoFixo> buscarAgendaPorUserDetalheObservacaoTipoPaginado(
            final Usuario user, final DetalheProcedimento detalhe,
            final String observacao, final TipoProcedimento tipo,
            final int[] range) {
        Map<String, Object> parans = getMapParans();
        populateParans(parans, user, detalhe, observacao, tipo);
        return listPesqParam("AgendaProcedimentoFixo.selectDetalhePeriodoTipo",
                parans, range[1] - range[0], range[0]);
    }

    private void populateParans(Map<String, Object> parans, final Usuario user,
            final DetalheProcedimento detalhe,
            final String observacao,
            final TipoProcedimento tipo) {
        parans.put("usuario", user);
        parans.put("detalhe", detalhe);
        parans.put("detalhe2", detalhe == null ? "todos" : "filtro");
        parans.put("observacao2", StringUtils.isBlank(observacao) ? "todos" : "filtro");
        parans.put("observacao", StringBeanUtils.acertaNomeParaLike(observacao, StringBeanUtils.LIKE_END));
        parans.put("tipo", tipo);
        parans.put("tipo2", tipo == null ? "todos" : "filtro");
    }
}
