/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.enums.TipoConta;
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
    private DetalheUsuarioBeanLocal detalheUsuarioBean;

    /** Creates a new instance of SelectItemManager */
    public SelectItemManager() {
    }
    
        /**
     * Converte a enum Linguagem em uma lista de SelectItems
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
     * Retorna todos os detalhes do usuário com status passado para seleção em um selectitem
     * @param usuario
     * @param ativa 
     * @return 
     */
    public List<SelectItem> getDetalhesUsuario(Usuario usuario, boolean ativa){
        List<DetalheMovimentacao> detalhes = detalheUsuarioBean.buscarDetalheMovimentacaoPorUsuarioFlag(usuario, ativa);
        Collections.sort(detalhes);
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for(DetalheMovimentacao dm : detalhes){
            toReturn.add(new SelectItem(dm, StringUtils.substring(dm.getDetalhe(), 0, DetalheMovimentacaoManager.CARACTERES_DETALHE_MOVIMENTACAO_LIMIT)));
        }
        return toReturn;
    }
}