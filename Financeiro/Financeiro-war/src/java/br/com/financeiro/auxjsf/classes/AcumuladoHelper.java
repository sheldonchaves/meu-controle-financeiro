/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.classes;

import br.com.financeiro.auxjsf.classes.interfaces.AcumuladoInterface;
import br.com.financeiro.auxjsf.classes.interfaces.TipoValorInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author gbvbahia
 */
public class AcumuladoHelper implements AcumuladoInterface {

    private Date mesAno;
    private boolean mesAtual;
    private List<TipoValorInterface> tipoValor;
    
    public boolean isMesAtual() {
        return mesAtual;
    }

    public void setMesAtual(boolean mesAtual) {
        this.mesAtual = mesAtual;
    }

    public void setMesAno(Date mesAno) {
        this.mesAno = mesAno;
    }

    public Date getMesAno() {
        return this.mesAno;
    }

    public void add(TipoValorInterface tipoValor) {
        this.getTipoValorInterface().add(tipoValor);
    }

    public List<TipoValorInterface> getTipoValorInterface() {
        return this.getTipoValor();
    }

    public List<TipoValorInterface> getTipoValor() {
        if(this.tipoValor == null) this.tipoValor = new ArrayList<TipoValorInterface>();
        return tipoValor;
    }

    public void setTipoValor(List<TipoValorInterface> tipoValor) {
        this.tipoValor = tipoValor;
    }

    public int getMes(){
        Calendar c = Calendar.getInstance();
        c.setTime(mesAno);
        return c.get(Calendar.MONTH);
    }

    public int getAno(){
        Calendar c = Calendar.getInstance();
        c.setTime(mesAno);
        return c.get(Calendar.YEAR);
    }

}
