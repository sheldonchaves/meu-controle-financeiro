/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.classes.interfaces;

import java.util.Date;
import java.util.List;

/**
 *
 * @author gbvbahia
 */
public interface AcumuladoInterface {

    public boolean isMesAtual();
    public Date getMesAno();
    public List<TipoValorInterface> getTipoValorInterface();
    public void add(TipoValorInterface tipoValor);
    public int getMes();
    public int getAno();
}
