/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.interfaces;

import javax.ejb.Local;

/**
 *
 * @author Usuário do Windows
 */
@Local
public interface AcompanhamentoCartaoCreditoBusiness {
    /**
     * Envia notificação de acompanhamento do cartão de crédito.
     */
    void avisarCartaoCredito();
}
