/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.modelos;

import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Um cartão de crédito repesenta o cartão utilizado para compras, no
 * sistema ele é utilizado para facilitar a definição de dadas de
 * vencimento e buscas de contas a pagar ou pagas com o mesmo.<br> Todas as
 * contas pagas com o cartão devem ter o mesmo dia de vencimento e
 * referenciar o cartão.
 *
 * @since 2012/04/12
 * @author Guilherme
 */
@Entity
@Table(name = "fin_cartao_credito", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "id", "ds_cartao"
    })
})
@NamedQueries({
    @NamedQuery(name = "CartaoCredito.Ativos",
    query = "Select c From CartaoCredito c "
    + "Where c.ativo = true "
    + "AND (c.usuario = :usuario OR c.usuario.conjuge = :usuario) "),
    @NamedQuery(name = "Cartao.countUser",
    query = "SELECT count(a) FROM CartaoCredito a "
    + " WHERE (a.usuario = :usuario OR a.usuario.conjuge = :usuario)"
    + " AND (:cartao2 = 'todos' OR a.cartao like :cartao)"),
    @NamedQuery(name = "Cartao.selectUser",
    query = "SELECT distinct a FROM CartaoCredito a "
    + " WHERE (a.usuario = :usuario OR a.usuario.conjuge = :usuario)"
    + " AND (:cartao2 = 'todos' OR a.cartao like :cartao)"
    + " ORDER BY a.cartao ")
})
public class CartaoCredito implements EntityInterface<CartaoCredito>,
        Serializable {

    /**
     * ID único no banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * Descrição única do cartão de crédito.
     */
    @NotNull
    @Size(max = 30, min = 3)
    @Column(name = "ds_cartao", nullable = false, length = 30)
    private String cartao;
    /**
     * Representa o dia do mês que o cartão vence.
     */
    @NotNull
    @Max(value = 31)
    @Min(value = 1)
    @Column(name = "dia_vencimento", nullable = false)
    private Integer diaVencimento;
    /**
     * Representa a quantidade de dias que a compra foi realizada para vir
     * ainda dentro do mesmo mês.<br> Se diaVencimento for 10, diaMesmoMes
     * igual a 5 e o dia da compra for dia 02/03, então: dia 02/03 mais 5 é
     * dia 07/03, antes do dia 10/03 portanto a comta ainda seria mesmo
     * mês.<br> Essa regra é para facilitar a seleção de data pelo usuário
     * ao cadastrar a conta.<br> Ao selecionar o cartão o sistema tem a
     * possibilidade de gerar a data de vencimento da conta.
     */
    @NotNull
    @Column(name = "dia_limite", nullable = false)
    @Max(15)
    @Min(0)
    private Integer diaMesmoMes = 7;
    /**
     * Define se o cartão ainda é utilizado ou não.
     */
    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;
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
     * Realiza o calculo baseado na data do momento somando diaMesmoMes
     * para determinar o mês de venimento da conta no cartão de crédito.
     *
     * @return Data de vencimento da fatura que virá a conta.
     */
    public Date getProximoVencimento() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, diaVencimento);
        Calendar aux = Calendar.getInstance();
        aux.add(Calendar.DAY_OF_MONTH, diaMesmoMes);
        if (aux.after(c)) {
            c.add(Calendar.MONTH, 1);
        }
        return c.getTime();
    }

    /**
     * Realiza o calculo baseado na data do momento somando diaMesmoMes
     * para determinar o mês de venimento da conta no cartão de crédito.
     *
     * @return Data de vencimento da fatura que virá a conta.
     */
    public Date getProximoVencimento(Date dataComparativa) {
        Calendar c = Calendar.getInstance();
        c.setTime(dataComparativa);
        int dias = 0;
        Calendar proxMes = Calendar.getInstance();
        proxMes.setTime(dataComparativa);
        proxMes.add(Calendar.MONTH, 1);
        proxMes.set(Calendar.DAY_OF_MONTH, diaVencimento);
        while (c.before(proxMes)) {
            dias++;
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (dias < diaMesmoMes) {
            c.setTime(dataComparativa);
            c.set(Calendar.DAY_OF_MONTH, diaVencimento);
            c.add(Calendar.MONTH, 2);
            return c.getTime();
        } else {
            c.setTime(dataComparativa);
            c.add(Calendar.DAY_OF_MONTH, diaMesmoMes);
            if (c.get(Calendar.MONTH) == proxMes.get(Calendar.MONTH)
                    && c.compareTo(proxMes) >= 0) {
                proxMes.add(Calendar.MONTH, 1);
            }
            if (dias > diaMesmoMes
                    && c.compareTo(proxMes) < 0) {
                proxMes.add(Calendar.MONTH, -1);
                if(c.compareTo(proxMes) > 0){
                    proxMes.add(Calendar.MONTH, 1);
                }
                return proxMes.getTime();
            }
            return proxMes.getTime();
        }
    }

    /**
     * Descrição única do cartão de crédito.
     *
     * @return Descrição do cartão.
     */
    public String getCartao() {
        return cartao;
    }

    /**
     * Descrição única do cartão de crédito.
     *
     * @param carDesc String
     */
    public void setCartao(final String carDesc) {
        this.cartao = carDesc;
    }

    /**
     * Representa a quantidade de dias que a compra foi realizada para vir
     * ainda dentro do mesmo mês.<br> Se diaVencimento for 10, diaMesmoMes
     * igual a 5 e o dia da compra for dia 02/03, então: dia 02/03 mais 5 é
     * dia 07/03, antes do dia 10/03 portanto a comta ainda seria mesmo
     * mês.<br> Essa regra é para facilitar a seleção de data pelo usuário
     * ao cadastrar a conta.<br> Ao selecionar o cartão o sistema tem a
     * possibilidade de gerar a data de vencimento da conta.
     *
     * @return Dias para vencimento mesmo mês ou próximo.
     */
    public Integer getDiaMesmoMes() {
        return diaMesmoMes;
    }

    /**
     * Representa a quantidade de dias que a compra foi realizada para vir
     * ainda dentro do mesmo mês.<br> Se diaVencimento for 10, diaMesmoMes
     * igual a 5 e o dia da compra for dia 02/03, então: dia 02/03 mais 5 é
     * dia 07/03, antes do dia 10/03 portanto a comta ainda seria mesmo
     * mês.<br> Essa regra é para facilitar a seleção de data pelo usuário
     * ao cadastrar a conta.<br> Ao selecionar o cartão o sistema tem a
     * possibilidade de gerar a data de vencimento da conta.
     *
     * @param diaMesmo Dias para vencimento mesmo mês ou próximo.
     */
    public void setDiaMesmoMes(final Integer diaMesmo) {
        this.diaMesmoMes = diaMesmo;
    }

    /**
     * Representa o dia do mês que o cartão vence.
     *
     * @return Dia do vencimento do cartão mensal.
     */
    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    /**
     * Representa o dia do mês que o cartão vence.
     *
     * @param diaVenc mensal.
     */
    public void setDiaVencimento(final Integer diaVenc) {
        this.diaVencimento = diaVenc;
    }

    /**
     * ID único banco de dados.
     *
     * @return Id unico BD.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Id unico banco de dados.
     *
     * @param idBd Id único BD.
     */
    public void setId(final Long idBd) {
        this.id = idBd;
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
     * Define se cartão está ativo ou não.
     *
     * @return True ativo, False não ativo.
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Define se cartão está ativo ou não.
     *
     * @param active True ativo, False não ativo.
     */
    public void setAtivo(final boolean active) {
        this.ativo = active;
    }

    @Override
    public String getLabel() {
        return StringUtils.substring(cartao, 0, CARACTERES_LABEL);
    }

    @Override
    public boolean verificarId() {
        return false;
    }

    @Override
    public int compareTo(final CartaoCredito o) {
        return this.cartao.compareToIgnoreCase(o.cartao);
    }

    @Override
    public String toString() {
        return "CartaoCredito{" + "id=" + id + ", cartao=" + cartao
                + ", diaVencimento=" + diaVencimento
                + ", diaMesmoMes=" + diaMesmoMes + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CartaoCredito other = (CartaoCredito) obj;
        if ((this.cartao == null)
                ? (other.cartao != null)
                : !this.cartao.equals(other.cartao)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.cartao != null
                ? this.cartao.hashCode() : 0);
        return hash;
    }
}
