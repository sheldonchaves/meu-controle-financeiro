/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report.utils;

import br.com.gbvbahia.financeiro.constantes.ClassificacaoProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.utils.MensagemUtils;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Usuário do Windows
 */
public class ClassificacaoMakePie {

    private List<Procedimento> detalhes;
    private PieChartModel pieClassModel;
    private FacesContext context;

    public ClassificacaoMakePie(List<Procedimento> detalhes,
            PieChartModel pieChartModel, FacesContext context) {
        this.detalhes = detalhes;
        this.pieClassModel = pieChartModel;
        this.context = context;
    }

    public PieChartModel makePie() {
        if (detalhes == null || detalhes.isEmpty()) {
            pieClassModel.set(MensagemUtils.getResourceBundle("semInformacao",
                    context), 100);
        }else {
            Map<ClassificacaoProcedimento, Double> map = new EnumMap<ClassificacaoProcedimento, Double>(ClassificacaoProcedimento.class);
            for (Procedimento dp : detalhes) {
                if (map.containsKey(dp.getClassificacaoProcedimento())) {
                    map.put(dp.getClassificacaoProcedimento(), map.get(dp.getClassificacaoProcedimento()) + dp.getValor().doubleValue());
                } else {
                    map.put(dp.getClassificacaoProcedimento(), dp.getValor().doubleValue());
                }
            }
            pieClassModel.set(MensagemUtils.getResourceBundle(ClassificacaoProcedimento.FIXA.toString(),
                    FacesContext.getCurrentInstance()), map.get(ClassificacaoProcedimento.FIXA));
            pieClassModel.set(MensagemUtils.getResourceBundle(ClassificacaoProcedimento.VARIAVEL.toString(),
                    FacesContext.getCurrentInstance()), map.get(ClassificacaoProcedimento.VARIAVEL));
        }
        return pieClassModel;
    }
}
