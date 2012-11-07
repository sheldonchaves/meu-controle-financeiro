/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.modelos.superclass.MovimentacaoFinanceira;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import javax.persistence.*;

/**
 * Representa uma movimentação financeira entre uma conta e um
 * procedimento, uma despesa paga ou receita recebida.
 *
 * @since v.3 03/06/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_movimentacao_financeira_procedimento")
@DiscriminatorValue("PROCEDIMENTO")
public class MovimentacaoProcedimento extends MovimentacaoFinanceira {

    /**
     * Representa o procedimento, receita/despesa/despesa parcelada ao qual
     * essa movimentação foi realizada. <br> Cascade MERGE, significa que
     * quando a movimentação for atualizada as alterações em procedimento
     * serão confirmadas no banco, no procedimento relacionado.
     */
    @OneToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "fk_procedimento_id", referencedColumnName = "id")
    private Procedimento procedimento;

    @Override
    public String getLabel() {
        return procedimento.getLabel()
                + " - "
                + DateUtils.getDateToString(getDataMovimentacao());
    }

    /**
     * Procedimento que foi pago ou recebido na movimentação.
     *
     * @return Procedimento.
     */
    public Procedimento getProcedimento() {
        return procedimento;
    }

    /**
     * Procedimento que foi pago ou recebido na movimentação.
     *
     * @param vProcedimento O procedimento a ser movimentado.
     */
    public void setProcedimento(final Procedimento vProcedimento) {
        this.procedimento = vProcedimento;
    }
}
