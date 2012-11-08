/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.jsfutil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

/**
 * @since v.1 19/05/2012
 * @author Guilherme
 */
@ManagedBean
@RequestScoped
public class ErrorController {

    /**
     * Creates a new instance of ErrorController.
     */
    public ErrorController() {
    }

    /**
     * Transforma a stacktrace em String para ser exibida na tela de
     * erro.
     *
     * @return StackTrace String.
     */
    public String getStackTrace() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map requestMap = context.getExternalContext().getRequestMap();
        Throwable ex = (Throwable)
                requestMap.get("javax.servlet.error.exception");
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        fillStackTrace(ex, pw);
        return writer.toString();
    }

    /**
     * Escreve a exceção no writer.
     *
     * @param ex Exceção que será trace convertido em string.
     * @param pw PrintWriter que escreverá a stacktrace.
     */
    private void fillStackTrace(final Throwable ex,
            final PrintWriter pw) {
        if (null == ex) {
            return;
        }
        ex.printStackTrace(pw);
        if (ex instanceof ServletException) {
            Throwable cause = ((ServletException) ex).getRootCause();
            if (null != cause) {
                pw.println("Root Cause:");
                fillStackTrace(cause, pw);
            }
        } else {
            Throwable cause = ex.getCause();
            if (null != cause) {
                pw.println("Cause:");
                fillStackTrace(cause, pw);
            }
        }
    }
}
