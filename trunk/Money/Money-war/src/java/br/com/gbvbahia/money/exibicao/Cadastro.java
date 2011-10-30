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

    private enum PaginaFluxo {

        CADASTRO_DETALHE_MOVIMENTACAO, CADASTRO_CONTA_BANCARIA, CADASTRO_CONTA_PAGAR;
    }

    /** Creates a new instance of Cadastro */
    public Cadastro() {
        super("Cadastro");
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

    private void alterarTela(PaginaFluxo paginaLogin) {
        exibirCadastroDetalheMovimentacao = (PaginaFluxo.CADASTRO_DETALHE_MOVIMENTACAO.equals(paginaLogin));
        exibirCadastroContaBancaria = (PaginaFluxo.CADASTRO_CONTA_BANCARIA.equals(paginaLogin));
        exibirCadastroContaPagar = (PaginaFluxo.CADASTRO_CONTA_PAGAR.equals(paginaLogin));
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
}