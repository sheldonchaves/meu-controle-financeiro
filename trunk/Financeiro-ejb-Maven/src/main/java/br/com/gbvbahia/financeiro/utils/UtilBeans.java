/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.utils;

import java.util.logging.Level;

/**
 *
 * Classe auxiliar com métodos estaticos para auxiliar em tarefas que
 * se reptem ao longo da camada de negócio, tornando tarefas
 * repetitivas centralizadas e sem "sujar" o código de negócio
 * propriamente dito.
 *
 * @since 2012/02/22
 * @author Guilherme
 */
public final class UtilBeans {

    /**
     * Não pode ser instaciada.
     */
    private UtilBeans() {
    }
    /**
     * Definie o nível de log para toda aplicação.
     */
    public static final Level LEVEL_LOG = Level.INFO;
    /**
     * Unidade de persistência da aplicação.
     */
    public static final String PERSISTENCE_UNIT = "money";

    /**
     * Valida os parâmetros para que se algum for nulo lance uma
     * IllegalArgumentException
     *
     * @param parametro Parâmetros a serem checados.
     */
    public static void checkNull(Object... parametro) {
        for (int i = 0; i < parametro.length; i++) {
            if (parametro[i] == null) {
                throw new IllegalArgumentException(
                        I18N.getMsg("parametrosObrigatorios"));
            }

        }
    }
}
