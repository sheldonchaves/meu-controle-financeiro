/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.DetalheTipoProcedimento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * Representa uma conta única de pagamento, como uma som comprado a vista ou
 * uma despesa fixa, como a conta de luz/água.<br> Não possui parcelas.
 *
 * @since 12/04/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_procedimento_despesa_unica")
@NamedQueries({
    @NamedQuery(name = "Despesa.CartaoStatusUsuario",
    query = " SELECT distinct d From DespesaProcedimento d "
    + " WHERE (:cartao is null OR d.cartaoCredito = :cartao) "
    + " AND (:status2 = 'todos' OR d.statusPagamento = :status) "
    + " AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario)"),
    @NamedQuery(name = "Procedimento.buscarCartaoStatusUsrTipoProcedimento",
    query = " SELECT distinct d From DespesaProcedimento d "
    + " WHERE (:cartao2 = 'todos' OR d.cartaoCredito = :cartao) "
    + " AND (:status2 = 'todos' OR d.statusPagamento = :status) "
    + " AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "
    + " AND (:tipoProcedimento2 = 'todos' OR d.tipoProcedimento = :tipoProcedimento) "),
    @NamedQuery(name = "DespesaProcedimento.intervaloDatas",
    query = " SELECT new br.com.gbvbahia.financeiro.modelos.dto.MinMaxDateDTO(min(d.dataMovimentacao), max(d.dataMovimentacao)) "
    + " From DespesaProcedimento d "
    + " WHERE (:cartao2 = 'todos' OR d.cartaoCredito = :cartao) "
    + " AND (:status2 = 'todos' OR d.statusPagamento = :status) "
    + " AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "),
    @NamedQuery(name = "DespesaProcedimento.buscarDespesaUsuarioIntervalo",
    query = " SELECT distinct d From DespesaProcedimento d "
    + " WHERE (:cartao2 = 'todos' OR d.cartaoCredito = :cartao) "
    + " AND (:status2 = 'todos' OR d.statusPagamento = :status) "
    + " AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "
    + " AND CASE WHEN d.dataCartao is null THEN d.dataMovimentacao "
    + " ELSE d.dataCartao END between :dataI and :dataF "),
    @NamedQuery(name = "DespesaProcedimento.buscarDespesaUsuarioIntervaloMovimentacao",
    query = " SELECT distinct d From DespesaProcedimento d "
    + " WHERE (:cartao2 = 'todos' OR d.cartaoCredito = :cartao) "
    + " AND (:status2 = 'todos' OR d.statusPagamento = :status) "
    + " AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario) "
    + " AND d.dataMovimentacao "
    + " between :dataI and :dataF "),
    @NamedQuery(name = "DespesaProcedimento.countProcedimento",
    query = " SELECT count(p) From DespesaProcedimento p "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND (:cartao2 = 'todos' OR p.cartaoCredito = :cartao) "
    + " AND (:detalheProcedimento2 = 'todos' OR p.detalheProcedimento = :detalheProcedimento) "
    + " AND (:statusPagamento2 = 'todos' OR p.statusPagamento = :statusPagamento)"
    + " AND (:observacao2 = 'todos' OR p.observacao LIKE :observacao)"
    + " AND (:dataMovimentacao2 = 'todos' OR p.dataMovimentacao = :dataMovimentacao)"),
    @NamedQuery(name = "DespesaProcedimento.selectProcedimento",
    query = " SELECT distinct p From DespesaProcedimento p "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND (:cartao2 = 'todos' OR p.cartaoCredito = :cartao) "
    + " AND (:detalheProcedimento2 = 'todos' OR p.detalheProcedimento = :detalheProcedimento) "
    + " AND (:statusPagamento2 = 'todos' OR p.statusPagamento = :statusPagamento) "
    + " AND (:observacao2 = 'todos' OR p.observacao LIKE :observacao)"
    + " AND (:dataMovimentacao2 = 'todos' OR p.dataMovimentacao = :dataMovimentacao)"
    + " ORDER BY p.dataMovimentacao, p.dataCartao, p.valorReal DESC, p.valorEstimado DESC"),
    @NamedQuery(name = "DespesaProcedimento.buscarDespesasCartao",
    query = " SELECT distinct p From DespesaProcedimento p JOIN p.cartaoCredito c "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND p.dataCartao between :dataI and :dataF "),
    @NamedQuery(name = "DespesaProcedimento.acumuladoCartaoPeriodo",
    query = " SELECT p.cartaoCredito, "
    + " SUM(CASE WHEN p.valorReal is null THEN p.valorEstimado ELSE p.valorReal END) "
    + " From DespesaProcedimento p "
    + " JOIN p.cartaoCredito c "
    + " WHERE (p.usuario = :usuario OR p.usuario.conjuge = :usuario) "
    + " AND (CASE WHEN p.dataCartao is null THEN p.dataMovimentacao "
        + "  ELSE p.dataCartao END) between :dataI and :dataF "
    + " GROUP BY p.cartaoCredito ")
})
@DiscriminatorValue("DESPESA_UNICA")
public class DespesaProcedimento extends Procedimento
        implements Serializable {

    /**
     * Data de vencimento do da conta no cartao. ALTER TABLE
     * `guilherm_money`.`fin_procedimento_despesa_unica` ADD COLUMN
     * `data_cartao` DATE NULL DEFAULT NULL AFTER `fk_cartao_credito_id` ;
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "data_cartao")
    private Date dataCartao;
    /**
     * Não obrigatório, se a conta for em um cartão o mesmo deve ser
     * informado.
     */
    @ManyToOne
    @JoinColumn(name = "fk_cartao_credito_id", nullable = true)
    private CartaoCredito cartaoCredito;

    /**
     * Construtor padrão que informa ao Procedimento que está extensão é uma
     * Despesa.
     */
    public DespesaProcedimento() {
        this(DetalheTipoProcedimento.DESPESA_UNICA);
    }

    /**
     * Facilitando a construção polimorfica. Cria a despesa com o cartão
     * informado, determina a data do cartão com base na data de movimentação
     * informada. Se a data movimentação não for informado irá considerar a
     * mesma como sendo a data atual do sistema operacional. Data movimentação
     * não é setado, utilizar setDataMovimentacao de Procedimento.
     *
     * @param cartaoCredito pode ser null.
     * @param dataMovimentacao pode ser null.
     */
    public DespesaProcedimento(CartaoCredito cartaoCredito, Date dataMovimentacao) {
        this(DetalheTipoProcedimento.DESPESA_UNICA);
        this.cartaoCredito = cartaoCredito;
        if (cartaoCredito != null && dataMovimentacao != null) {
            this.dataCartao = cartaoCredito.getProximoVencimento(dataMovimentacao);
        } else if (cartaoCredito != null) {
            this.dataCartao = cartaoCredito.getProximoVencimento();
        }
    }

    /**
     * Utilizado para definir outro tipo de detalhe por subclasses.
     *
     * @param detalheProcedimento
     */
    protected DespesaProcedimento(DetalheTipoProcedimento detalheProcedimento) {
        super(TipoProcedimento.DESPESA_FINANCEIRA, detalheProcedimento);
    }

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

    public Date getDataCartao() {
        return DateUtils.zerarHora(dataCartao);
    }

    public void setDataCartao(Date dataCartao) {
        this.dataCartao = DateUtils.zerarHora(dataCartao);
    }

    @Override
    public String getLabel() {
        if (cartaoCredito == null) {
            return super.getLabel();
        }
        return super.getLabel() + " | " + cartaoCredito.getLabel();
    }

    /**
     * Se data cartão nula retorna data movimentação, se não data cartão.
     *
     * @return
     */
    public Date getDate() {
        if (this.dataCartao == null) {
            return super.getDataMovimentacao();
        } else {
            return dataCartao;
        }
    }
}
