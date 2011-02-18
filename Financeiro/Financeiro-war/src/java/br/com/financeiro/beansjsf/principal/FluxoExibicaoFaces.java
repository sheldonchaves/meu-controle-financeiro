/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.principal;

import br.com.financeiro.utils.UtilMetodos;
import javax.faces.context.FacesContext;

/**
 *
 * @author Guilherme
 */
public class FluxoExibicaoFaces {

    private boolean inicial;

    private boolean pagas;

    private boolean pagarCartao;

    private boolean provisoesAcumulado;

    private boolean provisoesCartaoCredito;

    private boolean provisoesGrupoGasto;

    private boolean provisoesGrupoGastoDetalhe;

    private boolean provisoesObservacao;

    private boolean cadastroContaBancaria;

    private boolean cadastroCartaoCredito;

    private boolean cadastroCartaoCreditoDependente;

    private boolean cadastroConjuge;

    private boolean cadastroUsuarios;

    private boolean cadastroGrupoGasto;

    private boolean cadastroGrupoReceita;

    private boolean cadastroContas;

    private boolean cadastroReceita;

    private boolean pessoalSenha;

    private boolean pessoalAviso;

    private String tituloTela;

    private String img;

    private enum PaginaLogin {

        INICIAL, PAGAS, PAGAR_CARTAO, PROVISOES_ACUMULADO, PROVISOES_CARTAO_CREDITO, PROVISOES_GRUPO_GASTO, PROVISOES_GRUPO_GASTO_DETALHE, PROVISOES_OBSERVACAO,
        CADASTRO_CONTA_BANCARIA, CADASTRO_CARTAO_CREDITO, CADASTRO_CARTAO_CREDITO_DEPENDENTE, CADASTRO_CONJUGE, CADASTRO_USUARIOS, CADASTRO_GRUPO_GASTO,
        CADASTRO_GRUPO_RECEITA, CADASTRO_CONTAS, CADASTRO_RECEITA, PESSOAL_SENHA, PESSOAL_AVISO;

    }

    /** Creates a new instance of FluxoExibicaoFaces */
    public FluxoExibicaoFaces() {
        exibirInicial();
    }

    public void exibirInicial() {
        alterarTela(PaginaLogin.INICIAL);
    }

    public void exibirPagarCartao() {
        alterarTela(PaginaLogin.PAGAR_CARTAO);
    }

    public void exibirAcumulado() {
        alterarTela(PaginaLogin.PROVISOES_ACUMULADO);
    }

    public void exibirObservacoes() {
        alterarTela(PaginaLogin.PROVISOES_OBSERVACAO);
    }

    public void exibirCartaoCredito() {
        alterarTela(PaginaLogin.PROVISOES_CARTAO_CREDITO);
    }

    public void exibirGrupoGastoDTO() {
        alterarTela(PaginaLogin.PROVISOES_GRUPO_GASTO);
    }

    public void exibirGrupoGastoDetalheDTO() {
        alterarTela(PaginaLogin.PROVISOES_GRUPO_GASTO_DETALHE);
    }

    public void exibirPagas() {
        alterarTela(PaginaLogin.PAGAS);
    }

    public void exibirCadastroContaBancaria() {
        alterarTela(PaginaLogin.CADASTRO_CONTA_BANCARIA);
    }

    public void exibirCadastroCartaoCredito() {
        alterarTela(PaginaLogin.CADASTRO_CARTAO_CREDITO);
    }

    public void exibirCadastroCartaoCreditoDependnete() {
        alterarTela(PaginaLogin.CADASTRO_CARTAO_CREDITO_DEPENDENTE);
    }

    public void exibirCadastroConjuge() {
        alterarTela(PaginaLogin.CADASTRO_CONJUGE);
    }

    public void exibirCadastroUsuario() {
        alterarTela(PaginaLogin.CADASTRO_USUARIOS);
    }

    public void exibirCadastroGrupoGasto() {
        alterarTela(PaginaLogin.CADASTRO_GRUPO_GASTO);
    }

    public void exibirCadastroGrupoReceita() {
        alterarTela(PaginaLogin.CADASTRO_GRUPO_RECEITA);
    }

    public void exibirCadastroContasDebito() {
        alterarTela(PaginaLogin.CADASTRO_CONTAS);
    }

    public void exibirCadastroReceita() {
        alterarTela(PaginaLogin.CADASTRO_RECEITA);
    }

    public void exibirPessoalSenha() {
        alterarTela(PaginaLogin.PESSOAL_SENHA);
    }

    public void exibirPessoalAviso() {
        alterarTela(PaginaLogin.PESSOAL_AVISO);
    }

