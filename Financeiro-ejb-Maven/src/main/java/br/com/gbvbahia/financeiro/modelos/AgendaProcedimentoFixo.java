/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.Periodo;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.financeiro.modelos.superclass.DetalheProcedimento;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Reresenta um gaso ou receita fixa, sempre será cadastrado num
 * periodo de um ano.<br> Utiliza DetalheProcedimento para saber se é
 * uma receita ou gasto.<br> O valorFixo representa o valor
 * previsto.<br> A data primeiro vencimento representa a data de
 * inicio do procedimento fixo, a partir dela que as proximas serão
 * considerada. <br> Perido diz que campo quantidade deve acrescentar,
 * em dias, mes ou anos.
 *
 * @author Guilherme
 * @since v.3 01/04/2012
 */
@Entity
@Table(name = "fin_agenda_procedimento_fixo")
public class AgendaProcedimentoFixo
        implements EntityInterface<AgendaProcedimentoFixo>, Serializable {

    /**
     * Identificador no BD.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long codigo;
    /**
     * Valor que será definido na conta a pagar/receber quando for
     * criada. Esse valor é referente ao <b>previsto</b>.
     */
    @Column(name = "vl_fixo", nullable = false)
    @NotNull
    @DecimalMin(value = "0.01")
    private Double valorFixo = 0.00;
    /**
     * Data que será considerada como 1º vencimento da conta. A partir
     * desta data é realizado todo calculo para as contas serem
     * criadas.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_vencimento", nullable = false)
    @NotNull
    private Date dataPrimeiroVencimento;
    /**
     * Observção do procedimento fixo, essa mesma observação será
     * colocado na conta.
     */
    @Column(name = "ds_observacao", nullable = false, length = 50)
    @NotNull
    @Size(min = 5, max = 50)
    private String observacao;
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
     * Detalhe do Procedimento, este define se é uma Receita,
     * DetalheReceita ou Despesa DetalheDespesa.
     */
    @ManyToOne
    @JoinColumn(name = "fk_detalhe_id",
    referencedColumnName = "id",
    nullable = false)
    @NotNull
    private DetalheProcedimento detalhe;
    /**
     * Define a periodicidade das contas a serem criadas.<br> Default
     * Periodo.MESES.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "fl_periodo", nullable = false)
    @NotNull
    private Periodo periodo = Periodo.MESES;
    /**
     * Quantidade a ser aplicada no periodo.<br> Default 1.
     */
    @Column(name = "nm_quantidade", nullable = false)
    @Min(value = 1)
    private int quantidadePeriodo = 1;

    @Override
    public Serializable getId() {
        return this.codigo;
    }

    @Override
    public String getLabel() {
        return toString();
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(final AgendaProcedimentoFixo o) {
        return dataPrimeiroVencimento.compareTo(o.dataPrimeiroVencimento);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AgendaProcedimentoFixo other = (AgendaProcedimentoFixo) obj;
        if (this.codigo != other.codigo
                && (this.codigo == null
                || !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash
                + (this.codigo != null
                ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "AgendaProcedimentoFixo{" + "codigo=" + codigo
                + ", valorFixo=" + valorFixo
                + ", dataPrimeiroVencimento=" + dataPrimeiroVencimento
                + ", observacao=" + observacao + ", detalhe="
                + detalhe + ", periodo=" + periodo
                + ", quantidadePeriodo=" + quantidadePeriodo + '}';
    }

    /**
     * Identificador no BD.
     *
     * @return Long
     */
    public Long getCodigo() {
        return codigo;
    }

    /**
     * Identificador no BD.
     *
     * @param idBD id unico BD.
     */
    public void setCodigo(final Long idBD) {
        this.codigo = idBD;
    }

    /**
     * Data que será considerada como 1º vencimento da conta. A partir
     * desta data é realizado todo calculo para as contas serem
     * criadas.
     *
     * @return Date
     */
    public Date getDataPrimeiroVencimento() {
        return dataPrimeiroVencimento;
    }

    /**
     * Data que será considerada como 1º vencimento da conta. A partir
     * desta data é realizado todo calculo para as contas serem
     * criadas.
     *
     * @param data Date
     */
    public void setDataPrimeiroVencimento(final Date data) {
        this.dataPrimeiroVencimento = data;
    }

    /**
     * Detalhe do Procedimento, este define se é uma Receita,
     * DetalheReceita ou Despesa DetalheDespesa.
     *
     * @return Detalhe do Procedimento
     */
    public DetalheProcedimento getDetalhe() {
        return detalhe;
    }

    /**
     * Detalhe do Procedimento, este define se é uma Receita,
     * DetalheReceita ou Despesa DetalheDespesa.
     *
     * @param detalheProced Detalhe do Procedimento
     */
    public void setDetalhe(final DetalheProcedimento detalheProced) {
        this.detalhe = detalheProced;
    }

    /**
     * Observção do procedimento fixo, essa mesma observação será
     * colocado na conta.
     *
     * @return String
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * Observção do procedimento fixo, essa mesma observação será
     * colocado na conta.
     *
     * @param obs String
     */
    public void setObservacao(final String obs) {
        this.observacao = obs;
    }

    /**
     * Define a periodicidade das contas a serem criadas.<br> Default
     * Periodo.MESES.
     *
     * @return Periodo
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * Define a periodicidade das contas a serem criadas.<br> Default
     * Periodo.MESES.
     *
     * @param periodoAgenda Periodo.
     */
    public void setPeriodo(final Periodo periodoAgenda) {
        this.periodo = periodoAgenda;
    }

    /**
     * Quantidade a ser aplicada no periodo.<br> Default 1.
     *
     * @return int
     */
    public int getQuantidadePeriodo() {
        return quantidadePeriodo;
    }

    /**
     * Quantidade a ser aplicada no periodo.<br> Default 1.
     *
     * @param quantidade int
     */
    public void setQuantidadePeriodo(final int quantidade) {
        this.quantidadePeriodo = quantidade;
    }

    /**
     * Usuario responsavel.
     *
     * @return Usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Usuario responsavel.
     *
     * @param user Usuario
     */
    public void setUsuario(final Usuario user) {
        this.usuario = user;
    }

    /**
     * Valor que será definido na conta a pagar/receber quando for
     * criada. Esse valor é referente ao <b>previsto</b>.
     *
     * @return Double.
     */
    public Double getValorFixo() {
        return valorFixo;
    }

    /**
     * Valor que será definido na conta a pagar/receber quando for
     * criada. Esse valor é referente ao <b>previsto</b>.
     *
     * @param valor Double
     */
    public void setValorFixo(final Double valor) {
        this.valorFixo = valor;
    }
}
