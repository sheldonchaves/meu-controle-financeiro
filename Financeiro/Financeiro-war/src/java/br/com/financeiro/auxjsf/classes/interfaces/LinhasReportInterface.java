/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.classes.interfaces;

import java.util.List;

/**
 *
 * @author gbvbahia
 */
public interface LinhasReportInterface {

    public void addTipoValor(List<TipoValorInterface> tipoValor);

    public List<List<TipoValorInterface>> getTipoValores();
}
