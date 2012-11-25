/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.business.scheduler;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.business.interfaces.AcompanhamentoCartaoCreditoBusiness;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.SchedulerFacade;
import br.com.gbvbahia.financeiro.beans.jms.interfaces.EmailSendBusiness;
import br.com.gbvbahia.financeiro.beans.jms.sends.SimpleEmail;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Scheduler;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.financeiro.utils.NumberUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Usuário do Windows
 */
@Stateless
@Interceptors({LogTime.class})
@RunAs("sys")
public class AcompanhamentoCartaoCreditoBean implements AcompanhamentoCartaoCreditoBusiness {

    private Logger logger = Logger.getLogger(AcompanhamentoCartaoCreditoBean.class);
    @EJB
    private SchedulerFacade schedulerBean;
    @EJB
    private ProcedimentoFacade procedimentoFacade;
    @EJB
    private EmailSendBusiness emailSendBusiness;

    @Override
    //@Schedule(hour = "*", minute = "*", second = "10", dayOfWeek = "*")//Teste
    @Schedule(hour = "2", minute = "23", second = "10", dayOfWeek = "5-0")//Real
    public void avisarCartaoCredito() {
        List<Scheduler> schedules = schedulerBean.buscarTodosSchelersPorStatus(true);
        Calendar[] intervalo = primeiroUltimoDiasMes();
        for (Scheduler sc : schedules) {
            List<DespesaProcedimento> despesas = procedimentoFacade.buscarDespesasCartao(sc.getUser(), intervalo[0].getTime(), intervalo[1].getTime());
            despesas.addAll(procedimentoFacade.buscarDespesasCartao(sc.getUser(), intervalo[2].getTime(), intervalo[3].getTime()));
            Map<CartaoMesDTO, Double> contaValorMap =
                    new TreeMap<CartaoMesDTO, Double>();
            for (DespesaProcedimento mf : despesas) {
                CartaoMesDTO dto = new CartaoMesDTO(mf.getCartaoCredito(),
                        DateUtils.getDataFormatada(mf.getDate(), "MM/yyyy"));
                if (contaValorMap.containsKey(dto)) {
                    contaValorMap.put(dto, contaValorMap.get(dto)
                            + mf.getValor().doubleValue());
                } else {
                    contaValorMap.put(dto, +mf.getValor().doubleValue());
                }
            }
            if (!contaValorMap.isEmpty()) {
                String body = defineCartaoValor(contaValorMap);
                body = StringUtils.replace(body, "null", "");
                if (body != null && !body.trim().equals("") && !body.trim().equals("null")) {
                    String bodyFim = "<h3>Aviso semanal de cartão de crédito:</h3>" + body;
                    logger.info("Enviando e-mail para: " + sc.getUser().getUserId());
                    enviaEmail(bodyFim, "Financeiro :: Cartão de Crédito", sc);
                }
            }else{
                logger.info("Usuario: " + sc.getUser().getUserId() + " sem contas no cartão.");
            }
        }
        logger.info("Scheduler Cartão de Crédito: Executado.");

    }

    private Calendar[] primeiroUltimoDiasMes() {
        Date d = new Date();
        Date[] dates = new Date[4];
        dates[0] = DateUtils.primeiroUltimoDia(d)[0];
        dates[1] = DateUtils.primeiroUltimoDia(d)[1];
        dates[2] = DateUtils.primeiroUltimoDia(DateUtils.incrementar(d, 1, Calendar.MONTH))[0];
        dates[3] = DateUtils.primeiroUltimoDia(DateUtils.incrementar(d, 1, Calendar.MONTH))[1];
        Calendar[] toReturn = new Calendar[4];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = DateUtils.dateToCalendar(dates[i]);
        }
        return toReturn;
    }

    private String defineCartaoValor(Map<CartaoMesDTO, Double> contaValorMap) {
        String body = "";
        body = AvisoVencimentosBean.buscarTablePadrao(body);
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
                body += " <td class=\"red\"> " + NumberUtils.currencyFormat(contaValorMap.get(dto) * -1) + " </td> ";
            } else {
                body += " <td class=\"green\"> " + NumberUtils.currencyFormat(contaValorMap.get(dto)) + " </td> ";
            }
            body += " </tr> ";
        }
        body += " </table> ";
        return body;
    }

    private void enviaEmail(String body, String titulo, Scheduler lc) {
        SimpleEmail se = new SimpleEmail();
        se.setBody(body);
        se.setEmail(lc.getEmail());
        se.setSubject(titulo);
        emailSendBusiness.enviarEmailJMS(se);
    }

    private class CartaoMesDTO implements Comparable<CartaoMesDTO> {

        private CartaoCredito cartao;
        private String mesAno;

        public CartaoMesDTO(CartaoCredito cartao, String mesAno) {
            this.cartao = cartao;
            this.mesAno = mesAno;
        }

        public CartaoCredito getCartao() {
            return cartao;
        }

        public void setCartao(CartaoCredito cartao) {
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
