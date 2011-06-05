/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans.timeservices;

import br.com.financeiro.ejbbeans.interfaces.ContaPagarReceberLocal;
import br.com.financeiro.ejbbeans.interfaces.UserLocal;
import br.com.financeiro.ejbbeans.timeservices.interfaces.TimeServiceLocal;
import br.com.financeiro.ejbbeans.timeservices.interfaces.TimerContasLocal;
import br.com.financeiro.ejbbeans.timeservices.utilitarias.SimpleEmail;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.LembreteConta;
import br.com.financeiro.entidades.enums.FormaPagamento;
import br.com.financeiro.entidades.enums.StatusPagamento;
import br.com.financeiro.utils.UtilBeans;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Timer;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import javax.interceptor.Interceptors;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
@Stateless
public class TimeServiceBean implements TimeServiceLocal {

    @EJB
    private TimerContasLocal timerContas;

    @EJB
    private ContaPagarReceberLocal contaPagarReceberBean;

    @EJB
    private UserLocal userBean;

    @Resource
    private TimerService timer;

    private static final String timeServiceBean = "timeServiceBean";

    private static Date horaExecucao;

    private static int intervalo;

    /**
     * Temporizador que expira muitas vezes, primeiro expira para a data especificada e depois as expirações subsequentes ocorrem em
     * intervalos iguais ao paramêtro intervaloEntreExecucaoMilisegundos
     * 1000 * 60 * 60 * 24 * 90 - 90 dias
     * 1000 * 60 * 60 * 24 * 3  - 3 dias
     * 1000 * 60 * 10           - 10 minutos
     * 1000 * 60 * 60           - 1 hora
     * @param dataHorario
     * @param intervaloEntreExecucaoMilisegundos
     * @param msg
     */
    static {
        Calendar c = Calendar.getInstance();
        //c.set(Calendar.HOUR_OF_DAY, 23);
        //c.set(Calendar.MINUTE, 59);
        c.add(Calendar.DAY_OF_MONTH, 1);
        //Logger.getLogger(TimeServiceBean.class.getName()).log(Level.WARNING, "******* ATENÇÃO ATENÇÃO ******   ALTERAR A O DIA PARA O PROXIMO DIA!");
        c.set(Calendar.HOUR_OF_DAY, 3);
        //c.add(Calendar.SECOND, 30);
        horaExecucao = c.getTime();
        intervalo = 1000 * 60 * 60 * 24 * 1;
    }

    @Timeout
    @Interceptors(br.com.financeiro.ejbbeans.interceptadores.MethodExecutionTime.class)
    public void ejbTimeout(Timer time) {
        long startTime = System.currentTimeMillis();
        List<LembreteConta> list = this.userBean.busrcarLembreteContas(true);
        for (LembreteConta lc : list) {
            if (lc.isStatus()) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, -1);
                Calendar d = Calendar.getInstance();
                d.add(Calendar.DAY_OF_MONTH, 6);
                List<ContaPagar> lcp = this.contaPagarReceberBean.buscarContasPorData(c.getTime(), d.getTime(), StatusPagamento.NAO_PAGA, lc.getUser());
                String body = "";
                for (ContaPagar cp : lcp) {
                    body += this.verificaTempo(lc, cp);
                }
                body = StringUtils.replace(body, "null", "");
                if (body != null && !body.trim().equals("") && !body.trim().equals("null")) {
                    String bodyFim = "<h3>Lembrete de Proximos Vencimentos:</h3>" + body;
                    enviaEmail(bodyFim, lc);
                }
            }
        }
        long endTime = (System.currentTimeMillis() - startTime) / 1000;
        Logger.getLogger(TimeServiceBean.class.getName()).log(Level.WARNING, "M\u00e9todo {0} demorou: {1} segundos", new Object[]{"ejbTimeout: Verificação de Contas", endTime});
    }

    private void enviaEmail(String body, LembreteConta lc) {
        SimpleEmail se = new SimpleEmail();
        se.setBody(body);
        se.setEmail(lc.getEmail());
        se.setSubject("Lembrete de Contas a Vencer");
        timerContas.enviarLembreteContaEmail(se);
    }

    private String verificaTempo(LembreteConta ants, ContaPagar cp) {
        Calendar agora = Calendar.getInstance();
        agora.setTime(cp.getDataVencimento());
        for (int i = 0; i <= ants.getDias(); i++) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, i);
            if (comparaDiaMesAno(c, agora)) {
                return corpoEmail(cp, false);
            } else if (contaAtrazada(agora)) {
                return corpoEmail(cp, true);
            }
        }
        return null;
    }

    private String corpoEmail(ContaPagar cp, boolean atrasada) {
        String body = "";
        body += "<br>";
        if (atrasada) {
            body += "<h4>Conta Vencida</h4>";
        } else {
            body += "<h4>Conta a Vencer</h4>";

        }
        body += "Observação: " + cp.getObservacao();
        body += "<br>";
        body += "Vencimento: " + UtilBeans.getDataString(cp.getDataVencimento());
        body += "<br>";
        body += "Valor: " + UtilBeans.currencyFormat(cp.getContaValor());
        body += "<br>";
        body += "Grupo Gasto: " + cp.getGrupoGasto().getGrupoGasto();
        body += "<br>";
        body += "Parcela Atual: " + cp.getParcelaAtual().toString();
        body += "<br>";
        body += "Total de Parcelas: " + cp.getParcelaTotal().toString();
        body += "<br>";
        body += "Forma de Pagamento: " + cp.getFormaPagamento().getFormaPagamento();
        body += "<br>";
        if (cp.getFormaPagamento().equals(FormaPagamento.CARTAO_DE_CREDITO)) {
            body += "Cartão de Crédito: " + cp.getCartaoCreditoUnico();
            body += "<br>";
        }
        return body;
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

    private boolean contaAtrazada(Calendar dataVencimento) {
        Calendar c = Calendar.getInstance();
        int diaVencimento = dataVencimento.get(Calendar.DAY_OF_MONTH);
        int mesVencimento = dataVencimento.get(Calendar.MONTH);
        int anoVencimento = dataVencimento.get(Calendar.YEAR);
        int diaHoje = c.get(Calendar.DAY_OF_MONTH);
        int mesHoje = c.get(Calendar.MONTH);
        int anoHoje = c.get(Calendar.YEAR);
        if (diaVencimento < diaHoje && mesVencimento <= mesHoje && anoVencimento <= anoHoje) {
            return true;
        }
        return false;
    }

    @Override
    public void iniciarVerificacaoContas() {
        Collection<Timer> listTimers = (Collection<Timer>) timer.getTimers();
        for (Timer t : listTimers) {
            if (t.getInfo().equals(timeServiceBean)) {
                t.cancel();
            }
        }
        timer.createTimer(horaExecucao, intervalo, timeServiceBean);
    }
}
