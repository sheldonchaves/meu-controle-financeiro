/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report;

import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.ClassificacaoProcedimento;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.dto.MinMaxDateDTO;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.projeto.web.constante.Meses;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.projeto.web.jsfutil.LocaleController;
import br.com.gbvbahia.projeto.web.pages.report.comparator.DespesaProcedimentoDataComparator;
import br.com.gbvbahia.projeto.web.pages.report.utils.ClassificacaoMakePie;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class ChartsReport implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(ChartsReport.class);
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ProcedimentoFacade procedimentoFacade;
    @EJB
    private DetalheProcedimentoFacade detalheProcedimentoFacade;
    @ManagedProperty("#{localeController}")
    private LocaleController localeController;
    //SelectItem
    private List<Integer> listAnosSelect;
    private Meses mesOperacao;
    private Integer anoOperacao;
    //Chart Class
    private Date dataClassModel;
    private PieChartModel pieClassModel;
    private List<DespesaProcedimento> listFinxa;
    private List<DespesaProcedimento> listVariavel;
    private double totalVariavel;
    private double totalFixa;
    private boolean detalheNotNull = true;

    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...ChartsReport");
        MinMaxDateDTO intervalodDatas = procedimentoFacade.buscarIntervalodDatas(
                null, null, usuarioFacade.getUsuario());
        listAnosSelect = intervalodDatas.intervaloMinMaxAnos();
        Date agora = new Date();
        anoOperacao = DateUtils.getFieldDate(agora, Calendar.YEAR);
        mesOperacao = Meses.getByMonth(DateUtils.getFieldDate(agora, Calendar.MONTH));
        if (detalheProcedimentoFacade.countarDetalhePorUsuario(usuarioFacade.getUsuario(), null).equals(0L)) {
            detalheNotNull = false;
        }
    }

    //====================
    // Select Itens
    //====================
    public SelectItem[] getMeses() {
        return JsfUtil.getEnumSelectItems(Meses.class, false, FacesContext.getCurrentInstance());
    }

    public SelectItem[] getAnos() {
        SelectItem[] anos = new SelectItem[listAnosSelect.size()];
        for (int i = 0; i < anos.length; i++) {
            anos[i] = new SelectItem(listAnosSelect.get(i), listAnosSelect.get(i).toString());
        }
        return anos;
    }

    public void buscarDespesasPie() {
        makeClassPie();
    }

    /**
     * Busca as despesas no periodo selecionado, se não houver periodo irá
     * buscar na data atual.
     *
     * @return
     */
    private List<DespesaProcedimento> despesas() {
        List<DespesaProcedimento> toReturn;
        if (mesOperacao == null || anoOperacao == null) {
            Date agora = new Date();
            anoOperacao = DateUtils.getFieldDate(agora, Calendar.YEAR);
            mesOperacao = Meses.getByMonth(DateUtils.getFieldDate(agora, Calendar.MONTH));
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, anoOperacao);
        c.set(Calendar.MONTH, mesOperacao.getMes());
        dataClassModel = c.getTime();
        final Date[] intervalo = DateUtils.getIntervalo(c.getTime());
        toReturn = procedimentoFacade.buscarDespesaIntervalo(usuarioFacade.getUsuario(), null, null, intervalo);
        return toReturn;
    }

    private PieChartModel makeClassPie() {
        List<DespesaProcedimento> lDesp = despesas();
        listFinxa = new ArrayList<DespesaProcedimento>();
        listVariavel = new ArrayList<DespesaProcedimento>();
        totalFixa = 0;
        totalVariavel = 0;
        Map<ClassificacaoProcedimento, Double> map = new EnumMap<ClassificacaoProcedimento, Double>(ClassificacaoProcedimento.class);
        for (DespesaProcedimento dp : lDesp) {
            if (map.containsKey(dp.getClassificacaoProcedimento())) {
                map.put(dp.getClassificacaoProcedimento(), map.get(dp.getClassificacaoProcedimento()) + dp.getValor().doubleValue());
            } else {
                map.put(dp.getClassificacaoProcedimento(), dp.getValor().doubleValue());
            }
            if (ClassificacaoProcedimento.FIXA.equals(dp.getClassificacaoProcedimento())) {
                totalFixa += dp.getValor().doubleValue();
                listFinxa.add(dp);
            } else {
                listVariavel.add(dp);
                totalVariavel += dp.getValor().doubleValue();
            }
        }
        return  pieClassModel = new ClassificacaoMakePie(lDesp, new PieChartModel(), FacesContext.getCurrentInstance()).makePie();
    }

    public Meses getMesOperacao() {
        return mesOperacao;
    }

    public void setMesOperacao(Meses mesOperacao) {
        this.mesOperacao = mesOperacao;
    }

    public Integer getAnoOperacao() {
        return anoOperacao;
    }

    public void setAnoOperacao(Integer anoOperacao) {
        this.anoOperacao = anoOperacao;
    }

    public PieChartModel getPieClassModel() {
        if (pieClassModel == null) {
            buscarDespesasPie();
        }
        return pieClassModel;
    }

    public void setPieClassModel(PieChartModel pieClassModel) {
        this.pieClassModel = pieClassModel;
    }

    public LocaleController getLocaleController() {
        return localeController;
    }

    public void setLocaleController(LocaleController localeController) {
        this.localeController = localeController;
    }

    public String getPieClassModelTitle() {
        if (dataClassModel == null) {
            dataClassModel = new Date();
        }
        return DateUtils.getDataFormatada(dataClassModel,
                localeController.getLocale(), "MMMM-yyyy");
    }

    public List<DespesaProcedimento> getListFinxa() {
        if (listFinxa == null) {
            listFinxa = new ArrayList<DespesaProcedimento>();
        }
        Collections.sort(listFinxa, new DespesaProcedimentoDataComparator());
        return listFinxa;
    }

    public List<DespesaProcedimento> getListVariavel() {
        if (listVariavel == null) {
            listVariavel = new ArrayList<DespesaProcedimento>();
        }
        Collections.sort(listVariavel, new DespesaProcedimentoDataComparator());
        return listVariavel;
    }

    public double getTotalVariavel() {
        return totalVariavel;
    }

    public double getTotalFixa() {
        return totalFixa;
    }

    public boolean isDetalheNotNull() {
        return detalheNotNull;
    }

    public void setDetalheNotNull(boolean detalheNotNull) {
        this.detalheNotNull = detalheNotNull;
    }
}
