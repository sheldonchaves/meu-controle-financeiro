/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.aop;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * Classe utilizada para interceptação de métodos de negócio para criar Logs
 * no arquivo de log do sistema.<br> Desta forma podemos manter o código do
 * negócio limpo.<br> Para utilizar basta anotar o método de negócio com:
 * @Interceptors({LogTime.class})
 *
 * @since v.1 2012/05/21
 * @author Guilherme Braga
 */
public class LogTime {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(LogTime.class);

    @AroundInvoke
    protected Object logInterceptor(final InvocationContext ctx)
            throws Exception {
        String nome = "Classe: " + ctx.getTarget().getClass().getSimpleName()
                + " Método: " + ctx.getMethod().getName();
        StopWatch timeWatch = new StopWatch();
        timeWatch.start();
        try {
            return ctx.proceed();
        } finally {
            timeWatch.stop();
            String time = DurationFormatUtils.formatDuration(
                    timeWatch.getTime(), "HH:mm:ss");
            logger.info("Duração: " + time + " | " + nome);

        }
    }
}
