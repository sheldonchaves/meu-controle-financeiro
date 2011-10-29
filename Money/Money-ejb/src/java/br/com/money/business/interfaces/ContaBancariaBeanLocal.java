/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.enums.TipoConta;
import br.com.money.modelos.ContaBancaria;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface ContaBancariaBeanLocal {

    void salvarContaBancaria(ContaBancaria contaBancaria);

    ContaBancaria buscarContaBancariaPorNomeTipo(String nome, TipoConta tipo);
    
}
