/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.vaidators;

import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.exceptions.ReceitaDividaException;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.Stateless;
/**
 *
 * @author Guilherme
 */
@Stateless(name = "receitaDividaValidador")
public class ReceitaDividaValidador implements ValidadorInterface<ReceitaDivida, ReceitaDividaBeanLocal>{

    @Override
    public void validar(ReceitaDivida entidade, ReceitaDividaBeanLocal bean, Object object) throws ValidacaoException {
        if (entidade == null) {
            lancarException("receitaDividaNula", "Débito/Crédito");
        }
        if(entidade.getValor() == null){
            lancarException("receitaDividaValor", "Valor");
        }
        
        if(entidade.getJuros() == null){
            lancarException("receitaDividaJuros", "Juros");
        }
        
        if(entidade.getDataVencimento() == null){
            lancarException("receitaDividaData", "Data Vencimento");
        }
        
        if(entidade.getParcelaAtual() == null){
            lancarException("receitaDividaParcelaAtual", "Parcela Atual");
        }
        
        if(entidade.getParcelaTotal() == null){
            lancarException("receitaDividaParcelaTotal", "Parcela Atual");
        }
        
        if(entidade.getStatusPagamento() == null){
            lancarException("receitaDividaStatusPagamento", "Parcela Atual");
        }
        
        if(entidade.getTipoMovimentacao() == null){
            lancarException("receitaDividaTipoPagamento", "Débito/Crédito");
        }
        
        if(entidade.getUsuario() == null){
            lancarException("receitaDividaUsuario", "Débito/Crédito");
        }
        
        if(entidade.getIdentificador() == null){
            lancarException("receitaDividaIdentificador", "Identificador Único");
        }
    }
    
        private void lancarException(String msg, String atributo) {
        ReceitaDividaException due = new ReceitaDividaException(msg, atributo);
        throw due;
    }
}
