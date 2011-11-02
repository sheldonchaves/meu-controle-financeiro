/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.enums.TipoConta;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.DetalheMovimentacao;
import br.com.money.modelos.Usuario;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.model.SelectItem;
import java.util.*;
import org.apache.commons.lang.StringUtils;
/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "selectItemManager", eager = true)
@NoneScoped
public class SelectItemManager {
    @EJB
    private ContaBancariaBeanLocal contaBancariaBean;
    @EJB
    private DetalheUsuarioBeanLocal detalheUsuarioBean;

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
            toReturn.add(new SelectItem(cb, StringUtils.substring(cb.getNomeConta(), 0, DetalheMovimentacaoManager.CARACTERES_DETALHE_MOVIMENTACAO_LIMIT)));
        }
        return toReturn;
    }
}
