/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.ClassificacaoProcedimento;
import br.com.gbvbahia.financeiro.constantes.DetalheTipoProcedimento;
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
    @NamedQuery(name = "Procedimento.buscarStatusUsrTipoProcedimento",
    query = " SELECT d From Procedimento d "
    + "WHERE (:status2 = 'todos' OR d.statusPagamento = :status) "
    + "AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "
    + "AND (:tipoProcedimento2 = 'todos' OR d.tipoProcedimento = :tipoProcedimento) "),
    @NamedQuery(name = "Procedimento.removerProcedimentoAgenda",
    query = " Delete From Procedimento p "
    + " WHERE p.agenda = :agenda "
    + " AND p.statusPagamento = :status "),
    @NamedQuery(name = "Procedimento.countProcedimento",
    query = " SELECT count(p) From Procedimento p "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND (:detalheProcedimento2 = 'todos' OR p.detalheProcedimento = :detalheProcedimento) "
    + " AND (:statusPagamento2 = 'todos' OR p.statusPagamento = :statusPagamento)"
    + " AND (:observacao2 = 'todos' OR p.observacao LIKE :observacao)"
    + " AND (:dataMovimentacao2 = 'todos' OR p.dataMovimentacao = :dataMovimentacao)"),
    @NamedQuery(name = "Procedimento.selectProcedimento",
    query = " SELECT distinct p From Procedimento p "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND (:detalheProcedimento2 = 'todos' OR p.detalheProcedimento = :detalheProcedimento) "
    + " AND (:statusPagamento2 = 'todos' OR p.statusPagamento = :statusPagamento) "
    + " AND (:observacao2 = 'todos' OR p.observacao LIKE :observacao)"
    + " AND (:dataMovimentacao2 = 'todos' OR p.dataMovimentacao = :dataMovimentacao)"
    + " ORDER BY p.dataMovimentacao, p.valorReal DESC, p.valorEstimado DESC"),
    @NamedQuery(name = "Procedimento.countProcedimentoSemCartao",
    query = " SELECT count(p) From Procedimento p "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND (:detalheProcedimento2 = 'todos' OR p.detalheProcedimento = :detalheProcedimento) "
    + " AND (:statusPagamento2 = 'todos' OR p.statusPagamento = :statusPagamento) "
    + " AND (:observacao2 = 'todos' OR p.observacao LIKE :observacao) "
    + " AND (:dataMovimentacao2 = 'todos' OR p.dataMovimentacao = :dataMovimentacao) "
    + " AND p.id NOT IN (Select d.id From DespesaProcedimento d Join d.cartaoCredito c) "),
    @NamedQuery(name = "Procedimento.selectProcedimentoSemCartao",
    query = " SELECT distinct p From Procedimento p "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND (:detalheProcedimento2 = 'todos' OR p.detalheProcedimento = :detalheProcedimento) "
    + " AND (:statusPagamento2 = 'todos' OR p.statusPagamento = :statusPagamento) "
    + " AND (:observacao2 = 'todos' OR p.observacao LIKE :observacao) "
    + " AND (:dataMovimentacao2 = 'todos' OR p.dataMovimentacao = :dataMovimentacao) "
    + " AND p.id NOT IN (Select d.id From DespesaProcedimento d Join d.cartaoCredito c) "
    + " ORDER BY p.dataMovimentacao, p.valorReal DESC, p.valorEstimado DESC"),
    @NamedQuery(name = "Procedimento.acumuladoReceitaPeriodo",
    query = " SELECT p.tipoProcedimento, "
    + " SUM(CASE WHEN p.valorReal is null THEN p.valorEstimado ELSE p.valorReal END) "
    + " From Procedimento p "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND p.dataMovimentacao between :dataI and :dataF "
    + " AND p.tipoProcedimento = br.com.gbvbahia.financeiro.constantes.TipoProcedimento.RECEITA_FINANCEIRA "
    + " GROUP BY p.tipoProcedimento "),
    @NamedQuery(name = "Procedimento.intervaloReceitaDatas",
    query = " SELECT new br.com.gbvbahia.financeiro.modelos.dto.MinMaxDateDTO("
    + "min(d.dataMovimentacao), "
    + "max(d.dataMovimentacao)) "
    + " From Procedimento d "
    + " WHERE (d.tipoProcedimento = br.com.gbvbahia.financeiro.constantes.TipoProcedimento.RECEITA_FINANCEIRA) "
    + " AND (:status2 = 'todos' OR d.statusPagamento = :status) "
    + " AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "),
    @NamedQuery(name = "Procedimento.pesquisaDetalheProcedimento",
    query = " SELECT p  From Procedimento p "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND (p.dataMovimentacao between :dataI and :dataF) "
    + " AND (p.tipoProcedimento = br.com.gbvbahia.financeiro.constantes.TipoProcedimento.RECEITA_FINANCEIRA) "
    + " AND (:detalhe2 = 'todos' OR p.detalhe = :detalhe) "
    + " ORDER BY p.dataMovimentacao, p.valorReal DESC, p.valorEstimado DESC ")
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
     * Data de vencimento do procedimento. ALTER TABLE
     * `guilherm_money`.`fin_procedimento` CHANGE COLUMN `data_vencimento`
     * `data_movimentacao` DATE NOT NULL ;
     *
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "data_movimentacao", nullable = false)
    @NotNull(message = "{Procedimento.dataMovimentacao.null}")
    private Date dataMovimentacao = new Date();
    /**
     * Valor estimado a pagar/receber do procedimento, este valor sempre
     * deverá ser informado. Caso não se tenha o valor real este será
     * considerado em calculos de estimativas.<br> Em contas Variaveis, em que
     * o valor real já existe, setar este igual ao real.
     */
    @NotNull(message = "{Procedimento.valorEstimado.null}")
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
    @NotNull(message = "{Procedimento.detalhe.null}")
    @JoinColumn(name = "fk_detalhe_procedimento", nullable = false)
    private DetalheProcedimento detalhe;
    /**
     * Para receitas/despesas sem ser por agentamento, cadastradas pelo
     * usuário, sempre será Variavel.<br> Quando o procedimento for criado
     * pelo agendador, será Fixo.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "classe_procedimento", nullable = false)
    @NotNull(message = "{Procedimento.classificacaoProcedimento.null}")
    private ClassificacaoProcedimento classificacaoProcedimento =
            ClassificacaoProcedimento.VARIAVEL;
    /**
     * Define como saber se está recebida/paga.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false)
    @NotNull(message = "{Procedimento.statusPagamento.null}")
    private StatusPagamento statusPagamento = StatusPagamento.NAO_PAGA;
    /**
     * Qualquer informação relativa ao pagamento.
     */
    @NotNull(message = "{Procedimento.observacao.null}")
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
    @NotNull(message = "{Procedimento.usuario.null}")
    private Usuario usuario;
    /**
     * Deve ser informado no construtor de quem implementa.<br> Define se o
     * Procedimento é uma receita, entra dinheiro ou uma despesa, saída de
     * dinheiro.
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{Procedimento.tipoProcedimento.null}")
    @Column(name = "tipo_procedimento", nullable = false)
    private TipoProcedimento tipoProcedimento = TipoProcedimento.RECEITA_FINANCEIRA;
    /**
     * Deve ser informado no construtor de quem implementa.<br> Define se o
     * Procedimento é uma receita, entra dinheiro ou uma despesa unica ou
     * parcelada, saída de dinheiro.
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{Procedimento.detalheProcedimento.null}")
    @Column(name = "detalhe_procedimento", nullable = false)
    private DetalheTipoProcedimento detalheProcedimento = DetalheTipoProcedimento.RECEITA_UNICA;
    /**
     *
     */
    @Transient
    private boolean marcadoTransient;

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
    protected Procedimento(final TipoProcedimento tipoEnum,
            DetalheTipoProcedimento detalheEnum) {
        this.tipoProcedimento = tipoEnum;
        this.detalheProcedimento = detalheEnum;
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
                + ", dataMovimentacao=" + dataMovimentacao
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
    public Date getDataMovimentacao() {
        return DateUtils.zerarHora(dataMovimentacao);
    }

    /**
     * Data de vencimento do procedimento.
     *
     * @param dataVen data
     */
    public void setDataMovimentacao(final Date dataVen) {
        this.dataMovimentacao = DateUtils.zerarHora(dataVen);
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
                + DateUtils.getDateToString(dataMovimentacao)
                + " | "
                + NumberUtils.currencyFormat(getValor().doubleValue());
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
            i = this.dataMovimentacao.compareTo(o.dataMovimentacao);
        }
        if (i == 0) {
            i = this.valorEstimado.compareTo(o.valorEstimado);
        }
        return i;
    }

    public DetalheTipoProcedimento getDetalheProcedimento() {
        return detalheProcedimento;
    }

    @Override
    public boolean isMarcadoTransient() {
        return marcadoTransient;
    }

    @Override
    public void setMarcadoTransient(boolean marcadoTransient) {
        this.marcadoTransient = marcadoTransient;
    }
}
