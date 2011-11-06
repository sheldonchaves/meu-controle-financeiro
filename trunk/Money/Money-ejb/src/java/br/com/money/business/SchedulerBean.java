/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.business.interfaces.SchedulerBeanLocal;
import br.com.money.business.jms.jmsEmailUtilitarios.SimpleEmail;
import br.com.money.business.jms.jmsEmailUtilitarios.interfaces.EmailSendLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.modelos.Scheduler;
import br.com.money.modelos.Usuario;
import br.com.money.utils.UtilBeans;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Guilherme
 */
@Stateless
public class SchedulerBean implements SchedulerBeanLocal {

    @EJB
    private ReceitaDividaBeanLocal receitaDividaBean;
    @EJB(beanName = "schedulerValidador")
    private ValidadorInterface schedulerValidador;
    @EJB
    private EmailSendLocal emailSendBean;
    @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;

    @Override
    public void salvarScheduler(Scheduler scheduler) throws ValidacaoException {
        schedulerValidador.validar(scheduler, this, null);
        if (scheduler.getId() == null) {
            manager.persist(scheduler);
        } else {
            manager.merge(scheduler);
        }
        manager.flush();
    }

    @Override
    public Scheduler buscarSchedulerPorUsuario(Usuario usuario) {
        Query q = manager.createNamedQuery("SchedulerBean.buscarSchedulerPorUsuario");
        q.setParameter("user", usuario);
        try {
            return (Scheduler) q.getSingleResult();
        } catch (NoResultException nr) {
            return null;
        }
    }

    @Override
    public List<Scheduler> buscarTodosSchelersPorStatus(boolean status) {
        Query q = manager.createNamedQuery("SchedulerBean.buscarTodosSchelersPorStatus");
        q.setParameter("status", status);
        return q.getResultList();
    }

    @Schedule(hour = "3", minute = "23", dayOfWeek = "*")
    public void iniciarAvisoVencimento(Timer timer) {
        List<Scheduler> schedules = buscarTodosSchelersPorStatus(true);
        Calendar[] intervalo = getIntervalo();
        for (Scheduler sc : schedules) {
            List<ReceitaDivida> dividas = this.receitaDividaBean.buscarReceitaDividasPorDataUsuarioStatusTipoMovimentacao(intervalo[0].getTime(),
                    intervalo[1].getTime(), sc.getUser(), StatusPagamento.NAO_PAGA, TipoMovimentacao.RETIRADA);
            List[] contasDivididas = separarContas(dividas, sc);
            List<ReceitaDivida> contasAtrasadas = contasDivididas[0];
            List<ReceitaDivida> contasOK = contasDivididas[1];
            String body = "";
            if (!contasAtrasadas.isEmpty()) {
                body += corpoTableEmail(contasAtrasadas, true);
            }
            if (!contasOK.isEmpty()) {
                body += corpoTableEmail(contasOK, false);
            }
            body = StringUtils.replace(body, "null", "");
            if (body != null && !body.trim().equals("") && !body.trim().equals("null")) {
                String bodyFim = "<h3>Lembrete de Proximos Vencimentos:</h3>" + body;
                enviaEmail(bodyFim, sc);
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

    private List[] separarContas(List<ReceitaDivida> receitaDividas, Scheduler sc) {
        List[] contasLists = new List[2];
        List<ReceitaDivida> contasAtrasadas = new ArrayList<ReceitaDivida>();
        List<ReceitaDivida> contasOK = new ArrayList<ReceitaDivida>();
        for (ReceitaDivida cp : receitaDividas) {
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

    private String verificaTempo(Scheduler ants, ReceitaDivida cp) {
        Calendar agora = Calendar.getInstance();
        agora.setTime(cp.getDataVencimento());
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

    private String corpoTableEmail(List<ReceitaDivida> listCP, boolean atrasada) {
        String toReturn = atrasada ? "<h4>Contas Vencidas</h4>" : "<h4>Contas à Vencer</h4>";
        toReturn += "<table> "
                + "<thead><tr>"
                + "<th>Observação</th>"
                + "<th>Vencimento</th>"
                + "<th>Valor</th>"
                + "<th>Detalhe</th>"
                + "<th>Parcela Atual</th>"
                + "<th>Total Parcelas</th>"
                + "<th>Responsável</th>"
                + "</tr></thead>"
                + "<tbody>";
        for (ReceitaDivida cp : listCP) {
            toReturn += "<tr>";
            toReturn += "<td align='left'>" + cp.getObservacao() + "</td>";
            toReturn += "<td align='center'>" + UtilBeans.getDataString(cp.getDataVencimento()) + "</td>";
            toReturn += "<td align='right'>" + UtilBeans.currencyFormat(cp.getValor()) + "</td>";
            toReturn += "<td align='center'>" + cp.getDetalheMovimentacao().getDetalhe() + "</td>";
            toReturn += "<td align='center'>" + cp.getParcelaAtual().toString() + "</td>";
            toReturn += "<td align='center'>" + cp.getParcelaTotal().toString() + "</td>";
            toReturn += "<td align='center'>" + cp.getUsuario().getFirstName() + "</td>";
            toReturn += "</tr>";
        }
        toReturn += "</tbody></table>";
        return toReturn;
    }

    private void enviaEmail(String body, Scheduler lc) {
        SimpleEmail se = new SimpleEmail();
        se.setBody(body);
        se.setEmail(lc.getEmail());
        se.setSubject("Financeiro :: Aviso de Vencimentos");
        emailSendBean.enviarEmailJMS(se);
    }
}
