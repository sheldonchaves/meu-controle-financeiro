/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.observador;

import br.com.money.modelos.Usuario;
import java.util.Map;
import java.util.Observer;
import java.util.TreeMap;
import org.apache.commons.lang.NotImplementedException;

/**
 *  Classe responsável por armazenar e gerenciar todos as classes que implementam
 * java.util.Observer e notificam sua criação.
 * Um Map, privado, armazena como chave o ID dos usuários logados.
 * Seu objeto, para cada usuário, é uma classe SujeitoObservado, ou qualque outra classe que implemente ObservadorInterface
 * Todas as classes Observers instanciadas pelo usuário serão armazenadas dentro do respectivo SujeitoObservado de seu usuário, havendo
 * notificação de atualização de um usuário, somente seus observadores serão atualizados, atravéz do método notificaObservers.
 * Quando o usuário desconectar o método removeSessionClose será chamado para remover o usuário e seus objetos do Map.
 * @author gbvbahia
 */
public class ControleObserver {

    public static final Long id = -1l;
    private static Map<Long, ObservadorInterface> map = new TreeMap<Long, ObservadorInterface>();

    /**
     * Adiciona o usuario dentro do controle de Observadores, se ele já não estiver.
     * Adiciona a classe passada juntamente com o usuário realizando associação
     * entre os mesmos.
     * @param user
     * @param obs
     */
    public static void addBeanObserver(Usuario user, Observer obs) {
        if (user != null) {
            if (map.containsKey(user.getId())) {
                ObservadorInterface ob = map.get(user.getId());
                ob.addObserver(obs);
            } else {
                ObservadorInterface so = new SujeitoObservado();
                so.addObserver(obs);
                map.put(user.getId(), so);
            }
        }
    }

    /**
     * Remove o Observer associado ao usuário do conjunto de Observadores.
     * @param user
     * @param obs 
     */
    public static void removeBeanObserver(Usuario user, Observer obs) {
        if (user != null) {
            if (map.containsKey(user.getId())) {
                ObservadorInterface ob = map.get(user.getId());
                ob.removeObserver(obs);
            }
        }
    }

    /**
     * Para que não seja necessário que todos os observadores se atualizem
     * quando houver notificação, passe um array com os Eventos correspondentes as classes
     * que devem ser notificadas, elas irão fazer o check da string e irão se atualizar
     * se encontrarem seus eventos no array.
     * @param user
     * @param args
     */
    public static void notificaObservers(Usuario user, int... args) {
        if(args == null){
            throw new NotImplementedException("Um array de string deve ser passado, nem que seja vazio!");
        }
        if (user != null) {
            if (map.containsKey(user.getId())) {
                ObservadorInterface ob = map.get(user.getId());
                ob.executeUpdate(args);
            }
            //Avisa aos escopos da aplicação
            if (map.containsKey(ControleObserver.id)) {
                ObservadorInterface ob = map.get(ControleObserver.id);
                ob.executeUpdate(args);
            }
        }
    }

    /**
     * Quando a sessão é terminada o usuário deve ser removido
     * com todos os observadores.
     * Utilizado por br.com.saber.certificacao.listeners.SessionListener
     * @param user 
     */
    public static void removeSessionClose(Usuario user) {
        if (user != null) {
            map.remove(user.getId());
        }
    }

    /**
     * Selecione quais Observadores deseja que se atualizem, passando essas string
     * dentro do array como argumento no método notificaObservers
     */
    public static class Eventos {

        public static final int CAD_DETALHE_MOVIMENTACAO = 1000;
        
    }
}
