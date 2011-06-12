/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.provisoes;

import br.com.financeiro.auxjsf.charts.GrupoGastoPagamentosChart;
import br.com.financeiro.auxjsf.charts.factoring.ChartStore;
import br.com.financeiro.auxjsf.classes.AcumuladoHelper;
import br.com.financeiro.auxjsf.classes.LinhasReportHelper;
import br.com.financeiro.auxjsf.classes.TipoValor;
import br.com.financeiro.auxjsf.classes.TipoValores;
import br.com.financeiro.auxjsf.classes.comparators.AcumuladoComparator;
import br.com.financeiro.auxjsf.classes.interfaces.AcumuladoInterface;
import br.com.financeiro.auxjsf.classes.interfaces.TipoValorInterface;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.GrupoFinanceiroLocal;
import br.com.financeiro.ejbbeans.interfaces.RelatoriosLocal;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoGasto;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class GrupoGastoPagamentosFaces {

    @EJB
    private GrupoFinanceiroLocal grupoFinanceiroBean;

    @EJB
    private RelatoriosLocal relatoriosBean;

    private Locale locale = new Locale("pt", "BR");

    private User proprietario;

    private int meses = 2;

    private Date dataReferencia = Calendar.getInstance().getTime();

    private DataModel grupoDataModel;

    private LinhasReportHelper linhasReportHelper;

    /** Creates a new instance of GrupoGastoPagamentosFaces */
    public GrupoGastoPagamentosFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
    }

    public void atualizaValoresGrupos(ActionEvent event) {
        geraGrupos();
    }

    private DataModel geraGrupos() {
        List<AcumuladoInterface> toDataModel = new ArrayList<AcumuladoInterface>();
        for (int i = 0; i <= meses; i++) {
            toDataModel.add(geraGrupo(i));
            if (i != 0) {
                toDataModel.add(geraGrupo(-i));
            }
        }

        Collections.sort(toDataModel, new AcumuladoComparator());
        List<List<TipoValorInterface>> listAll = new ArrayList<List<TipoValorInterface>>();
        int mes = 0;
        int totalMes = 0;
        for (AcumuladoInterface acu : toDataModel) {
            if (totalMes < acu.getTipoValorInterface().size()) {
                totalMes = acu.getTipoValorInterface().size();
            }
        }
        for (AcumuladoInterface acu : toDataModel) {
            for (int g = 0; g < totalMes; g++) {
                if ((listAll.size() - 1) >= mes) {
                    if (acu.getTipoValorInterface().size() - 1 >= g) {
                        listAll.get(mes).add(acu.getTipoValorInterface().get(g));
                    } else {
                        listAll.get(mes).add(new TipoValor("", null));
                    }
                } else {
                    listAll.add(new ArrayList<TipoValorInterface>());
                    if (acu.getTipoValorInterface().size() - 1 >= g) {
                        listAll.get(mes).add(acu.getTipoValorInterface().get(g));
                    } else {
                        listAll.get(mes).add(new TipoValor("", null));
                    }
                }
                ++mes;
            }
            mes = 0;
        }
        listAll = cleanLista(listAll);
        this.getLinhasReportHelper().setTipoValores(listAll);
        this.grupoDataModel = new ListDataModel(toDataModel);
        return this.grupoDataModel;
    }

    private List<List<TipoValorInterface>> cleanLista(List<List<TipoValorInterface>> listaSuja) {
        List<List<TipoValorInterface>> toRemove = new ArrayList<List<TipoValorInterface>>();
        for (List<TipoValorInterface> listaAux : listaSuja) {
            double total = 0.00;
            for (TipoValorInterface tvi : listaAux) {
                total += tvi.getValor();
            }
            if (total == 0.00) {
                toRemove.add(listaAux);
            }
        }
        listaSuja.removeAll(toRemove);
        return listaSuja;
    }

    private AcumuladoInterface geraGrupo(int loop) {
        List<GrupoGasto> listGrupoGasto = this.grupoFinanceiroBean.buscarGrupoGastoParaEdicao(proprietario);
        AcumuladoHelper acumulado = new AcumuladoHelper();
        Calendar c = Calendar.getInstance();
        c.setTime(dataReferencia);
        c.add(Calendar.MONTH, loop);
        acumulado.setMesAno(c.getTime());
        List<Object[]> grupoValores = this.relatoriosBean.buscarGastoPorGrupoGasto(c, proprietario);
        if (grupoValores.isEmpty()) {
            for (GrupoGasto gg : listGrupoGasto) {
                TipoValor tv = new TipoValor(gg.getGrupoGasto(), 0.00);
                acumulado.add(tv);
            }
        } else {
            for (GrupoGasto gg : listGrupoGasto) {
                boolean encontrado = false;
                for (Object[] objArray : grupoValores) {
                    if (gg.getGrupoGasto().equals(objArray[0].toString())) {
                        encontrado = true;
                        String nomeGrupo = gg.getGrupoGasto();
                        Double valorGrupo = (Double) objArray[1];
                        TipoValor tv = new TipoValor(nomeGrupo, valorGrupo);
                        acumulado.add(tv);
                    }
                }
                if (!encontrado) {
                    TipoValor tv = new TipoValor(gg.getGrupoGasto(), 0.00);
                    acumulado.add(tv);
                }
            }
        }
        if (loop == 0) {
            acumulado.setMesAtual(true);
        }
        return acumulado;
    }

    public String getGraficoAcumulado() {
        ChartStore cs = new GrupoGastoPagamentosChart(FacesContext.getCurrentInstance(), proprietario, locale);
        return cs.getCaminhoChar(new Object[]{gtValores()});
    }

    private List<TipoValores> gtValores() {
        List<AcumuladoInterface> acumulados = (List<AcumuladoInterface>) getGrupoDataModel().getWrappedData();
        List<TipoValores> listTipoValores = new ArrayList<TipoValores>();
        for (AcumuladoInterface ai : acumulados) {
            for (TipoValorInterface tv : ai.getTipoValorInterface()) {
                TipoValores tvs = new TipoValores();
                tvs.setTipo(tv.getTipo());
                tvs.addValor(tv.getValor());
                tvs.addAno(ai.getAno());
                tvs.addMes(ai.getMes());
                tvs.addComparar(true);
                if (listTipoValores.contains(tvs)) {
                    listTipoValores.get(listTipoValores.indexOf(tvs)).addValor(tv.getValor());
                    listTipoValores.get(listTipoValores.indexOf(tvs)).addAno(ai.getAno());
                    listTipoValores.get(listTipoValores.indexOf(tvs)).addMes(ai.getMes());
                    listTipoValores.get(listTipoValores.indexOf(tvs)).addComparar(true);
                } else {
                    listTipoValores.add(tvs);
                }
            }
        }
        List<GrupoGasto> listGrupoGasto = this.grupoFinanceiroBean.buscarGrupoGastoParaEdicao(proprietario);
        for (TipoValores tvs : listTipoValores) {
            for (int zz = 0; zz < 7; zz++) {
                if (zz > meses) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.MONTH, zz);
                    List<Object[]> grupoValores = this.relatoriosBean.buscarGastoPorGrupoGasto(c, proprietario);
                    if (grupoValores.isEmpty()) {
                        for (GrupoGasto gg : listGrupoGasto) {
                            TipoValores tv = new TipoValores();
                            tv.setTipo(gg.getGrupoGasto());
                            listTipoValores.get(listTipoValores.indexOf(tv)).addValor(0.00);
                            listTipoValores.get(listTipoValores.indexOf(tv)).addAno(c.get(Calendar.YEAR));
                            listTipoValores.get(listTipoValores.indexOf(tv)).addMes(c.get(Calendar.MONTH));
                            listTipoValores.get(listTipoValores.indexOf(tv)).addComparar(false);
                        }
                    } else {
                        for (Object[] obj : grupoValores) {
                            TipoValores tvsAux = new TipoValores();
                            tvsAux.setTipo((String) obj[0]);
                            listTipoValores.get(listTipoValores.indexOf(tvsAux)).addValor((Double) obj[1]);
                            listTipoValores.get(listTipoValores.indexOf(tvsAux)).addAno(c.get(Calendar.YEAR));
                            listTipoValores.get(listTipoValores.indexOf(tvsAux)).addMes(c.get(Calendar.MONTH));
                            listTipoValores.get(listTipoValores.indexOf(tvsAux)).addComparar(false);
                        }
                    }
                }
                if (zz > meses) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.MONTH, -zz);
                    List<Object[]> grupoValores = this.relatoriosBean.buscarGastoPorGrupoGasto(c, proprietario);
                    if (grupoValores.isEmpty()) {
                        for (GrupoGasto gg : listGrupoGasto) {
                            TipoValores tv = new TipoValores();
                            tv.setTipo(gg.getGrupoGasto());
                            listTipoValores.get(listTipoValores.indexOf(tv)).addValor(0.00);
                            listTipoValores.get(listTipoValores.indexOf(tv)).addAno(c.get(Calendar.YEAR));
                            listTipoValores.get(listTipoValores.indexOf(tv)).addMes(c.get(Calendar.MONTH));
                            listTipoValores.get(listTipoValores.indexOf(tv)).addComparar(false);
                        }
                    } else {
                        for (Object[] obj : grupoValores) {
                            TipoValores tvsAux = new TipoValores();
                            tvsAux.setTipo((String) obj[0]);
                            listTipoValores.get(listTipoValores.indexOf(tvsAux)).addValor((Double) obj[1]);
                            listTipoValores.get(listTipoValores.indexOf(tvsAux)).addAno(c.get(Calendar.YEAR));
                            listTipoValores.get(listTipoValores.indexOf(tvsAux)).addMes(c.get(Calendar.MONTH));
                            listTipoValores.get(listTipoValores.indexOf(tvsAux)).addComparar(false);
                        }
                    }
                }
            }
            break;
        }

        return listTipoValores;
    }

    public DataModel getGrupoDataModel() {
        if (this.grupoDataModel == null) {
            atualizaValoresGrupos(null);
        }
        return grupoDataModel;
    }

    public void setGrupoDataModel(DataModel grupoDataModel) {
        this.grupoDataModel = grupoDataModel;
    }

    public LinhasReportHelper getLinhasReportHelper() {
        if (this.linhasReportHelper == null) {
            this.linhasReportHelper = new LinhasReportHelper();
        }
        return linhasReportHelper;
    }

    public void setLinhasReportHelper(LinhasReportHelper linhasReportHelper) {
        this.linhasReportHelper = linhasReportHelper;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getMeses() {
        return meses;
    }

    public void setMeses(int meses) {
        this.meses = meses;
    }

    public Date getDataReferencia() {
        return dataReferencia;
    }

    public void setDataReferencia(Date dataReferencia) {
        this.dataReferencia = dataReferencia;
    }
}
