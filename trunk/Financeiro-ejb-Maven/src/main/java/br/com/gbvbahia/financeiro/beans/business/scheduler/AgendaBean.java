/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.scheduler;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.business.interfaces.AgendaBusiness;
import br.com.gbvbahia.financeiro.beans.business.interfaces.ProvisaoBusiness;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import java.util.List;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.apache.log4j.Logger;

/**
 *
 * @author Usuário do Windows
 */
@Stateless
@Interceptors({LogTime.class})
@RunAs("sys")
public class AgendaBean implements AgendaBusiness {

    private static final int INTERVALO = 3;
    
    private Logger logger = Logger.getLogger(AgendaBean.class);
    @EJB
    private ProvisaoBusiness provisaoBean;
    @EJB
    private AgendaProcedimentoFixoFacade agendaProcedimentoFixoBean;

    @Override
    //@Schedule(hour = "*", minute = "*", second = "10", dayOfWeek = "*")//Teste
    @Schedule(hour = "6", minute = "23", second = "10", dayOfWeek = "*")//Real
    public void provisionarAgendas() {
        logger.info("AgendaBean.provisionarAgendas: Schedule INICIADO");
        int ini = 0;
        int fim = INTERVALO;
        List<AgendaProcedimentoFixo> agendas;
        do {
            agendas = agendaProcedimentoFixoBean.buscarAgendasPorStatus(true, new int[]{ini, fim});
            ini = fim + 1;
            fim += INTERVALO;
            for (AgendaProcedimentoFixo agenda : agendas) {
                try {
                    provisaoBean.provisionar(agenda);
                } catch (Exception e) {
                    logger.error("AgendaBean.provisionarAgendas: Schedule ERRO:", e);
                }
            }
        } while (!agendas.isEmpty());
        logger.info("AgendaBean.provisionarAgendas: Schedule TERMINADO");
    }
}
