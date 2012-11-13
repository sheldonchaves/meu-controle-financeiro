/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business;

import br.com.gbvbahia.financeiro.beans.business.interfaces.TrabalharOperacaoBusiness;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.MovimentacaoFinanceiraFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.utils.UtilBeans;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Usuário do Windows
 */
@Stateless
public class TrabalharOperacaoBean implements TrabalharOperacaoBusiness {
    @EJB
    private ProcedimentoFacade procedimentoBean;
    @EJB
    private ContaBancariaFacade contaBancariaBean;
    @EJB
    private MovimentacaoFinanceiraFacade movimentacaoFinanceiraBean;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void fecharOperacao(Procedimento procedimento, ContaBancaria disponivel) throws NegocioException {
        UtilBeans.checkNull(procedimento, disponivel);
        if(StatusPagamento.PAGA.equals(procedimento.getStatusPagamento())){
            throw new NegocioException("MovimentacaoProcedimentoPago", new String[]{procedimento.getObservacao()});
        }
        MovimentacaoProcedimento mp = new MovimentacaoProcedimento();
        mp.setContaBancariaDebitada(disponivel);
        mp.setSaldoAnterior(disponivel.getSaldo());
        disponivel.setSaldo(disponivel.getSaldo().add(procedimento.getValorProcedimento()));
        mp.setSaldoPosterior(disponivel.getSaldo());
        mp.setProcedimento(procedimento);
        procedimento.setStatusPagamento(StatusPagamento.PAGA);
        contaBancariaBean.update(disponivel);
        procedimentoBean.update(procedimento);
        movimentacaoFinanceiraBean.create(mp);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void abrirOperacao(Procedimento procedimento) throws NegocioException {
        UtilBeans.checkNull(procedimento);
        if(StatusPagamento.NAO_PAGA.equals(procedimento.getStatusPagamento())){
            throw new NegocioException("MovimentacaoProcedimentoNaoPago", new String[]{procedimento.getObservacao()});
        }
        MovimentacaoProcedimento mp = movimentacaoFinanceiraBean.buscarPorProcedimento(procedimento);
        if(mp == null){
            procedimento.setStatusPagamento(StatusPagamento.NAO_PAGA);
            procedimentoBean.update(procedimento);
        }else {
            ContaBancaria disponivel = mp.getContaBancariaDebitada();
            disponivel.setSaldo(disponivel.getSaldo().add(mp.getSaldoAnterior().subtract(mp.getSaldoPosterior())));
            procedimento.setStatusPagamento(StatusPagamento.NAO_PAGA);
            procedimentoBean.update(procedimento);
            contaBancariaBean.update(disponivel);
            movimentacaoFinanceiraBean.remove(mp);
        }
    }
}
