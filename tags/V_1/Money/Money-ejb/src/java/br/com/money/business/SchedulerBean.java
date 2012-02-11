/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.business.interfaces.SchedulerBeanLocal;
import br.com.money.business.jms.jmsEmailUtilitarios.SimpleEmail;
import br.com.money.business.jms.jmsEmailUtilitarios.interfaces.EmailSendLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoConta;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.MovimentacaoFinanceira;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.modelos.Scheduler;
import br.com.money.modelos.Usuario;
import br.com.money.utils.UtilBeans;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
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
    private MovimentacaoFinanceiraBeanLocal movimentacaoFinanceiraBean;
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
            String body = corpoTableEmail(contasOK, contasAtrasadas);
            body = StringUtils.replace(body, "null", "");
            if (body != null && !body.trim().equals("") && !body.trim().equals("null")) {
                String bodyFim = "<h3>Lembrete de Proximos Vencimentos:</h3>" + body;
                enviaEmail(bodyFim, "Financeiro :: Aviso de Vencimentos", sc);
            }
        }
    }

    /**
     * @param timer 
     */
    @Schedule(hour = "10", minute = "45", dayOfWeek = "Mon,Fri")//loop infinito
    public void avisarCartaoCredito(Timer timer) {
            List<Scheduler> schedules = buscarTodosSchelersPorStatus(true);
            Calendar[] intervalo = primeiroUltimoDiasMes();
            for (Scheduler sc : schedules) {
                List<MovimentacaoFinanceira> movimentacoes = this.movimentacaoFinanceiraBean.buscarMovimentacaoFinanceiraPorUsuarioPeriodo(sc.getUser(), TipoConta.CARTAO_DE_CREDITO, intervalo[0].getTime(), intervalo[1].getTime());
                movimentacoes.addAll(this.movimentacaoFinanceiraBean.buscarMovimentacaoFinanceiraPorUsuarioPeriodo(sc.getUser(), TipoConta.CARTAO_DE_CREDITO, intervalo[2].getTime(), intervalo[3].getTime()));
                Map<CartaoMesDTO, Double> contaValorMap = new TreeMap<CartaoMesDTO, Double>();
                for (MovimentacaoFinanceira mf : movimentacoes) {
                    CartaoMesDTO dto = new CartaoMesDTO(mf.getContaBancariaDebitada(), UtilBeans.mesAnoData(mf.getReceitaDivida().getDataVencimento()));
                    if (contaValorMap.containsKey(dto)) {
                        contaValorMap.put(dto, contaValorMap.get(dto) + mf.getReceitaDivida().getValorParaCalculoDireto());
                    } else {
                        contaValorMap.put(dto, mf.getReceitaDivida().getValorParaCalculoDireto());
                    }
                }
                if (!contaValorMap.isEmpty()) {
                    String body = defineCartaoValor(contaValorMap);
                    body = StringUtils.replace(body, "null", "");
                    if (body != null && !body.trim().equals("") && !body.trim().equals("null")) {
                        String bodyFim = "<h3>Aviso semanal de cartão de crédito:</h3>" + body;
                        enviaEmail(bodyFim, "Financeiro :: Cartão de Crédito", sc);
                    }
                }
            }
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Scheduler Cartão de Crédito: Executado.");
        }

    private String buscarTablePadrao(String body) {
        body += " <style type=\"text/css\">";
        body += " table.reference td.red {color: red; } table.reference td.green{color: #008200;}";
        body += " table.reference { ";
        body += " background-color:#ffffff;";
        body += " border:1px solid #c3c3c3;";
        body += " border-collapse:collapse;";
        body += " width:95%;}";
        body += " table.reference th { ";
        body += " background-color:#e5eecc;";
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

    private Calendar[] primeiroUltimoDiasMes() {
        Date d = new Date();
        Date[] dates = new Date[4];
        dates[0] = UtilBeans.primeiroUltimoDia(d)[0];
        dates[1] = UtilBeans.primeiroUltimoDia(d)[1];
        dates[2] = UtilBeans.primeiroUltimoDia(UtilBeans.aumentaMesDate(d, 1))[0];
        dates[3] = UtilBeans.primeiroUltimoDia(UtilBeans.aumentaMesDate(d, 1))[1];
        Calendar[] toReturn = new Calendar[4];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = UtilBeans.dateToCalendar(dates[i]);
        }
        return toReturn;
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

    private String corpoTableEmail(List<ReceitaDivida> listCPOK, List<ReceitaDivida> listCPAtrasada) {
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
        for (ReceitaDivida cp : listCPAtrasada) {
            toReturn += "<tr>";
            toReturn += "<td align='left' class=\"red\">" + cp.getObservacao() + "</td>";
            toReturn += "<td align='center' class=\"red\">" + UtilBeans.getDataString(cp.getDataVencimento()) + "</td>";
            toReturn += "<td align='right' class=\"red\">" + UtilBeans.currencyFormat(cp.getValor()) + "</td>";
            toReturn += "<td align='center' class=\"red\">" + cp.getDetalheMovimentacao().getDetalhe() + "</td>";
            toReturn += "<td align='center' class=\"red\">" + cp.getParcelaAtual().toString() + "</td>";
            toReturn += "<td align='center' class=\"red\">" + cp.getParcelaTotal().toString() + "</td>";
            toReturn += "<td align='center' class=\"red\">" + cp.getUsuario().getFirstName() + "</td>";
            toReturn += "</tr>";
        }
        for (ReceitaDivida cp : listCPOK) {
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

    private void enviaEmail(String body, String titulo, Scheduler lc) {
        SimpleEmail se = new SimpleEmail();
        se.setBody(body);
        se.setEmail(lc.getEmail());
        se.setSubject(titulo);
        emailSendBean.enviarEmailJMS(se);
    }

    private String defineCartaoValor(Map<CartaoMesDTO, Double> contaValorMap) {
        String body = "";
        body = buscarTablePadrao(body);
        body += " <tr> ";
        body += " <th align=\"left\">Cartão</th> ";
        body += " <th align=\"left\">Período</th> ";
        body += " <th align=\"left\">Total</th> ";
        body += " </tr> ";
        for (CartaoMesDTO dto : contaValorMap.keySet()) {
            body += " <tr> ";
            body += " <td> " + dto.getCartao().getLabel() + "</td> ";
            body += " <td> " + dto.getMesAno() + "</td> ";
            if (contaValorMap.get(dto) < 0) {
                body += " <td class=\"red\"> " + UtilBeans.currencyFormat(contaValorMap.get(dto) * -1) + " </td> ";
            } else {
                body += " <td class=\"green\"> " + UtilBeans.currencyFormat(contaValorMap.get(dto)) + " </td> ";
            }
            body += " </tr> ";
        }
        body += " </table> ";
        return body;
    }

    private class CartaoMesDTO implements Comparable<CartaoMesDTO> {

        private ContaBancaria cartao;
        private String mesAno;

        public CartaoMesDTO(ContaBancaria cartao, String mesAno) {
            this.cartao = cartao;
            this.mesAno = mesAno;
        }

        public ContaBancaria getCartao() {
            return cartao;
        }

        public void setCartao(ContaBancaria cartao) {
            this.cartao = cartao;
        }

        public String getMesAno() {
            return mesAno;
        }

        public void setMesAno(String mesAno) {
            this.mesAno = mesAno;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CartaoMesDTO other = (CartaoMesDTO) obj;
            if (this.cartao != other.cartao && (this.cartao == null || !this.cartao.equals(other.cartao))) {
                return false;
            }
            if ((this.mesAno == null) ? (other.mesAno != null) : !this.mesAno.equals(other.mesAno)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + (this.mesAno != null ? this.mesAno.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return "CartaoMesDTO{" + "cartao=" + cartao + ", mesAno=" + mesAno + '}';
        }

        @Override
        public int compareTo(CartaoMesDTO o) {
            int i = 0;
            if (i == 0) {
                i = (this.mesAno).compareTo(o.mesAno);
            }
            if (i == 0) {
                i = this.cartao.compareTo(o.cartao);
            }

            return i;
        }
    }
}
