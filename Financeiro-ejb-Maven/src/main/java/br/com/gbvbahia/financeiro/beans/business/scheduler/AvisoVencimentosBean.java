/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.scheduler;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.business.interfaces.AvisoVencimentosBusiness;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.SchedulerFacade;
import br.com.gbvbahia.financeiro.beans.jms.interfaces.EmailSendBusiness;
import br.com.gbvbahia.financeiro.beans.jms.sends.SimpleEmail;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.DespesaParceladaProcedimento;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Scheduler;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.financeiro.utils.NumberUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.interceptor.Interceptors;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Guilherme
 */
@Stateless
@RunAs("SYSTEM")
@Interceptors({LogTime.class})
public class AvisoVencimentosBean implements AvisoVencimentosBusiness {

    @EJB
    private SchedulerFacade schedulerBean;
    @EJB
    private ProcedimentoFacade procedimentoFacade;
    @EJB
    private EmailSendBusiness emailSendBusiness;

    @Schedule(hour = "*", minute = "*", second="10", dayOfWeek = "*")
    public void iniciarAvisoVencimento(Timer timer) {
        List<Scheduler> schedules = schedulerBean.buscarTodosSchelersPorStatus(true);
        Calendar[] intervalo = getIntervalo();
        for (Scheduler sc : schedules) {
            List<DespesaProcedimento> dividas = this.procedimentoFacade.
                    buscarDespesaIntervalo(sc.getUser(), null, StatusPagamento.NAO_PAGA,
                    new Date[]{intervalo[0].getTime(), intervalo[1].getTime()});
            List[] contasDivididas = separarContas(dividas, sc);
            List<DespesaProcedimento> contasAtrasadas = contasDivididas[0];
            List<DespesaProcedimento> contasOK = contasDivididas[1];
            String body = corpoTableEmail(contasOK, contasAtrasadas);
            body = StringUtils.replace(body, "null", "");
            if (body != null && !body.trim().equals("") && !body.trim().equals("null")) {
                String bodyFim = "<h3>Lembrete de Proximos Vencimentos:</h3>" + body;
                enviaEmail(bodyFim, "Financeiro :: Aviso de Vencimentos", sc);
            }
        }
    }

