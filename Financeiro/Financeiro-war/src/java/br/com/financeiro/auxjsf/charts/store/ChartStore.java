/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.charts.store;

import br.com.financeiro.entidades.User;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
public abstract class ChartStore {

    private FacesContext fc;
    private User proprietario;
    private Locale locale;

    public ChartStore(FacesContext fc, User proprietario, Locale locale) {
        this.fc = fc;
        this.proprietario = proprietario;
        this.locale = locale;
    }
    
    public abstract String getCaminhoChar(Object[] objetosGrad);
    
     protected String getCaminhoLogo(String nome) {
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        String caminho = session.getServletContext().getRealPath("temp");
        File temp = new File(caminho);
        temp.mkdir();
        //Logger.getLogger(AcumuladoFaces.class.getName()).log(Level.INFO, caminho + File.separator + this.proprietario.getId() + ".png");
        return caminho + File.separator + this.proprietario.getId() + "_" + nome + ".png";
    }
    
    protected void deletaFiles(String excecaoNome) {
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        File file = new File(session.getServletContext().getRealPath("temp"));
        File[] files = file.listFiles();
        for (File f : files) {
            Calendar c = Calendar.getInstance();
            Calendar ff = Calendar.getInstance();
            ff.setTime(new Date(f.lastModified()));
            int diaH = c.get(Calendar.DAY_OF_MONTH);
            int diaF = ff.get(Calendar.DAY_OF_MONTH);
            if (StringUtils.substringBefore(f.getName(), "_").equals(StringUtils.substringBefore(excecaoNome, "_"))
                    && !StringUtils.substringAfter(f.getName(), "_").equals(StringUtils.substringAfter(excecaoNome, "_"))
                    && diaH != diaF) {
                f.delete();
            }
        }
    }

    protected FacesContext getFc() {
        return fc;
    }

    protected User getProprietario() {
        return proprietario;
    }

    protected Locale getLocale() {
        return locale;
    }
}