    private void alterarTela(PaginaLogin paginaLogin) {
        inicial = (paginaLogin == PaginaLogin.INICIAL);
        pagas = (paginaLogin == PaginaLogin.PAGAS);
        pagarCartao = (paginaLogin == PaginaLogin.PAGAR_CARTAO);
        cadastroContaBancaria = (paginaLogin == PaginaLogin.CADASTRO_CONTA_BANCARIA);
        cadastroCartaoCredito = (paginaLogin == PaginaLogin.CADASTRO_CARTAO_CREDITO);
        cadastroCartaoCreditoDependente = (paginaLogin == PaginaLogin.CADASTRO_CARTAO_CREDITO_DEPENDENTE);
        cadastroConjuge = (paginaLogin == PaginaLogin.CADASTRO_CONJUGE);
        cadastroUsuarios = (paginaLogin == PaginaLogin.CADASTRO_USUARIOS);
        cadastroGrupoGasto = (paginaLogin == PaginaLogin.CADASTRO_GRUPO_GASTO);
        cadastroGrupoReceita = (paginaLogin == PaginaLogin.CADASTRO_GRUPO_RECEITA);
        cadastroContas = (paginaLogin == PaginaLogin.CADASTRO_CONTAS);
        cadastroReceita = (paginaLogin == PaginaLogin.CADASTRO_RECEITA);
        provisoesAcumulado = (paginaLogin == PaginaLogin.PROVISOES_ACUMULADO);
        pessoalSenha = (paginaLogin == PaginaLogin.PESSOAL_SENHA);
        pessoalAviso = (paginaLogin == PaginaLogin.PESSOAL_AVISO);
        provisoesCartaoCredito = (paginaLogin == PaginaLogin.PROVISOES_CARTAO_CREDITO);
        provisoesGrupoGasto = (paginaLogin == PaginaLogin.PROVISOES_GRUPO_GASTO);
        provisoesGrupoGastoDetalhe = (paginaLogin == PaginaLogin.PROVISOES_GRUPO_GASTO_DETALHE);
        provisoesObservacao = (paginaLogin == PaginaLogin.PROVISOES_OBSERVACAO);
        tituloTela(paginaLogin);
    }

    private void tituloTela(PaginaLogin paginaLogin) {
        FacesContext fc = FacesContext.getCurrentInstance();
        switch (paginaLogin) {
            case INICIAL:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleInicial", fc);
                this.img = "/imagens/ret47.png";
                break;
            case PAGAS:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitlePagas", fc);
                this.img = "/imagens/ret47.png";
                break;
            case PAGAR_CARTAO:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitlePagarCartao", fc);
                this.img = "/imagens/cc347.png";
                break;
            case PROVISOES_ACUMULADO:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleAcumulado", fc);
                this.img = "/imagens/graf.png";
                break;
            case CADASTRO_CONTA_BANCARIA:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCadastroContaBancaria", fc);
                this.img = "/imagens/pig47.png";
                break;
            case CADASTRO_CARTAO_CREDITO:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCadastroCartaoCredito", fc);
                this.img = "/imagens/cc47.png";
                break;
            case CADASTRO_CARTAO_CREDITO_DEPENDENTE:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCadastroCartaoCreditoDependente", fc);
                this.img = "/imagens/cc47_dep.png";
                break;
            case CADASTRO_CONJUGE:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCadastroConjuge", fc);
                this.img = "/imagens/conjuge.png";
                break;
            case CADASTRO_USUARIOS:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCadastroUsuario", fc);
                this.img = "/imagens/user47.png";
                break;
            case CADASTRO_GRUPO_GASTO:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCadastroGrupoGasto", fc);
                this.img = "/imagens/gGasto.png";
                break;
            case CADASTRO_GRUPO_RECEITA:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCadastroGrupoReceita", fc);
                this.img = "/imagens/gReceita.png";
                break;
            case CADASTRO_CONTAS:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCadastroContasDebito", fc);
                this.img = "/imagens/pg47.png";
                break;
            case CADASTRO_RECEITA:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCadastroReceita", fc);
                this.img = "/imagens/rc47.png";
                break;
            case PESSOAL_SENHA:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitlePessoalAlterarSenha", fc);
                this.img = "/imagens/login.png";
                break;
            case PESSOAL_AVISO:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitlePessoalAlterarAviso", fc);
                this.img = "/imagens/clock47.png";
                break;
            case PROVISOES_CARTAO_CREDITO:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleCartaoCredito", fc);
                this.img = "/imagens/cc247.png";
                break;
            case PROVISOES_GRUPO_GASTO:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleProvisoesGrupoGasto", fc);
                this.img = "/imagens/gragg.png";
                break;
            case PROVISOES_GRUPO_GASTO_DETALHE:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleProvisoesGrupoGasto2", fc);
                this.img = "/imagens/grupoG.png";
                break;
            case PROVISOES_OBSERVACAO:
                this.tituloTela = UtilMetodos.getResourceBundle("topTitleProvisoesObservacoes", fc);
                this.img = "/imagens/obs_search.png";
                break;
        }
    }

