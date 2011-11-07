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
public class LazyMovimentacaoFinanceiraModel extends LazyDataModel<MovimentacaoFinanceira> {

    private MovimentacaoFinanceiraBeanLocal bean;
    private Usuario usuario;

    public LazyMovimentacaoFinanceiraModel(MovimentacaoFinanceiraBeanLocal bean, Usuario usuario) {
        this.bean = bean;
        this.usuario = usuario;
    }

    @Override
    public List<MovimentacaoFinanceira> load(int first, int tamanho, String string, SortOrder so, Map<String, String> map) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Lazy Load LazyMovimentacaoFinanceiraModel INICIO");
        List<MovimentacaoFinanceira> toReturn = null;
        toReturn = bean.buscarMovimentacaoPorUsuarioStatusPaginada(first, tamanho, usuario);
        setRowCount(bean.buscarQtdadeMovimentacaoPorUsuarioStatusPaginada(usuario));
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Lazy Load LazyMovimentacaoFinanceiraModel FIM");
        return toReturn;
    }
}
