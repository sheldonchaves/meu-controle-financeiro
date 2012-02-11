/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.enums.TipoConta;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.DetalheMovimentacao;
import br.com.money.modelos.Usuario;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.model.SelectItem;
import java.util.*;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "selectItemManager", eager = true)
@NoneScoped
public class SelectItemManager implements Serializable{
    @EJB
    private ContaBancariaBeanLocal contaBancariaBean;
    @EJB
    private DetalheUsuarioBeanLocal detalheUsuarioBean;

    public static final String PATTERN = "dd/MM/yyyy";
    public static final Locale BRASIL = new Locale("pt", "BR");
    
    /** Creates a new instance of SelectItemManager */
    public SelectItemManager() {
    }
    
        /**
     * Converte a enum TipoConta em uma lista de SelectItems
     * @return List de SelectItem
     */
    public List<SelectItem> getLinguagens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (TipoConta lin : TipoConta.values()) {
            toReturn.add(new SelectItem(lin, lin.getTipoContaString()));
        }
        return toReturn;
    }
    
            /**
     * Converte a enum Linguagem em uma lista de SelectItems
     * @return List de SelectItem
     */
    public List<SelectItem> getTipoMovimentacao() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (TipoMovimentacao lin : TipoMovimentacao.values()) {
            toReturn.add(new SelectItem(lin, lin.getTipoMovimentacaoString()));
        }
        return toReturn;
    }
    
    /**
     * Retorna todos os detalhes do usuário com status passado para seleção em um selectitem
     * @param usuario
     * @param ativa 
     * @return 
     */
    public List<SelectItem> getDetalhesUsuario(Usuario usuario, boolean ativa, TipoMovimentacao tipoMovimentacao){
        List<DetalheMovimentacao> detalhes = detalheUsuarioBean.buscarDetalheMovimentacaoPorUsuarioFlagTipoMovimentacao(usuario, ativa, tipoMovimentacao);
        Collections.sort(detalhes);
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for(DetalheMovimentacao dm : detalhes){
            toReturn.add(new SelectItem(dm, StringUtils.substring(dm.getDetalhe(), 0, DetalheMovimentacaoManager.CARACTERES_DETALHE_MOVIMENTACAO_LIMIT)));
        }
        return toReturn;
    }
    
    public List<SelectItem> getContaBancaria(Usuario usuario){
        List<ContaBancaria> contas = this.contaBancariaBean.buscarContaBancariasPorUsuario(usuario);
        Collections.sort(contas);
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for(ContaBancaria cb : contas){
            toReturn.add(new SelectItem(cb, StringUtils.substring(cb.getLabel(), 0, DetalheMovimentacaoManager.CARACTERES_DETALHE_MOVIMENTACAO_LIMIT)));
        }
        return toReturn;
    }
    
    /**
     * Deve ser utilizado somente em filtros de tabela, pos 
     * a conta bancaria não é inserida, somente sua label.
     * @param usuario
     * @return 
     */
    public List<SelectItem> getContaBancariaToTable(Usuario usuario, FacesContext fc){
        List<ContaBancaria> contas = this.contaBancariaBean.buscarContaBancariasPorUsuario(usuario);
        Collections.sort(contas);
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        toReturn.add(new SelectItem(null, UtilMetodos.getResourceBundle("selecione", fc)));
        for(ContaBancaria cb : contas){
            toReturn.add(new SelectItem(cb.getId(), StringUtils.substring(cb.getLabel(), 0, DetalheMovimentacaoManager.CARACTERES_DETALHE_MOVIMENTACAO_LIMIT)));
        }
        return toReturn;
    }
    
    public List<SelectItem> getContaBancaria(Usuario usuario, TipoConta tipo){
        List<ContaBancaria> contas = this.contaBancariaBean.buscarContaBancariasPorUsuarioTipo(usuario, tipo);
        Collections.sort(contas);
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for(ContaBancaria cb : contas){
            toReturn.add(new SelectItem(cb, StringUtils.substring(cb.getLabel(), 0, DetalheMovimentacaoManager.CARACTERES_DETALHE_MOVIMENTACAO_LIMIT)));
        }
        return toReturn;
    }
//    
//     private Map<String, String> themes;  
//    @PostConstruct  
//    public void init() {  
//          
//        themes = new TreeMap<String, String>();  
//        themes.put("Aristo", "aristo");  
//        themes.put("Black-Tie", "black-tie");  
//        themes.put("Blitzer", "blitzer");  
//        themes.put("Bluesky", "bluesky");  
//        themes.put("Casablanca", "casablanca");  
//        themes.put("Cupertino", "cupertino");  
//        themes.put("Dark-Hive", "dark-hive");  
//        themes.put("Dot-Luv", "dot-luv");  
//        themes.put("Eggplant", "eggplant");  
//        themes.put("Excite-Bike", "excite-bike");  
//        themes.put("Flick", "flick");  
//        themes.put("Glass-X", "glass-x");  
//        themes.put("Hot-Sneaks", "hot-sneaks");  
//        themes.put("Humanity", "humanity");  
//        themes.put("Le-Frog", "le-frog");  
//        themes.put("Midnight", "midnight");  
//        themes.put("Mint-Choc", "mint-choc");  
//        themes.put("Overcast", "overcast");  
//        themes.put("Pepper-Grinder", "pepper-grinder");  
//        themes.put("Redmond", "redmond");  
//        themes.put("Rocket", "rocket");  
//        themes.put("Sam", "sam");  
//        themes.put("Smoothness", "smoothness");  
//        themes.put("South-Street", "south-street");  
//        themes.put("Start", "start");  
//        themes.put("Sunny", "sunny");  
//        themes.put("Swanky-Purse", "swanky-purse");  
//        themes.put("Trontastic", "trontastic");  
//        themes.put("UI-Darkness", "ui-darkness");  
//        themes.put("UI-Lightness", "ui-lightness");  
//        themes.put("Vader", "vader");  
//    }  
//    
//    public Map<String, String> getThemes() {  
//        return themes;  
//    } 
}
