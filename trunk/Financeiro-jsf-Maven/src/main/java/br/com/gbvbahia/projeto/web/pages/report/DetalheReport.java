/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report;

import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.projeto.web.constante.Meses;
import br.com.gbvbahia.projeto.web.jsfutil.LocaleController;
import br.com.gbvbahia.projeto.web.pages.report.comparator.DetalheValorComprator;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class DetalheReport implements Serializable {

    private static final int LIMITE_DETALHES = 4;
    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(DetalheReport.class);
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ProcedimentoFacade procedimentoFacade;
    @EJB
    private DetalheProcedimentoFacade detalheProcedimentoFacade;
    @ManagedProperty("#{localeController}")
    private LocaleController localeController;
    //Chart Class
    private boolean detalheNotNull = true;

    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...DetalheReport");
        if (detalheProcedimentoFacade.countarDetalhePorUsuario(usuarioFacade.getUsuario(), null).equals(0L)) {
            detalheNotNull = false;
        }
    }

    /**
     * Busca as despesas no periodo selecionado, se não houver periodo irá
     * buscar na data atual.
     *
     * @return
     */
    private List<DespesaProcedimento> despesas(Integer anoOperacao, Meses mesOperacao) {
        List<DespesaProcedimento> toReturn;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, anoOperacao);
        c.set(Calendar.MONTH, mesOperacao.getMes());
        final Date[] intervalo = DateUtils.getIntervalo(c.getTime());
        toReturn = procedimentoFacade.buscarDespesaIntervalo(usuarioFacade.getUsuario(), null, null, intervalo);
        return toReturn;
    }

    private PieChartModel makeClassPie(Date agora, PieChartModel pieClassModel) {
        Integer anoOperacao = DateUtils.getFieldDate(agora, Calendar.YEAR);
        Meses mesOperacao = Meses.getByMonth(DateUtils.getFieldDate(agora, Calendar.MONTH));
        List<DespesaProcedimento> lDesp = despesas(anoOperacao, mesOperacao);
        if (lDesp.isEmpty()) {
            pieClassModel.set(MensagemUtils.getResourceBundle("semInformacao",
                    FacesContext.getCurrentInstance()), 100);
        } else {
            Map<String, Double> map = new HashMap<String, Double>();
            for (DespesaProcedimento dp : lDesp) {
                if (map.containsKey(dp.getDetalhe().getDetalhe())) {
                    map.put(dp.getDetalhe().getDetalhe(), map.get(dp.getDetalhe().getDetalhe()) + dp.getValor().doubleValue());
                } else {
                    map.put(dp.getDetalhe().getDetalhe(), dp.getValor().doubleValue());
                }
            }
            List<DetalheValorComprator> detalhes = new ArrayList<DetalheValorComprator>();
            for (String key : map.keySet()) {
                DetalheValorComprator det = new DetalheValorComprator(key, map.get(key));
                if (detalhes.contains(det)) {
                    detalhes.get(detalhes.indexOf(det)).addValor(map.get(key));
                } else {
                    detalhes.add(det);
                }
            }
            Collections.sort(detalhes);
            int laco = 0;
            double totalOutros = 0;
            String outros = MensagemUtils.getResourceBundle("outros", FacesContext.getCurrentInstance());
            for (DetalheValorComprator dv : detalhes) {
                if (laco++ > LIMITE_DETALHES) {
                    totalOutros += dv.getValor();
                } else {
                    pieClassModel.set(dv.getDetalhe(), dv.getValor());
                }
            }
            pieClassModel.set(outros, totalOutros);
        }
        return pieClassModel;
    }

    //Atual
    public PieChartModel getPieClassModel() {
        PieChartModel pieClassModel = new PieChartModel();
        makeClassPie(new Date(), pieClassModel);
        return pieClassModel;
    }

    public String getPieClassModelTitle() {
        return DateUtils.getDataFormatada(new Date(),
                localeController.getLocale(), "MMMM-yyyy");
    }
    
    //Anterior
    public PieChartModel getPieClassModelAnterior() {
        PieChartModel pieClassModel = new PieChartModel();
        makeClassPie(DateUtils.incrementar(new Date(), -1, Calendar.MONTH), pieClassModel);
        return pieClassModel;
    }

    public String getPieClassModelTitleAnterior() {
        return DateUtils.getDataFormatada(DateUtils.incrementar(new Date(), -1, Calendar.MONTH),
                localeController.getLocale(), "MMMM-yyyy");
    }
    
    //Posterior
    public PieChartModel getPieClassModelPosterior() {
        PieChartModel pieClassModel = new PieChartModel();
        makeClassPie(DateUtils.incrementar(new Date(), 1, Calendar.MONTH), pieClassModel);
        return pieClassModel;
    }

    public String getPieClassModelTitlePosterior() {
        return DateUtils.getDataFormatada(DateUtils.incrementar(new Date(), 1, Calendar.MONTH),
                localeController.getLocale(), "MMMM-yyyy");
    }

    public LocaleController getLocaleController() {
        return localeController;
    }

    public void setLocaleController(LocaleController localeController) {
        this.localeController = localeController;
    }

    public boolean isDetalheNotNull() {
        return detalheNotNull;
    }

    public void setDetalheNotNull(boolean detalheNotNull) {
        this.detalheNotNull = detalheNotNull;
    }
}
