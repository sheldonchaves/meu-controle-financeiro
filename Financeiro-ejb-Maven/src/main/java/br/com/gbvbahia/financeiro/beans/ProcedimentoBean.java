/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.Procedimento;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Bean de entidade para Procedimento e todas suas subclasses.
 *
 * @since v.3 08/04/2012
 * @author Guilherme
 */
@Stateless
public class ProcedimentoBean
        extends AbstractFacade<Procedimento, Long>
        implements ProcedimentoFacade {

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
    public ProcedimentoBean() {
        super(Procedimento.class);
    }

    @Override
    public List<DespesaProcedimento> buscarDespesaProcedimento(
            final CartaoCredito cartao, final StatusPagamento status,
            final Usuario usuario) {
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", usuario);
        //Se null StatusPagamento.NAO_PAGA é ignorado
        parans.put("status", status == null
                ? StatusPagamento.NAO_PAGA : status);
        //Se null todos = todos e retorna tudo.
        parans.put("status2", status == null ? "todos" : "filtro");
        parans.put("cartao", cartao);
        List<DespesaProcedimento> toReturn =
                new ArrayList<DespesaProcedimento>();
        final List<Procedimento> despesas =
                listPesqParam("Despesa.CartaoStatusUsuario", parans);
        for (Procedimento p : despesas) {
            toReturn.add((DespesaProcedimento) p);
        }
        return toReturn;
    }
}
