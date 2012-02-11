/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.filtros;

import br.com.gbvbahia.money.manager.LoginManager;
import br.com.money.modelos.Usuario;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Garante que somente usuários logados entrem na aplicação
 * @author gbvbahia
 */

public class FilterLogin implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) request;
        HttpServletResponse rs = (HttpServletResponse) response;
        HttpSession session = rq.getSession();
        Usuario us = (Usuario)session.getAttribute(LoginManager.SESSION_PROPRIETARIO);
        if ( us == null) {
            rs.sendRedirect(session.getServletContext().getContextPath() + "/index.xhtml");
        }/*else if (us.getAcessos() == 0) {
        rs.sendRedirect(session.getServletContext().getContextPath() + "/certificacao/principal.xhtml");
        }*/ else {
            chain.doFilter(rq, rs);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
}
