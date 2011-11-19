/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager.lazyTables;

import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
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
public class LazyTransferenciaEntreContas extends LazyDataModel<MovimentacaoFinanceira>{

    private MovimentacaoFinanceiraBeanLocal bean;
    private Usuario usuario;

    public LazyTransferenciaEntreContas(MovimentacaoFinanceiraBeanLocal bean, Usuario usuario) {
        this.bean = bean;
        this.usuario = usuario;
    }
    
    @Override
    public List<MovimentacaoFinanceira> load(int i, int i1, String string, SortOrder so, Map<String, String> map) {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Lazy Load LazyTransferenciaEntreContas INICIO");
        List<MovimentacaoFinanceira> toReturn = null;
        toReturn = bean.buscarTodasTransferenciasEntreContasPaginada(i, i1, usuario);
        setRowCount(bean.buscarQtdadeTodasTransferenciasEntreContasPaginada(usuario));
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Lazy Load LazyTransferenciaEntreContas FIM");
        return toReturn;
    }
    
    
}
