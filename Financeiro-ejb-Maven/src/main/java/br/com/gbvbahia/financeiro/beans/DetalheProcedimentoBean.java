/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Bean para trabalhar com DetalheProcedimento Receita e Despesas.
 *
 * @author Guilherme
 * @since v.3 01/04/2012
 */
@Stateless
//@RolesAllowed({ "admin", "user" })
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
     * Construtor padrão que passa o tipo de classe para
     * AbstractFacade.
     */
    public DetalheProcedimentoBean() {
        super(DetalheProcedimento.class);
    }

    @Override
    public List<DetalheProcedimento> findAllDetalheReceita(
            final Usuario user, final Boolean ativo) {
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", user);
        //Se null esse true é ignorado
        parans.put("ativo", ativo == null ? true : ativo);
        //Se null esse garante trazer todos.
        parans.put("ativo2", ativo == null ? "todos" : "filtro");
        return listPesqParam("DetalheProcedimento.findAllReceita", parans);
    }

    @Override
    public List<DetalheProcedimento> findAllDetalheDespesa(
            final Usuario user, final Boolean ativo) {
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", user);
        //Se null esse true é ignorado
        parans.put("ativo", ativo == null ? true : ativo);
        //Se null esse garante trazer todos.
        parans.put("ativo2", ativo == null ? "todos" : "filtro");
        return listPesqParam("DetalheProcedimento.findAllDespesa", parans);
    }
}
