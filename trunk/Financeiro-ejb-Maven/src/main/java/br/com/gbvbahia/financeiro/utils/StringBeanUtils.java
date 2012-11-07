/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.utils;

import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

/**
 * @since v.3 29/03/2012
 * @author Guilherme
 */
public final class StringBeanUtils {

    /**
     * Define a variação da String para LIKE fixa no final e variavel
     * no inicio. Ex: "%aria"
     */
    public static final int LIKE_START = 0;
    /**
     * Define a variação da String para LIKE fixa no inicio e variavel
     * no final. Ex: "Mar%"
     */
    public static final int LIKE_END = 1;
    /**
     * Define a variação da String para LIKE variavel no inicio e
     * variavel no final. Ex: "%ari%"
     */
    public static final int LIKE_MIDDLE = 2;

    /**
     * Não pode ser instânciada.
     */
    private StringBeanUtils() {
    }

    /**
     * Acerta o parametro para null ou concatena com % para ser
     * incluido em consultas do tipo: <i>(:nome is null or
     * upper(o.nome) like upper(:nome))</i>.
     *
     * @param string String a ser alterada.
     * @param likeType Utilizar as constantes:<br>
     * UtilBeans.StringBeanUtils.LIKE_START<br>
     * UtilBeans.StringBeanUtils.LIKE_END<br>
     * UtilBeans.StringBeanUtils.LIKE_MIDDLE<br>
     * @return java.lang.String ou null
     */
    public static String acertaNomeParaLike(final String string,
            final int likeType) {
        if (StringUtils.isBlank(string)) {
            return null;
        } else {
            switch (likeType) {
                case LIKE_START:
                    return "%" + string;
                case LIKE_MIDDLE:
                    return "%" + string + "%";
                case LIKE_END:
                    return string + "%";
                default:
                    return string;
            }

        }
    }

    /**
     * Retorna uma String que pode ser utilizada como único
     * identificador.
     *
     * @param login Um identificador do usuário proprietario. Não pode
     * ser nulo.
     * @param data Uma data para casar com o usuário, será olhado até
     * os milesimos desta data, garantindo 99,999% de confiabilidade
     * na unicidade.<br> Não pode ser nula.
     * @return String formato: 2-00000133-76d1-7b80-0000-01337a76f1f9
     */
    public static synchronized String getIdentificadorUnico(
            final String login, final Date data) {
        if (data == null) {
            throw new IllegalArgumentException(I18N.getMsg("idDataInvalida"));
        }
        if (login == null) {
            throw new IllegalArgumentException(I18N.getMsg("idUserInvalido"));
        }
        UUID uuid = new UUID(data.getTime(), new Date().getTime());
        String toReturn = StringUtils.substring(login, 0, 5)
                + "-" + uuid.toString();
        return toReturn;
    }
}
