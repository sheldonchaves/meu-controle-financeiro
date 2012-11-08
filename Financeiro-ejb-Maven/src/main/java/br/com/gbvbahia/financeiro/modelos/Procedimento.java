/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.ClassificacaoProcedimento;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.financeiro.utils.NumberUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 * Representa uma conta a pagar ou receber, devido a mistura de conta com
 * conta bancaria, alterei o nome para procedimento.
 *
 * @since v.3 08/04/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_procedimento")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo",
discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("RECEITA")
@NamedQueries({
    @NamedQuery(name = "Procedimento.TipoProcedimento",
    query = " SELECT p From Procedimento p "
    + "WHERE (p.tipoProcedimento = :tipoProcedimento) "
    + "AND (p.usuario = :usuario OR p.usuario.conjuge = :usuario)"),
    
    @NamedQuery(name = "Procedimento.buscarCartaoStatusUsrTipoProcedimento_1",
    query = " SELECT d From DespesaProcedimento d "
    + "WHERE (:cartao2 = 'todos' OR d.cartaoCredito = :cartao) "
    + "AND (:status2 = 'todos' OR d.statusPagamento = :status) "
    + "AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "
    + "AND (:tipoProcedimento2 = 'todos' OR d.tipoProcedimento = :tipoProcedimento) "),
    @NamedQuery(name = "Procedimento.buscarCartaoStatusUsrTipoProcedimento_2",
    query = " SELECT d From DespesaParceladaProcedimento d "
    + "WHERE (:cartao2 = 'todos' OR d.cartaoCredito = :cartao) "
    + "AND (:status2 = 'todos' OR d.statusPagamento = :status) "
    + "AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "
    + "AND (:tipoProcedimento2 = 'todos' OR d.tipoProcedimento = :tipoProcedimento) "),
    
    @NamedQuery(name = "Procedimento.buscarStatusUsrTipoProcedimento",
    query = " SELECT d From Procedimento d "
    + "WHERE (:status2 = 'todos' OR d.statusPagamento = :status) "
    + "AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "
    + "AND (:tipoProcedimento2 = 'todos' OR d.tipoProcedimento = :tipoProcedimento) ")
})
public class Procedimento
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
     * Valor estimado a pagar/receber do procedimento, este valor sempre
     * deverá ser informado. Caso não se tenha o valor real este será
     * considerado em calculos de estimativas.<br> Em contas Variaveis, em que
     * o valor real já existe, setar este igual ao real.
     */
    @NotNull
    @Column(name = "valor_estimado", nullable = false)
    @Digits(fraction = 2, integer = 12)
    private BigDecimal valorEstimado;
    /**
     * Valor real pago na conta, utilizado nas receitas fixas, em que
     * estimativas futuras são realizadas. Não é obrigatório nas
     * despesas/receitas fixas, nas variaveis sempre deverá ser informado.<br>
     * <b>PODE SER NULO</b>
     */
    @Column(name = "valor_real")
    @Digits(fraction = 2, integer = 12)
    private BigDecimal valorReal;
    /**
     * Representa o detalhe do gasto/receita.
     */
    @ManyToOne
    @NotNull
    @JoinColumn(name = "detalhe_procedimento", nullable = false)
    private DetalheProcedimento detalhe;
    /**
     * Para receitas/despesas sem ser por agentamento, cadastradas pelo
     * usuário, sempre será Variavel.<br> Quando o procedimento for criado
     * pelo agendador, será Fixo.
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
     * Se essa conta for criada por uma agenda, a mesma deverá ser cadastrada
     * para fins de atualização.
     */
    @ManyToOne
    @JoinColumn(name = "fk_agenda_procedimento_fixo")
    private AgendaProcedimentoFixo agenda;
    /**
     * Usuario responsavel.
     *
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id",
    referencedColumnName = "user_id",
    nullable = false)
    @NotNull
    private Usuario usuario;
    /**
     * Deve ser informado no construtor de quem implementa.<br> Define se o
     * Procedimento é uma receita, entra dinheiro ou uma despesa, saída de
     * dinheiro.
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "tipo_procedimento", nullable = false)
    private TipoProcedimento tipoProcedimento = TipoProcedimento.RECEITA_FINANCEIRA;

    /**
     * Padrão, nunca deve ser utilizado, lança RuntimeException.
     */
    public Procedimento() {
    }

    /**
     * Obrigatório informar o tipo de procedimento.<br> Retirada determina uma
     * DESPESA.<br> Deposito determina uma RECEITA.
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
                + ", tipoProcedimento=" + tipoProcedimento
                + ", dataVencimento=" + dataVencimento
                + ", valorEstimado=" + valorEstimado
                + ", valorReal=" + valorReal
                + ", detalhe=" + detalhe
                + ", classProcedimento=" + classificacaoProcedimento
                + ", statusPagamento=" + statusPagamento
                + ", observacao=" + observacao + '}';
    }

    /**
     * Para receitas/despesas sem ser por agentamento, cadastradas pelo
     * usuário, sempre será Variavel.<br> Quando o procedimento for criado
     * pelo agendador, será Fixo.
     *
     * @return Classificação correspondente.
     */
    public ClassificacaoProcedimento getClassificacaoProcedimento() {
        return classificacaoProcedimento;
    }

    /**
     * Para receitas/despesas sem ser por agentamento, cadastradas pelo
     * usuário, sempre será Variavel.<br> Quando o procedimento for criado
     * pelo agendador, será Fixo.
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
    public void setId(final Long idBd) {
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
     * Usuário proprietario.
     *
     * @return proprietario.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Usuario proprietario.
     *
     * @param user proprietario;
     */
    public void setUsuario(final Usuario user) {
        this.usuario = user;
    }

    /**
     * Valor estimado a pagar/receber do procedimento, este valor sempre
     * deverá ser informado. Caso não se tenha o valor real este será
     * considerado em calculos de estimativas.<br> Em contas Variaveis, em que
     * o valor real já existe, setar este igual ao real.
     *
     * @return Valor.
     */
    public BigDecimal getValorEstimado() {
        return valorEstimado;
    }

    /**
     * Valor estimado a pagar/receber do procedimento, este valor sempre
     * deverá ser informado. Caso não se tenha o valor real este será
     * considerado em calculos de estimativas.<br> Em contas Variaveis, em que
     * o valor real já existe, setar este igual ao real.
     *
     * @param valor Valor
     */
    public void setValorEstimado(final BigDecimal valor) {
        this.valorEstimado = valor;
    }

    /**
     * Valor real pago na conta, utilizado nas receitas fixas, em que
     * estimativas futuras são realizadas. Não é obrigatório nas
     * despesas/receitas fixas, nas variaveis sempre deverá ser informado.<br>
     * <b>PODE SER NULO</b>
     *
     * @return Valor <b>PODE SER NULO</b>
     */
    public BigDecimal getValorReal() {
        return valorReal;
    }

    /**
     * Valor real pago na conta, utilizado nas receitas fixas, em que
     * estimativas futuras são realizadas. Não é obrigatório nas
     * despesas/receitas fixas, nas variaveis sempre deverá ser informado.
     *
     * @param valor valor
     */
    public void setValorReal(final BigDecimal valor) {
        this.valorReal = valor;
    }

    /**
     * Define se o Procedimento é uma receita, entra dinheiro ou uma despesa,
     * saída de dinheiro.
     *
     * @return TipoProcedimento.DESPESA_FINANCEIRA para gasto e
     * TipoProcedimento.RECEITA_FINANCEIRA para receita.
     */
    public TipoProcedimento getTipoProcedimento() {
        return tipoProcedimento;
    }

    /**
     * Recupera a agenda de origem.
     *
     * @return Agenda ou null se não for de uma agenda.
     */
    public AgendaProcedimentoFixo getAgenda() {
        return agenda;
    }

    /**
     * Determina a agenda que criou.
     *
     * @param agendaProcedimento Agenda de origem.
     */
    public void setAgenda(final AgendaProcedimentoFixo agendaProcedimento) {
        this.agenda = agendaProcedimento;
    }

    @Override
    public String getLabel() {
        return StringUtils.substring(observacao, 0, CARACTERES_LABEL)
                + " | "
                + DateUtils.getDateToString(dataVencimento)
                + " | "
                + NumberUtils.currencyFormat(getValor().doubleValue())
                + " | "
                + tipoProcedimento.name();
    }

    /**
     * Retorna o valor real se o mesmo não for nulo, se for, retorna o valor
     * estimado.
     *
     * @return Real se houver ou estimado.
     */
    public BigDecimal getValor() {
        if (valorReal != null) {
            return valorReal;
        }
        return valorEstimado;
    }

    /**
     * Retorna o valor, real ou estimado e negativo se for despesa e positivo
     * se for receita.
     *
     * @see getValor()
     * @return Positivo de receita negativo se despesa.
     */
    public BigDecimal getValorProcedimento() {
        BigDecimal toReturn = getValor();
        if (this.tipoProcedimento.equals(TipoProcedimento.DESPESA_FINANCEIRA)) {
            return toReturn.multiply(new BigDecimal("-1"));
        } else {
            return toReturn;
        }
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
