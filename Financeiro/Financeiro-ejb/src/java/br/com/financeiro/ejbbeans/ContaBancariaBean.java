/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans;

import br.com.financeiro.ejbbeans.interfaces.ContaBancariaLocal;
import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.idcompostos.ContaBancariaPK;
import br.com.financeiro.excecoes.ContaBancariaAgenciaException;
import br.com.financeiro.excecoes.ContaBancariaDelecaoException;
import br.com.financeiro.excecoes.ContaBancariaExistenteException;
import br.com.financeiro.excecoes.ContaBancariaNomeBancoException;
import br.com.financeiro.excecoes.ContaBancariaNumeroContaException;
import br.com.financeiro.excecoes.ContaBancariaObservacaoException;
import br.com.financeiro.excecoes.ContaBancariaProprietarioException;
import br.com.financeiro.excecoes.ContaBancariaSaldoException;
import br.com.financeiro.excecoes.ContaBancariaTipoContaException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Guilherme
 */
@Stateless
public class ContaBancariaBean implements ContaBancariaLocal {

    @PersistenceContext(unitName = "financeiro")
    private EntityManager entityManager;

    @Override
    public void apagarContaBancaria(ContaBancaria contaBancaria) throws ContaBancariaDelecaoException, ContaBancariaExistenteException {
        if (contaBancaria.getAgencia() == null || contaBancaria.getNumeroConta() == null) {
            throw new ContaBancariaDelecaoException("Você deve informar a agência e conta que deseja apagar");
        }
        ContaBancaria cb = entityManager.find(ContaBancaria.class, new ContaBancariaPK(contaBancaria.getAgencia(), contaBancaria.getNumeroConta()));
        if (cb == null) {
            throw new ContaBancariaExistenteException("A conta informada não foi encontrada!");
        }
        Query q = entityManager.createNamedQuery("deletaContasBancarias");
        q.setParameter("agencia", contaBancaria.getAgencia());
        q.setParameter("numeroConta", contaBancaria.getNumeroConta());
        q.executeUpdate();
    }

    @Override
    public void salvaContaBancaria(ContaBancaria contaBancaria) throws ContaBancariaProprietarioException,
            ContaBancariaNomeBancoException, ContaBancariaTipoContaException, ContaBancariaAgenciaException,
            ContaBancariaNumeroContaException, ContaBancariaSaldoException, ContaBancariaObservacaoException,
            ContaBancariaExistenteException {
        validaContaBancaria(contaBancaria);
        ContaBancaria cb = entityManager.find(ContaBancaria.class, new ContaBancariaPK(contaBancaria.getAgencia(), contaBancaria.getNumeroConta()));
        if (cb == null) {
            entityManager.persist(contaBancaria);
            entityManager.flush();
        } else {
            throw new ContaBancariaExistenteException("Já existe uma conta cadastrada para esta agência e conta corrente!");
        }
    }

    @Override
    public ContaBancaria buscaContabancaria(String agencia, String numeroConta) {
        return entityManager.find(ContaBancaria.class, new ContaBancariaPK(agencia, numeroConta));
    }

    @Override
    public List<ContaBancaria> contasProprietario(User user) {
        if (user.getConjugeUser() == null) {
            Query query = entityManager.createNamedQuery("buscaContasProprietario");
            query.setParameter("proprietario", user);
            List<ContaBancaria> toReturn = query.getResultList();
            return toReturn;
        } else {
            Query query = entityManager.createNamedQuery("buscaContasProprietarioConjuge");
            query.setParameter("proprietario", user);
            query.setParameter("conjugeUser", user.getConjugeUser());
            List<ContaBancaria> toReturn = query.getResultList();
            return toReturn;
        }
    }

    private void validaContaBancaria(ContaBancaria contaBancaria) throws ContaBancariaProprietarioException,
            ContaBancariaNomeBancoException, ContaBancariaTipoContaException, ContaBancariaAgenciaException,
            ContaBancariaNumeroContaException, ContaBancariaSaldoException, ContaBancariaObservacaoException {
        if (contaBancaria.getUser() == null) {
            throw new ContaBancariaProprietarioException("Proprietario deve ser informado!");
        }
        if (contaBancaria.getNomeBanco() == null) {
            throw new ContaBancariaNomeBancoException("O nome do banco deve ser informado!");
        }
        if (contaBancaria.getTipoConta() == null) {
            throw new ContaBancariaTipoContaException("O tipo da conta deve ser selecionado!");
        }
        if (contaBancaria.getAgencia() == null) {
            throw new ContaBancariaAgenciaException("A agência deve ser selecionado!");
        }
        if (contaBancaria.getNumeroConta() == null) {
            throw new ContaBancariaNumeroContaException("O número da conta deve ser informado!");
        }
        if (contaBancaria.getSaldo() == null) {
            throw new ContaBancariaSaldoException("O Saldo da conta deve ser informado!");
        }
        if (contaBancaria.getObservacao() != null && contaBancaria.getObservacao().length() > 255) {
            throw new ContaBancariaObservacaoException("A observação não é obrigatória, mas deve ter até 255 caracteres");
        }
    }
}
