/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.logger;

import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Usuário do Windows
 */
public class I18nLogger {
    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private static Logger logger = Logger.getLogger(I18nLogger.class);
    /**
     * Construtor privado, classe não pode ser instânciada.
     */
    private I18nLogger() {
    }

    /**
     * Retira o valor do arquivo:
     * utils.I18n.properties, onde todas as mensagens
     * que são enviadas ao usuário ficam armazenadas.
     *
     * @param chave java.lang.String chave da mensagem que será enviada.
     * @return java.lang.String a ser utilizada para a chave passada, caso
     * ocorra algum erro ou a chave não exista, será retornado o mesmo valor
     * passado como parâmetro chave.
     */
    public static String getMsg(final String chave) {
        try {
            return ResourceBundle.getBundle("i18n/logger").getString(chave);
        } catch (Exception e) {
            logger.warn("Menssagem não encontrada para " + chave);
            return chave;
        }
    }

    /**
     * Retira o valor do arquivo:
     * utils.I18n.properties, onde todas as mensagens
     * que são enviadas ao usuário ficam armazenadas. Este substitui todos {*}
     * pela posição correspondente no vararg.
     *
     * @param msg Messagem do Resource.
     * @param var Variações da mensagem.
     * @return Menssagem formada.
     */
    public static String getMsg(final String msg, final Object... var) {
        String toReturn = getMsg(msg);
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
}
