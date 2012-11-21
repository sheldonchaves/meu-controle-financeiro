/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.interfaces;

import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface AvisoVencimentosBusiness {
    /**
     * Envia aviso de vencimento de procedimentos aos usuários.
     */
    public void iniciarAvisoVencimento();
}
