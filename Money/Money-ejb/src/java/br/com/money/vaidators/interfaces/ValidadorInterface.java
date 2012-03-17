/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.money.vaidators.interfaces;

import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.commons.EntityInterface;
import javax.ejb.Local;


/**
 *  Interface que permite validar as entidades nos ejb´s.
 *  No generics a classe que a implementa, que a classe que fará a validação,
 * deve informar qual Entidade ela valida "E" e qual bean ela deve receber
 * para auxiliar na validação. Como por exemplo, para checar valores no
 * banco antes de tentar atualizar ou persistir uma entidade.
 * @author gbvbahia
 */
@Local
public interface ValidadorInterface<E extends EntityInterface, Bean> {

    /**
     * Recebe a entidade a ser validada, um bean
     * que irá auxiliar nessa validação e um terceiro objeto qualquer que necessite ser validado.
     * @param entidade
     * @param bean
     * @param object
     * @throws ValidacaoException
     */
    public void validar(E entidade, Bean bean, Object object) throws ValidacaoException;
}
