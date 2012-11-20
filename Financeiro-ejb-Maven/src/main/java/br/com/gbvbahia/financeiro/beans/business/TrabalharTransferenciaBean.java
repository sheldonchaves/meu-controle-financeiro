/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business;

import br.com.gbvbahia.financeiro.beans.business.interfaces.TrabalharTransferenciaBusiness;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.MovimentacaoFinanceiraFacade;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoTrasnferencia;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import java.math.BigDecimal;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Guilherme
 */
@Stateless
@RolesAllowed({"admin", "user","SYSTEM"})
public class TrabalharTransferenciaBean implements TrabalharTransferenciaBusiness {

    @EJB
    private ContaBancariaFacade contaBancariaBean;
    @EJB
    private MovimentacaoFinanceiraFacade movimentacaoFinanceiraBean;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transferirEntreDisponiveis(final ContaBancaria debitada,
            final ContaBancaria creditada, final BigDecimal valor)
            throws NegocioException {
        UtilBeans.checkNull(debitada, creditada, valor);
        if(creditada.equals(debitada)){
            throw new NegocioException("ContaDestinoIgualChegada");
        }
        MovimentacaoTrasnferencia mt = new MovimentacaoTrasnferencia();
        //Disponivel de origem
        mt.setContaBancariaDebitada(debitada);
        mt.setSaldoAnterior(debitada.getSaldo());
        debitada.setSaldo(debitada.getSaldo().subtract(valor));
        mt.setSaldoPosterior(debitada.getSaldo());
        //Disponivel de destino
        mt.setContaBancariaTransferida(creditada);
        mt.setSaldoTransferidaAnterior(creditada.getSaldo());
        creditada.setSaldo(creditada.getSaldo().add(valor));
        mt.setSaldoTransferidaPosterior(creditada.getSaldo());
        //Salvar
        contaBancariaBean.update(debitada);
        contaBancariaBean.update(creditada);
        movimentacaoFinanceiraBean.create(mt);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void desfazerTransferencia(final MovimentacaoTrasnferencia mt)
            throws NegocioException {
        UtilBeans.checkNull(mt);
        ContaBancaria creditada = mt.getContaBancariaTransferida();
        ContaBancaria debitada = mt.getContaBancariaDebitada();
        BigDecimal valor = mt.getSaldoAnterior().subtract(mt.getSaldoPosterior());
        debitada.setSaldo(debitada.getSaldo().add(valor));
        creditada.setSaldo(creditada.getSaldo().subtract(valor));
        contaBancariaBean.update(debitada);
        contaBancariaBean.update(creditada);
        movimentacaoFinanceiraBean.remove(mt);
    }
}
