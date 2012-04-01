/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.utils;

import java.util.logging.Level;

/**
 *
 * Classe auxiliar com métodos estaticos para auxiliar em tarefas que se
 * reptem ao longo da camada de negócio, tornando tarefas repetitivas
 * centralizadas e sem "sujar" o código de negócio propriamente dito.
 *
 * @since 2012/02/22
 * @author Guilherme
 */
public class UtilBeans {

        /**
     * Definie o nível de log para toda aplicação
     */
    public static final Level LEVEL_LOG = Level.INFO;
    
    public static final String PERSISTENCE_UNIT = "moneyT";
}
