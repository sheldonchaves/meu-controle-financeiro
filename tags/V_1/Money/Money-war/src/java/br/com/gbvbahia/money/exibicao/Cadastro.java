/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.exibicao;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author gbvbahia
 */
@ManagedBean(name = "fluxoCadastro")
@SessionScoped
public class Cadastro extends FluxoExibicaoMaster {

    private boolean exibirCadastroDetalheMovimentacao;
    private boolean exibirCadastroContaBancaria;
    private boolean exibirCadastroContaPagar;
    private boolean exibirCadastroContaReceber;
    private boolean exibirTransferenciaEntreContas;
    private boolean exibirScheduler;
    private boolean exibirCadastroContaPagarCartao;
    private boolean exibirRelatorioAcumuladoMensal;
    private boolean exibirRelatorioCartaoMensal;

    private enum PaginaFluxo {

        CADASTRO_DETALHE_MOVIMENTACAO, CADASTRO_CONTA_BANCARIA, CADASTRO_CONTA_PAGAR, CADASTRO_CONTA_PAGAR_CARTAO, CADASTRO_CONTA_RECEBER,
        TRANSFERENCIA_ENTRE_CONTAS,
        SCHEDULER,
        ACUMULADO_MENSAL, CARTAO_MENSAL;
    }

    /** Creates a new instance of Cadastro */
    public Cadastro() {
        super("Cadastro");
        fluxoExibirCadastroContaPagar();
    }

    public void fluxoExibirCadastroDetalheMovimentacao() {
        alterarTela(PaginaFluxo.CADASTRO_DETALHE_MOVIMENTACAO);
    }

    public void fluxoExibirCadastroContaBancaria() {
        alterarTela(PaginaFluxo.CADASTRO_CONTA_BANCARIA);
    }

    public void fluxoExibirCadastroContaPagar() {
        alterarTela(PaginaFluxo.CADASTRO_CONTA_PAGAR);
    }

    public void fluxoExibirCadastroContaPagarCartao() {
        alterarTela(PaginaFluxo.CADASTRO_CONTA_PAGAR_CARTAO);
    }

    public void fluxoExibirCadastroContaReceita() {
        alterarTela(PaginaFluxo.CADASTRO_CONTA_RECEBER);
    }

    public void fluxoExibirTransferenciaEntreContas() {
        alterarTela(PaginaFluxo.TRANSFERENCIA_ENTRE_CONTAS);
    }

    public void fluxoExibirScheduler() {
        alterarTela(PaginaFluxo.SCHEDULER);
    }

    public void fluxoExibirAcumuladoMensal() {
        alterarTela(PaginaFluxo.ACUMULADO_MENSAL);
    }

    public void fluxoExibirCartaoMensal() {
        alterarTela(PaginaFluxo.CARTAO_MENSAL);
    }

    private void alterarTela(PaginaFluxo paginaLogin) {
        exibirCadastroDetalheMovimentacao = (PaginaFluxo.CADASTRO_DETALHE_MOVIMENTACAO.equals(paginaLogin));
        exibirCadastroContaBancaria = (PaginaFluxo.CADASTRO_CONTA_BANCARIA.equals(paginaLogin));
        exibirCadastroContaPagar = (PaginaFluxo.CADASTRO_CONTA_PAGAR.equals(paginaLogin));
        exibirCadastroContaPagarCartao = (PaginaFluxo.CADASTRO_CONTA_PAGAR_CARTAO.equals(paginaLogin));
        exibirCadastroContaReceber = (PaginaFluxo.CADASTRO_CONTA_RECEBER.equals(paginaLogin));
        exibirTransferenciaEntreContas = (PaginaFluxo.TRANSFERENCIA_ENTRE_CONTAS.equals(paginaLogin));
        exibirScheduler = (PaginaFluxo.SCHEDULER.equals(paginaLogin));
        exibirRelatorioAcumuladoMensal = (PaginaFluxo.ACUMULADO_MENSAL.equals(paginaLogin));
        exibirRelatorioCartaoMensal = (PaginaFluxo.CARTAO_MENSAL.equals(paginaLogin));
        defineAlinhamneto(paginaLogin);
    }

    /**
     *  VALIGN= MIDDLE TOP BOTTOM (horizontal)
     *  ALIGN=  LEFT - CENTER - RIGHT (alinhamento)
     * @param paginaFluxo 
     */
    private void defineAlinhamneto(PaginaFluxo paginaFluxo) {
        switch (paginaFluxo) {
            case ACUMULADO_MENSAL:
                setAlinhamento("center");
                setHorizontal("top");
                break;
            case CARTAO_MENSAL:
                setAlinhamento("center");
                setHorizontal("top");
                break;
            default:
                setAlinhamento("left");
                setHorizontal("middle");
        }
    }

    public boolean isExibirScheduler() {
        return exibirScheduler;
    }

    public void setExibirScheduler(boolean exibirScheduler) {
        this.exibirScheduler = exibirScheduler;
    }

    public boolean isExibirCadastroDetalheMovimentacao() {
        return exibirCadastroDetalheMovimentacao;
    }

    public void setExibirCadastroDetalheMovimentacao(boolean exibirCadastroDetalheMovimentacao) {
        this.exibirCadastroDetalheMovimentacao = exibirCadastroDetalheMovimentacao;
    }

    public boolean isExibirCadastroContaBancaria() {
        return exibirCadastroContaBancaria;
    }

    public void setExibirCadastroContaBancaria(boolean exibirCadastroContaBancaria) {
        this.exibirCadastroContaBancaria = exibirCadastroContaBancaria;
    }

    public boolean isExibirCadastroContaPagar() {
        return exibirCadastroContaPagar;
    }

    public void setExibirCadastroContaPagar(boolean exibirCadastroContaPagar) {
        this.exibirCadastroContaPagar = exibirCadastroContaPagar;
    }

    public boolean isExibirCadastroContaReceber() {
        return exibirCadastroContaReceber;
    }

    public void setExibirCadastroContaReceber(boolean exibirCadastroContaReceber) {
        this.exibirCadastroContaReceber = exibirCadastroContaReceber;
    }

    public boolean isExibirTransferenciaEntreContas() {
        return exibirTransferenciaEntreContas;
    }

    public void setExibirTransferenciaEntreContas(boolean exibirTransferenciaEntreContas) {
        this.exibirTransferenciaEntreContas = exibirTransferenciaEntreContas;
    }

    public boolean isExibirCadastroContaPagarCartao() {
        return exibirCadastroContaPagarCartao;
    }

    public void setExibirCadastroContaPagarCartao(boolean exibirCadastroContaPagarCartao) {
        this.exibirCadastroContaPagarCartao = exibirCadastroContaPagarCartao;
    }

    public boolean isExibirRelatorioAcumuladoMensal() {
        return exibirRelatorioAcumuladoMensal;
    }

    public void setExibirRelatorioAcumuladoMensal(boolean exibirRelatorioAcumuladoMensal) {
        this.exibirRelatorioAcumuladoMensal = exibirRelatorioAcumuladoMensal;
    }

    public boolean isExibirRelatorioCartaoMensal() {
        return exibirRelatorioCartaoMensal;
    }

    public void setExibirRelatorioCartaoMensal(boolean exibirRelatorioCartaoMensal) {
        this.exibirRelatorioCartaoMensal = exibirRelatorioCartaoMensal;
    }
}