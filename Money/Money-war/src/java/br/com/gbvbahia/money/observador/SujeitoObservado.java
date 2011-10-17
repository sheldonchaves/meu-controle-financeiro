/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.observador;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Classe que armazena todas as classes que implementam
 * java.util.observer.
 * Sua unica utilização é para gerenciar os observers.
 * @author gbvbahia
 */
class SujeitoObservado implements ObservadorInterface {

    private List<Observer> observers = new ArrayList<Observer>();

    @Override
    public void addObserver(Observer observer) {
        if (!this.observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void executeUpdate(int[] args) {
        for (Observer obs : observers) {
            obs.update(null, args);
        }
    }
}
