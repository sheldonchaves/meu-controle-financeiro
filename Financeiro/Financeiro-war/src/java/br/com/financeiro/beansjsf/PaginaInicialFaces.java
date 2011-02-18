/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf;

import br.com.financeiro.utils.UtilMetodos;
import javax.faces.context.FacesContext;

/**
 *
 * @author Guilherme
 */
public class PaginaInicialFaces {

    private boolean login = true;

    private String tituloTela;

    private enum PaginaLogin {

        Login;

    }

    public PaginaInicialFaces() {
        exibirLogin();
    }

    public void exibirLogin() {
        alterarTela(PaginaLogin.Login);
    }

    private void alterarTela(PaginaLogin paginaLogin) {
        login = (paginaLogin == PaginaLogin.Login);
        tituloTela(paginaLogin);
    }

    private void tituloTela(PaginaLogin paginaLogin) {
        FacesContext fc = FacesContext.getCurrentInstance();
        switch (paginaLogin) {
            case Login:
                this.tituloTela = UtilMetodos.getResourceBundle("loginPageTitle", fc);
                break;
        }
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getTituloTela() {
        return tituloTela;
    }

    public void setTituloTela(String tituloTela) {
        this.tituloTela = tituloTela;
    }
}