    private Calendar[] getIntervalo() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Calendar d = Calendar.getInstance();
        d.add(Calendar.DAY_OF_MONTH, 6);
        return new Calendar[]{c, d};
    }

    private List[] separarContas(List<DespesaProcedimento> receitaDividas, Scheduler sc) {
        List[] contasLists = new List[2];
        List<DespesaProcedimento> contasAtrasadas = new ArrayList<DespesaProcedimento>();
        List<DespesaProcedimento> contasOK = new ArrayList<DespesaProcedimento>();
        for (DespesaProcedimento cp : receitaDividas) {
            String tempo = this.verificaTempo(sc, cp);
            if (tempo == null) {
                //faz nada
            } else if (tempo.equals("OK")) {
                contasOK.add(cp);
            } else if (tempo.equals("NOK")) {
                contasAtrasadas.add(cp);
            }
        }
        contasLists[0] = contasAtrasadas;
        contasLists[1] = contasOK;
        return contasLists;
    }

    private String verificaTempo(Scheduler ants, DespesaProcedimento cp) {
        Calendar agora = Calendar.getInstance();
        agora.setTime(cp.getDataCartao() == null ? cp.getDataMovimentacao() : cp.getDataCartao());
        if (Calendar.getInstance().after(agora)) {
            return "NOK";
        }
        for (int i = 0; i <= ants.getDias(); i++) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, i);
            if (comparaDiaMesAno(c, agora)) {
                return "OK";
            }
        }
        return null;
    }

    private boolean comparaDiaMesAno(Calendar init, Calendar fim) {
        int diaI = init.get(Calendar.DAY_OF_MONTH);
        int mesI = init.get(Calendar.MONTH);
        int anoI = init.get(Calendar.YEAR);
        int diaF = fim.get(Calendar.DAY_OF_MONTH);
        int mesF = fim.get(Calendar.MONTH);
        int anoF = fim.get(Calendar.YEAR);
        if (diaI == diaF && mesI == mesF && anoI == anoF) {
            return true;
        }
        return false;
    }

    private String corpoTableEmail(List<DespesaProcedimento> listCPOK, List<DespesaProcedimento> listCPAtrasada) {
        String toReturn = "";
        toReturn += buscarTablePadrao(toReturn);
        toReturn += "<thead><tr>"
                + "<th>Observação</th>"
                + "<th>Vencimento</th>"
                + "<th>Valor</th>"
                + "<th>Detalhe</th>"
                + "<th>Parcela Atual</th>"
                + "<th>Total Parcelas</th>"
                + "<th>Responsável</th>"
                + "</tr></thead>"
                + "<tbody>";
        for (DespesaProcedimento cp : listCPAtrasada) {
            toReturn += "<tr>";
            toReturn += "<td align='left' class=\"red\">" + cp.getObservacao() + "</td>";
            toReturn += "<td align='center' class=\"red\">" + DateUtils.getDataFormatada(cp.getDate(), "dd/MMM/yyyy") + "</td>";
            toReturn += "<td align='right' class=\"red\">" + NumberUtils.currencyFormat(cp.getValor().doubleValue()) + "</td>";
            toReturn += "<td align='center' class=\"red\">" + cp.getDetalhe().getDetalhe() + "</td>";
            if (cp instanceof DespesaParceladaProcedimento) {
                DespesaParceladaProcedimento dpp = (DespesaParceladaProcedimento) cp;
                toReturn += "<td align='center' class=\"red\">" + dpp.getParcelaAtual().toString() + "</td>";
                toReturn += "<td align='center' class=\"red\">" + dpp.getParcelaTotal().toString() + "</td>";
            } else {
                toReturn += "<td align='center' class=\"red\"> 0 </td>";
                toReturn += "<td align='center' class=\"red\"> 0 </td>";
            }
            toReturn += "<td align='center' class=\"red\">" + cp.getUsuario().getFirstName() + "</td>";
            toReturn += "</tr>";
        }
        for (DespesaProcedimento cp : listCPOK) {
            toReturn += "<tr>";
            toReturn += "<td align='left'>" + cp.getObservacao() + "</td>";
            toReturn += "<td align='center'>" + DateUtils.getDataFormatada(cp.getDate(), "dd/MMM/yyyy") + "</td>";
            toReturn += "<td align='right'>" + NumberUtils.currencyFormat(cp.getValor().doubleValue()) + "</td>";
            toReturn += "<td align='center'>" + cp.getDetalhe().getDetalhe() + "</td>";
            if (cp instanceof DespesaParceladaProcedimento) {
                DespesaParceladaProcedimento dpp = (DespesaParceladaProcedimento) cp;
                toReturn += "<td align='center'>" + dpp.getParcelaAtual().toString() + "</td>";
                toReturn += "<td align='center'>" + dpp.getParcelaTotal().toString() + "</td>";
            } else {
                toReturn += "<td align='center'> 0 </td>";
                toReturn += "<td align='center'> 0 </td>";
            }
            toReturn += "<td align='center'>" + cp.getUsuario().getFirstName() + "</td>";
            toReturn += "</tr>";
        }
        toReturn += "</tbody></table>";
        return toReturn;
    }

    private String buscarTablePadrao(String body) {
        body += " <style type=\"text/css\">";
        body += " table.reference td.red {color: red; }"
                + " table.reference td.green{color: #B2D1FF;}";
        body += " table.reference { ";
        body += " background-color:#ffffff;";
        body += " border:1px solid #c3c3c3;";
        body += " border-collapse:collapse;";
        body += " width:95%;}";
        body += " table.reference th { ";
        body += " background-color:#ba4c32;";
        body += " color:#ffffff;";
        body += " border:1px solid #c3c3c3;";
        body += " padding:3px;";
        body += " vertical-align:top; }";
        body += " table.reference td { ";
        body += " border:1px solid #c3c3c3;";
        body += " padding:3px;";
        body += " vertical-align:top; } </style>";
        body += " <table class=\"reference\">";
        return body;
    }

    private void enviaEmail(String body, String titulo, Scheduler lc) {
        SimpleEmail se = new SimpleEmail();
        se.setBody(body);
        se.setEmail(lc.getEmail());
        se.setSubject(titulo);
        emailSendBusiness.enviarEmailJMS(se);
    }
}
