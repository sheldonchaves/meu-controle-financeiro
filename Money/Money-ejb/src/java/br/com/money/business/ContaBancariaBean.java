/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.enums.TipoConta;
import br.com.money.exceptions.ContaBancariaException;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.Usuario;
import br.com.money.utils.UtilBeans;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Guilherme
 */
@Stateless
public class ContaBancariaBean extends AbstractFacade<ContaBancaria, Long> implements ContaBancariaBeanLocal {

    @Override
    protected EntityManager getEntityManager() {
        return this.manager;
    }

    @Override
    protected ValidadorInterface getValidador() {
        return this.contaBancariaValidador;
    }

    public ContaBancariaBean() {
        super(ContaBancaria.class);
    }

    @EJB(beanName = "contaBancariaValidador")
    private ValidadorInterface contaBancariaValidador;
    @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;

    @Override
    public void salvarContaBancaria(ContaBancaria contaBancaria) {
        if (contaBancaria.getId() == null) {
            create(contaBancaria);
        } else {
            update(contaBancaria);
        }
    }
   
    @Override
    public ContaBancaria buscarContaBancariaPorNomeTipo(String nomeConta, TipoConta tipo) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("nomeConta", nomeConta);
        parans.put("tipoConta", tipo);
        return pesqParam("ContaBancariaBean.buscarContaBancariaPorNomeTipo", parans);
    }

    @Override
    public List<ContaBancaria> buscarContaBancariasPorUsuario(Usuario usuario) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("user", usuario);
        final List<ContaBancaria> resultList =
                listPesqParam("ContaBancariaBean.buscarConta"
                + "BancariasPorUsuario", parans);
        Collections.sort(resultList);
        return resultList;
    }

    @Override
    public void apagarContaBancaria(Long id) {
        ContaBancaria toDelete = find(id);
        if (toDelete != null) {
            if (toDelete.getMovimentacaoFinanceira().isEmpty() 
                    && toDelete.getMovimentacaoFinanceiraTransferida().isEmpty()) {
                remove(toDelete);
            } else {
                throw new ContaBancariaException("contaComMovimentacaoToDelete");
            }
        }
    }
    
    /**
     * Retorna as contas de um usuário com base no tipo solicitado.
     * @param ususario
     * @param tipo
     * @return 
     */
    @Override
    public List<ContaBancaria> buscarContaBancariasPorUsuarioTipo(Usuario ususario, TipoConta tipo){
        List<ContaBancaria> toReturn = this.buscarContaBancariasPorUsuario(ususario);
        for(int i = toReturn.size()-1; i >= 0; i--){
            if(!toReturn.get(i).getTipoConta().equals(tipo)){
                toReturn.remove(i);
            }
        }
        return toReturn;
    }

    @Override
    public List<ContaBancaria> buscarContaBancariasPorUsuarioTipoPaginado(
            final Usuario ususario, final TipoConta tipo,
            final String nomeConta, final int inicial, final int total) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("user", ususario);
        parans.put("tipoConta", tipo);
        parans.put("tipoConta2", tipo);
        parans.put("nomeConta",
                UtilBeans.acertaNomeParaLike(nomeConta, UtilBeans.LIKE_END));
        return listPesqParam("ContaBancariaBean.buscarContaBancarias"
                + "PorUsuarioTipoPaginado", parans, total - inicial, inicial);
    }

    @Override
    public int contarContaBancariasPorUsuarioTipo(
            final Usuario ususario, final TipoConta tipo,
            final String nomeConta) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("user", ususario);
        parans.put("tipoConta", tipo);
        parans.put("tipoConta2", tipo);
        parans.put("nomeConta",
                UtilBeans.acertaNomeParaLike(nomeConta, UtilBeans.LIKE_END));
        return pesqCount("ContaBancariaBean.contarContaBancariasPor"
                + "UsuarioTipo", parans).intValue();
    }
}
