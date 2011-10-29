/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.vaidators;

import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.exceptions.DetalheMovimentacaoException;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.DetalheMovimentacao;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
@Stateless(name = "detalheMobvimentacaoValidador")
public class DetalheMobvimentacaoValidador implements ValidadorInterface<DetalheMovimentacao, DetalheUsuarioBeanLocal> {

    @Override
    public void validar(DetalheMovimentacao entidade, DetalheUsuarioBeanLocal bean, Object object) throws ValidacaoException {
        if (entidade == null) {
            lancarException("detalheMovimentacaoNulo", "Detalhe Movimentação");
        }

        if (StringUtils.isBlank(entidade.getDetalhe())) {
            lancarException("detalheMovimentacaoDetalheNulo", "Detalhe Movimentação");
        }

        if (entidade.getDetalhe().length() > DetalheMovimentacao.QUANTIDADE_CARACTERES_DETALHE) {
            lancarException("detalheMovimentacaoDetalheLong", "Detalhe Movimentação");
        }

        if(entidade.getUsuarioProprietario() == null || entidade.getUsuarioProprietario().getId() == null){
            lancarException("detalheMovimentacaoUsuarioNull", "Usuário Proprietário");
        }
        
        DetalheMovimentacao detalhe = bean.buscarDetalheMovimentacaoPorDetalheUsuario(entidade.getDetalhe(), entidade.getUsuarioProprietario());
        if (entidade.getId() == null && detalhe != null) {
            lancarException("detalheMovimentacaoDetalheExiste", "Detalhe Movimentação");
        }
        if (detalhe != null && entidade.getId() != null && !entidade.getId().equals(detalhe.getId())
                && entidade.getDetalhe().equalsIgnoreCase(detalhe.getDetalhe())) {
            lancarException("detalheMovimentacaoDetalheExiste", "Detalhe Movimentação");
        }
    }

    private void lancarException(String msg, String atributo) {
        DetalheMovimentacaoException due = new DetalheMovimentacaoException(msg, atributo);
        throw due;
    }
}
