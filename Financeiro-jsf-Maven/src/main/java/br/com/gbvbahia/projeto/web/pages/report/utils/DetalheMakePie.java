/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report.utils;

import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.projeto.web.pages.report.DetalheReport;
import br.com.gbvbahia.projeto.web.pages.report.comparator.DetalheValorComparator;
import br.com.gbvbahia.utils.MensagemUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Usuário do Windows
 */
public class DetalheMakePie {

    private List<DespesaProcedimento> detalhes;
    private PieChartModel pieChartModel;
    private FacesContext context;

    public DetalheMakePie(List<DespesaProcedimento> detalhes, PieChartModel pieChartModel, FacesContext context) {
        this.detalhes = detalhes;
        this.pieChartModel = pieChartModel;
        this.context = context;
    }

    public PieChartModel makePie() {
        if (detalhes == null || detalhes.isEmpty()) {
            pieChartModel.set(MensagemUtils.getResourceBundle("semInformacao",
                    context), 100);
        }
        List<DetalheValorComparator> det = gerarDetalheValorComparator(detalhes);
        int laco = 0;
        double totalOutros = 0;
        String outros = MensagemUtils.getResourceBundle("outros", FacesContext.getCurrentInstance());
        for (DetalheValorComparator dv : det) {
            if (laco++ > DetalheReport.LIMITE_DETALHES) {
                totalOutros += dv.getValor();
            } else {
                pieChartModel.set(dv.getDetalhe(), dv.getValor());
            }
        }
        if (totalOutros > 0) {
            pieChartModel.set(outros, totalOutros);
        }
        return pieChartModel;
    }

    private List<DetalheValorComparator> gerarDetalheValorComparator(List<DespesaProcedimento> lDesp) {
        Map<String, Double> map = new HashMap<String, Double>();
        for (DespesaProcedimento dp : lDesp) {
            if (map.containsKey(dp.getDetalhe().getDetalhe())) {
                map.put(dp.getDetalhe().getDetalhe(), map.get(dp.getDetalhe().getDetalhe()) + dp.getValor().doubleValue());
            } else {
                map.put(dp.getDetalhe().getDetalhe(), dp.getValor().doubleValue());
            }
        }
        List<DetalheValorComparator> detalhes = new ArrayList<DetalheValorComparator>();
        for (String key : map.keySet()) {
            DetalheValorComparator det = new DetalheValorComparator(key, map.get(key));
            if (detalhes.contains(det)) {
                detalhes.get(detalhes.indexOf(det)).addValor(map.get(key));
            } else {
                detalhes.add(det);
            }
        }
        Collections.sort(detalhes);
        return detalhes;
    }
}
