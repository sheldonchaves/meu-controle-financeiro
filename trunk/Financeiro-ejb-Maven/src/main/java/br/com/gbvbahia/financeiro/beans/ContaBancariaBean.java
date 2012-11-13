/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.commons.AbstractFacade;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Bean de entidade para ContaBancaria.<br> <strong>SEGURANCA</strong>
 * <br> RolesAllowed pode ser aplicada na classe e/ou em metodos,
 * aplicada na classe tem valor em todos os métodos, menos os que a
 * sobrescreverem ou utilizar outra aotação, como:<br> PermitAll,
 * DenyAll e RolesAllowed.<br> Sendo que esta ultima pode ser menos ou
 * mais restringida:@RolesAllowed({"admins"})<br><br>
 *
 * @since v.3 31/03/2012
 * @author Guilherme
 */
@Stateless
@RolesAllowed({ "admin", "user" })
@Interceptors({LogTime.class})
public class ContaBancariaBean
        extends AbstractFacade<ContaBancaria, Long>
        implements ContaBancariaFacade {

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
    public ContaBancariaBean() {
        super(ContaBancaria.class);
    }

    @Override
    public List<ContaBancaria> findAll(final Usuario proprietario,
            final Boolean status) {
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", proprietario);
        //Se null esse true é ignorado
        parans.put("status", status == null ? true : status);
        //Se null esse garante trazer todos.
        parans.put("status2", status == null ? "todos" : "filtro");
        return listPesqParam("ContaBancaria.findAll", parans);
    }

    @Override
    public List<ContaBancaria> buscarTipoConta(final TipoConta tipo,
            final Usuario proprietario, final Boolean status) {
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", proprietario);
        parans.put("tipoConta", tipo);
        //Se null esse true é ignorado
        parans.put("status", status == null ? true : status);
        //Se null esse garante trazer todos.
        parans.put("status2", status == null ? "todos" : "filtro");
        return listPesqParam("ContaBancaria.findTipoConta", parans);
    }
    
    @Override
    public List<ContaBancaria> buscarContasUsuarioPaginado(Usuario usr, int[] range){
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", usr);
        return listPesqParam("ContaBancaria.selecUser", parans, range[1] - range[0], range[0]);
    }
    
    @Override
    public Long contarContasUsuario(Usuario usr){
        Map<String, Object> parans = getMapParans();
        parans.put("usuario", usr);
        return pesqCount("ContaBancaria.countUser", parans);
    }
}
