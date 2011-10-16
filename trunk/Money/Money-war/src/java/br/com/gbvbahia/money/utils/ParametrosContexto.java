/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class ParametrosContexto {
 
    public static String getParameterContexto(String parametro, FacesContext currentInstance){
        HttpSession session = (HttpSession) currentInstance.getExternalContext().getSession(true);
        return session.getServletContext().getInitParameter(parametro);
    }
}
