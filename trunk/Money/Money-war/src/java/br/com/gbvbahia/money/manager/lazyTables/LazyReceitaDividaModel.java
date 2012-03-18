/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager.lazyTables;

import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.modelos.Usuario;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guilherme
 */
public class LazyReceitaDividaModel extends LazyDataModel<ReceitaDivida> {

    private ReceitaDividaBeanLocal bean;
    private Usuario usuario;
    private StatusPagamento statusPagamento;
    private TipoMovimentacao tipoMovimentacao;

    public LazyReceitaDividaModel(ReceitaDividaBeanLocal bean, Usuario usuario, StatusPagamento statusPagamento, TipoMovimentacao tipoMovimentacao) {
        this.bean = bean;
        this.usuario = usuario;
        this.statusPagamento = statusPagamento;
        this.tipoMovimentacao = tipoMovimentacao;
    }

    @Override
    public List<ReceitaDivida> load(int first, int tamanho, String ordernarPorCampo, SortOrder so, Map<String, String> filtros) {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Lazy Load LazyReceitaDividaModel INICIO");
        List<ReceitaDivida> toReturn = null;
        if (tipoMovimentacao == null) {
            toReturn = bean.buscarReceitaDividasPorUsuarioStatusPaginada(first, tamanho, usuario, statusPagamento);
            this.setRowCount(bean.buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(usuario, statusPagamento));
        } else {
            toReturn = bean.buscarReceitaDividasPorUsuarioStatusPaginada(
                    first, tamanho, usuario, statusPagamento, tipoMovimentacao, null, null);
            this.setRowCount(bean.buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(
                    usuario, statusPagamento, tipoMovimentacao, null, null));
        }
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Lazy Load LazyReceitaDividaModel FIM");
        return toReturn;
    }


}
