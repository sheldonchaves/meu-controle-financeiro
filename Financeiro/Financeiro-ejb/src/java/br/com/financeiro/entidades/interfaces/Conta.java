/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades.interfaces;

import br.com.financeiro.entidades.MovimentacaoFinanceira;
import br.com.financeiro.entidades.enums.TipoMovimentacao;
import java.util.Date;

/**
 *
 * @author gbvbahia
 */
public interface Conta {

    /**
     * Data de pgto ou receita
     * @return
     */
    public Date getContaDataConta();//

    /**
     * O grupo da conta
     * @return
     */
    public String getContaTipo();//

    /**
     * Paga ou Não Paga
     * Recebida ou Não Recebida
     * @return
     */
    public String getContaStatus();//

    /**
     * O valor da conta ou receita
     * @return
     */
    public Double getContaValor();//
    public void setContaValor(Double valor);

    /**
     * A parcela atual da conta ou receita
     * @return
     */
    public Integer getContaParcelaAtual();//

    /**
     * O total de parcelas da conta
     * @return
     */
    public Integer getContaParcelaTotal();//

    /**
     * A observação descrita
     * @return
     */
    public String getContaObservacao();//

    /**
     * A forma como a conta será paga ou recebida!
     * @return
     */
    public String getContaForma();//

    /**
     * Retorna a movimentacao financeira associada a conta, se não houver retorna null
     * @return
     */
    public MovimentacaoFinanceira getContaMovimentacaoFinanceira();
    public void setContaMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira);

    /**
     * Retorna uma EnumTipoMovimentacao
     * Retirada para Pagamentos
     * Deposito para receita
     * @return
     */
    public TipoMovimentacao getContaTipoMovimentacao();//
}
