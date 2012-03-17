/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager.reportManager;

import br.com.gbvbahia.money.manager.LoginManager;
import br.com.gbvbahia.money.manager.SelectItemManager;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.business.interfaces.ReportBeanLocal;
import br.com.money.enums.TipoConta;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.modelos.ContaBancaria;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "cartaoMensalManager")
@RequestScoped
public class CartaoMensalManager {
    @EJB
    private ContaBancariaBeanLocal contaBancariaBean;

    private static final Integer PERIODO = 13;
    @EJB
    private ReportBeanLocal reportBean;
    
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    private CartesianChartModel categoryModel;

    public CartaoMensalManager() {
    }

    //====================
    // Iniciadores        
    //====================
    @PostConstruct
    public void init() {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "CartaoMensalManager.end() executado!");
        atualizarBarrChart();
    }

    @PreDestroy
    public void end() {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "CartaoMensalManager.init() executado!");
    }
    //====================
    //Métodos de Negócio  
    //====================

    private void atualizarBarrChart() {
        Date[] datas = periodoInformacao();
        categoryModel = new CartesianChartModel();
        //Busca todas as contas cartão de credito
        List<ContaBancaria> contas = contaBancariaBean.buscarContaBancariasPorUsuarioTipo(loginManager.getUsuario(), TipoConta.CARTAO_DE_CREDITO);
        List<Map<ContaBancaria, Double>> list = new ArrayList<Map<ContaBancaria, Double>>();
        for (Date date : datas) {//Busca todos os pagamentos de cartão por data do periodo
            list.add(this.reportBean.acumuladoTipoContaPeriodo(date, loginManager.getUsuario(), TipoConta.CARTAO_DE_CREDITO, TipoMovimentacao.RETIRADA));
        }
        for(ContaBancaria cc : contas){//Percorre todas as contas cartão de credito
            ChartSeries serie = new ChartSeries(cc.getNomeLimitado());//Cria uma serie(linha) para inserir no gráfico por cartao ou conta
            int dataPos = 0;//Contador para a data
            for(Map<ContaBancaria, Double> map : list){
                Double temp = map.get(cc);//Procura a conta bancaria no map
                if(temp == null){
                    temp = 0d;//Se não encontrar define valor 0;
                }
                serie.set(UtilMetodos.getDataStringMesAno(datas[dataPos++]), temp);//Adiciona o periodo com o valor na linha
            }
            categoryModel.addSeries(serie);//Adiciona a linha no gráfico
        }
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
    public Locale getLocale() {
        return SelectItemManager.BRASIL;
    }

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
