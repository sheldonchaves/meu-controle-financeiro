/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

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
    void edit(T entity);

    T find(Object id);

    List<T> findAll();

    List<T> findRange(int[] range);
    @Deprecated//Até ser refatorado para vaidar
    void remove(T entity);
    
}
