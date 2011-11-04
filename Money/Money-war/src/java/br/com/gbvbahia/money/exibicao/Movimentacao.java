/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.exibicao;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "fluxoMovimentacao")
@SessionScoped
public class Movimentacao extends FluxoExibicaoMaster {

    private boolean exibirReceitasDivias;
    private boolean exibirMovimentcao;

    private enum PaginaFluxo {

        RECEITA_DIVIDA, MOVIMENTACAO_FINANCEIRA;
    }

    public Movimentacao() {
        super("Movimentacao");
        fluxoExibirReceitaDivida();
    }

    public void fluxoExibirReceitaDivida() {
        alterarTela(PaginaFluxo.RECEITA_DIVIDA);
    }
    public void fluxoExibirMovimentacao() {
        alterarTela(PaginaFluxo.MOVIMENTACAO_FINANCEIRA);
    }
    
     private void alterarTela(PaginaFluxo paginaLogin) {
        exibirReceitasDivias = (PaginaFluxo.RECEITA_DIVIDA.equals(paginaLogin));
        exibirMovimentcao = (PaginaFluxo.MOVIMENTACAO_FINANCEIRA.equals(paginaLogin));
    }

    public boolean isExibirMovimentcao() {
        return exibirMovimentcao;
    }

    public void setExibirMovimentcao(boolean exibirMovimentcao) {
        this.exibirMovimentcao = exibirMovimentcao;
    }

    public boolean isExibirReceitasDivias() {
        return exibirReceitasDivias;
    }

    public void setExibirReceitasDivias(boolean exibirReceitasDivias) {
        this.exibirReceitasDivias = exibirReceitasDivias;
    }
}
