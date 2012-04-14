/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.superclass.Procedimento;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * Representa uma conta única de pagamento, como uma som comprado a
 * vista ou uma despesa fixa, como a conta de luz/água.<br> Não possui
 * parcelas.
 *
 * @since 12/04/2012
 * @author Guilherme
 */
@Entity
@Table(name = "fin_procedimento_despesa_unica")
@NamedQueries({
    @NamedQuery(name = "Despesa.CartaoStatusUsuario",
    query = " SELECT d From DespesaProcedimento d "
        + "WHERE (:cartao is null OR d.cartaoCredito = :cartao) "
        + "AND (:status2 = 'todos' OR d.statusPagamento = :status) "
        + "AND (d.usuario = :usuario OR d.usuario.conjuge = :usuario)")
})
@DiscriminatorValue("DESPESA")
public class DespesaProcedimento extends Procedimento
        implements Serializable {

    /**
     * Construtor padrão que informa ao Procedimento que está extensão
     * é uma Despesa.
     */
    public DespesaProcedimento() {
        super(TipoProcedimento.DESPESA_FINANCEIRA);
    }
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
}
