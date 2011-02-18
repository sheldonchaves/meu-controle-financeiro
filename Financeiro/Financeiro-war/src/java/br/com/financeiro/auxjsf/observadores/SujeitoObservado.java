/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.observadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 *
 * @author gbvbahia
 */
   class SujeitoObservado implements ObservadorInterface {

        private List<Observer> observers = new ArrayList<Observer>();

        public void addObserver(Observer observer) {
            if (!this.observers.contains(observer)) {
                observers.add(observer);
            }
        }

        public void removeObserver(Observer observer) {
            this.observers.remove(observer);
        }

        public void executeUpdate() {
            for (Observer obs : observers) {
                obs.update(null, null);
            }
        }
    }
