/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.vaidators;

import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.exceptions.DetalheUsuarioException;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.embeddedId.DetalheUsuario;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.Stateless;

/**
 *
 * @author gbvbahia
 */
@Stateless(name = "detalheUsuarioValidador")
public class DetalheUsuarioValidador implements ValidadorInterface<DetalheUsuario, DetalheUsuarioBeanLocal> {

    @Override
    public void validar(DetalheUsuario entidade, DetalheUsuarioBeanLocal bean, Object object) throws ValidacaoException {
        if(entidade == null){
            lancarException(("detalheUsuarioNulo"), "Detalhe Usuário");
        }
        if(entidade.getDetalheMovimentacao() == null){
            lancarException(("detalheMovimentacaoNulo"), "Detalhe Movimentação");
        }else if(entidade.getDetalheMovimentacao().getId() == null){
            lancarException(("detalheMovimentacaoIdNulo"), "Detalhe Movimentação");
        }
        
        if(entidade.getUsuario() == null){
            lancarException(("usuarioNulo"), "Usuário");
        }else if(entidade.getUsuario().getId() == null){
            lancarException(("usuarioIdNulo"), "Usuário");
        }
        if(bean.buscarDetalheUsuarioPorDetalheMovimentacaoUsuario(entidade.getDetalheMovimentacao(), entidade.getUsuario()) != null){
            lancarException(("detalheMovimentacaoUsuarioExistente"), "Detalhe Movimentação, Usuário");
        }
    }

    private void lancarException(String msg, String atributo) {
        DetalheUsuarioException due = new DetalheUsuarioException(msg, atributo);
        throw due;
    }
}
