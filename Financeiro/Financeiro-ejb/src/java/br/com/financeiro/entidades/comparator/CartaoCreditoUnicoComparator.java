/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.entidades.comparator;

import br.com.financeiro.entidades.CartaoCreditoUnico;
import java.util.Comparator;

/**
 * Utilizado para comparar Cartão Titular com Cartão dependete.
 * @author gbvbahia
 */
public class CartaoCreditoUnicoComparator implements Comparator<CartaoCreditoUnico>{

    public int compare(CartaoCreditoUnico o1, CartaoCreditoUnico o2) {
        if(o1.getCartaoCreditoTitular() != null && o2.getCartaoCreditoTitular() == null){
            return -1;
        }
        if(o2.getCartaoCreditoTitular() != null && o1.getCartaoCreditoTitular() == null){
            return 1;
        }
        return o1.compareTo(o2);
    }


}
