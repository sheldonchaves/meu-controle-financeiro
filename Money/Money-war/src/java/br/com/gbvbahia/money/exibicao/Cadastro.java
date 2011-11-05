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

    private enum PaginaFluxo {

        CADASTRO_DETALHE_MOVIMENTACAO, CADASTRO_CONTA_BANCARIA, CADASTRO_CONTA_PAGAR, CADASTRO_CONTA_RECEBER,
        TRANSFERENCIA_ENTRE_CONTAS;
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

    public void fluxoExibirCadastroContaReceita() {
        alterarTela(PaginaFluxo.CADASTRO_CONTA_RECEBER);
    }
    
    public void fluxoExibirTransferenciaEntreContas() {
        alterarTela(PaginaFluxo.TRANSFERENCIA_ENTRE_CONTAS);
    }
    
    private void alterarTela(PaginaFluxo paginaLogin) {
        exibirCadastroDetalheMovimentacao = (PaginaFluxo.CADASTRO_DETALHE_MOVIMENTACAO.equals(paginaLogin));
        exibirCadastroContaBancaria = (PaginaFluxo.CADASTRO_CONTA_BANCARIA.equals(paginaLogin));
        exibirCadastroContaPagar = (PaginaFluxo.CADASTRO_CONTA_PAGAR.equals(paginaLogin));
        exibirCadastroContaReceber = (PaginaFluxo.CADASTRO_CONTA_RECEBER.equals(paginaLogin));
        exibirTransferenciaEntreContas = (PaginaFluxo.TRANSFERENCIA_ENTRE_CONTAS.equals(paginaLogin));
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
}