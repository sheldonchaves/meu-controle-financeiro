package br.com.gbvbahia.financeiro.utils;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Classe de Internacionalização para os EJB´s.
 * @since v.3 29/03/2012
 * @author Guilherme
 */
public final class I18N {

    /**
     * Construtor privado, classe não pode ser instânciada.
     */
    private I18N() {
    }

    /**
     * Retira o valor do arquivo:
     * br.com.convergeti.solida.utils.I18n.properties, onde todas as
     * mensagens que são enviadas ao usuário ficam armazenadas.
     *
     * @param chave java.lang.String chave da mensagem que será
     * enviada.
     * @return java.lang.String a ser utilizada para a chave passada,
     * caso ocorra algum erro ou a chave não exista, será retornado o
     * mesmo valor passado como parâmetro chave.
     */
    public static String getMsg(final String chave) {
        try {
            return ResourceBundle.getBundle("Messagens").getString(chave);
        } catch (Exception e) {
            Logger.getLogger(I18N.class.getName()).log(Level.INFO,
                    "Menssagem não encontrada para {0}", new Object[]{chave});
            return chave;
        }
    }

    /**
     * Retira o valor do arquivo:
     * br.com.convergeti.solida.utils.I18n.properties, onde todas as
     * mensagens que são enviadas ao usuário ficam armazenadas. Este
     * substitui todos {*} pela posição correspondente no vararg.
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
