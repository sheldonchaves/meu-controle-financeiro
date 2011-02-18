/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.entidades;

import br.com.financeiro.entidades.detalhes.GrupoReceita;
import br.com.financeiro.entidades.enums.FormaRecebimento;
import br.com.financeiro.entidades.enums.StatusReceita;
import br.com.financeiro.entidades.enums.TipoMovimentacao;
import br.com.financeiro.entidades.interfaces.Conta;
import br.com.financeiro.excecoes.ContaPagarReceberValueException;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Guilherme
 */
@Entity
@Table(name = "contas_receber")
public class ContaReceber implements Serializable, Comparable<ContaReceber>, Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;
    @Column(name = "valor", nullable = false)
    private Double valor;
    @Column(name = "data_pagamento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataPagamento;
    @Column(name = "parcela_atual", nullable = false)
    private Integer parcelaAtual;
    @Column(name = "parcela_total", nullable = false)
    private Integer parcelaTotal;
    @Column(name = "observacao", length = 255, nullable = true)
    private String observacao;
    @Column(name = "status_receita", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusReceita statusReceita;
    @Column(name = "forma_receita", nullable = false)
    @Enumerated(EnumType.STRING)
    private FormaRecebimento formaRecebimento;
    @OneToOne(targetEntity = br.com.financeiro.entidades.MovimentacaoFinanceira.class, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "movimentacao_id", referencedColumnName = "id")
    private MovimentacaoFinanceira movimentacaoFinanceira;
    @ManyToOne(targetEntity = br.com.financeiro.entidades.detalhes.GrupoReceita.class)
    @JoinColumn(name = "grupo_receita_id", referencedColumnName = "id", nullable = false)
    private GrupoReceita grupoReceita;
    @ManyToOne(targetEntity = br.com.financeiro.entidades.User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", columnDefinition = "integer", nullable = false)
    private User user;

    @PreRemove
    private void verificaConsistenciaDelContaReceber() {
        if (this.movimentacaoFinanceira != null) {
            throw new ContaPagarReceberValueException("Você não pode apagar uma receita que possui movimentação financeira em sua conta!");
        }
    }

    @PreUpdate
    @PrePersist
    private void verificaConsistenciaContaReceber() {
        if (this.valor == null) {
            throw new ContaPagarReceberValueException("O valor da receita deve ser informado!");
        }
        if (this.dataPagamento == null) {
            throw new ContaPagarReceberValueException("A data de pagamento desta receita deve ser informada!");
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
        if (this.statusReceita == null) {
            throw new ContaPagarReceberValueException("O status(Paga/Não Paga) deve ser informado!");
        }
        if (this.user == null) {
            throw new ContaPagarReceberValueException("O usuário responsável deve ser informado!");
        }
        if (this.formaRecebimento == null) {
            throw new ContaPagarReceberValueException("A forma de recebimento da receita deve ser informado!");
        }
        if (this.parcelaAtual > this.parcelaTotal) {
            throw new ContaPagarReceberValueException("A parcela atual não pode ser maior que o total de parcelas!");
        }
        if (this.user == null) {
            throw new ContaPagarReceberValueException("O usuário deve ser informado!");
        }
        if (this.grupoReceita == null) {
            throw new ContaPagarReceberValueException("O grupo de receita deve ser informado!");
        }
    }

    public GrupoReceita getGrupoReceita() {
        return grupoReceita;
    }

    public void setGrupoReceita(GrupoReceita grupoReceita) {
        this.grupoReceita = grupoReceita;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public MovimentacaoFinanceira getMovimentacaoFinanceira() {
        return movimentacaoFinanceira;
    }

    public void setMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) {
        this.movimentacaoFinanceira = movimentacaoFinanceira;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getSmallObs() {
        return StringUtils.substring(getObservacao(), 0, 20);
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

    public StatusReceita getStatusReceita() {
        return statusReceita;
    }

    public void setStatusReceita(StatusReceita statusReceita) {
        this.statusReceita = statusReceita;
    }

    public Double getValor() {
        return valor;
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

    @Override
    public int compareTo(ContaReceber o) {
        int i = 0;
        if (i == 0) {
            i = this.dataPagamento.compareTo(o.getDataPagamento());
        }
        if (i == 0) {
            i = this.valor.compareTo(o.getValor()) * (-1);
        }
        return i;
    }

    public FormaRecebimento getFormaRecebimento() {
        return formaRecebimento;
    }

    public void setFormaRecebimento(FormaRecebimento formaRecebimento) {
        this.formaRecebimento = formaRecebimento;
    }

    @Override
    public Date getContaDataConta() {
        return this.dataPagamento;
    }

    @Override
    public String getContaForma() {
        if (this.formaRecebimento != null) {
            return this.formaRecebimento.getFormaRecebimento();
        } else {
            return null;
        }
    }

    @Override
    public String getContaObservacao() {
        return this.observacao;
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
        if (this.statusReceita != null) {
            return this.statusReceita.getStatusString();
        } else {
            return null;
        }
    }

    @Override
    public String getContaTipo() {
        if (this.grupoReceita != null) {
            return this.grupoReceita.getGrupoReceita();
        } else {
            return null;
        }
    }

    @Override
    public Double getContaValor() {
        return this.valor;
    }

    @Override
    public void setContaValor(Double valor) {
        this.setValor(valor);
    }

    @Override
    public MovimentacaoFinanceira getContaMovimentacaoFinanceira() {
        return this.getMovimentacaoFinanceira();
    }

    @Override
    public TipoMovimentacao getContaTipoMovimentacao() {
        return TipoMovimentacao.DEPOSITO;
    }

    @Override
    public void setContaMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) {
        this.setMovimentacaoFinanceira(movimentacaoFinanceira);
    }
}
