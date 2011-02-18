/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans.interfaces;

import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.User;
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
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface ContaBancariaLocal {

    void salvaContaBancaria(ContaBancaria contaBancaria) throws ContaBancariaProprietarioException,
            ContaBancariaNomeBancoException, ContaBancariaTipoContaException, ContaBancariaAgenciaException,
            ContaBancariaNumeroContaException, ContaBancariaSaldoException, ContaBancariaObservacaoException, ContaBancariaExistenteException;

    ContaBancaria buscaContabancaria(String agencia, String numeroConta);

    List<ContaBancaria> contasProprietario(User user);

    void apagarContaBancaria(ContaBancaria contaBancaria) throws ContaBancariaDelecaoException,ContaBancariaExistenteException;
}
