/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager.lazyTables;

import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
import br.com.money.enums.TipoConta;
import br.com.money.modelos.MovimentacaoFinanceira;
import br.com.money.modelos.Usuario;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Guilherme
 */
public class LazyMovimentacaoTipoContaModel extends LazyDataModel<MovimentacaoFinanceira> {

    private MovimentacaoFinanceiraBeanLocal bean;
    private Usuario usuario;
    private TipoConta tipoConta;
    
    public LazyMovimentacaoTipoContaModel(MovimentacaoFinanceiraBeanLocal bean, Usuario usuario, TipoConta tipoConta) {
        this.bean = bean;
        this.usuario = usuario;
        this.tipoConta = tipoConta;
    }
    
    
    @Override
    public List<MovimentacaoFinanceira> load(int first, int tamanho, String string, SortOrder so, Map<String, String> map) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Lazy Load LazyMovimentacaoTipoContaModel INICIO");
        List<MovimentacaoFinanceira> toReturn = null;
        toReturn = bean.buscarMovimentacaoPorUsuarioContaPaginada(first, tamanho, usuario, tipoConta);
        setRowCount(bean.buscarQtdadeMovimentacaoPorUsuarioContaPaginada(usuario, tipoConta));
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Lazy Load LazyMovimentacaoTipoContaModel FIM");
        return toReturn;
    }
}
