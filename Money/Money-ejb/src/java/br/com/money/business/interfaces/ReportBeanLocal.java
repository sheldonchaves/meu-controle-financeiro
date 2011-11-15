/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.interfaces;

import br.com.money.enums.TipoMovimentacao;
import br.com.money.modelos.Usuario;
import java.util.Date;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface ReportBeanLocal {
     /**
     * Retorna os valores somados de gasto por periodo agrupado por tipo de movimentação.<br>
     * RETIRADA("Pagamento"), 
     * DEPOSITO("Receita");
     * @param mesAno
     * @param usuario
     * @return 
     */
    public Map<TipoMovimentacao, Double> acumuladoMes(Date mesAno, Usuario usuario);
}