    //Getters AND Setters
    public boolean isInicial() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public String getTituloTela() {
        return tituloTela;
    }

    public void setTituloTela(String tituloTela) {
        this.tituloTela = tituloTela;
    }

    public boolean isCadastroContaBancaria() {
        return cadastroContaBancaria;
    }

    public void setCadastroContaBancaria(boolean cadastroContaBancaria) {
        this.cadastroContaBancaria = cadastroContaBancaria;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isCadastroCartaoCredito() {
        return cadastroCartaoCredito;
    }

    public void setCadastroCartaoCredito(boolean cadastroCartaoCredito) {
        this.cadastroCartaoCredito = cadastroCartaoCredito;
    }

    public boolean isCadastroUsuarios() {
        return cadastroUsuarios;
    }

    public void setCadastroUsuarios(boolean cadastroUsuarios) {
        this.cadastroUsuarios = cadastroUsuarios;
    }

    public boolean isCadastroGrupoGasto() {
        return cadastroGrupoGasto;
    }

    public void setCadastroGrupoGasto(boolean cadastroGrupoGasto) {
        this.cadastroGrupoGasto = cadastroGrupoGasto;
    }

    public boolean isCadastroGrupoReceita() {
        return cadastroGrupoReceita;
    }

    public void setCadastroGrupoReceita(boolean cadastroGrupoReceita) {
        this.cadastroGrupoReceita = cadastroGrupoReceita;
    }

    public boolean isCadastroContas() {
        return cadastroContas;
    }

    public void setCadastroContas(boolean cadastroContas) {
        this.cadastroContas = cadastroContas;
    }

    public boolean isCadastroReceita() {
        return cadastroReceita;
    }

    public void setCadastroReceita(boolean cadastroReceita) {
        this.cadastroReceita = cadastroReceita;
    }

    public boolean isPagas() {
        return pagas;
    }

    public void setPagas(boolean pagas) {
        this.pagas = pagas;
    }

    public boolean isPessoalSenha() {
        return pessoalSenha;
    }

    public void setPessoalSenha(boolean pessoalSenha) {
        this.pessoalSenha = pessoalSenha;
    }

    public boolean isPessoalAviso() {
        return pessoalAviso;
    }

    public void setPessoalAviso(boolean pessoalAviso) {
        this.pessoalAviso = pessoalAviso;
    }

    public boolean isProvisoesAcumulado() {
        return provisoesAcumulado;
    }

    public void setProvisoesAcumulado(boolean provisoesAcumulado) {
        this.provisoesAcumulado = provisoesAcumulado;
    }

    public boolean isProvisoesCartaoCredito() {
        return provisoesCartaoCredito;
    }

    public void setProvisoesCartaoCredito(boolean provisoesCartaoCredito) {
        this.provisoesCartaoCredito = provisoesCartaoCredito;
    }

    public boolean isProvisoesGrupoGasto() {
        return provisoesGrupoGasto;
    }

    public void setProvisoesGrupoGasto(boolean provisoesGrupoGasto) {
        this.provisoesGrupoGasto = provisoesGrupoGasto;
    }

    public boolean isProvisoesGrupoGastoDetalhe() {
        return provisoesGrupoGastoDetalhe;
    }

    public void setProvisoesGrupoGastoDetalhe(boolean provisoesGrupoGastoDetalhe) {
        this.provisoesGrupoGastoDetalhe = provisoesGrupoGastoDetalhe;
    }

    public boolean isCadastroCartaoCreditoDependente() {
        return cadastroCartaoCreditoDependente;
    }

    public void setCadastroCartaoCreditoDependente(boolean cadastroCartaoCreditoDependente) {
        this.cadastroCartaoCreditoDependente = cadastroCartaoCreditoDependente;
    }

    public boolean isCadastroConjuge() {
        return cadastroConjuge;
    }

    public void setCadastroConjuge(boolean cadastroConjuge) {
        this.cadastroConjuge = cadastroConjuge;
    }

    public boolean isPagarCartao() {
        return pagarCartao;
    }

    public void setPagarCartao(boolean pagarCartao) {
        this.pagarCartao = pagarCartao;
    }

    public boolean isProvisoesObservacao() {
        return provisoesObservacao;
    }

    public void setProvisoesObservacao(boolean provisoesObservacao) {
        this.provisoesObservacao = provisoesObservacao;
    }
}
