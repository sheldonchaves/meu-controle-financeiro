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
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Guilherme
 */
@Stateless
public class ContaBancariaBean extends AbstractFacade<ContaBancaria> implements ContaBancariaBeanLocal {

    @Override
    protected EntityManager getEntityManager() {
        return this.manager;
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
        contaBancariaValidador.validar(contaBancaria, this, null);
        if (contaBancaria.getId() == null) {
            manager.persist(contaBancaria);
        } else {
            manager.merge(contaBancaria);
        }
        manager.flush();
    }

    @Override
    public ContaBancaria buscarContaBancariaPorId(long id) {
        return manager.find(ContaBancaria.class, id);
    }
    
    @Override
    public ContaBancaria buscarContaBancariaPorNomeTipo(String nomeConta, TipoConta tipo) {
        Query q = manager.createNamedQuery("ContaBancariaBean.buscarContaBancariaPorNomeTipo");
        q.setParameter("nomeConta", nomeConta);
        q.setParameter("tipoConta", tipo);
        try {
            return (ContaBancaria) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<ContaBancaria> buscarContaBancariasPorUsuario(Usuario usuario) {
        Query q = manager.createNamedQuery("ContaBancariaBean.buscarContaBancariasPorUsuario");
        q.setParameter("user", usuario);
        final List<ContaBancaria> resultList = q.getResultList();
        Collections.sort(resultList);
        return resultList;
    }

    @Override
    public void apagarContaBancaria(Long id) {
        ContaBancaria toDelete = manager.find(ContaBancaria.class, id);
        if (toDelete != null) {
            if (toDelete.getMovimentacaoFinanceira().isEmpty() && toDelete.getMovimentacaoFinanceiraTransferida().isEmpty()) {
                manager.remove(toDelete);
                manager.flush();
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
    public List<ContaBancaria> findRange(int[] range, Usuario usuarioProprietario) {
        Query q = manager.createNamedQuery("ContaBancariaBean.buscarContaBancariasPorUsuario");
        q.setParameter("user", usuarioProprietario);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        final List<ContaBancaria> resultList = q.getResultList();
        return resultList;
    }
    
        

}
