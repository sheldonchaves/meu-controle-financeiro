package br.com.gbvbahia.projeto.web.jsfutil;


import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe utilitaria para trabalhar com JSF.
 *
 * @author Guilherme
 * @since 2012/03/14
 */
public final class JsfUtil {

    /**
     * String que representa o retorno para ficar na mesma página.
     */
    public static final String MANTEM = "mesma_pagina";
    /**
     * Direciona para /pages/principal.xhtml.<br>
     * <b>Menssagens GROWL são ignoradas.</b>
     */
    public static final String PAGES_PRINCIPAL = "principal";
    /**
     * Direciona para /pages/principal.xhtml, mostrando mensagens
     * no growl para o usuário.
     */
    public static final String PAGES_PRINCIPAL_NR = "principal_nr";
    /**
     * Direciona para /admin/principal.xhtml.<br>
     * <b>Menssagens GROWL são ignoradas.</b>
     */
    public static final String ADMIN_PRINCIPAL = "admin_principal";
    /**
     * Direciona para página de login.<br>
     * <b>Menssagens GROWL são ignoradas.</b>
     */
    public static final String LOGIN_PAGE = "login";
    /**
     * Direciona para página de alteração de senha.<br>
     * <b>Menssagens GROWL são ignoradas.</b>
     */
    public static final String PASSWORD_PAGE = "password";


    /**
     * Passe o arquivo e co contexto para que o cliente realize o
     * download do arquivo.<br> Utiliza o Servlet:
     * br.com.convergeti.solida.servlet.ServletFileDownload
     *
     * @param file Arquivo a ser enviado ao usuário.
     * @param facesContext Contexto JSF
     */
    public static void downloadFile(final File file,
            final FacesContext facesContext) {
        String caminhoArquivo = file.getAbsolutePath();
        String nomeArquivo = file.getName();
        HttpServletResponse resp = (HttpServletResponse)
                facesContext.getExternalContext().getResponse();
        try {
            resp.sendRedirect(gerarUrlToDownload(facesContext,
                    caminhoArquivo, nomeArquivo));
        } catch (IOException e) {
            e.printStackTrace();
        }
        facesContext.responseComplete();
    }

    /**
     * Gera uma url que pode ser utilizada para chamar o Servlel para
     * baixar o arquivo desejado.
     *
     * @param facesContext Contexto JSF
     * @param caminhoArquivo Caminho para o arquivo.
     * @param nomeArquivo Nome do arquivo.
     * @return String que pode ser utilziada como URL para baixar o
     * arquivo.
     */
    public static String gerarUrlToDownload(final FacesContext facesContext,
            final String caminhoArquivo, final String nomeArquivo) {
        return facesContext.getExternalContext().getRequestContextPath()
                + "/SFile?caminhoArquivo=" + caminhoArquivo
                + "&nomeArquivo=" + nomeArquivo;
    }

    /**
     * Criado pelo IDE esse método retorna um Array de SelectItem que
     * você utiliza para seleção de Objetos, bem genérico, funciona
     * com qualquer entidade.
     *
     * @param entities Uma lista de EntityInterface
     * @param selectOne True acrescenta a opção Selecione, false
     * somente opções da entidade são exibidas.
     * @param contex Contexto JSF
     * @return Array de SelectItem[] para ser utilizado na tag
     * f:selectItems
     */
    public static SelectItem[] getSelectItems(
            final Collection<? extends EntityInterface> entities,
            final boolean selectOne, final FacesContext contex) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem(null,
                    MensagemUtils.getResourceBundle("selecione", contex));
            i++;
        }
        for (EntityInterface x : entities) {
            items[i++] = new SelectItem(x, x.getLabel());
        }
        return items;
    }

    /**
     * Converte a Enum em um selectItem para ser inserido em um campo de selecao.
     * @param enumm
     * @param selectOne
     * @param contex
     * @return SelectItem[] bem bunitim.
     */
    public static SelectItem[] getEnumSelectItems(final Class<? extends Enum> enumm,
            final boolean selectOne, final FacesContext contex){
        List<SelectItem> items = new ArrayList<SelectItem>();
        int i = 0;
        if (selectOne) {
            items.add(new SelectItem(null,
                    MensagemUtils.getResourceBundle("selecione", contex)));
            i++;
        }
        for(Object o : enumm.getEnumConstants()){
            items.add(new SelectItem(o.toString(),
                    MensagemUtils.getResourceBundle(o.toString(), contex)));
        }
        return items.toArray(new SelectItem[items.size()]);
    }
    
    /**
     * Passe o nome do Controller (Manager, Bean, varios nomes) JSF
     * juntamente com o Contexto JSF e receba o Controller necessário.
     *
     * @throws java.lang.NullPointerException - Se FacesContext for
     * nulo
     * @throws PropertyNotFoundException - se não encontrar o
     * Controller, procure buscar de sessão ou aplicação, os de visão
     * ou requisição bem provavel não existirem.
     * @throws ELException - Caso um erro inesperado ocorra poderá ser
     * encapsulado nesta exceção
     * @param <T> Retorna o controler solicitado, por utilizar
     * generics você deve definir uma variavel para receber o retorno.
     * @param controlerName O nome do Controller definido na anotação
     * @ManagedBean(name = "orgaoController")
     * @param facesContext O contexto JSF, normalmente se utiliza
     * FacesContext.getCurrentInstance() para recuperar o mesmo.
     * @return O controler solicitado, por utilizar generics, passe o
     * retorno para uma variavel declarada.
     */
    public static <T> T getController(final String controlerName,
            final FacesContext facesContext) {
        T controller = (T) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, controlerName);
        return controller;
    }

    /**
     * Não pode ser instânciado.
     */
    private JsfUtil() {
    }
}