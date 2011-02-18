/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.classes;

import br.com.financeiro.auxjsf.classes.interfaces.LinhasReportInterface;
import br.com.financeiro.auxjsf.classes.interfaces.TipoValorInterface;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gbvbahia
 */
public class LinhasReportHelper implements LinhasReportInterface{

    private List<List<TipoValorInterface>> tipoValorInterfaces;

    @Override
    public void addTipoValor(List<TipoValorInterface> tipoValor) {
        getTipoValores().add(tipoValor);
    }

    @Override
    public List<List<TipoValorInterface>> getTipoValores() {
        if(this.tipoValorInterfaces == null) this.tipoValorInterfaces = new ArrayList<List<TipoValorInterface>>();
        return tipoValorInterfaces;
    }

    public void setTipoValores(List<List<TipoValorInterface>> tipoValorInterfaces) {
        this.tipoValorInterfaces = tipoValorInterfaces;
    }


}
