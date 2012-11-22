/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.facades.ReportFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Usuário do Windows
 */
@Stateless
public class ReportBean implements ReportFacade {

    /**
     * Unidade de persistência <i>jdbc/money</i>.
     */
    @PersistenceContext(unitName = UtilBeans.PERSISTENCE_UNIT)
    private EntityManager em;

    @Override
    public Map<CartaoCredito, Double> acumuladoCartaoPeriodo(Date mesAno, Usuario usuario) {
        Date[] periodo = DateUtils.primeiroUltimoDia(mesAno);
        Query q = em.createNamedQuery("DespesaProcedimento.acumuladoCartaoPeriodo");
        q.setParameter("dataI", periodo[0]);
        q.setParameter("dataF", periodo[1]);
        q.setParameter("usuario", usuario);
        List<Object[]> valores = q.getResultList();
        Map<CartaoCredito, Double> toReturn = new HashMap<CartaoCredito, Double>();
        for (Object[] valor : valores) {
            toReturn.put((CartaoCredito) valor[0], ((BigDecimal) valor[1]).doubleValue());
        }
        return toReturn;
    }
}
