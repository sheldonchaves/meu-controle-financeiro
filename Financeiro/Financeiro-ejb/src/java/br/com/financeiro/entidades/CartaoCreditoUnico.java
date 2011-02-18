/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

/**
 *  Classe utilizada para armazenar cartões de créditos
 * XML: CartaoCreditoUnico.xml
 * Uma forma de pagamento CartaoCredito contém referencia para um cartão de crédito, que é mapeado por esta classo.
 * O nome Unico foi uma forma de diferenciar as duas classes.
 * @author Guilherme
 */
@Entity
@Table(name = "cartoes_de_creditos")
public class CartaoCreditoUnico implements Serializable, Comparable<CartaoCreditoUnico> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "status_cartao", nullable = false)
    private boolean statusCartao = true;

    @Column(name = "numero_cartao", nullable = false, length = 30, unique = true)
    private String numeroCartao;

    @Column(name = "dia_vencimento_cartao", nullable = false)
    private Integer diaVencimento;

    @Column(name = "dia_limite_conta_mesmo_mes", nullable = false)
    private Integer diaMesmoMes;

    @Column(name = "nome_cartao", nullable = false, length = 100)
    private String empresaCartao;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = br.com.financeiro.entidades.User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", columnDefinition = "Integer", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = br.com.financeiro.entidades.CartaoCreditoUnico.class)
    @JoinColumn(name = "cartao_titular_id", referencedColumnName = "id", columnDefinition = "Integer", nullable = true)
    private CartaoCreditoUnico cartaoCreditoTitular;

    public Integer getDiaMesmoMes() {
        return diaMesmoMes;
    }

    public void setDiaMesmoMes(Integer diaMesmoMes) {
        this.diaMesmoMes = diaMesmoMes;
    }

    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public String getEmpresaCartao() {
        return empresaCartao;
    }

    public void setEmpresaCartao(String empresaCartao) {
        this.empresaCartao = empresaCartao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public boolean isStatusCartao() {
        return statusCartao;
    }

    public void setStatusCartao(boolean statusCartao) {
        this.statusCartao = statusCartao;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CartaoCreditoUnico getCartaoCreditoTitular() {
        return cartaoCreditoTitular;
    }

    public void setCartaoCreditoTitular(CartaoCreditoUnico cartaoCreditoTitular) {
        this.cartaoCreditoTitular = cartaoCreditoTitular;
    }

    public String getLabelCartao() {
        if (this.empresaCartao != null && this.numeroCartao != null) {
            return  (this.cartaoCreditoTitular == null ? "" : "(D) ") + this.empresaCartao + "/" + numeroCartao.substring(0, 4);
        } else {
            return null;
        }
    }

    public Date getProximoVencimento() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, getDiaVencimento());
        Calendar aux = Calendar.getInstance();
        aux.add(Calendar.DAY_OF_MONTH, 7);
        if (aux.after(c)) {
            c.add(Calendar.MONTH, 1);
        }
        return c.getTime();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CartaoCreditoUnico other = (CartaoCreditoUnico) obj;
        if ((this.numeroCartao == null) ? (other.numeroCartao != null) : !this.numeroCartao.equals(other.numeroCartao)) {
            return false;
        }
        if ((this.empresaCartao == null) ? (other.empresaCartao != null) : !this.empresaCartao.equals(other.empresaCartao)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.empresaCartao != null ? this.empresaCartao.hashCode() : 0);
        return hash;
    }

    public int compareTo(CartaoCreditoUnico o) {
        if (getLabelCartao() == null) {
            return -1;
        } else if (o.getLabelCartao() == null) {
            return 1;
        }
        return getLabelCartao().compareTo(o.getLabelCartao());
    }
}
