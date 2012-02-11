/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import java.util.Locale;

/**
 *
 * @author gbvbahia
 */
public interface InterfaceManager extends java.io.Serializable {

    public void init();
    public void end();
    public Locale getLocale();
    public String getPattern();
}
