/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.MovimentacaoFinanceiraFacade;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.superclass.MovimentacaoFinanceira;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.Map;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Usuário do Windows
 */
@Stateless
@Interceptors({LogTime.class})
public class MovimentacaoFinanceiraBean extends AbstractFacade<MovimentacaoFinanceira, Long>
        implements MovimentacaoFinanceiraFacade {

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
    public MovimentacaoFinanceiraBean() {
        super(MovimentacaoFinanceira.class);
    }

    @Override
    public MovimentacaoProcedimento buscarPorProcedimento(Procedimento procedimento) {
        Map<String, Object> parans = getMapParans();
        parans.put("procedimento", procedimento);
        MovimentacaoFinanceira m = pesqParam("MovimentacaoProcedimento.buscarPorProcedimento", parans);
        if (m == null) {
            return null;
        }
        return (MovimentacaoProcedimento) m;
    }
}
