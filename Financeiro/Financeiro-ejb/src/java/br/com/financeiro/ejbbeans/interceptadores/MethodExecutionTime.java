/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.interceptadores;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author gbvbahia
 */
public class MethodExecutionTime {

    @AroundInvoke
    public Object timeCheck(InvocationContext invocation) throws Exception{
        long startTime = System.currentTimeMillis();
        try{
            return invocation.proceed();
        }finally{
            long endTime = (System.currentTimeMillis() - startTime) / 1000;
            Logger.getLogger(invocation.getClass().getName()).log(Level.INFO, "M\u00e9todo {0} demorou: {1} segundos", new Object[]{invocation.getMethod(), endTime});
        }
    }
}
