/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.ReportBeanLocal;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.modelos.Usuario;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Guilherme
 */
@Stateless
public class ReportBean implements ReportBeanLocal {

    @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;

    /**
     * Retorna os valores somados de gasto por periodo agrupado por tipo de movimentação.<br>
     * RETIRADA("Pagamento"), 
     * DEPOSITO("Receita");
     * @param mesAno
     * @param usuario
     * @return 
     */
    @Override
    public Map<TipoMovimentacao, Double> acumuladoMes(Date mesAno, Usuario usuario) {
        Date[] periodo = inicialFinal(dateToCalendar(mesAno));
        Query q = manager.createNamedQuery("ReportBean.acumuladoMes");
        q.setParameter("dataI", periodo[0]);
        q.setParameter("dataF", periodo[1]);
        q.setParameter("usuario", usuario);
        List<Object[]> valores = q.getResultList();
        Map<TipoMovimentacao, Double> toReturn = new EnumMap<TipoMovimentacao, Double>(TipoMovimentacao.class);
        for(Object[] valor : valores){
            toReturn.put((TipoMovimentacao)valor[0],(Double) valor[1]);
        }
        return toReturn;
    }

    private Calendar dateToCalendar(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }
    
    private Date[] inicialFinal(Calendar mesAno) {
        Date[] toReturn = new Date[2];
        mesAno.set(Calendar.DAY_OF_MONTH, 1);
        toReturn[0] = mesAno.getTime();
        mesAno.set(Calendar.DAY_OF_MONTH, mesAno.getActualMaximum(Calendar.DAY_OF_MONTH));
        toReturn[1] = mesAno.getTime();
        return toReturn;
    }
}
