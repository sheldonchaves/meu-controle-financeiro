/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.MovimentacaoFinanceiraFacade;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoProcedimento;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoTrasnferencia;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.dto.MinMaxDateDTO;
import br.com.gbvbahia.financeiro.modelos.superclass.MovimentacaoFinanceira;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    @Override
    public List<MovimentacaoTrasnferencia> buscarTransferencias(Usuario usr,
            Date data, ContaBancaria debitada, ContaBancaria creditada,
            int[] range) {
        Map<String, Object> parans = getMapParans();
        parans(parans, data, debitada, creditada, usr);
        List result = listPesqParam("MovimentacaoTrasnferencia.selectMovimentacao",
                parans, range[1] - range[0], range[0]);
        return result;
    }

    @Override
    public Long contarTransferencias(Usuario usr,
            Date data, ContaBancaria debitada, ContaBancaria creditada) {
        Map<String, Object> parans = getMapParans();
        parans(parans, data, debitada, creditada, usr);
        return pesqCount("MovimentacaoTrasnferencia.countMovimentacao", parans);
    }

    @Override
    public List<MovimentacaoFinanceira> pesquisarMovimentacaoPorPeriodo(final Date[] periodo,
            final ContaBancaria conta) {
        Map<String, Object> parans = getMapParans();
        parans.put("dataI", periodo[0]);
        parans.put("dataF", periodo[1]);
        parans.put("contaBancariaDebitada", conta);
        return listPesqParam("MovimentacaoFinanceira.pesquisarMovimentacaoPorPeriodo", parans);
    }

    @Override
    public MinMaxDateDTO buscarIntervalodDatas(final Usuario usr) {
        Query q = getEntityManager().createNamedQuery("MovimentacaoFinanceira.intervaloDatas");
        q.setParameter("usuario", usr);
        try {
            return (MinMaxDateDTO) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Isere os parametros no map.
     *
     * @param parans
     * @param data
     * @param debitada
     * @param creditada
     * @param usr
     */
    private void parans(Map<String, Object> parans, Date data, ContaBancaria debitada, ContaBancaria creditada, Usuario usr) {
        parans.put("usuario", usr);
        parans.put("dataMovimentacao", data);
        parans.put("contaBancariaDebitada", debitada);
        parans.put("contaBancariaTransferida", creditada);
        parans.put("dataMovimentacao2", data == null ? "todos" : "filtro");
        parans.put("contaBancariaDebitada2", debitada == null ? "todos" : "filtro");
        parans.put("contaBancariaTransferida2", creditada == null ? "todos" : "filtro");
    }
}
