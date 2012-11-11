package br.com.gbvbahia.projeto.web.jsfutil;

import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Manager Bean jsf responsável por gerenciar a internacionalização
 * da aplicação.
 * @author Guilherme Braga
 * @since 29/04/2012
 */
@ManagedBean
@SessionScoped
public class LocaleController {
    
        /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(LocaleController.class);
    /**
     * Referente ao idioma do usuário.
     */
    private Locale locale;
    private Locale localeUI;

    /**
     * Após ser construído o bean recupera o Locale atual do usuário.
     */
    @PostConstruct
    public void init() {
        FacesContext jsf = FacesContext.getCurrentInstance();
        locale = jsf.getExternalContext().getRequestLocale();
        localeUI = locale;
    }

    /**
     * Getter to Locale.
     * @return Locale atual.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Seter to locale.
     * @param localeDefinido Locale a ser definido.
     */
    public void setLocale(final Locale localeDefinido) {
        logger.info("Locale: " + localeDefinido.toString());
        this.locale = localeDefinido;
    }

    /**
     * Devolve os locales do facesConfig.xml.
     * @return SelectItem[] com locales.
     */
    public SelectItem[] getLocales() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        FacesContext jsf = FacesContext.getCurrentInstance();
        Application app = jsf.getApplication();
        Iterator<Locale> supportedLocales = app.getSupportedLocales();
        while (supportedLocales.hasNext()) {
            Locale loc = supportedLocales.next();
            items.add(new SelectItem(loc.toString(),
                    loc.getDisplayName()));
        }
        return items.toArray(new SelectItem[] {});
    }

    /**
     * Devolve o Locale atual.
     * @return String locale, pt_BR...
     */
    public String getSelectedLocale() {
        return getLocale().toString();
    }

    /**
     * Recupera o Locale selecionado.
     */
    public void defineLocale() {
        FacesContext jsf = FacesContext.getCurrentInstance();
        ExternalContext exContext = jsf.getExternalContext();
        Map<String, String> map = exContext.getRequestParameterMap();
        setSelectedLocale(map.get("locale"));
    }

    /**
     * Define o locale selecionado.
     * @param localeString Locale selecionado.
     */
   public void setSelectedLocale(final String localeString) {
        FacesContext jsf = FacesContext.getCurrentInstance();
        Application app = jsf.getApplication();
        Iterator<Locale> supportedLocales = app.getSupportedLocales();
        while (supportedLocales.hasNext()) {
            Locale loc = supportedLocales.next();
            if (loc.toString().equals(localeString)) {
                this.locale = loc;
                break;
            }
        }
        if(StringUtils.equalsIgnoreCase("en", localeString)){
            localeUI = new Locale("en", "US");
        }else {
            localeUI = new Locale("pt", "BR");
        }
    }

    public Locale getLocaleUI() {
        return localeUI;
    }

    public void setLocaleUI(Locale localeUI) {
        this.localeUI = localeUI;
    }
}