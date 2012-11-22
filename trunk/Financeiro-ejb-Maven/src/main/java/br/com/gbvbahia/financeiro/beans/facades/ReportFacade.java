/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.facades;

import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import java.util.Date;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author Usuário do Windows
 */
@Local
public interface ReportFacade {
    /**
     * Retorna um map com os cartões e valores gastos dentro do periodo.
     * @param mesAno
     * @param usuario
     * @return 
     */
    public Map<CartaoCredito, Double> acumuladoCartaoPeriodo(Date mesAno,
            Usuario usuario);
}
