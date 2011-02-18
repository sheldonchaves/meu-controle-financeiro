/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.observadores;

import java.util.Observer;

/**
 *
 * @author gbvbahia
 */
public interface ObservadorInterface {

    void addObserver(Observer observer);

    void executeUpdate();

    void removeObserver(Observer observer);

}
