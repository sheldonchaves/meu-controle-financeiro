/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades;

import br.com.financeiro.entidades.detalhes.GrupoGasto;
import br.com.financeiro.entidades.enums.FormaPagamento;
import br.com.financeiro.entidades.enums.StatusPagamento;
import br.com.financeiro.entidades.enums.TipoMovimentacao;
import br.com.financeiro.entidades.interfaces.Conta;
import br.com.financeiro.excecoes.ContaPagarReceberValueException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;
import org.apache.commons.lang.StringUtils;

/**
 * Representa uma super classe para contas a pagar ou receber.
 * Ela é abstrada e dentro de contas você encontra seus "filhos"
 * @author Guilherme
 */
@Entity
@Table(name = "contas_pagar")
public class ContaPagar implements Serializable, Comparable<ContaPagar>, Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "juros", nullable = false)
    private Double juros = 0.0;

    @Column(name = "data_vencimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataVencimento;

    @Column(name = "parcela_atual", nullable = false)
    private Integer parcelaAtual;

    @Column(name = "parcela_total", nullable = false)
    private Integer parcelaTotal;

    @Column(name = "observacao", length = 255, nullable = true)
    private String observacao;

    @Column(name = "status_pgto", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    @Column(name = "forma_pgto", nullable = false)
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    @Column(name="identificador", length=50, nullable=true)
    private String identificador;

    @OneToOne(targetEntity = br.com.financeiro.entidades.MovimentacaoFinanceira.class, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "movimentacao_id", referencedColumnName = "id")
    private MovimentacaoFinanceira movimentacaoFinanceira;

    @ManyToOne(targetEntity = br.com.financeiro.entidades.detalhes.GrupoGasto.class)
    @JoinColumn(name = "grupo_gasto_id", referencedColumnName = "id", nullable = false)
    private GrupoGasto grupoGasto;

    @ManyToOne(targetEntity = br.com.financeiro.entidades.User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", columnDefinition = "integer", nullable = false)
    private User user;

    @ManyToOne(targetEntity = br.com.financeiro.entidades.CartaoCreditoUnico.class)
    @JoinColumn(name = "cartao_credito_utilizado", referencedColumnName = "id")
    private CartaoCreditoUnico cartaoCreditoUnico;
    
    @ManyToOne(targetEntity = br.com.financeiro.entidades.ContaBancaria.class)
    @JoinColumns({
        @JoinColumn(name = "agencia_destino_id", referencedColumnName = "agencia_id"),
        @JoinColumn(name = "numero_conta_destino_id", referencedColumnName = "numero_conta")
    })
    private ContaBancaria contaPara;

    @Transient
    private boolean marcado;

    public MovimentacaoFinanceira getMovimentacaoFinanceira() {
        return movimentacaoFinanceira;
    }

    public void setMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) {
        this.movimentacaoFinanceira = movimentacaoFinanceira;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public Integer getId() {
        return id;
    }

    public Double getJuros() {
        return juros;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getSmallObs(){
        return StringUtils.substring(getObservacao(), 0, 20);
    }

    public Integer getParcelaAtual() {
        return parcelaAtual;
    }

    public Integer getParcelaTotal() {
        return parcelaTotal;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public Double getValor() {
        return valor;
    }
    
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setJuros(Double juros) {
        this.juros = juros;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setParcelaAtual(Integer parcelaAtual) {
        this.parcelaAtual = parcelaAtual;
    }

    public void setParcelaTotal(Integer parcelaTotal) {
        this.parcelaTotal = parcelaTotal;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CartaoCreditoUnico getCartaoCreditoUnico() {
        return cartaoCreditoUnico;
    }

    public void setCartaoCreditoUnico(CartaoCreditoUnico cartaoCreditoUnico) {
        this.cartaoCreditoUnico = cartaoCreditoUnico;
    }

    public ContaBancaria getContaPara() {
        return contaPara;
    }

    public void setContaPara(ContaBancaria contaPara) {
        this.contaPara = contaPara;
    }

    public GrupoGasto getGrupoGasto() {
        return grupoGasto;
    }

    public void setGrupoGasto(GrupoGasto grupoGasto) {
        this.grupoGasto = grupoGasto;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContaPagar other = (ContaPagar) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @PreRemove
    private void verificaConsistenciaDeletionContaPagarReceber() throws Exception {
        if (this.movimentacaoFinanceira != null) {
            throw new ContaPagarReceberValueException("Você não pode apagar uma conta que possui movimentação financeira em sua conta!");
        }
    }

    @PrePersist
    @PreUpdate
    private void verificaConsistenciaContaPagarReceber() throws Exception {
        if (this.valor == null) {
            throw new ContaPagarReceberValueException("O valor da receita deve ser informado!");
        }
        if (this.dataVencimento == null) {
            throw new ContaPagarReceberValueException("A data de vencimento desta receita deve ser informada!");
        }
        if (this.parcelaAtual == null) {
            throw new ContaPagarReceberValueException("O número da parcela deve ser informado!");
        }
        if (this.parcelaTotal == null) {
            throw new ContaPagarReceberValueException("O número total de parcelas deve ser informado!");
        }
        if (this.observacao == null) {
            throw new ContaPagarReceberValueException("A observação deve ser cadastrada!");
        }
        if (this.statusPagamento == null) {
            throw new ContaPagarReceberValueException("O status(Paga/Não Paga) deve ser informado!");
        }
        if (this.user == null) {
            throw new ContaPagarReceberValueException("O usuário responsável deve ser informado!");
        }
        if (this.parcelaAtual > this.parcelaTotal) {
            throw new ContaPagarReceberValueException("A parcela atual não pode ser maior que o total de parcelas!");
        }
        if (this.formaPagamento == null) {
            throw new ContaPagarReceberValueException("A forma de pagamento deve ser informada!");
        } else if (this.formaPagamento.equals(FormaPagamento.CARTAO_DE_CREDITO) && this.cartaoCreditoUnico == null) {
            throw new ContaPagarReceberValueException("O cartão de crédito deve ser informado!");
        } else if (this.formaPagamento.equals(FormaPagamento.CARTAO_DE_CREDITO) && this.cartaoCreditoUnico != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(this.getDataVencimento());
            if (!this.getCartaoCreditoUnico().getDiaVencimento().equals(c.get(Calendar.DAY_OF_MONTH))) {
                throw new ContaPagarReceberValueException("O dia de vencimento desta conta (" +c.get(Calendar.DAY_OF_MONTH) + ") deve ser o mesmo do dia informado no cartão de crédito (" + this.getCartaoCreditoUnico().getDiaVencimento()+ ")");
            }
        }
        if (this.user == null) {
            throw new ContaPagarReceberValueException("O usuário deve ser informado!");
        }
        if (this.grupoGasto == null) {
            throw new ContaPagarReceberValueException("O grupo de gasto deve ser informado!");
        }
        if(this.identificador != null && this.identificador.length() > 40){
            throw new ContaPagarReceberValueException("O identificador deve ter até 40 caracteres!");
        }
    }

    @Override
    public int compareTo(ContaPagar o) {
        int i = 0;
        if (i == 0) {
            i = this.dataVencimento.compareTo(o.getDataVencimento());
        }
        if (i == 0) {
            i = this.valor.compareTo(o.getValor()) * (-1);
        }

        return i;
    }

    @Override
    public Date getContaDataConta() {
        return this.dataVencimento;
    }

    @Override
    public String getContaForma() {
        if (this.formaPagamento != null) {
            return this.formaPagamento.getFormaPagamento();
        } else {
            return null;
        }
    }

    @Override
    public String getContaObservacao() {
        String toReturn = "";
        if (this.formaPagamento.equals(FormaPagamento.CARTAO_DE_CREDITO) && this.cartaoCreditoUnico != null) {
            toReturn += this.cartaoCreditoUnico.getLabelCartao() + " ";
        } else if (this.formaPagamento.equals(FormaPagamento.TRASFERENCIA_ELETRONICA) && this.contaPara != null) {
            toReturn += this.contaPara.getLabel() + " ";
        }
        return toReturn + this.observacao;
    }

    @Override
    public Integer getContaParcelaAtual() {
        return this.parcelaAtual;
    }

    @Override
    public Integer getContaParcelaTotal() {
        return this.parcelaTotal;
    }

    @Override
    public String getContaStatus() {
        if (this.statusPagamento != null) {
            return this.statusPagamento.getStatusString();
        } else {
            return null;
        }
    }

    @Override
    public String getContaTipo() {
        if(this.grupoGasto != null){
            return this.grupoGasto.getGrupoGasto();
        }else{
            return null;
        }
    }

    @Override
    public Double getContaValor() {
        return this.valor;
    }

    @Override
    public void setContaValor(Double valor){
        this.setValor(valor);
    }

    @Override
    public MovimentacaoFinanceira getContaMovimentacaoFinanceira() {
        return this.getMovimentacaoFinanceira();
    }

    @Override
    public TipoMovimentacao getContaTipoMovimentacao() {
        return TipoMovimentacao.RETIRADA;
    }

    @Override
    public void setContaMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) {
        this.setMovimentacaoFinanceira(movimentacaoFinanceira);
    }

    @Override
    public String toString() {
        return "Conta a Pagar {" + "Valor=" + valor + "dataVencimento=" + dataVencimento + "observacao=" + observacao + '}';
    }

}
