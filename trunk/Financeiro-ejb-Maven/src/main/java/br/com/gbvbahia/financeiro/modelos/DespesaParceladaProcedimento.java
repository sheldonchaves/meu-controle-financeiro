/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * Representa uma conta parcelada de pagamento, como uma som comprado em 12x
 * ou um carro em 7x prestações.<br> Possui parcelas.
 *
 * @since 14/04/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_procedimento_despesa_parcelada")
@NamedQueries({
    @NamedQuery(name = "DespesaParcelada.cartaoStatusUsuarioData",
    query = " SELECT d From DespesaParceladaProcedimento d "
    + "WHERE (:cartao2 = 'todos' OR d.cartaoCredito = :cartao) "
    + "AND (:status2 = 'todos' OR d.statusPagamento = :status) "
    + "AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "
    + "AND (:dataI2 = 'todos' OR d.dataVencimento >= :dataI) "
    + "AND (:dataF2 = 'todos' OR d.dataVencimento <= :dataF) ")
})
@DiscriminatorValue("DESPESA_PARCELADA")
public class DespesaParceladaProcedimento extends Procedimento
        implements Serializable {

    /**
     * Construtor padrão que informa ao Procedimento que está extensão é uma
     * Despesa.
     */
    public DespesaParceladaProcedimento() {
        super(TipoProcedimento.DESPESA_FINANCEIRA);
    }

    /**
     * Construtor que cria uma despesa carregando os dados de um procedimento.
     *
     * @param procedimento Procedimento de origem.
     */
    public DespesaParceladaProcedimento(final Procedimento procedimento) {
        super(TipoProcedimento.DESPESA_FINANCEIRA);
        setClassificacaoProcedimento(
                procedimento.getClassificacaoProcedimento());
        setDataVencimento(procedimento.getDataVencimento());
        setDetalhe(procedimento.getDetalhe());
        setObservacao(procedimento.getObservacao());
        setStatusPagamento(procedimento.getStatusPagamento());
        setUsuario(procedimento.getUsuario());
        setValorEstimado(procedimento.getValorEstimado());
        setValorReal(procedimento.getValorReal());
    }
    /**
     * Define a parcela atual do pagamento.
     */
    @Column(name = "nm_parcela_atual", nullable = false)
    @NotNull
    @Min(value = 1)
    private Integer parcelaAtual;
    /**
     * Define a quantidade de parcelas total do pagamento.
     */
    @Column(name = "nm_parcela_total", nullable = false)
    @NotNull
    @Min(value = 2)
    private Integer parcelaTotal;
    /**
     * Cada conta parcelada deve ter um ID que as une, para facilitar uma
     * deleção e/ou atualização.<br> Cada parcelamento tem seu ID único.
     */
    @Column(name = "nm_identificador", length = 100, nullable = false)
    @NotNull
    @Size(max = 100)
    private String identificador;
    /**
     * Não obrigatório, se a conta for em um cartão o mesmo deve ser
     * informado.
     */
    @ManyToOne
    @JoinColumn(name = "cartao_credito_id", nullable = true)
    private CartaoCredito cartaoCredito;

    /**
     * Recupera o cartão de crédito.
     *
     * @return Se não for em cartão devolve null.
     */
    public CartaoCredito getCartaoCredito() {
        return cartaoCredito;
    }

    /**
     * Determina o cartão que será cobrado.
     *
     * @param cartao Cartão gasto.
     */
    public void setCartaoCredito(final CartaoCredito cartao) {
        this.cartaoCredito = cartao;
    }

    /**
     * O Identificador deste parcelamento.
     *
     * @return String idParcelamento
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Determina o ID unico do parceamento.
     *
     * @param ident String ID.
     */
    public void setIdentificador(final String ident) {
        this.identificador = ident;
    }

    /**
     * Determina a parcela atual do parcelamento.
     *
     * @return Parcela atual.
     */
    public Integer getParcelaAtual() {
        return parcelaAtual;
    }

    /**
     * Determina a parcela atual do parcelamento.
     *
     * @param parAtual Parcela atual.
     */
    public void setParcelaAtual(final Integer parAtual) {
        this.parcelaAtual = parAtual;
    }

    /**
     * Total de parcelas do parcelamento.
     *
     * @return Total parcelas.
     */
    public Integer getParcelaTotal() {
        return parcelaTotal;
    }

    /**
     * Total de parcelas do parcelamento.
     *
     * @param parTotal Total parcelas.
     */
    public void setParcelaTotal(final Integer parTotal) {
        this.parcelaTotal = parTotal;
    }

    @Override
    public String toString() {
        return "DespesaParceladaProcedimento{" + "parcelaAtual=" + parcelaAtual + ", parcelaTotal=" + parcelaTotal + ", identificador=" + identificador + '}';
    }
}
