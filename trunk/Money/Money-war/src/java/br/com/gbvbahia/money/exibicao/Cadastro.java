/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.exibicao;

import br.com.gbvbahia.money.utils.UtilMetodos;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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

    private enum PaginaFluxo {

        CADASTRO_DETALHE_MOVIMENTACAO, CADASTRO_CONTA_BANCARIA, CADASTRO_CONTA_PAGAR, CADASTRO_CONTA_RECEBER;
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
    
    private void alterarTela(PaginaFluxo paginaLogin) {
        exibirCadastroDetalheMovimentacao = (PaginaFluxo.CADASTRO_DETALHE_MOVIMENTACAO.equals(paginaLogin));
        exibirCadastroContaBancaria = (PaginaFluxo.CADASTRO_CONTA_BANCARIA.equals(paginaLogin));
        exibirCadastroContaPagar = (PaginaFluxo.CADASTRO_CONTA_PAGAR.equals(paginaLogin));
        exibirCadastroContaReceber = (PaginaFluxo.CADASTRO_CONTA_RECEBER.equals(paginaLogin));
        definirAtributos(paginaLogin);
    }

    private void definirAtributos(PaginaFluxo paginaLogin) {
        FacesContext fc = FacesContext.getCurrentInstance();
        switch (paginaLogin) {
            case CADASTRO_DETALHE_MOVIMENTACAO:
                this.tituloPanel = UtilMetodos.getResourceBundle("titlePanelCadastroDetalheMovimentacao", fc);
                break;
            case CADASTRO_CONTA_BANCARIA:
                this.tituloPanel = UtilMetodos.getResourceBundle("titlePanelCadastroContaBancaria", fc);
                break;
            case CADASTRO_CONTA_PAGAR:
                this.tituloPanel = UtilMetodos.getResourceBundle("titlePanelCadastroContaPagar", fc);
                break;
            case CADASTRO_CONTA_RECEBER:
                this.tituloPanel = UtilMetodos.getResourceBundle("titlePanelCadastroContaReceber", fc);
                break;
            default:
                throw new AssertionError("Não foi definido case para a enum: " + paginaLogin.toString());
        }
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
}