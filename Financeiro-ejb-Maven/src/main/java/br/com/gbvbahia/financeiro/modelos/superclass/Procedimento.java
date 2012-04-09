/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos.superclass;

import br.com.gbvbahia.financeiro.constantes.ClassificacaoProcedimento;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.financeiro.utils.I18N;
import br.com.gbvbahia.financeiro.utils.NumberUtils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 * Representa uma conta a pagar ou receber, devido a mistura de conta
 * com conta bancaria, alterei o nome para procedimento.
 *
 * @since v.3 08/04/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_procedimento")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Procedimento
        implements EntityInterface<Procedimento>, Serializable {

    /**
     * ID único no banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * Data de vencimento do procedimento.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "data_vencimento", nullable = false)
    @NotNull
    private Date dataVencimento;
    /**
     * Valor estimado a pagar/receber do procedimento, este valor
     * sempre deverá ser informado. Caso não se tenha o valor real
     * este será considerado em calculos de estimativas.<br> Em contas
     * Variaveis, em que o valor real já existe, setar este igual ao
     * real.
     */
    @NotNull
    @Column(name = "valor_estimado", nullable = false)
    private Double valorEstimado;
    /**
     * Valor real pago na conta, utilizado nas receitas fixas, em que
     * estimativas futuras são realizadas. Não é obrigatório nas
     * despesas/receitas fixas, nas variaveis sempre deverá ser
     * informado.<br> <b>PODE SER NULO</b>
     */
    @Column(name = "valor_real")
    private Double valorReal;
    /**
     * Representa o detalhe do gasto/receita.
     */
    @ManyToOne
    @JoinColumn(name = "detalhe_procedimento")
    private DetalheProcedimento detalhe;
    /**
     * Para receitas/despesas sem ser por agentamento, cadastradas
     * pelo usuário, sempre será Variavel.<br> Quando o procedimento
     * for criado pelo agendador, será Fixo.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "classe_procedimento", nullable = false)
    @NotNull
    private ClassificacaoProcedimento classificacaoProcedimento =
            ClassificacaoProcedimento.VARIAVEL;
    /**
     * Define como saber se está recebida/paga.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false)
    @NotNull
    private StatusPagamento statusPagamento = StatusPagamento.NAO_PAGA;
    /**
     * Qualquer informação relativa ao pagamento.
     */
    @NotNull
    @Size(max = 150, min = 5)
    @Column(name = "observacao", nullable = false, length = 150)
    private String observacao;
    /**
     * Deve ser informado no construtor de quem implementa.<br> Define
     * se o Procedimento é uma receita, entra dinheiro ou uma despesa,
     * saída de dinheiro.
     */
    @Transient
    private final TipoProcedimento tipoProcedimento;

    /**
     * Padrão, nunca deve ser utilizado, lança RuntimeException.
     */
    public Procedimento() {
        throw new IllegalArgumentException(
                I18N.getMsg("ProcedimentoConstrutorErro"));
    }

    /**
     * Obrigatório informar o tipo de procedimento.<br> Retirada
     * determina uma DESPESA.<br> Deposito determina uma RECEITA.
     *
     * @param tipoEnum Tipo de Procedimento.
     */
    public Procedimento(final TipoProcedimento tipoEnum) {
        this.tipoProcedimento = tipoEnum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Procedimento other = (Procedimento) obj;
        if (this.id != other.id
                && (this.id == null
                || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Procedimento{" + "id=" + id
                + ", dataVencimento=" + dataVencimento
                + ", valorEstimado=" + valorEstimado
                + ", valorReal=" + valorReal
                + ", detalhe=" + detalhe
                + ", classProcedimento=" + classificacaoProcedimento
                + ", statusPagamento=" + statusPagamento
                + ", observacao=" + observacao + '}';
    }

    /**
     * Para receitas/despesas sem ser por agentamento, cadastradas
     * pelo usuário, sempre será Variavel.<br> Quando o procedimento
     * for criado pelo agendador, será Fixo.
     *
     * @return Classificação correspondente.
     */
    public ClassificacaoProcedimento getClassificacaoProcedimento() {
        return classificacaoProcedimento;
    }

    /**
     * Para receitas/despesas sem ser por agentamento, cadastradas
     * pelo usuário, sempre será Variavel.<br> Quando o procedimento
     * for criado pelo agendador, será Fixo.
     *
     * @param classProcedimento Classificação correspondente.
     */
    public void setClassificacaoProcedimento(
            final ClassificacaoProcedimento classProcedimento) {
        this.classificacaoProcedimento = classProcedimento;
    }

    /**
     * Data de vencimento do procedimento.
     *
     * @return java.util.Date
     */
    public Date getDataVencimento() {
        return dataVencimento;
    }

    /**
     * Data de vencimento do procedimento.
     *
     * @param dataVen data
     */
    public void setDataVencimento(final Date dataVen) {
        this.dataVencimento = dataVen;
    }

    /**
     * Representa o detalhe do gasto/receita.
     *
     * @return Subclasse de DetalheProcedimento
     */
    public DetalheProcedimento getDetalhe() {
        return detalhe;
    }

    /**
     * Representa o detalhe do gasto/receita.
     *
     * @param det SubClasse de DetalheProcedimento.
     */
    public void setDetalhe(final DetalheProcedimento det) {
        this.detalhe = det;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * ID banco de dados.
     *
     * @param idBd ID banco.
     */
    public void setId(Long idBd) {
        this.id = idBd;
    }

    /**
     * Qualquer informação relativa ao pagamento.
     *
     * @return String.
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * Qualquer informação relativa ao pagamento.
     *
     * @param obs String
     */
    public void setObservacao(final String obs) {
        this.observacao = obs;
    }

    /**
     * Define como saber se está recebida/paga.
     *
     * @return StatusPagamento
     */
    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    /**
     * Define como saber se está recebida/paga.
     *
     * @param status StatusPagamento
     */
    public void setStatusPagamento(final StatusPagamento status) {
        this.statusPagamento = status;
    }

    /**
     * Valor estimado a pagar/receber do procedimento, este valor
     * sempre deverá ser informado. Caso não se tenha o valor real
     * este será considerado em calculos de estimativas.<br> Em contas
     * Variaveis, em que o valor real já existe, setar este igual ao
     * real.
     *
     * @return Valor.
     */
    public Double getValorEstimado() {
        return valorEstimado;
    }

    /**
     * Valor estimado a pagar/receber do procedimento, este valor
     * sempre deverá ser informado. Caso não se tenha o valor real
     * este será considerado em calculos de estimativas.<br> Em contas
     * Variaveis, em que o valor real já existe, setar este igual ao
     * real.
     *
     * @param valor Valor
     */
    public void setValorEstimado(final Double valor) {
        this.valorEstimado = valor;
    }

    /**
     * Valor real pago na conta, utilizado nas receitas fixas, em que
     * estimativas futuras são realizadas. Não é obrigatório nas
     * despesas/receitas fixas, nas variaveis sempre deverá ser
     * informado.<br> <b>PODE SER NULO</b>
     *
     * @return Valor <b>PODE SER NULO</b>
     */
    public Double getValorReal() {
        return valorReal;
    }

    /**
     * Valor real pago na conta, utilizado nas receitas fixas, em que
     * estimativas futuras são realizadas. Não é obrigatório nas
     * despesas/receitas fixas, nas variaveis sempre deverá ser
     * informado.
     *
     * @param valor valor
     */
    public void setValorReal(final Double valor) {
        this.valorReal = valor;
    }

    /**
     * Define se o Procedimento é uma receita, entra dinheiro ou uma
     * despesa, saída de dinheiro.
     *
     * @return TipoProcedimento.DESPESA_FINANCEIRA para gasto e
     * TipoProcedimento.RECEITA_FINANCEIRA para receita.
     */
    public TipoProcedimento getTipoProcedimento() {
        return tipoProcedimento;
    }

    @Override
    public String getLabel() {
        return StringUtils.substring(observacao, 0, CARACTERES_LABEL)
                + " | "
                + DateUtils.getDateToString(dataVencimento)
                + " | "
                + NumberUtils.currencyFormat(getValor())
                + " | "
                + tipoProcedimento.name();
    }

    /**
     * Retorna o valor real se o mesmo não for nulo, se for, retorna o
     * valor estimado.
     *
     * @return Real se houver ou estimado.
     */
    public Double getValor() {
        if (valorReal != null) {
            return valorReal;
        }
        return valorEstimado;
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(final Procedimento o) {
        int i = 0;
        if (i == 0) {
            i = this.dataVencimento.compareTo(o.dataVencimento);
        }
        if (i == 0) {
            i = this.valorEstimado.compareTo(o.valorEstimado);
        }
        return i;
    }
}
