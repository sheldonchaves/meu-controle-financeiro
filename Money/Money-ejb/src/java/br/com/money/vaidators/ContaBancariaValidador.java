/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.vaidators;

import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.exceptions.ContaBancariaException;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Guilherme
 */
@Stateless(name = "contaBancariaValidador")
public class ContaBancariaValidador implements ValidadorInterface<ContaBancaria, ContaBancariaBeanLocal> {

    @Override
    public void validar(ContaBancaria entidade, ContaBancariaBeanLocal bean, Object object) throws ValidacaoException {
        if (entidade == null) {
            lancarException("contaBancariaNula", "Conta Bancaria");
        }
        if (StringUtils.isBlank(entidade.getNomeConta())) {
            lancarException("contaBancariaNomeContaNula", "Nome");
        }
        if (entidade.getNomeConta().length() > ContaBancaria.CARACTERES_NOME_CONTA) {
            lancarException("contaBancariaNomeContaLong", "Nome");
        }
        if (entidade.getTipoConta() == null) {
            lancarException("contaBancariaTipoContaNull", "Tipo de Conta");
        }
        if (entidade.getSaldo() == null) {
            lancarException("contaBancariaSaldoNull", "Saldo");
        }
        if (entidade.getUser() == null) {
            lancarException("contaBancariaUsuarioNull", "Saldo");
        }
        ContaBancaria cb = bean.buscarContaBancariaPorNomeTipo(entidade.getNomeConta(), entidade.getTipoConta());
        if (entidade == null && cb != null) {
            lancarException("contaBancariaExiste", "Conta Bancária");
        }
        if (cb != null && entidade.getId() != null && !entidade.getId().equals(cb.getId()) && (entidade.equals(cb))) {
            lancarException("contaBancariaExiste", "Conta Bancária");
        }
    }

    private void lancarException(String msg, String atributo) {
        ContaBancariaException due = new ContaBancariaException(msg, atributo);
        throw due;
    }
}
