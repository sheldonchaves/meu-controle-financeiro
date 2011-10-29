/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.money.enums.TipoConta;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.model.SelectItem;
import java.util.*;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "selectItemManager", eager = true)
@NoneScoped
public class SelectItemManager {

    /** Creates a new instance of SelectItemManager */
    public SelectItemManager() {
    }
    
        /**
     * Converte a enum Linguagem em uma lista de SelectItems
     * @return List de SelectItem
     */
    public List<SelectItem> getLinguagens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (TipoConta lin : TipoConta.values()) {
            toReturn.add(new SelectItem(lin, lin.getTipoContaString()));
        }
        return toReturn;
    }
}
