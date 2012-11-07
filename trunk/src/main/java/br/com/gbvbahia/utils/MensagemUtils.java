/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @since 29/04/2012
 * @author Guilherme
 */
public final class MensagemUtils {

    /**
     * Não pode ser instânciada.
     */
    private MensagemUtils() {
    }

    /**
     * Cria FacesMessage para ser envida ao usuario atraves do
     * resource bundle. Menssagem a adicionada dentro deste método.
     *
     * @param msg Texto em negrito, antes do detail.
     * @param variantes Um array de objeto ara sobrescrever todos
     * {0}... da msg
     * @param severity Severidade da MSG, INFO, ERROR, FATAL, WARN
     * @param currentJsf COntexto JSF
     * @return Retorna um texto do resource bundle em formato de
     * string com variações.
     */
    public static String messageFactoringFull(final String msg,
            final Object[] variantes,
            final FacesMessage.Severity severity,
            final FacesContext currentJsf) {
        return messageFactoringFull(null, msg, variantes, severity,
                currentJsf);
    }

    /**
     * Cria FacesMessage para ser envida ao usuario atraves do
     * resource bundle. Menssagem a adicionada dentro deste método.
     *
     * @param clientId ID no JSF, um input text, slectItem...
     * @param msg Texto em negrito, antes do detail.
     * @param variantes Um array de objeto ara sobrescrever todos
     * {0}... da msg
     * @param severity Severidade da MSG, INFO, ERROR, FATAL, WARN
     * @param currentJsf COntexto JSF
     * @return Retorna um texto do resource bundle em formato de
     * string com variações.
     */
    public static String messageFactoringFull(final String clientId,
            final String msg,
            final Object[] variantes,
            final FacesMessage.Severity severity,
            final FacesContext currentJsf) {
        return messageFactoringFull(null, clientId, msg, variantes,
                severity, currentJsf);
    }

    /**
     * Cria FacesMessage para ser envida ao usuario atraves do
     * resource bundle. Menssagem a adicionada dentro deste método.
     *
     * @param detail Texto normal, apos a msg
     * @param clientId ID no JSF, um input text, slectItem...
     * @param msg Texto em negrito, antes do detail.
     * @param variantes Um array de objeto ara sobrescrever todos
     * {0}... da msg, pode ser null se não houver.
     * @param severity Severidade da MSG, INFO, ERROR, FATAL, WARN
     * @param currentJsf COntexto JSF
     * @return Retorna um texto do resource bundle em formato de
     * string com variações.
     */
    public static String messageFactoringFull(final String detail,
            final String clientId, final String msg,
            final Object[] variantes,
            final FacesMessage.Severity severity,
            final FacesContext currentJsf) {
        FacesMessage message = new FacesMessage();
        message.rendered();
        try {
            String msgPropert = getBundleVarArgs(msg,
                    currentJsf, variantes);
            message.setSummary(msgPropert);
        } catch (MissingResourceException e) {
            message.setSummary(msg);
        }
        if (severity != null) {
            message.setSeverity(severity);
        }
        if (detail == null) {
            message.setDetail("");
        } else {
            message.setDetail(": " + detail);
        }
        if (clientId != null) {
            currentJsf.addMessage(null, message);
        }
        currentJsf.addMessage(clientId, message);
        return message.getSummary();
    }

    /**
     * NAO TORNAR ESTE METODO PUBLICO! Ele faz o trabalho real de
     * busca no Messagem.properties e lança exceção se não encontrar,
     * podendo ter um comportamento inesperado na visão.
     *
     * @param msg Menssagem do arquivo messagens.properties
     * @param context Contexto JSF
     * @param var Variações de objetos, pode ser null se não houver.
     * @return Retorna um texto do resource bundle em formato de
     * string com variações.
     */
    private static String getBundleVarArgs(final String msg,
            final FacesContext context, final Object... var) {
        String toReturn = getResourceBundle(msg, context);
        if (var != null) {
            for (int i = 0; i < var.length; i++) {
                if (var[i] != null) {
                    toReturn = StringUtils.replace(toReturn,
                            "{" + i + "}", var[i].toString());
                }
            }
        }
        return toReturn;
    }

    /**
     * Procura uma String no resource e insere variações. Se não
     * encontrar retorna a msg enviada.
     *
     * @param msg Menssagem do arquivo messagens.properties
     * @param context Contexto JSF
     * @param var Variações de objetos
     * @return Retorna um texto do resource bundle em formato de
     * string com variações.
     */
    public static String getMsgVarArgs(final String msg,
            final FacesContext context, final Object[] var) {
        try {
            return getBundleVarArgs(msg, context, var);
        } catch (MissingResourceException e) {
            return msg;
        }
    }

    /**
     * Pega uma String do resource bundle com base em uma chave
     * informada.
     *
     * @param msg Menssagem do arquivo messagens.properties
     * @param current Contexto JSF
     * @return Retorna um texto do resource bundle em formato de
     * string
     */
    public static String getResourceBundle(final String msg,
            final FacesContext current) {
        try {
            String mBundle = current.getApplication().getMessageBundle();
            Locale locale = current.getViewRoot().getLocale();
            return ResourceBundle.getBundle(mBundle, locale).getString(msg);
        } catch (Exception ex) {
            return msg;
        }
    }

    /**
     * Passe um nome completo e ele retorna apenas os dois primeiros
     * nomes, caso o nome tenha um segundo nome muito curto, até 3
     * caracteres, ele retorna o 1º, 2º e 3º nome. Ex: Maria Dalva
     * Pereira Retorna: Maria Dalva, José dos Dias Silveira Retorna:
     * José dos Dias
     *
     * @param nomeCompleto Nome, sobrenome,..., ultimo nome
     * @return String com os dois primeiros nomes
     */
    public static String doisNomes(final String nomeCompleto) {
        int i = 0;
        int b = 0;
        int a = 0;
        String nome = null;
        for (int x = 0; x < nomeCompleto.length(); x++) {
            Character z = nomeCompleto.charAt(x);
            if (Character.isSpaceChar(z)) {
                if (a == 0) {
                    a = x;
                    i++;
                } else {
                    b = x;
                    i++;
                }
                //Caso o 2º Nome seja muito curto
                if (b != 0 && (b - a) <= 4) {
                    for (int j = b + 1; j < nomeCompleto.length(); j++) {
                        Character zz = nomeCompleto.charAt(j);
                        if (Character.isSpaceChar(zz)) {
                            x = j;
                            break;
                        } else if (j == nomeCompleto.length() - 1) {
                            x = j + 1;
                            break;
                        }
                    }
                }
            }
            if (i == 2) {
                nome = nomeCompleto.substring(0, x);
                return nome;
            }
        }
        return nomeCompleto;
    }
}
