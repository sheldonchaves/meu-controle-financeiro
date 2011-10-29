/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.enums.TipoConta;
import br.com.money.modelos.ContaBancaria;
import br.com.money.vaidators.interfaces.ValidadorInterface;
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
public class ContaBancariaBean implements ContaBancariaBeanLocal {

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
}
