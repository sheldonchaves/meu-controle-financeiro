/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.report;

import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ReportFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@RequestScoped
public class CartaoMensalReport {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(CartaoMensalReport.class);
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ReportFacade reportFacade;    
    @EJB
    private CartaoCreditoFacade cartaoCreditoFacade;
    //Grafico
    private CartesianChartModel categoryModel;
    private static final Integer PERIODO = 13;
    /**
     * Creates a new instance of CartaoMensalReport
     */
    public CartaoMensalReport() {
    }

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
        logger.info("end()...CartaoMensalReport");
    }
//====================
    //Métodos de Negócio  
    //====================

    private void atualizarBarrChart() {
        Date[] datas = periodoInformacao();
        categoryModel = new CartesianChartModel();
        //Busca todas as contas cartão de credito
        List<CartaoCredito> cartoes = cartaoCreditoFacade.buscarCartoesAtivos(usuarioFacade.getUsuario());
        List<Map<CartaoCredito, Double>> list = new ArrayList<Map<CartaoCredito, Double>>();
        for (Date date : datas) {//Busca todos os pagamentos de cartão por data do periodo
            list.add(reportFacade.acumuladoCartaoPeriodo(date, usuarioFacade.getUsuario()));
        }
        for(CartaoCredito cc : cartoes){//Percorre todas as contas cartão de credito
            ChartSeries serie = new ChartSeries(cc.getLabel());//Cria uma serie(linha) para inserir no gráfico por cartao ou conta
            int dataPos = 0;//Contador para a data
            for(Map<CartaoCredito, Double> map : list){
                Double temp = map.get(cc);//Procura a conta bancaria no map
                if(temp == null){
                    temp = 0d;//Se não encontrar define valor 0;
                }
                serie.set(DateUtils.getDataFormatada(datas[dataPos++],"MM/yyyy"), temp);//Adiciona o periodo com o valor na linha
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
    //Getters AND Setters
    //====================
    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CartesianChartModel categoryModel) {
        this.categoryModel = categoryModel;
    }
}
