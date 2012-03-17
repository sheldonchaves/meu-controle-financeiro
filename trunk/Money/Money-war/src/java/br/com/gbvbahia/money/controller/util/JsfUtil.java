package br.com.gbvbahia.money.controller.util;

import br.com.gbvbahia.money.utils.MensagemUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Guilherme
 */
public class JsfUtil {

    /**
     * Definie o nível de log para toda aplicação
     */
    public static final Level LEVEL_LOG = Level.INFO;

    /**
     * Criado pelo IDE esse método retorna um Array de SelectItem que você
     * utiliza para seleção de Objetos, bem genérico, funciona com qualquer
     * entidade
     *
     * @param entities Uma lista de entidades
     * @param selectOne Um booleano a respeito se será numa lista multipla
     * false ou unica true
     * @return Array de SelectItem[] para ser utilizado na tag f:selectItems
     */
    public static SelectItem[] getSelectItems(List<?> entities,
            boolean selectOne, FacesContext contex) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem(null,
                    MensagemUtils.getResourceBundle("selecione", contex));
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    /**
     * Passe o nome do Controller (Manager, Bean, varios nomes) JSF juntamente
     * com o Contexto JSF e receba o Controller necessário.
     *
     * @throws java.lang.NullPointerException - Se FacesContext for nulo
     * @throws PropertyNotFoundException - se não encontrar o Controller,
     * procure buscar de sessão ou aplicação, os de visão ou requisição bem
     * provavel não existirem.
     * @throws ELException - Caso um erro inesperado ocorra poderá ser
     * encapsulado nesta exceção
     * @param <T> Retorna o controler solicitado, por utilizar generics você
     * deve definir uma variavel para receber o retorno.
     * @param controlerName O nome do Controller definido na anotação
     * @ManagedBean(name = "orgaoController")
     * @param facesContext O contexto JSF, normalmente se utiliza
     * FacesContext.getCurrentInstance() para recuperar o mesmo.
     * @return O controler solicitado, por utilizar generics, passe o retorno
     * para uma variavel declarada.
     */
    public static <T> T getController(String controlerName,
            FacesContext facesContext) {
        T controller = (T) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, controlerName);
        return controller;
    }

        /**
     * Utilizando para recuperar um bean da sessão do usuário, podendo
     * fazer comunicação entre os beans.
     * @param name nome do bean JSF
     * @param fc Contexto JSF
     * @return T Mesmo tipo passado.
     */
    public static <T> T getBean(String name, FacesContext fc) {
        Map sessionMap = fc.getExternalContext().getSessionMap();
        return (T) sessionMap.get(name);
    }
    /**
     * Internet Explorer o nome do arquivo vem totalmente qualificado. <br>
     * FireFox vem somente o nome do arquivo, sem os diretorios.<br> Esse
     * método faz o tratamento.
     *
     * @param file Arquivo enviado pelo usuário.
     * @return java.lang.String
     */
    public static String getFileName(final UploadedFile file) {
        String nome = "";
        nome = StringUtils.substringAfterLast(file.getFileName(),
                File.separator);
        if (StringUtils.isBlank(nome)) {
            return file.getFileName();
        }
        return nome;
    }

    /**
     * Passe o arquivo e co contexto para que o cliente realize o download
     * do arquivo.<br>
     * Utiliza o Servlet: br.com.convergeti.solida.servlet.ServletFileDownload
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
     * Gera uma url que pode ser utilizada para chamar o Servlel para baixar
     * o arquivo desejado.
     * @param facesContext Contexto JSF
     * @param caminhoArquivo Caminho para o arquivo.
     * @param nomeArquivo Nome do arquivo.
     * @return  String que pode ser utilziada como URL para baixar o arquivo.
     */
    public static String gerarUrlToDownload(final FacesContext facesContext,
            final String caminhoArquivo, final String nomeArquivo) {
        return facesContext.getExternalContext()
     .getRequestContextPath()
     + "/SFile?caminhoArquivo=" + caminhoArquivo
     + "&nomeArquivo=" + nomeArquivo;
    }
}