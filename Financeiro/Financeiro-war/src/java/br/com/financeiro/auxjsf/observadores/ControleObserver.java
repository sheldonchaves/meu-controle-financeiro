/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.observadores;

import br.com.financeiro.entidades.User;
import java.util.Map;
import java.util.Observer;
import java.util.TreeMap;

/**
 * Classe estatica que contem um map de todos os usuários logados (key) com uma classe SujeitoObservador (valor).
 * Sujeito observador contém uma Lista de interface Observer (java.util).
 * Quando for realizado uma alteração em contas, saldos, ou qualquer tipo de movimentação financeira
 * esse ControleObserver irá atualizar as informações de todos os beans de Sessão.
 * Não é necessário atualizar os beans de requisição, já que sempre os mesmos irá buscar a informação sempre que forem criados.
 * @author gbvbahia
 */
public class ControleObserver {

    private static Map<Integer, ObservadorInterface> map = new TreeMap<Integer, ObservadorInterface>();

    public static void addBeanObserver(User user, Observer obs) {
        if (user != null) {
            if (map.containsKey(user.getId())) {
                ObservadorInterface ob = map.get(user.getId());
                ob.addObserver(obs);
            } else {
                SujeitoObservado so = new SujeitoObservado();
                so.addObserver(obs);
                map.put(user.getId(), so);
            }
        }
    }

    public static void removeBeanObserver(User user, Observer obs) {
        if (user != null) {
            if (map.containsKey(user.getId())) {
                ObservadorInterface ob = map.get(user.getId());
                ob.removeObserver(obs);
            }
        }
    }

    public static void notificaObservers(User user) {
        if (user != null) {
            if (map.containsKey(user.getId())) {
                ObservadorInterface ob = map.get(user.getId());
                ob.executeUpdate();
            }
        }
    }

    public static void removeSessionClose(User user) {
        if (user != null) {
            map.remove(user.getId());
        }
    }
}
