/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.beans.commons.InterfaceFacade;
import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface de negócio local para ContaBancariaBean.
 *
 * @author Guilherme
 * @since v.3 31/03/2012
 */
@Local
public interface ContaBancariaFacade extends
        InterfaceFacade<ContaBancaria, Long> {

    /**
     * Realiza busca por todas as contas de um usuário ou seu conjuge,
     * com filtro no status.
     *
     * @param proprietario Obrigatório, pode ser prorpietário ou
     * conjuge do mesmo.
     * @param status Se nulo trará todas, true somente as ativas,
     * false somente as bloqueadas.
     * @return Lista com contas solicitadas pelos parâmtros, se não
     * houver será uma lista vazia não nula.
     */
    List<ContaBancaria> findAll(Usuario proprietario, Boolean status);

    /**
     * Realiza busca por todas as contas de um usuário ou seu conjuge,
     * com filtro no status e no tipo de conta.<br>Conta de
     * movimentação financeira. CORRENTE<br> Conta onde o dinheiro
     * deve ficar parado recebendo rendimentos. POUPANCA<br> Conta
     * fixa, onde o dinehrio não deve ou não pode ser resgatado a
     * qualquer hora, como um titulo de capitalização ou aponsetadoria
     * privada. INVESTIMENTO;<br>
     *
     * @param proprietario Obrigatório, pode ser prorpietário ou
     * conjuge do mesmo.
     * @param status Se nulo trará todas, true somente as ativas,
     * false somente as bloqueadas.
     * @param tipo Obrigatório TipoConta.CORRENTE, TipoConta.POUPANCA
     * ...
     * @return Lista com contas solicitadas pelos parâmtros, se não
     * houver será uma lista vazia não nula.
     */
    List<ContaBancaria> buscarTipoConta(final TipoConta tipo,
            final Usuario proprietario, final Boolean status);
}
