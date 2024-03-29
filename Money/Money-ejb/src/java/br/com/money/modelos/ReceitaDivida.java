/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.modelos;

import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.modelos.commons.EntityInterface;
import br.com.money.utils.UtilBeans;
import br.com.money.vaidators.interfaces.ValidadoInterface;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author gbvbahia
 */
@Entity
@Table(name="money_receita_divida")
public class ReceitaDivida implements ValidadoInterface,
                                      EntityInterface<ReceitaDivida> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "vl_valor", nullable = false)
    @NotNull
    private Double valor;

    @Column(name = "vl_juros", nullable = false)
    @NotNull
    private Double juros = 0.0;

    @Column(name = "dt_vencimento", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dataVencimento;

    @Column(name = "nm_parcela_atual", nullable = false)
    @NotNull
    private Integer parcelaAtual;

    @Column(name = "nm_parcela_total", nullable = false)
    @NotNull
    private Integer parcelaTotal;

    @Column(name = "ds_observacao", length = 255, nullable = true)
    @Size(max = 255)
    private String observacao;
    
    @Column(name="nm_identificador", length=50, nullable=true)
    @NotNull
    @Size(max = 50)
    private String identificador;
    
    @Column(name = "en_status_pgto", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusPagamento statusPagamento = StatusPagamento.NAO_PAGA;

    @Column(name = "en_tipo_movimentacao", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoMovimentacao tipoMovimentacao;

    @NotNull
    @ManyToOne
    @JoinColumn(name="fk_detalhe_movimentacao_id", referencedColumnName="id",
            nullable=false)
    private DetalheMovimentacao detalheMovimentacao;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="fk_usuario_id", referencedColumnName="id", nullable = false)
    @NotNull
    private Usuario usuario;
    
    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Double getJuros() {
        return juros;
    }

    public void setJuros(Double juros) {
        this.juros = juros;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getParcelaAtual() {
        return parcelaAtual;
    }

    public void setParcelaAtual(Integer parcelaAtual) {
        this.parcelaAtual = parcelaAtual;
    }

    public Integer getParcelaTotal() {
        return parcelaTotal;
    }

    public void setParcelaTotal(Integer parcelaTotal) {
        this.parcelaTotal = parcelaTotal;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public DetalheMovimentacao getDetalheMovimentacao() {
        return detalheMovimentacao;
    }

    public void setDetalheMovimentacao(DetalheMovimentacao detalheMovimentacao) {
        this.detalheMovimentacao = detalheMovimentacao;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReceitaDivida)) {
            return false;
        }
        ReceitaDivida other = (ReceitaDivida) object;
        if ((this.id == null 
                && other.id != null) 
                || (this.id != null 
                && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReceitaDivida{" + "id=" + id + ", valor*=" + valor
                + ", juros=" + juros + ", dataVencimento*=" + dataVencimento
                + ", parcelaAtual=" + parcelaAtual + ", parcelaTotal*="
                + parcelaTotal + ", observacao=" + observacao
                + ", identificador=" + identificador + ", statusPagamento="
                + statusPagamento + ", tipoMovimentacao*="
                + tipoMovimentacao + '}';
    }



    @Override
    public int compareTo(ReceitaDivida o) {
        int i = 0;
        if(i == 0) i = this.dataVencimento.compareTo(o.dataVencimento);
        if(i == 0) i = (this.valor.compareTo(o.valor) * -1);
        if(i == 0) i = (this.parcelaAtual.compareTo(o.parcelaAtual) * -1);
        if(i == 0) i = this.id.compareTo(o.id);
        return i;
    }
    
    /**
     * Negativo para dívida e positivo para receita.
     * @return 
     */
    public Double getValorParaCalculoDireto(){
        //Se for uma recceita
        if(this.tipoMovimentacao.equals(TipoMovimentacao.DEPOSITO)){
            return this.valor;
        }else{//Se for uma dívida
            return this.valor * (-1);
        }
    }

    @Override
    public String getLabel() {
        try {
        return getTipoMovimentacao().getTipoMovimentacaoString() + " "
                + UtilBeans.getDataString(getDataVencimento()) + " "
                + getParcelaTotal() + " "
                + UtilBeans.currencyFormat(getValor());
        } catch (Exception e) {
            return toString();
        }
    }

    @Override
    public boolean verificarId() {
        return false;
    }
    
    //===============
    //TRANSIENTES
    //===============
    @Transient
    private ContaBancaria contaBancariaTransiente;

    public ContaBancaria getContaBancariaTransiente() {
        return contaBancariaTransiente;
    }

    public void setContaBancariaTransiente(ContaBancaria contaBancariaTransiente) {
        this.contaBancariaTransiente = contaBancariaTransiente;
    }
}
