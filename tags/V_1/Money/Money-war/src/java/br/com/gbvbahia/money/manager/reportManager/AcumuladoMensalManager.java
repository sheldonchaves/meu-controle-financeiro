/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager.reportManager;

import br.com.gbvbahia.money.manager.InterfaceManager;
import br.com.gbvbahia.money.manager.LoginManager;
import br.com.gbvbahia.money.manager.SelectItemManager;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.ReportBeanLocal;
import br.com.money.enums.TipoMovimentacao;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "acumuladoMensalManager")
@RequestScoped
public class AcumuladoMensalManager implements InterfaceManager {

    private static final Integer PERIODO = 13;
    @EJB
    private ReportBeanLocal reportBean;
    
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
        
    private CartesianChartModel categoryModel;

    public AcumuladoMensalManager() {
    }

    //====================
    // Iniciadores        
    //====================
    @Override
    @PostConstruct
    public void init() {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "AcumuladoMensalManager.end() executado!");
        atualizarBarrChart();
    }

    @Override
    @PreDestroy
    public void end() {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "AcumuladoMensalManager.init() executado!");
    }
    //====================
    //Métodos de Negócio  
    //====================

    private void atualizarBarrChart() {
        Date[] datas = periodoInformacao();
        categoryModel = new CartesianChartModel();
        ChartSeries receitas = new ChartSeries(UtilMetodos.getResourceBundle("receitas", FacesContext.getCurrentInstance()));
        ChartSeries dividas = new ChartSeries(UtilMetodos.getResourceBundle("dividas", FacesContext.getCurrentInstance()));
        for (Date date : datas) {
            Map<TipoMovimentacao, Double> infoMap = this.reportBean.acumuladoMes(date, this.loginManager.getUsuario());
            receitas.set(UtilMetodos.getDataStringMesAno(date), infoMap.get(TipoMovimentacao.DEPOSITO));
            dividas.set(UtilMetodos.getDataStringMesAno(date), infoMap.get(TipoMovimentacao.RETIRADA));
        }
        categoryModel.addSeries(receitas);
        categoryModel.addSeries(dividas);
    }

    private Date[] periodoInformacao() {
        Date[] toReturn = new Date[PERIODO];
        int j = 0;
        for (int i = (PERIODO / 2 * (-1)); i <= PERIODO / 2; i++) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, i);
            toReturn[j++] = c.getTime();
        }
        return toReturn;
    }

    //====================
    //Table Actions       
    //====================
    //====================
    //SelectItem          
    //====================
    //====================
    //Getters AND Setters 
    //====================
    @Override
    public Locale getLocale() {
        return SelectItemManager.BRASIL;
    }

    @Override
    public String getPattern() {
        return SelectItemManager.PATTERN;
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CartesianChartModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }
}
