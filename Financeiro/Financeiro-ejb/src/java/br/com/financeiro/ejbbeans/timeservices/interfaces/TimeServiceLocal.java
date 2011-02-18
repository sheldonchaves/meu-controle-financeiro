/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.timeservices.interfaces;

import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface TimeServiceLocal {

    void iniciarVerificacaoContas();
    
}
