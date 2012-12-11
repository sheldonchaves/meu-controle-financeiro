/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report.utils;

import br.com.gbvbahia.financeiro.beans.facades.MovimentacaoFinanceiraFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoProcedimento;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoTrasnferencia;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.financeiro.modelos.superclass.MovimentacaoFinanceira;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.utils.MensagemUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Usuário do Windows
 */
public class ConjugeMakeBar {

    private static final int PERIODO_BAR = 6;
    private MovimentacaoFinanceiraFacade movimentacaoFacade;
    private UsuarioFacade usuarioFacade;

    public ConjugeMakeBar(MovimentacaoFinanceiraFacade movimentacaoFacade, UsuarioFacade usuarioFacade) {
        this.movimentacaoFacade = movimentacaoFacade;
        this.usuarioFacade = usuarioFacade;
    }

    public CartesianChartModel makeBar() {
        CartesianChartModel toReturn = new CartesianChartModel();
        if (usuarioFacade.getUsuario().getConjuge() == null) {
            ChartSeries emptyChar = new ChartSeries(usuarioFacade.getUsuario().getFirstName());
            emptyChar.set(MensagemUtils.getResourceBundle("semInformacao", FacesContext.getCurrentInstance()), 100);
            toReturn.addSeries(emptyChar);
            return toReturn;
        }
        final Usuario titular = usuarioFacade.getUsuario();
        final Usuario conjuge = usuarioFacade.getUsuario().getConjuge();
        ChartSeries titularChar = new ChartSeries(titular.getFirstName());
        ChartSeries conjugeChar = new ChartSeries(conjuge.getFirstName());
        for (Date[] intervalo : intervalos()) {
            Map<Usuario, Double> map = getMap(titular, conjuge);
            String mes = DateUtils.getDataFormatada(intervalo[0], "MMM/yy");
            criarSeries(intervalo, titular, map);
            titularChar.set(mes, map.get(titular));
            conjugeChar.set(mes, map.get(conjuge));
        }
        toReturn.addSeries(titularChar);
        toReturn.addSeries(conjugeChar);
        return toReturn;
    }

    private List<Date[]> intervalos() {
        Date umAno = new Date();
        umAno = DateUtils.incrementar(umAno, -PERIODO_BAR, Calendar.MONTH);
        List<Date[]> toReturn = new ArrayList<Date[]>();
        for (int i = 1; i <= PERIODO_BAR; i++) {
            toReturn.add(DateUtils.getIntervalo(DateUtils.incrementar(umAno, i, Calendar.MONTH)));
        }
        return toReturn;
    }

    private void addMap(Map<Usuario, Double> map, Usuario user, Double valor) {
        map.put(user, map.get(user) + valor);
    }

    private Map<Usuario, Double> getMap(final Usuario titular, final Usuario conjuge) {
        Map<Usuario, Double> valores = new HashMap<Usuario, Double>();
        valores.put(titular, 0D);
        valores.put(conjuge, 0D);
        return valores;
    }

    private void criarSeries(Date[] intervalo, final Usuario titular, Map<Usuario, Double> map) {
        List<MovimentacaoFinanceira> movimentacoes = movimentacaoFacade.pesquisarMovimentacaoPorPeriodoUsuario(intervalo, titular);
        for (MovimentacaoFinanceira mov : movimentacoes) {
            if (mov instanceof MovimentacaoTrasnferencia) {
                MovimentacaoTrasnferencia transf = (MovimentacaoTrasnferencia) mov;
                addMap(map, transf.getContaBancariaDebitada().getUsuario(), transf.getValorTransferenciaDiferenca().doubleValue() * (-1));
                addMap(map, transf.getContaBancariaTransferida().getUsuario(), transf.getValorTransferenciaDiferenca().doubleValue());
            } else if (mov instanceof MovimentacaoProcedimento) {
                MovimentacaoProcedimento pro = (MovimentacaoProcedimento) mov;
                if (pro.getProcedimento().getTipoProcedimento().equals(TipoProcedimento.DESPESA_FINANCEIRA)) {
                    addMap(map, pro.getContaBancariaDebitada().getUsuario(), pro.getValorTransferenciaDiferenca().doubleValue() * (-1));
                }
            }
        }
    }
}
