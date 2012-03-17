/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.modelos.Usuario;
import java.util.List;

/**
 *
 * @author Guilherme
 */
public interface AbstractFacadeLocal<T> {

    int count();
    @Deprecated//Até ser refatorado para vaidar
    void create(T entity);
    @Deprecated//Até ser refatorado para vaidar
    void update(T entity);

    T find(Object id);

    List<T> findAll();

    List<T> findRange(int[] range);
    List<T> findRange(int[] range, Usuario usuarioProprietario);
    @Deprecated//Até ser refatorado para vaidar
    void remove(T entity);
    
}
