/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.gbvbahia.money.observador;

import java.util.Observer;

/**
 *
 * @author gbvbahia
 */
public interface ObservadorInterface {

    void addObserver(Observer observer);

    void executeUpdate(int[] args);

    void removeObserver(Observer observer);

}
