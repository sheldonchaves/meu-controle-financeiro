/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report;

import br.com.gbvbahia.financeiro.beans.facades.ReportFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.utils.MensagemUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Guilherme
 */
@ManagedBean
@RequestScoped
public class AcumuladoMensalReport {

    private static final Integer PERIODO = 5;
    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(AcumuladoMensalReport.class);
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ReportFacade reportFacade;
    private CartesianChartModel categoryModel;
    //====================
    // Iniciadores        
    //====================

    @PostConstruct
    public void init() {
        logger.info("init()...CartaoMensalReport");
        atualizarBarrChart();
    }

    @PreDestroy
    public void end() {
        logger.info("init()...CartaoMensalReport");
    }
    //====================
    //Métodos de Negócio  
    //====================

    private void atualizarBarrChart() {
        Date[] datas = periodoInformacao();
        categoryModel = new CartesianChartModel();
        ChartSeries receitas = new ChartSeries(MensagemUtils.getResourceBundle("receitas", FacesContext.getCurrentInstance()));
        ChartSeries dividas = new ChartSeries(MensagemUtils.getResourceBundle("dividas", FacesContext.getCurrentInstance()));
        for (Date date : datas) {
            Map<TipoProcedimento, Double> infoMap = this.reportFacade.acumuladoReceitaDespesaPeriodo(date, usuarioFacade.getUsuario());
            receitas.set(DateUtils.getDataFormatada(date, "MM/yy"), infoMap.get(TipoProcedimento.RECEITA_FINANCEIRA));
            dividas.set(DateUtils.getDataFormatada(date, "MM/yy"), infoMap.get(TipoProcedimento.DESPESA_FINANCEIRA));
        }
        categoryModel.addSeries(receitas);
        categoryModel.addSeries(dividas);
    }

    private Date[] periodoInformacao() {
        Date[] toReturn = new Date[PERIODO];
        for(int i = -1; i < (PERIODO - 1);){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, i);
            toReturn[++i] = c.getTime();
        }
        return toReturn;
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CartesianChartModel categoryModel) {
        this.categoryModel = categoryModel;
    }
}
